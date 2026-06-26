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

package tech.qiantong.qdata.module.dg.service.desensitizeList;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeList.DgDesensitizeAssetcolumnDO;
/**
 * 脱敏清单关联关系Service接口
 *
 * @author qdata
 * @date 2026-04-12
 */
public interface IDgDesensitizeAssetcolumnService extends IService<DgDesensitizeAssetcolumnDO> {

    /**
     * 获得脱敏清单关联关系分页列表
     *
     * @param pageReqVO 分页请求
     * @return 脱敏清单关联关系分页列表
     */
    PageResult<DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnPage(DgDesensitizeAssetcolumnPageReqVO pageReqVO);

    /**
     * 创建脱敏清单关联关系
     *
     * @param createReqVO 脱敏清单关联关系信息
     * @return 脱敏清单关联关系编号
     */
    Long createDgDesensitizeAssetcolumn(DgDesensitizeAssetcolumnSaveReqVO createReqVO);

    /**
     * 更新脱敏清单关联关系
     *
     * @param updateReqVO 脱敏清单关联关系信息
     */
    int updateDgDesensitizeAssetcolumn(DgDesensitizeAssetcolumnSaveReqVO updateReqVO);

    /**
     * 删除脱敏清单关联关系
     *
     * @param idList 脱敏清单关联关系编号
     */
    int removeDgDesensitizeAssetcolumn(Collection<Long> idList);

    /**
     * 获得脱敏清单关联关系详情
     *
     * @param id 脱敏清单关联关系编号
     * @return 脱敏清单关联关系
     */
    DgDesensitizeAssetcolumnDO getDgDesensitizeAssetcolumnById(Long id);

    DgDesensitizeAssetcolumnDO getDgDesensitizeAssetcolumnByAid(Long assetcolumnId);

    /**
     * 获得全部脱敏清单关联关系列表
     *
     * @return 脱敏清单关联关系列表
     */
    List<DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnList();

    /**
     * 获得全部脱敏清单关联关系 Map
     *
     * @return 脱敏清单关联关系 Map
     */
    Map<Long, DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnMap();


    /**
     * 导入脱敏清单关联关系数据
     *
     * @param importExcelList 脱敏清单关联关系数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDgDesensitizeAssetcolumn(List<DgDesensitizeAssetcolumnRespVO> importExcelList, boolean isUpdateSupport, String operName);

    PageResult<DgDesensitizeAssetcolumnDO> getDgDesensitizePagebyRuleId(DgDesensitizeAssetcolumnPageReqVO dgDesensitizeAssetcolumn);

    DgDesensitizeAssetcolumnDO getByassetcolumnId(Long assetcolumnId);

    int deleteByassetcolumnId(Long assetcolumnId);
}
