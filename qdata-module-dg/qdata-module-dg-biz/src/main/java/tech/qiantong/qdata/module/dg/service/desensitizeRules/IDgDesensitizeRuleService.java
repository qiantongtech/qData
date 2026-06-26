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
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRuleRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRuleSaveReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRulePageReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeRuleDO;
/**
 * 脱敏规则Service接口
 *
 * @author qdata
 * @date 2026-04-10
 */
public interface IDgDesensitizeRuleService extends IService<DgDesensitizeRuleDO> {

    /**
     * 获得脱敏规则分页列表
     *
     * @param pageReqVO 分页请求
     * @return 脱敏规则分页列表
     */
    PageResult<DgDesensitizeRuleDO> getDgDesensitizeRulePage(DgDesensitizeRulePageReqVO pageReqVO);

    /**
     * 创建脱敏规则
     *
     * @param createReqVO 脱敏规则信息
     * @return 脱敏规则编号
     */
    Long createDgDesensitizeRule(DgDesensitizeRuleSaveReqVO createReqVO);

    /**
     * 更新脱敏规则
     *
     * @param updateReqVO 脱敏规则信息
     */
    int updateDgDesensitizeRule(DgDesensitizeRuleSaveReqVO updateReqVO);

    /**
     * 删除脱敏规则
     *
     * @param idList 脱敏规则编号
     */
    int removeDgDesensitizeRule(Collection<Long> idList);

    /**
     * 获得脱敏规则详情
     *
     * @param id 脱敏规则编号
     * @return 脱敏规则
     */
    DgDesensitizeRuleDO getDgDesensitizeRuleById(Long id);

    /**
     * 获得全部脱敏规则列表
     *
     * @return 脱敏规则列表
     */
    List<DgDesensitizeRuleDO> getDgDesensitizeRuleList();

    /**
     * 获得全部脱敏规则 Map
     *
     * @return 脱敏规则 Map
     */
    Map<Long, DgDesensitizeRuleDO> getDgDesensitizeRuleMap();


    /**
     * 导入脱敏规则数据
     *
     * @param importExcelList 脱敏规则数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDgDesensitizeRule(List<DgDesensitizeRuleRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 根据分类id获取规则数量
     *
     * @param idList 分类id数组
     * @return 规则数量
     */
    Long getCountByCategoryIds(Collection<Long> idList);
    DgDesensitizeRuleDO getDgDesensitizeRuleByDataCategoryId(Long dataCategoryId);
}
