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

package tech.qiantong.qdata.module.dpp.service.etl.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskAssetReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppQualityLogMapper;
import tech.qiantong.qdata.module.dpp.dal.mapper.qa.DppQualityTaskMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEvaluateLogService;
import tech.qiantong.qdata.module.dpp.service.etl.IDppQualityLogService;
import tech.qiantong.qdata.module.system.api.message.dto.MessageSaveReqDTO;
import tech.qiantong.qdata.module.system.service.ISysMessageService;

import static tech.qiantong.qdata.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;

/**
 * Data Quality Log Service business layer processing
 *
 * @author qdata
 * @date 2025-07-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class DppQualityLogServiceImpl  extends ServiceImpl<DppQualityLogMapper,DppQualityLogDO> implements IDppQualityLogService {
    private final DppQualityLogMapper dppQualityLogMapper;
    private final DppQualityTaskMapper qualityTaskMapper;
    private final IDppEvaluateLogService dppEvaluateLogService;
    private final ISysMessageService messageService;

    @Override
    public PageResult<DppQualityLogDO> getDppQualityLogPage(DppQualityLogPageReqVO pageReqVO) {
        PageResult<DppQualityLogDO> dppQualityLogDOPageResult = dppQualityLogMapper.selectPage(pageReqVO);
        List<DppQualityLogDO> rows = (List<DppQualityLogDO>)dppQualityLogDOPageResult.getRows();
        List<DppQualityLogDO> dppQualityLogDOS = new ArrayList<>();
        for (DppQualityLogDO row : rows) {
            Map<String, Object> map = dppEvaluateLogService.sumTotalAndProblemTotalByTaskLogId(String.valueOf(row.getId()));

            // Get total and problem count (ensure null safety)
            Long total = map.get("total") == null ? 0L : (Long) map.get("total");
            Long problemTotal = map.get("problemTotal") == null ? 0L : (Long) map.get("problemTotal");

            // Calculate quality score (percentage, two decimal places)
            BigDecimal score = BigDecimal.ZERO;
            if (total > 0) {
                score = BigDecimal.valueOf(total - problemTotal)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
            }

            // Set score and problem count
            row.setScore(score.longValue());
            row.setProblemData(problemTotal);
            dppQualityLogDOS.add(row);
        }
        dppQualityLogDOPageResult.setRows(dppQualityLogDOS);
        return dppQualityLogDOPageResult;
    }

    @Override
    public Long createDppQualityLog(DppQualityLogSaveReqVO createReqVO) {
        DppQualityLogDO dictType = BeanUtils.toBean(createReqVO, DppQualityLogDO.class);
        dppQualityLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppQualityLog(DppQualityLogSaveReqVO updateReqVO) {
        // Validate

        // Update data quality log
        DppQualityLogDO updateObj = BeanUtils.toBean(updateReqVO, DppQualityLogDO.class);
        return dppQualityLogMapper.updateById(updateObj);
    }
    @Override
    public int removeDppQualityLog(Collection<Long> idList) {
        // Batch delete data quality log
        return dppQualityLogMapper.deleteBatchIds(idList);
    }

    @Override
    public DppQualityLogDO getDppQualityLogById(Long id) {
        return dppQualityLogMapper.selectById(id);
    }

    @Override
    public DppQualityLogDO selectPrevLogByIdWithWrapper(Long id) {
        return dppQualityLogMapper.selectPrevLogByIdWithWrapper(String.valueOf(id));
    }

    @Override
    public DppQualityLogDO getDppQualityLogById(DppQualityTaskAssetReqVO reqVO) {
        LambdaQueryWrapper<DppQualityLogDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DppQualityLogDO::getQualityId, reqVO.getId())
                .orderByDesc(DppQualityLogDO::getStartTime);

        Page<DppQualityLogDO> page = new Page<>(1, 1);
        IPage<DppQualityLogDO> resultPage = dppQualityLogMapper.selectPage(page, wrapper);

        return resultPage.getRecords().isEmpty() ? null : resultPage.getRecords().get(0);
    }

    @Override
    public List<DppQualityLogDO> getDppQualityLogList() {
        return dppQualityLogMapper.selectList();
    }

    @Override
    public Map<Long, DppQualityLogDO> getDppQualityLogMap() {
        List<DppQualityLogDO> dppQualityLogList = dppQualityLogMapper.selectList();
        return dppQualityLogList.stream()
                .collect(Collectors.toMap(
                        DppQualityLogDO::getId,
                        dppQualityLogDO -> dppQualityLogDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data quality log data
     *
     * @param importExcelList data quality log data list
     * @param isUpdateSupport whether to support update; if already exists, update the data
     * @param operName operator user
     * @return result
     */
    @Override
    public String importDppQualityLog(List<DppQualityLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dpp.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DppQualityLogRespVO respVO : importExcelList) {
            try {
                DppQualityLogDO dppQualityLogDO = BeanUtils.toBean(respVO, DppQualityLogDO.class);
                Long dppQualityLogId = respVO.getId();
                if (isUpdateSupport) {
                    if (dppQualityLogId != null) {
                        DppQualityLogDO existingDppQualityLog = dppQualityLogMapper.selectById(dppQualityLogId);
                        if (existingDppQualityLog != null) {
                            dppQualityLogMapper.updateById(dppQualityLogDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dpp.import.update.success",
                                    "Data update successful, ID {0} {1} record.", dppQualityLogId, MessageUtils.messageWithFallback("dpp.entity.quality.log", "Data quality log")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", dppQualityLogId, MessageUtils.messageWithFallback("dpp.entity.quality.log", "Data quality log")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DppQualityLogDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppQualityLogId);
                    DppQualityLogDO existingDppQualityLog = dppQualityLogMapper.selectOne(queryWrapper);
                    if (existingDppQualityLog == null) {
                        dppQualityLogMapper.insert(dppQualityLogDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", dppQualityLogId, MessageUtils.messageWithFallback("dpp.entity.quality.log", "Data quality log")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", dppQualityLogId, MessageUtils.messageWithFallback("dpp.entity.quality.log", "Data quality log")));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("dpp.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.fail",
                    "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                    failureNum, failureDetails));
            throw new ServiceException("dpp.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.success",
                    "Congratulations! All data imported! Total: {0} records.", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public void sendMessage(Long id) {
        DppQualityLogDO dppQualityLogDO = dppQualityLogMapper.selectById(id);
        DppQualityTaskDO dppQualityTaskDO = qualityTaskMapper.selectById(dppQualityLogDO.getQualityId());
        Map<String, Object> map = dppEvaluateLogService.sumTotalAndProblemTotalByTaskLogId(String.valueOf(dppQualityLogDO.getId()));
        // Get total and problem count (ensure null safety)
        Long total = map.get("total") == null ? 0L : (Long) map.get("total");
        Long problemTotal = map.get("problemTotal") == null ? 0L : (Long) map.get("problemTotal");

        // Calculate quality score (percentage, two decimal places)
        BigDecimal score = BigDecimal.ZERO;
        if (total > 0) {
            score = BigDecimal.valueOf(total - problemTotal)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
        }
        MessageSaveReqDTO messageSaveReqDTO = new MessageSaveReqDTO();
        messageSaveReqDTO.setReceiverId(Long.valueOf(dppQualityTaskDO.getContactId()));
        HashMap<String, Object> messageMeta = new HashMap<>();
        messageMeta.put("taskName", dppQualityTaskDO.getTaskName());
        messageMeta.put("executionTime", DateUtils.parseDateToStr(YYYY_MM_DD_HH_MM_SS,dppQualityLogDO.getEndTime()));
        messageMeta.put("qualityScore", score);
        messageMeta.put("totalNumber", total);
        messageMeta.put("errorNumber", problemTotal);
        messageService.send(7L, messageSaveReqDTO, messageMeta);
    }
}
