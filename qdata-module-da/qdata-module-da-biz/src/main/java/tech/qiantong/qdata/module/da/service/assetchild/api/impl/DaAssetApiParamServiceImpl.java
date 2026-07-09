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

package tech.qiantong.qdata.module.da.service.assetchild.api.impl;

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
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiParamDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.api.DaAssetApiParamMapper;
import tech.qiantong.qdata.module.da.service.assetchild.api.IDaAssetApiParamService;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * Data Asset - External API - Parameters Service Business Layer
 *
 * @author qdata
 * @date 2025-04-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetApiParamServiceImpl  extends ServiceImpl<DaAssetApiParamMapper,DaAssetApiParamDO> implements IDaAssetApiParamService {
    @Resource
    private DaAssetApiParamMapper daAssetApiParamMapper;

    @Override
    public PageResult<DaAssetApiParamDO> getDaAssetApiParamPage(DaAssetApiParamPageReqVO pageReqVO) {
        return daAssetApiParamMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaAssetApiParam(DaAssetApiParamSaveReqVO createReqVO) {
        DaAssetApiParamDO dictType = BeanUtils.toBean(createReqVO, DaAssetApiParamDO.class);
        daAssetApiParamMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDaAssetApiParamDeep(List<DaAssetApiParamSaveReqVO> paramList, Long daAssetApiId) {
        this.removeThemeRelByAssetApiId(daAssetApiId);
        if (paramList == null || paramList.isEmpty()) {
            return;
        }
        paramList.forEach(param -> createRecursively(param, null,daAssetApiId));
    }


    /**
     * Recursively process a single parameter and its sub-parameters
     *
     * @param vo       The VO to be inserted
     * @param parentId Parent parameter ID (null for root nodes)
     */
    private void createRecursively(DaAssetApiParamSaveReqVO vo, Long parentId, Long daAssetApiId) {
        vo.setParentId(parentId);
        vo.setApiId(daAssetApiId);
        vo.setId(null);
        // Insert current node and get the generated primary key
        Long newId = createDaAssetApiParam(vo);
        // Process child nodes
        List<DaAssetApiParamSaveReqVO> children = vo.getDaAssetApiParamList();
        if (children != null && !children.isEmpty()) {
            children.forEach(child -> createRecursively(child, newId,daAssetApiId));
        }
    }

    @Override
    public int updateDaAssetApiParam(DaAssetApiParamSaveReqVO updateReqVO) {
        // Validation

        // Update Data Asset - External API - Parameters
        DaAssetApiParamDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetApiParamDO.class);
        return daAssetApiParamMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetApiParam(Collection<Long> idList) {
        // Batch delete Data Asset - External API - Parameters
        return daAssetApiParamMapper.deleteBatchIds(idList);
    }

    @Override
    public int removeThemeRelByAssetApiId(Long assetApiId) {
        daAssetApiParamMapper.removeThemeRelByAssetApiId(assetApiId);
        return 0;
    }

    @Override
    public DaAssetApiParamDO getDaAssetApiParamById(Long id) {
        return daAssetApiParamMapper.selectById(id);
    }

    @Override
    public List<DaAssetApiParamDO> getDaAssetApiParamList() {
        return daAssetApiParamMapper.selectList();
    }
    @Override
    public List<DaAssetApiParamRespVO> getDaAssetApiParamList(Long id) {
        MPJLambdaWrapper<DaAssetApiParamDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.eq(id != null, DaAssetApiParamDO::getApiId, id);
        List<DaAssetApiParamDO> daAssetApiParamDOS = daAssetApiParamMapper.selectList(lambdaWrapper);
        List<DaAssetApiParamRespVO> daAssetApiParamRespVOList = BeanUtils.toBean(daAssetApiParamDOS, DaAssetApiParamRespVO.class);
        return buildParamTree(daAssetApiParamRespVOList);
    }


    /**
     * Assemble a flat parameter list into a tree structure
     *
     * @param flatList The RespVO list queried and converted from the database
     * @return Tree-structured RespVO list (only root nodes)
     */
    public List<DaAssetApiParamRespVO> buildParamTree(List<DaAssetApiParamRespVO> flatList) {
        if (flatList == null || flatList.isEmpty()) {
            return Collections.emptyList();
        }
        // Use id -> node mapping to speed up lookup
        Map<Long, DaAssetApiParamRespVO> idMap = flatList.stream()
                .collect(Collectors.toMap(DaAssetApiParamRespVO::getId, Function.identity()));

        List<DaAssetApiParamRespVO> tree = new ArrayList<>();
        for (DaAssetApiParamRespVO node : flatList) {
            Long parentId = node.getParentId();
            if (parentId == null || parentId == 0) {
                // No parent node, treat as root
                tree.add(node);
            } else {
                DaAssetApiParamRespVO parent = idMap.get(parentId);
                if (parent != null) {
                    if (parent.getDaAssetApiParamList() == null) {
                        parent.setDaAssetApiParamList(new ArrayList<>());
                    }
                    parent.getDaAssetApiParamList().add(node);
                } else {
                    // Parent node not found, also treat as root
                    tree.add(node);
                }
            }
        }
        return tree;
    }

    @Override
    public Map<Long, DaAssetApiParamDO> getDaAssetApiParamMap() {
        List<DaAssetApiParamDO> daAssetApiParamList = daAssetApiParamMapper.selectList();
        return daAssetApiParamList.stream()
                .collect(Collectors.toMap(
                        DaAssetApiParamDO::getId,
                        daAssetApiParamDO -> daAssetApiParamDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import Data Asset - External API - Parameters data
     *
     * @param importExcelList Data Asset - External API - Parameters data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName Operating user
     * @return Result
     */
    @Override
    public String importDaAssetApiParam(List<DaAssetApiParamRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetApiParamRespVO respVO : importExcelList) {
            try {
                DaAssetApiParamDO daAssetApiParamDO = BeanUtils.toBean(respVO, DaAssetApiParamDO.class);
                Long daAssetApiParamId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetApiParamId != null) {
                        DaAssetApiParamDO existingDaAssetApiParam = daAssetApiParamMapper.selectById(daAssetApiParamId);
                        if (existingDaAssetApiParam != null) {
                            daAssetApiParamMapper.updateById(daAssetApiParamDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "数据更新成功，ID为 " + daAssetApiParamId + " 的数据资产-外部API-参数记录。", daAssetApiParamId, "数据资产-外部API-参数"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "数据更新失败，ID为 " + daAssetApiParamId + " 的数据资产-外部API-参数记录不存在。", daAssetApiParamId, "数据资产-外部API-参数"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DaAssetApiParamDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetApiParamId);
                    DaAssetApiParamDO existingDaAssetApiParam = daAssetApiParamMapper.selectOne(queryWrapper);
                    if (existingDaAssetApiParam == null) {
                        daAssetApiParamMapper.insert(daAssetApiParamDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "数据插入成功，ID为 " + daAssetApiParamId + " 的数据资产-外部API-参数记录。", daAssetApiParamId, "数据资产-外部API-参数"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "数据插入失败，ID为 " + daAssetApiParamId + " 的数据资产-外部API-参数记录已存在。", daAssetApiParamId, "数据资产-外部API-参数"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
        }
        return resultMsg.toString();
    }
}
