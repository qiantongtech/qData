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

package tech.qiantong.qdata.module.att.service.rule.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.core.text.Convert;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttCleanRuleSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttCleanRuleDO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.enums.CleanRuleTypeEnum;
import tech.qiantong.qdata.module.att.dal.mapper.rule.AttCleanRuleMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttCleanCatService;
import tech.qiantong.qdata.module.att.service.rule.IAttCleanRuleService;

/**
 * Cleaning Rule Service business layer processing
 *
 * @author qdata
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttCleanRuleServiceImpl extends ServiceImpl<AttCleanRuleMapper, AttCleanRuleDO>
        implements IAttCleanRuleService {
    @Resource
    private AttCleanRuleMapper attCleanRuleMapper;
    @Resource
    private IAttCleanCatService attCleanCatService;

    @Override
    public PageResult<AttCleanRuleDO> getAttCleanRulePage(AttCleanRulePageReqVO pageReqVO) {
        return attCleanRuleMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttCleanRule(AttCleanRuleSaveReqVO createReqVO) {
        List<AttCleanRuleDO> code = attCleanRuleMapper.selectList("code", createReqVO.getCode());
        if (code.size() > 0) {
            throw new ServiceException("att.error.rule.code.duplicate", "规则编码重复请重新输入");
        }
        AttCleanRuleDO dictType = BeanUtils.toBean(createReqVO, AttCleanRuleDO.class);
        attCleanRuleMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttCleanRule(AttCleanRuleSaveReqVO updateReqVO) {
        // Validate
        List<AttCleanRuleDO> code = attCleanRuleMapper.selectList("code", updateReqVO.getCode());
        if (code.size() > 0) {
            throw new ServiceException("att.error.rule.code.duplicate", "规则编码重复请重新输入");
        }
        // Update cleaning rule
        AttCleanRuleDO updateObj = BeanUtils.toBean(updateReqVO, AttCleanRuleDO.class);
        return attCleanRuleMapper.updateById(updateObj);
    }

    @Override
    public int removeAttCleanRule(Collection<Long> idList) {
        // Batch delete cleaning rules
        return attCleanRuleMapper.deleteBatchIds(idList);
    }

    @Override
    public AttCleanRuleDO getAttCleanRuleById(Long id) {
        return attCleanRuleMapper.selectById(id);
    }

    @Override
    public List<AttCleanRuleDO> getAttCleanRuleList() {
        return attCleanRuleMapper.selectList();
    }

    @Override
    public List<AttCleanRuleRespVO> getAttCleanRuleList(AttCleanRulePageReqVO attCleanRule) {

        MPJLambdaWrapper<AttCleanRuleDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(AttCleanRuleDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("ATT_CLEAN_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .likeRight(org.apache.commons.lang3.StringUtils.isNotBlank(attCleanRule.getCatCode()), AttCleanRuleDO::getCatCode, attCleanRule.getCatCode());
//        LambdaQueryWrapperX<AttCleanRuleDO> x = new LambdaQueryWrapperX<>();
//        x.eqIfPresent(AttCleanRuleDO::getType , attCleanRule.getType());
//        x.eqIfPresent(AttCleanRuleDO::getValidFlag , attCleanRule.getValidFlag());
        List<AttCleanRuleDO> attCleanRuleDOS = attCleanRuleMapper.selectList(lambdaWrapper);
        List<AttCleanRuleRespVO> bean = BeanUtils.toBean(attCleanRuleDOS, AttCleanRuleRespVO.class);
        for (AttCleanRuleRespVO respVO : bean) {

            respVO.setParentType(Convert.toStr(respVO.getCatID()));
            respVO.setParentName(respVO.getCatName());
        }
        return bean;
    }

    @Override
    public Map<Long, AttCleanRuleDO> getAttCleanRuleMap() {
        List<AttCleanRuleDO> attCleanRuleList = attCleanRuleMapper.selectList();
        return attCleanRuleList.stream()
                .collect(Collectors.toMap(
                        AttCleanRuleDO::getId,
                        attCleanRuleDO -> attCleanRuleDO,
                        // Keep existing value
                        (existing, replacement) -> existing));
    }

    /**
     * Import cleaning rule data
     *
     *  importExcelList cleaning rule data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     *  operName Operator
     *  @return Result
     */
    @Override
    public String importAttCleanRule(List<AttCleanRuleRespVO> importExcelList, boolean isUpdateSupport,
                                     String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("att.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttCleanRuleRespVO respVO : importExcelList) {
            try {
                AttCleanRuleDO attCleanRuleDO = BeanUtils.toBean(respVO, AttCleanRuleDO.class);
                Long attCleanRuleId = respVO.getId();
                if (isUpdateSupport) {
                    if (attCleanRuleId != null) {
                        AttCleanRuleDO existingAttCleanRule = attCleanRuleMapper.selectById(attCleanRuleId);
                        if (existingAttCleanRule != null) {
                            attCleanRuleMapper.updateById(attCleanRuleDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                    "数据Update 成功，ID为 " + attCleanRuleId + " 的清洗规则记录。", attCleanRuleId, "清洗规则"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                    "数据Update 失败，ID为 " + attCleanRuleId + " 的清洗规则记录不存在。", attCleanRuleId, "清洗规则"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                "数据Update 失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<AttCleanRuleDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attCleanRuleId);
                    AttCleanRuleDO existingAttCleanRule = attCleanRuleMapper.selectOne(queryWrapper);
                    if (existingAttCleanRule == null) {
                        attCleanRuleMapper.insert(attCleanRuleDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                "数据插入成功，ID为 " + attCleanRuleId + " 的清洗规则记录。", attCleanRuleId, "清洗规则"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                "数据插入失败，ID为 " + attCleanRuleId + " 的清洗规则记录已存在。", attCleanRuleId, "清洗规则"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("att.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("att.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("att.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("att.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public List<AttCleanRuleRespVO> getAttCleanRuleTree(Long dataElemId) {
        // 1. Get all cleaning rule list
        List<AttCleanRuleDO> list = attCleanRuleMapper.selectAttCleanRuleList(dataElemId);
        // 2. Convert to VO objects
        List<AttCleanRuleRespVO> voList = BeanUtils.toBean(list, AttCleanRuleRespVO.class);
        // 3. Build tree structure
        return buildTreeByType(voList);
    }

    @Override
    public List<AttCleanRuleRespVO> getCleaningRuleTree(Long[] dataElemId) {
        List<AttCleanRuleDO> list =null;
        if (dataElemId == null || dataElemId.length == 0) {
            // Array is empty or not initialized
            list = attCleanRuleMapper.selectList();
        }else {
            list = attCleanRuleMapper.getCleaningRuleTreeIds(dataElemId);
        }
        // 2. Convert to VO objects
        List<AttCleanRuleRespVO> voList = BeanUtils.toBean(list, AttCleanRuleRespVO.class);
        // 3. Build tree structure
        return buildTreeByType(voList);
    }

    @Override
    public Long getCount(String catCode) {
        return attCleanRuleMapper.selectCount(Wrappers.lambdaQuery(AttCleanRuleDO.class)
                .likeRight(AttCleanRuleDO::getCatCode, catCode));
    }

    /**
     * Build tree structure - use type field as parent node
     *
     * @param list Rule list
     *  Tree structure list
     */
    private List<AttCleanRuleRespVO> buildTreeByType(List<AttCleanRuleRespVO> list) {
        List<AttCleanRuleRespVO> resultList = new ArrayList<>();
        // Create type mapping for storing nodes with same type
        Map<String, List<AttCleanRuleRespVO>> typeMap = list.stream()
                .collect(Collectors.groupingBy(AttCleanRuleRespVO::getType));

        // Iterate each type group
        for (Map.Entry<String, List<AttCleanRuleRespVO>> entry : typeMap.entrySet()) {
            String type = entry.getKey();
            List<AttCleanRuleRespVO> typeNodes = entry.getValue();
            for (AttCleanRuleRespVO typeNode : typeNodes) {
                typeNode.setDataType("2");
            }
            // Create parent node
            AttCleanRuleRespVO parentNode = new AttCleanRuleRespVO();
            parentNode.setId(0L); // Set a special ID
            parentNode.setType(type);
            parentNode.setDataType("1");
            String typeName = CleanRuleTypeEnum.getNameByType(type);
            parentNode.setName(typeName); // Set parent node name
            parentNode.setChildren(new ArrayList<>(typeNodes));

            resultList.add(parentNode);
        }

        return resultList;
    }
}
