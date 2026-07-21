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
import tech.qiantong.qdata.module.att.api.cat.dto.AttDataDevCatReqDTO;
import tech.qiantong.qdata.module.att.api.cat.dto.AttDataDevCatRespDTO;
import tech.qiantong.qdata.module.att.api.service.cat.IAttDataDevCatApiService;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataDevCatDO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTaskCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttDataDevCatMapper;
import tech.qiantong.qdata.module.att.service.cat.IAttDataDevCatService;
import tech.qiantong.qdata.module.dpp.api.service.etl.DppEtlTaskService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Development Category Management - Service business layer processing
 *
 * @author qdata
 * @date 2025-03-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttDataDevCatServiceImpl extends ServiceImpl<AttDataDevCatMapper, AttDataDevCatDO> implements IAttDataDevCatService, IAttDataDevCatApiService {
    @Resource
    private AttDataDevCatMapper attDataDevCatMapper;
    @Resource
    private DppEtlTaskService dppEtlTaskService;

    @Override
    public PageResult<AttDataDevCatDO> getAttDataDevCatPage(AttDataDevCatPageReqVO pageReqVO) {
        return attDataDevCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttDataDevCat(AttDataDevCatSaveReqVO createReqVO) {
        normalizeAndValidate(createReqVO);
        checkDuplicate(createReqVO.getId(), createReqVO.getParentId(), createReqVO.getName());
        AttDataDevCatDO dictType = BeanUtils.toBean(createReqVO, AttDataDevCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attDataDevCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttDataDevCat(AttDataDevCatSaveReqVO updateReqVO) {
        normalizeAndValidate(updateReqVO);
        AttDataDevCatDO existing = attDataDevCatMapper.selectById(updateReqVO.getId());
        if (existing == null) {
            throw new ServiceException("att.error.category.notfound", "Category does not exist");
        }
        checkDuplicate(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());
        checkParentCycle(existing, updateReqVO.getParentId());

        // Update Data Development Category Management
        AttDataDevCatDO updateObj = BeanUtils.toBean(updateReqVO, AttDataDevCatDO.class);
        int rows = attDataDevCatMapper.updateById(updateObj);
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            this.lambdaUpdate().likeRight(AttDataDevCatDO::getCode, existing.getCode())
                    .set(AttDataDevCatDO::getValidFlag, false).update();
        }
        return rows;
    }

    @Override
    public List<AttDataDevCatRespDTO> getAttDataDevCatApiList(AttDataDevCatReqDTO reqVO) {
        MPJLambdaWrapper<AttDataDevCatDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(AttDataDevCatDO.class)
                .like(StringUtils.isNotBlank(reqVO.getName()), AttDataDevCatDO::getName, reqVO.getName());
        List<AttDataDevCatDO> attTaskCatDOS = attDataDevCatMapper.selectList(wrapper);
        return BeanUtils.toBean(attTaskCatDOS, AttDataDevCatRespDTO.class);
    }

    @Override
    public int removeAttDataDevCat(Collection<Long> idList) {
        for (AttDataDevCatDO category : attDataDevCatMapper.selectBatchIds(idList)) {
            long childCount = this.lambdaQuery().likeRight(AttDataDevCatDO::getCode, category.getCode())
                    .ne(AttDataDevCatDO::getId, category.getId()).count();
            long taskCount = dppEtlTaskService.getCountByCatCode(category.getCode(), java.util.Collections.singletonList("3"));
            if (childCount > 0 || taskCount > 0) {
                throw new ServiceException("att.error.category.not.empty",
                        "The category contains {0} subcategories and {1} tasks and cannot be deleted directly",
                        childCount, taskCount);
            }
        }
        return attDataDevCatMapper.deleteBatchIds(idList);
    }

    private void normalizeAndValidate(AttDataDevCatSaveReqVO reqVO) {
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
        long count = this.lambdaQuery().eq(AttDataDevCatDO::getParentId, parentId)
                .eq(AttDataDevCatDO::getName, name).ne(id != null, AttDataDevCatDO::getId, id).count();
        if (count > 0) {
            throw new ServiceException("att.error.category.name.duplicate",
                    "A category with the same name already exists under the selected parent");
        }
    }

    private void checkParentCycle(AttDataDevCatDO current, Long parentId) {
        if (parentId == null || parentId == 0) return;
        if (parentId.equals(current.getId())) {
            throw new ServiceException("att.error.category.parent.invalid",
                    "A category or its descendant cannot be selected as its parent");
        }
        AttDataDevCatDO parent = attDataDevCatMapper.selectById(parentId);
        if (parent == null || (parent.getCode() != null && parent.getCode().startsWith(current.getCode()))) {
            throw new ServiceException("att.error.category.parent.invalid",
                    "A category or its descendant cannot be selected as its parent");
        }
    }

    @Override
    public AttDataDevCatDO getAttDataDevCatById(Long id) {
        return attDataDevCatMapper.selectById(id);
    }

    @Override
    public List<AttDataDevCatDO> getAttDataDevCatList() {
        return attDataDevCatMapper.selectList();
    }

    @Override
    public List<AttDataDevCatDO> getAttDataDevCatList(AttDataDevCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttDataDevCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttDataDevCatDO::getName, reqVO.getName())
                .likeRightIfPresent(AttDataDevCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttDataDevCatDO::getProjectId,reqVO.getProjectId())
                .eqIfPresent(AttDataDevCatDO::getProjectCode,reqVO.getProjectCode())
                .eqIfPresent(AttDataDevCatDO::getValidFlag,reqVO.getValidFlag())
                .orderByAsc(AttDataDevCatDO::getSortOrder);
        return attDataDevCatMapper.selectList(queryWrapperX);
    }

    @Override
    public Map<Long, AttDataDevCatDO> getAttDataDevCatMap() {
        List<AttDataDevCatDO> attDataDevCatList = attDataDevCatMapper.selectList();
        return attDataDevCatList.stream()
                .collect(Collectors.toMap(
                        AttDataDevCatDO::getId,
                        attDataDevCatDO -> attDataDevCatDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
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
        LambdaQueryWrapper<AttDataDevCatDO> query = new LambdaQueryWrapper<AttDataDevCatDO>()
                .eq(AttDataDevCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttDataDevCatDO::getCode, parentCode)
                .isNotNull(AttDataDevCatDO::getCode)
                .orderByDesc(AttDataDevCatDO::getCode);
        List<AttDataDevCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                // Case 1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                // Case 2
                AttDataDevCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            // Case 3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }


    /**
     * Import Data Development Category Management data
     *
     *  importExcelList Data Development Category Management data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     *  operName Operator
     *  Result
     */
    @Override
    public String importAttDataDevCat(List<AttDataDevCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttDataDevCatRespVO respVO : importExcelList) {
            try {
                AttDataDevCatDO attDataDevCatDO = BeanUtils.toBean(respVO, AttDataDevCatDO.class);
                Long attDataDevCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (attDataDevCatId != null) {
                        AttDataDevCatDO existingAttDataDevCat = attDataDevCatMapper.selectById(attDataDevCatId);
                        if (existingAttDataDevCat != null) {
                            attDataDevCatMapper.updateById(attDataDevCatDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                    "Data update successful, ID {0} {1} record.", attDataDevCatId, MessageUtils.messageWithFallback("att.entity.data.development.category", "Data development category")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", attDataDevCatId, MessageUtils.messageWithFallback("att.entity.data.development.category", "Data development category")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<AttDataDevCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attDataDevCatId);
                    AttDataDevCatDO existingAttDataDevCat = attDataDevCatMapper.selectOne(queryWrapper);
                    if (existingAttDataDevCat == null) {
                        attDataDevCatMapper.insert(attDataDevCatDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", attDataDevCatId, MessageUtils.messageWithFallback("att.entity.data.development.category", "Data development category")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", attDataDevCatId, MessageUtils.messageWithFallback("att.entity.data.development.category", "Data development category")));
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
