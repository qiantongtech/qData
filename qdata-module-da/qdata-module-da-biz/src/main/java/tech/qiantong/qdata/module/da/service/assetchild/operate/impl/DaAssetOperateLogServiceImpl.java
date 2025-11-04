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
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbDataType;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.exception.ServiceException;
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
 * 数据资产操作记录Service业务层处理
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
        // 批量删除数据资产操作记录
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
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据资产操作记录数据
     *
     * @param importExcelList 数据资产操作记录数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDaAssetOperateLog(List<DaAssetOperateLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
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
                            successMessages.add("数据更新成功，ID为 " + daAssetOperateLogId + " 的数据资产操作记录记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daAssetOperateLogId + " 的数据资产操作记录记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaAssetOperateLogDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetOperateLogId);
                    DaAssetOperateLogDO existingDaAssetOperateLog = daAssetOperateLogMapper.selectOne(queryWrapper);
                    if (existingDaAssetOperateLog == null) {
                        daAssetOperateLogMapper.insert(daAssetOperateLogDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daAssetOperateLogId + " 的数据资产操作记录记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daAssetOperateLogId + " 的数据资产操作记录记录已存在。");
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

    @Override
    public void rollBack(Long id) {
        DaAssetOperateLogDO daAssetOperateLogById = this.getDaAssetOperateLogById(id);
        if (daAssetOperateLogById == null || daAssetOperateLogById.getDelFlag()) {
            throw new AssetOperateException("未查询操作信息，请刷新后重试！");
        }
        //判断状态 状态;1:执行中  2:失败  3:成功   4:回滚失败  5:回滚成功
        String status = daAssetOperateLogById.getStatus();
        if (StringUtils.equals("1", status)
                || StringUtils.equals("2", status)
                || StringUtils.equals("5", status)) {
            throw new AssetOperateException("此记录暂不支持回滚，请刷新后重试！");
        }
        String operateType = daAssetOperateLogById.getOperateType();
        DaAssetOperateLogSaveReqVO bean = BeanUtils.toBean(daAssetOperateLogById, DaAssetOperateLogSaveReqVO.class);
        this.applyOperateTypeLogic(bean, operateType);
        this.updateDaAssetOperateLog(bean);
    }

    /**
     * @param bean        待封装的 VO 对象
     * @param operateType 操作类型，"1"、"2"、"3"、"4"
     * @return 处理后的 VO 对象
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
        // 对于 "1"、"3"、"4" 不做额外处理，直接返回原 bean
        return bean;
    }

    /**
     * 根据 operateType 返回对应字符串：
     * 新增 (1) -> "3"
     * 修改 (2) -> "2"
     * 删除 (3) -> "1"
     * 其他      -> ""
     */
    public static String mapOperateType(String operateType) {
        if (operateType == null) {
            return "";
        }
        switch (operateType) {
            case "1": // 新增
                return "3";
            case "2": // 修改
                return "2";
            case "3": // 删除
                return "1";
            default:  // 导入(4) 或未知类型
                return "0";
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDaAssetOperateLog(DaAssetOperateLogSaveReqVO updateReqVO) {
        // 相关校验
        // 1. 校验资产和数据源
        DaAssetRespVO asset = iDaAssetService.getDaAssetByIdSimple(updateReqVO.getAssetId());
        if (asset == null || asset.getDelFlag()) {
            throw new AssetOperateException("未查询到资产信息，请刷新后重试！");
        }
        DaDatasourceRespVO ds = iDaDatasourceService.getDaDatasourceByIdSimple(updateReqVO.getDatasourceId());
        if (ds == null) {
            throw new AssetOperateException("未查询到数据源信息，请刷新后重试！");
        }

        // 2. 分发到具体操作
        String type = StringUtils.trimToNull(updateReqVO.getOperateType());
        if (type == null) {
            throw new AssetOperateException("未获取到变动类型，请刷新后重试！");
        }
        PreContext ctx = prepareContext(ds, updateReqVO.getTableName());
        handlers.getOrDefault(type, (r, c) -> {
            throw new AssetOperateException("不支持的操作类型: " + type);
        }).accept(updateReqVO, ctx);

        // 更新数据资产操作记录
        DaAssetOperateLogDO updateObj = new DaAssetOperateLogDO();
        updateObj.setId(updateReqVO.getId());
        updateObj.setStatus(updateReqVO.getStatus());
        updateObj.setExecuteTime(updateReqVO.getExecuteTime());
        return daAssetOperateLogMapper.updateById(updateObj);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDaAssetOperateLog(DaAssetOperateLogSaveReqVO reqVO) {
        // 1. 校验资产和数据源
        DaAssetRespVO asset = iDaAssetService.getDaAssetByIdSimple(reqVO.getAssetId());
        if (asset == null || asset.getDelFlag()) {
            throw new AssetOperateException("未查询到资产信息，请刷新后重试！");
        }
        DaDatasourceRespVO ds = iDaDatasourceService.getDaDatasourceByIdSimple(reqVO.getDatasourceId());
        if (ds == null) {
            throw new AssetOperateException("未查询到数据源信息，请刷新后重试！");
        }

        // 2. 分发到具体操作
        String type = StringUtils.trimToNull(reqVO.getOperateType());
        if (type == null) {
            throw new AssetOperateException("未获取到变动类型，请刷新后重试！");
        }
        PreContext ctx = prepareContext(ds, reqVO.getTableName());
        handlers.getOrDefault(type, (r, c) -> {
            throw new AssetOperateException("不支持的操作类型: " + type);
        }).accept(reqVO, ctx);

        // 3. 写入日志
        DaAssetOperateLogDO logDo = BeanUtils.toBean(reqVO, DaAssetOperateLogDO.class);
        daAssetOperateLogMapper.insert(logDo);
        return logDo.getId();
    }

    /**
     * 公共：校验表，构造查询上下文
     */
    private PreContext prepareContext(DaDatasourceRespVO ds, String tableName) {
        if (StringUtils.isBlank(tableName)) {
            throw new AssetOperateException("表名不能为空！");
        }
        DbQueryProperty prop = new DbQueryProperty(
                ds.getDatasourceType(), ds.getIp(), ds.getPort(), ds.getDatasourceConfig());
        DbQuery query = dataSourceFactory.createDbQuery(prop);
        if (!query.valid()) {
            throw new DataQueryException("建立实时数据源链接失败！");
        }
        if (query.generateCheckTableExistsSQL(prop, tableName) == 0) {
            throw new DataQueryException("库表不存在，请查看数据库！");
        }
        List<DbColumn> cols = query.getTableColumns(prop, tableName);
        String fullTable;
        if (StringUtils.equals(prop.getDbType(), DbType.KINGBASE8.getDb())
                || StringUtils.equals(prop.getDbType(), DbType.SQL_SERVER.getDb())) {
            fullTable = StringUtils.isNotBlank(prop.getDbName()) ? prop.getDbName() + "." + prop.getSid() + "." + tableName : tableName;
        } else {
            fullTable = StringUtils.isNotBlank(prop.getDbName()) ? prop.getDbName() + "." + tableName : tableName;
        }
        return new PreContext(query, prop, cols, fullTable);
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
     * 根据 keys 中的 commentKeyList，从 after 中取值，
     * 构造一个有序 Map，然后计算其 JSON 的 MD5 并设置到 req 中。
     *
     * @param req       需要设置 updateWhereMd5 的请求对象
     * @param after     原始值 Map
     * @param whereCols 包含 commentKeyList 的 Map
     */
    public static void fillUpdateWhereMd5(DaAssetOperateLogSaveReqVO req,
                                          Map<String, Object> after,
                                          List<String> whereCols) {
        // 2. 按顺序从 after 中取值
        Map<String, Object> whereMap = new LinkedHashMap<>(whereCols.size());
        for (String col : whereCols) {
            if (after.containsKey(col)) {
                whereMap.put(col, after.get(col));
            }
        }
        // 3. 序列化成 JSON
        String json = JSON.toJSONString(whereMap);
        // 4. 计算 MD5 并设置
        String md5 = null;
        try {
            md5 = MD5Util.getInstance().encode(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        req.setUpdateWhereMd5(md5);
    }


    /**
     * 新增逻辑
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
     * 更新逻辑
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
     * 删除逻辑占位
     */
    private void doDelete(DaAssetOperateLogSaveReqVO req, PreContext ctx) {
        Map<String, Object> after = JSONUtils.convertTaskDefinitionJsonMap(req.getUpdateAfter());
        Map<String, Object> keys = JSONUtils.convertTaskDefinitionJsonMap(req.getFieldNames());
        // TODO: 按需补充
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
     * 导入逻辑占位
     */
    private void doImport(DaAssetOperateLogSaveReqVO req, PreContext ctx) {
        // TODO: 按需补充
    }

    /**
     * 格式化单个列的 SET/WHERE 表达式
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
                .orElseThrow(() -> new AssetOperateException("字段 " + name + " 不存在！"));
    }

    /**
     * 数据库与列的上下文
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
     * 自定义业务异常
     */
    public static class AssetOperateException extends RuntimeException {
        public AssetOperateException(String msg) {
            super(msg);
        }
    }
}
