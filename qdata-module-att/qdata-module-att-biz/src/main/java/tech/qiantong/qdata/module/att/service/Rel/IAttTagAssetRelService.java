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

package tech.qiantong.qdata.module.att.service.Rel;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.tagAssetRel.vo.AttTagAssetRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.Rel.AttTagAssetRelDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 标签与资产关联关系Service接口
 *
 * @author qdata
 * @date 2025-07-11
 */
public interface IAttTagAssetRelService extends IService<AttTagAssetRelDO> {

    /**
     * 获得标签与资产关联关系分页列表
     *
     * @param pageReqVO 分页请求
     * @return 标签与资产关联关系分页列表
     */
    PageResult<AttTagAssetRelDO> getAttTagAssetRelPage(AttTagAssetRelPageReqVO pageReqVO);

    /**
     * 创建标签与资产关联关系
     *
     * @param createReqVO 标签与资产关联关系信息
     * @return 标签与资产关联关系编号
     */
    Long createAttTagAssetRel(AttTagAssetRelSaveReqVO createReqVO);

    /**
     * 更新标签与资产关联关系
     *
     * @param updateReqVO 标签与资产关联关系信息
     */
    int updateAttTagAssetRel(AttTagAssetRelSaveReqVO updateReqVO);

    /**
     * 删除标签与资产关联关系
     *
     * @param idList 标签与资产关联关系编号
     */
    int removeAttTagAssetRel(Collection<Long> idList);



    /**
     * 获得标签与资产关联关系详情
     *
     * @param id 标签与资产关联关系编号
     * @return 标签与资产关联关系
     */
    AttTagAssetRelDO getAttTagAssetRelById(Long id);

    /**
     * 获得全部标签与资产关联关系列表
     *
     * @return 标签与资产关联关系列表
     */
    List<AttTagAssetRelDO> getAttTagAssetRelList();

    /**
     * 获得全部标签与资产关联关系 Map
     *
     * @return 标签与资产关联关系 Map
     */
    Map<Long, AttTagAssetRelDO> getAttTagAssetRelMap();


    /**
     * 导入标签与资产关联关系数据
     *
     * @param importExcelList 标签与资产关联关系数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttTagAssetRel(List<AttTagAssetRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

    int removeAttTagAssetRel(Long id, AttTagAssetRelPageReqVO attTagAssetRel);
}
