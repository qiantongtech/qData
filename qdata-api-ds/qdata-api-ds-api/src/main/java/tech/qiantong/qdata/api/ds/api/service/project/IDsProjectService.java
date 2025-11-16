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

package tech.qiantong.qdata.api.ds.api.service.project;

import tech.qiantong.qdata.api.ds.api.project.DsProjectCreateReqDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectDeleteRespDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectRespDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectUpdateReqDTO;

/**
 * <P>
 * 用途:ds项目相关接口
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 14:26
 **/
public interface IDsProjectService {

    /**
     * 新增项目
     *
     * @param dsProjectCreateReqDTO
     * @return
     */
    DsProjectRespDTO saveProject(DsProjectCreateReqDTO dsProjectCreateReqDTO);

    /**
     * 修改项目
     *
     * @param dsProjectUpdateReqDTO
     * @return
     */
    DsProjectRespDTO updateProject(DsProjectUpdateReqDTO dsProjectUpdateReqDTO);

    /**
     * 删除项目
     *
     * @param projectCode 项目编码
     * @return
     */
    DsProjectDeleteRespDTO deleteProject(Long projectCode);
}
