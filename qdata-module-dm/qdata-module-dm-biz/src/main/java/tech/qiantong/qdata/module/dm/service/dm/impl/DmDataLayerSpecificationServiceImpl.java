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

package tech.qiantong.qdata.module.dm.service.dm.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSpecificationSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerSpecificationDO;
import tech.qiantong.qdata.module.dm.dal.mapper.dm.DmDataLayerSpecificationMapper;
import tech.qiantong.qdata.module.dm.service.dm.IDmDataLayerSpecificationService;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * Data Warehouse Layer Specification Service - Business Layer Processing
 *
 * @author FXB
 * @date 2026-03-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmDataLayerSpecificationServiceImpl extends ServiceImpl<DmDataLayerSpecificationMapper, DmDataLayerSpecificationDO> implements IDmDataLayerSpecificationService {
    @Resource
    private DmDataLayerSpecificationMapper dmDataLayerSpecificationMapper;

    @Override
    public PageResult<DmDataLayerSpecificationDO> getDmDataLayerSpecificationPage(DmDataLayerSpecificationPageReqVO pageReqVO) {
        return dmDataLayerSpecificationMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDmDataLayerSpecification(DmDataLayerSpecificationSaveReqVO createReqVO) {
        DmDataLayerSpecificationDO dictType = BeanUtils.toBean(createReqVO, DmDataLayerSpecificationDO.class);
        dmDataLayerSpecificationMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDmDataLayerSpecification(DmDataLayerSpecificationSaveReqVO updateReqVO) {
        // Related validation

        // Update data warehouse layer specification
        DmDataLayerSpecificationDO updateObj = BeanUtils.toBean(updateReqVO, DmDataLayerSpecificationDO.class);
        return dmDataLayerSpecificationMapper.updateById(updateObj);
    }

    @Override
    public int removeDmDataLayerSpecification(Collection<Long> idList) {
        // Batch delete data warehouse layer specifications
        return dmDataLayerSpecificationMapper.deleteBatchIds(idList);
    }

    @Override
    public DmDataLayerSpecificationDO getDmDataLayerSpecificationById(Long id) {
        MPJLambdaWrapperX<DmDataLayerSpecificationDO> lambdaWrapper = new MPJLambdaWrapperX<>();

        lambdaWrapper.selectAll(DmDataLayerSpecificationDO.class)
                .select("u.NICK_NAME AS ownerUserName","u.PHONENUMBER AS ownerUserPhoneNumber")
                .leftJoin("SYSTEM_USER u on t.OWNER_USER_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .eq(DmDataLayerSpecificationDO::getId, id);
        return dmDataLayerSpecificationMapper.selectOne(lambdaWrapper);
    }

    @Override
    public List<DmDataLayerSpecificationDO> getDmDataLayerSpecificationPage() {
        return dmDataLayerSpecificationMapper.selectList();
    }

    @Override
    public Map<Long, DmDataLayerSpecificationDO> getDmDataLayerSpecificationMap() {
        List<DmDataLayerSpecificationDO> dmDataLayerSpecificationList = dmDataLayerSpecificationMapper.selectList();
        return dmDataLayerSpecificationList.stream()
                .collect(Collectors.toMap(
                        DmDataLayerSpecificationDO::getId,
                        dmDataLayerSpecificationDO -> dmDataLayerSpecificationDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data warehouse layer specification data
     *
     * @param importExcelList Data warehouse layer specification data list
     * @param isUpdateSupport Whether to support update, if exists, update the data
     * @param operName        Operation user
     * @return Result
     */
    @Override
    public String importDmDataLayerSpecification(List<DmDataLayerSpecificationRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dm.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DmDataLayerSpecificationRespVO respVO : importExcelList) {
            try {
                DmDataLayerSpecificationDO dmDataLayerSpecificationDO = BeanUtils.toBean(respVO, DmDataLayerSpecificationDO.class);
                Long dmDataLayerSpecificationId = respVO.getId();
                if (isUpdateSupport) {
                    if (dmDataLayerSpecificationId != null) {
                        DmDataLayerSpecificationDO existingDmDataLayerSpecification = dmDataLayerSpecificationMapper.selectById(dmDataLayerSpecificationId);
                        if (existingDmDataLayerSpecification != null) {
                            dmDataLayerSpecificationMapper.updateById(dmDataLayerSpecificationDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dm.import.update.success",
                                    "Data update successful, data warehouse layer specification record with ID " + dmDataLayerSpecificationId + ".", dmDataLayerSpecificationId, "DataWarehouseLayerSpecification"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.fail",
                                    "Data update failed, data warehouse layer specification record with ID " + dmDataLayerSpecificationId + " does not exist.", dmDataLayerSpecificationId, "DataWarehouseLayerSpecification"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.id.missing",
                                "Data update failed, a record has no ID."));
                    }
                } else {
                    QueryWrapper<DmDataLayerSpecificationDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dmDataLayerSpecificationId);
                    DmDataLayerSpecificationDO existingDmDataLayerSpecification = dmDataLayerSpecificationMapper.selectOne(queryWrapper);
                    if (existingDmDataLayerSpecification == null) {
                        dmDataLayerSpecificationMapper.insert(dmDataLayerSpecificationDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dm.import.insert.success",
                                "Data insert successful, data warehouse layer specification record with ID " + dmDataLayerSpecificationId + ".", dmDataLayerSpecificationId, "DataWarehouseLayerSpecification"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dm.import.insert.fail",
                                "Data insert failed, data warehouse layer specification record with ID " + dmDataLayerSpecificationId + " already exists.", dmDataLayerSpecificationId, "DataWarehouseLayerSpecification"));
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
}
