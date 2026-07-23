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

package tech.qiantong.qdata.module.da.service.assetchild.projectRel.impl;

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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.projectRel.DaAssetProjectRelDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.projectRel.DaAssetProjectRelMapper;
import tech.qiantong.qdata.module.da.service.assetchild.projectRel.IDaAssetProjectRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Data asset and project relationship Service business layer processing
 *
 * @author qdata
 * @date 2025-04-18
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetProjectRelServiceImpl  extends ServiceImpl<DaAssetProjectRelMapper,DaAssetProjectRelDO> implements IDaAssetProjectRelService {
    @Resource
    private DaAssetProjectRelMapper daAssetProjectRelMapper;

    @Override
    public PageResult<DaAssetProjectRelDO> getDaAssetProjectRelPage(DaAssetProjectRelPageReqVO pageReqVO) {
        return daAssetProjectRelMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DaAssetProjectRelDO> getDaAssetProjectRelList(DaAssetProjectRelPageReqVO pageReqVO) {
        return null;
    }

    @Override
    public Long createDaAssetProjectRel(DaAssetProjectRelSaveReqVO createReqVO) {
        this.removeProjectRelByAssetId(createReqVO.getAssetId());
        DaAssetProjectRelDO dictType = BeanUtils.toBean(createReqVO, DaAssetProjectRelDO.class);
        daAssetProjectRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int removeProjectRelByAssetId(Long assetId) {
        daAssetProjectRelMapper.removeProjectRelByAssetId(assetId);
        return 1;
    }

    @Override
    public int updateDaAssetProjectRel(DaAssetProjectRelSaveReqVO updateReqVO) {
        // Validation checks

        // Update data asset and project relationship
        DaAssetProjectRelDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetProjectRelDO.class);
        return daAssetProjectRelMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetProjectRel(Collection<Long> idList) {
        // Batch delete data asset and project relationship
        return daAssetProjectRelMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetProjectRelDO getDaAssetProjectRelById(Long id) {
        return daAssetProjectRelMapper.selectById(id);
    }

    @Override
    public List<DaAssetProjectRelDO> getDaAssetProjectRelList() {
        return daAssetProjectRelMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetProjectRelDO> getDaAssetProjectRelMap() {
        List<DaAssetProjectRelDO> daAssetProjectRelList = daAssetProjectRelMapper.selectList();
        return daAssetProjectRelList.stream()
                .collect(Collectors.toMap(
                        DaAssetProjectRelDO::getId,
                        daAssetProjectRelDO -> daAssetProjectRelDO,
                        // Retain existing values
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data asset and project relationship data
     *
     * @param importExcelList Data asset and project relationship data list
     * @param isUpdateSupport Whether to support update, if already exists, update the data
     * @param operName Operator user
     * @return Result
     */
    @Override
    public String importDaAssetProjectRel(List<DaAssetProjectRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetProjectRelRespVO respVO : importExcelList) {
            try {
                DaAssetProjectRelDO daAssetProjectRelDO = BeanUtils.toBean(respVO, DaAssetProjectRelDO.class);
                Long daAssetProjectRelId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetProjectRelId != null) {
                        DaAssetProjectRelDO existingDaAssetProjectRel = daAssetProjectRelMapper.selectById(daAssetProjectRelId);
                        if (existingDaAssetProjectRel != null) {
                            daAssetProjectRelMapper.updateById(daAssetProjectRelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "Data update successful, ID {0} {1} record.", daAssetProjectRelId, MessageUtils.messageWithFallback("da.entity.asset.project.relation", "Data asset-project relation")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", daAssetProjectRelId, MessageUtils.messageWithFallback("da.entity.asset.project.relation", "Data asset-project relation")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DaAssetProjectRelDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetProjectRelId);
                    DaAssetProjectRelDO existingDaAssetProjectRel = daAssetProjectRelMapper.selectOne(queryWrapper);
                    if (existingDaAssetProjectRel == null) {
                        daAssetProjectRelMapper.insert(daAssetProjectRelDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", daAssetProjectRelId, MessageUtils.messageWithFallback("da.entity.asset.project.relation", "Data asset-project relation")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", daAssetProjectRelId, MessageUtils.messageWithFallback("da.entity.asset.project.relation", "Data asset-project relation")));
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
}
