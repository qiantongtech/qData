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

package tech.qiantong.qdata.module.dg.service.whitelist;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;
/**
 * 脱敏白名单与用户关联关系Service接口
 *
 * @author qdata
 * @date 2026-04-09
 */
public interface IDgDesensitizeUserRelService extends IService<DgDesensitizeUserRelDO> {

    /**
     * 获得脱敏白名单与用户关联关系分页列表
     *
     * @param pageReqVO 分页请求
     * @return 脱敏白名单与用户关联关系分页列表
     */
    PageResult<DgDesensitizeUserRelDO> getDgDesensitizeUserRelPage(DgDesensitizeUserRelPageReqVO pageReqVO);

    /**
     * 创建脱敏白名单与用户关联关系
     *
     * @param createReqVO 脱敏白名单与用户关联关系信息
     * @return 脱敏白名单与用户关联关系编号
     */
    Long createDgDesensitizeUserRel(DgDesensitizeUserRelSaveReqVO createReqVO);

    /**
     * 更新脱敏白名单与用户关联关系
     *
     * @param updateReqVO 脱敏白名单与用户关联关系信息
     */
    int updateDgDesensitizeUserRel(DgDesensitizeUserRelSaveReqVO updateReqVO);

    /**
     * 删除脱敏白名单与用户关联关系
     *
     * @param idList 脱敏白名单与用户关联关系编号
     */
    int removeDgDesensitizeUserRel(Collection<Long> idList);

    /**
     * 获得脱敏白名单与用户关联关系详情
     *
     * @param id 脱敏白名单与用户关联关系编号
     * @return 脱敏白名单与用户关联关系
     */
    DgDesensitizeUserRelDO getDgDesensitizeUserRelById(Long id);

    /**
     * 获得全部脱敏白名单与用户关联关系列表
     *
     * @return 脱敏白名单与用户关联关系列表
     */
    List<DgDesensitizeUserRelDO> getDgDesensitizeUserRelList();

    /**
     * 获得全部脱敏白名单与用户关联关系 Map
     *
     * @return 脱敏白名单与用户关联关系 Map
     */
    Map<Long, DgDesensitizeUserRelDO> getDgDesensitizeUserRelMap();


    /**
     * 导入脱敏白名单与用户关联关系数据
     *
     * @param importExcelList 脱敏白名单与用户关联关系数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDgDesensitizeUserRel(List<DgDesensitizeUserRelRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
