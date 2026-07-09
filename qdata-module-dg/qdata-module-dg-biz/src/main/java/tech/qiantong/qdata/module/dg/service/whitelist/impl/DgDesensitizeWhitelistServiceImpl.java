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

import java.util.*;
import java.util.stream.Collectors;

import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.whitelist.vo.DgDesensitizeWhitelistSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeRuleDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeWhitelistDO;
import tech.qiantong.qdata.module.dg.dal.mapper.dataCategory.DgDataCategoryMapper;
import tech.qiantong.qdata.module.dg.dal.mapper.whitelist.DgDesensitizeUserRelMapper;
import tech.qiantong.qdata.module.dg.dal.mapper.whitelist.DgDesensitizeWhitelistMapper;
import tech.qiantong.qdata.module.dg.service.whitelist.IDgDesensitizeWhitelistService;
/**
 * Desensitize Whitelist Service Business Layer Processing
 *
 * @author qdata
 * @date 2026-04-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDesensitizeWhitelistServiceImpl  extends ServiceImpl<DgDesensitizeWhitelistMapper,DgDesensitizeWhitelistDO> implements IDgDesensitizeWhitelistService {
    @Resource
    private DgDesensitizeWhitelistMapper dgDesensitizeWhitelistMapper;

    @Resource
    private DgDesensitizeUserRelMapper dgDesensitizeUserRelMapper;

    @Resource
    private DgDataCategoryMapper dgDataCategoryMapper;

    @Override
    public PageResult<DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistPage(DgDesensitizeWhitelistPageReqVO pageReqVO) {
        PageResult<DgDesensitizeWhitelistDO> pageResult = dgDesensitizeWhitelistMapper.selectPage(pageReqVO);
        // Query user collection by desensitize whitelist ID and store in DgDesensitizeWhitelistDO
        pageResult.getRows().forEach(item -> {
            item.setUserList(dgDesensitizeUserRelMapper.selectList(new LambdaQueryWrapper<DgDesensitizeUserRelDO>().eq(DgDesensitizeUserRelDO::getDesensitizeId, item.getId())));
        });
        return pageResult;
    }

    @Override
    public Long createDgDesensitizeWhitelist(DgDesensitizeWhitelistSaveReqVO createReqVO) {
        DgDesensitizeWhitelistDO dictType = BeanUtils.toBean(createReqVO, DgDesensitizeWhitelistDO.class);
        // Check if category already exists in whitelist
        if (dgDesensitizeWhitelistMapper.selectCount(new LambdaQueryWrapper<DgDesensitizeWhitelistDO>()
                .eq(DgDesensitizeWhitelistDO::getDataCategoryId, dictType.getDataCategoryId())) > 0) {
            throw new ServiceException("dg.error.duplicate.category", "数据分类已存在");
        }
        dgDesensitizeWhitelistMapper.insert(dictType);
        // Insert user collection
        if (dictType.getUserList() != null && !dictType.getUserList().isEmpty()) {
            dictType.getUserList().forEach(user -> {
                user.setDesensitizeId(dictType.getId());
                user.setDesensitizeName(dictType.getName());
            });
            dgDesensitizeUserRelMapper.insertBatch(dictType.getUserList());
        }
        return dictType.getId();
    }
    @Override
    public int updateDgDesensitizeWhitelist(DgDesensitizeWhitelistSaveReqVO updateReqVO) {
        // Related validation
        // Update desensitize whitelist
        DgDesensitizeWhitelistDO updateObj = BeanUtils.toBean(updateReqVO, DgDesensitizeWhitelistDO.class);
        if(updateObj.getUserList() != null && !updateObj.getUserList().isEmpty()){
            // First delete the old user collection
            dgDesensitizeUserRelMapper.delete(new LambdaQueryWrapper<DgDesensitizeUserRelDO>().eq(DgDesensitizeUserRelDO::getDesensitizeId, updateObj.getId()));
            // Update user collection
            if (updateObj.getUserList() != null && !updateObj.getUserList().isEmpty()) {
                updateObj.getUserList().forEach(user -> {
                    user.setDesensitizeId(updateObj.getId());
                    user.setDesensitizeName(updateObj.getName());
                });
                dgDesensitizeUserRelMapper.insertBatch(updateObj.getUserList());
            }
        }
        return dgDesensitizeWhitelistMapper.updateById(updateObj);
    }
    @Override
    public int removeDgDesensitizeWhitelist(Collection<Long> idList) {
        // First delete the old user collection
        dgDesensitizeUserRelMapper.delete(new LambdaQueryWrapper<DgDesensitizeUserRelDO>().in(DgDesensitizeUserRelDO::getDesensitizeId, idList));
        // Batch delete desensitize whitelist
        return dgDesensitizeWhitelistMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDesensitizeWhitelistDO getDgDesensitizeWhitelistById(Long id) {
        // Query user collection by desensitize whitelist ID and store in DgDesensitizeWhitelistDO
        DgDesensitizeWhitelistDO dgDesensitizeWhitelistDO = dgDesensitizeWhitelistMapper.selectById(id);

        // Convert category ID in dgDesensitizeWhitelistDO to category name
        if (dgDesensitizeWhitelistDO != null && dgDesensitizeWhitelistDO.getDataCategoryId()!=null) {
            dgDesensitizeWhitelistDO.setDataCategoryName(dgDataCategoryMapper.selectById(dgDesensitizeWhitelistDO.getDataCategoryId()).getName());
        }

        if (dgDesensitizeWhitelistDO != null) {
            // Query user collection by desensitize whitelist ID and store in DgDesensitizeWhitelistDO
            dgDesensitizeWhitelistDO.setUserList(dgDesensitizeUserRelMapper.selectList(new LambdaQueryWrapper<DgDesensitizeUserRelDO>().eq(DgDesensitizeUserRelDO::getDesensitizeId, id)));
        }
        return dgDesensitizeWhitelistDO;
    }

    @Override
    public DgDesensitizeWhitelistDO getDgDesensitizeWhitelistByCategoryId(Long categoryId) {
        // Query user collection by desensitize whitelist ID and store in DgDesensitizeWhitelistDO
        DgDesensitizeWhitelistDO dgDesensitizeWhitelistDO = dgDesensitizeWhitelistMapper.selectOne(new LambdaQueryWrapper<DgDesensitizeWhitelistDO>().eq(DgDesensitizeWhitelistDO::getDataCategoryId, categoryId));

        if (dgDesensitizeWhitelistDO != null) {
            // Query user collection by desensitize whitelist ID and store in DgDesensitizeWhitelistDO
            dgDesensitizeWhitelistDO.setUserList(dgDesensitizeUserRelMapper.selectList(new LambdaQueryWrapper<DgDesensitizeUserRelDO>().eq(DgDesensitizeUserRelDO::getDesensitizeId, dgDesensitizeWhitelistDO.getId())));
        }
        return dgDesensitizeWhitelistDO;
    }


    @Override
    public List<DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistList() {
        return dgDesensitizeWhitelistMapper.selectList();
    }

    @Override
    public Map<Long, DgDesensitizeWhitelistDO> getDgDesensitizeWhitelistMap() {
        List<DgDesensitizeWhitelistDO> dgDesensitizeWhitelistList = dgDesensitizeWhitelistMapper.selectList();
        return dgDesensitizeWhitelistList.stream()
                .collect(Collectors.toMap(
                        DgDesensitizeWhitelistDO::getId,
                        dgDesensitizeWhitelistDO -> dgDesensitizeWhitelistDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import desensitize whitelist data
         *
         * @param importExcelList Desensitize whitelist data list
         * @param isUpdateSupport Whether to update support, if already exists, update the data
         * @param operName        Operator user
         * @return Result
         */
        @Override
        public String importDgDesensitizeWhitelist(List<DgDesensitizeWhitelistRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dg.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DgDesensitizeWhitelistRespVO respVO : importExcelList) {
                try {
                    DgDesensitizeWhitelistDO dgDesensitizeWhitelistDO = BeanUtils.toBean(respVO, DgDesensitizeWhitelistDO.class);
                    Long dgDesensitizeWhitelistId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dgDesensitizeWhitelistId != null) {
                            DgDesensitizeWhitelistDO existingDgDesensitizeWhitelist = dgDesensitizeWhitelistMapper.selectById(dgDesensitizeWhitelistId);
                            if (existingDgDesensitizeWhitelist != null) {
                                dgDesensitizeWhitelistMapper.updateById(dgDesensitizeWhitelistDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dg.import.update.success",
                                        "数据更新成功，ID为 " + dgDesensitizeWhitelistId + " 的脱敏白名单记录。", dgDesensitizeWhitelistId, "脱敏白名单"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.fail",
                                        "数据更新失败，ID为 " + dgDesensitizeWhitelistId + " 的脱敏白名单记录不存在。", dgDesensitizeWhitelistId, "脱敏白名单"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DgDesensitizeWhitelistDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dgDesensitizeWhitelistId);
                        DgDesensitizeWhitelistDO existingDgDesensitizeWhitelist = dgDesensitizeWhitelistMapper.selectOne(queryWrapper);
                        if (existingDgDesensitizeWhitelist == null) {
                            dgDesensitizeWhitelistMapper.insert(dgDesensitizeWhitelistDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dg.import.insert.success",
                                    "数据插入成功，ID为 " + dgDesensitizeWhitelistId + " 的脱敏白名单记录。", dgDesensitizeWhitelistId, "脱敏白名单"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.insert.fail",
                                    "数据插入失败，ID为 " + dgDesensitizeWhitelistId + " 的脱敏白名单记录已存在。", dgDesensitizeWhitelistId, "脱敏白名单"));
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
