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

package tech.qiantong.qdata.module.dg.service.desensitizeList.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.desensitizeList.vo.DgDesensitizeAssetcolumnSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeList.DgDesensitizeAssetcolumnDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeUserRelDO;
import tech.qiantong.qdata.module.dg.dal.mapper.desensitizeList.DgDesensitizeAssetcolumnMapper;
import tech.qiantong.qdata.module.dg.service.desensitizeList.IDgDesensitizeAssetcolumnService;
/**
 * Desensitize List Relationship Service Business Layer Processing
 *
 * @author qdata
 * @date 2026-04-12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDesensitizeAssetcolumnServiceImpl  extends ServiceImpl<DgDesensitizeAssetcolumnMapper,DgDesensitizeAssetcolumnDO> implements IDgDesensitizeAssetcolumnService {
    @Resource
    private DgDesensitizeAssetcolumnMapper dgDesensitizeAssetcolumnMapper;

    @Override
    public PageResult<DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnPage(DgDesensitizeAssetcolumnPageReqVO pageReqVO) {
        return dgDesensitizeAssetcolumnMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgDesensitizeAssetcolumn(DgDesensitizeAssetcolumnSaveReqVO createReqVO) {
        DgDesensitizeAssetcolumnDO dictType = BeanUtils.toBean(createReqVO, DgDesensitizeAssetcolumnDO.class);
        dgDesensitizeAssetcolumnMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDesensitizeAssetcolumn(DgDesensitizeAssetcolumnSaveReqVO updateReqVO) {
        // Related validation

        // Update desensitize list relationship
        DgDesensitizeAssetcolumnDO updateObj = BeanUtils.toBean(updateReqVO, DgDesensitizeAssetcolumnDO.class);
        return dgDesensitizeAssetcolumnMapper.updateById(updateObj);
    }
    @Override
    public int removeDgDesensitizeAssetcolumn(Collection<Long> idList) {
        // Batch delete desensitize list relationships
        return dgDesensitizeAssetcolumnMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDesensitizeAssetcolumnDO getDgDesensitizeAssetcolumnById(Long id) {
        return dgDesensitizeAssetcolumnMapper.selectDesensitizeAssetcolumnById(id);
    }

    @Override
    public DgDesensitizeAssetcolumnDO getDgDesensitizeAssetcolumnByAid(Long assetcolumnId) {
        // Query by assetcolumnId
        return dgDesensitizeAssetcolumnMapper.selectOne( new LambdaQueryWrapper<DgDesensitizeAssetcolumnDO>().eq(DgDesensitizeAssetcolumnDO::getAssetcolumnId, assetcolumnId));
    }

    @Override
    public List<DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnList() {
        return dgDesensitizeAssetcolumnMapper.selectList();
    }

    @Override
    public Map<Long, DgDesensitizeAssetcolumnDO> getDgDesensitizeAssetcolumnMap() {
        List<DgDesensitizeAssetcolumnDO> dgDesensitizeAssetcolumnList = dgDesensitizeAssetcolumnMapper.selectList();
        return dgDesensitizeAssetcolumnList.stream()
                .collect(Collectors.toMap(
                        DgDesensitizeAssetcolumnDO::getId,
                        dgDesensitizeAssetcolumnDO -> dgDesensitizeAssetcolumnDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import desensitize list relationship data
         *
         * @param importExcelList Desensitize list relationship data list
         * @param isUpdateSupport Whether to update support, if already exists, update the data
         * @param operName        Operator user
         * @return Result
         */
        @Override
        public String importDgDesensitizeAssetcolumn(List<DgDesensitizeAssetcolumnRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dg.error.import.empty", "Imported data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DgDesensitizeAssetcolumnRespVO respVO : importExcelList) {
                try {
                    DgDesensitizeAssetcolumnDO dgDesensitizeAssetcolumnDO = BeanUtils.toBean(respVO, DgDesensitizeAssetcolumnDO.class);
                    Long dgDesensitizeAssetcolumnId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dgDesensitizeAssetcolumnId != null) {
                            DgDesensitizeAssetcolumnDO existingDgDesensitizeAssetcolumn = dgDesensitizeAssetcolumnMapper.selectById(dgDesensitizeAssetcolumnId);
                            if (existingDgDesensitizeAssetcolumn != null) {
                                dgDesensitizeAssetcolumnMapper.updateById(dgDesensitizeAssetcolumnDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dg.import.update.success",
                                        "Data updated successfully, desensitization list relationship record with ID " + dgDesensitizeAssetcolumnId + ".", dgDesensitizeAssetcolumnId, "Desensitization List Relationship"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.fail",
                                        "Data update failed, desensitization list relationship record with ID " + dgDesensitizeAssetcolumnId + " does not exist.", dgDesensitizeAssetcolumnId, "Desensitization List Relationship"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.id.missing",
                                    "Data update failed, a record's ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DgDesensitizeAssetcolumnDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dgDesensitizeAssetcolumnId);
                        DgDesensitizeAssetcolumnDO existingDgDesensitizeAssetcolumn = dgDesensitizeAssetcolumnMapper.selectOne(queryWrapper);
                        if (existingDgDesensitizeAssetcolumn == null) {
                            dgDesensitizeAssetcolumnMapper.insert(dgDesensitizeAssetcolumnDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dg.import.insert.success",
                                    "Data inserted successfully, desensitization list relationship record with ID " + dgDesensitizeAssetcolumnId + ".", dgDesensitizeAssetcolumnId, "Desensitization List Relationship"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.insert.fail",
                                    "Data insertion failed, desensitization list relationship record with ID " + dgDesensitizeAssetcolumnId + " already exists.", dgDesensitizeAssetcolumnId, "Desensitization List Relationship"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("dg.import.error.detail",
                "Data import failed, error message: " + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.fail",
                        "Sorry, import failed! A total of " + failureNum + " records have incorrect format, errors as follows:<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("dg.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.success",
                        "Congratulations, all data imported successfully! Total: " + successNum + " records.", successNum));
            }
            return resultMsg.toString();
        }

    @Override
    public PageResult<DgDesensitizeAssetcolumnDO> getDgDesensitizePagebyRuleId(DgDesensitizeAssetcolumnPageReqVO dgDesensitizeAssetcolumn) {
            return dgDesensitizeAssetcolumnMapper.selectPagebyRuleId(dgDesensitizeAssetcolumn);
    }

    @Override
    public DgDesensitizeAssetcolumnDO getByassetcolumnId(Long assetcolumnId) {
            return dgDesensitizeAssetcolumnMapper.selectOne( new LambdaQueryWrapper<DgDesensitizeAssetcolumnDO>().eq(DgDesensitizeAssetcolumnDO::getAssetcolumnId, assetcolumnId));
    }

    @Override
    public int deleteByassetcolumnId(Long assetcolumnId) {
            return dgDesensitizeAssetcolumnMapper.delete(new LambdaQueryWrapper<DgDesensitizeAssetcolumnDO>().eq(DgDesensitizeAssetcolumnDO::getAssetcolumnId, assetcolumnId));
    }

}
