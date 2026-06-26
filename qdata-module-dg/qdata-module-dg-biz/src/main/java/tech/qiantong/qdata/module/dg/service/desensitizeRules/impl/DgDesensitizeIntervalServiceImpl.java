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

package tech.qiantong.qdata.module.dg.service.desensitizeRules.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeRules.vo.DgDesensitizeIntervalSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeIntervalDO;
import tech.qiantong.qdata.module.dg.dal.mapper.desensitizeRules.DgDesensitizeIntervalMapper;
import tech.qiantong.qdata.module.dg.service.desensitizeRules.IDgDesensitizeIntervalService;
/**
 * 脱敏区间Service业务层处理
 *
 * @author qdata
 * @date 2026-04-10
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDesensitizeIntervalServiceImpl  extends ServiceImpl<DgDesensitizeIntervalMapper,DgDesensitizeIntervalDO> implements IDgDesensitizeIntervalService {
    @Resource
    private DgDesensitizeIntervalMapper dgDesensitizeIntervalMapper;

    @Override
    public PageResult<DgDesensitizeIntervalDO> getDgDesensitizeIntervalPage(DgDesensitizeIntervalPageReqVO pageReqVO) {
        return dgDesensitizeIntervalMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgDesensitizeInterval(DgDesensitizeIntervalSaveReqVO createReqVO) {
        DgDesensitizeIntervalDO dictType = BeanUtils.toBean(createReqVO, DgDesensitizeIntervalDO.class);
        dgDesensitizeIntervalMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDesensitizeInterval(DgDesensitizeIntervalSaveReqVO updateReqVO) {
        // 相关校验

        // 更新脱敏区间
        DgDesensitizeIntervalDO updateObj = BeanUtils.toBean(updateReqVO, DgDesensitizeIntervalDO.class);
        return dgDesensitizeIntervalMapper.updateById(updateObj);
    }
    @Override
    public int removeDgDesensitizeInterval(Collection<Long> idList) {
        // 批量删除脱敏区间
        return dgDesensitizeIntervalMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDesensitizeIntervalDO getDgDesensitizeIntervalById(Long id) {
        return dgDesensitizeIntervalMapper.selectById(id);
    }

    @Override
    public List<DgDesensitizeIntervalDO> getDgDesensitizeIntervalList() {
        return dgDesensitizeIntervalMapper.selectList();
    }

    @Override
    public Map<Long, DgDesensitizeIntervalDO> getDgDesensitizeIntervalMap() {
        List<DgDesensitizeIntervalDO> dgDesensitizeIntervalList = dgDesensitizeIntervalMapper.selectList();
        return dgDesensitizeIntervalList.stream()
                .collect(Collectors.toMap(
                        DgDesensitizeIntervalDO::getId,
                        dgDesensitizeIntervalDO -> dgDesensitizeIntervalDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入脱敏区间数据
         *
         * @param importExcelList 脱敏区间数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDgDesensitizeInterval(List<DgDesensitizeIntervalRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dg.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DgDesensitizeIntervalRespVO respVO : importExcelList) {
                try {
                    DgDesensitizeIntervalDO dgDesensitizeIntervalDO = BeanUtils.toBean(respVO, DgDesensitizeIntervalDO.class);
                    Long dgDesensitizeIntervalId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dgDesensitizeIntervalId != null) {
                            DgDesensitizeIntervalDO existingDgDesensitizeInterval = dgDesensitizeIntervalMapper.selectById(dgDesensitizeIntervalId);
                            if (existingDgDesensitizeInterval != null) {
                                dgDesensitizeIntervalMapper.updateById(dgDesensitizeIntervalDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dg.import.update.success",
                                        "数据更新成功，ID为 " + dgDesensitizeIntervalId + " 的脱敏区间记录。", dgDesensitizeIntervalId, "脱敏区间"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.fail",
                                        "数据更新失败，ID为 " + dgDesensitizeIntervalId + " 的脱敏区间记录不存在。", dgDesensitizeIntervalId, "脱敏区间"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DgDesensitizeIntervalDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dgDesensitizeIntervalId);
                        DgDesensitizeIntervalDO existingDgDesensitizeInterval = dgDesensitizeIntervalMapper.selectOne(queryWrapper);
                        if (existingDgDesensitizeInterval == null) {
                            dgDesensitizeIntervalMapper.insert(dgDesensitizeIntervalDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dg.import.insert.success",
                                    "数据插入成功，ID为 " + dgDesensitizeIntervalId + " 的脱敏区间记录。", dgDesensitizeIntervalId, "脱敏区间"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.insert.fail",
                                    "数据插入失败，ID为 " + dgDesensitizeIntervalId + " 的脱敏区间记录已存在。", dgDesensitizeIntervalId, "脱敏区间"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("dg.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.fail",
                        "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("dg.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.success",
                        "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
            }
            return resultMsg.toString();
        }
}
