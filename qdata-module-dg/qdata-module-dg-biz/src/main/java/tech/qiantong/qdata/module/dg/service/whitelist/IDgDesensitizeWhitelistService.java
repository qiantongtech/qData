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
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeWhitelistDO;
/**
 * 脱敏白名单Service接口
 *
 * @author qdata
 * @date 2026-04-09
 */
public interface IDgDesensitizeWhitelistService extends IService<DgDesensitizeWhitelistDO> {

    /**
     * 获得脱敏白名单分页列表
     *
     * @param pageReqVO 分页请求
     * @return 脱敏白名单分页列表
     */
    PageResult<DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistPage(DgDesensitizeWhitelistPageReqVO pageReqVO);

    /**
     * 创建脱敏白名单
     *
     * @param createReqVO 脱敏白名单信息
     * @return 脱敏白名单编号
     */
    Long createDgDesensitizeWhitelist(DgDesensitizeWhitelistSaveReqVO createReqVO);

    /**
     * 更新脱敏白名单
     *
     * @param updateReqVO 脱敏白名单信息
     */
    int updateDgDesensitizeWhitelist(DgDesensitizeWhitelistSaveReqVO updateReqVO);

    /**
     * 删除脱敏白名单
     *
     * @param idList 脱敏白名单编号
     */
    int removeDgDesensitizeWhitelist(Collection<Long> idList);

    /**
     * 获得脱敏白名单详情
     *
     * @param id 脱敏白名单编号
     * @return 脱敏白名单
     */
    DgDesensitizeWhitelistDO getDgDesensitizeWhitelistById(Long id);

    /**
     * 获得全部脱敏白名单列表
     *
     * @return 脱敏白名单列表
     */
    List<DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistList();

    /**
     * 获得全部脱敏白名单 Map
     *
     * @return 脱敏白名单 Map
     */
    Map<Long, DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistMap();


    /**
     * 导入脱敏白名单数据
     *
     * @param importExcelList 脱敏白名单数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDgDesensitizeWhitelist(List<DgDesensitizeWhitelistRespVO> importExcelList, boolean isUpdateSupport, String operName);

    //根据分类ID查询脱敏白名单
    DgDesensitizeWhitelistDO getDgDesensitizeWhitelistByCategoryId(Long categoryId);
}
