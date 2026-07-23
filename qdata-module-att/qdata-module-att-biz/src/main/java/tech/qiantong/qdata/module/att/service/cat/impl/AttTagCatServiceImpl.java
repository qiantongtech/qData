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
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttTagCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttTagCatDO;
import tech.qiantong.qdata.module.att.dal.mapper.cat.AttTagCatMapper;
import tech.qiantong.qdata.module.att.service.Tag.IAttTagService;
import tech.qiantong.qdata.module.att.service.cat.IAttTagCatService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tag Category Management Service business layer processing
 *
 * @author qdata
 * @date 2025-07-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttTagCatServiceImpl extends ServiceImpl<AttTagCatMapper,AttTagCatDO> implements IAttTagCatService {
    @Resource
    private AttTagCatMapper attTagCatMapper;

    @Resource
    private IAttTagService attTagService;

    @Override
    public PageResult<AttTagCatDO> getAttTagCatPage(AttTagCatPageReqVO pageReqVO) {
        return attTagCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttTagCat(AttTagCatSaveReqVO createReqVO) {
        AttTagCatDO dictType = BeanUtils.toBean(createReqVO, AttTagCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        attTagCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttTagCat(AttTagCatSaveReqVO updateReqVO) {
        AttTagCatDO catDO = attTagCatMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        // Check if the category itself is selected
        if (catDO.getId().equals(updateReqVO.getParentId())){
            throw new ServiceException("att.error.parent.self", "Cannot select self as parent category");
        }
        if (Boolean.FALSE.equals(updateReqVO.getValidFlag())) {
            Long countData = attTagService.getCountByCatCode(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("att.error.disable.tag", "Tag exists, disable not allowed");
            }
            attTagCatMapper.updateValidFlag(catDO.getCode(), updateReqVO.getValidFlag());
        } else if (Boolean.TRUE.equals(updateReqVO.getValidFlag())) {
            AttTagCatDO parent = attTagCatMapper.selectById(catDO.getParentId());
            if (parent != null && Boolean.FALSE.equals(parent.getValidFlag())) {
                throw new ServiceException("att.error.parent.disabled", "Please enable the parent category first");
            }
        }

        // Check if parent-child hierarchy changed
        boolean flag = false;
        if (!catDO.getParentId().equals(updateReqVO.getParentId()) ) {
            updateReqVO.setCode(createCode(updateReqVO.getParentId(), null));
            flag = true;
        }

        // Update Tag Category Management
        AttTagCatDO updateObj = BeanUtils.toBean(updateReqVO, AttTagCatDO.class);
        int i = attTagCatMapper.updateById(updateObj);

        attTagService.updateCatCode(catDO.getCode(),updateObj.getCode());
        // Check if parent-child hierarchy has changed
        if (flag) {
            // Update all child categories
            changeCodeByPid(updateObj.getId(), updateObj.getCode());
        }

        return i;
    }

    @Override
    public void changeCodeByPid(Long pid, String parentCode) {
        List<AttTagCatDO> list = baseMapper.selectList(Wrappers.lambdaQuery(AttTagCatDO.class)
                .eq(AttTagCatDO::getParentId, pid)
                .orderByAsc(AttTagCatDO::getCreateTime));
        if (list != null && list.size() > 0) {
            list.forEach(e -> {
                String codeOld = e.getCode();
                String codeNew = createCode(e.getParentId(), parentCode);
                e.setCode(codeNew);
                baseMapper.updateById(e);
                attTagService.updateCatCode(codeOld,codeNew);
                this.changeCodeByPid(e.getId(), e.getCode());
            });
        }
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
        LambdaQueryWrapper<AttTagCatDO> query = new LambdaQueryWrapper<AttTagCatDO>()
                .eq(AttTagCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), AttTagCatDO::getCode, parentCode)
                .isNotNull(AttTagCatDO::getCode)
                .orderByDesc(AttTagCatDO::getCode);
        List<AttTagCatDO> list = attTagCatMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                // Case 1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                // Case 2
                AttTagCatDO parent = attTagCatMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            // Case 3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }

    @Override
    public Integer removeAttTagCat(Long id) {
        int count = 0;
        AttTagCatDO cat = attTagCatMapper.selectById(id);
        // Check if data exists
        if (attTagService.getCountByCatCode(cat.getCode()) > 0) {
            throw new ServiceException("att.error.delete.tag", "Tag exists, deletion not allowed");
        }
        if (cat != null) {
            count += attTagCatMapper.delete(Wrappers.lambdaQuery(AttTagCatDO.class)
                    .likeRight(AttTagCatDO::getCode, cat.getCode()));
        }
        return count;
    }

//    @Override
//    public int removeAttTagCat(Collection<Long> idList) {
//        // Batch delete Tag Category Management
//        return attTagCatMapper.deleteBatchIds(idList);
//    }

    @Override
    public AttTagCatDO getAttTagCatById(Long id) {
        return attTagCatMapper.selectById(id);
    }

    @Override
    public List<AttTagCatDO> getAttTagCatList() {
        return attTagCatMapper.selectList();
    }

    @Override
    public Map<Long, AttTagCatDO> getAttTagCatMap() {
        List<AttTagCatDO> attTagCatList = attTagCatMapper.selectList();
        return attTagCatList.stream()
                .collect(Collectors.toMap(
                        AttTagCatDO::getId,
                        attTagCatDO -> attTagCatDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import Tag Category Management data
         *
         * @param importExcelList Tag Category Management data list
         * @param isUpdateSupport Whether to support update; if already exists, update the data
         * @param operName Operator
         * @return Import result
         */
        @Override
        public String importAttTagCat(List<AttTagCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (AttTagCatRespVO respVO : importExcelList) {
                try {
                    AttTagCatDO attTagCatDO = BeanUtils.toBean(respVO, AttTagCatDO.class);
                    Long attTagCatId = respVO.getId();
                    if (isUpdateSupport) {
                        if (attTagCatId != null) {
                            AttTagCatDO existingAttTagCat = attTagCatMapper.selectById(attTagCatId);
                            if (existingAttTagCat != null) {
                                attTagCatMapper.updateById(attTagCatDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                        "Data update successful, ID {0} {1} record.", attTagCatId, MessageUtils.messageWithFallback("att.entity.tag.category", "Tag category")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", attTagCatId, MessageUtils.messageWithFallback("att.entity.tag.category", "Tag category")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<AttTagCatDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", attTagCatId);
                        AttTagCatDO existingAttTagCat = attTagCatMapper.selectOne(queryWrapper);
                        if (existingAttTagCat == null) {
                            attTagCatMapper.insert(attTagCatDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", attTagCatId, MessageUtils.messageWithFallback("att.entity.tag.category", "Tag category")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", attTagCatId, MessageUtils.messageWithFallback("att.entity.tag.category", "Tag category")));
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
    public List<AttTagCatDO> getAttTagCatLIst(AttTagCatPageReqVO reqVO) {
        LambdaQueryWrapperX<AttTagCatDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.likeIfPresent(AttTagCatDO::getName, reqVO.getName())
                .eqIfPresent(AttTagCatDO::getParentId, reqVO.getParentId())
                .eqIfPresent(AttTagCatDO::getValidFlag, reqVO.getValidFlag())
                .eqIfPresent(AttTagCatDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(AttTagCatDO::getDescription, reqVO.getDescription())
                .likeRightIfPresent(AttTagCatDO::getCode, reqVO.getCode())
                .eqIfPresent(AttTagCatDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(AttTagCatDO::getSortOrder);
        return attTagCatMapper.selectList(queryWrapperX);
    }
}
