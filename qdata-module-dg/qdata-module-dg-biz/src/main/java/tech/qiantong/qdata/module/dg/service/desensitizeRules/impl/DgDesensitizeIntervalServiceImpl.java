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
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeIntervalDO;
import tech.qiantong.qdata.module.dg.dal.mapper.desensitizeRules.DgDesensitizeIntervalMapper;
import tech.qiantong.qdata.module.dg.service.desensitizeRules.IDgDesensitizeIntervalService;
/**
 * Desensitize Interval Service Business Layer Processing
 *
 * @author qdata
 * @date 2026-04-10
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDesensitizeIntervalServiceImpl  extends ServiceImpl<DgDesensitizeIntervalMapper,DgDesensitizeIntervalDO> implements IDgDesensitizeIntervalService {
    @Resource
    private DgDesensitizeIntervalMapper dgDesensitizeIntervalMapper;

    @Override
    public PageResult<DgDesensitizeIntervalDO> getDgDesensitizeIntervalPage(DgDesensitizeIntervalPageReqVO pageReqVO) {
        return dgDesensitizeIntervalMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgDesensitizeInterval(DgDesensitizeIntervalSaveReqVO createReqVO) {
        DgDesensitizeIntervalDO dictType = BeanUtils.toBean(createReqVO, DgDesensitizeIntervalDO.class);
        dgDesensitizeIntervalMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDesensitizeInterval(DgDesensitizeIntervalSaveReqVO updateReqVO) {
        // Related validation

        // Update desensitize interval
        DgDesensitizeIntervalDO updateObj = BeanUtils.toBean(updateReqVO, DgDesensitizeIntervalDO.class);
        return dgDesensitizeIntervalMapper.updateById(updateObj);
    }
    @Override
    public int removeDgDesensitizeInterval(Collection<Long> idList) {
        // Batch delete desensitize intervals
        return dgDesensitizeIntervalMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDesensitizeIntervalDO getDgDesensitizeIntervalById(Long id) {
        return dgDesensitizeIntervalMapper.selectById(id);
    }

    @Override
    public List<DgDesensitizeIntervalDO> getDgDesensitizeIntervalList() {
        return dgDesensitizeIntervalMapper.selectList();
    }

    @Override
    public Map<Long, DgDesensitizeIntervalDO> getDgDesensitizeIntervalMap() {
        List<DgDesensitizeIntervalDO> dgDesensitizeIntervalList = dgDesensitizeIntervalMapper.selectList();
        return dgDesensitizeIntervalList.stream()
                .collect(Collectors.toMap(
                        DgDesensitizeIntervalDO::getId,
                        dgDesensitizeIntervalDO -> dgDesensitizeIntervalDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import desensitize interval data
         *
         * @param importExcelList Desensitize interval data list
         * @param isUpdateSupport Whether to update support, if already exists, update the data
         * @param operName        Operator user
         * @return Result
         */
        @Override
        public String importDgDesensitizeInterval(List<DgDesensitizeIntervalRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dg.error.import.empty", "Imported data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DgDesensitizeIntervalRespVO respVO : importExcelList) {
                try {
                    DgDesensitizeIntervalDO dgDesensitizeIntervalDO = BeanUtils.toBean(respVO, DgDesensitizeIntervalDO.class);
                    Long dgDesensitizeIntervalId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dgDesensitizeIntervalId != null) {
                            DgDesensitizeIntervalDO existingDgDesensitizeInterval = dgDesensitizeIntervalMapper.selectById(dgDesensitizeIntervalId);
                            if (existingDgDesensitizeInterval != null) {
                                dgDesensitizeIntervalMapper.updateById(dgDesensitizeIntervalDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dg.import.update.success",
                                        "Data updated successfully, desensitization interval record with ID " + dgDesensitizeIntervalId + ".", dgDesensitizeIntervalId, "Desensitization Interval"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.fail",
                                        "Data update failed, desensitization interval record with ID " + dgDesensitizeIntervalId + " does not exist.", dgDesensitizeIntervalId, "Desensitization Interval"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.id.missing",
                                    "Data update failed, a record's ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DgDesensitizeIntervalDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dgDesensitizeIntervalId);
                        DgDesensitizeIntervalDO existingDgDesensitizeInterval = dgDesensitizeIntervalMapper.selectOne(queryWrapper);
                        if (existingDgDesensitizeInterval == null) {
                            dgDesensitizeIntervalMapper.insert(dgDesensitizeIntervalDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dg.import.insert.success",
                                    "Data inserted successfully, desensitization interval record with ID " + dgDesensitizeIntervalId + ".", dgDesensitizeIntervalId, "Desensitization Interval"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.insert.fail",
                                    "Data insertion failed, desensitization interval record with ID " + dgDesensitizeIntervalId + " already exists.", dgDesensitizeIntervalId, "Desensitization Interval"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("dg.import.error.detail",
                "Data import failed, error message: " + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.fail",
                        "Sorry, import failed! A total of " + failureNum + " records have incorrect format, errors as follows:<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("dg.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.success",
                        "Congratulations, all data imported successfully! Total: " + successNum + " records.", successNum));
            }
            return resultMsg.toString();
        }
}
