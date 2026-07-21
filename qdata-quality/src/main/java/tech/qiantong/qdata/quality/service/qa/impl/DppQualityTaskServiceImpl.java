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
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskDO;
import tech.qiantong.qdata.quality.dal.mapper.qa.DppQualityTaskMapper;
import tech.qiantong.qdata.quality.service.qa.IDppQualityTaskEvaluateService;
import tech.qiantong.qdata.quality.service.qa.IDppQualityTaskObjService;
import tech.qiantong.qdata.quality.service.qa.IDppQualityTaskService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Data quality task Service business layer processing
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppQualityTaskServiceImpl  extends ServiceImpl<DppQualityTaskMapper, DppQualityTaskDO> implements IDppQualityTaskService {
    @Resource
    private DppQualityTaskMapper dppQualityTaskMapper;

    @Override
    public PageResult<DppQualityTaskDO> getDppQualityTaskPage(DppQualityTaskPageReqVO pageReqVO) {
        return dppQualityTaskMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppQualityTask(DppQualityTaskSaveReqVO createReqVO) {
        DppQualityTaskDO dictType = BeanUtils.toBean(createReqVO, DppQualityTaskDO.class);
        dppQualityTaskMapper.insert(dictType);


        return dictType.getId();
    }

    @Override
    public int updateDppQualityTask(DppQualityTaskSaveReqVO updateReqVO) {
        // Related verification

        // Update data quality tasks
        DppQualityTaskDO updateObj = BeanUtils.toBean(updateReqVO, DppQualityTaskDO.class);
        return dppQualityTaskMapper.updateById(updateObj);
    }
    @Override
    public int removeDppQualityTask(Collection<Long> idList) {
        // Deleting data quality tasks in batches
        return dppQualityTaskMapper.deleteBatchIds(idList);
    }

    @Override
    public DppQualityTaskRespVO getDppQualityTaskById(Long id) {
        DppQualityTaskDO dppQualityTaskDO = dppQualityTaskMapper.selectById(id);

        DppQualityTaskRespVO bean = BeanUtils.toBean(dppQualityTaskDO, DppQualityTaskRespVO.class);
        return bean;
    }

    @Override
    public List<DppQualityTaskDO> getDppQualityTaskList() {
        return dppQualityTaskMapper.selectList();
    }

    @Override
    public Map<Long, DppQualityTaskDO> getDppQualityTaskMap() {
        List<DppQualityTaskDO> dppQualityTaskList = dppQualityTaskMapper.selectList();
        return dppQualityTaskList.stream()
                .collect(Collectors.toMap(
                        DppQualityTaskDO::getId,
                        dppQualityTaskDO -> dppQualityTaskDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data quality task data
         *
         * @param importExcelList Data quality task data list
         * @param isUpdateSupport Whether to update support, if it already exists, update the data
         * @param operName operating user
         * @return result
         */
        @Override
        public String importDppQualityTask(List<DppQualityTaskRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("quality.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DppQualityTaskRespVO respVO : importExcelList) {
                try {
                    DppQualityTaskDO dppQualityTaskDO = BeanUtils.toBean(respVO, DppQualityTaskDO.class);
                    Long dppQualityTaskId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dppQualityTaskId != null) {
                            DppQualityTaskDO existingDppQualityTask = dppQualityTaskMapper.selectById(dppQualityTaskId);
                            if (existingDppQualityTask != null) {
                                dppQualityTaskMapper.updateById(dppQualityTaskDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("quality.import.update.success",
                                        "Data update successful, ID {0} {1} record.", dppQualityTaskId, MessageUtils.messageWithFallback("quality.entity.task", "Data quality task")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", dppQualityTaskId, MessageUtils.messageWithFallback("quality.entity.task", "Data quality task")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DppQualityTaskDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppQualityTaskId);
                        DppQualityTaskDO existingDppQualityTask = dppQualityTaskMapper.selectOne(queryWrapper);
                        if (existingDppQualityTask == null) {
                            dppQualityTaskMapper.insert(dppQualityTaskDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("quality.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", dppQualityTaskId, MessageUtils.messageWithFallback("quality.entity.task", "Data quality task")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("quality.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", dppQualityTaskId, MessageUtils.messageWithFallback("quality.entity.task", "Data quality task")));
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
