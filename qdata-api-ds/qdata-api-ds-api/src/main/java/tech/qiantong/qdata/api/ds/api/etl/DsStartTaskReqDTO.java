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

package tech.qiantong.qdata.api.ds.api.etl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <P>
 * 用途:手动启动一次的参数
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-26 18:12
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DsStartTaskReqDTO {
    /**
     * 流程编码
     */
    private Long processDefinitionCode;
    /**
     * 失败策略 写死 CONTINUE
     * CONTINUE 为继续
     */
    private String failureStrategy;
    /**
     * 写死 NONE
     */
    private String warningType;
    /**
     * 写死 MEDIUM
     */
    private String processInstancePriority;
    /**
     * 写死{"complementStartDate":"当前天 00:00:00","complementEndDate":"当前天 00:00:00"}
     * 例子{"complementStartDate":"2025-03-26 00:00:00","complementEndDate":"2025-03-26 00:00:00"}
     */
    private String scheduleTime;//: {"complementStartDate":"2025-03-26 00:00:00","complementEndDate":"2025-03-26 00:00:00"}
}
