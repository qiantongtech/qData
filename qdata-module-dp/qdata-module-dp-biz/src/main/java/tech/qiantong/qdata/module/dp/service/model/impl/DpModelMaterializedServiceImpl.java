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
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetReqDTO;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetRespDTO;
import tech.qiantong.qdata.module.da.api.service.asset.IDaAssetApiOutService;
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
 * 物化模型记录Service业务层处理
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpModelMaterializedServiceImpl  extends ServiceImpl<DpModelMaterializedMapper,DpModelMaterializedDO> implements IDpModelMaterializedService {
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
     * 物化建表
     * @param dpModelMaterialized
     * @return
     */
    @Override
    public Long createMaterializedTable(DpMaterializedMethodReqVO dpModelMaterialized) {
        //取出模型id
        List<Long> modelIdList = dpModelMaterialized.getModelId();
        if(CollectionUtils.isEmpty(modelIdList)){
            throw new RuntimeException("获取信息失败,原因:物化信息为空");
        }

        DbQueryProperty dbQueryProperty = new DbQueryProperty(dpModelMaterialized.getDatasourceType()
                ,dpModelMaterialized.getIP(),dpModelMaterialized.getPort(),dpModelMaterialized.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        //测试链接
        if (!dbQuery.valid()) {
            throw new RuntimeException("数据库连接失败！");
        }

        for (Long modelId : modelIdList) {

            DpModelMaterializedDO dpModelMaterializedDO = this.anotherAsyncTaskSingle(modelId, dpModelMaterialized,dbQuery,dbQueryProperty);

            dpModelMaterializedDO.setCreatorId(dpModelMaterialized.getCreatorId());
            dpModelMaterializedDO.setCreateBy(dpModelMaterialized.getCreateBy());
            dpModelMaterializedDO.setCreateTime(dpModelMaterializedDO.getCreateTime());

            String status = dpModelMaterializedDO.getStatus();
            if(StringUtils.equals("3",status)){
                //资产
                DaAssetReqDTO daAssetReqDTO = new DaAssetReqDTO();
                daAssetReqDTO.setSource("2");
                daAssetReqDTO.setModelId(modelId);
                daAssetReqDTO.setDatasourceId(dpModelMaterialized.getDatasourceId());
                daAssetReqDTO.setFieldCount(dpModelMaterializedDO.getFieldCount());
                DaAssetRespDTO daAssetRespDTO = iDaAssetApiService.insertDaAsset(daAssetReqDTO);
                Long id = daAssetRespDTO.getId();//资产id
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

        // 先创建一个日志对象，并设置一些基础信息；发生异常也能返回它
        DpModelMaterializedDO dpModelMaterializedDO = buildLogRecord(modelId, dpModelMaterialized);

        // 默认先给它一个状态：2=创建中
        dpModelMaterializedDO.setStatus("2");
        // 在往数据库插入前，建议先 setCreateTime / setCreateBy / setUpdateTime 等，根据需要

        try {
            // 1. 查询模型/字段，若校验不通过则抛异常
            DpModelDO dpModelDO = checkAndGetModel(modelId);
            dpModelMaterializedDO.setModelName(dpModelDO.getModelName());
            dpModelMaterializedDO.setModelAlias(dpModelDO.getModelComment());


            List<DpModelColumnDO> columnList = checkAndGetModelColumns(modelId);
            //设置字段数据量
            dpModelMaterializedDO.setFieldCount(Long.valueOf(columnList.size()));


            String tableName = dpModelDO.getModelName();
            int tableStatus = dbQuery.generateCheckTableExistsSQL(dbQueryProperty,tableName);
            if (tableStatus > 0) {
                //应产品要求改动为成功失败
                dpModelMaterializedDO.setStatus("4"); // 5=已存在
                dpModelMaterializedDO.setMessage("表 [" + tableName + "] 已存在，无需重复创建");
                return dpModelMaterializedDO;
            }
            List<DbColumn> dbColumns = this.setColumnsListFromDpModelColumns(columnList);

            List<String> tableSQLList = dbQuery.generateCreateTableSQL(dbQueryProperty
                    ,tableName, dpModelDO.getModelComment(),dbColumns);

            dpModelMaterializedDO.setSqlCommand(tableSQLList.toString()); // 记录一下执行SQL

            for (String sql : tableSQLList) {
                dbQuery.execute(sql);
            }

            // 若执行成功 -> 状态=3
            dpModelMaterializedDO.setStatus("3");
            dpModelMaterializedDO.setMessage("建表成功");

        } catch (Exception ex) {
            // 不管任何异常都要记录到日志中
            dpModelMaterializedDO.setStatus("4"); // 4=失败
            dpModelMaterializedDO.setMessage("建表失败：" + ex.getMessage());
        }

        return dpModelMaterializedDO;
    }


    /**
     * 将 DpModelColumnDO 转换为 DbColumn，并赋值给 columnsList
     * @param columnList DpModelColumnDO 列表
     */
    public List<DbColumn> setColumnsListFromDpModelColumns(List<DpModelColumnDO> columnList) {
        return  columnList.stream()
                .map(dpColumn -> DbColumn.builder()
                        .colName(dpColumn.getEngName())
                        .dataType(dpColumn.getColumnType())
                        .dataLength(dpColumn.getColumnLength() != null ? dpColumn.getColumnLength().toString() : null)
                        .dataScale(dpColumn.getColumnScale() != null ? dpColumn.getColumnScale().toString() : null)
                        .colKey("1".equals(dpColumn.getPkFlag()))
                        .nullable("0".equals(dpColumn.getNullableFlag()))
                        .colPosition(dpColumn.getSortOrder() == null? 1:dpColumn.getSortOrder().intValue())
                        .dataDefault(dpColumn.getDefaultValue())
                        .colComment(dpColumn.getCnName())  // 或者使用其它字段填充
                        .build())
                .collect(Collectors.toList());
    }


    /**
     * 组装一个日志记录对象，填充基础字段
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
        return logRecord;
    }

    /**
     * 查询并校验 DpModelDO，不存在则抛异常
     */
    private DpModelDO checkAndGetModel(Long modelId) {
        DpModelDO dpModelDO = dpModelService.getById(modelId);
        if (dpModelDO == null) {
            throw new RuntimeException("逻辑模型不存在, modelId=" + modelId);
        }
        return dpModelDO;
    }


    /**
     * 查询并校验字段列表，不存在则抛异常
     */
    private List<DpModelColumnDO> checkAndGetModelColumns(Long modelId) {
        DpModelColumnSaveReqVO reqVO = new DpModelColumnSaveReqVO();
        reqVO.setModelId(modelId);
        List<DpModelColumnDO> columnList = dpModelColumnService.getDpModelColumnList(reqVO);
        if (CollectionUtils.isEmpty(columnList)) {
            throw new RuntimeException("逻辑模型无字段，无法建表, modelId=" + modelId);
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
        // 相关校验

        // 更新物化模型记录
        DpModelMaterializedDO updateObj = BeanUtils.toBean(updateReqVO, DpModelMaterializedDO.class);
        return dpModelMaterializedMapper.updateById(updateObj);
    }
    @Override
    public int removeDpModelMaterialized(Collection<Long> idList) {
        // 批量删除物化模型记录
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
                .collect(Collectors.toMap(
                        DpModelMaterializedDO::getId,
                        dpModelMaterializedDO -> dpModelMaterializedDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入物化模型记录数据
         *
         * @param importExcelList 物化模型记录数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDpModelMaterialized(List<DpModelMaterializedRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
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
                                successMessages.add("数据更新成功，ID为 " + dpModelMaterializedId + " 的物化模型记录记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dpModelMaterializedId + " 的物化模型记录记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DpModelMaterializedDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dpModelMaterializedId);
                        DpModelMaterializedDO existingDpModelMaterialized = dpModelMaterializedMapper.selectOne(queryWrapper);
                        if (existingDpModelMaterialized == null) {
                            dpModelMaterializedMapper.insert(dpModelMaterializedDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dpModelMaterializedId + " 的物化模型记录记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dpModelMaterializedId + " 的物化模型记录记录已存在。");
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
                resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
                throw new ServiceException(resultMsg.toString());
            } else {
                resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
            }
            return resultMsg.toString();
        }

}
