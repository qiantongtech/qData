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

package tech.qiantong.qdata.module.dpp.api.service.etl;

import java.util.List;

public interface DppEtlTaskService {

    /**
     * Check if datasource primary key set is referenced
     * @param datasourceIdList Datasource ID list
     * @return Count
     */
    int checkTaskIdInDatasource(List<Long> datasourceIdList,List<Long> projectIdList);

    /**
     * Check if asset primary key set is referenced
     * @param assetIdList Asset primary key list
     * @return Count
     */
    int checkTaskIdInAsset(List<Long> assetIdList);

    /**
     * Count tasks under a category and all of its descendants.
     */
    long getCountByCatCode(String catCode, List<String> taskTypes);
}
