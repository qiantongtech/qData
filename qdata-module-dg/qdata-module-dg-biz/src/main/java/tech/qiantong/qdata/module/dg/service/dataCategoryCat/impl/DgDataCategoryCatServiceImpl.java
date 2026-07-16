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

package tech.qiantong.qdata.module.dg.service.dataCategoryCat.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

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
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataCategoryCat.vo.DgDataCategoryCatSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataCategoryCat.DgDataCategoryCatDO;
import tech.qiantong.qdata.module.dg.dal.mapper.dataCategoryCat.DgDataCategoryCatMapper;
import tech.qiantong.qdata.module.dg.service.dataCategory.IDgDataCategoryService;
import tech.qiantong.qdata.module.dg.service.dataCategoryCat.IDgDataCategoryCatService;

/**
 * Data Category - Category Service Business Layer Processing
 *
 * @author FXB
 * @date 2026-04-07
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDataCategoryCatServiceImpl extends ServiceImpl<DgDataCategoryCatMapper, DgDataCategoryCatDO> implements IDgDataCategoryCatService {
    @Resource
    private DgDataCategoryCatMapper dgDataCategoryCatMapper;

    @Resource
    private IDgDataCategoryService dgDataCategoryService;

    @Override
    public PageResult<DgDataCategoryCatDO> getDgDataCategoryCatPage(DgDataCategoryCatPageReqVO pageReqVO) {
        return dgDataCategoryCatMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgDataCategoryCat(DgDataCategoryCatSaveReqVO createReqVO) {
        DgDataCategoryCatDO dictType = BeanUtils.toBean(createReqVO, DgDataCategoryCatDO.class);
        dictType.setCode(createCode(createReqVO.getParentId(), null));
        dgDataCategoryCatMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDataCategoryCat(DgDataCategoryCatSaveReqVO updateReqVO) {
        DgDataCategoryCatDO catDO = baseMapper.selectById(updateReqVO.getId());
        if (catDO == null) {
            return 0;
        }
        //Check if it selected itself
        if (catDO.getId().equals(updateReqVO.getParentId())) {
            throw new ServiceException("dg.error.parent.self", "切换上级不能选择自身作为上级类目");
        }
        //Check parent-child relationship change
        boolean flag = false;
        if (!catDO.getParentId().equals(updateReqVO.getParentId())) {
            updateReqVO.setCode(createCode(updateReqVO.getParentId(), null));
            flag = true;
        }

        // Update data service category management
        DgDataCategoryCatDO updateObj = BeanUtils.toBean(updateReqVO, DgDataCategoryCatDO.class);
        int i = dgDataCategoryCatMapper.updateById(updateObj);

        dgDataCategoryService.updateCatCode(catDO.getCode(), updateObj.getCode());
        //Check if parent-child relationship has changed
        if (flag) {
            //Update all children
            changeCodeByPid(updateObj.getId(), updateObj.getCode());
        }
        return i;
    }

    @Override
    public int removeDgDataCategoryCat(Collection<Long> idList) {
        List<DgDataCategoryCatDO> attApiCatDOS = baseMapper.selectBatchIds(idList);
        for (DgDataCategoryCatDO catDO : attApiCatDOS) {
            Long countData = dgDataCategoryService.getCountByCatCode(catDO.getCode());
            if (countData > 0) {
                throw new ServiceException("dg.error.delete.category", "存在分类，不允许删除");
            }
        }
        // Batch delete data category-category
        return dgDataCategoryCatMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDataCategoryCatDO getDgDataCategoryCatById(Long id) {
        return dgDataCategoryCatMapper.selectById(id);
    }

    @Override
    public List<DgDataCategoryCatDO> getDgDataCategoryCatList() {
        return dgDataCategoryCatMapper.selectList();
    }

    @Override
    public Map<Long, DgDataCategoryCatDO> getDgDataCategoryCatMap() {
        List<DgDataCategoryCatDO> dgDataCategoryCatList = dgDataCategoryCatMapper.selectList();
        return dgDataCategoryCatList.stream()
                .collect(Collectors.toMap(
                        DgDataCategoryCatDO::getId,
                        dgDataCategoryCatDO -> dgDataCategoryCatDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data category-category data
     *
     * @param importExcelList Data category-category data list
     * @param isUpdateSupport Whether to update support, if exists then update data
     * @param operName        Operator user
     * @return Result
     */
    @Override
    public String importDgDataCategoryCat(List<DgDataCategoryCatRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dg.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DgDataCategoryCatRespVO respVO : importExcelList) {
            try {
                DgDataCategoryCatDO dgDataCategoryCatDO = BeanUtils.toBean(respVO, DgDataCategoryCatDO.class);
                Long dgDataCategoryCatId = respVO.getId();
                if (isUpdateSupport) {
                    if (dgDataCategoryCatId != null) {
                        DgDataCategoryCatDO existingDgDataCategoryCat = dgDataCategoryCatMapper.selectById(dgDataCategoryCatId);
                        if (existingDgDataCategoryCat != null) {
                            dgDataCategoryCatMapper.updateById(dgDataCategoryCatDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dg.import.update.success",
                                    "数据更新成功，ID为 " + dgDataCategoryCatId + " 的数据分类-类目记录。", dgDataCategoryCatId, "数据分类-类目"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.fail",
                                    "数据更新失败，ID为 " + dgDataCategoryCatId + " 的数据分类-类目记录不存在。", dgDataCategoryCatId, "数据分类-类目"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DgDataCategoryCatDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dgDataCategoryCatId);
                    DgDataCategoryCatDO existingDgDataCategoryCat = dgDataCategoryCatMapper.selectOne(queryWrapper);
                    if (existingDgDataCategoryCat == null) {
                        dgDataCategoryCatMapper.insert(dgDataCategoryCatDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dg.import.insert.success",
                                "数据插入成功，ID为 " + dgDataCategoryCatId + " 的数据分类-类目记录。", dgDataCategoryCatId, "数据分类-类目"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dg.import.insert.fail",
                                "数据插入失败，ID为 " + dgDataCategoryCatId + " 的数据分类-类目记录已存在。", dgDataCategoryCatId, "数据分类-类目"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("dg.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("dg.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public String createCode(Long parentId, String parentCode) {
        String categoryCode = null;
        /*
         * Divided into three cases
         * 1. No data in database, call YouBianCodeUtil.getNextYouBianCode(null);
         * 2. Add child node without sibling, YouBianCodeUtil.getSubYouBianCode(parentCode,null);
         * 3. Add child node with sibling, YouBianCodeUtil.getNextYouBianCode(lastCode);
         * */
        //Find the largest code value among siblings
        LambdaQueryWrapper<DgDataCategoryCatDO> query = new LambdaQueryWrapper<DgDataCategoryCatDO>()
                .eq(DgDataCategoryCatDO::getParentId, parentId)
                .likeRight(StringUtils.isNotBlank(parentCode), DgDataCategoryCatDO::getCode, parentCode)
                .isNotNull(DgDataCategoryCatDO::getCode)
                .orderByDesc(DgDataCategoryCatDO::getCode);
        List<DgDataCategoryCatDO> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (parentId == 0) {
                //Case 1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //Case 2
                DgDataCategoryCatDO parent = baseMapper.selectById(parentId);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //Case 3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }

    @Override
    public void changeCodeByPid(Long pid, String parentCode) {
        List<DgDataCategoryCatDO> list = baseMapper.selectList(Wrappers.lambdaQuery(DgDataCategoryCatDO.class)
                .eq(DgDataCategoryCatDO::getParentId, pid)
                .orderByAsc(DgDataCategoryCatDO::getCreateTime));
        if (list != null && list.size() > 0) {
            list.forEach(e -> {
                String codeOld = e.getCode();
                String codeNew = createCode(e.getParentId(), parentCode);
                e.setCode(codeNew);
                baseMapper.updateById(e);
                dgDataCategoryService.updateCatCode(codeOld, codeNew);
                this.changeCodeByPid(e.getId(), e.getCode());
            });
        }
    }
}
