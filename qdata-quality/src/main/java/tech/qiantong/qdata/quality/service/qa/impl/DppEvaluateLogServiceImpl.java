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

package tech.qiantong.qdata.quality.service.qa.impl;

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
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppEvaluateLogDO;
import tech.qiantong.qdata.quality.dal.mapper.qa.DppEvaluateLogMapper;
import tech.qiantong.qdata.quality.service.qa.IDppEvaluateLogService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Evaluation rule results Service business layer processing
 *
 * @author qdata
 * @date 2025-07-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEvaluateLogServiceImpl  extends ServiceImpl<DppEvaluateLogMapper, DppEvaluateLogDO> implements IDppEvaluateLogService {
    @Resource
    private DppEvaluateLogMapper dppEvaluateLogMapper;

    @Override
    public PageResult<DppEvaluateLogDO> getDppEvaluateLogPage(DppEvaluateLogPageReqVO pageReqVO) {
        return dppEvaluateLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppEvaluateLog(DppEvaluateLogSaveReqVO createReqVO) {
        DppEvaluateLogDO dictType = BeanUtils.toBean(createReqVO, DppEvaluateLogDO.class);
        dppEvaluateLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppEvaluateLog(DppEvaluateLogSaveReqVO updateReqVO) {
        // Related verification

        // Update evaluation rule results
        DppEvaluateLogDO updateObj = BeanUtils.toBean(updateReqVO, DppEvaluateLogDO.class);
        return dppEvaluateLogMapper.updateById(updateObj);
    }
    @Override
    public int removeDppEvaluateLog(Collection<Long> idList) {
        // Delete evaluation rule results in batches
        return dppEvaluateLogMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEvaluateLogDO getDppEvaluateLogById(Long id) {
        return dppEvaluateLogMapper.selectById(id);
    }

    @Override
    public List<DppEvaluateLogDO> getDppEvaluateLogList() {
        return dppEvaluateLogMapper.selectList();
    }

    @Override
    public Map<Long, DppEvaluateLogDO> getDppEvaluateLogMap() {
        List<DppEvaluateLogDO> dppEvaluateLogList = dppEvaluateLogMapper.selectList();
        return dppEvaluateLogList.stream()
                .collect(Collectors.toMap(
                        DppEvaluateLogDO::getId,
                        dppEvaluateLogDO -> dppEvaluateLogDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import evaluation rule result data
         *
         * @param importExcelList Evaluation rule result data list
         * @param isUpdateSupport Whether to update support, if it already exists, update the data
         * @param operName operating user
         * @return result
         */
        @Override
        public String importDppEvaluateLog(List<DppEvaluateLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("quality.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DppEvaluateLogRespVO respVO : importExcelList) {
                try {
                    DppEvaluateLogDO dppEvaluateLogDO = BeanUtils.toBean(respVO, DppEvaluateLogDO.class);
                    Long dppEvaluateLogId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dppEvaluateLogId != null) {
                            DppEvaluateLogDO existingDppEvaluateLog = dppEvaluateLogMapper.selectById(dppEvaluateLogId);
                            if (existingDppEvaluateLog != null) {
                                dppEvaluateLogMapper.updateById(dppEvaluateLogDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("quality.import.update.success",
                                        "Data update successful, ID {0} {1} record.", dppEvaluateLogId, MessageUtils.messageWithFallback("quality.entity.evaluation.rule.result", "Evaluation rule result")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", dppEvaluateLogId, MessageUtils.messageWithFallback("quality.entity.evaluation.rule.result", "Evaluation rule result")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DppEvaluateLogDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppEvaluateLogId);
                        DppEvaluateLogDO existingDppEvaluateLog = dppEvaluateLogMapper.selectOne(queryWrapper);
                        if (existingDppEvaluateLog == null) {
                            dppEvaluateLogMapper.insert(dppEvaluateLogDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("quality.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", dppEvaluateLogId, MessageUtils.messageWithFallback("quality.entity.evaluation.rule.result", "Evaluation rule result")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("quality.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", dppEvaluateLogId, MessageUtils.messageWithFallback("quality.entity.evaluation.rule.result", "Evaluation rule result")));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("quality.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("quality.import.result.fail",
                        "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                        failureNum, failureDetails));
                throw new ServiceException("quality.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("quality.import.result.success",
                        "Congratulations! All data imported! Total: {0} records.", successNum));
            }
            return resultMsg.toString();
        }
}
