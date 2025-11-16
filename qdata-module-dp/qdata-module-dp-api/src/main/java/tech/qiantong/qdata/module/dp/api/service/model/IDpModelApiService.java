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

package tech.qiantong.qdata.module.dp.api.service.model;

import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelReqDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelRespDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelColumnRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelRespDTO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 逻辑模型Service接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpModelApiService {

    /**
     * 根据逻辑模型ID获取逻辑模型信息
     *
     * @param id
     * @return
     */
    DpModelRespDTO getDpModelByIdApi(Long id);

    /**
     * 根据逻辑模型ID获取逻辑模型列信息
     *
     * @param modelId 逻辑模型ID
     * @return 逻辑模型列信息
     */
    List<DpModelColumnRespDTO> getDpModelColumnListByModelIdApi(Long modelId);

    /**
     * 根据数据元id查询数据元信息
     *
     * @param ids
     * @return
     */
    List<DpDataElemRespDTO> getDpDataElemListByIdsApi(Set<Long> ids);

    /**
     * 根据资产id获取数据元id集合
     *
     * @param assetId
     * @return
     */
    Set<Long> getDpDataElemListByAssetIdApi(Long assetId);

    List<DpDataElemAssetRelRespDTO> getDpDataElemListByColumnIdInApi(Collection<Long> columnIds);

    /**
     * 根据资产id及字段id获取数据元id集合
     *
     * @param assetId
     * @return
     */
    Set<Long> getDpDataElemListByAssetIdAndColumnId(Long assetId, Long columnId);

    /**
     * 插入数据元和资产关系数据
     *
     * @param dpDataElemAssetRel
     * @return
     */
    boolean insertElementAssetRelation(List<DpDataElemAssetRelReqDTO> dpDataElemAssetRel);

    /**
     * 根据类目编码查询数量
     *
     * @return
     */
    Long getCountByCatCode(String catCode);

    /**
     * 更新数据元和资产关系数据
     *
     * @param dpDataElemAssetRel
     * @return
     */
    boolean updateElementAssetRelation(DpDataElemAssetRelReqDTO dpDataElemAssetRel);

    /**
     * 根据资产id和代码表id查询数据元信息
     *
     * @param assetId
     * @param codeId
     * @return
     */
    List<DpDataElemRespDTO> getDpDataElemListByAssetId(Long assetId, Set<Long> codeId);

    /**
     * 更具模型id查询模型下的字段集合
     *
     * @param modelId 模型id
     */
    List<DpModelColumnRespDTO> getModelIdColumnList(Long modelId);
}
