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

package tech.qiantong.qdata.module.dm.service.businessCategory.impl;

import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessDomainRelSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessDomainRelDO;
import tech.qiantong.qdata.module.dm.dal.mapper.businessCategory.DmBusinessDomainRelMapper;
import tech.qiantong.qdata.module.dm.service.businessCategory.IDmBusinessDomainRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Business Category Data Domain Relation Service - Business Layer Processing
 *
 * @author qdata
 * @date 2026-04-12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmBusinessDomainRelServiceImpl extends ServiceImpl<DmBusinessDomainRelMapper,DmBusinessDomainRelDO> implements IDmBusinessDomainRelService {
    @Resource
    private DmBusinessDomainRelMapper dmBusinessDomainRelMapper;

    @Override
    public PageResult<DmBusinessDomainRelDO> getDmBusinessDomainRelPage(DmBusinessDomainRelPageReqVO pageReqVO) {
        return dmBusinessDomainRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDmBusinessDomainRel(DmBusinessDomainRelSaveReqVO createReqVO) {
        DmBusinessDomainRelDO dictType = BeanUtils.toBean(createReqVO, DmBusinessDomainRelDO.class);
        dmBusinessDomainRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDmBusinessDomainRel(DmBusinessDomainRelSaveReqVO updateReqVO) {
        // Related validation

        // Update business category data domain relation
        DmBusinessDomainRelDO updateObj = BeanUtils.toBean(updateReqVO, DmBusinessDomainRelDO.class);
        return dmBusinessDomainRelMapper.updateById(updateObj);
    }
    @Override
    public int removeDmBusinessDomainRel(Collection<Long> idList) {
        // Batch delete business category data domain relations
        return dmBusinessDomainRelMapper.deleteBatchIds(idList);
    }

    @Override
    public DmBusinessDomainRelDO getDmBusinessDomainRelById(Long id) {
        return dmBusinessDomainRelMapper.selectById(id);
    }

    @Override
    public List<DmBusinessDomainRelDO> getDmBusinessDomainRelList() {
        return dmBusinessDomainRelMapper.selectList();
    }

    @Override
    public Map<Long, DmBusinessDomainRelDO> getDmBusinessDomainRelMap() {
        List<DmBusinessDomainRelDO> dmBusinessDomainRelList = dmBusinessDomainRelMapper.selectList();
        return dmBusinessDomainRelList.stream()
                .collect(Collectors.toMap(
                        DmBusinessDomainRelDO::getId,
                        dmBusinessDomainRelDO -> dmBusinessDomainRelDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import business category data domain relation data
         *
         * @param importExcelList Business category data domain relation data list
         * @param isUpdateSupport Whether to support update, if exists, update the data
         * @param operName Operation user
         * @return Result
         */
        @Override
        public String importDmBusinessDomainRel(List<DmBusinessDomainRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dm.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DmBusinessDomainRelRespVO respVO : importExcelList) {
                try {
                    DmBusinessDomainRelDO dmBusinessDomainRelDO = BeanUtils.toBean(respVO, DmBusinessDomainRelDO.class);
                    Long dmBusinessDomainRelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dmBusinessDomainRelId != null) {
                            DmBusinessDomainRelDO existingDmBusinessDomainRel = dmBusinessDomainRelMapper.selectById(dmBusinessDomainRelId);
                            if (existingDmBusinessDomainRel != null) {
                                dmBusinessDomainRelMapper.updateById(dmBusinessDomainRelDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dm.import.update.success",
                                        "Data update successful, business category data domain relation record with ID " + dmBusinessDomainRelId + ".", dmBusinessDomainRelId, "BusinessCategoryDomainRel"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.fail",
                                        "Data update failed, business category data domain relation record with ID " + dmBusinessDomainRelId + " does not exist.", dmBusinessDomainRelId, "BusinessCategoryDomainRel"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.id.missing",
                                    "Data update failed, a record has no ID."));
                        }
                    } else {
                        QueryWrapper<DmBusinessDomainRelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dmBusinessDomainRelId);
                        DmBusinessDomainRelDO existingDmBusinessDomainRel = dmBusinessDomainRelMapper.selectOne(queryWrapper);
                        if (existingDmBusinessDomainRel == null) {
                            dmBusinessDomainRelMapper.insert(dmBusinessDomainRelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dm.import.insert.success",
                                    "Data insert successful, business category data domain relation record with ID " + dmBusinessDomainRelId + ".", dmBusinessDomainRelId, "BusinessCategoryDomainRel"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dm.import.insert.fail",
                                    "Data insert failed, business category data domain relation record with ID " + dmBusinessDomainRelId + " already exists.", dmBusinessDomainRelId, "BusinessCategoryDomainRel"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("dm.import.error.detail",
                            "Data import failed, error: " + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("dm.import.result.fail",
                        "Import failed! " + failureNum + " records have incorrect format, errors below:<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("dm.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("dm.import.result.success",
                        "All data imported successfully! Total " + successNum + " records.", successNum));
            }
            return resultMsg.toString();
        }

    @Override
    public Integer removeDmBusinessDomainRelByDomainId(Long domainId, Long businessCategoryId) {
         if (domainId == null || businessCategoryId == null || businessCategoryId == 0) {
              throw new ServiceException("dm.error.id.empty", "Data domain ID or business category ID cannot be empty!");
         }
        // Delete relation by data domain ID and business category ID
        return dmBusinessDomainRelMapper.delete(new LambdaQueryWrapper<DmBusinessDomainRelDO>()
               .eq(DmBusinessDomainRelDO::getDataDomainId, domainId)
               .eq(DmBusinessDomainRelDO::getBusinessCategoryId, businessCategoryId));
    }
}
