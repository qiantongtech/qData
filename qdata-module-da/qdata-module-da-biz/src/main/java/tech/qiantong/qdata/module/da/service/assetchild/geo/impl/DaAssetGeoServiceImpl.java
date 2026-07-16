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

package tech.qiantong.qdata.module.da.service.assetchild.geo.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.geo.DaAssetGeoDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.geo.DaAssetGeoMapper;
import tech.qiantong.qdata.module.da.service.assetchild.geo.IDaAssetGeoService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Asset - Vector Service business layer processing
 *
 * @author qdata
 * @date 2025-04-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetGeoServiceImpl  extends ServiceImpl<DaAssetGeoMapper,DaAssetGeoDO> implements IDaAssetGeoService {
    @Resource
    private DaAssetGeoMapper daAssetGeoMapper;

    @Override
    public PageResult<DaAssetGeoDO> getDaAssetGeoPage(DaAssetGeoPageReqVO pageReqVO) {
        return daAssetGeoMapper.selectPage(pageReqVO);
    }

    @Override
    public DaAssetGeoRespVO getDaAssetGeoByAssetId(Long assetId) {
        LambdaQueryWrapperX<DaAssetGeoDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eqIfPresent(DaAssetGeoDO::getAssetId,assetId);
        DaAssetGeoDO daAssetApiDO = daAssetGeoMapper.selectOne(queryWrapperX);
        return BeanUtils.toBean(daAssetApiDO, DaAssetGeoRespVO.class);
    }

    @Override
    public Long createDaAssetGeo(DaAssetGeoSaveReqVO createReqVO) {
        DaAssetGeoDO dictType = BeanUtils.toBean(createReqVO, DaAssetGeoDO.class);
        daAssetGeoMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetGeo(DaAssetGeoSaveReqVO updateReqVO) {
        // Related validation

        // Update data asset - vector
        DaAssetGeoDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetGeoDO.class);
        return daAssetGeoMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetGeo(Collection<Long> idList) {
        // Batch delete data asset - vector
        return daAssetGeoMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetGeoDO getDaAssetGeoById(Long id) {
        return daAssetGeoMapper.selectById(id);
    }

    @Override
    public List<DaAssetGeoDO> getDaAssetGeoList() {
        return daAssetGeoMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetGeoDO> getDaAssetGeoMap() {
        List<DaAssetGeoDO> daAssetGeoList = daAssetGeoMapper.selectList();
        return daAssetGeoList.stream()
                .collect(Collectors.toMap(
                        DaAssetGeoDO::getId,
                        daAssetGeoDO -> daAssetGeoDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data asset - vector data
         *
         * @param importExcelList Data asset - vector data list
         * @param isUpdateSupport Whether to support update, if already exists, update the data
         * @param operName Operator user
         * @return Result
         */
        @Override
        public String importDaAssetGeo(List<DaAssetGeoRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("da.error.import.empty", "Import data cannot be empty!", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DaAssetGeoRespVO respVO : importExcelList) {
                try {
                    DaAssetGeoDO daAssetGeoDO = BeanUtils.toBean(respVO, DaAssetGeoDO.class);
                    Long daAssetGeoId = respVO.getId();
                    if (isUpdateSupport) {
                        if (daAssetGeoId != null) {
                            DaAssetGeoDO existingDaAssetGeo = daAssetGeoMapper.selectById(daAssetGeoId);
                            if (existingDaAssetGeo != null) {
                                daAssetGeoMapper.updateById(daAssetGeoDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                        "Data updated successfully, data asset - vector record with ID " + daAssetGeoId + ".", daAssetGeoId, "Data Asset - Vector"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                        "Data update failed, data asset - vector record with ID " + daAssetGeoId + " does not exist.", daAssetGeoId, "Data Asset - Vector"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                    "Data update failed, ID of a record is missing."));
                        }
                    } else {
                        QueryWrapper<DaAssetGeoDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daAssetGeoId);
                        DaAssetGeoDO existingDaAssetGeo = daAssetGeoMapper.selectOne(queryWrapper);
                        if (existingDaAssetGeo == null) {
                            daAssetGeoMapper.insert(daAssetGeoDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                    "Data inserted successfully, data asset - vector record with ID " + daAssetGeoId + ".", daAssetGeoId, "Data Asset - Vector"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                    "Data insert failed, data asset - vector record with ID " + daAssetGeoId + " already exists.", daAssetGeoId, "Data Asset - Vector"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "Data import failed, error message: " + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                        "Sorry, import failed! A total of " + failureNum + " records had incorrect format, errors are as follows: <br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                        "Congratulations! All data has been imported successfully! A total of " + successNum + " records.", successNum));
            }
            return resultMsg.toString();
        }
}
