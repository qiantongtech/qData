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

package tech.qiantong.qdata.quality.service.datasource.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.quality.controller.da.datasource.vo.DaDatasourcePageReqVO;
import tech.qiantong.qdata.quality.controller.da.datasource.vo.DaDatasourceRespVO;
import tech.qiantong.qdata.quality.controller.da.datasource.vo.DaDatasourceSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.quality.dal.mapper.datasource.DaDatasourceMapper;
import tech.qiantong.qdata.quality.service.datasource.IDaDatasourceQualityService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data source Service business layer processing
 *
 * @author lhs
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaDatasourceQualityServiceImpl extends ServiceImpl<DaDatasourceMapper, DaDatasourceDO> implements IDaDatasourceQualityService {
    @Resource
    private DaDatasourceMapper daDatasourceMapper;

    @Autowired
    private DataSourceFactory dataSourceFactory;

    /**
     * Query the data source connection information of data assets
     *
     * @param daAsset
     * @return
     */
    @Override
    public List<DaDatasourceDO> getDataSourceByAsset(DaDatasourceRespVO daAsset) {
        return daDatasourceMapper.selectList();
    }

    @Override
    public PageResult<DaDatasourceDO> getDaDatasourcePage(DaDatasourcePageReqVO pageReqVO) {
        return daDatasourceMapper.selectPage(pageReqVO);
    }


    @Override
    public List<DaDatasourceDO> getDaDatasourceList(DaDatasourcePageReqVO reqVO) {
        LambdaQueryWrapperX<DaDatasourceDO> daDatasourceDOLambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        daDatasourceDOLambdaQueryWrapperX.likeIfPresent(DaDatasourceDO::getDatasourceName, reqVO.getDatasourceName())
                .like(StringUtils.isNotEmpty(reqVO.getDatasourceType()), DaDatasourceDO::getDatasourceType, reqVO.getDatasourceType())
                .eq(StringUtils.isNotEmpty(reqVO.getDatasourceConfig()), DaDatasourceDO::getDatasourceConfig, reqVO.getDatasourceConfig())
                .eq(StringUtils.isNotEmpty(reqVO.getIp()), DaDatasourceDO::getIp, reqVO.getIp());

        return daDatasourceMapper.selectList(daDatasourceDOLambdaQueryWrapperX);
    }

    @Override
    public Long createDaDatasource(DaDatasourceSaveReqVO createReqVO) {
        DaDatasourceDO dictType = BeanUtils.toBean(createReqVO, DaDatasourceDO.class);
        daDatasourceMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int removeDaDatasource(Collection<Long> idList) {
        // Delete data sources in batches
        return daDatasourceMapper.deleteBatchIds(idList);
    }

    @Override
    public DaDatasourceRespDTO getDatasourceById(Long id) {
        DaDatasourceRespDTO dto = new DaDatasourceRespDTO();
        DaDatasourceDO daDatasourceDO = daDatasourceMapper.selectById(id);
        org.springframework.beans.BeanUtils.copyProperties(daDatasourceDO, dto);
        return dto;
    }

    @Override
    public DaDatasourceDO getDaDatasourceById(Long id) {
        DaDatasourceDO daDatasourceDO = daDatasourceMapper.selectById(id);
        if (daDatasourceDO == null) {
            return null;
        }
        return daDatasourceDO;
    }

    @Override
    public DaDatasourceRespVO getDaDatasourceByIdSimple(Long id) {
        return BeanUtils.toBean(daDatasourceMapper.selectById(id), DaDatasourceRespVO.class);
    }

    @Override
    public List<DaDatasourceDO> getDaDatasourceList() {
        return daDatasourceMapper.selectList();
    }

    @Override
    public Map<Long, DaDatasourceDO> getDaDatasourceMap() {
        List<DaDatasourceDO> daDatasourceList = daDatasourceMapper.selectList();
        return daDatasourceList.stream()
                .collect(Collectors.toMap(
                        DaDatasourceDO::getId,
                        daDatasourceDO -> daDatasourceDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data source data
     *
     * @param importExcelList data source data list
     * @param isUpdateSupport Whether to update support, if it already exists, update the data
     * @param operName operating user
     * @return result
     */
    @Override
    public String importDaDatasource(List<DaDatasourceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("quality.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaDatasourceRespVO respVO : importExcelList) {
            try {
                DaDatasourceDO daDatasourceDO = BeanUtils.toBean(respVO, DaDatasourceDO.class);
                Long daDatasourceId = respVO.getId();
                if (isUpdateSupport) {
                    if (daDatasourceId != null) {
                        DaDatasourceDO existingDaDatasource = daDatasourceMapper.selectById(daDatasourceId);
                        if (existingDaDatasource != null) {
                            daDatasourceMapper.updateById(daDatasourceDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("quality.import.update.success",
                                    "数据更新成功，ID为 " + daDatasourceId + " 的数据源记录。", daDatasourceId, "数据源"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.fail",
                                    "数据更新失败，ID为 " + daDatasourceId + " 的数据源记录不存在。", daDatasourceId, "数据源"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DaDatasourceDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daDatasourceId);
                    DaDatasourceDO existingDaDatasource = daDatasourceMapper.selectOne(queryWrapper);
                    if (existingDaDatasource == null) {
                        daDatasourceMapper.insert(daDatasourceDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("quality.import.insert.success",
                                "数据插入成功，ID为 " + daDatasourceId + " 的数据源记录。", daDatasourceId, "数据源"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("quality.import.insert.fail",
                                "数据插入失败，ID为 " + daDatasourceId + " 的数据源记录已存在。", daDatasourceId, "数据源"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("quality.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("quality.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("quality.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("quality.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
        }
        return resultMsg.toString();
    }


    @Override
    public AjaxResult clientsTest(Long id) {
        DbQuery dbQuery = this.buildDbQuery(id);
        if (dbQuery.valid()) {
            dbQuery.close();
            return AjaxResult.success(MessageUtils.messageWithFallback("quality.error.connection.success", "数据库连接成功"));
        }
        dbQuery.close();
        return AjaxResult.error(MessageUtils.messageWithFallback("quality.error.connection.fail", "数据库连接失败"));

    }

    public DbQuery buildDbQuery(Long id) {
        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
        if (daDatasourceBy == null) {
            throw new DataQueryException("db.error.datasource.detail.fail", "数据源详情信息查询失败");
        }
        DbQueryProperty dbQueryProperty = new DbQueryProperty(
                daDatasourceBy.getDatasourceType(),
                daDatasourceBy.getIp(),
                daDatasourceBy.getPort(),
                daDatasourceBy.getDatasourceConfig()
        );
        return dataSourceFactory.createDbQuery(dbQueryProperty);
    }

    /**
     * @param id data source id
     * @return
     */
    @Override
    public List<DbTable> getDbTables(Long id) {
        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
        if (daDatasourceBy == null) {
            throw new DataQueryException("db.error.datasource.detail.fail", "数据源详情信息查询失败");
        }

        DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceBy.getDatasourceType()
                , daDatasourceBy.getIp(), daDatasourceBy.getPort(), daDatasourceBy.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            throw new DataQueryException("db.error.connection.fail", "数据库连接失败");
        }
        List<DbTable> tables = dbQuery.getTables(dbQueryProperty);
        dbQuery.close();
        return tables;
    }

    /**
     * @param id data source id
     * @param tableName table name
     * @return
     */
    @Override
    public List<DbColumn> getDbTableColumns(Long id, String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new DataQueryException("db.error.table.empty", "表名不能为空");
        }

        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
        if (daDatasourceBy == null) {
            throw new DataQueryException("db.error.datasource.detail.fail", "数据源详情信息查询失败");
        }

        DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceBy.getDatasourceType()
                , daDatasourceBy.getIp(), daDatasourceBy.getPort(), daDatasourceBy.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            throw new DataQueryException("db.error.connection.fail", "数据库连接失败");
        }
        List<DbColumn> tableColumns = dbQuery.getTableColumns(dbQueryProperty, tableName);
        dbQuery.close();

        return tableColumns;
    }



}
