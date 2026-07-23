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

package tech.qiantong.qdata.module.da.service.assetchild.operate.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbDialect;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.DialectFactory;
import tech.qiantong.qdata.common.database.constants.DbDataType;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.common.utils.MD5Util;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateLogSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceRespVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateLogDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.operate.DaAssetOperateLogMapper;
import tech.qiantong.qdata.module.da.service.asset.IDaAssetService;
import tech.qiantong.qdata.module.da.service.assetchild.operate.IDaAssetOperateLogService;
import tech.qiantong.qdata.module.da.service.datasource.IDaDatasourceService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Data asset operation log Service business layer processing
 *
 * @author qdata
 * @date 2025-05-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetOperateLogServiceImpl extends ServiceImpl<DaAssetOperateLogMapper, DaAssetOperateLogDO> implements IDaAssetOperateLogService {

    private final static String sql_INSERT = "INSERT INTO {tableName} ({columns}) VALUES ({values})";
    private final static String sql_UPDATE = "UPDATE {tableName} SET {setValue} WHERE {where}";
    private final static String sql_DELETE = "DELETE FROM {tableName} WHERE {where}";

    @Resource
    private DaAssetOperateLogMapper daAssetOperateLogMapper;

    @Autowired
    private DataSourceFactory dataSourceFactory;

    @Autowired
    @Lazy
    private IDaAssetService iDaAssetService;

    @Autowired
    @Lazy
    private IDaDatasourceService iDaDatasourceService;

    @Override
    public PageResult<DaAssetOperateLogDO> getDaAssetOperateLogPage(DaAssetOperateLogPageReqVO pageReqVO) {
        return daAssetOperateLogMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<DaAssetOperateLogDO> queryDaAssetOperateLogPage(DaAssetOperateLogPageReqVO daAssetOperateLog) {

        Map<String, Object> after = JSONUtils.convertTaskDefinitionJsonMap(daAssetOperateLog.getUpdateBefore());
        Map<String, Object> keys = JSONUtils.convertTaskDefinitionJsonMap(daAssetOperateLog.getFieldNames());
        List<String> whereCols = JSONUtils.splitListByString(keys.get("commentKeyList"));
        DaAssetOperateLogSaveReqVO daAssetOperateLogSaveReqVO = new DaAssetOperateLogSaveReqVO();
        fillUpdateWhereMd5(daAssetOperateLogSaveReqVO, after, whereCols);

        daAssetOperateLog.setUpdateWhereMd5(daAssetOperateLogSaveReqVO.getUpdateWhereMd5());

        return daAssetOperateLogMapper.selectPage(daAssetOperateLog);
    }

    @Override
    public int removeDaAssetOperateLog(Collection<Long> idList) {
        // Batch delete data asset operation logs
        return daAssetOperateLogMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetOperateLogDO getDaAssetOperateLogById(Long id) {
        return daAssetOperateLogMapper.selectById(id);
    }

    @Override
    public List<DaAssetOperateLogDO> getDaAssetOperateLogList() {
        return daAssetOperateLogMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetOperateLogDO> getDaAssetOperateLogMap() {
        List<DaAssetOperateLogDO> daAssetOperateLogList = daAssetOperateLogMapper.selectList();
        return daAssetOperateLogList.stream()
                .collect(Collectors.toMap(
                        DaAssetOperateLogDO::getId,
                        daAssetOperateLogDO -> daAssetOperateLogDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data asset operation log data
     *
     * @param importExcelList Data asset operation log data list
     * @param isUpdateSupport Whether to support update, if already exists, update the data
     * @param operName        Operating user
     * @return Result
     */
    @Override
    public String importDaAssetOperateLog(List<DaAssetOperateLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetOperateLogRespVO respVO : importExcelList) {
            try {
                DaAssetOperateLogDO daAssetOperateLogDO = BeanUtils.toBean(respVO, DaAssetOperateLogDO.class);
                Long daAssetOperateLogId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetOperateLogId != null) {
                        DaAssetOperateLogDO existingDaAssetOperateLog = daAssetOperateLogMapper.selectById(daAssetOperateLogId);
                        if (existingDaAssetOperateLog != null) {
                            daAssetOperateLogMapper.updateById(daAssetOperateLogDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "Data update successful, ID {0} {1} record.", daAssetOperateLogId, MessageUtils.messageWithFallback("da.entity.asset.operation.record", "Data asset operation record")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", daAssetOperateLogId, MessageUtils.messageWithFallback("da.entity.asset.operation.record", "Data asset operation record")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DaAssetOperateLogDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetOperateLogId);
                    DaAssetOperateLogDO existingDaAssetOperateLog = daAssetOperateLogMapper.selectOne(queryWrapper);
                    if (existingDaAssetOperateLog == null) {
                        daAssetOperateLogMapper.insert(daAssetOperateLogDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", daAssetOperateLogId, MessageUtils.messageWithFallback("da.entity.asset.operation.record", "Data asset operation record")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", daAssetOperateLogId, MessageUtils.messageWithFallback("da.entity.asset.operation.record", "Data asset operation record")));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                    "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                    failureNum, failureDetails));
            throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                    "Congratulations! All data imported! Total: {0} records.", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public void rollBack(Long id) {
        DaAssetOperateLogDO daAssetOperateLogById = this.getDaAssetOperateLogById(id);
        if (daAssetOperateLogById == null || daAssetOperateLogById.getDelFlag()) {
            throw new AssetOperateException(MessageUtils.messageWithFallback(
                    "da.error.asset.operation.notfound", "Operation information was not found; refresh and try again"));
        }
        // Check status; status: 1: running, 2: failed, 3: success, 4: rollback failed, 5: rollback success
        String status = daAssetOperateLogById.getStatus();
        if (StringUtils.equals("1", status)
                || StringUtils.equals("2", status)
                || StringUtils.equals("5", status)) {
            throw new AssetOperateException(MessageUtils.messageWithFallback(
                    "da.error.asset.rollback.unsupported",
                    "This record does not support rollback; refresh and try again"));
        }
        String operateType = daAssetOperateLogById.getOperateType();
        DaAssetOperateLogSaveReqVO bean = BeanUtils.toBean(daAssetOperateLogById, DaAssetOperateLogSaveReqVO.class);
        this.applyOperateTypeLogic(bean, operateType);
        this.updateDaAssetOperateLog(bean);
    }

    /**
     * @param bean        VO object to encapsulate
     * @param operateType Operation type: "1", "2", "3", "4"
     * @return Processed VO object
     */
    public static DaAssetOperateLogSaveReqVO applyOperateTypeLogic(
            DaAssetOperateLogSaveReqVO bean,
            String operateType) {

        bean.setStatus("-1");
        bean.setOperateType(mapOperateType(operateType));

        if ("2".equals(operateType)) {
            String before = bean.getUpdateBefore();
            bean.setUpdateBefore(bean.getUpdateAfter());
            bean.setUpdateAfter(before);
        }
        // For "1", "3", "4", no additional processing needed, return the bean as is
        return bean;
    }

    /**
     * Returns the corresponding string based on operateType:
     * Add (1) -> "3"
     * Modify (2) -> "2"
     * Delete (3) -> "1"
     * Other      -> ""
     */
    public static String mapOperateType(String operateType) {
        if (operateType == null) {
            return "";
        }
        switch (operateType) {
            case "1": // Add
                return "3";
            case "2": // Modify
                return "2";
            case "3": // Delete
                return "1";
            default:  // Import (4) or unknown type
                return "0";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDaAssetOperateLog(DaAssetOperateLogSaveReqVO updateReqVO) {
        // Related validation
        // 1. Validate asset and datasource
        DaAssetRespVO asset = iDaAssetService.getDaAssetByIdSimple(updateReqVO.getAssetId());
        if (asset == null || asset.getDelFlag()) {
            throw new AssetOperateException(MessageUtils.messageWithFallback(
                    "da.error.asset.notfound", "Asset information was not found; refresh and try again"));
        }
        DaDatasourceRespVO ds = iDaDatasourceService.getDaDatasourceByIdSimple(updateReqVO.getDatasourceId());
        if (ds == null) {
            throw new AssetOperateException(MessageUtils.messageWithFallback(
                    "da.error.datasource.notfound", "Data source information was not found; refresh and try again"));
        }

        // 2. Dispatch to specific operation
        String type = StringUtils.trimToNull(updateReqVO.getOperateType());
        if (type == null) {
            throw new AssetOperateException(MessageUtils.messageWithFallback(
                    "da.error.asset.change.type.missing", "Change type was not obtained; refresh and try again"));
        }
        PreContext ctx = prepareContext(ds, updateReqVO.getTableName());
        handlers.getOrDefault(type, (r, c) -> {
            throw new AssetOperateException(MessageUtils.messageWithFallback(
                    "da.error.asset.operation.type.unsupported", "Unsupported operation type: {0}", type));
        }).accept(updateReqVO, ctx);

        // Update data asset operation log
        DaAssetOperateLogDO updateObj = new DaAssetOperateLogDO();
        updateObj.setId(updateReqVO.getId());
        updateObj.setStatus(updateReqVO.getStatus());
        updateObj.setExecuteTime(updateReqVO.getExecuteTime());
        return daAssetOperateLogMapper.updateById(updateObj);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDaAssetOperateLog(DaAssetOperateLogSaveReqVO reqVO) {
        // 1. Validate asset and datasource
        DaAssetRespVO asset = iDaAssetService.getDaAssetByIdSimple(reqVO.getAssetId());
        if (asset == null || asset.getDelFlag()) {
            throw new AssetOperateException(MessageUtils.messageWithFallback(
                    "da.error.asset.notfound", "Asset information was not found; refresh and try again"));
        }
        DaDatasourceRespVO ds = iDaDatasourceService.getDaDatasourceByIdSimple(reqVO.getDatasourceId());
        if (ds == null) {
            throw new AssetOperateException(MessageUtils.messageWithFallback(
                    "da.error.datasource.notfound", "Data source information was not found; refresh and try again"));
        }

        // 2. Dispatch to specific operation
        String type = StringUtils.trimToNull(reqVO.getOperateType());
        if (type == null) {
            throw new AssetOperateException(MessageUtils.messageWithFallback(
                    "da.error.asset.change.type.missing", "Change type was not obtained; refresh and try again"));
        }
        PreContext ctx = prepareContext(ds, reqVO.getTableName());
        handlers.getOrDefault(type, (r, c) -> {
            throw new AssetOperateException(MessageUtils.messageWithFallback(
                    "da.error.asset.operation.type.unsupported", "Unsupported operation type: {0}", type));
        }).accept(reqVO, ctx);

        // 3. Write log
        DaAssetOperateLogDO logDo = BeanUtils.toBean(reqVO, DaAssetOperateLogDO.class);
        daAssetOperateLogMapper.insert(logDo);
        return logDo.getId();
    }

    /**
     * Common: validate table, construct query context
     */
    private PreContext prepareContext(DaDatasourceRespVO ds, String tableName) {
        if (StringUtils.isBlank(tableName)) {
            throw new AssetOperateException(MessageUtils.messageWithFallback(
                    "da.error.table.name.empty", "Table name cannot be empty"));
        }
        DbQueryProperty prop = new DbQueryProperty(
                ds.getDatasourceType(), ds.getIp(), ds.getPort(), ds.getDatasourceConfig());
        DbQuery query = dataSourceFactory.createDbQuery(prop);
        if (!query.valid()) {
            throw new DataQueryException("db.error.datasource.realtime.fail", "Failed to establish real-time datasource connection!");
        }
        if (query.generateCheckTableExistsSQL(prop, tableName) == 0) {
            throw new DataQueryException("db.error.table.not.exist", "Table does not exist, please check the database!");
        }
        List<DbColumn> cols = query.getTableColumns(prop, tableName);
        DbDialect dbDialect = DialectFactory.getDialect(DbType.getDbType(prop.getDbType()));
        return new PreContext(query, prop, cols, dbDialect.getTableName(prop, tableName));
    }

    private final Map<String, BiConsumer<DaAssetOperateLogSaveReqVO, PreContext>> handlers = new HashMap<>();

    @PostConstruct
    private void init() {
        handlers.put("1", this::doAdd);
        handlers.put("2", this::doUpdate);
        handlers.put("3", this::doDelete);
        handlers.put("4", this::doImport);
    }

    /**
     * Based on commentKeyList in keys, extract values from after,
     * construct an ordered Map, then compute the MD5 of its JSON and set it in req.
     *
     * @param req       Request object that needs updateWhereMd5 set
     * @param after     Original value Map
     * @param whereCols Map containing commentKeyList
     */
    public static void fillUpdateWhereMd5(DaAssetOperateLogSaveReqVO req,
                                          Map<String, Object> after,
                                          List<String> whereCols) {
        // 2. Extract values from after in order
        Map<String, Object> whereMap = new LinkedHashMap<>(whereCols.size());
        for (String col : whereCols) {
            if (after.containsKey(col)) {
                whereMap.put(col, after.get(col));
            }
        }
        // 3. Serialize to JSON
        String json = JSON.toJSONString(whereMap);
        // 4. Compute MD5 and set
        String md5 = null;
        try {
            md5 = MD5Util.getInstance().encode(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        req.setUpdateWhereMd5(md5);
    }


    /**
     * Add logic
     */
    private void doAdd(DaAssetOperateLogSaveReqVO req, PreContext ctx) {
        Map<String, Object> after = JSONUtils.convertTaskDefinitionJsonMap(req.getUpdateAfter());

        Map<String, Object> keys = JSONUtils.convertTaskDefinitionJsonMap(req.getFieldNames());
        List<String> whereCols = JSONUtils.splitListByString(keys.get("commentKeyList"));
        fillUpdateWhereMd5(req, after, whereCols);


        StringJoiner colsJs = new StringJoiner(","), valsJs = new StringJoiner(",");
        after.forEach((key, val) -> {
            DbColumn col = findColumn(key, ctx.columns);
            colsJs.add(col.getColName());
            valsJs.add(packFormatValue(val, col.getDataType(), ctx.prop));
        });

        String sql = sql_INSERT
                .replace("{tableName}", ctx.fullTable)
                .replace("{columns}", colsJs.toString())
                .replace("{values}", valsJs.toString());
        log.info("ADD SQL: {}", sql);

        req.setExecuteTime(DateUtils.getExecutionDate());
        int cnt = ctx.query.update(sql);
        if (StringUtils.isNotEmpty(req.getStatus()) && StringUtils.equals("-1", req.getStatus())) {
            req.setStatus(cnt > 0 ? "5" : "4");
        } else {
            req.setStatus(cnt > 0 ? "3" : "2");
        }
    }

    /**
     * Update logic
     */
    private void doUpdate(DaAssetOperateLogSaveReqVO req, PreContext ctx) {
        Map<String, Object> after = JSONUtils.convertTaskDefinitionJsonMap(req.getUpdateAfter());
        Map<String, Object> keys = JSONUtils.convertTaskDefinitionJsonMap(req.getFieldNames());
        List<String> setCols = JSONUtils.splitListByString(keys.get("tableCommentList"));
        List<String> whereCols = JSONUtils.splitListByString(keys.get("commentKeyList"));
        fillUpdateWhereMd5(req, after, whereCols);

        String setClause = setCols.stream()
                .map(colName -> formatExpression(colName, after.get(colName), findColumn(colName, ctx.columns), ctx.prop))
                .collect(Collectors.joining(","));
        String whereClause = whereCols.stream()
                .map(colName -> formatExpression(colName, after.get(colName), findColumn(colName, ctx.columns), ctx.prop))
                .collect(Collectors.joining(" AND "));

        String sql = sql_UPDATE
                .replace("{tableName}", ctx.fullTable)
                .replace("{setValue}", setClause)
                .replace("{where}", whereClause);
        log.info("UPDATE SQL: {}", sql);

        req.setExecuteTime(DateUtils.getExecutionDate());
        int cnt = ctx.query.update(sql);
        if (StringUtils.isNotEmpty(req.getStatus()) && StringUtils.equals("-1", req.getStatus())) {
            req.setStatus(cnt > 0 ? "5" : "4");
        } else {
            req.setStatus(cnt > 0 ? "3" : "2");
        }
    }

    /**
     * Delete logic placeholder
     */
    private void doDelete(DaAssetOperateLogSaveReqVO req, PreContext ctx) {
        Map<String, Object> after = JSONUtils.convertTaskDefinitionJsonMap(req.getUpdateAfter());
        Map<String, Object> keys = JSONUtils.convertTaskDefinitionJsonMap(req.getFieldNames());
        // TODO: supplement as needed
        List<String> whereCols = JSONUtils.splitListByString(keys.get("commentKeyList"));
        String whereClause = whereCols.stream()
                .map(colName -> formatExpression(colName, after.get(colName), findColumn(colName, ctx.columns), ctx.prop))
                .collect(Collectors.joining(" AND "));
        String sql = sql_DELETE
                .replace("{tableName}", ctx.fullTable)
                .replace("{where}", whereClause);
        log.info("UPDATE SQL: {}", sql);

        req.setExecuteTime(DateUtils.getExecutionDate());
        int cnt = ctx.query.update(sql);
        if (StringUtils.isNotEmpty(req.getStatus()) && StringUtils.equals("-1", req.getStatus())) {
            req.setStatus(cnt > 0 ? "5" : "4");
        } else {
            req.setStatus(cnt > 0 ? "3" : "2");
        }
    }

    /**
     * Import logic placeholder
     */
    private void doImport(DaAssetOperateLogSaveReqVO req, PreContext ctx) {
        // TODO: supplement as needed
    }

    /**
     * Format a single column's SET/WHERE expression
     */
    private static String formatExpression(String colName, Object val, DbColumn col, DbQueryProperty prop) {
        String expr;
        String timeType = DbDataType.checkTime(col.getDataType());
        if (val == null) {
            expr = "NULL";
        } else if (StringUtils.isNotBlank(timeType)) {
            DbDataType dt = DbDataType.getByDbTypeAndFieldType(prop.getDbType(), timeType);
            expr = dt.getSql().replace("${data}", val.toString());
        } else if (isBooleanType(col.getDataType())) {
            expr = ((Boolean) val) ? "1" : "0";
        } else if (val instanceof Number) {
            expr = val.toString();
        } else {
            expr = "'" + val + "'";
        }
        return colName + " = " + expr;
    }

    private static String packFormatValue(Object v, String dataType, DbQueryProperty prop) {
        if (v == null) return "NULL";
        if (isBooleanType(dataType)) return ((Boolean) v) ? "1" : "0";
        return (v instanceof Number) ? v.toString() : "'" + v + "'";
    }

    private static boolean isBooleanType(String dt) {
        return "BOOLEAN".equalsIgnoreCase(dt) || "boolean".equalsIgnoreCase(dt);
    }

    private static DbColumn findColumn(String name, List<DbColumn> cols) {
        return cols.stream()
                .filter(c -> name.equals(c.getColName()))
                .findFirst()
                .orElseThrow(() -> new AssetOperateException(MessageUtils.messageWithFallback(
                        "da.error.field.notfound", "Field {0} does not exist", name)));
    }

    /**
     * Database and column context
     */
    private static class PreContext {
        final DbQuery query;
        final DbQueryProperty prop;
        final List<DbColumn> columns;
        final String fullTable;

        PreContext(DbQuery q, DbQueryProperty p, List<DbColumn> c, String ft) {
            this.query = q;
            this.prop = p;
            this.columns = c;
            this.fullTable = ft;
        }
    }

    /**
     * Custom business exception
     */
    public static class AssetOperateException extends RuntimeException {
        public AssetOperateException(String msg) {
            super(msg);
        }
    }
}
