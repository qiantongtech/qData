package tech.qiantong.qdata.quality.service.quality.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import org.springframework.data.mongodb.core.MongoTemplate;
import tech.qiantong.qdata.common.httpClient.HttpTaskLogger;
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.quality.dal.dataobject.quality.CheckErrorData;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityCheckResult;
import tech.qiantong.qdata.quality.dal.dataobject.quality.QualityRuleEntity;
import tech.qiantong.qdata.quality.service.qa.IDppEvaluateLogService;
import tech.qiantong.qdata.quality.utils.quality.MongoUtil;
import tech.qiantong.qdata.quality.utils.quality.QualitySqlGenerateFactory;
import tech.qiantong.qdata.quality.utils.quality.QualitySqlGenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class RuleExecutorTask implements Callable<QualityCheckResult> {

    private final QualityRuleEntity rule;
    private final String batch;
    private final DbQuery dbQuery;;
    private final QualitySqlGenerateFactory sqlFactory;
    private final MongoTemplate mongoTemplate;
    private final HttpTaskLogger logger;
    private final IDppEvaluateLogService iDppEvaluateLogService;

    public RuleExecutorTask(QualityRuleEntity rule, String batch,
                            DbQuery dbQuery,
                            QualitySqlGenerateFactory sqlFactory,
                            MongoTemplate mongoTemplate,
                            HttpTaskLogger logger,
                            IDppEvaluateLogService iDppEvaluateLogService) {
        this.rule = rule;
        this.batch = batch;
        this.dbQuery = dbQuery;
        this.sqlFactory = sqlFactory;
        this.mongoTemplate = mongoTemplate;
        this.logger = logger;
        this.iDppEvaluateLogService = iDppEvaluateLogService;
    }

    @Override
    public QualityCheckResult call() {
        try {
            DppEvaluateLogSaveReqVO createReqVO = new DppEvaluateLogSaveReqVO(rule);
            logger.log("质量任务-开始执行规则，规则ID：" + rule.getId() + "，规则类型：" + rule.getRuleType());
            // 1. 生成 SQL 脚本（策略模式）
            logger.log("质量任务-获取 SQL 生成器并生成 SQL 脚本。");
            QualitySqlGenerator generator = sqlFactory.getGenerator(rule.getRuleType());
            String checkSql = generator.generateSql(rule);
            String errorSql = generator.generateErrorSql(rule);
            logger.log("质量任务-SQL 生成完成，CheckSQL：" + checkSql + "，ErrorSQL：" + errorSql);

            // 2. 执行 SQL
            try (Connection conn = getConn(rule,dbQuery).getConnection();
                 Statement stmt = conn.createStatement()) {

                logger.log("质量任务-开始执行校验 SQL。");
                // 查询异常数 & 总数
                int errorCount = 0;
                int totalCount = 0;
                try (ResultSet rs = stmt.executeQuery(checkSql)) {
                    if (rs.next()) {
//                        errorCount = rs.getInt(1);
//                        totalCount = rs.getInt(2);
                        totalCount = ((Number) rs.getObject("totalCount")).intValue();
                        errorCount = ((Number) rs.getObject("errorCount")).intValue();
                    }
                }
                logger.log("质量任务-校验 SQL 执行完成，异常数：" + errorCount + "，总数：" + totalCount);
                // 查询异常明细
                logger.log("质量任务-开始查询错误明细数据。");
                List<JSONObject> errorList = new ArrayList<>();
                try (ResultSet rs = stmt.executeQuery(errorSql)) {
                    while (rs.next()) {
                        JSONObject row = new JSONObject();
                        for (String col : rule.getShowErrorColumns()) {
                            row.put(col, rs.getObject(col));
                        }
                        errorList.add(row);
                    }
                }

                logger.log("质量任务-错误明细数据查询完成，共：" + errorList.size() + " 条。");

                createReqVO.setTotal((long)totalCount);
                createReqVO.setProblemTotal((long)errorCount);
                Long dppEvaluateLog = iDppEvaluateLogService.createDppEvaluateLog(createReqVO);

                // 保存 Mongo 错误
                logger.log("质量任务-开始写入错误数据至 MongoDB。");
                for (JSONObject obj : errorList) {
                    CheckErrorData doc = CheckErrorData.builder()
                            .reportId(String.valueOf(dppEvaluateLog))
//                            .reportId(rule.getId())
                            .dataJsonStr(obj.toJSONString())
                            .dataJsonStrOLd(obj.toJSONString())
                            .jsonData(obj)
                            .jsonDataOld(obj)
                            .count(totalCount)
                            .errorCount(errorCount)
                            .time(new Date())
                            .repair(0)
                            .remark("")
                            .build();
                    MongoUtil.safeSave(mongoTemplate, doc, "quality_error_data");
                }
                logger.log("质量任务-MongoDB 写入完成。");
                // 构建返回
                logger.log("质量任务-规则执行完成，构建结果对象返回。");
                return new QualityCheckResult(rule.getId(), batch, errorCount, totalCount);
            }
        } catch (Exception e) {
            logger.log("质量任务-规则执行异常，规则ID：" + rule.getId() + "，错误信息：" + e.getMessage());
            e.printStackTrace();
            return new QualityCheckResult(rule.getId(), batch, e.getMessage());
        }
    }

    private DbQuery getConn(QualityRuleEntity rule, DbQuery dbQuery) {







        return dbQuery;
    }

}
