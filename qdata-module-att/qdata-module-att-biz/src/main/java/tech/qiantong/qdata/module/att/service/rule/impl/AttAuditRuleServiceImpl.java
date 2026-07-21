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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRulePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRuleRespVO;
import tech.qiantong.qdata.module.att.controller.admin.rule.vo.AttAuditRuleSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.AttAuditRuleDO;
import tech.qiantong.qdata.module.att.dal.dataobject.rule.enums.RuleTypeEnum;
import tech.qiantong.qdata.module.att.dal.mapper.rule.AttAuditRuleMapper;
import tech.qiantong.qdata.module.att.service.rule.IAttAuditRuleService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Audit Rule Service business layer processing
 *
 * @author qdata
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttAuditRuleServiceImpl extends ServiceImpl<AttAuditRuleMapper, AttAuditRuleDO>
        implements IAttAuditRuleService {
    @Resource
    private AttAuditRuleMapper attAuditRuleMapper;

    @Override
    public PageResult<AttAuditRuleDO> getAttAuditRulePage(AttAuditRulePageReqVO pageReqVO) {
        return attAuditRuleMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttAuditRule(AttAuditRuleSaveReqVO createReqVO) {
        AttAuditRuleDO dictType = BeanUtils.toBean(createReqVO, AttAuditRuleDO.class);
        attAuditRuleMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttAuditRule(AttAuditRuleSaveReqVO updateReqVO) {
        // Validate

        // Update audit rule
        AttAuditRuleDO updateObj = BeanUtils.toBean(updateReqVO, AttAuditRuleDO.class);
        return attAuditRuleMapper.updateById(updateObj);
    }

    @Override
    public int removeAttAuditRule(Collection<Long> idList) {
        // Batch delete audit rules
        return attAuditRuleMapper.deleteBatchIds(idList);
    }

    @Override
    public AttAuditRuleDO getAttAuditRuleById(Long id) {
        return attAuditRuleMapper.selectById(id);
    }

    @Override
    public List<AttAuditRuleDO> getAttAuditRuleList() {
        return attAuditRuleMapper.selectList();
    }

    @Override
    public Map<Long, AttAuditRuleDO> getAttAuditRuleMap() {
        List<AttAuditRuleDO> attAuditRuleList = attAuditRuleMapper.selectList();
        return attAuditRuleList.stream()
                .collect(Collectors.toMap(
                        AttAuditRuleDO::getId,
                        attAuditRuleDO -> attAuditRuleDO,
                        // Keep existing value
                        (existing, replacement) -> existing));
    }

    @Override
    public List<AttAuditRuleRespVO> getAttAuditRuleTree(Long dataElemId) {
        // 1. Get all audit rule list
        List<AttAuditRuleDO> list = attAuditRuleMapper.selectAttAuditRuleList(dataElemId);
        // 2. Convert to VO objects
        List<AttAuditRuleRespVO> voList = BeanUtils.toBean(list, AttAuditRuleRespVO.class);
        // 3. Build tree structure
        return buildTreeByType(voList);
    }

    /**
     * Build tree structure - use type field as parent node
     *
     * @param list Rule list
     *  Tree structure list
     */
    private List<AttAuditRuleRespVO> buildTreeByType(List<AttAuditRuleRespVO> list) {
        List<AttAuditRuleRespVO> resultList = new ArrayList<>();
        // Create type mapping for storing nodes with same type
        Map<String, List<AttAuditRuleRespVO>> typeMap = list.stream()
                .collect(Collectors.groupingBy(AttAuditRuleRespVO::getType));

        // Iterate each type group
        for (Map.Entry<String, List<AttAuditRuleRespVO>> entry : typeMap.entrySet()) {
            String type = entry.getKey();
            List<AttAuditRuleRespVO> typeNodes = entry.getValue();
            for (AttAuditRuleRespVO typeNode : typeNodes) {
                typeNode.setDataType("2");
            }
            // Create parent node
            AttAuditRuleRespVO parentNode = new AttAuditRuleRespVO();
            parentNode.setId(0L); // Set a special ID
            parentNode.setType(type);
            parentNode.setDataType("1");
            // Use enum to get type name
            String typeName = RuleTypeEnum.getNameByType(type);
            parentNode.setName(typeName); // Set parent node name
            parentNode.setChildren(new ArrayList<>(typeNodes));

            resultList.add(parentNode);
        }

        return resultList;
    }

    /**
     * Import audit rule data
     *
     *  importExcelList audit rule data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     *  operName Operator
     *  @return Result
     */
    @Override
    public String importAttAuditRule(List<AttAuditRuleRespVO> importExcelList, boolean isUpdateSupport,
                                     String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttAuditRuleRespVO respVO : importExcelList) {
            try {
                AttAuditRuleDO attAuditRuleDO = BeanUtils.toBean(respVO, AttAuditRuleDO.class);
                Long attAuditRuleId = respVO.getId();
                if (isUpdateSupport) {
                    if (attAuditRuleId != null) {
                        AttAuditRuleDO existingAttAuditRule = attAuditRuleMapper.selectById(attAuditRuleId);
                        if (existingAttAuditRule != null) {
                            attAuditRuleMapper.updateById(attAuditRuleDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                    "Data update successful, ID {0} {1} record.", attAuditRuleId, MessageUtils.messageWithFallback("att.entity.audit.rule", "Audit rule")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", attAuditRuleId, MessageUtils.messageWithFallback("att.entity.audit.rule", "Audit rule")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<AttAuditRuleDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attAuditRuleId);
                    AttAuditRuleDO existingAttAuditRule = attAuditRuleMapper.selectOne(queryWrapper);
                    if (existingAttAuditRule == null) {
                        attAuditRuleMapper.insert(attAuditRuleDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", attAuditRuleId, MessageUtils.messageWithFallback("att.entity.audit.rule", "Audit rule")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", attAuditRuleId, MessageUtils.messageWithFallback("att.entity.audit.rule", "Audit rule")));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("att.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("att.import.result.fail",
                    "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                    failureNum, failureDetails));
            throw new ServiceException("att.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("att.import.result.success",
                    "Congratulations! All data imported successfully! Total: {0} records.", successNum));
        }
        return resultMsg.toString();
    }
}
