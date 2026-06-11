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

package tech.qiantong.qdata.module.system.domain;

import tech.qiantong.qdata.common.utils.StringUtils;

/**
 * 缓存信息
 *
 * @author qdata
 */
public class SysCache
{
    /** 缓存名称 */
    private String cacheName = "";

    /** 缓存键名 */
    private String cacheKey = "";

    /** 缓存内容 */
    private String cacheValue = "";

    /** 备注 */
    private String remark = "";

    public SysCache()
    {

    }

    public SysCache(String cacheName, String remark)
    {
        this.cacheName = cacheName;
        this.remark = remark;
    }

    public SysCache(String cacheName, String cacheKey, String cacheValue)
    {
        this.cacheName = StringUtils.replace(cacheName, ":", "");
        this.cacheKey = StringUtils.replace(cacheKey, cacheName, "");
        this.cacheValue = cacheValue;
    }

    public String getCacheName()
    {
        return cacheName;
    }

    public void setCacheName(String cacheName)
    {
        this.cacheName = cacheName;
    }

    public String getCacheKey()
    {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey)
    {
        this.cacheKey = cacheKey;
    }

    public String getCacheValue()
    {
        return cacheValue;
    }

    public void setCacheValue(String cacheValue)
    {
        this.cacheValue = cacheValue;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}
