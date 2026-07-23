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
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluatePageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluateRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluateSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskEvaluateDO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskObjDO;
import tech.qiantong.qdata.quality.dal.mapper.qa.DppQualityTaskEvaluateMapper;
import tech.qiantong.qdata.quality.service.qa.IDppQualityTaskEvaluateService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Data quality task-evaluation rules Service business layer processing
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppQualityTaskEvaluateServiceImpl  extends ServiceImpl<DppQualityTaskEvaluateMapper, DppQualityTaskEvaluateDO> implements IDppQualityTaskEvaluateService {
    @Resource
    private DppQualityTaskEvaluateMapper dppQualityTaskEvaluateMapper;

    @Override
    public PageResult<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluatePage(DppQualityTaskEvaluatePageReqVO pageReqVO) {
        return dppQualityTaskEvaluateMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppQualityTaskEvaluate(DppQualityTaskEvaluateSaveReqVO createReqVO) {
        DppQualityTaskEvaluateDO dictType = BeanUtils.toBean(createReqVO, DppQualityTaskEvaluateDO.class);
        dppQualityTaskEvaluateMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppQualityTaskEvaluate(DppQualityTaskEvaluateSaveReqVO updateReqVO) {
        // Related verification

        // Update data quality task-evaluation rules
        DppQualityTaskEvaluateDO updateObj = BeanUtils.toBean(updateReqVO, DppQualityTaskEvaluateDO.class);
        return dppQualityTaskEvaluateMapper.updateById(updateObj);
    }
    @Override
    public int removeDppQualityTaskEvaluate(Collection<Long> idList) {
        // Deleting data quality tasks in batches-evaluation rules
        return dppQualityTaskEvaluateMapper.deleteBatchIds(idList);
    }

    @Override
    public DppQualityTaskEvaluateDO getDppQualityTaskEvaluateById(Long id) {
        return dppQualityTaskEvaluateMapper.selectById(id);
    }

    @Override
    public List<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateList(List<Long> idList) {
        LambdaQueryWrapperX<DppQualityTaskEvaluateDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.in(DppQualityTaskEvaluateDO::getObjId, idList)
                .eq(DppQualityTaskEvaluateDO::getDelFlag,"0");
        return dppQualityTaskEvaluateMapper.selectList(queryWrapperX);
    }

    @Override
    public List<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateList() {
        return dppQualityTaskEvaluateMapper.selectList();
    }

    @Override
    public Map<Long, DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateMap() {
        List<DppQualityTaskEvaluateDO> dppQualityTaskEvaluateList = dppQualityTaskEvaluateMapper.selectList();
        return dppQualityTaskEvaluateList.stream()
                .collect(Collectors.toMap(
                        DppQualityTaskEvaluateDO::getId,
                        dppQualityTaskEvaluateDO -> dppQualityTaskEvaluateDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data quality task-evaluation rule data
         *
         * @param importExcelList Data quality task-evaluation rule data list
         * @param isUpdateSupport Whether to update support, if it already exists, update the data
         * @param operName operating user
         * @return result
         */
        @Override
        public String importDppQualityTaskEvaluate(List<DppQualityTaskEvaluateRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("quality.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DppQualityTaskEvaluateRespVO respVO : importExcelList) {
                try {
                    DppQualityTaskEvaluateDO dppQualityTaskEvaluateDO = BeanUtils.toBean(respVO, DppQualityTaskEvaluateDO.class);
                    Long dppQualityTaskEvaluateId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dppQualityTaskEvaluateId != null) {
                            DppQualityTaskEvaluateDO existingDppQualityTaskEvaluate = dppQualityTaskEvaluateMapper.selectById(dppQualityTaskEvaluateId);
                            if (existingDppQualityTaskEvaluate != null) {
                                dppQualityTaskEvaluateMapper.updateById(dppQualityTaskEvaluateDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("quality.import.update.success",
                                        "Data update successful, ID {0} {1} record.", dppQualityTaskEvaluateId, MessageUtils.messageWithFallback("quality.entity.task.evaluation.rule", "Data quality task evaluation rule")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", dppQualityTaskEvaluateId, MessageUtils.messageWithFallback("quality.entity.task.evaluation.rule", "Data quality task evaluation rule")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DppQualityTaskEvaluateDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppQualityTaskEvaluateId);
                        DppQualityTaskEvaluateDO existingDppQualityTaskEvaluate = dppQualityTaskEvaluateMapper.selectOne(queryWrapper);
                        if (existingDppQualityTaskEvaluate == null) {
                            dppQualityTaskEvaluateMapper.insert(dppQualityTaskEvaluateDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("quality.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", dppQualityTaskEvaluateId, MessageUtils.messageWithFallback("quality.entity.task.evaluation.rule", "Data quality task evaluation rule")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("quality.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", dppQualityTaskEvaluateId, MessageUtils.messageWithFallback("quality.entity.task.evaluation.rule", "Data quality task evaluation rule")));
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
