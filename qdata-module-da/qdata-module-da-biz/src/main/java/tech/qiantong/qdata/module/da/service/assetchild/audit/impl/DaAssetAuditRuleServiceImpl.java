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

package tech.qiantong.qdata.module.da.service.assetchild.audit.impl;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRulePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditRuleDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.audit.DaAssetAuditRuleMapper;
import tech.qiantong.qdata.module.da.service.assetchild.audit.IDaAssetAuditRuleService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Data Asset Quality Result Record Service business layer processing
 *
 * @author qdata
 * @date 2025-05-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetAuditRuleServiceImpl  extends ServiceImpl<DaAssetAuditRuleMapper,DaAssetAuditRuleDO> implements IDaAssetAuditRuleService {
    @Resource
    private DaAssetAuditRuleMapper daAssetAuditRuleMapper;

    @Override
    public PageResult<DaAssetAuditRuleDO> getDaAssetAuditRulePage(DaAssetAuditRulePageReqVO pageReqVO) {
        return daAssetAuditRuleMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaAssetAuditRule(DaAssetAuditRuleSaveReqVO createReqVO) {
        DaAssetAuditRuleDO dictType = BeanUtils.toBean(createReqVO, DaAssetAuditRuleDO.class);
        daAssetAuditRuleMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetAuditRule(DaAssetAuditRuleSaveReqVO updateReqVO) {
        // Related validation

        // Update Data Asset Quality Result Record
        DaAssetAuditRuleDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetAuditRuleDO.class);
        return daAssetAuditRuleMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetAuditRule(Collection<Long> idList) {
        // Batch delete Data Asset Quality Result Record
        return daAssetAuditRuleMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetAuditRuleDO getDaAssetAuditRuleById(Long id) {
        return daAssetAuditRuleMapper.selectById(id);
    }

    @Override
    public List<DaAssetAuditRuleDO> getDaAssetAuditRuleList() {
        return daAssetAuditRuleMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetAuditRuleDO> getDaAssetAuditRuleMap() {
        List<DaAssetAuditRuleDO> daAssetAuditRuleList = daAssetAuditRuleMapper.selectList();
        return daAssetAuditRuleList.stream()
                .collect(Collectors.toMap(
                        DaAssetAuditRuleDO::getId,
                        daAssetAuditRuleDO -> daAssetAuditRuleDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import Data Asset Quality Result Record data
     *
     * @param importExcelList Data Asset Quality Result Record data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName Operator name
     * @return result
     */
    @Override
    public String importDaAssetAuditRule(List<DaAssetAuditRuleRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetAuditRuleRespVO respVO : importExcelList) {
            try {
                DaAssetAuditRuleDO daAssetAuditRuleDO = BeanUtils.toBean(respVO, DaAssetAuditRuleDO.class);
                Long daAssetAuditRuleId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetAuditRuleId != null) {
                        DaAssetAuditRuleDO existingDaAssetAuditRule = daAssetAuditRuleMapper.selectById(daAssetAuditRuleId);
                        if (existingDaAssetAuditRule != null) {
                            daAssetAuditRuleMapper.updateById(daAssetAuditRuleDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "Data update successful, ID {0} {1} record.", daAssetAuditRuleId, MessageUtils.messageWithFallback("da.entity.asset.quality.result", "Data asset quality result")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", daAssetAuditRuleId, MessageUtils.messageWithFallback("da.entity.asset.quality.result", "Data asset quality result")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DaAssetAuditRuleDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetAuditRuleId);
                    DaAssetAuditRuleDO existingDaAssetAuditRule = daAssetAuditRuleMapper.selectOne(queryWrapper);
                    if (existingDaAssetAuditRule == null) {
                        daAssetAuditRuleMapper.insert(daAssetAuditRuleDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", daAssetAuditRuleId, MessageUtils.messageWithFallback("da.entity.asset.quality.result", "Data asset quality result")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", daAssetAuditRuleId, MessageUtils.messageWithFallback("da.entity.asset.quality.result", "Data asset quality result")));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                    "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                    failureNum, failureDetails));
            throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                    "Congratulations! All data imported! Total: {0} records.", successNum));
        }
        return resultMsg.toString();
    }
}
