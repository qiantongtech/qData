/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
 * 数据集成调度信息Service业务层处理
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
        // 相关校验

        // 更新数据集成调度信息
        DppEtlSchedulerDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlSchedulerDO.class);
        return dppEtlSchedulerMapper.updateById(updateObj);
    }
    @Override
    public int removeDppEtlScheduler(Collection<Long> idList) {
        // 批量删除数据集成调度信息
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
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据集成调度信息数据
         *
         * @param importExcelList 数据集成调度信息数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
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
