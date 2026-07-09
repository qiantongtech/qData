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
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerDO;
import tech.qiantong.qdata.module.dm.dal.mapper.dm.DmDataDomainMapper;
import tech.qiantong.qdata.module.dm.service.dm.IDmDataDomainService;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * Data Domain Service - Business Layer Processing
 *
 * @author FXB
 * @date 2026-03-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmDataDomainServiceImpl  extends ServiceImpl<DmDataDomainMapper,DmDataDomainDO> implements IDmDataDomainService {
    @Resource
    private DmDataDomainMapper dmDataDomainMapper;

    @Override
    public PageResult<DmDataDomainDO> getDmDataDomainPage(DmDataDomainPageReqVO pageReqVO) {
        return dmDataDomainMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDmDataDomain(DmDataDomainSaveReqVO createReqVO) {
        DmDataDomainDO dictType = BeanUtils.toBean(createReqVO, DmDataDomainDO.class);
        dmDataDomainMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDmDataDomain(DmDataDomainSaveReqVO updateReqVO) {
        // Related validation

        // Update data domain
        DmDataDomainDO updateObj = BeanUtils.toBean(updateReqVO, DmDataDomainDO.class);
        return dmDataDomainMapper.updateById(updateObj);
    }
    @Override
    public int removeDmDataDomain(Collection<Long> idList) {
        // Batch delete data domains
        return dmDataDomainMapper.deleteBatchIds(idList);
    }

    @Override
    public DmDataDomainDO getDmDataDomainById(Long id) {
        MPJLambdaWrapperX<DmDataDomainDO> lambdaWrapper = new MPJLambdaWrapperX<>();

        lambdaWrapper.selectAll(DmDataDomainDO.class)
                .select("u.NICK_NAME AS ownerUserName","u.PHONENUMBER AS ownerUserPhoneNumber")
                .leftJoin("SYSTEM_USER u on t.OWNER_USER_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .eq(DmDataDomainDO::getId, id);
        return dmDataDomainMapper.selectOne(lambdaWrapper);
    }

    @Override
    public List<DmDataDomainDO> getDmDataDomainList() {
        return dmDataDomainMapper.selectList();
    }

    @Override
    public Map<Long, DmDataDomainDO> getDmDataDomainMap() {
        List<DmDataDomainDO> dmDataDomainList = dmDataDomainMapper.selectList();
        return dmDataDomainList.stream()
                .collect(Collectors.toMap(
                        DmDataDomainDO::getId,
                        dmDataDomainDO -> dmDataDomainDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data domain data
         *
         * @param importExcelList Data domain data list
         * @param isUpdateSupport Whether to support update, if exists, update the data
         * @param operName Operation user
         * @return Result
         */
        @Override
        public String importDmDataDomain(List<DmDataDomainRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dm.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DmDataDomainRespVO respVO : importExcelList) {
                try {
                    DmDataDomainDO dmDataDomainDO = BeanUtils.toBean(respVO, DmDataDomainDO.class);
                    Long dmDataDomainId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dmDataDomainId != null) {
                            DmDataDomainDO existingDmDataDomain = dmDataDomainMapper.selectById(dmDataDomainId);
                            if (existingDmDataDomain != null) {
                                dmDataDomainMapper.updateById(dmDataDomainDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dm.import.update.success",
                                        "Data update successful, data domain record with ID " + dmDataDomainId + ".", dmDataDomainId, "DataDomain"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.fail",
                                        "Data update failed, data domain record with ID " + dmDataDomainId + " does not exist.", dmDataDomainId, "DataDomain"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.id.missing",
                                    "Data update failed, a record has no ID."));
                        }
                    } else {
                        QueryWrapper<DmDataDomainDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dmDataDomainId);
                        DmDataDomainDO existingDmDataDomain = dmDataDomainMapper.selectOne(queryWrapper);
                        if (existingDmDataDomain == null) {
                            dmDataDomainMapper.insert(dmDataDomainDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dm.import.insert.success",
                                    "Data insert successful, data domain record with ID " + dmDataDomainId + ".", dmDataDomainId, "DataDomain"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dm.import.insert.fail",
                                    "Data insert failed, data domain record with ID " + dmDataDomainId + " already exists.", dmDataDomainId, "DataDomain"));
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
    public PageResult<DmDataDomainDO> getDmDataDomainByCategoryId(DmDataDomainPageReqVO dmDataDomain) {
        return dmDataDomainMapper.selectlistBybusinessDomainId(dmDataDomain);
    }
}
