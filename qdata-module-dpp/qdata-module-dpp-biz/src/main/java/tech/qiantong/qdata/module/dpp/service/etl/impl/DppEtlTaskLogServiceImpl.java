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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskLogDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlTaskLogMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskLogService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Data Integration Task - Log Service business layer processing
 *
 * @author qdata
 * @date 2025-02-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlTaskLogServiceImpl  extends ServiceImpl<DppEtlTaskLogMapper,DppEtlTaskLogDO> implements IDppEtlTaskLogService {
    @Resource
    private DppEtlTaskLogMapper dppEtlTaskLogMapper;

    @Override
    public PageResult<DppEtlTaskLogDO> getDppEtlTaskLogPage(DppEtlTaskLogPageReqVO pageReqVO) {
        return dppEtlTaskLogMapper.selectPage(pageReqVO);
    }

    @Override
    public DppEtlTaskLogRespVO getDppEtlTaskLogById(DppEtlTaskLogPageReqVO reqVO) {
        MPJLambdaWrapper<DppEtlTaskLogDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppEtlTaskLogDO.class)
                .eq(StringUtils.isNotEmpty(reqVO.getCode()) , DppEtlTaskLogDO::getCode, reqVO.getCode())
                .eq(reqVO.getVersion() != null, DppEtlTaskLogDO::getVersion, reqVO.getVersion());
        DppEtlTaskLogDO dppEtlNodeLogDO = dppEtlTaskLogMapper.selectOne(wrapper);
        return BeanUtils.toBean(dppEtlNodeLogDO, DppEtlTaskLogRespVO.class);
    }

    @Override
    public Long createDppEtlTaskLog(DppEtlTaskLogSaveReqVO createReqVO) {
        DppEtlTaskLogDO dictType = BeanUtils.toBean(createReqVO, DppEtlTaskLogDO.class);
        dppEtlTaskLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppEtlTaskLog(DppEtlTaskLogSaveReqVO updateReqVO) {
        // Validate

        // Update Data Integration Task - Log
        DppEtlTaskLogDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlTaskLogDO.class);
        return dppEtlTaskLogMapper.updateById(updateObj);
    }
    @Override
    public int removeDppEtlTaskLog(Collection<Long> idList) {
        // Batch delete Data Integration Task - Log
        return dppEtlTaskLogMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEtlTaskLogDO getDppEtlTaskLogById(Long id) {
        return dppEtlTaskLogMapper.selectById(id);
    }

    @Override
    public List<DppEtlTaskLogDO> getDppEtlTaskLogList() {
        return dppEtlTaskLogMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlTaskLogDO> getDppEtlTaskLogMap() {
        List<DppEtlTaskLogDO> dppEtlTaskLogList = dppEtlTaskLogMapper.selectList();
        return dppEtlTaskLogList.stream()
                .collect(Collectors.toMap(
                        DppEtlTaskLogDO::getId,
                        dppEtlTaskLogDO -> dppEtlTaskLogDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import Data Integration Task - Log data
         *
         * @param importExcelList Data Integration Task - Log data list
         * @param isUpdateSupport whether to support update; if already exists, update the data
         * @param operName operator user
         * @return result
         */
        @Override
        public String importDppEtlTaskLog(List<DppEtlTaskLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dpp.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DppEtlTaskLogRespVO respVO : importExcelList) {
                try {
                    DppEtlTaskLogDO dppEtlTaskLogDO = BeanUtils.toBean(respVO, DppEtlTaskLogDO.class);
                    Long dppEtlTaskLogId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dppEtlTaskLogId != null) {
                            DppEtlTaskLogDO existingDppEtlTaskLog = dppEtlTaskLogMapper.selectById(dppEtlTaskLogId);
                            if (existingDppEtlTaskLog != null) {
                                dppEtlTaskLogMapper.updateById(dppEtlTaskLogDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dpp.import.update.success",
                                        "Data update successful, ID {0} {1} record.", dppEtlTaskLogId, MessageUtils.messageWithFallback("dpp.entity.etl.task.log", "Data integration task log")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", dppEtlTaskLogId, MessageUtils.messageWithFallback("dpp.entity.etl.task.log", "Data integration task log")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DppEtlTaskLogDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppEtlTaskLogId);
                        DppEtlTaskLogDO existingDppEtlTaskLog = dppEtlTaskLogMapper.selectOne(queryWrapper);
                        if (existingDppEtlTaskLog == null) {
                            dppEtlTaskLogMapper.insert(dppEtlTaskLogDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", dppEtlTaskLogId, MessageUtils.messageWithFallback("dpp.entity.etl.task.log", "Data integration task log")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", dppEtlTaskLogId, MessageUtils.messageWithFallback("dpp.entity.etl.task.log", "Data integration task log")));
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
    public Integer queryMaxVersionByCode(String taskCode) {
        return baseMapper.queryMaxVersionByCode(taskCode);
    }
}
