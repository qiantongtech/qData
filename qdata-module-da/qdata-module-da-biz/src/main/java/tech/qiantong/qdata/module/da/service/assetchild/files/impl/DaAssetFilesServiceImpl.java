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

package tech.qiantong.qdata.module.da.service.assetchild.files.impl;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.files.DaAssetFilesDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.files.DaAssetFilesMapper;
import tech.qiantong.qdata.module.da.service.assetchild.files.IDaAssetFilesService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Asset - Files Service business layer processing
 *
 * @author qdata
 * @date 2025-06-26
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetFilesServiceImpl  extends ServiceImpl<DaAssetFilesMapper,DaAssetFilesDO> implements IDaAssetFilesService {
    @Resource
    private DaAssetFilesMapper daAssetFilesMapper;

    @Override
    public PageResult<DaAssetFilesDO> getDaAssetFilesPage(DaAssetFilesPageReqVO pageReqVO) {
        return daAssetFilesMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaAssetFiles(DaAssetFilesSaveReqVO createReqVO) {
        DaAssetFilesDO dictType = BeanUtils.toBean(createReqVO, DaAssetFilesDO.class);
        daAssetFilesMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetFiles(DaAssetFilesSaveReqVO updateReqVO) {
        // Related validation

        // Update data asset - files service
        DaAssetFilesDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetFilesDO.class);
        return daAssetFilesMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetFiles(Collection<Long> idList) {
        // Batch delete data asset - files service
        return daAssetFilesMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetFilesDO getDaAssetFilesById(Long id) {
        return daAssetFilesMapper.selectById(id);
    }

    @Override
    public List<DaAssetFilesDO> getDaAssetFilesList() {
        return daAssetFilesMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetFilesDO> getDaAssetFilesMap() {
        List<DaAssetFilesDO> daAssetFilesList = daAssetFilesMapper.selectList();
        return daAssetFilesList.stream()
                .collect(Collectors.toMap(
                        DaAssetFilesDO::getId,
                        daAssetFilesDO -> daAssetFilesDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data asset - files service data
         *
         * @param importExcelList Data asset - files service data list
         * @param isUpdateSupport Whether to support update, if already exists, update the data
         * @param operName Operator user
         * @return Result
         */
        @Override
        public String importDaAssetFiles(List<DaAssetFilesRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("da.error.import.empty", "Import data cannot be empty!", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DaAssetFilesRespVO respVO : importExcelList) {
                try {
                    DaAssetFilesDO daAssetFilesDO = BeanUtils.toBean(respVO, DaAssetFilesDO.class);
                    Long daAssetFilesId = respVO.getId();
                    if (isUpdateSupport) {
                        if (daAssetFilesId != null) {
                            DaAssetFilesDO existingDaAssetFiles = daAssetFilesMapper.selectById(daAssetFilesId);
                            if (existingDaAssetFiles != null) {
                                daAssetFilesMapper.updateById(daAssetFilesDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                        "Data updated successfully, data asset - files service record with ID " + daAssetFilesId + ".", daAssetFilesId, "Data Asset - Files Service"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                        "Data update failed, data asset - files service record with ID " + daAssetFilesId + " does not exist.", daAssetFilesId, "Data Asset - Files Service"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                    "Data update failed, ID of a record is missing."));
                        }
                    } else {
                        QueryWrapper<DaAssetFilesDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daAssetFilesId);
                        DaAssetFilesDO existingDaAssetFiles = daAssetFilesMapper.selectOne(queryWrapper);
                        if (existingDaAssetFiles == null) {
                            daAssetFilesMapper.insert(daAssetFilesDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                    "Data inserted successfully, data asset - files service record with ID " + daAssetFilesId + ".", daAssetFilesId, "Data Asset - Files Service"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                    "Data insert failed, data asset - files service record with ID " + daAssetFilesId + " already exists.", daAssetFilesId, "Data Asset - Files Service"));
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
