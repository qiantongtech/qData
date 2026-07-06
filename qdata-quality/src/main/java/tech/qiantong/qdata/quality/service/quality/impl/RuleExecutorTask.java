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

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import org.springframework.data.mongodb.core.MongoTemplate;
import tech.qiantong.qdata.common.httpClient.HttpTaskLogger;
import tech.qiantong.qdata.common.utils.MessageUtils;
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
            logger.log(MessageUtils.messageEn("quality.log.rule.exec.start", rule.getId(), rule.getRuleType()));
            // 1. 生成 SQL 脚本（策略模式）
            logger.log(MessageUtils.messageEn("quality.log.sql.generating"));
            QualitySqlGenerator generator = sqlFactory.getGenerator(rule.getRuleType());
            String checkSql = generator.generateSql(rule);
            String errorSql = generator.generateErrorSql(rule);
            logger.log(MessageUtils.messageEn("quality.log.sql.generated", checkSql, errorSql));

            // 2. 执行 SQL
            try (Connection conn = getConn(rule,dbQuery).getConnection();
                 Statement stmt = conn.createStatement()) {

                logger.log(MessageUtils.messageEn("quality.log.sql.executing"));
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
                logger.log(MessageUtils.messageEn("quality.log.sql.executed", errorCount, totalCount));
                // 查询异常明细
                logger.log(MessageUtils.messageEn("quality.log.error.query"));
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

                logger.log(MessageUtils.messageEn("quality.log.error.count", errorList.size()));

                createReqVO.setTotal((long)totalCount);
                createReqVO.setProblemTotal((long)errorCount);
                Long dppEvaluateLog = iDppEvaluateLogService.createDppEvaluateLog(createReqVO);

                // 保存 Mongo 错误
                logger.log(MessageUtils.messageEn("quality.log.mongo.writing"));
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
                logger.log(MessageUtils.messageEn("quality.log.mongo.done"));
                // 构建返回
                logger.log(MessageUtils.messageEn("quality.log.result.build"));
                return new QualityCheckResult(rule.getId(), batch, errorCount, totalCount);
            }
        } catch (Exception e) {
            logger.log(MessageUtils.messageEn("quality.log.rule.exception", rule.getId(), e.getMessage()));
            e.printStackTrace();
            return new QualityCheckResult(rule.getId(), batch, e.getMessage());
        }
    }

    private DbQuery getConn(QualityRuleEntity rule, DbQuery dbQuery) {







        return dbQuery;
    }

}
