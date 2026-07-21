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

package tech.qiantong.qdata.module.da.service.daAssetApply.impl;

import cn.hutool.core.date.DateUtil;
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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyRespVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplySaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.daAssetApply.DaAssetApplyDO;
import tech.qiantong.qdata.module.da.dal.mapper.daAssetApply.DaAssetApplyMapper;
import tech.qiantong.qdata.module.da.service.assetchild.theme.IDaAssetThemeRelService;
import tech.qiantong.qdata.module.da.service.daAssetApply.IDaAssetApplyService;
import tech.qiantong.qdata.module.system.api.message.dto.MessageSaveReqDTO;
import tech.qiantong.qdata.module.system.service.ISysMessageService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data asset application Service business layer processing
 *
 * @author shu
 * @date 2025-03-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetApplyServiceImpl extends ServiceImpl<DaAssetApplyMapper, DaAssetApplyDO> implements IDaAssetApplyService {
    @Resource
    private DaAssetApplyMapper daAssetApplyMapper;
    @Resource
    private ISysMessageService iSysMessageService;
    @Resource
    private IDaAssetThemeRelService daAssetThemeRelService;

    @Override
    public PageResult<DaAssetApplyDO> getDaAssetApplyPage(DaAssetApplyPageReqVO pageReqVO) {
        if (StringUtils.isNotEmpty(pageReqVO.getThemeName())) {
            DaAssetThemeRelPageReqVO rel = new DaAssetThemeRelPageReqVO();
            rel.setThemeName(pageReqVO.getThemeName());
            List<DaAssetThemeRelRespVO> daAssetThemeRelList = daAssetThemeRelService.getDaAssetThemeRelList(rel);
            if (daAssetThemeRelList.isEmpty()) {
                return PageResult.empty();
            }
            Set<Long> assetIds = daAssetThemeRelList.stream().map(DaAssetThemeRelRespVO::getAssetId).collect(Collectors.toSet());
            pageReqVO.setAssetIds(assetIds);
        }
        PageResult<DaAssetApplyDO> daAssetApplyDOPageResult = daAssetApplyMapper.selectPage(pageReqVO);

        List<DaAssetApplyDO> daAssetApplyDOList = (List<DaAssetApplyDO>) daAssetApplyDOPageResult.getRows();
        for (DaAssetApplyDO daAssetApplyDO : daAssetApplyDOList) {
            DaAssetThemeRelPageReqVO daAssetThemeRelPageReqVO = new DaAssetThemeRelPageReqVO();
            daAssetThemeRelPageReqVO.setAssetId(daAssetApplyDO.getAssetId());
            List<DaAssetThemeRelRespVO> daAssetThemeRelList = daAssetThemeRelService.getDaAssetThemeRelList(daAssetThemeRelPageReqVO);
            if (!daAssetThemeRelList.isEmpty()) {
                String themeName = daAssetThemeRelList.stream().map(DaAssetThemeRelRespVO::getThemeName).collect(Collectors.joining(","));
                daAssetApplyDO.setThemeName(themeName);
                daAssetApplyDO.setDaAssetThemeRelList(daAssetThemeRelList);
            }
        }
        return daAssetApplyDOPageResult;
    }

    @Override
    public Long createDaAssetApply(DaAssetApplySaveReqVO createReqVO) {
        DaAssetApplyDO dictType = BeanUtils.toBean(createReqVO, DaAssetApplyDO.class);
        LambdaQueryWrapperX<DaAssetApplyDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eq(true, DaAssetApplyDO::getAssetId, dictType.getAssetId())
                .eq(true, DaAssetApplyDO::getProjectId, dictType.getProjectId())
                .eq(true, DaAssetApplyDO::getProjectCode, dictType.getProjectCode());
        DaAssetApplyDO daAssetApplyDO = daAssetApplyMapper.selectOne(queryWrapperX);
        if (daAssetApplyDO != null && "2".equals(daAssetApplyDO.getStatus())) {
            daAssetApplyDO.setStatus("1");
            int updateById = daAssetApplyMapper.updateById(daAssetApplyDO);
            return daAssetApplyDO.getId();
        }
        if (daAssetApplyDO != null) {
            throw new ServiceException("da.error.apply.duplicate",
                    "{0} project has applied for {1} asset, please wait for approval!",
                    createReqVO.getProjectName(), createReqVO.getAssetName());
        }
        daAssetApplyMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetApply(DaAssetApplySaveReqVO updateReqVO) {
        // Related validation

        // Update data asset application
        DaAssetApplyDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetApplyDO.class);
        int updateById = daAssetApplyMapper.updateById(updateObj);
        if (updateById > 0 && !"1".equals(updateObj.getStatus())) {
            MessageSaveReqDTO messageSaveReqDTO = new MessageSaveReqDTO();
            messageSaveReqDTO.setSenderId(1L);
            messageSaveReqDTO.setCreatorId(1L);
            messageSaveReqDTO.setCreateBy("超级管理员");
            messageSaveReqDTO.setReceiverId(updateObj.getCreatorId());
            Map<String, Object> map = new HashMap<>();
            map.put("assetName", updateObj.getAssetName());
            map.put("time", DateUtil.date());
            map.put("userName", updateObj.getUpdateBy());
        map.put("statusName", "2".equals(updateObj.getStatus())
                ? MessageUtils.messageWithFallback("da.asset.apply.status.rejected", "Rejected")
                : MessageUtils.messageWithFallback("da.asset.apply.status.approved", "Approved"));
            map.put("approvalReason", updateObj.getApprovalReason());
            iSysMessageService.send("2".equals(updateObj.getStatus()) ? 6L : 5L, messageSaveReqDTO, map);
        }
        return updateById;
    }

    @Override
    public int removeDaAssetApply(Collection<Long> idList) {
        // Batch delete data asset applications
        return daAssetApplyMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetApplyDO getDaAssetApplyById(Long id) {
        MPJLambdaWrapper<DaAssetApplyDO> lambdaWrapper = new MPJLambdaWrapper<>();

        lambdaWrapper.selectAll(DaAssetApplyDO.class)
                .select("t2.NAME AS assetName,t2.DESCRIPTION AS description, t2.TABLE_NAME AS assetTableName, t3.NAME AS projectName, " +
                        "t5.DATASOURCE_NAME AS datasourceName, t5.IP AS datasourceIp, t5.DATASOURCE_TYPE AS datasourceType, t6.PHONENUMBER AS phonenumber")
                .leftJoin("DA_ASSET t2 on t.ASSET_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .leftJoin("ATT_PROJECT t3 on t.PROJECT_ID = t3.ID AND t3.DEL_FLAG = '0'")
//                .leftJoin("ATT_THEME t4 on t2.THEME_ID = t4.ID AND t4.DEL_FLAG = '0'")
                .leftJoin("DA_DATASOURCE t5 on t2.DATASOURCE_ID = t5.ID AND t5.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER t6 on t.CREATOR_ID = t6.USER_ID AND t6.DEL_FLAG = '0'")
                .eq(true, DaAssetApplyDO::getId, id);
        DaAssetApplyDO daAssetApplyDO = daAssetApplyMapper.selectOne(lambdaWrapper);

        DaAssetThemeRelPageReqVO daAssetThemeRelPageReqVO = new DaAssetThemeRelPageReqVO();
        daAssetThemeRelPageReqVO.setAssetId(daAssetApplyDO.getId());
        List<DaAssetThemeRelRespVO> daAssetThemeRelList = daAssetThemeRelService.getDaAssetThemeRelList(daAssetThemeRelPageReqVO);
        daAssetApplyDO.setDaAssetThemeRelList(daAssetThemeRelList);

        return daAssetApplyDO;
    }

    @Override
    public List<DaAssetApplyDO> getDaAssetApplyList() {
        return daAssetApplyMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetApplyDO> getDaAssetApplyMap() {
        List<DaAssetApplyDO> daAssetApplyList = daAssetApplyMapper.selectList();
        return daAssetApplyList.stream()
                .collect(Collectors.toMap(
                        DaAssetApplyDO::getId,
                        daAssetApplyDO -> daAssetApplyDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data asset application data
     *
     * @param importExcelList Data asset application data list
     * @param isUpdateSupport Whether update is supported; if the record already exists, perform an update
     * @param operName        Operator user
     * @return Result
     */
    @Override
    public String importDaAssetApply(List<DaAssetApplyRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetApplyRespVO respVO : importExcelList) {
            try {
                DaAssetApplyDO daAssetApplyDO = BeanUtils.toBean(respVO, DaAssetApplyDO.class);
                Long daAssetApplyId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetApplyId != null) {
                        DaAssetApplyDO existingDaAssetApply = daAssetApplyMapper.selectById(daAssetApplyId);
                        if (existingDaAssetApply != null) {
                            daAssetApplyMapper.updateById(daAssetApplyDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "Data update successful, ID {0} {1} record.", daAssetApplyId, MessageUtils.messageWithFallback("da.entity.asset.request", "Data asset request")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", daAssetApplyId, MessageUtils.messageWithFallback("da.entity.asset.request", "Data asset request")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DaAssetApplyDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetApplyId);
                    DaAssetApplyDO existingDaAssetApply = daAssetApplyMapper.selectOne(queryWrapper);
                    if (existingDaAssetApply == null) {
                        daAssetApplyMapper.insert(daAssetApplyDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", daAssetApplyId, MessageUtils.messageWithFallback("da.entity.asset.request", "Data asset request")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", daAssetApplyId, MessageUtils.messageWithFallback("da.entity.asset.request", "Data asset request")));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                    "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                    failureNum, failureDetails));
            throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                    "Congratulations! All data imported! Total: {0} records.", successNum));
        }
        return resultMsg.toString();
    }
}
