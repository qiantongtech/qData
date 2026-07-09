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

package tech.qiantong.qdata.module.da.service.assetchild.theme.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.theme.DaAssetThemeRelDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.theme.DaAssetThemeRelMapper;
import tech.qiantong.qdata.module.da.service.assetchild.theme.IDaAssetThemeRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data asset-theme relationship Service business layer processing
 *
 * @author qdata
 * @date 2025-04-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetThemeRelServiceImpl extends ServiceImpl<DaAssetThemeRelMapper, DaAssetThemeRelDO> implements IDaAssetThemeRelService {
    @Resource
    private DaAssetThemeRelMapper daAssetThemeRelMapper;

    @Override
    public PageResult<DaAssetThemeRelDO> getDaAssetThemeRelPage(DaAssetThemeRelPageReqVO pageReqVO) {
        return daAssetThemeRelMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DaAssetThemeRelRespVO> getDaAssetThemeRelList(DaAssetThemeRelPageReqVO pageReqVO) {
        MPJLambdaWrapper<DaAssetThemeRelDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DaAssetThemeRelDO.class)
                .select("t2.NAME as themeName")
                .leftJoin("ATT_THEME t2 on t.THEME_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .likeIfExists("t2.NAME", pageReqVO.getThemeName())
                .eq("t2.DEL_FLAG","0")
                .eq(pageReqVO.getAssetId() != null, DaAssetThemeRelDO::getAssetId, pageReqVO.getAssetId())
                .in(CollectionUtils.isNotEmpty(pageReqVO.getThemeIdList()), DaAssetThemeRelDO::getThemeId, pageReqVO.getThemeIdList());
        List<DaAssetThemeRelDO> daAssetThemeRelDOList = daAssetThemeRelMapper.selectJoinList(DaAssetThemeRelDO.class, lambdaWrapper);
        return BeanUtils.toBean(daAssetThemeRelDOList, DaAssetThemeRelRespVO.class);
    }

    @Override
    public List<Long> getDaAssetIdList(List<Long> themeIdList) {
        return daAssetThemeRelMapper.getDaAssetIdList(themeIdList);
    }

    @Override
    public Long createDaAssetThemeRel(DaAssetThemeRelSaveReqVO createReqVO) {
        DaAssetThemeRelDO dictType = BeanUtils.toBean(createReqVO, DaAssetThemeRelDO.class);
        daAssetThemeRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public void createDaAssetThemeRelList(List<String> themeIdList, Long assetId) {
        this.removeThemeRelByAssetId(assetId);
        for (String themeId : themeIdList) {
            DaAssetThemeRelSaveReqVO createReqVO = new DaAssetThemeRelSaveReqVO();
            createReqVO.setThemeId(JSONUtils.convertToLong(themeId));
            createReqVO.setAssetId(assetId);
            this.createDaAssetThemeRel(createReqVO);
        }
    }

    @Override
    public int updateDaAssetThemeRel(DaAssetThemeRelSaveReqVO updateReqVO) {
        // Validation checks

        // Update data asset-theme relationship
        DaAssetThemeRelDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetThemeRelDO.class);
        return daAssetThemeRelMapper.updateById(updateObj);
    }

    @Override
    public int removeDaAssetThemeRel(Collection<Long> idList) {
        // Batch delete data asset-theme relationship
        return daAssetThemeRelMapper.deleteBatchIds(idList);
    }

    @Override
    public int removeThemeRelByAssetId(Long assetId) {
        daAssetThemeRelMapper.deleteDaAssetThemeRelByAssetId(assetId);
        return 1;
    }

    @Override
    public DaAssetThemeRelDO getDaAssetThemeRelById(Long id) {
        return daAssetThemeRelMapper.selectById(id);
    }

    @Override
    public List<DaAssetThemeRelDO> getDaAssetThemeRelList() {
        return daAssetThemeRelMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetThemeRelDO> getDaAssetThemeRelMap() {
        List<DaAssetThemeRelDO> daAssetThemeRelList = daAssetThemeRelMapper.selectList();
        return daAssetThemeRelList.stream()
                .collect(Collectors.toMap(
                        DaAssetThemeRelDO::getId,
                        daAssetThemeRelDO -> daAssetThemeRelDO,
                        // Retain existing values
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data asset-theme relationship data
     *
     * @param importExcelList Data asset-theme relationship data list
     * @param isUpdateSupport Whether to support update, if already exists, update the data
     * @param operName        Operator user
     * @return Result
     */
    @Override
    public String importDaAssetThemeRel(List<DaAssetThemeRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetThemeRelRespVO respVO : importExcelList) {
            try {
                DaAssetThemeRelDO daAssetThemeRelDO = BeanUtils.toBean(respVO, DaAssetThemeRelDO.class);
                Long daAssetThemeRelId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetThemeRelId != null) {
                        DaAssetThemeRelDO existingDaAssetThemeRel = daAssetThemeRelMapper.selectById(daAssetThemeRelId);
                        if (existingDaAssetThemeRel != null) {
                            daAssetThemeRelMapper.updateById(daAssetThemeRelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "数据更新成功，ID为 " + daAssetThemeRelId + " 的数据资产-主题关联关系记录。", daAssetThemeRelId, "数据资产-主题关联关系"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "数据更新失败，ID为 " + daAssetThemeRelId + " 的数据资产-主题关联关系记录不存在。", daAssetThemeRelId, "数据资产-主题关联关系"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DaAssetThemeRelDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetThemeRelId);
                    DaAssetThemeRelDO existingDaAssetThemeRel = daAssetThemeRelMapper.selectOne(queryWrapper);
                    if (existingDaAssetThemeRel == null) {
                        daAssetThemeRelMapper.insert(daAssetThemeRelDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "数据插入成功，ID为 " + daAssetThemeRelId + " 的数据资产-主题关联关系记录。", daAssetThemeRelId, "数据资产-主题关联关系"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "数据插入失败，ID为 " + daAssetThemeRelId + " 的数据资产-主题关联关系记录已存在。", daAssetThemeRelId, "数据资产-主题关联关系"));
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
