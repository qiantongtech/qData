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
import tech.qiantong.qdata.common.utils.MessageUtils;
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
import tech.qiantong.qdata.quality.utils.quality.enums.CharacterValidationGenerator;
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
    // Local file path prefix
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
            throw new ServiceException("quality.error.task.running", "Previous task is still running, please try again later");
        }
        redisService.set(key, "1", 1200);

        qualityTaskExecutor.submit(() -> executeQualityTask(taskId,key));
    }

    public void executeQualityTask(String taskId,String key) {

        // Create a TaskLogger instance and specify the folder and file name where the log file is stored.
        String tmpFilePath = "taskLog-" + IdUtil.simpleUUID() + ".txt";
        HttpTaskLogger logger = new HttpTaskLogger(prefixUrl, tmpFilePath);
        logger.log(MessageUtils.messageEn("quality.log.task.start"));

        //1. Query basic information of the task
        DppQualityTaskRespVO dppQualityTaskById = iDppQualityTaskService.getDppQualityTaskById(JSONUtils.convertToLong(taskId));
        if(dppQualityTaskById == null){
            logger.log(MessageUtils.messageEn("quality.log.task.query.empty"));
            logger.log(MessageUtils.messageEn("quality.log.task.end"));
            // After the task is completed, close the logger and release resources
            logger.close();
            redisService.set(key, "3", 300);
            return;
        }

        logger.log(MessageUtils.messageEn("quality.log.task.info", dppQualityTaskById.toString()));
        //2. Generate this batch number
        String batch = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        logger.log(MessageUtils.messageEn("quality.log.batch.generate", batch));

        //Get file address path
        String filePath = logger.getFilePath();
        //Create a log for this task. Create the log first and then execute it.
        DppQualityLogSaveReqVO dppQualityLogSaveReqVO = new DppQualityLogSaveReqVO(dppQualityTaskById);
        dppQualityLogSaveReqVO.setPath(filePath);
        Long taskLogId = iDppQualityLogService.createDppQualityLog(dppQualityLogSaveReqVO);
        logger.log(MessageUtils.messageEn("quality.log.log.created", taskLogId));

        // 3. Query the list of data sources required for this task
        logger.log(MessageUtils.messageEn("quality.log.datasource.list.start"));
        List<DppQualityTaskObjDO> qualityTaskObjDOList = iDppQualityTaskObjService.getDppQualityTaskObjList(taskId);
        if (CollectionUtils.isEmpty(qualityTaskObjDOList)) {
            logger.log(MessageUtils.messageEn("quality.log.datasource.list.empty"));
            logger.log(MessageUtils.messageEn("quality.log.task.end"));
            updateQualityLog(taskLogId,"1");
            // After the task is completed, close the logger and release resources
            logger.close();
            redisService.set(key, "3", 300);
            return;
        }
        logger.log(MessageUtils.messageEn("quality.log.datasource.list.count", qualityTaskObjDOList.size()));

        logger.log(MessageUtils.messageEn("quality.log.group.start"));
        //4. Group according to data source ID, no need to repeatedly create data source links
        Map<Long, List<Long>> groupIdsByDatasourceId = groupIdsByDatasourceId(qualityTaskObjDOList);
        logger.log(MessageUtils.messageEn("quality.log.group.end", groupIdsByDatasourceId.size()));
        for (Map.Entry<Long, List<Long>> entry : groupIdsByDatasourceId.entrySet()) {
            Long datasourceId = entry.getKey();          // I.e. daDatasourceById
            List<Long> idList = entry.getValue();        // id collection

            logger.log(MessageUtils.messageEn("quality.log.datasource.connecting"));
            //5. Obtain basic information about data sources
            DaDatasourceDO daDatasourceById = iDaDatasourceQualityService.getDaDatasourceById(datasourceId);
            if (daDatasourceById == null){
                logger.log(MessageUtils.messageEn("quality.log.datasource.fetch.fail", datasourceId));
                logger.log(MessageUtils.messageEn("quality.log.rule.skip.by.datasource", datasourceId));
                continue;
            }
            logger.log(MessageUtils.messageEn("quality.log.datasource.fetch.success", daDatasourceById.getDatasourceName(), daDatasourceById.getId()));
            //6. Test whether the data source is normal. If not, end and proceed to the next step.
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
                    logger.log(MessageUtils.messageEn("quality.log.datasource.connection.fail"));
                    logger.log(MessageUtils.messageEn("quality.log.rule.skip.by.name", daDatasourceById.getDatasourceName(), daDatasourceById.getId()));
                    continue;
                }
            }catch (Exception e){
                logger.log(MessageUtils.messageEn("quality.log.datasource.connection.error", e.getMessage()));
                logger.log(MessageUtils.messageEn("quality.log.datasource.connection.error.skip"));
                logger.log(MessageUtils.messageEn("quality.log.rule.skip.by.name", daDatasourceById.getDatasourceName(), daDatasourceById.getId()));
                continue;
            }
            logger.log(MessageUtils.messageEn("quality.log.datasource.connection.success"));

            logger.log(MessageUtils.messageEn("quality.log.rules.fetch"));
            //7. Retrieve rules from the data source for rule query
            List<DppQualityTaskEvaluateDO> dppQualityTaskEvaluateList = iDppQualityTaskEvaluateService.getDppQualityTaskEvaluateList(idList);

            logger.log(MessageUtils.messageEn("quality.log.rules.process"));
            //Round robin rules
            for (DppQualityTaskEvaluateDO dppQualityTaskEvaluateDO : dppQualityTaskEvaluateList) {
                //8. Encapsulation rule retrieval method
                QualityRuleEntity qualityRuleEntity = new QualityRuleEntity(dppQualityTaskEvaluateDO);
                logger.log(MessageUtils.messageEn("quality.log.rule.prepare"));
                qualityRuleEntity.setTaskLogId(taskLogId);
                List<DbColumn> tableColumns = dbQuery.getTableColumns(dbQueryProperty, qualityRuleEntity.getTableName());
                if(CollectionUtils.isEmpty(tableColumns)){
                    logger.log(MessageUtils.messageEn("quality.log.rule.columns.fail"));
                    continue;
                }
                List<String> showErrorColumns = tableColumns.stream()
                        .map(DbColumn::getColName)
                        .collect(Collectors.toList());
                //Store parameters
                qualityRuleEntity.setShowErrorColumns(showErrorColumns);
                qualityRuleEntity.setTaskLogId(taskLogId);
                qualityRuleEntity.setDaDatasourceById(daDatasourceById);
                logger.log(MessageUtils.messageEn("quality.log.rule.ready"));
                try {
                    RuleExecutorTask ruleExecutorTask = new RuleExecutorTask(qualityRuleEntity, batch,dbQuery, qualitySqlGenerateFactory, mongoTemplate,logger, iDppEvaluateLogService);
                    logger.log(MessageUtils.messageEn("quality.log.rule.executing"));
                    ruleExecutorTask.call();
                }catch (Exception e){
                    logger.log(MessageUtils.messageEn("quality.log.rule.execute.fail"));
                    logger.log(e.getMessage());
                    continue;
                }
                logger.log(MessageUtils.messageEn("quality.log.rule.done"));
            }
        }
        //Update and improve log
        updateQualityLog(taskLogId,"0");
        logger.log(MessageUtils.messageEn("quality.log.task.end"));
        // After the task is completed, close the logger and release resources
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
     * Update data quality log status (only update successFlag and end time)
     * @param id log ID
     * @param successFlag status flag (1: success, 2: failure)
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
// // 1. Query task details (simulation or from task table)
// // Contains data source information, table names, task batches, etc.
//        String batch = DateUtil.format(new Date(), "yyyyMMddHHmmss");
//
// // 2. Query quality rules (executable)
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
// //Save the result report, expandable logic
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
            throw new ServiceException("db.error.datasource.realtime.fail", "Failed to establish real-time datasource connection!");
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
                throw new DataQueryException("db.error.datasource.realtime.fail", "Failed to establish real-time datasource connection!");            }
        }catch (Exception e){
            throw new DataQueryException("db.error.datasource.realtime.error", "Error establishing real-time datasource connection!");
        }
        List<DbColumn> tableColumns = dbQuery.getTableColumns(dbQueryProperty, queryReqDTO.getTableName());
        List<String> showErrorColumns = tableColumns.stream()
                .map(DbColumn::getColName)
                .collect(Collectors.toList());
        ValidationSqlResult validationSqlResult = new ValidationSqlResult();
        //Store parameters
        validationSqlResult.setShowErrorColumns(tableColumns);

        QualitySqlGenerator generator = qualitySqlGenerateFactory.getGenerator(queryReqDTO.getRuleType());
        QualityRuleEntity rule = new QualityRuleEntity(queryReqDTO);
        rule.setShowErrorColumns(showErrorColumns);
        rule.setDaDatasourceById(daDatasourceById);
        String checkSql = generator.generateValidDataSql(rule,queryReqDTO.getLimit(),queryReqDTO.getOffset());
        // 2. Execute SQL
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
            throw new DataQueryException("db.error.datasource.realtime.fail", "Failed to establish real-time datasource connection!");
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
            throw new ServiceException("db.error.datasource.realtime.fail", "Failed to establish real-time datasource connection!");
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
                throw new DataQueryException("db.error.datasource.realtime.fail", "Failed to establish real-time datasource connection!");            }
        }catch (Exception e){
            throw new DataQueryException("db.error.datasource.realtime.error", "Error establishing real-time datasource connection!");
        }
        List<DbColumn> tableColumns = dbQuery.getTableColumns(dbQueryProperty, queryReqDTO.getTableName());
        List<String> showErrorColumns = tableColumns.stream()
                .map(DbColumn::getColName)
                .collect(Collectors.toList());
        ValidationSqlResult validationSqlResult = new ValidationSqlResult();
        validationSqlResult.setShowErrorColumns(tableColumns);

        QualitySqlGenerator generator = qualitySqlGenerateFactory.getGenerator(queryReqDTO.getRuleType());
        QualityRuleEntity rule = new QualityRuleEntity(queryReqDTO);
        //Store parameters
        rule.setShowErrorColumns(showErrorColumns);
        rule.setDaDatasourceById(daDatasourceById);
        String checkSql = generator.generateErrorSql(rule);
        // 2. Execute SQL
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
            throw new DataQueryException("db.error.datasource.realtime.fail", "Failed to establish real-time datasource connection!");
        }
        return validationSqlResult;
    }

//    @Override
//    public Page<CheckErrorData> pageErrorData(PageRequest of,  CheckErrorDataReqDTO checkErrorDataReqDTO) {
//        CheckErrorData person =  this.convertFrom(checkErrorDataReqDTO);
//        Example<CheckErrorData> example = Example.of(person);
//        Page<CheckErrorData> page = checkErrorDataRepository.findAll(example, of);
// System.out.println("Total number of items: " + page.getTotalElements());
// System.out.println("Total number of pages: " + page.getTotalPages());
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


        // data_json subfield conditions
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
        // Pagination information
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
            case "1": // Modify data

                if (dto.getUpdatedData() != null && !dto.getUpdatedData().isEmpty()) {
                    boolean success = updatePhysicalTable(dto);
                    if (!success) return false;

                    //Continue if successful, return if unsuccessful
                    update.set("data_json",JSONObject.toJSONString(dto.getUpdatedData()));
                    Map<String, Object> oldData = dto.getOldData();
                    String dataJsonStr = MapUtils.getString(oldData, "dataJsonStr");
                    Object jsonData = MapUtils.getObject(oldData, "jsonData");
                    String id = MapUtils.getString(oldData, "id");
                    query.addCriteria(Criteria.where("id").is(id)); // Mongo primary key
                    // Synchronously modify old data fields
                    update.set("data_json_old",dataJsonStr);
                    update.set("json_data_old",jsonData);
                    update.set("repair", "1");
                }
                break;

            case "2": // Modify remarks
                if (dto.getId() == null) {
                    return false;
                }
                query.addCriteria(Criteria.where("id").is(dto.getId())); // Mongo primary key
                update.set("remark", dto.getRemark() == null ? "" : dto.getRemark());
                break;

            case "3": // Modify status (repair field)
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

        // Get the jsonData field in oldData (Map<String, Object>)
        Object jsonDataObj = oldDataMap.get("jsonData");
        if (!(jsonDataObj instanceof Map)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> whereData = (Map<String, Object>) jsonDataObj;

        // Table name (prerequisite: whitelist verification has been done)
        String fullTable = buildFullTableName(dbQueryProperty,dto.getTableName());

// // SET clause
//        StringBuilder setClause = new StringBuilder();
//        for (Map.Entry<String, Object> entry : updatedData.entrySet()) {
//            if (setClause.length() > 0) setClause.append(", ");
//            setClause.append("`").append(entry.getKey()).append("` = ")
//                    .append(toSqlLiteral(entry.getValue()));
//        }
//
// // WHERE clause (null -> IS NULL; collection -> IN(...))
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
// // if toSqlLiteral is static: use lambda to avoid this:: error
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
            if (v instanceof BigDecimal) {
                return ((BigDecimal) v).toPlainString();
            }
            return v.toString();
        }
        if (v instanceof Boolean) {
            return ((Boolean) v) ? "1" : "0";
        }
        if (v instanceof LocalDate) {
            return "'" + v.toString() + "'";
        }
        if (v instanceof LocalDateTime) {
            return "'" + v.toString().replace('T', ' ') + "'";
        }
        if (v instanceof Date) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "'" + fmt.format((Date) v) + "'";
        }
        // Defaults to string
        return "'" + escapeSql(v.toString()) + "'";
    }
    private static String escapeSql(String s) {
        // Single quote -> two single quotes; backslash -> double backslash (if NO_BACKSLASH_ESCAPES is turned on, only single quotes can be replaced)
        return s.replace("\\", "\\\\").replace("'", "''");
    }

    /**
     * Get the full table name
     * @param dbQueryProperty data source configuration
     * @return full table name
     */
    public static String buildFullTableName(DbQueryProperty dbQueryProperty, String tableName ) {
        String dbType = dbQueryProperty.getDbType();
        String dbName = dbQueryProperty.getDbName();
        String sid = dbQueryProperty.getSid();

        if (StringUtils.equals(DbType.SQL_SERVER.getDb(), dbType)) {
            // SQL Server splicing method: dbName.sid.tableName
            if (StringUtils.isNotBlank(dbName) && StringUtils.isNotBlank(sid)) {
                return dbName + "." + sid + "." + tableName;
            } else if (StringUtils.isNotBlank(dbName)) {
                return dbName + ".." + tableName; // Just spell the database name
            } else {
                return tableName; // Only name
            }
        } else {
            // Default MySQL and others, use backticks
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

    // ========== Identifier quotes ==========
    private static String openQuote(String dbType) {
        return StringUtils.equalsIgnoreCase(DbType.SQL_SERVER.getDb(), dbType) ? "[" : "`";
    }
    private static String closeQuote(String dbType) {
        return StringUtils.equalsIgnoreCase(DbType.SQL_SERVER.getDb(), dbType) ? "]" : "`";
    }

    // ========== Convert value to SQL literal ==========
    private static String toSqlLiteral(Object v, String dbType) {
        if (v == null) return "NULL";

        if (v instanceof Number) {
            return (v instanceof BigDecimal) ? ((BigDecimal) v).toPlainString() : String.valueOf(v);
        }
        if (v instanceof Boolean) {
            return (Boolean) v ? "1" : "0";
        }
        if (v instanceof Date) {
            String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) v);
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



    @Override
    public String generateDataCheck(QualityRuleQueryReqDTO queryReqDTO) {
        DaDatasourceDO daDatasourceById = iDaDatasourceQualityService.getDaDatasourceById(JSONUtils.convertToLong(queryReqDTO.getDataId()));
        if (daDatasourceById == null) {
            throw new ServiceException("db.error.datasource.realtime.fail", "Failed to establish real-time datasource connection!");
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
                throw new DataQueryException("db.error.datasource.realtime.fail", "Failed to establish real-time datasource connection!");
            }
        } catch (Exception e) {
            throw new DataQueryException("db.error.datasource.realtime.error", "Error establishing real-time datasource connection!");
        }

        CharacterValidationGenerator characterValidationGenerator = new CharacterValidationGenerator();


        QualityRuleEntity rule = new QualityRuleEntity(queryReqDTO);
        rule.setDaDatasourceById(daDatasourceById);
        String checkSql = characterValidationGenerator.generateDataCheckSql(rule, queryReqDTO.getInputValue());

        // Execute SQL and get the first row and first column (0/1)
        try (Connection conn = dbQuery.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {


            if (rs.next()) {
                Object val = rs.getObject(1);
                // Uniformly returns the string "0" / "1"
                return val == null ? "0" : String.valueOf(val);
            }
            return "0";
        } catch (Exception e) {
            throw new DataQueryException("db.error.datasource.realtime.fail", "Failed to establish real-time datasource connection!");
        }
    }
}
