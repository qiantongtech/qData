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

package tech.qiantong.qdata.module.dpp.service.etl.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceLogDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlTaskInstanceLogMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskInstanceLogService;

import javax.annotation.Resource;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Data integration task instance - Log Service business layer processing
 *
 * @author qdata
 * @date 2025-08-05
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlTaskInstanceLogServiceImpl extends ServiceImpl<DppEtlTaskInstanceLogMapper, DppEtlTaskInstanceLogDO> implements IDppEtlTaskInstanceLogService {
    /**
     * Log-only placeholder pool used to protect passwords.
     * <p>
     * These values replace plaintext passwords immediately before log persistence. They are never used by
     * DataX or Spark for data source connections and cannot be used to recover the original password.
     * A placeholder is selected randomly so that real passwords are never stored in integration logs.
     */
    private static final String[] LOG_PASSWORD_PLACEHOLDERS = {
            "U2FsdGVkX18DA5YXOxtkrjnAmL2zDqdN7P8yjxggvos=",
            "U2FsdGVkX1+oAoyFtI8RPvKmb5f/vHcn3c5kYrNnBuw=",
            "U2FsdGVkX18RGIgTzxDE+4wqIhCZvlQ42yPozzodUtY=",
            "U2FsdGVkX1+0sqXbf9hJ/ftf/dKzhdiF3Jb6ZIB+D60=",
            "U2FsdGVkX1+7JUt8fNMkW8O0KgenjhRRJOgoXTkUMAo="
    };

    /**
     * Matches password fields in JSON fragments embedded in log text.
     * Supports case-insensitive field names, whitespace around the colon, and escaped characters in string values.
     * Only the field value is replaced, preserving the original JSON structure.
     */
    private static final Pattern JSON_PASSWORD_PATTERN = Pattern.compile(
            "(\\\"password\\\"\\s*:\\s*\\\")((?:\\\\.|[^\\\"\\\\])*)(\\\")",
            Pattern.CASE_INSENSITIVE);

    @Resource
    private DppEtlTaskInstanceLogMapper dppEtlTaskInstanceLogMapper;

    @Override
    public boolean saveOrUpdate(DppEtlTaskInstanceLogDO entity) {
        // DataX and Spark integration logs are persisted here, so passwords are protected without changing runtime parameters.
        entity.setLogContent(replaceJsonPasswords(entity.getLogContent()));
        DppEtlTaskInstanceLogDO old = this.getOne(Wrappers.lambdaQuery(DppEtlTaskInstanceLogDO.class)
                .eq(DppEtlTaskInstanceLogDO::getTaskInstanceId, entity.getTaskInstanceId()));
        if (old != null) {
            old.setTm(entity.getTm());
            old.setLogContent(entity.getLogContent());
            return this.update(old, Wrappers.lambdaUpdate(DppEtlTaskInstanceLogDO.class)
                    .eq(DppEtlTaskInstanceLogDO::getTaskInstanceId, entity.getTaskInstanceId()));
        } else {
            return this.save(entity);
        }
    }

    /**
     * Protects JSON password fields immediately before the complete log content is persisted.
     *
     * @param logContent complete log content to be stored
     * @return log content with each password value replaced by a randomly selected protective placeholder
     */
    private static String replaceJsonPasswords(String logContent) {
        if (logContent == null || logContent.isEmpty()) {
            return logContent;
        }
        Matcher matcher = JSON_PASSWORD_PATTERN.matcher(logContent);
        StringBuffer result = new StringBuffer(logContent.length());
        while (matcher.find()) {
            String placeholder = LOG_PASSWORD_PLACEHOLDERS[
                    ThreadLocalRandom.current().nextInt(LOG_PASSWORD_PLACEHOLDERS.length)];
            matcher.appendReplacement(result, Matcher.quoteReplacement(
                    matcher.group(1) + placeholder + matcher.group(3)));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public boolean saveOrUpdateRealtime(DppEtlTaskInstanceLogDO entity) {
        return saveOrUpdate(entity);
    }

    @Override
    public String getLog(Long taskInstanceId) {
        DppEtlTaskInstanceLogDO dppEtlTaskInstanceLogDO = this.getOne(Wrappers.lambdaQuery(DppEtlTaskInstanceLogDO.class)
                .eq(DppEtlTaskInstanceLogDO::getTaskInstanceId, taskInstanceId));
        if (dppEtlTaskInstanceLogDO != null) {
            return dppEtlTaskInstanceLogDO.getLogContent();
        }
        return null;
    }
}
