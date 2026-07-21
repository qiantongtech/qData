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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.cat.dto.AttTaskCatReqDTO;
import tech.qiantong.qdata.module.att.api.cat.dto.AttTaskCatRespDTO;
import tech.qiantong.qdata.module.att.api.service.cat.IAttTaskCatApiService;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTaskCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttModelCatDO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTaskCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttTaskCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttTaskCatService;
import tech.qiantong.qdata.module.dpp.api.service.etl.DppEtlTaskService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Integration Task Category Management Service business layer processing
 *
 * @author qdata
 * @date 2025-03-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttTaskCatServiceImpl extends ServiceImpl<AttTaskCatMapper, AttTaskCatDO> implements IAttTaskCatService, IAttTaskCatApiService {
    @Resource
    private AttTaskCatMapper attTaskCatMapper;
    @Resource
    private DppEtlTaskService dppEtlTaskService;

    @Override
    public PageResult<AttTaskCatDO> getAttTaskCatPage(AttTaskCatPageReqVO pageReqVO) {
        return attTaskCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttTaskCat(AttTaskCatSaveReqVO createReqVO) {
        normalizeAndValidate(createReqVO);
        checkDuplicate(createReqVO.getId(), createReqVO.getParentId(), createReqVO.getName());
        AttTaskCatDO dictType = BeanUtils.toBean(createReqVO, AttTaskCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attTaskCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public List<AttTaskCatRespDTO> getAttTaskCatApiList(AttTaskCatReqDTO reqVO) {
        MPJLambdaWrapper<AttTaskCatDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(AttTaskCatDO.class)
                .like(StringUtils.isNotBlank(reqVO.getName()), AttTaskCatDO::getName, reqVO.getName());
        List<AttTaskCatDO> attTaskCatDOS = attTaskCatMapper.selectList(wrapper);
        return BeanUtils.toBean(attTaskCatDOS, AttTaskCatRespDTO.class);
    }

    @Override
    public int updateAttTaskCat(AttTaskCatSaveReqVO updateReqVO) {
        normalizeAndValidate(updateReqVO);
        AttTaskCatDO existing = attTaskCatMapper.selectById(updateReqVO.getId());
        if (existing == null) {
            throw new ServiceException("att.error.category.notfound", "Category does not exist");
        }
        checkDuplicate(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());
        checkParentCycle(existing, updateReqVO.getParentId());

        // Update Data Integration Task Category Management
        AttTaskCatDO updateObj = BeanUtils.toBean(updateReqVO, AttTaskCatDO.class);
        int rows = attTaskCatMapper.updateById(updateObj);
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            this.lambdaUpdate().likeRight(AttTaskCatDO::getCode, existing.getCode())
                    .set(AttTaskCatDO::getValidFlag, false).update();
        }
        return rows;
    }

    @Override
    public int removeAttTaskCat(Collection<Long> idList) {
        for (AttTaskCatDO category : attTaskCatMapper.selectBatchIds(idList)) {
            long childCount = this.lambdaQuery().likeRight(AttTaskCatDO::getCode, category.getCode())
                    .ne(AttTaskCatDO::getId, category.getId()).count();
            long taskCount = dppEtlTaskService.getCountByCatCode(category.getCode(), java.util.Collections.singletonList("1"));
            if (childCount > 0 || taskCount > 0) {
                throw new ServiceException("att.error.category.not.empty",
                        "The category contains {0} subcategories and {1} tasks and cannot be deleted directly",
                        childCount, taskCount);
            }
        }
        return attTaskCatMapper.deleteBatchIds(idList);
    }

    private void normalizeAndValidate(AttTaskCatSaveReqVO reqVO) {
        reqVO.setName(reqVO.getName() == null ? null : reqVO.getName().trim());
        if (StringUtils.isBlank(reqVO.getName()) || reqVO.getName().matches(".*\\s+.*")
                || !reqVO.getName().matches(".*[A-Za-z0-9\\u4e00-\\u9fa5].*")) {
            throw new ServiceException("att.error.category.name.invalid",
                    "Category name cannot be empty, contain whitespace, or consist only of symbols");
        }
        if (reqVO.getSortOrder() != null && reqVO.getSortOrder() < 0) {
            throw new ServiceException("att.error.category.sort.invalid",
                    "Invalid sort value; enter a non-negative integer");
        }
    }

    private void checkDuplicate(Long id, Long parentId, String name) {
        long count = this.lambdaQuery().eq(AttTaskCatDO::getParentId, parentId)
                .eq(AttTaskCatDO::getName, name).ne(id != null, AttTaskCatDO::getId, id).count();
        if (count > 0) {
            throw new ServiceException("att.error.category.name.duplicate",
                    "A category with the same name already exists under the selected parent");
        }
    }

    private void checkParentCycle(AttTaskCatDO current, Long parentId) {
        if (parentId == null || parentId == 0) return;
        if (parentId.equals(current.getId())) {
            throw new ServiceException("att.error.category.parent.invalid",
                    "A category or its descendant cannot be selected as its parent");
        }
        AttTaskCatDO parent = attTaskCatMapper.selectById(parentId);
        if (parent == null || (parent.getCode() != null && parent.getCode().startsWith(current.getCode()))) {
            throw new ServiceException("att.error.category.parent.invalid",
                    "A category or its descendant cannot be selected as its parent");
        }
    }

    @Override
    public AttTaskCatDO getAttTaskCatById(Long id) {
        return attTaskCatMapper.selectById(id);
    }

    @Override
    public List<AttTaskCatDO> getAttTaskCatList() {
        return attTaskCatMapper.selectList();
    }

    @Override
    public List<AttTaskCatDO> getAttTaskCatList(AttTaskCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttTaskCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttTaskCatDO::getName, reqVO.getName())
                .eqIfPresent(AttTaskCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttTaskCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttTaskCatDO::getDescription, reqVO.getDescription())
                .eqIfPresent(AttTaskCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttTaskCatDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AttTaskCatDO::getProjectId,reqVO.getProjectId())
                .eqIfPresent(AttTaskCatDO::getProjectCode,reqVO.getProjectCode())
                .eqIfPresent(AttTaskCatDO::getValidFlag,reqVO.getValidFlag())
                .orderByAsc(AttTaskCatDO::getSortOrder);
        return attTaskCatMapper.selectList(queryWrapperX);
    }

    @Override
    public Map<Long, AttTaskCatDO> getAttTaskCatMap() {
        List<AttTaskCatDO> attTaskCatList = attTaskCatMapper.selectList();
        return attTaskCatList.stream()
                .collect(Collectors.toMap(
                        AttTaskCatDO::getId,
                        attTaskCatDO -> attTaskCatDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
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
        LambdaQueryWrapper<AttTaskCatDO> query = new LambdaQueryWrapper<AttTaskCatDO>()
                .eq(AttTaskCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttTaskCatDO::getCode, parentCode)
                .isNotNull(AttTaskCatDO::getCode)
                .orderByDesc(AttTaskCatDO::getCode);
        List<AttTaskCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                // Case 1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                // Case 2
                AttTaskCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            // Case 3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }

    /**
     * Import Data Integration Task Category Management data
     *
     * @param importExcelList Data Integration Task Category Management data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName Operator
     * @return Import result
     */
    @Override
    public String importAttTaskCat(List<AttTaskCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttTaskCatRespVO respVO : importExcelList) {
            try {
                AttTaskCatDO attTaskCatDO = BeanUtils.toBean(respVO, AttTaskCatDO.class);
                Long attTaskCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attTaskCatId != null) {
                        AttTaskCatDO existingAttTaskCat = attTaskCatMapper.selectById(attTaskCatId);
                        if (existingAttTaskCat != null) {
                            attTaskCatMapper.updateById(attTaskCatDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                    "Data update successful, ID {0} {1} record.", attTaskCatId, MessageUtils.messageWithFallback("att.entity.data.integration.task.category", "Data integration task category")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", attTaskCatId, MessageUtils.messageWithFallback("att.entity.data.integration.task.category", "Data integration task category")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<AttTaskCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attTaskCatId);
                    AttTaskCatDO existingAttTaskCat = attTaskCatMapper.selectOne(queryWrapper);
                    if (existingAttTaskCat == null) {
                        attTaskCatMapper.insert(attTaskCatDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", attTaskCatId, MessageUtils.messageWithFallback("att.entity.data.integration.task.category", "Data integration task category")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", attTaskCatId, MessageUtils.messageWithFallback("att.entity.data.integration.task.category", "Data integration task category")));
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
}
