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

package tech.qiantong.qdata.module.att.service.cat.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttQualityCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttQualityCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttQualityCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttQualityCatService;
import tech.qiantong.qdata.module.dpp.api.service.qa.DppQualityTaskApiService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * Data Quality Category Service business layer processing
 *
 * @author qdata
 * @date 2025-07-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttQualityCatServiceImpl  extends ServiceImpl<AttQualityCatMapper,AttQualityCatDO> implements IAttQualityCatService {
    @Resource
    private AttQualityCatMapper attQualityCatMapper;
    @Resource
    private DppQualityTaskApiService taskApiService;

    @Override
    public PageResult<AttQualityCatDO> getAttQualityCatPage(AttQualityCatPageReqVO pageReqVO) {
        return attQualityCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttQualityCat(AttQualityCatSaveReqVO createReqVO) {
        AttQualityCatDO dictType = BeanUtils.toBean(createReqVO, AttQualityCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attQualityCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttQualityCat(AttQualityCatSaveReqVO updateReqVO) {
        AttQualityCatDO catDO = attQualityCatMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            Long countData = taskApiService.getCountByCatCode(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("att.error.disable.quality", "Data quality task exists, disable not allowed");
            }
            attQualityCatMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            AttQualityCatDO parent = attQualityCatMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("att.error.parent.disabled", "Please enable the parent category first");
            }
        }
        // Update Data Quality Category
        AttQualityCatDO updateObj = BeanUtils.toBean(updateReqVO, AttQualityCatDO.class);
        return attQualityCatMapper.updateById(updateObj);
    }
    @Override
    public int removeAttQualityCat(Collection<Long> idList) {
        // Check if data exists
        List<AttQualityCatDO> attQualityCatDOS = attQualityCatMapper.selectBatchIds(idList);
        for (AttQualityCatDO cat : attQualityCatDOS) {
            if (taskApiService.getCountByCatCode(cat.getCode()) > 0) {
                throw new ServiceException("att.error.delete.quality", "Data quality task exists, deletion not allowed");
            }
        }
        // Batch delete Data Quality Category
        return attQualityCatMapper.deleteBatchIds(idList);
    }

    @Override
    public AttQualityCatDO getAttQualityCatById(Long id) {
        return attQualityCatMapper.selectById(id);
    }

    @Override
    public List<AttQualityCatDO> getAttQualityCatList(AttQualityCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttQualityCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttQualityCatDO::getName, reqVO.getName())
                .likeRightIfPresent(AttQualityCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttQualityCatDO::getValidFlag, reqVO.getValidFlag())
                .orderByAsc(AttQualityCatDO::getSortOrder);
        return attQualityCatMapper.selectList(queryWrapperX);
    }

    @Override
    public Map<Long, AttQualityCatDO> getAttQualityCatMap() {
        List<AttQualityCatDO> attQualityCatList = attQualityCatMapper.selectList();
        return attQualityCatList.stream()
                .collect(Collectors.toMap(
                        AttQualityCatDO::getId,
                        attQualityCatDO -> attQualityCatDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import Data Quality Category data
     *
     *  importExcelList Data Quality Category data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     *  operName Operator
     *  Result
     */
    @Override
    public String importAttQualityCat(List<AttQualityCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttQualityCatRespVO respVO : importExcelList) {
            try {
                AttQualityCatDO attQualityCatDO = BeanUtils.toBean(respVO, AttQualityCatDO.class);
                Long attQualityCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attQualityCatId != null) {
                        AttQualityCatDO existingAttQualityCat = attQualityCatMapper.selectById(attQualityCatId);
                        if (existingAttQualityCat != null) {
                            attQualityCatMapper.updateById(attQualityCatDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                    "Data update successful, ID {0} {1} record.", attQualityCatId, MessageUtils.messageWithFallback("att.entity.data.quality.category", "Data quality category")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", attQualityCatId, MessageUtils.messageWithFallback("att.entity.data.quality.category", "Data quality category")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<AttQualityCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attQualityCatId);
                    AttQualityCatDO existingAttQualityCat = attQualityCatMapper.selectOne(queryWrapper);
                    if (existingAttQualityCat == null) {
                        attQualityCatMapper.insert(attQualityCatDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", attQualityCatId, MessageUtils.messageWithFallback("att.entity.data.quality.category", "Data quality category")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", attQualityCatId, MessageUtils.messageWithFallback("att.entity.data.quality.category", "Data quality category")));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("att.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("att.import.result.fail",
                    "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                    failureNum, failureDetails));
            throw new ServiceException("att.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("att.import.result.success",
                    "Congratulations! All data imported successfully! Total: {0} records.", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public String createCode(Long parentId, String parentCode) {
        String categoryCode = null;
        /*
         * Three scenarios:
         * 1. No data in database - call YouBianCodeUtil.getNextYouBianCode(null);
         * 2. Adding child node, no sibling elements - YouBianCodeUtil.getSubYouBianCode(parentCode,null);
         * 3. Adding child node with sibling elements - YouBianCodeUtil.getNextYouBianCode(lastCode);
         * */
        // Find same category and determine the previous maximum code value
        LambdaQueryWrapper<AttQualityCatDO> query = new LambdaQueryWrapper<AttQualityCatDO>()
                .eq(AttQualityCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttQualityCatDO::getCode, parentCode)
                .isNotNull(AttQualityCatDO::getCode)
                .orderByDesc(AttQualityCatDO::getCode);
        List<AttQualityCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                // Case 1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                // Case 2
                AttQualityCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            // Case 3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }
}
