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

package tech.qiantong.qdata.module.dpp.service.qa.impl;

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
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluatePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluateRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluateSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskEvaluateDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.qa.DppQualityTaskEvaluateMapper;
import tech.qiantong.qdata.module.dpp.service.qa.IDppQualityTaskEvaluateService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Data Quality Task - Evaluation Rule Service business layer processing
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppQualityTaskEvaluateServiceImpl  extends ServiceImpl<DppQualityTaskEvaluateMapper,DppQualityTaskEvaluateDO> implements IDppQualityTaskEvaluateService {
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
        // Validate

        // Update Data Quality Task - Evaluation Rule
        DppQualityTaskEvaluateDO updateObj = BeanUtils.toBean(updateReqVO, DppQualityTaskEvaluateDO.class);
        return dppQualityTaskEvaluateMapper.updateById(updateObj);
    }
    @Override
    public int removeDppQualityTaskEvaluate(Collection<Long> idList) {
        // Batch delete Data Quality Task - Evaluation Rule
        return dppQualityTaskEvaluateMapper.deleteBatchIds(idList);
    }

    @Override
    public DppQualityTaskEvaluateDO getDppQualityTaskEvaluateById(Long id) {
        return dppQualityTaskEvaluateMapper.selectById(id);
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
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import Data Quality Task - Evaluation Rule data
         *
         * @param importExcelList Data Quality Task - Evaluation Rule data list
         * @param isUpdateSupport whether to support update; if already exists, update the data
         * @param operName operator user
         * @return result
         */
        @Override
        public String importDppQualityTaskEvaluate(List<DppQualityTaskEvaluateRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dpp.error.import.empty", "导入数据不能为空！");
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
                                successMessages.add(MessageUtils.messageWithFallback("dpp.import.update.success",
                                        "数据更新成功，ID为 " + dppQualityTaskEvaluateId + " 的数据质量任务-评测规则记录。", dppQualityTaskEvaluateId, "数据质量任务-评测规则"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.fail",
                                        "数据更新失败，ID为 " + dppQualityTaskEvaluateId + " 的数据质量任务-评测规则记录不存在。", dppQualityTaskEvaluateId, "数据质量任务-评测规则"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DppQualityTaskEvaluateDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppQualityTaskEvaluateId);
                        DppQualityTaskEvaluateDO existingDppQualityTaskEvaluate = dppQualityTaskEvaluateMapper.selectOne(queryWrapper);
                        if (existingDppQualityTaskEvaluate == null) {
                            dppQualityTaskEvaluateMapper.insert(dppQualityTaskEvaluateDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.success",
                                    "数据插入成功，ID为 " + dppQualityTaskEvaluateId + " 的数据质量任务-评测规则记录。", dppQualityTaskEvaluateId, "数据质量任务-评测规则"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.fail",
                                    "数据插入失败，ID为 " + dppQualityTaskEvaluateId + " 的数据质量任务-评测规则记录已存在。", dppQualityTaskEvaluateId, "数据质量任务-评测规则"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("dpp.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.fail",
                        "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("dpp.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.success",
                        "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
            }
            return resultMsg.toString();
        }
}
