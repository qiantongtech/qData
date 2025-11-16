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

package tech.qiantong.qdata.module.att.service.rule;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttCleanCatDO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttCleanRuleDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 清洗规则Service接口
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IAttCleanRuleService extends IService<AttCleanRuleDO> {

    /**
     * 获得清洗规则分页列表
     *
     * @param pageReqVO 分页请求
     * @return 清洗规则分页列表
     */
    PageResult<AttCleanRuleDO> getAttCleanRulePage(AttCleanRulePageReqVO pageReqVO);

    /**
     * 创建清洗规则
     *
     * @param createReqVO 清洗规则信息
     * @return 清洗规则编号
     */
    Long createAttCleanRule(AttCleanRuleSaveReqVO createReqVO);

    /**
     * 更新清洗规则
     *
     * @param updateReqVO 清洗规则信息
     */
    int updateAttCleanRule(AttCleanRuleSaveReqVO updateReqVO);

    /**
     * 删除清洗规则
     *
     * @param idList 清洗规则编号
     */
    int removeAttCleanRule(Collection<Long> idList);

    /**
     * 获得清洗规则详情
     *
     * @param id 清洗规则编号
     * @return 清洗规则
     */
    AttCleanRuleDO getAttCleanRuleById(Long id);

    /**
     * 获得全部清洗规则列表
     *
     * @return 清洗规则列表
     */
    List<AttCleanRuleDO> getAttCleanRuleList();
    List<AttCleanRuleRespVO> getAttCleanRuleList(AttCleanRulePageReqVO attCleanRule);

    /**
     * 获得全部清洗规则 Map
     *
     * @return 清洗规则 Map
     */
    Map<Long, AttCleanRuleDO> getAttCleanRuleMap();

    /**
     * 导入清洗规则数据
     *
     * @param importExcelList 清洗规则数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importAttCleanRule(List<AttCleanRuleRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 获取清洗规则树形结构
     *
     * @return 树形结构列表
     */
    List<AttCleanRuleRespVO> getAttCleanRuleTree(Long dataElemId);

    List<AttCleanRuleRespVO> getCleaningRuleTree(Long[] dataElemId);

    /**
     * @param catCode {@link AttCleanCatDO#code}
     */
    Long getCount(String catCode);

}
