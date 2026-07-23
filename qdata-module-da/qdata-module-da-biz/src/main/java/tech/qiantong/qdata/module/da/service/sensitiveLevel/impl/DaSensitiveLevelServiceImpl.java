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

package tech.qiantong.qdata.module.da.service.sensitiveLevel.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.sensitiveLevel.vo.DaSensitiveLevelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.module.da.dal.dataobject.sensitiveLevel.DaSensitiveLevelDO;
import tech.qiantong.qdata.module.da.dal.mapper.sensitiveLevel.DaSensitiveLevelMapper;
import tech.qiantong.qdata.module.da.service.assetColumn.IDaAssetColumnService;
import tech.qiantong.qdata.module.da.service.sensitiveLevel.IDaSensitiveLevelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Sensitive Level Service business layer implementation
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaSensitiveLevelServiceImpl extends ServiceImpl<DaSensitiveLevelMapper, DaSensitiveLevelDO> implements IDaSensitiveLevelService {
    @Resource
    private DaSensitiveLevelMapper daSensitiveLevelMapper;
    @Resource
    private IDaAssetColumnService daAssetColumnService;

    @Override
    public PageResult<DaSensitiveLevelDO> getDaSensitiveLevelPage(DaSensitiveLevelPageReqVO pageReqVO) {
        return daSensitiveLevelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaSensitiveLevel(DaSensitiveLevelSaveReqVO createReqVO) {
        DaSensitiveLevelDO dictType = BeanUtils.toBean(createReqVO, DaSensitiveLevelDO.class);
        daSensitiveLevelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaSensitiveLevel(DaSensitiveLevelSaveReqVO updateReqVO) {
        // Related validation

        // Update sensitive level
        DaSensitiveLevelDO updateObj = BeanUtils.toBean(updateReqVO, DaSensitiveLevelDO.class);
        return daSensitiveLevelMapper.updateById(updateObj);
    }

    @Override
    public int removeDaSensitiveLevel(Collection<Long> idList) {
        // Batch delete sensitive level
        return daSensitiveLevelMapper.deleteBatchIds(idList);
    }

    @Override
    public DaSensitiveLevelDO getDaSensitiveLevelById(Long id) {
        return daSensitiveLevelMapper.selectById(id);
    }

    @Override
    public List<DaSensitiveLevelDO> getDaSensitiveLevelList() {
        return daSensitiveLevelMapper.selectList();
    }

    @Override
    public Map<Long, DaSensitiveLevelDO> getDaSensitiveLevelMap() {
        List<DaSensitiveLevelDO> daSensitiveLevelList = daSensitiveLevelMapper.selectList();
        return daSensitiveLevelList.stream()
                .collect(Collectors.toMap(
                        DaSensitiveLevelDO::getId,
                        daSensitiveLevelDO -> daSensitiveLevelDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import sensitive level data
     *
     * @param importExcelList Sensitive level data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName        Operator name
     * @return Result
     */
    @Override
    public String importDaSensitiveLevel(List<DaSensitiveLevelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaSensitiveLevelRespVO respVO : importExcelList) {
            try {
                DaSensitiveLevelDO daSensitiveLevelDO = BeanUtils.toBean(respVO, DaSensitiveLevelDO.class);
                Long daSensitiveLevelId = respVO.getId();
                if (isUpdateSupport) {
                    if (daSensitiveLevelId != null) {
                        DaSensitiveLevelDO existingDaSensitiveLevel = daSensitiveLevelMapper.selectById(daSensitiveLevelId);
                        if (existingDaSensitiveLevel != null) {
                            daSensitiveLevelMapper.updateById(daSensitiveLevelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "Data update successful, ID {0} {1} record.", daSensitiveLevelId, MessageUtils.messageWithFallback("da.entity.sensitivity.level", "Sensitivity level")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", daSensitiveLevelId, MessageUtils.messageWithFallback("da.entity.sensitivity.level", "Sensitivity level")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DaSensitiveLevelDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daSensitiveLevelId);
                    DaSensitiveLevelDO existingDaSensitiveLevel = daSensitiveLevelMapper.selectOne(queryWrapper);
                    if (existingDaSensitiveLevel == null) {
                        daSensitiveLevelMapper.insert(daSensitiveLevelDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", daSensitiveLevelId, MessageUtils.messageWithFallback("da.entity.sensitivity.level", "Sensitivity level")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", daSensitiveLevelId, MessageUtils.messageWithFallback("da.entity.sensitivity.level", "Sensitivity level")));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                    "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                    failureNum, failureDetails));
            throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                    "Congratulations! All data imported! Total: {0} records.", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public Boolean updateStatus(Long id, Long status) {
        DaAssetColumnPageReqVO daAssetColumnPageReqVO = new DaAssetColumnPageReqVO();
        daAssetColumnPageReqVO.setSensitiveLevelId(id.toString());
        List<DaAssetColumnDO> daAssetColumnList = daAssetColumnService.getDaAssetColumnList(daAssetColumnPageReqVO);
        if (!daAssetColumnList.isEmpty()) {
            return false;
        }
        return this.update(Wrappers.lambdaUpdate(DaSensitiveLevelDO.class)
                .eq(DaSensitiveLevelDO::getId, id)
                .set(DaSensitiveLevelDO::getOnlineFlag, status));
    }
}
