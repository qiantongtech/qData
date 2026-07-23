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

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.domain.TreeData;
import tech.qiantong.qdata.common.core.domain.entity.SysDictData;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dm.api.service.dataLayer.IDmDataLayerApiService;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerPageReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerRespVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerSaveReqVO;
import tech.qiantong.qdata.module.dm.controller.admin.dm.vo.DmDataLayerTreeRespVO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmDataLayerDO;
import tech.qiantong.qdata.module.dm.dal.dataobject.dm.DmThemeDomainDO;
import tech.qiantong.qdata.module.dm.dal.mapper.dm.DmDataLayerMapper;
import tech.qiantong.qdata.module.dm.service.dm.IDmDataLayerService;
import tech.qiantong.qdata.module.system.service.ISysDictDataService;
import tech.qiantong.qdata.mybatis.core.query.MPJLambdaWrapperX;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data Warehouse Layer Service - Business Layer Processing
 *
 * @author FXB
 * @date 2026-03-24
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DmDataLayerServiceImpl extends ServiceImpl<DmDataLayerMapper, DmDataLayerDO> implements IDmDataLayerService, IDmDataLayerApiService {
    @Resource
    private DmDataLayerMapper dmDataLayerMapper;

    @Resource
    private ISysDictDataService sysDictDataService;

    @Override
    public PageResult<DmDataLayerDO> getDmDataLayerPage(DmDataLayerPageReqVO pageReqVO) {
        return dmDataLayerMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDmDataLayer(DmDataLayerSaveReqVO createReqVO) {
        DmDataLayerDO dictType = BeanUtils.toBean(createReqVO, DmDataLayerDO.class);
        dmDataLayerMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDmDataLayer(DmDataLayerSaveReqVO updateReqVO) {
        // Related validation

        // Update data warehouse layer
        DmDataLayerDO updateObj = BeanUtils.toBean(updateReqVO, DmDataLayerDO.class);
        return dmDataLayerMapper.updateById(updateObj);
    }

    @Override
    public int removeDmDataLayer(Collection<Long> idList) {
        // Batch delete data warehouse layers
        return dmDataLayerMapper.deleteBatchIds(idList);
    }

    @Override
    public DmDataLayerDO getDmDataLayerById(Long id) {
        return dmDataLayerMapper.selectById(id);
    }

    @Override
    public List<DmDataLayerDO> getDmDataLayerList() {
        return dmDataLayerMapper.selectList();
    }

    @Override
    public Map<Long, DmDataLayerDO> getDmDataLayerMap() {
        List<DmDataLayerDO> dmDataLayerList = dmDataLayerMapper.selectList();
        return dmDataLayerList.stream()
                .collect(Collectors.toMap(
                        DmDataLayerDO::getId,
                        dmDataLayerDO -> dmDataLayerDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data warehouse layer data
     *
     * @param importExcelList Data warehouse layer data list
     * @param isUpdateSupport Whether to support update, if exists, update the data
     * @param operName        Operation user
     * @return Result
     */
    @Override
    public String importDmDataLayer(List<DmDataLayerRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dm.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DmDataLayerRespVO respVO : importExcelList) {
            try {
                DmDataLayerDO dmDataLayerDO = BeanUtils.toBean(respVO, DmDataLayerDO.class);
                Long dmDataLayerId = respVO.getId();
                if (isUpdateSupport) {
                    if (dmDataLayerId != null) {
                        DmDataLayerDO existingDmDataLayer = dmDataLayerMapper.selectById(dmDataLayerId);
                        if (existingDmDataLayer != null) {
                            dmDataLayerMapper.updateById(dmDataLayerDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dm.import.update.success",
                                    "Data update successful, data warehouse layer record with ID " + dmDataLayerId + ".", dmDataLayerId, "DataWarehouseLayer"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.fail",
                                    "Data update failed, data warehouse layer record with ID " + dmDataLayerId + " does not exist.", dmDataLayerId, "DataWarehouseLayer"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dm.import.update.id.missing",
                                "Data update failed, a record has no ID."));
                    }
                } else {
                    QueryWrapper<DmDataLayerDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dmDataLayerId);
                    DmDataLayerDO existingDmDataLayer = dmDataLayerMapper.selectOne(queryWrapper);
                    if (existingDmDataLayer == null) {
                        dmDataLayerMapper.insert(dmDataLayerDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dm.import.insert.success",
                                "Data insert successful, data warehouse layer record with ID " + dmDataLayerId + ".", dmDataLayerId, "DataWarehouseLayer"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dm.import.insert.fail",
                                "Data insert failed, data warehouse layer record with ID " + dmDataLayerId + " already exists.", dmDataLayerId, "DataWarehouseLayer"));
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
    public List<DmDataLayerTreeRespVO> tree() {
        List<DmDataLayerDO> list = this.list();

        List<SysDictData> sysDictDataList = sysDictDataService.selectDictDataList(SysDictData.builder()
                .dictType("dm_data_layer_category")
                .status("0")
                .build());

        sysDictDataList.stream().sorted(Comparator.comparingLong(SysDictData::getDictSort));

        List<DmDataLayerTreeRespVO> tree = sysDictDataList.stream()
                .map(sysDictData -> DmDataLayerTreeRespVO.builder()
                        .id(Long.parseLong(sysDictData.getDictValue()))
                        .parentId(0L)
                        .name(sysDictData.getDictLabel())
                        .build())
                .collect(Collectors.toList());

        list.forEach(dmDataLayerDO -> {
            DmDataLayerTreeRespVO dmDataLayerRespVO = tree.stream()
                    .filter(item -> String.valueOf(item.getId()).equals(dmDataLayerDO.getCategory()))
                    .findFirst()
                    .orElse(null);
            if (dmDataLayerRespVO != null) {
                List<DmDataLayerTreeRespVO> childrenList = dmDataLayerRespVO.getChildren();
                if (childrenList == null) {
                    childrenList = new ArrayList();
                    dmDataLayerRespVO.setChildren(childrenList);
                }
                DmDataLayerTreeRespVO children = BeanUtils.toBean(dmDataLayerDO, DmDataLayerTreeRespVO.class);
                children.setParentId(Long.parseLong(children.getCategory()));
                childrenList.add(children);
            }
        });
        return tree;
    }

    @Override
    public List<TreeData> getTreeData(String type) {
        MPJLambdaWrapperX<DmDataLayerDO> lambdaWrapper = new MPJLambdaWrapperX<>();
        lambdaWrapper
                .selectAll(DmDataLayerDO.class)
                .eq(DmThemeDomainDO::getValidFlag, true);
        String statisticsSql = null;
        if (StringUtils.isNotBlank(type)) {
            switch (type) {
                case "1":
                    statisticsSql = "(SELECT COUNT(1) FROM DA_ASSET a WHERE t.ID = a.DATA_LAYER_ID) AS num";
                    break;
            }
            if (StringUtils.isNotBlank(statisticsSql)) {
                lambdaWrapper.select(statisticsSql);
            }
        }
        List<DmDataLayerDO> list = this.list(lambdaWrapper);

        List<SysDictData> sysDictDataList = sysDictDataService.selectDictDataList(SysDictData.builder()
                .dictType("dm_data_layer_category")
                .status("0")
                .build());

        sysDictDataList.stream().sorted(Comparator.comparingLong(SysDictData::getDictSort));

        List<TreeData> tree = sysDictDataList.stream()
                .map(sysDictData -> TreeData.builder()
                        .id(Long.parseLong(sysDictData.getDictValue()))
                        .name(sysDictData.getDictLabel())
                        .type("4")
                        .build())
                .collect(Collectors.toList());

        list.forEach(dmDataLayerDO -> {
            TreeData treeData = tree.stream()
                    .filter(item -> String.valueOf(item.getId()).equals(dmDataLayerDO.getCategory()))
                    .findFirst()
                    .orElse(null);
            if (treeData != null) {
                List<TreeData> childrenList = treeData.getChildren();
                if (childrenList == null) {
                    childrenList = new ArrayList();
                    treeData.setChildren(childrenList);
                }
                childrenList.add(TreeData.builder()
                        .id(dmDataLayerDO.getId())
                        .name(dmDataLayerDO.getName())
                        .type("5")
                        .otherData(JSONObject.of(
                                "engName", dmDataLayerDO.getEngName(),
                                "num", dmDataLayerDO.getNum()))
                        .build());
            }
        });
        return tree;
    }
}
