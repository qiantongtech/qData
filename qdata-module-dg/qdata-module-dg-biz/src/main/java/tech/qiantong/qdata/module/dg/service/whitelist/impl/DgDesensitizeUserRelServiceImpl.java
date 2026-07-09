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

package tech.qiantong.qdata.module.dg.service.whitelist.impl;

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
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeUserRelSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;
import tech.qiantong.qdata.module.dg.dal.mapper.whitelist.DgDesensitizeUserRelMapper;
import tech.qiantong.qdata.module.dg.service.whitelist.IDgDesensitizeUserRelService;
/**
 * Desensitize Whitelist User Relationship Service Business Layer Processing
 *
 * @author qdata
 * @date 2026-04-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDesensitizeUserRelServiceImpl  extends ServiceImpl<DgDesensitizeUserRelMapper,DgDesensitizeUserRelDO> implements IDgDesensitizeUserRelService {
    @Resource
    private DgDesensitizeUserRelMapper dgDesensitizeUserRelMapper;

    @Override
    public PageResult<DgDesensitizeUserRelDO> getDgDesensitizeUserRelPage(DgDesensitizeUserRelPageReqVO pageReqVO) {
        return dgDesensitizeUserRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgDesensitizeUserRel(DgDesensitizeUserRelSaveReqVO createReqVO) {
        DgDesensitizeUserRelDO dictType = BeanUtils.toBean(createReqVO, DgDesensitizeUserRelDO.class);
        dgDesensitizeUserRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDesensitizeUserRel(DgDesensitizeUserRelSaveReqVO updateReqVO) {
        // Related validation

        // Update desensitize whitelist user relationship
        DgDesensitizeUserRelDO updateObj = BeanUtils.toBean(updateReqVO, DgDesensitizeUserRelDO.class);
        return dgDesensitizeUserRelMapper.updateById(updateObj);
    }
    @Override
    public int removeDgDesensitizeUserRel(Collection<Long> idList) {
        // Batch delete desensitize whitelist user relationships
        return dgDesensitizeUserRelMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDesensitizeUserRelDO getDgDesensitizeUserRelById(Long id) {
        return dgDesensitizeUserRelMapper.selectById(id);
    }

    @Override
    public List<DgDesensitizeUserRelDO> getDgDesensitizeUserRelList() {
        return dgDesensitizeUserRelMapper.selectList();
    }

    @Override
    public Map<Long, DgDesensitizeUserRelDO> getDgDesensitizeUserRelMap() {
        List<DgDesensitizeUserRelDO> dgDesensitizeUserRelList = dgDesensitizeUserRelMapper.selectList();
        return dgDesensitizeUserRelList.stream()
                .collect(Collectors.toMap(
                        DgDesensitizeUserRelDO::getId,
                        dgDesensitizeUserRelDO -> dgDesensitizeUserRelDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import desensitize whitelist user relationship data
         *
         * @param importExcelList Desensitize whitelist user relationship data list
         * @param isUpdateSupport Whether to update support, if already exists, update the data
         * @param operName        Operator user
         * @return Result
         */
        @Override
        public String importDgDesensitizeUserRel(List<DgDesensitizeUserRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dg.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DgDesensitizeUserRelRespVO respVO : importExcelList) {
                try {
                    DgDesensitizeUserRelDO dgDesensitizeUserRelDO = BeanUtils.toBean(respVO, DgDesensitizeUserRelDO.class);
                    Long dgDesensitizeUserRelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dgDesensitizeUserRelId != null) {
                            DgDesensitizeUserRelDO existingDgDesensitizeUserRel = dgDesensitizeUserRelMapper.selectById(dgDesensitizeUserRelId);
                            if (existingDgDesensitizeUserRel != null) {
                                dgDesensitizeUserRelMapper.updateById(dgDesensitizeUserRelDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dg.import.update.success",
                                        "数据更新成功，ID为 " + dgDesensitizeUserRelId + " 的脱敏白名单与用户关联关系记录。", dgDesensitizeUserRelId, "脱敏白名单与用户关联关系"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.fail",
                                        "数据更新失败，ID为 " + dgDesensitizeUserRelId + " 的脱敏白名单与用户关联关系记录不存在。", dgDesensitizeUserRelId, "脱敏白名单与用户关联关系"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DgDesensitizeUserRelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dgDesensitizeUserRelId);
                        DgDesensitizeUserRelDO existingDgDesensitizeUserRel = dgDesensitizeUserRelMapper.selectOne(queryWrapper);
                        if (existingDgDesensitizeUserRel == null) {
                            dgDesensitizeUserRelMapper.insert(dgDesensitizeUserRelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dg.import.insert.success",
                                    "数据插入成功，ID为 " + dgDesensitizeUserRelId + " 的脱敏白名单与用户关联关系记录。", dgDesensitizeUserRelId, "脱敏白名单与用户关联关系"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.insert.fail",
                                    "数据插入失败，ID为 " + dgDesensitizeUserRelId + " 的脱敏白名单与用户关联关系记录已存在。", dgDesensitizeUserRelId, "脱敏白名单与用户关联关系"));
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
