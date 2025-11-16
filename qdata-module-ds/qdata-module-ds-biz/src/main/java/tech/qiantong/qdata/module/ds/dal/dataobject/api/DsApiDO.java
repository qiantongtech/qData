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

package tech.qiantong.qdata.module.ds.dal.dataobject.api;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;
import tech.qiantong.qdata.module.ds.dal.dataobject.dto.ReqParam;
import tech.qiantong.qdata.module.ds.dal.dataobject.dto.ResParam;

import java.util.List;

/**
 * API服务 DO 对象 DS_API
 *
 * @author lhs
 * @date 2025-02-12
 */
@Data
@TableName(value = "DS_API")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
// @KeySequence("DS_API_seq")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DsApiDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 类目id */
    private Long catId;

    /** 类目编码 */
    private String catCode;

    /** 类目名称 */
    @TableField(exist = false)
    private String catName;

    /** API服务名称 */
    private String name;

    /** API版本 */
    private String apiVersion;

    /** API路径 */
    private String apiUrl;

    /** 请求方式 */
    private String reqMethod;

    /** 服务提供类型 */
    private String apiServiceType;

    /** 返回结果类型
     * 1-详情、2-列表、3-分页
     * */
    private String resDataType;

    /** IP黑名单多个，隔开 */
    private String denyIp;

    /** 执行配置JSON */
    private String configJson;

    /** 限流配置JSON */
    private String limitJson;

    /**
     *转发类型;1:API 2:地理空间数据'
     */
    private String transmitType;

    /**
     *apiId
     */
    private String apiId;

    /**
     *Header配置json
     */
    private String headerJson;

    /** 请求参数 */
    @TableField(value = "REQ_PARAMS", exist = false, typeHandler = JacksonTypeHandler.class)
    private List<ReqParam> reqParamsList;

    /** 返回参数 */
    @TableField(value = "RES_PARAMS",exist = false, typeHandler = JacksonTypeHandler.class)
    private List<ResParam> resParamsList;

    private String resParams;

    private String reqParams;

    /** 描述 */
    private String description;

    /** 状态 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;


    @TableField(exist = false)
    private ExecuteConfig executeConfig;


    /**
     * 是否开启缓存 0:否 1:是
     */
    @TableField(exist = false)
    private String cacheSwitch;


    //写个方法将resParams、reqParams转换成reqParamsList、resParamsList
    public  void setResParamsList() {
        if (this.resParams != null) {
            this.resParamsList = JSONArray.parseArray(this.resParams, ResParam.class);
        }
        if (this.reqParams != null) {
            this.reqParamsList = JSONArray.parseArray(this.reqParams, ReqParam.class);
        }
    }

}
