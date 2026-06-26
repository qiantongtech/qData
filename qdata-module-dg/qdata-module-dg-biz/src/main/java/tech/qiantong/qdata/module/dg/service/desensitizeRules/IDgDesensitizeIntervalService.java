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

package tech.qiantong.qdata.module.dg.service.desensitizeRules;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalPageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeIntervalDO;
/**
 * 脱敏区间Service接口
 *
 * @author qdata
 * @date 2026-04-10
 */
public interface IDgDesensitizeIntervalService extends IService<DgDesensitizeIntervalDO> {

    /**
     * 获得脱敏区间分页列表
     *
     * @param pageReqVO 分页请求
     * @return 脱敏区间分页列表
     */
    PageResult<DgDesensitizeIntervalDO> getDgDesensitizeIntervalPage(DgDesensitizeIntervalPageReqVO pageReqVO);

    /**
     * 创建脱敏区间
     *
     * @param createReqVO 脱敏区间信息
     * @return 脱敏区间编号
     */
    Long createDgDesensitizeInterval(DgDesensitizeIntervalSaveReqVO createReqVO);

    /**
     * 更新脱敏区间
     *
     * @param updateReqVO 脱敏区间信息
     */
    int updateDgDesensitizeInterval(DgDesensitizeIntervalSaveReqVO updateReqVO);

    /**
     * 删除脱敏区间
     *
     * @param idList 脱敏区间编号
     */
    int removeDgDesensitizeInterval(Collection<Long> idList);

    /**
     * 获得脱敏区间详情
     *
     * @param id 脱敏区间编号
     * @return 脱敏区间
     */
    DgDesensitizeIntervalDO getDgDesensitizeIntervalById(Long id);

    /**
     * 获得全部脱敏区间列表
     *
     * @return 脱敏区间列表
     */
    List<DgDesensitizeIntervalDO> getDgDesensitizeIntervalList();

    /**
     * 获得全部脱敏区间 Map
     *
     * @return 脱敏区间 Map
     */
    Map<Long, DgDesensitizeIntervalDO> getDgDesensitizeIntervalMap();


    /**
     * 导入脱敏区间数据
     *
     * @param importExcelList 脱敏区间数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDgDesensitizeInterval(List<DgDesensitizeIntervalRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
