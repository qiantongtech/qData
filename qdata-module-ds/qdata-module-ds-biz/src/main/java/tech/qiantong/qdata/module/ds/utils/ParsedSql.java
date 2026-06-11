/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.ds.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 此类封装NamedParameterSql
 */
public class ParsedSql implements Serializable {

    private static final long serialVersionUID=1L;

    private String originalSql;
    //参数名
    private List<String> paramNames = new ArrayList<>();
    //参数在sql中对应的位置
    private List<int[]> paramIndexs = new ArrayList<>();
    //统计参数个数（不包含重复）
    private int namedParamCount;
    //统计sql中？的个数
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
