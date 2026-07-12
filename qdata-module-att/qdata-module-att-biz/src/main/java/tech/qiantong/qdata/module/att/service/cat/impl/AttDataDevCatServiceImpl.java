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

    @Override
    public PageResult<AttDataDevCatDO> getAttDataDevCatPage(AttDataDevCatPageReqVO pageReqVO) {
        return attDataDevCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttDataDevCat(AttDataDevCatSaveReqVO createReqVO) {
        AttDataDevCatDO dictType = BeanUtils.toBean(createReqVO, AttDataDevCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attDataDevCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttDataDevCat(AttDataDevCatSaveReqVO updateReqVO) {
        // Validation

        // Update Data Development Category Management
        AttDataDevCatDO updateObj = BeanUtils.toBean(updateReqVO, AttDataDevCatDO.class);
        return attDataDevCatMapper.updateById(updateObj);
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
        // Batch delete Data Development Category Management
        return attDataDevCatMapper.deleteBatchIds(idList);
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
            throw new ServiceException("att.error.import.empty", "导入数据不能为空！");
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
                                    "数据Update 成功，ID为 " + attDataDevCatId + " 的数据开发类目管理记录。", attDataDevCatId, "数据开发类目管理"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                    "数据Update 失败，ID为 " + attDataDevCatId + " 的数据开发类目管理记录不存在。", attDataDevCatId, "数据开发类目管理"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                "数据Update 失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<AttDataDevCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attDataDevCatId);
                    AttDataDevCatDO existingAttDataDevCat = attDataDevCatMapper.selectOne(queryWrapper);
                    if (existingAttDataDevCat == null) {
                        attDataDevCatMapper.insert(attDataDevCatDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                "数据插入成功，ID为 " + attDataDevCatId + " 的数据开发类目管理记录。", attDataDevCatId, "数据开发类目管理"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                "数据插入失败，ID为 " + attDataDevCatId + " 的数据开发类目管理记录已存在。", attDataDevCatId, "数据开发类目管理"));
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
}
