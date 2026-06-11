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

package tech.qiantong.qdata.module.att.api.project;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectReqDTO;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectRespDTO;

/**
 * <P>
 * 用途:项目相关接口
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-25 14:31
 **/
public interface IAttProjectApi {

    /**
     * 根据项目编码获取项目id
     *
     * @param projectCode
     * @return
     */
    Long getProjectIdByProjectCode(String projectCode);

    /**
     * 获得项目分页列表
     *
     * @param pageReqVO 分页请求
     * @return 项目分页列表
     */
    PageResult<AttProjectRespDTO> getAttProjectPage(AttProjectReqDTO pageReqVO);
}
