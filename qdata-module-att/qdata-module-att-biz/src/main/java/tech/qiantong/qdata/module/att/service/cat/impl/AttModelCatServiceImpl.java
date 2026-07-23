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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttModelCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttModelCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttModelCatService;
import tech.qiantong.qdata.module.dp.api.service.model.IDpModelApiService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Logical Model Category Management Service business layer processing
 *
 * @author qdata
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttModelCatServiceImpl extends ServiceImpl<AttModelCatMapper, AttModelCatDO> implements IAttModelCatService {
    @Resource
    private AttModelCatMapper attModelCatMapper;

    @Resource
    private IDpModelApiService dpModelApiService;

    @Override
    public PageResult<AttModelCatDO> getAttModelCatPage(AttModelCatPageReqVO pageReqVO) {
        return attModelCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttModelCat(AttModelCatSaveReqVO createReqVO) {
        AttModelCatDO dictType = BeanUtils.toBean(createReqVO, AttModelCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attModelCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttModelCat(AttModelCatSaveReqVO updateReqVO) {
        AttModelCatDO catDO = baseMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            Long countData = dpModelApiService.getCountByCatCode(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("att.error.disable.model", "Logical model exists, disable not allowed");
            }
            baseMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            AttModelCatDO parent = baseMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("att.error.parent.disabled", "Please enable the parent category first");
            }
        }
        // Update Logical Model Category Management
        AttModelCatDO updateObj = BeanUtils.toBean(updateReqVO, AttModelCatDO.class);
        return baseMapper.updateById(updateObj);
    }

    @Override
    public int removeAttModelCat(Collection<Long> idList) {
        int count = 0;
        for (Long id : idList) {
            AttModelCatDO cat = baseMapper.selectById(id);
            // Check if data exists
            if (dpModelApiService.getCountByCatCode(cat.getCode()) > 0) {
                throw new ServiceException("att.error.delete.model", "Logical model exists, deletion not allowed");
            }
            if (cat != null) {
                count += baseMapper.delete(Wrappers.lambdaQuery(AttModelCatDO.class)
                        .likeRight(AttModelCatDO::getCode, cat.getCode()));
            }
        }
        return count;
    }

    @Override
    public int removeAttModelCat(Long id) {
        int count = 0;
        AttModelCatDO cat = baseMapper.selectById(id);
        // Check if data exists
        if (dpModelApiService.getCountByCatCode(cat.getCode()) > 0) {
            throw new ServiceException("att.error.delete.model", "Logical model exists, deletion not allowed");
        }
        if (cat != null) {
            count += baseMapper.delete(Wrappers.lambdaQuery(AttModelCatDO.class)
                    .likeRight(AttModelCatDO::getCode, cat.getCode()));
        }
        return count;
    }

    @Override
    public AttModelCatDO getAttModelCatById(Long id) {
        return attModelCatMapper.selectById(id);
    }

    @Override
    public List<AttModelCatDO> getAttModelCatList() {
        return attModelCatMapper.selectList();
    }

    @Override
    public List<AttModelCatDO> getAttModelCatList(AttModelCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttModelCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttModelCatDO::getName, reqVO.getName())
                .likeRightIfPresent(AttModelCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttModelCatDO::getValidFlag, reqVO.getValidFlag())
                .orderByAsc(AttModelCatDO::getSortOrder);
        return attModelCatMapper.selectList(queryWrapperX);
    }

    @Override
    public Map<Long, AttModelCatDO> getAttModelCatMap() {
        List<AttModelCatDO> attModelCatList = attModelCatMapper.selectList();
        return attModelCatList.stream()
                .collect(Collectors.toMap(
                        AttModelCatDO::getId,
                        attModelCatDO -> attModelCatDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import Logical Model Category Management data
     *
     *  importExcelList Logical Model Category Management data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     *  operName Operator
     *  Result
     */
    @Override
    public String importAttModelCat(List<AttModelCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttModelCatRespVO respVO : importExcelList) {
            try {
                AttModelCatDO attModelCatDO = BeanUtils.toBean(respVO, AttModelCatDO.class);
                Long attModelCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attModelCatId != null) {
                        AttModelCatDO existingAttModelCat = attModelCatMapper.selectById(attModelCatId);
                        if (existingAttModelCat != null) {
                            attModelCatMapper.updateById(attModelCatDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                    "Data update successful, ID {0} {1} record.", attModelCatId, MessageUtils.messageWithFallback("att.entity.logical.model.category", "Logical model category")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", attModelCatId, MessageUtils.messageWithFallback("att.entity.logical.model.category", "Logical model category")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<AttModelCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attModelCatId);
                    AttModelCatDO existingAttModelCat = attModelCatMapper.selectOne(queryWrapper);
                    if (existingAttModelCat == null) {
                        attModelCatMapper.insert(attModelCatDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", attModelCatId, MessageUtils.messageWithFallback("att.entity.logical.model.category", "Logical model category")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", attModelCatId, MessageUtils.messageWithFallback("att.entity.logical.model.category", "Logical model category")));
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
        LambdaQueryWrapper<AttModelCatDO> query = new LambdaQueryWrapper<AttModelCatDO>()
                .eq(AttModelCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttModelCatDO::getCode, parentCode)
                .isNotNull(AttModelCatDO::getCode)
                .orderByDesc(AttModelCatDO::getCode);
        List<AttModelCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                // Case 1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                // Case 2
                AttModelCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            // Case 3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }
}
