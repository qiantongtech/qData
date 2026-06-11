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

package tech.qiantong.qdata.module.da.dal.dataobject.discovery;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import tech.qiantong.qdata.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 数据发现节点实例-日志 DO 对象 DA_DISCOVERY_LOG_BODY
 *
 * @author qdata
 * @date 2025-10-15
 */
@Data
@TableName(value = "DA_DISCOVERY_LOG_BODY")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DaDiscoveryLogBodyDO extends BaseEntity {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /** 时间; 日志入库的时间 */
    private Date tm;

    /** 任务id */
    private Long taskId;

    /** 日志内容 */
    private String logContent;

    /** 是否有效;0：无效，1：有效 */
    private Boolean validFlag;

    /** 删除标志;1：已删除，0：未删除 */
    @TableLogic
    private Boolean delFlag;

    /** 创建人 */
    private String createBy;

    /** 创建人id */
    private Long creatorId;

    /** 创建时间 */
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新人id */
    private Long updaterId;

    /** 更新时间 */
    private Date updateTime;

    /** 备注 */
    private String remark;
}
