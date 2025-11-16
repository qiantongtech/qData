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
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
