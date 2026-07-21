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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates NamedParameterSql.
 */
public class ParsedSql implements Serializable {

    private static final long serialVersionUID=1L;

    private String originalSql;
    //Parameter name.
    private List<String> paramNames = new ArrayList<>();
    //Parameter positions in the SQL.
    private List<int[]> paramIndexs = new ArrayList<>();
    //Counts distinct parameters.
    private int namedParamCount;
    //Counts question-mark placeholders in the SQL.
    private int unnamedParamCount;

    private int totalParamCount;

    public ParsedSql(String originalSql){
        this.originalSql = originalSql;
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public void addParamNames(String paramName,int startIndex,int endIndex) {
        paramNames.add(paramName);
        paramIndexs.add(new int[]{startIndex,endIndex});
    }

    public int[] getParamIndexs(int position) {
        return paramIndexs.get(position);
    }

    public String getOriginalSql() {
        return originalSql;
    }

    public int getNamedParamCount() {
        return namedParamCount;
    }

    public void setNamedParamCount(int namedParamCount) {
        this.namedParamCount = namedParamCount;
    }

    public int getUnnamedParamCount() {
        return unnamedParamCount;
    }

    public void setUnnamedParamCount(int unnamedParamCount) {
        this.unnamedParamCount = unnamedParamCount;
    }

    public int getTotalParamCount() {
        return totalParamCount;
    }

    public void setTotalParamCount(int totalParamCount) {
        this.totalParamCount = totalParamCount;
    }

    @Override
    public String toString() {
        return "ParsedSql{" +
                "originalSql='" + originalSql + '\'' +
                ", paramNames=" + paramNames +
                ", paramIndexs=" + paramIndexs +
                ", namedParamCount=" + namedParamCount +
                ", unnamedParamCount=" + unnamedParamCount +
                ", totalParamCount=" + totalParamCount +
                '}';
    }
}
