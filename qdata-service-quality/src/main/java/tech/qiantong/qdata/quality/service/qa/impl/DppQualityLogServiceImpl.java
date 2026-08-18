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
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityLogDO;
import tech.qiantong.qdata.quality.dal.mapper.qa.DppQualityLogMapper;
import tech.qiantong.qdata.quality.service.qa.IDppQualityLogService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Data quality log Service business layer processing
 *
 * @author qdata
 * @date 2025-07-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppQualityLogServiceImpl  extends ServiceImpl<DppQualityLogMapper, DppQualityLogDO> implements IDppQualityLogService {
    @Resource
    private DppQualityLogMapper dppQualityLogMapper;

    @Override
    public PageResult<DppQualityLogDO> getDppQualityLogPage(DppQualityLogPageReqVO pageReqVO) {
        return dppQualityLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppQualityLog(DppQualityLogSaveReqVO createReqVO) {
        DppQualityLogDO dictType = BeanUtils.toBean(createReqVO, DppQualityLogDO.class);
        dppQualityLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppQualityLog(DppQualityLogSaveReqVO updateReqVO) {
        // Related verification

        // Update data quality log
        DppQualityLogDO updateObj = BeanUtils.toBean(updateReqVO, DppQualityLogDO.class);
        return dppQualityLogMapper.updateById(updateObj);
    }
    @Override
    public int removeDppQualityLog(Collection<Long> idList) {
        // Delete data quality logs in batches
        return dppQualityLogMapper.deleteBatchIds(idList);
    }

    @Override
    public DppQualityLogDO getDppQualityLogById(Long id) {
        return dppQualityLogMapper.selectById(id);
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
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data quality log data
         *
         * @param importExcelList Data quality log data list
         * @param isUpdateSupport Whether to update support, if it already exists, update the data
         * @param operName operating user
         * @return result
         */
        @Override
        public String importDppQualityLog(List<DppQualityLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("quality.error.import.empty", "Import data cannot be empty!");
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
                                successMessages.add(MessageUtils.messageWithFallback("quality.import.update.success",
                                        "Data update successful, ID {0} {1} record.", dppQualityLogId, MessageUtils.messageWithFallback("quality.entity.log", "Data quality log")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", dppQualityLogId, MessageUtils.messageWithFallback("quality.entity.log", "Data quality log")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DppQualityLogDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppQualityLogId);
                        DppQualityLogDO existingDppQualityLog = dppQualityLogMapper.selectOne(queryWrapper);
                        if (existingDppQualityLog == null) {
                            dppQualityLogMapper.insert(dppQualityLogDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("quality.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", dppQualityLogId, MessageUtils.messageWithFallback("quality.entity.log", "Data quality log")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("quality.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", dppQualityLogId, MessageUtils.messageWithFallback("quality.entity.log", "Data quality log")));
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
