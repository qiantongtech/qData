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

package tech.qiantong.qdata.module.att.api.project;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectReqDTO;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectRespDTO;

/**
 * <P>
 * Purpose: Project-related interfaces
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-25 14:31
 **/
public interface IAttProjectApi {

    /**
     * Get project ID by project code
     *
     * @param projectCode
     * @return
     */
    Long getProjectIdByProjectCode(String projectCode);

    /**
     * Get paginated project list
     *
     * @param pageReqVO pagination request
     * @return paginated project list
     */
    PageResult<AttProjectRespDTO> getAttProjectPage(AttProjectReqDTO pageReqVO);
}
