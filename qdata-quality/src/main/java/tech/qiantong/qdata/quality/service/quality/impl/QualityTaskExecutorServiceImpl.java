package tech.qiantong.qdata.quality.service.quality.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.httpClient.HttpTaskLogger;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskRespVO;
import tech.qiantong.qdata.quality.controller.quality.vo.CheckErrorDataReqDTO;
import tech.qiantong.qdata.quality.controller.quality.vo.QualityRuleQueryReqDTO;
import tech.qiantong.qdata.quality.controller.quality.vo.ValidationSqlResult;
import tech.qiantong.qdata.quality.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskEvaluateDO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskObjDO;
import tech.qiantong.qdata.quality.dal.dataobject.quality.CheckErrorData;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityCheckResult;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.repository.CheckErrorDataRepository;
import tech.qiantong.qdata.quality.service.datasource.IDaDatasourceQualityService;
import tech.qiantong.qdata.quality.service.qa.*;
import tech.qiantong.qdata.quality.service.quality.QualityTaskExecutorService;
import tech.qiantong.qdata.quality.utils.quality.QualitySqlGenerateFactory;
import tech.qiantong.qdata.quality.utils.quality.QualitySqlGenerator;
import tech.qiantong.qdata.redis.service.IRedisService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class QualityTaskExecutorServiceImpl implements QualityTaskExecutorService {

//    @Autowired
//    private QualityRuleService qualityRuleService;

    @Autowired
    private QualitySqlGenerateFactory qualitySqlGenerateFactory;

    @Autowired(required = false)
    @Lazy
    private MongoTemplate mongoTemplate;

    @Autowired
    private DataSourceFactory dataSourceFactory;

    @Autowired
    private CheckErrorDataRepository checkErrorDataRepository;

    @Autowired
    private IDppQualityTaskService iDppQualityTaskService;

    @Autowired
    private IDppQualityTaskObjService iDppQualityTaskObjService;

    @Autowired
    private IDppQualityTaskEvaluateService iDppQualityTaskEvaluateService;

    @Autowired
    private IDppQualityLogService iDppQualityLogService;

    @Autowired
    private IDppEvaluateLogService iDppEvaluateLogService;

    @Resource
    @Lazy
    private IDaDatasourceQualityService iDaDatasourceQualityService;

    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor qualityTaskExecutor;

    @Autowired
    private IRedisService redisService;
    // 本地文件路径前缀
    private static String prefixUrl;

    @Value("${file.job.log.qualitytask_prefix_url}")
    private void setPrefixUrl(String prefixUrl) {
        this.prefixUrl = prefixUrl;
    }


    @Override
    public void executeTask(String taskId) {
        String key = "executeQualityTask-" + taskId;
        String status = redisService.get(key);
        if (StringUtils.isEmpty(status) && StringUtils.equals("1", status)) {
            throw new RuntimeException("历史任务未执行完毕，请稍后重试");
        }
        redisService.set(key, "1", 1200);

        qualityTaskExecutor.submit(() -> executeQualityTask(taskId,key));
    }

    public void executeQualityTask(String taskId,String key) {

        // 创建TaskLogger实例，指定日志文件存放的文件夹和文件名
        String tmpFilePath = "taskLog-" + IdUtil.simpleUUID() + ".txt";
        HttpTaskLogger logger = new HttpTaskLogger(prefixUrl, tmpFilePath);
        logger.log("任务开始执行");

        //1、查询任务基本信息
        DppQualityTaskRespVO dppQualityTaskById = iDppQualityTaskService.getDppQualityTaskById(JSONUtils.convertToLong(taskId));
        if(dppQualityTaskById == null){
            logger.log("质量任务-查询本次任务所需执行任务信息:查询为空，未获取到！");
            logger.log("任务结束");
            // 任务完成后，关闭logger，释放资源
            logger.close();
            redisService.set(key, "3", 300);
            return;
        }

        logger.log("质量任务-查询本次触发执行的质量任务信息："+dppQualityTaskById.toString());
        //2、生成本次批次号
        String batch = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        logger.log("质量任务-生成本次批次号："+batch);

        //获取文件地址路径
        String filePath = logger.getFilePath();
        //创建本次任务日志，先创建日志，再执行
        DppQualityLogSaveReqVO dppQualityLogSaveReqVO = new DppQualityLogSaveReqVO(dppQualityTaskById);
        dppQualityLogSaveReqVO.setPath(filePath);
        Long taskLogId = iDppQualityLogService.createDppQualityLog(dppQualityLogSaveReqVO);
        logger.log("质量任务-生成本次任务日志，先创建日志，再执行："+taskLogId);

        // 3. 查询本次任务所需执行的数据源列表
        logger.log("质量任务-查询本次任务所需执行的数据源列表开始");
        List<DppQualityTaskObjDO> qualityTaskObjDOList = iDppQualityTaskObjService.getDppQualityTaskObjList(taskId);
        if (CollectionUtils.isEmpty(qualityTaskObjDOList)) {
            logger.log("质量任务-查询本次任务所需执行的数据源列表:查询为空，未获取到列表！");
            logger.log("任务结束");
            updateQualityLog(taskLogId,"1");
            // 任务完成后，关闭logger，释放资源
            logger.close();
            redisService.set(key, "3", 300);
            return;
        }
        logger.log("质量任务-查询本次任务所需执行的数据源列表,数量为："+qualityTaskObjDOList.size());

        logger.log("质量任务-归纳本次质量任务，同数据源归纳整理。");
        //4、根据数据源id分组，不用重复创建数据源链接
        Map<Long, List<Long>> groupIdsByDatasourceId = groupIdsByDatasourceId(qualityTaskObjDOList);
        logger.log("质量任务-归纳本次质量任务，同数据源归纳整理结束，总计："+groupIdsByDatasourceId.size()+" 个");
        for (Map.Entry<Long, List<Long>> entry : groupIdsByDatasourceId.entrySet()) {
            Long datasourceId = entry.getKey();          // 即 daDatasourceById
            List<Long> idList = entry.getValue();        // id集合

            logger.log("质量任务-开始获取数据源链接信息。");
            //5、获取数据源基本信息
            DaDatasourceDO daDatasourceById = iDaDatasourceQualityService.getDaDatasourceById(datasourceId);
            if (daDatasourceById == null){
                logger.log("质量任务-数据源信息获取失败，跳过该数据源。跳过的数据源id："+datasourceId);
                logger.log("质量任务-数据源信息获取失败，数据源id为："+datasourceId+" 的规则无法执行，跳过。");
                continue;
            }
            logger.log("质量任务-数据源信息获取成功，准备建立数据库连接。数据源名称为："+daDatasourceById.getDatasourceName()+" ,数据源ID为："+daDatasourceById.getId());
            //6、测试数据源是否正常，不正常则结束进行下一个
            DbQueryProperty dbQueryProperty = new DbQueryProperty(
                    daDatasourceById.getDatasourceType(),
                    daDatasourceById.getIp(),
                    daDatasourceById.getPort(),
                    daDatasourceById.getDatasourceConfig()
            );
            DbQuery dbQuery;
            try {
                dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
                if (!dbQuery.valid()) {
                    logger.log("质量任务-数据源连接测试失败，跳过该数据源。");
                    logger.log("质量任务-数据源信息获取失败，数据源名称为："+daDatasourceById.getDatasourceName()+" ,数据源id为："+daDatasourceById.getId()+" 的规则无法执行，跳过。");
                    continue;
                }
            }catch (Exception e){
                logger.log("质量任务-数据源连接异常：" + e.getMessage());
                logger.log("质量任务-数据源连接异常，跳过该数据源。");
                logger.log("质量任务-数据源信息获取失败，数据源名称为："+daDatasourceById.getDatasourceName()+" ,数据源id为："+daDatasourceById.getId()+" 的规则无法执行，跳过。");
                continue;
            }
            logger.log("质量任务-数据源信息获取成功，建立数据库连接成功。");

            logger.log("质量任务-开始获取该数据源下的质量规则。");
            //7、从数据源取出规则进行规则查询
            List<DppQualityTaskEvaluateDO> dppQualityTaskEvaluateList = iDppQualityTaskEvaluateService.getDppQualityTaskEvaluateList(idList);

            logger.log("质量任务-准备逐个处理该数据源下每个质量规则。");
            //循环规则
            for (DppQualityTaskEvaluateDO dppQualityTaskEvaluateDO : dppQualityTaskEvaluateList) {
                //8、封装规则进行调取方法
                QualityRuleEntity qualityRuleEntity = new QualityRuleEntity(dppQualityTaskEvaluateDO);
                logger.log("质量任务-规则封装完成，准备获取字段信息。");
                qualityRuleEntity.setTaskLogId(taskLogId);
                List<DbColumn> tableColumns = dbQuery.getTableColumns(dbQueryProperty, qualityRuleEntity.getTableName());
                if(CollectionUtils.isEmpty(tableColumns)){
                    logger.log("质量任务-字段信息获取失败，终止当前规则处理。");
                    continue;
                }
                List<String> showErrorColumns = tableColumns.stream()
                        .map(DbColumn::getColName)
                        .collect(Collectors.toList());
                //存储参数
                qualityRuleEntity.setShowErrorColumns(showErrorColumns);
                qualityRuleEntity.setTaskLogId(taskLogId);
                qualityRuleEntity.setDaDatasourceById(daDatasourceById);
                logger.log("质量任务-字段信息设置完成，准备执行规则。");
                try {
                    RuleExecutorTask ruleExecutorTask = new RuleExecutorTask(qualityRuleEntity, batch,dbQuery, qualitySqlGenerateFactory, mongoTemplate,logger, iDppEvaluateLogService);
                    logger.log("质量任务-开始执行规则任务。");
                    ruleExecutorTask.call();
                }catch (Exception e){
                    logger.log("质量任务-执行规则任务失败，终止当前规则处理。");
                    logger.log(e.getMessage());
                    continue;
                }
                logger.log("质量任务-规则任务执行完成。");
            }
        }
        //更新完善日志
        updateQualityLog(taskLogId,"0");
        logger.log("任务结束");
        // 任务完成后，关闭logger，释放资源
        logger.close();
        redisService.set(key, "2", 300);
    }

    public static Map<Long, List<Long>> groupIdsByDatasourceId(List<DppQualityTaskObjDO> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyMap();
        }

        return list.stream()
                .filter(obj -> obj.getDatasourceId() != null && obj.getId() != null)
                .collect(Collectors.groupingBy(
                        DppQualityTaskObjDO::getDatasourceId,
                        Collectors.mapping(DppQualityTaskObjDO::getId, Collectors.toList())
                ));
    }


    /**
     * 更新数据质量日志状态（仅更新 successFlag 和结束时间）
     * @param id 日志ID
     * @param successFlag 状态标志（1：成功，2：失败）
     */
    public void updateQualityLog(Long id, String successFlag) {
        DppQualityLogSaveReqVO vo = new DppQualityLogSaveReqVO();
        vo.setId(id);
        vo.setSuccessFlag(successFlag);
        vo.setEndTime(DateUtil.date());

        iDppQualityLogService.updateDppQualityLog(vo);
    }

//
//    public void executeTask2(QualityRuleEntity taskId) {
//        // 1. 查询任务详情（模拟或从任务表）
//        // 包含数据源信息、表名、任务批次等
//        String batch = DateUtil.format(new Date(), "yyyyMMddHHmmss");
//
//        // 2. 查询质量规则（可执行的）
////        List<QualityRuleEntity> rules = qualityRuleService.getRulesByTaskId(taskId);
//        List<QualityRuleEntity> rules =new ArrayList<>();
//        rules.add(taskId);
//        if (CollectionUtils.isEmpty(rules)) {
//            return;
//        }
//
//        ExecutorService executor = Executors.newFixedThreadPool(5);
//
//        List<Future<QualityCheckResult>> futures = new ArrayList<>();
//        for (QualityRuleEntity rule : rules) {
//            DaDatasourceDO daDatasourceById = iDaDatasourceQualityService.getDaDatasourceById(JSONUtils.convertToLong(rule.getDataId()));
//            rule.setDaDatasourceById(daDatasourceById);
//            futures.add(executor.submit(new RuleExecutorTask(rule, batch, qualitySqlGenerateFactory, mongoTemplate,dataSourceFactory)));
//        }
//
//        try {
//            for (Future<QualityCheckResult> future : futures) {
//                QualityCheckResult result = future.get();
//                System.out.println(result.toString());
//                // 保存结果 report，可扩展逻辑
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            executor.shutdown();
//        }
//    }



    @Override
    public ValidationSqlResult generateValidationValidDataSql(QualityRuleQueryReqDTO queryReqDTO) {
        DaDatasourceDO daDatasourceById = iDaDatasourceQualityService.getDaDatasourceById(JSONUtils.convertToLong(queryReqDTO.getDataId()));
        if (daDatasourceById == null){
            throw new ServiceException("建立实时数据源链接失败！");
        }
        DbQueryProperty dbQueryProperty = new DbQueryProperty(
                daDatasourceById.getDatasourceType(),
                daDatasourceById.getIp(),
                daDatasourceById.getPort(),
                daDatasourceById.getDatasourceConfig()
        );
        DbQuery dbQuery;
        try {
            dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
            if (!dbQuery.valid()) {
                throw new DataQueryException("建立实时数据源链接失败！");            }
        }catch (Exception e){
            throw new DataQueryException("建立实时数据源链接异常！");
        }
        List<DbColumn> tableColumns = dbQuery.getTableColumns(dbQueryProperty, queryReqDTO.getTableName());
        List<String> showErrorColumns = tableColumns.stream()
                .map(DbColumn::getColName)
                .collect(Collectors.toList());
        ValidationSqlResult validationSqlResult = new ValidationSqlResult();
        //存储参数
        validationSqlResult.setShowErrorColumns(tableColumns);

        QualitySqlGenerator generator = qualitySqlGenerateFactory.getGenerator(queryReqDTO.getRuleType());
        QualityRuleEntity rule = new QualityRuleEntity(queryReqDTO);
        rule.setShowErrorColumns(showErrorColumns);
        rule.setDaDatasourceById(daDatasourceById);
        String checkSql = generator.generateValidDataSql(rule,queryReqDTO.getLimit(),queryReqDTO.getOffset());
        // 2. 执行 SQL
        try (Connection conn = dbQuery.getConnection();
             Statement stmt = conn.createStatement()) {
            List<JSONObject> errorList = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery(checkSql)) {
                while (rs.next()) {
                    JSONObject row = new JSONObject();
                    for (String col : rule.getShowErrorColumns()) {
                        row.put(col, rs.getObject(col));
                    }
                    errorList.add(row);
                }
            }
            validationSqlResult.setDataList(errorList);
        }catch (Exception e){
            throw new DataQueryException("建立实时数据源链接失败！");
        }

        validationSqlResult.setLimit(queryReqDTO.getLimit());
        validationSqlResult.setOffset(queryReqDTO.getOffset());
        validationSqlResult.setPageNum(queryReqDTO.getPageNum());
        validationSqlResult.setPageSize(queryReqDTO.getPageSize());
        return validationSqlResult;
    }

    @Override
    public ValidationSqlResult generateValidationErrorDataSql(QualityRuleQueryReqDTO queryReqDTO) {
        DaDatasourceDO daDatasourceById = iDaDatasourceQualityService.getDaDatasourceById(JSONUtils.convertToLong(queryReqDTO.getDataId()));
        if (daDatasourceById == null){
            throw new ServiceException("建立实时数据源链接失败！");
        }
        DbQueryProperty dbQueryProperty = new DbQueryProperty(
                daDatasourceById.getDatasourceType(),
                daDatasourceById.getIp(),
                daDatasourceById.getPort(),
                daDatasourceById.getDatasourceConfig()
        );
        DbQuery dbQuery;
        try {
            dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
            if (!dbQuery.valid()) {
                throw new DataQueryException("建立实时数据源链接失败！");            }
        }catch (Exception e){
            throw new DataQueryException("建立实时数据源链接异常！");
        }
        List<DbColumn> tableColumns = dbQuery.getTableColumns(dbQueryProperty, queryReqDTO.getTableName());
        List<String> showErrorColumns = tableColumns.stream()
                .map(DbColumn::getColName)
                .collect(Collectors.toList());
        ValidationSqlResult validationSqlResult = new ValidationSqlResult();
        validationSqlResult.setShowErrorColumns(tableColumns);

        QualitySqlGenerator generator = qualitySqlGenerateFactory.getGenerator(queryReqDTO.getRuleType());
        QualityRuleEntity rule = new QualityRuleEntity(queryReqDTO);
        //存储参数
        rule.setShowErrorColumns(showErrorColumns);
        rule.setDaDatasourceById(daDatasourceById);
        String checkSql = generator.generateErrorSql(rule);
        // 2. 执行 SQL
        try (Connection conn = dbQuery.getConnection();
             Statement stmt = conn.createStatement()) {
            List<JSONObject> errorList = new ArrayList<>();
            try (ResultSet rs = stmt.executeQuery(checkSql)) {
                while (rs.next()) {
                    JSONObject row = new JSONObject();
                    for (String col : rule.getShowErrorColumns()) {
                        row.put(col, rs.getObject(col));
                    }
                    errorList.add(row);
                }
            }
            validationSqlResult.setDataList(errorList);
        }catch (Exception e){
            throw new DataQueryException("建立实时数据源链接失败！");
        }
        return validationSqlResult;
    }

//    @Override
//    public Page<CheckErrorData> pageErrorData(PageRequest of,  CheckErrorDataReqDTO checkErrorDataReqDTO) {
//        CheckErrorData person =  this.convertFrom(checkErrorDataReqDTO);
//        Example<CheckErrorData> example = Example.of(person);
//        Page<CheckErrorData> page = checkErrorDataRepository.findAll(example, of);
//        System.out.println("总条数：" + page.getTotalElements());
//        System.out.println("总页数：" + page.getTotalPages());
//        page.getContent().forEach(s -> {
//            s.setJsonData(JSONObject.parseObject(s.getDataJsonStr()));
//        });
//        return page;
//    }
//
//    public static CheckErrorData convertFrom(CheckErrorDataReqDTO dto) {
//        CheckErrorData data = new CheckErrorData();
//        data.setReportId(dto.getReportId());
//
//        if (dto.getRepair() != null){
//            data.setRepair(dto.getRepair());
//        }
//
//
//
//        return data;
//    }

    @Override
    public Page<CheckErrorData> pageErrorData(PageRequest pageRequest, CheckErrorDataReqDTO dto) {
        Query query = new Query();

        query.addCriteria(Criteria.where("reportId").is(dto.getReportId()));


        // data_json 子字段条件
        Map<String, Object> keyWordData = dto.getKeyWordData();
        if (keyWordData != null && !keyWordData.isEmpty()) {
            List<Criteria> orCriteriaList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : keyWordData.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value != null) {
                    orCriteriaList.add(Criteria.where("json_data." + key)
                            .regex(Pattern.compile(value.toString(), Pattern.CASE_INSENSITIVE)));
                }
            }
            if (!orCriteriaList.isEmpty()) {
                query.addCriteria(new Criteria().orOperator(orCriteriaList.toArray(new Criteria[0])));
            }
        }
        // 分页信息
        long total = mongoTemplate.count(query, CheckErrorData.class);
        List<CheckErrorData> content = mongoTemplate.find(query.with(pageRequest), CheckErrorData.class);
        content.forEach(s -> {
            s.setJsonData(JSONObject.parseObject(s.getDataJsonStr()));
        });

        return new PageImpl<>(content, pageRequest, total);
    }

    @Override
    public boolean updateErrorData(CheckErrorDataReqDTO dto) {
        if (dto.getReportId() == null ) {
            return false;
        }

        String updateType = dto.getUpdateType();
        if (updateType == null) {
            return false;
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("reportId").is(dto.getReportId()));

        Update update = new Update();

        switch (updateType) {
            case "1": // 修改数据

                if (dto.getUpdatedData() != null && !dto.getUpdatedData().isEmpty()) {
                    boolean success = updatePhysicalTable(dto);
                    if (!success) return false;

                    //成功继续，不成功返回
                    update.set("data_json",JSONObject.toJSONString(dto.getUpdatedData()));
                    Map<String, Object> oldData = dto.getOldData();
                    String dataJsonStr = MapUtils.getString(oldData, "dataJsonStr");
                    Object jsonData = MapUtils.getObject(oldData, "jsonData");
                    String id = MapUtils.getString(oldData, "id");
                    query.addCriteria(Criteria.where("id").is(id)); // Mongo 主键
                    // 同步修改旧数据字段
                    update.set("data_json_old",dataJsonStr);
                    update.set("json_data_old",jsonData);
                    update.set("repair", "1");
                }
                break;

            case "2": // 修改备注
                if (dto.getId() == null) {
                    return false;
                }
                query.addCriteria(Criteria.where("id").is(dto.getId())); // Mongo 主键
                update.set("remark", dto.getRemark() == null ? "" : dto.getRemark());
                break;

            case "3": // 修改状态（repair 字段）
                if (CollectionUtils.isEmpty(dto.getErrorDataId())) {
                    return false;
                }
                query.addCriteria(Criteria.where("id").in(dto.getErrorDataId()));
                update.set("repair", "2");
                break;

            default:
                return false;
        }

        UpdateResult result = mongoTemplate.updateFirst(query, update, CheckErrorData.class);
        return result.getModifiedCount() > 0;
    }

    private boolean updatePhysicalTable(CheckErrorDataReqDTO dto) {
        DaDatasourceDO datasource = iDaDatasourceQualityService.getDaDatasourceById(dto.getDatasourceId());
        if (datasource == null) {
            return false;
        }

        DbQueryProperty dbQueryProperty = new DbQueryProperty(
                datasource.getDatasourceType(),
                datasource.getIp(),
                datasource.getPort(),
                datasource.getDatasourceConfig()
        );

        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        try {
            if(!dbQuery.valid()){
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        Map<String, Object> updatedData = dto.getKeyWordData();
        Map<String, Object> oldDataMap = dto.getOldData();
        if (updatedData == null || oldDataMap == null || oldDataMap.isEmpty()) {
            return false;
        }

        // 取出 oldData 中的 jsonData 字段（Map<String, Object>）
        Object jsonDataObj = oldDataMap.get("jsonData");
        if (!(jsonDataObj instanceof Map)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> whereData = (Map<String, Object>) jsonDataObj;

        // 表名（前提：已做白名单校验）
        String fullTable = buildFullTableName(dbQueryProperty,dto.getTableName());

//        // SET 子句
//        StringBuilder setClause = new StringBuilder();
//        for (Map.Entry<String, Object> entry : updatedData.entrySet()) {
//            if (setClause.length() > 0) setClause.append(", ");
//            setClause.append("`").append(entry.getKey()).append("` = ")
//                    .append(toSqlLiteral(entry.getValue()));
//        }
//
//        // WHERE 子句（null -> IS NULL；集合 -> IN(...)）
//        StringBuilder whereClause = new StringBuilder();
//        for (Map.Entry<String, Object> entry : whereData.entrySet()) {
//            if (whereClause.length() > 0) whereClause.append(" AND ");
//            whereClause.append("`").append(entry.getKey()).append("` ");
//            Object v = entry.getValue();
//            if (v == null) {
//                whereClause.append("IS NULL");
//            } else if (v instanceof Collection) {
//                Collection<?> col = (Collection<?>) v;
//                if (col.isEmpty()) {
//                    whereClause.append("IN (NULL)");
//                } else {
//                    whereClause.append("IN (")
//                            // if toSqlLiteral 是 static：用 lambda 避免 this:: 报错
//                            .append(col.stream().map(o -> toSqlLiteral(o))
//                                    .collect(Collectors.joining(", ")))
//                            .append(")");
//                }
//            } else {
//                whereClause.append("= ").append(toSqlLiteral(v));
//            }
//        }
//
//        if (setClause.length() == 0 || whereClause.length() == 0) return false;

        String oq = openQuote( dbQueryProperty.getDbType()), cq = closeQuote( dbQueryProperty.getDbType());

        String setClause = updatedData.entrySet().stream()
                .map(e -> oq + e.getKey() + cq + " = " + toSqlLiteral(e.getValue(),  dbQueryProperty.getDbType()))
                .collect(Collectors.joining(", "));

        String whereClause = whereData.entrySet().stream()
                .map(e -> buildWhereAtom(oq, cq, e.getKey(), e.getValue(),  dbQueryProperty.getDbType()))
                .collect(Collectors.joining(" AND "));

        String sql = String.format("UPDATE %s SET %s WHERE %s", fullTable, setClause, whereClause);

        try {
            int rows = dbQuery.update(sql);
            return rows > 0;
        } catch (Exception e) {
            return false;
        }

    }
    private static String toSqlLiteral(Object v) {
        if (v == null) return "NULL";

        if (v instanceof Number) {
            if (v instanceof java.math.BigDecimal) {
                return ((java.math.BigDecimal) v).toPlainString();
            }
            return v.toString();
        }
        if (v instanceof Boolean) {
            return ((Boolean) v) ? "1" : "0";
        }
        if (v instanceof java.time.LocalDate) {
            return "'" + v.toString() + "'";
        }
        if (v instanceof java.time.LocalDateTime) {
            return "'" + v.toString().replace('T', ' ') + "'";
        }
        if (v instanceof java.util.Date) {
            java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "'" + fmt.format((java.util.Date) v) + "'";
        }
        // 默认当作字符串
        return "'" + escapeSql(v.toString()) + "'";
    }
    private static String escapeSql(String s) {
        // 单引号 -> 两个单引号；反斜杠 -> 双反斜杠（若 NO_BACKSLASH_ESCAPES 开启，可仅替换单引号）
        return s.replace("\\", "\\\\").replace("'", "''");
    }

    /**
     * 获取完整表名
     * @param dbQueryProperty 数据源配置
     * @return 完整表名
     */
    public static String buildFullTableName(DbQueryProperty dbQueryProperty, String tableName ) {
        String dbType = dbQueryProperty.getDbType();
        String dbName = dbQueryProperty.getDbName();
        String sid = dbQueryProperty.getSid();

        if (StringUtils.equals(DbType.SQL_SERVER.getDb(), dbType)) {
            // SQL Server 拼接方式：dbName.sid.tableName
            if (StringUtils.isNotBlank(dbName) && StringUtils.isNotBlank(sid)) {
                return dbName + "." + sid + "." + tableName;
            } else if (StringUtils.isNotBlank(dbName)) {
                return dbName + ".." + tableName; // 只拼库名
            } else {
                return tableName; // 只表名
            }
        } else {
            // 默认 MySQL 及其他，使用反引号
            return StringUtils.isNotBlank(dbName)
                    ? "`" + dbName + "`.`" + tableName + "`"
                    : "`" + tableName + "`";
        }
    }

    private static String buildWhereAtom(String oq, String cq, String key, Object val, String dbType) {
        String col = oq + key + cq;
        if (val == null) return col + " IS NULL";

        if (val.getClass().isArray()) {
            val = Arrays.asList((Object[]) val);
        }
        if (val instanceof Collection<?>) {
            Collection<?> c = (Collection<?>) val;
            if (c.isEmpty()) return col + " IN (NULL)";
            String inVals = c.stream().map(v -> toSqlLiteral(v, dbType)).collect(Collectors.joining(", "));
            return col + " IN (" + inVals + ")";
        }
        return col + " = " + toSqlLiteral(val, dbType);
    }

    // ========== 标识符引号 ==========
    private static String openQuote(String dbType) {
        return StringUtils.equalsIgnoreCase(DbType.SQL_SERVER.getDb(), dbType) ? "[" : "`";
    }
    private static String closeQuote(String dbType) {
        return StringUtils.equalsIgnoreCase(DbType.SQL_SERVER.getDb(), dbType) ? "]" : "`";
    }

    // ========== 值转 SQL 字面量 ==========
    private static String toSqlLiteral(Object v, String dbType) {
        if (v == null) return "NULL";

        if (v instanceof Number) {
            return (v instanceof BigDecimal) ? ((BigDecimal) v).toPlainString() : String.valueOf(v);
        }
        if (v instanceof Boolean) {
            return (Boolean) v ? "1" : "0";
        }
        if (v instanceof java.util.Date) {
            String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((java.util.Date) v);
            return quoteString(s, dbType, hasNonAscii(s));
        }
        if (v instanceof LocalDate) {
            String s = ((LocalDate) v).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return quoteString(s, dbType, hasNonAscii(s));
        }
        if (v instanceof LocalDateTime) {
            String s = ((LocalDateTime) v).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return quoteString(s, dbType, hasNonAscii(s));
        }
        if (v instanceof OffsetDateTime) {
            String s = ((OffsetDateTime) v).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return quoteString(s, dbType, hasNonAscii(s));
        }
        if (v instanceof Timestamp) {
            String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Timestamp) v);
            return quoteString(s, dbType, hasNonAscii(s));
        }

        String s = String.valueOf(v);
        return quoteString(s, dbType, hasNonAscii(s));
    }

    private static String quoteString(String s, String dbType, boolean nonAscii) {
        String esc = s.replace("'", "''");
        if (StringUtils.equalsIgnoreCase(DbType.SQL_SERVER.getDb(), dbType) && nonAscii) {
            return "N'" + esc + "'";
        }
        return "'" + esc + "'";
    }

    private static boolean hasNonAscii(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) > 127) return true;
        }
        return false;
    }
}
