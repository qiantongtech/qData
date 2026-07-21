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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttAssetCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttAssetCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttAssetCatService;
import tech.qiantong.qdata.module.da.api.service.asset.IDaAssetApiOutService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Asset Category Management - Service business layer processing
 *
 * @author qdata
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttAssetCatServiceImpl extends ServiceImpl<AttAssetCatMapper, AttAssetCatDO> implements IAttAssetCatService {
    @Resource
    private AttAssetCatMapper attAssetCatMapper;

    @Resource
    private IDaAssetApiOutService daAssetApiService;

    @Override
    public PageResult<AttAssetCatDO> getAttAssetCatPage(AttAssetCatPageReqVO pageReqVO) {
        return attAssetCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttAssetCat(AttAssetCatSaveReqVO createReqVO) {
        AttAssetCatDO dictType = BeanUtils.toBean(createReqVO, AttAssetCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attAssetCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttAssetCat(AttAssetCatSaveReqVO updateReqVO) {
        AttAssetCatDO catDO = baseMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            Long countData = daAssetApiService.getCountByCatCode(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("att.error.disable.asset", "Data asset exists, disable not allowed");
            }
            baseMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            AttAssetCatDO parent = baseMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("att.error.parent.disabled", "Please enable the parent category first");
            }
        }
        // Update Data Asset Category Management
        AttAssetCatDO updateObj = BeanUtils.toBean(updateReqVO, AttAssetCatDO.class);
        return attAssetCatMapper.updateById(updateObj);
    }

    @Override
    public int removeAttAssetCat(Collection<Long> idList) {
        int count = 0;
        for (Long id : idList) {
            AttAssetCatDO cat = baseMapper.selectById(id);
            // Check if data assets exist
            if (daAssetApiService.getCountByCatCode(cat.getCode()) > 0) {
                throw new ServiceException("att.error.delete.asset", "Data asset exists, deletion not allowed");
            }
            if (cat != null) {
                count += baseMapper.delete(Wrappers.lambdaQuery(AttAssetCatDO.class)
                        .likeRight(AttAssetCatDO::getCode, cat.getCode()));
            }
        }
        return count;
    }


    @Override
    public AttAssetCatDO getAttAssetCatById(Long id) {
        return attAssetCatMapper.selectById(id);
    }

    @Override
    public List<AttAssetCatDO> getAttAssetCatList() {
        return attAssetCatMapper.selectList();
    }

    @Override
    public List<AttAssetCatDO> getAttAssetCatList(AttAssetCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttAssetCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttAssetCatDO::getName, reqVO.getName())
                .likeRightIfPresent(AttAssetCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttAssetCatDO::getValidFlag, reqVO.getValidFlag())
                .orderByAsc(AttAssetCatDO::getSortOrder);
        return attAssetCatMapper.selectList(queryWrapperX);
    }

    @Override
    public Map<Long, AttAssetCatDO> getAttAssetCatMap() {
        List<AttAssetCatDO> attAssetCatList = attAssetCatMapper.selectList();
        return attAssetCatList.stream()
                .collect(Collectors.toMap(
                        AttAssetCatDO::getId,
                        attAssetCatDO -> attAssetCatDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import Data Asset Category Management data
     *
     *  importExcelList Data Asset Category Management data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     *  operName Operator
     *  Result
     */
    @Override
    public String importAttAssetCat(List<AttAssetCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttAssetCatRespVO respVO : importExcelList) {
            try {
                AttAssetCatDO attAssetCatDO = BeanUtils.toBean(respVO, AttAssetCatDO.class);
                Long attAssetCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attAssetCatId != null) {
                        AttAssetCatDO existingAttAssetCat = attAssetCatMapper.selectById(attAssetCatId);
                        if (existingAttAssetCat != null) {
                            attAssetCatMapper.updateById(attAssetCatDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                    "Data update successful, ID {0} {1} record.", attAssetCatId, MessageUtils.messageWithFallback("att.entity.data.asset.category", "Data asset category")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", attAssetCatId, MessageUtils.messageWithFallback("att.entity.data.asset.category", "Data asset category")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<AttAssetCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attAssetCatId);
                    AttAssetCatDO existingAttAssetCat = attAssetCatMapper.selectOne(queryWrapper);
                    if (existingAttAssetCat == null) {
                        attAssetCatMapper.insert(attAssetCatDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", attAssetCatId, MessageUtils.messageWithFallback("att.entity.data.asset.category", "Data asset category")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", attAssetCatId, MessageUtils.messageWithFallback("att.entity.data.asset.category", "Data asset category")));
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
         * Three cases
         * 1. No data in database, call YouBianCodeUtil.getNextYouBianCode(null);
         * 2. Adding child node with no sibling elements: YouBianCodeUtil.getSubYouBianCode(parentCode, null);
         * 3. Adding child node with sibling elements: YouBianCodeUtil.getNextYouBianCode(lastCode);
         * */
        // Find siblings to determine the last largest code value
        LambdaQueryWrapper<AttAssetCatDO> query = new LambdaQueryWrapper<AttAssetCatDO>()
                .eq(AttAssetCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttAssetCatDO::getCode, parentCode)
                .isNotNull(AttAssetCatDO::getCode)
                .orderByDesc(AttAssetCatDO::getCode);
        List<AttAssetCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                // Case 1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                // Case 2
                AttAssetCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            // Case 3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }

}
