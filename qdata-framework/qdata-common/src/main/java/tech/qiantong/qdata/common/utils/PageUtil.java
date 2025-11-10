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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.common.utils;

import java.io.Serializable;

public class PageUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Integer DEFAULT_MAX_COUNT = 5000;

    // 当前页码
    private Integer pageNum = 1;
    // 分页条数
    private Integer pageSize = 20;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        if (this.pageSize > 0) {
            this.pageSize = this.pageSize > DEFAULT_MAX_COUNT ? DEFAULT_MAX_COUNT : this.pageSize;
        } else {
            this.pageSize = 20;
        }
    }

    public PageUtil(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        if (this.pageSize > 0) {
            this.pageSize = this.pageSize > DEFAULT_MAX_COUNT ? DEFAULT_MAX_COUNT : this.pageSize;
        } else {
            this.pageSize = 20;
        }
    }

    public Integer getOffset() {
        pageSize = pageSize == null ? 20 : pageSize;
        pageNum = pageNum == null ? 1 : pageNum;
        int offset = pageNum > 0 ? (pageNum - 1) * pageSize : 0;
        return offset;
    }
}
