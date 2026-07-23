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

package tech.qiantong.qdata.module.dm.service.dm.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import tech.qiantong.qdata.common.core.domain.TreeData;
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
import tech.qiantong.qdata.module.dm.api.service.themeDomain.IDmThemeDomainApiService;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmThemeDomainSaveReqVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataDomainDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmThemeDomainDO;
import tech.qiantong.qdata.module.dm.dal.mapper.dm.DmThemeDomainMapper;
import tech.qiantong.qdata.module.dm.service.dm.IDmThemeDomainService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

/**
 * Theme Domain Service - Business Layer Processing
 *
 * @author FXB
 * @date 2026-03-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmThemeDomainServiceImpl extends ServiceImpl<DmThemeDomainMapper, DmThemeDomainDO> implements IDmThemeDomainService, IDmThemeDomainApiService {
    @Resource
    private DmThemeDomainMapper dmThemeDomainMapper;

    @Override
    public PageResult<DmThemeDomainDO> getDmThemeDomainPage(DmThemeDomainPageReqVO pageReqVO) {
        return dmThemeDomainMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDmThemeDomain(DmThemeDomainSaveReqVO createReqVO) {
        DmThemeDomainDO dictType = BeanUtils.toBean(createReqVO, DmThemeDomainDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        dmThemeDomainMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDmThemeDomain(DmThemeDomainSaveReqVO updateReqVO) {
        DmThemeDomainDO catDO = baseMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        // Check if the parent is set to itself
        if (catDO.getId().equals(updateReqVO.getParentId())) {
            throw new ServiceException("dm.error.parent.self", "Cannot select itself as the parent category");
        }
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            baseMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            DmThemeDomainDO parent = baseMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("dm.error.parent.disabled", "Parent must be enabled first");
            }
        }
        // Check if the parent relationship has changed
        boolean flag = false;
        if (!catDO.getParentId().equals(updateReqVO.getParentId())) {
            updateReqVO.setCode(createCode(updateReqVO.getParentId(), null));
            flag = true;
        }

        // Update theme domain
        DmThemeDomainDO updateObj = BeanUtils.toBean(updateReqVO, DmThemeDomainDO.class);
        int i = baseMapper.updateById(updateObj);

        // If the parent relationship has changed
        if (flag) {
            // Update all child codes
            changeCodeByPid(updateObj.getId(), updateObj.getCode());
        }
        return i;
    }

    @Override
    public int removeDmThemeDomain(Collection<Long> idList) {
        // Batch delete theme domains
        return dmThemeDomainMapper.deleteBatchIds(idList);
    }

    @Override
    public DmThemeDomainDO getDmThemeDomainById(Long id) {
        MPJLambdaWrapperX<DmThemeDomainDO> lambdaWrapper = new MPJLambdaWrapperX<>();

        lambdaWrapper.selectAll(DmThemeDomainDO.class)
                .select("u.NICK_NAME AS ownerUserName","u.PHONENUMBER AS ownerUserPhoneNumber")
                .leftJoin("SYSTEM_USER u on t.OWNER_USER_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .eq(DmThemeDomainDO::getId, id);
        return dmThemeDomainMapper.selectOne(lambdaWrapper);
    }

    @Override
    public List<DmThemeDomainDO> getDmThemeDomainList() {
        return dmThemeDomainMapper.selectList();
    }

    @Override
    public Map<Long, DmThemeDomainDO> getDmThemeDomainMap() {
        List<DmThemeDomainDO> dmThemeDomainList = dmThemeDomainMapper.selectList();
        return dmThemeDomainList.stream()
                .collect(Collectors.toMap(
                        DmThemeDomainDO::getId,
                        dmThemeDomainDO -> dmThemeDomainDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import theme domain data
     *
     * @param importExcelList Theme domain data list
     * @param isUpdateSupport Whether to support update, if exists, update the data
     * @param operName        Operation user
     * @return Result
     */
    @Override
    public String importDmThemeDomain(List<DmThemeDomainRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dm.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DmThemeDomainRespVO respVO : importExcelList) {
            try {
                DmThemeDomainDO dmThemeDomainDO = BeanUtils.toBean(respVO, DmThemeDomainDO.class);
                Long dmThemeDomainId = respVO.getId();
                if (isUpdateSupport) {
                    if (dmThemeDomainId != null) {
                        DmThemeDomainDO existingDmThemeDomain = dmThemeDomainMapper.selectById(dmThemeDomainId);
                        if (existingDmThemeDomain != null) {
                            dmThemeDomainMapper.updateById(dmThemeDomainDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dm.import.update.success",
                                    "Data update successful, theme domain record with ID " + dmThemeDomainId + ".", dmThemeDomainId, "ThemeDomain"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.fail",
                                    "Data update failed, theme domain record with ID " + dmThemeDomainId + " does not exist.", dmThemeDomainId, "ThemeDomain"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.id.missing",
                                "Data update failed, a record has no ID."));
                    }
                } else {
                    QueryWrapper<DmThemeDomainDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dmThemeDomainId);
                    DmThemeDomainDO existingDmThemeDomain = dmThemeDomainMapper.selectOne(queryWrapper);
                    if (existingDmThemeDomain == null) {
                        dmThemeDomainMapper.insert(dmThemeDomainDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dm.import.insert.success",
                                "Data insert successful, theme domain record with ID " + dmThemeDomainId + ".", dmThemeDomainId, "ThemeDomain"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dm.import.insert.fail",
                                "Data insert failed, theme domain record with ID " + dmThemeDomainId + " already exists.", dmThemeDomainId, "ThemeDomain"));
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
                    "Import failed! " + failureNum + " records have incorrect format, errors below:<br/>" + failureDetails, failureNum, failureDetails));
            throw new ServiceException("dm.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("dm.import.result.success",
                    "All data imported successfully! Total " + successNum + " records.", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public List<DmThemeDomainDO> getDmThemeDomainList(DmThemeDomainPageReqVO reqVO) {
        MPJLambdaWrapperX<DmThemeDomainDO> lambdaWrapper = new MPJLambdaWrapperX<>();

        lambdaWrapper.selectAll(DmThemeDomainDO.class)
                .select("u.NICK_NAME AS ownerUserName", "u.PHONENUMBER AS ownerUserPhoneNumber", "layer.name AS dataLayerName")
                .leftJoin("SYSTEM_USER u on t.OWNER_USER_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .leftJoin(DmDataLayerDO.class, "layer", DmDataLayerDO::getId, DmThemeDomainDO::getDataLayerId);

        lambdaWrapper.eqIfPresent(DmThemeDomainDO::getCode, reqVO.getCode())
                .likeIfPresent(DmThemeDomainDO::getName, reqVO.getName())
                .likeIfPresent(DmThemeDomainDO::getEngName, reqVO.getEngName())
                .eqIfPresent(DmThemeDomainDO::getParentId, reqVO.getParentId())
                .eqIfPresent(DmThemeDomainDO::getOwnerUserId, reqVO.getOwnerUserId())
                .eqIfPresent(DmThemeDomainDO::getDataLayerId, reqVO.getDataLayerId())
                .likeIfPresent(DmThemeDomainDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DmThemeDomainDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add exact matching condition for name (name = '<name>')
                // .likeIfPresent(DmThemeDomainDO::getName, reqVO.getName())
                // Sort by createTime in descending order
                .orderByDesc(DmThemeDomainDO::getCreateTime);
        return dmThemeDomainMapper.selectList(lambdaWrapper);
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
        LambdaQueryWrapper<DmThemeDomainDO> query = new LambdaQueryWrapper<DmThemeDomainDO>()
                .eq(DmThemeDomainDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), DmThemeDomainDO::getCode, parentCode)
                .isNotNull(DmThemeDomainDO::getCode)
                .orderByDesc(DmThemeDomainDO::getCode);
        List<DmThemeDomainDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                // Scenario 1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                // Scenario 2
                DmThemeDomainDO parent = baseMapper.selectById(parentId);
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
        List<DmThemeDomainDO> list = baseMapper.selectList(Wrappers.lambdaQuery(DmThemeDomainDO.class)
                .eq(DmThemeDomainDO::getParentId, pid)
                .orderByAsc(DmThemeDomainDO::getCreateTime));
        if (list != null && list.size() > 0) {
            list.forEach(e -> {
                String codeNew = createCode(e.getParentId(), parentCode);
                e.setCode(codeNew);
                baseMapper.updateById(e);
                this.changeCodeByPid(e.getId(), e.getCode());
            });
        }
    }

    @Override
    public List<TreeData> getTreeData(String type) {
        // Get all enabled data
        MPJLambdaWrapperX<DmThemeDomainDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        lambdaWrapper.selectAll(DmThemeDomainDO.class)
                .eq(DmThemeDomainDO::getValidFlag, true);
        String statisticsSql = null;
        if (StringUtils.isNotBlank(type)) {
            switch (type) {
                case "1":
                    statisticsSql = "(SELECT COUNT(1) FROM DA_ASSET a WHERE t.ID = a.THEME_DOMAIN_ID) AS num";
                    break;
            }
            if (StringUtils.isNotBlank(statisticsSql)) {
                lambdaWrapper.select(statisticsSql);
            }
        }
        List<DmThemeDomainDO> list = baseMapper.selectList(lambdaWrapper);

        // Build business category tree
        Map<Long, TreeData> treeDataMap = list.stream()
                .collect(Collectors.toMap(k -> k.getId(), v -> TreeData.builder()
                        .id(v.getId())
                        .parentId(v.getParentId())
                        .name(v.getName())
                        .type("3")
                        .otherData(JSONObject.of(
                                "code", v.getCode(),
                                "engName", v.getEngName(),
                                "num", v.getNum()))
                        .build()));

        for (TreeData treeData : treeDataMap.values()) {
            TreeData parent = treeDataMap.get(treeData.getParentId());
            if (parent != null) {
                List<TreeData> children = parent.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    parent.setChildren(children);
                }
                children.add(treeData);
            }
        }
        return treeDataMap.values()
                .stream()
                .filter(treeData -> treeData.getParentId() == 0)
                .collect(Collectors.toList());
    }
}
