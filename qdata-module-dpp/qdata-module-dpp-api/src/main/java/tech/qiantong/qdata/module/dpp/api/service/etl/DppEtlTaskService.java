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

package tech.qiantong.qdata.module.dpp.api.service.etl;

import java.util.List;

public interface DppEtlTaskService {

    /**
     * 查询数据源主键集合是否被引用
     * @param datasourceIdList 数据源id集合
     * @return 条数
     */
    int checkTaskIdInDatasource(List<Long> datasourceIdList,List<Long> projectIdList);

    /**
     * 查询资产主键集合是否被引用
     * @param assetIdList 资产主键集合
     * @return 条数
     */
    int checkTaskIdInAsset(List<Long> assetIdList);
}
