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

package tech.qiantong.qdata.module.ds.utils;
import tech.qiantong.qdata.common.exception.ServiceException;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;
import tech.qiantong.qdata.common.enums.WhereType;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.ds.dal.dataobject.dto.ReqParam;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds SQL statements dynamically.
 * ${ segment... } represents a conditional block.
 * <p>
 * String sql = "select * from user where 1=1
 * ${ and username = :username }
 * ${ and password = :password }
 * ${ and age = :age }"
 * <p>
 * Map filters = new HashMap();
 * filters.put("username", "yuwei");
 * filters.put("age", "12");
 * filters.put("id", "123");
 * <p>
 * SqlFilterResult result = SqlBuilderUtil.applyFilters(sql, filters);
 * <p>
 * Result of result.getSql().
 * select * from user where 1=1 and username=:username and age=:age
 * <p>
 * Result of result.getAcceptedFilters().
 * {username=yuwei}
 * {age=12}
 */
@Slf4j
public class SqlBuilderUtil {

    private SqlBuilderUtil() {
    }

    private static volatile SqlBuilderUtil instance;

    public static SqlBuilderUtil getInstance() {
        if (instance == null) {
            synchronized (SqlBuilderUtil.class) {
                if (instance == null) {
                    instance = new SqlBuilderUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Space character.
     */
    private final String SPACE = " ";
    /**
     * Colon placeholder.
     */
    private final String COLON = ":";
    /**
     * Question-mark placeholder.
     */
    private final String MARK = "?";
    /**
     * WHERE keyword.
     */
    private final String WHERE_SQL = "WHERE";
    /**
     * AND operator.
     */
    private final String WHERE_AND = "AND";
    /**
     * WHERE 1=1 condition.
     */
    private final String WHERE_INIT = WHERE_SQL + " 1 = 1";
    /**
     * Left parenthesis.
     */
    private final String LEFT_BRACKET = "(";
    /**
     * Right parenthesis.
     */
    private final String RIGHT_BRACKET = ")";
    /**
     * Percent sign.
     */
    private final String PERCENT_SIGN = "%";
    /**
     * Single quote.
     */
    private final String SINGLE_QUOTE = "'";
    /**
     * Conditional block start marker.
     */
    public final String MARK_KEY_START = "${";
    /**
     * Conditional block end marker.
     */
    public final String MARK_KEY_END = "}";

    /**
     * Builds named-parameter SQL.
     *
     * @param sql
     * @param params
     * @return
     */
    public String buildHql(String sql, List<ReqParam> params) {
        Assert.notNull(sql, "SQL must not be null");
        return buildHql(new StringBuffer(sql), params);
    }

    private String buildHql(StringBuffer sql, List<ReqParam> params) {
        if (CollUtil.isEmpty(params)) {
            return sql.toString();
        }
        sql.append(SPACE).append(WHERE_INIT);

        //Check whether params is empty to support queries without conditions.
        if (CollectionUtils.isEmpty(params)) {
            return sql.toString();
        }

        for (int i = 0; i < params.size(); i++) {
            ReqParam reqParam = params.get(i);
            sql.append(SPACE).append(MARK_KEY_START).append(WHERE_AND).append(SPACE).append(reqParam.getParamName());
            if (StringUtils.equals(WhereType.LIKE.getType(), reqParam.getWhereType())) {
                // Spaces are required around :username in LIKE '%' :username '%'; without them the query returns no data.
                sql.append(SPACE).append(WhereType.getWhereType(reqParam.getWhereType()).getKey())
                        .append(SPACE).append(SINGLE_QUOTE).append(PERCENT_SIGN).append(SINGLE_QUOTE).append(SPACE)
                        .append(COLON).append(reqParam.getParamName())
                        .append(SPACE).append(SINGLE_QUOTE).append(PERCENT_SIGN).append(SINGLE_QUOTE).append(MARK_KEY_END);
            } else if (StringUtils.equals(WhereType.LIKE_LEFT.getType(), reqParam.getWhereType())) {
                sql.append(SPACE).append(WhereType.getWhereType(reqParam.getWhereType()).getKey())
                        .append(SPACE).append(SINGLE_QUOTE).append(PERCENT_SIGN).append(SINGLE_QUOTE).append(SPACE)
                        .append(COLON).append(reqParam.getParamName()).append(MARK_KEY_END);
            } else if (StringUtils.equals(WhereType.LIKE_RIGHT.getType(), reqParam.getWhereType())) {
                sql.append(SPACE).append(WhereType.getWhereType(reqParam.getWhereType()).getKey())
                        .append(SPACE).append(COLON).append(reqParam.getParamName())
                        .append(SPACE).append(SINGLE_QUOTE).append(PERCENT_SIGN).append(SINGLE_QUOTE).append(MARK_KEY_END);
            } else if (StringUtils.equals(WhereType.NULL.getType(), reqParam.getWhereType()) || StringUtils.equals(WhereType.NOT_NULL.getType(), reqParam.getWhereType())) {
                // IS NULL and IS NOT NULL do not require parameter values.
                sql.append(SPACE).append(WhereType.getWhereType(reqParam.getWhereType()).getKey()).append(MARK_KEY_END);
            } else if (StringUtils.equals(WhereType.IN.getType(), reqParam.getWhereType())) {
                // in (:ids)
                sql.append(SPACE).append(WhereType.getWhereType(reqParam.getWhereType()).getKey())
                        .append(SPACE).append(LEFT_BRACKET)
                        .append(COLON).append(reqParam.getParamName())
                        .append(RIGHT_BRACKET).append(MARK_KEY_END);
            } else {
                sql.append(SPACE).append(WhereType.getWhereType(reqParam.getWhereType()).getKey())
                        .append(SPACE).append(COLON).append(reqParam.getParamName()).append(MARK_KEY_END);
            }
        }
        return sql.toString();
    }

    /**
     * Builds SQL dynamically from the input parameters.
     *
     * @param sql
     * @param filters
     * @return
     */
    public SqlFilterResult applyFilters(String sql, Map<String, Object> filters) {
        Assert.notNull(sql, "SQL must not be null");
        return applyFilters(new StringBuffer(sql), filters);
    }

    private SqlFilterResult applyFilters(StringBuffer sql, Map<String, Object> filters) {
        LinkedHashMap<String, Object> acceptedFilters = new LinkedHashMap<>();
        for (int i = 0, end = 0, start = sql.indexOf(MARK_KEY_START); ((start = sql.indexOf(MARK_KEY_START, end)) >= 0); i++) {
            end = sql.indexOf(MARK_KEY_END, start);
            // Builds NamedParameterSql for the conditional block.
            ParsedSql parsedSql = getSegmentParsedSql(sql, start, end);
            if (CollUtil.isEmpty(parsedSql.getParamNames())) {
                throw new ServiceException("ds.error.sql.key.missing", "Not key found in segment=" + sql.substring(start, end + MARK_KEY_END.length()));
            }
            // Check whether the input filters contain query parameters.
            if (isAcceptedKeys(filters, parsedSql.getParamNames())) {
                // Build executable SQL by removing the ${ } markers around the conditional block.
                if (log.isDebugEnabled()) {
                    log.debug("The filter namedParameters=" + parsedSql.getParamNames() + " is accepted on segment=" + sql.substring(start, end + MARK_KEY_END.length()));
                }
                // Either of the following methods can obtain the conditional block.
                // select id, name from user where 1 = 1 and id = :id
//                String segment = sql.substring(start + MARK_KEY_START.length(), end);
                String segment = parsedSql.getOriginalSql();
                // Convert named parameters to question-mark placeholders.
                // select id, name from user where 1 = 1 and id = ?
//                String segment = NamedParameterUtil.substituteNamedParams(parsedSql, filters);
                // Get named-parameter data from the input.
                LinkedHashMap<String, Object> linkAcceptedFilters = NamedParameterUtil.buildValueArray(parsedSql, filters);
                acceptedFilters.putAll(linkAcceptedFilters);
                sql.replace(start, end + MARK_KEY_END.length(), segment);
                end = start + segment.length();
            } else {
                // Discard the conditional block.
                if (log.isDebugEnabled()) {
                    log.debug("The filter namedParameters=" + parsedSql.getParamNames() + " is removed from the query on segment=" + sql.substring(start, end + MARK_KEY_END.length()));
                }
                sql.replace(start, end + MARK_KEY_END.length(), "");
                end = start;
            }
        }
        return new SqlFilterResult(sql.toString(), acceptedFilters);
    }

    /**
     * Validates input parameters and filters out empty values.
     */
    private boolean isAcceptedKeys(Map<String, Object> filters, List<String> keys) {
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = getProperty(filters, key);
            if (!isValuePopulated(value, true)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Builds NamedParameterSql for the conditional block.
     */
    private ParsedSql getSegmentParsedSql(StringBuffer sql, int start, int end) {
        String segment = sql.substring(start + MARK_KEY_START.length(), end);
        ParsedSql parsedSql = NamedParameterUtil.parseSqlStatement(segment);
        return parsedSql;
    }

    /**
     * Gets the parameter value.
     *
     * @param filters
     * @param key
     * @return
     */
    private Object getProperty(Map<String, Object> filters, String key) {
        if (MapUtil.isEmpty(filters))
            return null;
        return filters.get(key);
    }

    /**
     * Validates whether the parameter value is empty.
     *
     * @param value
     * @param isRemoveEmpty
     * @return
     */
    private boolean isValuePopulated(Object value, boolean isRemoveEmpty) {
        if (value == null) {
            return false;
        }
        if (isRemoveEmpty) {
            return ObjectUtil.isNotEmpty(value);
        } else {
            return true;
        }
    }

    public class SqlFilterResult implements Serializable {

        private static final long serialVersionUID = 1L;

        private String sql;

        private Map<String, Object> acceptedFilters;

        public SqlFilterResult(String sql, Map<String, Object> acceptedFilters) {
            this.setSql(sql);
            this.setAcceptedFilters(acceptedFilters);
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public Map<String, Object> getAcceptedFilters() {
            return acceptedFilters;
        }

        public void setAcceptedFilters(Map<String, Object> acceptedFilters) {
            this.acceptedFilters = acceptedFilters;
        }

        @Override
        public String toString() {
            return "SqlFilterResult{" +
                    "sql='" + sql + '\'' +
                    ", acceptedFilters=" + acceptedFilters +
                    '}';
        }
    }
}
