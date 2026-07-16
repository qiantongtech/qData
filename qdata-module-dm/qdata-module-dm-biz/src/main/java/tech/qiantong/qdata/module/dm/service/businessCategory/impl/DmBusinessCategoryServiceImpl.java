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

package tech.qiantong.qdata.module.dm.service.businessCategory.impl;

import com.alibaba.fastjson2.JSONObject;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.domain.TreeData;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.YouBianCodeUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dm.api.service.businessCategory.IDmBusinessCategoryApiService;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategoryRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.businessCategory.vo.DmBusinessCategorySaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessCategoryDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.businessCategory.DmBusinessDomainRelDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;
import tech.qiantong.qdata.module.dm.dal.mapper.businessCategory.DmBusinessCategoryMapper;
import tech.qiantong.qdata.module.dm.dal.mapper.businessCategory.DmBusinessDomainRelMapper;
import tech.qiantong.qdata.module.dm.dal.mapper.dm.DmDataDomainMapper;
import tech.qiantong.qdata.module.dm.service.businessCategory.IDmBusinessCategoryService;
import tech.qiantong.qdata.module.system.mapper.SysUserMapper;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Business Category Service - Business Layer Processing
 *
 * @author qdata
 * @date 2026-04-08
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmBusinessCategoryServiceImpl extends ServiceImpl<DmBusinessCategoryMapper, DmBusinessCategoryDO> implements IDmBusinessCategoryService, IDmBusinessCategoryApiService {
    @Resource
    private DmBusinessCategoryMapper dmBusinessCategoryMapper;

    @Resource
    private DmBusinessDomainRelMapper dmBusinessDomainRelMapper;

    @Resource
    private DmDataDomainMapper dmDataDomainMapper;

    @Resource
    private SysUserMapper dmUserMapper;


    @Override
    public PageResult<DmBusinessCategoryDO> getDmBusinessCategoryPage(DmBusinessCategoryPageReqVO pageReqVO) {
        PageResult<DmBusinessCategoryDO> pageResult = dmBusinessCategoryMapper.selectPage(pageReqVO);
      /*  // Query data domain collection by business category ID and store in DmBusinessCategoryDO
        pageResult.getRows().forEach(item -> {
            item.setDataDomainList(dmDataDomainMapper.selectlistBybusinessDomainId(item.getId()));
        });*/
        return pageResult;
    }

    @Override
    public Long createDmBusinessCategory(DmBusinessCategorySaveReqVO createReqVO) {
        DmBusinessCategoryDO dictType = BeanUtils.toBean(createReqVO, DmBusinessCategoryDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        dmBusinessCategoryMapper.insert(dictType);
        // Insert data domain relation
        if (dictType.getDomainList() != null && !dictType.getDomainList().isEmpty()) {
            dictType.getDomainList().forEach(domain -> {
                domain.setBusinessCategoryId(dictType.getId());
                domain.setBusinessCategoryName(dictType.getName());
            });
            dmBusinessDomainRelMapper.insertBatch(dictType.getDomainList());
        }
        return dictType.getId();
    }

    @Override
    public int updateDmBusinessCategory(DmBusinessCategorySaveReqVO updateReqVO) {
        DmBusinessCategoryDO catDO = baseMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        // Check if the parent is set to itself
        if (catDO.getId().equals(updateReqVO.getParentId())) {
            throw new tech.qiantong.qdata.common.exception.ServiceException("Cannot select itself as the parent category");
        }

        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            baseMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            DmBusinessCategoryDO parent = baseMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new tech.qiantong.qdata.common.exception.ServiceException("Parent must be enabled first");
            }
        }
        // Check if the parent relationship has changed
        boolean flag = false;
        if (!catDO.getParentId().equals(updateReqVO.getParentId())) {
            updateReqVO.setCode(createCode(updateReqVO.getParentId(), null));
            flag = true;
        }

        // Update business category
        DmBusinessCategoryDO updateObj = BeanUtils.toBean(updateReqVO, DmBusinessCategoryDO.class);
        // Insert new data domain relation
        if (updateObj.getDomainList() != null && !updateObj.getDomainList().isEmpty()) {
            // First delete the old domain relations by business category ID
            dmBusinessDomainRelMapper.delete(new LambdaQueryWrapper<DmBusinessDomainRelDO>().eq(DmBusinessDomainRelDO::getBusinessCategoryId, updateObj.getId()));
            updateObj.getDomainList().forEach(domain -> {
                domain.setBusinessCategoryId(updateObj.getId());
                domain.setBusinessCategoryName(updateObj.getName());
            });
            dmBusinessDomainRelMapper.insertBatch(updateObj.getDomainList());
        }

        int i = dmBusinessCategoryMapper.updateById(updateObj);
        // If the parent relationship has changed
        if (flag) {
            // Update all child codes
            changeCodeByPid(updateObj.getId(), updateObj.getCode());
        }

        return i;
    }

    @Override
    public int removeDmBusinessCategory(Collection<Long> idList) {
        // First check if there are any child business categories. If so, prompt to delete child categories first.
        if (idList.stream()
                .anyMatch(id -> dmBusinessCategoryMapper.selectCount(new LambdaQueryWrapper<DmBusinessCategoryDO>().eq(DmBusinessCategoryDO::getParentId, id)) > 0)) {
            throw new IllegalArgumentException("Business category has child categories, cannot delete");
        }
        // First delete the domain relations associated with the business category
        dmBusinessDomainRelMapper.delete(new LambdaQueryWrapper<DmBusinessDomainRelDO>().in(DmBusinessDomainRelDO::getBusinessCategoryId, idList));
        // Delete the business category relations
        return dmBusinessCategoryMapper.deleteBatchIds(idList);
    }

    @Override
    public DmBusinessCategoryDO getDmBusinessCategoryById(Long id) {
        DmBusinessCategoryDO dmBusinessCategoryDO = dmBusinessCategoryMapper.selectById(id);
        // Query user table, convert ownerId to ownerName
        if (dmBusinessCategoryDO.getOwnerId() != null) {
            SysUser sysUser = dmUserMapper.selectUserById(dmBusinessCategoryDO.getOwnerId());
            if (sysUser != null) {
                dmBusinessCategoryDO.setOwnerName(sysUser.getNickName());
            }
        }
        // Query business category table, convert parentId to parentName
        if (dmBusinessCategoryDO.getParentId() != null) {
            DmBusinessCategoryDO categoryDO = dmBusinessCategoryMapper.selectById(dmBusinessCategoryDO.getParentId());
            if (categoryDO != null) {
                dmBusinessCategoryDO.setParentName(categoryDO.getName());
            }
        }
        // Query data domain ID collection by business category ID and store in DmBusinessCategoryDO
        dmBusinessCategoryDO.setDomainIds(dmBusinessDomainRelMapper.selectList(new LambdaQueryWrapper<DmBusinessDomainRelDO>().eq(DmBusinessDomainRelDO::getBusinessCategoryId, id))
                .stream()
                .map(DmBusinessDomainRelDO::getDataDomainId)
                .map(String::valueOf)
                .collect(Collectors.toList()));
        /*if (dmBusinessCategoryDO != null) {
            dmBusinessCategoryDO.setDataDomainList(dmDataDomainMapper.selectlistBybusinessDomainId(id));
        }*/
        return dmBusinessCategoryDO;
    }

    @Override
    public List<DmBusinessCategoryDO> getDmBusinessCategoryList(DmBusinessCategoryPageReqVO dmBusinessCategory) {
        List<DmBusinessCategoryDO> pageResult = dmBusinessCategoryMapper.selectAllList(dmBusinessCategory);
        // Query data domain collection by business category ID and store in DmBusinessCategoryDO
        pageResult.forEach(item -> {
            List<DmBusinessDomainRelDO> cc = dmBusinessDomainRelMapper.selectList(new LambdaQueryWrapper<DmBusinessDomainRelDO>().eq(DmBusinessDomainRelDO::getBusinessCategoryId, item.getId()));
            item.setDomainList(cc);
        });
        return pageResult;
    }


    @Override
    public Map<Long, DmBusinessCategoryDO> getDmBusinessCategoryMap() {
        List<DmBusinessCategoryDO> dmBusinessCategoryList = dmBusinessCategoryMapper.selectList();
        return dmBusinessCategoryList.stream()
                .collect(Collectors.toMap(DmBusinessCategoryDO::getId, dmBusinessCategoryDO -> dmBusinessCategoryDO,
                        // Keep existing value
                        (existing, replacement) -> existing));
    }


    /**
     * Import business category data
     *
     * @param importExcelList Business category data list
     * @param isUpdateSupport Whether to support update, if exists, update the data
     * @param operName        Operation user
     * @return Result
     */
    @Override
    public String importDmBusinessCategory(List<DmBusinessCategoryRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dm.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DmBusinessCategoryRespVO respVO : importExcelList) {
            try {
                DmBusinessCategoryDO dmBusinessCategoryDO = BeanUtils.toBean(respVO, DmBusinessCategoryDO.class);
                Long dmBusinessCategoryId = respVO.getId();
                if (isUpdateSupport) {
                    if (dmBusinessCategoryId != null) {
                        DmBusinessCategoryDO existingDmBusinessCategory = dmBusinessCategoryMapper.selectById(dmBusinessCategoryId);
                        if (existingDmBusinessCategory != null) {
                            dmBusinessCategoryMapper.updateById(dmBusinessCategoryDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dm.import.update.success",
                                    "Data update successful, business category record with ID " + dmBusinessCategoryId + ".", dmBusinessCategoryId, "BusinessCategory"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.fail",
                                    "Data update failed, business category record with ID " + dmBusinessCategoryId + " does not exist.", dmBusinessCategoryId, "BusinessCategory"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.id.missing",
                                "Data update failed, a record has no ID."));
                    }
                } else {
                    QueryWrapper<DmBusinessCategoryDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dmBusinessCategoryId);
                    DmBusinessCategoryDO existingDmBusinessCategory = dmBusinessCategoryMapper.selectOne(queryWrapper);
                    if (existingDmBusinessCategory == null) {
                        dmBusinessCategoryMapper.insert(dmBusinessCategoryDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dm.import.insert.success",
                                "Data insert successful, business category record with ID " + dmBusinessCategoryId + ".", dmBusinessCategoryId, "BusinessCategory"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dm.import.insert.fail",
                                "Data insert failed, business category record with ID " + dmBusinessCategoryId + " already exists.", dmBusinessCategoryId, "BusinessCategory"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("dm.import.error.detail",
                        "Data import failed, error: " + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("dm.import.result.fail",
                    "Import failed! " + failureNum + " records have incorrect format, errors below:<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("dm.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("dm.import.result.success",
                    "All data imported successfully! Total " + successNum + " records.", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public String createCode(Long parentId, String parentCode) {
        String categoryCode = null;
        /*
         * Divided into three scenarios:
         * 1. No data in the database, call YouBianCodeUtil.getNextYouBianCode(null);
         * 2. Adding child node, no sibling elements, call YouBianCodeUtil.getSubYouBianCode(parentCode,null);
         * 3. Adding child node with sibling elements, call YouBianCodeUtil.getNextYouBianCode(lastCode);
         * */
        // Find siblings to determine the previous maximum code value
        LambdaQueryWrapper<DmBusinessCategoryDO> query = new LambdaQueryWrapper<DmBusinessCategoryDO>().eq(DmBusinessCategoryDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), DmBusinessCategoryDO::getCode, parentCode)
                .isNotNull(DmBusinessCategoryDO::getCode)
                .orderByDesc(DmBusinessCategoryDO::getCode);
        List<DmBusinessCategoryDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                // Scenario 1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                // Scenario 2
                DmBusinessCategoryDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            // Scenario 3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }

    @Override
    public void changeCodeByPid(Long pid, String parentCode) {
        List<DmBusinessCategoryDO> list = baseMapper.selectList(Wrappers.lambdaQuery(DmBusinessCategoryDO.class)
                .eq(DmBusinessCategoryDO::getParentId, pid)
                .orderByAsc(DmBusinessCategoryDO::getCreateTime));
        if (list != null && list.size() > 0) {
            list.forEach(e -> {
                String codeOld = e.getCode();
                String codeNew = createCode(e.getParentId(), parentCode);
                e.setCode(codeNew);
                baseMapper.updateById(e);
                this.changeCodeByPid(e.getId(), e.getCode());
            });
        }
    }


    @Override
    public List<TreeData> getTreeData(String type) {
        // Get all enabled business categories
        List<DmBusinessCategoryDO> dmBusinessCategoryDOList = baseMapper.selectList(Wrappers.lambdaQuery(DmBusinessCategoryDO.class)
                .eq(DmBusinessCategoryDO::getValidFlag, true)
                .orderByAsc(DmBusinessCategoryDO::getSortOrder));

        // Build business category tree
        List<TreeData> treeDataList = dmBusinessCategoryDOList.stream()
                .map(v -> TreeData.builder()
                        .id(v.getId())
                        .parentId(v.getParentId())
                        .name(v.getName())
                        .type("1")
                        .otherData(JSONObject.of(
                                "code", v.getCode(),
                                "engName", v.getEngName()
                        ))
                        .build())
                .collect(Collectors.toList());

        Map<Long, TreeData> dmBusinessCategoryMap = treeDataList.stream()
                .collect(Collectors.toMap(k -> k.getId(), v -> v));

        for (TreeData treeData : treeDataList) {
            TreeData parent = dmBusinessCategoryMap.get(treeData.getParentId());
            if (parent != null) {
                List<TreeData> children = parent.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    parent.setChildren(children);
                }
                children.add(treeData);
            }
        }

        // Get data domains related to business categories
        MPJLambdaWrapperX<DmDataDomainDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        String statisticsSql = null;
        if (StringUtils.isNotBlank(type)) {
            switch (type) {
                case "1":
                    statisticsSql = "(SELECT COUNT(1) FROM DA_ASSET a WHERE t.ID = a.DATA_DOMAIN_ID) AS num";
                    break;
            }
            if (StringUtils.isNotBlank(statisticsSql)) {
                lambdaWrapper.select(statisticsSql);
            }
        }
        lambdaWrapper.selectAll(DmDataDomainDO.class)
                .selectAs(DmBusinessDomainRelDO::getBusinessCategoryId, "businessCategoryId")
                .innerJoin(DmBusinessDomainRelDO.class, DmBusinessDomainRelDO::getDataDomainId, DmDataDomainDO::getId)
                .innerJoin(DmBusinessCategoryDO.class, DmBusinessCategoryDO::getId, DmBusinessDomainRelDO::getBusinessCategoryId)
                .eq(DmBusinessCategoryDO::getValidFlag, true);
        List<DmDataDomainDO> dataDomainDOList = dmDataDomainMapper.selectList(lambdaWrapper);

        // Associate data domains with business categories
        for (DmDataDomainDO dmDataDomainDO : dataDomainDOList) {
            TreeData parent = dmBusinessCategoryMap.get(dmDataDomainDO.getBusinessCategoryId());
            if (parent != null) {
                List<TreeData> children = parent.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    parent.setChildren(children);
                }
                children.add(TreeData.builder()
                        .id(dmDataDomainDO.getId())
                        .parentId(dmDataDomainDO.getBusinessCategoryId())
                        .name(dmDataDomainDO.getName())
                        .type("2")
                        .otherData(JSONObject.of(
                                "code", parent.getOtherData().getString("code"),
                                "engName", dmDataDomainDO.getEngName(),
                                "num", dmDataDomainDO.getNum()))
                        .build());
            }
        }
        return treeDataList.stream()
                .filter(dataCategoryTreeRespVO -> dataCategoryTreeRespVO.getParentId() == 0)
                .collect(Collectors.toList());
    }
}
