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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataElemCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataElemCatDO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTaskCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttDataElemCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttDataElemCatService;
import tech.qiantong.qdata.module.dp.api.service.dataElem.IDataElemApiService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Element Category Management - Service business layer processing
 *
 * @author qdata
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttDataElemCatServiceImpl extends ServiceImpl<AttDataElemCatMapper, AttDataElemCatDO> implements IAttDataElemCatService {
    @Resource
    private AttDataElemCatMapper attDataElemCatMapper;
    @Resource
    private IDataElemApiService dataElemApiService;

    @Override
    public PageResult<AttDataElemCatDO> getAttDataElemCatPage(AttDataElemCatPageReqVO pageReqVO) {
        return attDataElemCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttDataElemCat(AttDataElemCatSaveReqVO createReqVO) {
        AttDataElemCatDO dictType = BeanUtils.toBean(createReqVO, AttDataElemCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attDataElemCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttDataElemCat(AttDataElemCatSaveReqVO updateReqVO) {
        AttDataElemCatDO attDataElemCatDO = attDataElemCatMapper.selectById(updateReqVO.getId());
        if (attDataElemCatDO == null) {
            return 0;
        }
        // Update Data Element Category Management
        AttDataElemCatDO updateObj = BeanUtils.toBean(updateReqVO, AttDataElemCatDO.class);
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            Long countData = dataElemApiService.getCountByCatCode(attDataElemCatDO.getCode());
            if (countData > 0) {
                throw new ServiceException("att.error.disable.elem", "Data elements exist, disable not allowed");
            }
            attDataElemCatMapper.updateValidFlag(attDataElemCatDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            AttDataElemCatDO parent = attDataElemCatMapper.selectById(attDataElemCatDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("att.error.parent.disabled", "Parent must be enabled first");
            }
        }
        return attDataElemCatMapper.updateById(updateObj);
    }

    @Override
    public int removeAttDataElemCat(Collection<Long> idList) {
        int count = 0;
        for (Long id : idList) {
            AttDataElemCatDO cat = baseMapper.selectById(id);
            // Check if data assets exist
            if (dataElemApiService.getCountByCatCode(cat.getCode()) > 0) {
                throw new ServiceException("att.error.delete.elem", "Data elements exist, Delete not allowed");
            }
            if (cat != null) {
                count += baseMapper.delete(Wrappers.lambdaQuery(AttDataElemCatDO.class)
                        .likeRight(AttDataElemCatDO::getCode, cat.getCode()));
            }
        }
        return count;
    }

    @Override
    public AttDataElemCatDO getAttDataElemCatById(Long id) {
        return attDataElemCatMapper.selectById(id);
    }

    @Override
    public List<AttDataElemCatDO> getAttDataElemCatList() {
        return attDataElemCatMapper.selectList();
    }

    @Override
    public List<AttDataElemCatDO> getAttDataElemCatList(AttDataElemCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttDataElemCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttDataElemCatDO::getName, reqVO.getName())
                .eqIfPresent(AttDataElemCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttDataElemCatDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(AttDataElemCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttDataElemCatDO::getDescription, reqVO.getDescription())
                .likeRightIfPresent(AttDataElemCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttDataElemCatDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AttDataElemCatDO::getValidFlag,reqVO.getValidFlag())
                .orderByAsc(AttDataElemCatDO::getSortOrder);
        return attDataElemCatMapper.selectList(queryWrapperX);
    }

    @Override
    public Map<Long, AttDataElemCatDO> getAttDataElemCatMap() {
        List<AttDataElemCatDO> attDataElemCatList = attDataElemCatMapper.selectList();
        return attDataElemCatList.stream()
                .collect(Collectors.toMap(
                        AttDataElemCatDO::getId,
                        attDataElemCatDO -> attDataElemCatDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import Data Element Category Management data
     *
     *  importExcelList Data Element Category Management data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     *  operName Operator
     *  Result
     */
    @Override
    public String importAttDataElemCat(List<AttDataElemCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("att.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttDataElemCatRespVO respVO : importExcelList) {
            try {
                AttDataElemCatDO attDataElemCatDO = BeanUtils.toBean(respVO, AttDataElemCatDO.class);
                Long attDataElemCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attDataElemCatId != null) {
                        AttDataElemCatDO existingAttDataElemCat = attDataElemCatMapper.selectById(attDataElemCatId);
                        if (existingAttDataElemCat != null) {
                            attDataElemCatMapper.updateById(attDataElemCatDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                    "数据Update 成功，ID为 " + attDataElemCatId + " 的数据元类目管理记录。", attDataElemCatId, "数据元类目管理"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                    "数据Update 失败，ID为 " + attDataElemCatId + " 的数据元类目管理记录不存在。", attDataElemCatId, "数据元类目管理"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                "数据Update 失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<AttDataElemCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attDataElemCatId);
                    AttDataElemCatDO existingAttDataElemCat = attDataElemCatMapper.selectOne(queryWrapper);
                    if (existingAttDataElemCat == null) {
                        attDataElemCatMapper.insert(attDataElemCatDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                "数据插入成功，ID为 " + attDataElemCatId + " 的数据元类目管理记录。", attDataElemCatId, "数据元类目管理"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                "数据插入失败，ID为 " + attDataElemCatId + " 的数据元类目管理记录已存在。", attDataElemCatId, "数据元类目管理"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("att.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("att.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("att.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("att.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
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
        LambdaQueryWrapper<AttDataElemCatDO> query = new LambdaQueryWrapper<AttDataElemCatDO>()
                .eq(AttDataElemCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttDataElemCatDO::getCode, parentCode)
                .isNotNull(AttDataElemCatDO::getCode)
                .orderByDesc(AttDataElemCatDO::getCode);
        List<AttDataElemCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                // Case 1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                // Case 2
                AttDataElemCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            // Case 3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }
}
