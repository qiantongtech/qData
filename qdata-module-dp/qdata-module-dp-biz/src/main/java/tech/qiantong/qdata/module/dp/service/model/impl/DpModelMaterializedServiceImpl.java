/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.dp.service.model.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetReqDTO;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetRespDTO;
import tech.qiantong.qdata.module.da.api.service.asset.IDaAssetApiOutService;
import tech.qiantong.qdata.module.dp.api.enums.DpModelReleaseMode;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.*;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelColumnDO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelDO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelMaterializedDO;
import tech.qiantong.qdata.module.dp.dal.mapper.model.DpModelMaterializedMapper;
import tech.qiantong.qdata.module.dp.service.model.IDpModelColumnService;
import tech.qiantong.qdata.module.dp.service.model.IDpModelMaterializedService;
import tech.qiantong.qdata.module.dp.service.model.IDpModelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Materialized Model Record Service Business Layer Processing
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpModelMaterializedServiceImpl extends ServiceImpl<DpModelMaterializedMapper, DpModelMaterializedDO> implements IDpModelMaterializedService {
    @Resource
    private DpModelMaterializedMapper dpModelMaterializedMapper;
    @Resource
    private IDpModelColumnService dpModelColumnService;
    @Resource
    private IDpModelService dpModelService;
    @Resource
    private IDaAssetApiOutService iDaAssetApiService;

    @Autowired
    private DataSourceFactory dataSourceFactory;

    /**
     * Materialized table creation
     *
     * @param dpModelMaterialized
     * @return
     */
    @Override
    public Long createMaterializedTable(DpMaterializedMethodReqVO dpModelMaterialized) {
        // Get model IDs
        List<Long> modelIdList = dpModelMaterialized.getModelId();
        if (CollectionUtils.isEmpty(modelIdList)) {
            throw new RuntimeException(MessageUtils.messageWithFallback(
                    "dp.error.model.release.info.empty",
                    "Failed to get information because the release information is empty"));
        }

        DbQueryProperty dbQueryProperty = new DbQueryProperty(dpModelMaterialized.getDatasourceType(), dpModelMaterialized.getIp(), dpModelMaterialized.getPort(), dpModelMaterialized.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        // Test connection
        if (!dbQuery.valid()) {
            throw new RuntimeException(MessageUtils.messageWithFallback(
                    "dp.error.database.connection.fail", "Database connection failed"));
        }

        for (Long modelId : modelIdList) {

            DpModelMaterializedDO dpModelMaterializedDO = this.anotherAsyncTaskSingle(modelId, dpModelMaterialized, dbQuery, dbQueryProperty);

            dpModelMaterializedDO.setCreatorId(dpModelMaterialized.getCreatorId());
            dpModelMaterializedDO.setCreateBy(dpModelMaterialized.getCreateBy());
            dpModelMaterializedDO.setCreateTime(dpModelMaterializedDO.getCreateTime());

            String status = dpModelMaterializedDO.getStatus();
            if (StringUtils.equals("3", status)) {
                // Asset
                DaAssetReqDTO daAssetReqDTO = new DaAssetReqDTO();
                daAssetReqDTO.setSource("2");
                daAssetReqDTO.setModelId(modelId);
                daAssetReqDTO.setDatasourceId(dpModelMaterialized.getDatasourceId());
                daAssetReqDTO.setFieldCount(dpModelMaterializedDO.getFieldCount());
                DaAssetRespDTO daAssetRespDTO = iDaAssetApiService.insertDaAsset(daAssetReqDTO);
                Long id = daAssetRespDTO.getId();// Asset ID
                dpModelMaterializedDO.setAssetId(id);
            }
            dpModelMaterializedMapper.insert(dpModelMaterializedDO);
        }
        dbQuery.close();

        return 1L;
    }

    /**
     * @param modelId
     * @param dpModelMaterialized
     * @param dbQuery
     * @param dbQueryProperty
     */
    private DpModelMaterializedDO anotherAsyncTaskSingle(Long modelId, DpMaterializedMethodReqVO dpModelMaterialized, DbQuery dbQuery, DbQueryProperty dbQueryProperty) {
        DpModelReleaseMode releaseMode = DpModelReleaseMode.getByCode(dpModelMaterialized.getReleaseMode());
        // First create a log object with basic info; return it even if an exception occurs
        DpModelMaterializedDO dpModelMaterializedDO = buildLogRecord(modelId, dpModelMaterialized);

        // Default status: 2=Creating
        dpModelMaterializedDO.setStatus("2");
        // Before inserting into DB, set CreateTime/CreateBy/UpdateTime etc. as needed
        try {
            // 1. Query model/columns, throw exception if validation fails
            DpModelDO dpModelDO = checkAndGetModel(modelId);
            dpModelMaterializedDO.setModelName(dpModelDO.getModelName());
            dpModelMaterializedDO.setModelAlias(dpModelDO.getModelComment());


            List<DpModelColumnDO> columnList = checkAndGetModelColumns(modelId);
            // Set field count
            dpModelMaterializedDO.setFieldCount(Long.valueOf(columnList.size()));


            String tableName = dpModelDO.getTableName();
            int tableStatus = dbQuery.generateCheckTableExistsSQL(dbQueryProperty, tableName);

            // Is creating table true: create directly false: needs field update
            Boolean createTable = true;
            if (tableStatus > 0) {
                // Check if delete and rebuild
                if (StringUtils.equals(DpModelReleaseMode.DELETE_REBUILD.getCode(), releaseMode.getCode())) {
                    dbQuery.deleteTable(dbQueryProperty, tableName);
                } else if (StringUtils.equals(DpModelReleaseMode.INCREMENT_RELEASE.getCode(), releaseMode.getCode())) {
                    createTable = false;
                }
            }
            int creatInt;
            List<DbColumn> dbColumns = this.setColumnsListFromDpModelColumns(columnList);
            if (createTable) {
                List<String> tableSQLList = dbQuery.generateCreateTableSQL(dbQueryProperty, tableName, dpModelDO.getModelComment(), dbColumns);
                if (CollectionUtils.isNotEmpty(tableSQLList)) {
                    dpModelMaterializedDO.setSqlCommand(tableSQLList.toString()); // Record the executed SQL
                }

                creatInt = dbQuery.createCollectionWithSchema(dbQueryProperty, tableName, dpModelDO.getModelComment(), dbColumns);
            } else {
                List<String> updateTableSQLList = dbQuery.generateUpdateTableSQL(dbQueryProperty, tableName, dpModelDO.getModelComment(), dbColumns);
                if (CollectionUtils.isNotEmpty(updateTableSQLList)) {
                    dpModelMaterializedDO.setSqlCommand(updateTableSQLList.toString()); // Record the executed SQL
                }
                if (updateTableSQLList.size() == 0) {
                    creatInt = 1;
                } else {
                    creatInt = 0;
                    for (String sql : updateTableSQLList) {
                        dbQuery.execute(sql);
                        creatInt++;
                    }
                }
            }
            if (creatInt > 0) {
                // On success -> status=3
                dpModelMaterializedDO.setStatus("3");
                dpModelMaterializedDO.setMessage("Table created successfully");
            } else {
                // Log any exception
                dpModelMaterializedDO.setStatus("4"); // 4=Failed
                dpModelMaterializedDO.setMessage("Table creation failed: Please contact administrator");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // Log any exception
            dpModelMaterializedDO.setStatus("4"); // 4=Failed
            dpModelMaterializedDO.setMessage("Table creation failed: " + ex.getMessage());
        }

        return dpModelMaterializedDO;
    }


    /**
     * Convert DpModelColumnDO to DbColumn and assign to columnsList
     *
     * @param columnList DpModelColumnDO list
     */
    public List<DbColumn> setColumnsListFromDpModelColumns(List<DpModelColumnDO> columnList) {
        return columnList.stream()
                .map(dpColumn -> DbColumn.builder()
                        .colName(dpColumn.getEngName())
                        .dataType(dpColumn.getColumnType())
                        .dataLength(dpColumn.getColumnLength() != null ? dpColumn.getColumnLength().toString() : null)
                        .dataScale(dpColumn.getColumnScale() != null ? dpColumn.getColumnScale().toString() : null)
                        .colKey("1".equals(dpColumn.getPkFlag()))
                        .nullable("0".equals(dpColumn.getNullableFlag()))
                        .colPosition(dpColumn.getSortOrder() == null ? 1 : dpColumn.getSortOrder().intValue())
                        .dataDefault(dpColumn.getDefaultValue())
                        .colComment(dpColumn.getCnName())  // Or fill with other fields
                        .build())
                .collect(Collectors.toList());
    }


    /**
     * Build a log record object with basic fields
     *
     * @param modelId
     * @param dpModelMaterialized
     * @return
     */
    private DpModelMaterializedDO buildLogRecord(Long modelId, DpMaterializedMethodReqVO dpModelMaterialized) {
        DpModelMaterializedDO logRecord = new DpModelMaterializedDO();
        logRecord.setModelId(modelId);
        logRecord.setDatasourceId(dpModelMaterialized.getDatasourceId().toString());
        logRecord.setDatasourceType(dpModelMaterialized.getDatasourceType());
        logRecord.setDatasourceName(dpModelMaterialized.getDatasourceName());
        logRecord.setValidFlag(true);
        logRecord.setDelFlag(false);
        logRecord.setRemark(dpModelMaterialized.getRemark());
        return logRecord;
    }

    /**
     * Query and validate DpModelDO, throw exception if not exists
     */
    private DpModelDO checkAndGetModel(Long modelId) {
        DpModelDO dpModelDO = dpModelService.getDpModelById(modelId);
        if (dpModelDO == null) {
            throw new RuntimeException("Logical model does not exist, modelId=" + modelId);
        }
        return dpModelDO;
    }


    /**
     * Query and validate column list, throw exception if not exists
     */
    private List<DpModelColumnDO> checkAndGetModelColumns(Long modelId) {
        DpModelColumnSaveReqVO reqVO = new DpModelColumnSaveReqVO();
        reqVO.setModelId(modelId);
        List<DpModelColumnDO> columnList = dpModelColumnService.getDpModelColumnList(reqVO);
        if (CollectionUtils.isEmpty(columnList)) {
            throw new RuntimeException("Logical model has no columns, cannot create table, modelId=" + modelId);
        }
        return columnList;
    }


    @Override
    public PageResult<DpModelMaterializedDO> getDpModelMaterializedPage(DpModelMaterializedPageReqVO pageReqVO) {
        return dpModelMaterializedMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDpModelMaterialized(DpModelMaterializedSaveReqVO createReqVO) {
        DpModelMaterializedDO dictType = BeanUtils.toBean(createReqVO, DpModelMaterializedDO.class);
        dpModelMaterializedMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpModelMaterialized(DpModelMaterializedSaveReqVO updateReqVO) {
        // Related validation

        // Update materialized model record
        DpModelMaterializedDO updateObj = BeanUtils.toBean(updateReqVO, DpModelMaterializedDO.class);
        return dpModelMaterializedMapper.updateById(updateObj);
    }

    @Override
    public int removeDpModelMaterialized(Collection<Long> idList) {
        // Batch delete materialized model record
        return dpModelMaterializedMapper.deleteBatchIds(idList);
    }

    @Override
    public DpModelMaterializedDO getDpModelMaterializedById(Long id) {
        return dpModelMaterializedMapper.selectById(id);
    }

    @Override
    public List<DpModelMaterializedDO> getDpModelMaterializedList() {
        return dpModelMaterializedMapper.selectList();
    }

    @Override
    public Map<Long, DpModelMaterializedDO> getDpModelMaterializedMap() {
        List<DpModelMaterializedDO> dpModelMaterializedList = dpModelMaterializedMapper.selectList();
        return dpModelMaterializedList.stream()
                .collect(Collectors.toMap(DpModelMaterializedDO::getId, dpModelMaterializedDO -> dpModelMaterializedDO,
                        // Keep existing value
                        (existing, replacement) -> existing));
    }


    /**
     * Import materialized model record data
     *
     * @param importExcelList Materialized model record data list
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName        Operator
     * @return Result
     */
    @Override
    public String importDpModelMaterialized(List<DpModelMaterializedRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dp.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DpModelMaterializedRespVO respVO : importExcelList) {
            try {
                DpModelMaterializedDO dpModelMaterializedDO = BeanUtils.toBean(respVO, DpModelMaterializedDO.class);
                Long dpModelMaterializedId = respVO.getId();
                if (isUpdateSupport) {
                    if (dpModelMaterializedId != null) {
                        DpModelMaterializedDO existingDpModelMaterialized = dpModelMaterializedMapper.selectById(dpModelMaterializedId);
                        if (existingDpModelMaterialized != null) {
                            dpModelMaterializedMapper.updateById(dpModelMaterializedDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dp.import.update.success",
                                    "Data update successful, ID {0} {1} record.", dpModelMaterializedId, MessageUtils.messageWithFallback("dp.entity.materialized.model", "Materialized model record")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", dpModelMaterializedId, MessageUtils.messageWithFallback("dp.entity.materialized.model", "Materialized model record")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DpModelMaterializedDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dpModelMaterializedId);
                    DpModelMaterializedDO existingDpModelMaterialized = dpModelMaterializedMapper.selectOne(queryWrapper);
                    if (existingDpModelMaterialized == null) {
                        dpModelMaterializedMapper.insert(dpModelMaterializedDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dp.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", dpModelMaterializedId, MessageUtils.messageWithFallback("dp.entity.materialized.model", "Materialized model record")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dp.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", dpModelMaterializedId, MessageUtils.messageWithFallback("dp.entity.materialized.model", "Materialized model record")));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("dp.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("dp.import.result.fail",
                    "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                    failureNum, failureDetails));
            throw new ServiceException("dp.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("dp.import.result.success",
                    "Congratulations! All data imported! Total: {0} records.", successNum));
        }
        return resultMsg.toString();
    }

}
