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

package tech.qiantong.qdata.module.ds.service.apiLog.impl;

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
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogPageReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogRespVO;
import tech.qiantong.qdata.module.ds.controller.admin.apiLog.vo.DsApiLogSaveReqVO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apiLog.DsApiLogDO;
import tech.qiantong.qdata.module.ds.dal.mapper.apiLog.DsApiLogMapper;
import tech.qiantong.qdata.module.ds.service.apiLog.IDsApiLogService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * API service call log service implementation
 *
 * @author lhs
 * @date 2025-02-12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DsApiLogServiceImpl  extends ServiceImpl<DsApiLogMapper,DsApiLogDO> implements IDsApiLogService {
    @Resource
    private DsApiLogMapper dsApiLogMapper;

    @Override
    public PageResult<DsApiLogDO> getDsApiLogPage(DsApiLogPageReqVO pageReqVO) {
        return dsApiLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDsApiLog(DsApiLogSaveReqVO createReqVO) {
        DsApiLogDO dictType = BeanUtils.toBean(createReqVO, DsApiLogDO.class);
        dsApiLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDsApiLog(DsApiLogSaveReqVO updateReqVO) {
        // Perform related validation.

        // Updates an API service call log.
        DsApiLogDO updateObj = BeanUtils.toBean(updateReqVO, DsApiLogDO.class);
        return dsApiLogMapper.updateById(updateObj);
    }
    @Override
    public int removeDsApiLog(Collection<Long> idList) {
        // Deletes API service call logs in batches.
        return dsApiLogMapper.deleteBatchIds(idList);
    }

    @Override
    public DsApiLogDO getDsApiLogById(Long id) {
        return dsApiLogMapper.selectDsApiLogByID(id);
    }

    @Override
    public List<DsApiLogDO> getDsApiLogList() {
        return dsApiLogMapper.selectList();
    }

    @Override
    public Map<Long, DsApiLogDO> getDsApiLogMap() {
        List<DsApiLogDO> dsApiLogList = dsApiLogMapper.selectList();
        return dsApiLogList.stream()
                .collect(Collectors.toMap(
                        DsApiLogDO::getId,
                        dsApiLogDO -> dsApiLogDO,
                        // Preserve the existing value.
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Imports API service call log data.
         *
         * @param importExcelList API service call log data list
         * @param isUpdateSupport whether existing records should be updated
         * @param operName operator
         * @return the result
         */
        @Override
        public String importDsApiLog(List<DsApiLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("ds.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DsApiLogRespVO respVO : importExcelList) {
                try {
                    DsApiLogDO dsApiLogDO = BeanUtils.toBean(respVO, DsApiLogDO.class);
                    Long dsApiLogId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dsApiLogId != null) {
                            DsApiLogDO existingDsApiLog = dsApiLogMapper.selectById(dsApiLogId);
                            if (existingDsApiLog != null) {
                                dsApiLogMapper.updateById(dsApiLogDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("ds.import.update.success",
                                        "Data update successful, ID {0} {1} record.", dsApiLogId, MessageUtils.messageWithFallback("ds.entity.api.call.log", "API service call log")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("ds.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", dsApiLogId, MessageUtils.messageWithFallback("ds.entity.api.call.log", "API service call log")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("ds.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DsApiLogDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dsApiLogId);
                        DsApiLogDO existingDsApiLog = dsApiLogMapper.selectOne(queryWrapper);
                        if (existingDsApiLog == null) {
                            dsApiLogMapper.insert(dsApiLogDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("ds.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", dsApiLogId, MessageUtils.messageWithFallback("ds.entity.api.call.log", "API service call log")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("ds.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", dsApiLogId, MessageUtils.messageWithFallback("ds.entity.api.call.log", "API service call log")));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("ds.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("ds.import.result.fail",
                        "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                        failureNum, failureDetails));
                throw new ServiceException("ds.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("ds.import.result.success",
                        "Congratulations! All data imported! Total: {0} records.", successNum));
            }
            return resultMsg.toString();
        }
}
