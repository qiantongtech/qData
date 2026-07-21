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

package tech.qiantong.qdata.module.att.service.theme.impl;

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
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemePageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemeRespVO;
import tech.qiantong.qdata.module.att.controller.admin.theme.vo.AttThemeSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.theme.AttThemeDO;
import tech.qiantong.qdata.module.att.dal.mapper.theme.AttThemeMapper;
import tech.qiantong.qdata.module.att.service.theme.IAttThemeService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Theme Service business layer processing
 *
 * @author qdata
 * @date 2025-01-20
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttThemeServiceImpl  extends ServiceImpl<AttThemeMapper,AttThemeDO> implements IAttThemeService {
    @Resource
    private AttThemeMapper attThemeMapper;

    @Override
    public PageResult<AttThemeDO> getAttThemePage(AttThemePageReqVO pageReqVO) {
        return attThemeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AttThemeDO> getAttThemeListByReqVO(AttThemePageReqVO reqVO) {
        MPJLambdaWrapper<AttThemeDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(AttThemeDO.class)
                .like(StringUtils.isNotBlank(reqVO.getName()), AttThemeDO::getName, reqVO.getName());
        return attThemeMapper.selectList(wrapper);
    }

    @Override
    public Long createAttTheme(AttThemeSaveReqVO createReqVO) {
        AttThemeDO dictType = BeanUtils.toBean(createReqVO, AttThemeDO.class);
        attThemeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttTheme(AttThemeSaveReqVO updateReqVO) {
        // Validate

        // Update Theme
        AttThemeDO updateObj = BeanUtils.toBean(updateReqVO, AttThemeDO.class);
        return attThemeMapper.updateById(updateObj);
    }
    @Override
    public int removeAttTheme(Collection<Long> idList) {
        // Batch Delete Theme
        return attThemeMapper.deleteBatchIds(idList);
    }

    @Override
    public AttThemeDO getAttThemeById(Long id) {
        return attThemeMapper.selectById(id);
    }

    @Override
    public List<AttThemeDO> getAttThemeList() {
        return attThemeMapper.selectList();
    }

    @Override
    public Map<Long, AttThemeDO> getAttThemeMap() {
        List<AttThemeDO> attThemeList = attThemeMapper.selectList();
        return attThemeList.stream()
                .collect(Collectors.toMap(
                        AttThemeDO::getId,
                        attThemeDO -> attThemeDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import Theme data
     *
     *  importExcelList Theme data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     *  operName Operator
     *  @return Result
     */
    @Override
    public String importAttTheme(List<AttThemeRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (AttThemeRespVO respVO : importExcelList) {
            try {
                AttThemeDO attThemeDO = BeanUtils.toBean(respVO, AttThemeDO.class);
                Long attThemeId = respVO.getId();
                if (isUpdateSupport) {
                    if (attThemeId != null) {
                        AttThemeDO existingAttTheme = attThemeMapper.selectById(attThemeId);
                        if (existingAttTheme != null) {
                            attThemeMapper.updateById(attThemeDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                    "Data update successful, ID {0} {1} record.", attThemeId, MessageUtils.messageWithFallback("att.entity.topic", "Topic")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", attThemeId, MessageUtils.messageWithFallback("att.entity.topic", "Topic")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<AttThemeDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", attThemeId);
                    AttThemeDO existingAttTheme = attThemeMapper.selectOne(queryWrapper);
                    if (existingAttTheme == null) {
                        attThemeMapper.insert(attThemeDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", attThemeId, MessageUtils.messageWithFallback("att.entity.topic", "Topic")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", attThemeId, MessageUtils.messageWithFallback("att.entity.topic", "Topic")));
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
}
