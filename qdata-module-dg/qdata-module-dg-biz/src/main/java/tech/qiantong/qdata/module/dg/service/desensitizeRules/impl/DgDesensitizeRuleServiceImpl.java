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

package tech.qiantong.qdata.module.dg.service.desensitizeRules.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRulePageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRuleRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeRuleSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategory.DgDataCategoryDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeIntervalDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeRuleDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;
import tech.qiantong.qdata.module.dg.dal.mapper.dataCategory.DgDataCategoryMapper;
import tech.qiantong.qdata.module.dg.dal.mapper.dataCategoryCat.DgDataCategoryCatMapper;
import tech.qiantong.qdata.module.dg.dal.mapper.desensitizeRules.DgDesensitizeIntervalMapper;
import tech.qiantong.qdata.module.dg.dal.mapper.desensitizeRules.DgDesensitizeRuleMapper;
import tech.qiantong.qdata.module.dg.service.desensitizeRules.IDgDesensitizeRuleService;
/**
 * 脱敏规则Service业务层处理
 *
 * @author qdata
 * @date 2026-04-10
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDesensitizeRuleServiceImpl  extends ServiceImpl<DgDesensitizeRuleMapper,DgDesensitizeRuleDO> implements IDgDesensitizeRuleService {
    @Resource
    private DgDesensitizeRuleMapper dgDesensitizeRuleMapper;

    @Resource
    private DgDesensitizeIntervalMapper dgDesensitizeIntervalMapper;

    @Resource
    private DgDataCategoryMapper dgDataCategoryMapper;


    @Override
    public PageResult<DgDesensitizeRuleDO> getDgDesensitizeRulePage(DgDesensitizeRulePageReqVO pageReqVO) {
        return dgDesensitizeRuleMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgDesensitizeRule(DgDesensitizeRuleSaveReqVO createReqVO) {
        DgDesensitizeRuleDO dictType = BeanUtils.toBean(createReqVO, DgDesensitizeRuleDO.class);
        //判断数据分类是否在当前规则下已存在
        if (dgDesensitizeRuleMapper.selectCount(new LambdaQueryWrapper<DgDesensitizeRuleDO>()
                .eq(DgDesensitizeRuleDO::getDataCategoryId, dictType.getDataCategoryId())) > 0) {
            throw new ServiceException("dg.error.duplicate.category", "数据分类已存在");
        }

        dgDesensitizeRuleMapper.insert(dictType);
        List<DgDesensitizeIntervalDO> intervalList = createReqVO.getIntervalList();
        if (StringUtils.isNotNull(intervalList)) {
            intervalList.forEach(interval -> {
                interval.setDesensitizeRuleId(dictType.getId());
            });
            dgDesensitizeIntervalMapper.insertBatch(intervalList);
        }
        return dictType.getId();
    }

    @Override
    public int updateDgDesensitizeRule(DgDesensitizeRuleSaveReqVO updateReqVO) {
        // 更新脱敏规则
        DgDesensitizeRuleDO updateObj = BeanUtils.toBean(updateReqVO, DgDesensitizeRuleDO.class);

       //先判断updateObj旧的区间是否存在，存在则删除旧的区间
        if (StringUtils.isNotNull(updateObj.getIntervalList())) {
            // 先删除旧的区间
            dgDesensitizeIntervalMapper.delete(
                    Wrappers.lambdaQuery(DgDesensitizeIntervalDO.class)
                            .eq(DgDesensitizeIntervalDO::getDesensitizeRuleId, updateObj.getId())
            );
        }
        // 再插入新的区间
        List<DgDesensitizeIntervalDO> intervalList = updateReqVO.getIntervalList();
        if (StringUtils.isNotNull(intervalList)) {
            intervalList.forEach(interval -> {
                interval.setDesensitizeRuleId(updateObj.getId());
                interval.setId(null);//防止再次插入报错
            });
            dgDesensitizeIntervalMapper.insertBatch(intervalList);
        }
        return dgDesensitizeRuleMapper.updateById(updateObj);
    }
    @Override
    public int removeDgDesensitizeRule(Collection<Long> idList) {
        // 批量删除脱敏规则和区间数据
        dgDesensitizeIntervalMapper.delete(
                Wrappers.lambdaQuery(DgDesensitizeIntervalDO.class)
                        .in(DgDesensitizeIntervalDO::getDesensitizeRuleId, idList)
        );
        //dgDesensitizeIntervalMapper.delete(new QueryWrapper<DgDesensitizeIntervalDO>().in("desensitize_id", idList));
        return dgDesensitizeRuleMapper.deleteBatchIds(idList);
    }
    @Override
    public DgDesensitizeRuleDO getDgDesensitizeRuleById(Long id) {
        DgDesensitizeRuleDO rule = dgDesensitizeRuleMapper.selectById(id);
        //将rule中的分类ID转换为分类名称
        if(rule.getDataCategoryId() != null){
            DgDataCategoryDO dgDataCategoryDO = dgDataCategoryMapper.selectById(rule.getDataCategoryId());
            if(dgDataCategoryDO!=null) {
                rule.setDataCategoryName(dgDataCategoryDO.getName());
            }
        }
        //根据脱敏规则ID 查询区间集合存入DgDesensitizeRuleDO
        rule.setIntervalList(dgDesensitizeIntervalMapper.selectList(new LambdaQueryWrapper<DgDesensitizeIntervalDO>().eq(DgDesensitizeIntervalDO::getDesensitizeRuleId, id)));
        return rule;
    }

    @Override
    public DgDesensitizeRuleDO getDgDesensitizeRuleByDataCategoryId(Long dataCategoryId) {
        DgDesensitizeRuleDO rule =  dgDesensitizeRuleMapper.selectOne(new LambdaQueryWrapper<DgDesensitizeRuleDO>().eq(DgDesensitizeRuleDO::getDataCategoryId, dataCategoryId));
        if(rule!=null){
            rule.setIntervalList(dgDesensitizeIntervalMapper.selectList(new LambdaQueryWrapper<DgDesensitizeIntervalDO>().eq(DgDesensitizeIntervalDO::getDesensitizeRuleId, rule.getId())));
        }
        return rule;
    }

    @Override
    public List<DgDesensitizeRuleDO> getDgDesensitizeRuleList() {
        return dgDesensitizeRuleMapper.selectList();
    }

    @Override
    public Map<Long, DgDesensitizeRuleDO> getDgDesensitizeRuleMap() {
        List<DgDesensitizeRuleDO> dgDesensitizeRuleList = dgDesensitizeRuleMapper.selectList();
        return dgDesensitizeRuleList.stream()
                .collect(Collectors.toMap(
                        DgDesensitizeRuleDO::getId,
                        dgDesensitizeRuleDO -> dgDesensitizeRuleDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入脱敏规则数据
         *
         * @param importExcelList 脱敏规则数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDgDesensitizeRule(List<DgDesensitizeRuleRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dg.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DgDesensitizeRuleRespVO respVO : importExcelList) {
                try {
                    DgDesensitizeRuleDO dgDesensitizeRuleDO = BeanUtils.toBean(respVO, DgDesensitizeRuleDO.class);
                    Long dgDesensitizeRuleId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dgDesensitizeRuleId != null) {
                            DgDesensitizeRuleDO existingDgDesensitizeRule = dgDesensitizeRuleMapper.selectById(dgDesensitizeRuleId);
                            if (existingDgDesensitizeRule != null) {
                                dgDesensitizeRuleMapper.updateById(dgDesensitizeRuleDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dg.import.update.success",
                                        "数据更新成功，ID为 " + dgDesensitizeRuleId + " 的脱敏规则记录。", dgDesensitizeRuleId, "脱敏规则"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.fail",
                                        "数据更新失败，ID为 " + dgDesensitizeRuleId + " 的脱敏规则记录不存在。", dgDesensitizeRuleId, "脱敏规则"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DgDesensitizeRuleDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dgDesensitizeRuleId);
                        DgDesensitizeRuleDO existingDgDesensitizeRule = dgDesensitizeRuleMapper.selectOne(queryWrapper);
                        if (existingDgDesensitizeRule == null) {
                            dgDesensitizeRuleMapper.insert(dgDesensitizeRuleDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dg.import.insert.success",
                                    "数据插入成功，ID为 " + dgDesensitizeRuleId + " 的脱敏规则记录。", dgDesensitizeRuleId, "脱敏规则"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.insert.fail",
                                    "数据插入失败，ID为 " + dgDesensitizeRuleId + " 的脱敏规则记录已存在。", dgDesensitizeRuleId, "脱敏规则"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("dg.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.fail",
                        "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("dg.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.success",
                        "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
            }
            return resultMsg.toString();
        }

    @Override
    public Long getCountByCategoryIds(Collection<Long> idList) {
        return baseMapper.selectCount(Wrappers
                .lambdaQuery(DgDesensitizeRuleDO.class)
                .in(DgDesensitizeRuleDO::getDataCategoryId, idList));
    }
}
