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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskNodeRelLogDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlTaskNodeRelLogMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskNodeRelLogService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Data Integration Task-Node Relation - Log Service business layer processing
 *
 * @author qdata
 * @date 2025-02-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlTaskNodeRelLogServiceImpl  extends ServiceImpl<DppEtlTaskNodeRelLogMapper,DppEtlTaskNodeRelLogDO> implements IDppEtlTaskNodeRelLogService {
    @Resource
    private DppEtlTaskNodeRelLogMapper dppEtlTaskNodeRelLogMapper;

    @Override
    public PageResult<DppEtlTaskNodeRelLogDO> getDppEtlTaskNodeRelLogPage(DppEtlTaskNodeRelLogPageReqVO pageReqVO) {
        return dppEtlTaskNodeRelLogMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DppEtlTaskNodeRelLogRespVO> getDppEtlTaskNodeRelLogRespVOList(DppEtlTaskNodeRelLogPageReqVO reqVO) {
        MPJLambdaWrapper<DppEtlTaskNodeRelLogDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppEtlTaskNodeRelLogDO.class)
                .eq(reqVO.getTaskId() != null, DppEtlTaskNodeRelLogDO::getTaskId, reqVO.getTaskId())
                .eq(reqVO.getTaskVersion() != null, DppEtlTaskNodeRelLogDO::getTaskVersion, reqVO.getTaskVersion())
                .eq(StringUtils.isNotBlank(reqVO.getTaskCode()), DppEtlTaskNodeRelLogDO::getTaskCode, reqVO.getTaskCode());
        List<DppEtlTaskNodeRelLogDO> dppEtlTaskNodeRelDOS = dppEtlTaskNodeRelLogMapper.selectList(wrapper);
        return BeanUtils.toBean(dppEtlTaskNodeRelDOS, DppEtlTaskNodeRelLogRespVO.class);
    }


    @Override
    public DppEtlTaskNodeRelLogRespVO getDppEtlTaskNodeRelLogById(DppEtlTaskNodeRelLogPageReqVO reqVO) {
        MPJLambdaWrapper<DppEtlTaskNodeRelLogDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppEtlTaskNodeRelLogDO.class)
                .eq(reqVO.getTaskId() != null, DppEtlTaskNodeRelLogDO::getTaskId, reqVO.getTaskId())
                .eq(reqVO.getTaskVersion() != null, DppEtlTaskNodeRelLogDO::getTaskVersion, reqVO.getTaskVersion())
                .eq(StringUtils.isNotBlank(reqVO.getTaskCode()), DppEtlTaskNodeRelLogDO::getTaskCode, reqVO.getTaskCode());
        DppEtlTaskNodeRelLogDO dppEtlTaskNodeRelLogDO = dppEtlTaskNodeRelLogMapper.selectOne(wrapper);
        return BeanUtils.toBean(dppEtlTaskNodeRelLogDO, DppEtlTaskNodeRelLogRespVO.class);
    }

    @Override
    public Long createDppEtlTaskNodeRelLog(DppEtlTaskNodeRelLogSaveReqVO createReqVO) {
        DppEtlTaskNodeRelLogDO dictType = BeanUtils.toBean(createReqVO, DppEtlTaskNodeRelLogDO.class);
        dictType.setId(null);
        dppEtlTaskNodeRelLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public void createDppEtlTaskNodeRelLogBatch(List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS) {
        for (DppEtlTaskNodeRelLogSaveReqVO dppEtlTaskNodeRelLogSaveReqVO : dppEtlTaskNodeRelLogSaveReqVOS) {
            this.createDppEtlTaskNodeRelLog(dppEtlTaskNodeRelLogSaveReqVO);
        }
    }

    @Override
    public int updateDppEtlTaskNodeRelLog(DppEtlTaskNodeRelLogSaveReqVO updateReqVO) {
        // Validate

        // Update Data Integration Task-Node Relation - Log
        DppEtlTaskNodeRelLogDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlTaskNodeRelLogDO.class);
        return dppEtlTaskNodeRelLogMapper.updateById(updateObj);
    }
    @Override
    public int removeDppEtlTaskNodeRelLog(Collection<Long> idList) {
        // Batch delete Data Integration Task-Node Relation - Log
        return dppEtlTaskNodeRelLogMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEtlTaskNodeRelLogDO getDppEtlTaskNodeRelLogById(Long id) {
        return dppEtlTaskNodeRelLogMapper.selectById(id);
    }

    @Override
    public List<DppEtlTaskNodeRelLogDO> getDppEtlTaskNodeRelLogList() {
        return dppEtlTaskNodeRelLogMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlTaskNodeRelLogDO> getDppEtlTaskNodeRelLogMap() {
        List<DppEtlTaskNodeRelLogDO> dppEtlTaskNodeRelLogList = dppEtlTaskNodeRelLogMapper.selectList();
        return dppEtlTaskNodeRelLogList.stream()
                .collect(Collectors.toMap(
                        DppEtlTaskNodeRelLogDO::getId,
                        dppEtlTaskNodeRelLogDO -> dppEtlTaskNodeRelLogDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import Data Integration Task-Node Relation - Log data
         *
         * @param importExcelList Data Integration Task-Node Relation - Log data list
         * @param isUpdateSupport whether to support update; if already exists, update the data
         * @param operName operator user
         * @return result
         */
        @Override
        public String importDppEtlTaskNodeRelLog(List<DppEtlTaskNodeRelLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dpp.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DppEtlTaskNodeRelLogRespVO respVO : importExcelList) {
                try {
                    DppEtlTaskNodeRelLogDO dppEtlTaskNodeRelLogDO = BeanUtils.toBean(respVO, DppEtlTaskNodeRelLogDO.class);
                    Long dppEtlTaskNodeRelLogId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dppEtlTaskNodeRelLogId != null) {
                            DppEtlTaskNodeRelLogDO existingDppEtlTaskNodeRelLog = dppEtlTaskNodeRelLogMapper.selectById(dppEtlTaskNodeRelLogId);
                            if (existingDppEtlTaskNodeRelLog != null) {
                                dppEtlTaskNodeRelLogMapper.updateById(dppEtlTaskNodeRelLogDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dpp.import.update.success",
                                        "数据更新成功，ID为 " + dppEtlTaskNodeRelLogId + " 的数据集成任务节点关系-日志记录。", dppEtlTaskNodeRelLogId, "数据集成任务节点关系-日志"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.fail",
                                        "数据更新失败，ID为 " + dppEtlTaskNodeRelLogId + " 的数据集成任务节点关系-日志记录不存在。", dppEtlTaskNodeRelLogId, "数据集成任务节点关系-日志"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DppEtlTaskNodeRelLogDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppEtlTaskNodeRelLogId);
                        DppEtlTaskNodeRelLogDO existingDppEtlTaskNodeRelLog = dppEtlTaskNodeRelLogMapper.selectOne(queryWrapper);
                        if (existingDppEtlTaskNodeRelLog == null) {
                            dppEtlTaskNodeRelLogMapper.insert(dppEtlTaskNodeRelLogDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.success",
                                    "数据插入成功，ID为 " + dppEtlTaskNodeRelLogId + " 的数据集成任务节点关系-日志记录。", dppEtlTaskNodeRelLogId, "数据集成任务节点关系-日志"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.fail",
                                    "数据插入失败，ID为 " + dppEtlTaskNodeRelLogId + " 的数据集成任务节点关系-日志记录已存在。", dppEtlTaskNodeRelLogId, "数据集成任务节点关系-日志"));
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
