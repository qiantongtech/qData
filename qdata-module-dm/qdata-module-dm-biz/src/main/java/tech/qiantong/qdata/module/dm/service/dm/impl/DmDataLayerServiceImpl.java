/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
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
 * 数仓分层管理Service业务层处理
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
        // 相关校验

        // 更新数仓分层管理
        DmDataLayerDO updateObj = BeanUtils.toBean(updateReqVO, DmDataLayerDO.class);
        return dmDataLayerMapper.updateById(updateObj);
    }

    @Override
    public int removeDmDataLayer(Collection<Long> idList) {
        // 批量删除数仓分层管理
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
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数仓分层管理数据
     *
     * @param importExcelList 数仓分层管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDmDataLayer(List<DmDataLayerRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
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
                            successMessages.add("数据更新成功，ID为 " + dmDataLayerId + " 的数仓分层管理记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dmDataLayerId + " 的数仓分层管理记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DmDataLayerDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dmDataLayerId);
                    DmDataLayerDO existingDmDataLayer = dmDataLayerMapper.selectOne(queryWrapper);
                    if (existingDmDataLayer == null) {
                        dmDataLayerMapper.insert(dmDataLayerDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dmDataLayerId + " 的数仓分层管理记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dmDataLayerId + " 的数仓分层管理记录已存在。");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
            throw new ServiceException(resultMsg.toString());
        } else {
            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
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
