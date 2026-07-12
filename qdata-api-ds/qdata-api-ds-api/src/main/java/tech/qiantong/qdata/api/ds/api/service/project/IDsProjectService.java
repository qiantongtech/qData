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

package tech.qiantong.qdata.api.ds.api.service.project;

import tech.qiantong.qdata.api.ds.api.project.DsProjectCreateReqDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectDeleteRespDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectRespDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectUpdateReqDTO;

/**
 * <P>
 * Description: DS project related interfaces
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 14:26
 **/
public interface IDsProjectService {

    /**
     * Create project
     *
     * @param dsProjectCreateReqDTO
     * @return
     */
    DsProjectRespDTO saveProject(DsProjectCreateReqDTO dsProjectCreateReqDTO);

    /**
     * Update project
     *
     * @param dsProjectUpdateReqDTO
     * @return
     */
    DsProjectRespDTO updateProject(DsProjectUpdateReqDTO dsProjectUpdateReqDTO);

    /**
     * Delete project
     *
     * @param projectCode project code
     * @return
     */
    DsProjectDeleteRespDTO deleteProject(Long projectCode);
}
