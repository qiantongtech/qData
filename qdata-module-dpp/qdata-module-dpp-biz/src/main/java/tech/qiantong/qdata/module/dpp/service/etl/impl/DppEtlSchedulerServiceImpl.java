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
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSchedulerDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlSchedulerMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlSchedulerService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Data Integration Schedule Info Service Business Layer Processing
 *
 * @author qdata
 * @date 2025-02-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlSchedulerServiceImpl  extends ServiceImpl<DppEtlSchedulerMapper,DppEtlSchedulerDO> implements IDppEtlSchedulerService {
    @Resource
    private DppEtlSchedulerMapper dppEtlSchedulerMapper;

    @Override
    public PageResult<DppEtlSchedulerDO> getDppEtlSchedulerPage(DppEtlSchedulerPageReqVO pageReqVO) {
        return dppEtlSchedulerMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppEtlScheduler(DppEtlSchedulerSaveReqVO createReqVO) {
        DppEtlSchedulerDO dictType = BeanUtils.toBean(createReqVO, DppEtlSchedulerDO.class);
        dppEtlSchedulerMapper.insert(dictType);
        return dictType.getId();
    }


    @Override
    public DppEtlSchedulerDO createDppEtlSchedulerNew(DppEtlSchedulerSaveReqVO createReqVO) {
        DppEtlSchedulerDO dictType = BeanUtils.toBean(createReqVO, DppEtlSchedulerDO.class);
        dppEtlSchedulerMapper.insert(dictType);
        return dictType;
    }

    @Override
    public int updateDppEtlScheduler(DppEtlSchedulerSaveReqVO updateReqVO) {
        // Related validation

        // Update data integration schedule info
        DppEtlSchedulerDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlSchedulerDO.class);
        return dppEtlSchedulerMapper.updateById(updateObj);
    }
    @Override
    public int removeDppEtlScheduler(Collection<Long> idList) {
        // Batch delete data integration schedule info
        return dppEtlSchedulerMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEtlSchedulerDO getDppEtlSchedulerById(Long id) {
        return dppEtlSchedulerMapper.selectById(id);
    }

    @Override
    public DppEtlSchedulerDO getDppEtlSchedulerById(DppEtlSchedulerPageReqVO reqVO) {
        MPJLambdaWrapper<DppEtlSchedulerDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppEtlSchedulerDO.class)
                .eq(reqVO.getTaskId() != null, DppEtlSchedulerDO::getTaskId, reqVO.getTaskId())
                .eq(reqVO.getTaskCode() != null, DppEtlSchedulerDO::getTaskCode, reqVO.getTaskCode())
                .eq(reqVO.getDsId() != null, DppEtlSchedulerDO::getDsId, reqVO.getDsId())
                .eq(reqVO.getId() != null, DppEtlSchedulerDO::getId, reqVO.getId())
                .eq(StringUtils.isNotBlank(reqVO.getTaskCode()), DppEtlSchedulerDO::getTaskCode, reqVO.getTaskCode());
        return dppEtlSchedulerMapper.selectOne(wrapper);
    }

    @Override
    public List<DppEtlSchedulerDO> getDppEtlSchedulerList() {
        return dppEtlSchedulerMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlSchedulerDO> getDppEtlSchedulerMap() {
        List<DppEtlSchedulerDO> dppEtlSchedulerList = dppEtlSchedulerMapper.selectList();
        return dppEtlSchedulerList.stream()
                .collect(Collectors.toMap(
                        DppEtlSchedulerDO::getId,
                        dppEtlSchedulerDO -> dppEtlSchedulerDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data integration schedule info data
         *
         * @param importExcelList data integration schedule info data list
         * @param isUpdateSupport whether update is supported, if already exists, update the data
         * @param operName operator name
         * @return result
         */
        @Override
        public String importDppEtlScheduler(List<DppEtlSchedulerRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dpp.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DppEtlSchedulerRespVO respVO : importExcelList) {
                try {
                    DppEtlSchedulerDO dppEtlSchedulerDO = BeanUtils.toBean(respVO, DppEtlSchedulerDO.class);
                    Long dppEtlSchedulerId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dppEtlSchedulerId != null) {
                            DppEtlSchedulerDO existingDppEtlScheduler = dppEtlSchedulerMapper.selectById(dppEtlSchedulerId);
                            if (existingDppEtlScheduler != null) {
                                dppEtlSchedulerMapper.updateById(dppEtlSchedulerDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dpp.import.update.success",
                                        "数据更新成功，ID为 " + dppEtlSchedulerId + " 的数据集成调度信息记录。", dppEtlSchedulerId, "数据集成调度信息"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.fail",
                                        "数据更新失败，ID为 " + dppEtlSchedulerId + " 的数据集成调度信息记录不存在。", dppEtlSchedulerId, "数据集成调度信息"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DppEtlSchedulerDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppEtlSchedulerId);
                        DppEtlSchedulerDO existingDppEtlScheduler = dppEtlSchedulerMapper.selectOne(queryWrapper);
                        if (existingDppEtlScheduler == null) {
                            dppEtlSchedulerMapper.insert(dppEtlSchedulerDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.success",
                                    "数据插入成功，ID为 " + dppEtlSchedulerId + " 的数据集成调度信息记录。", dppEtlSchedulerId, "数据集成调度信息"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.fail",
                                    "数据插入失败，ID为 " + dppEtlSchedulerId + " 的数据集成调度信息记录已存在。", dppEtlSchedulerId, "数据集成调度信息"));
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
