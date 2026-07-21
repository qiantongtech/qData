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

package tech.qiantong.qdata.module.dpp.service.etl.impl;

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
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskExtPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskExtRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskExtSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskExtDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlTaskExtMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskExtService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Integration Task - Extended Data Service business layer processing
 *
 * @author qdata
 * @date 2025-04-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlTaskExtServiceImpl extends ServiceImpl<DppEtlTaskExtMapper, DppEtlTaskExtDO> implements IDppEtlTaskExtService {
    @Resource
    private DppEtlTaskExtMapper dppEtlTaskExtMapper;

    @Override
    public PageResult<DppEtlTaskExtDO> getDppEtlTaskExtPage(DppEtlTaskExtPageReqVO pageReqVO) {
        return dppEtlTaskExtMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppEtlTaskExt(DppEtlTaskExtSaveReqVO createReqVO) {
        DppEtlTaskExtDO dictType = BeanUtils.toBean(createReqVO, DppEtlTaskExtDO.class);
        dppEtlTaskExtMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppEtlTaskExt(DppEtlTaskExtSaveReqVO updateReqVO) {
        // Validate

        // Update Data Integration Task - Extended Data
        DppEtlTaskExtDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlTaskExtDO.class);
        return dppEtlTaskExtMapper.updateById(updateObj);
    }

    @Override
    public int removeDppEtlTaskExt(Collection<Long> idList) {
        // Batch delete Data Integration Task - Extended Data
        return dppEtlTaskExtMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEtlTaskExtDO getDppEtlTaskExtById(Long id) {
        return dppEtlTaskExtMapper.selectById(id);
    }

    @Override
    public List<DppEtlTaskExtDO> getDppEtlTaskExtList() {
        return dppEtlTaskExtMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlTaskExtDO> getDppEtlTaskExtMap() {
        List<DppEtlTaskExtDO> dppEtlTaskExtList = dppEtlTaskExtMapper.selectList();
        return dppEtlTaskExtList.stream()
                .collect(Collectors.toMap(
                        DppEtlTaskExtDO::getId,
                        dppEtlTaskExtDO -> dppEtlTaskExtDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import Data Integration Task - Extended Data
     *
     * @param importExcelList Data Integration Task - Extended Data list
     * @param isUpdateSupport whether to support update; if already exists, update the data
     * @param operName        operator user
     * @return Result
     */
    @Override
    public String importDppEtlTaskExt(List<DppEtlTaskExtRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dpp.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DppEtlTaskExtRespVO respVO : importExcelList) {
            try {
                DppEtlTaskExtDO dppEtlTaskExtDO = BeanUtils.toBean(respVO, DppEtlTaskExtDO.class);
                Long dppEtlTaskExtId = respVO.getId();
                if (isUpdateSupport) {
                    if (dppEtlTaskExtId != null) {
                        DppEtlTaskExtDO existingDppEtlTaskExt = dppEtlTaskExtMapper.selectById(dppEtlTaskExtId);
                        if (existingDppEtlTaskExt != null) {
                            dppEtlTaskExtMapper.updateById(dppEtlTaskExtDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dpp.import.update.success",
                                    "Data update successful, ID {0} {1} record.", dppEtlTaskExtId, MessageUtils.messageWithFallback("dpp.entity.etl.task.extension", "Data integration task extension")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", dppEtlTaskExtId, MessageUtils.messageWithFallback("dpp.entity.etl.task.extension", "Data integration task extension")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DppEtlTaskExtDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppEtlTaskExtId);
                    DppEtlTaskExtDO existingDppEtlTaskExt = dppEtlTaskExtMapper.selectOne(queryWrapper);
                    if (existingDppEtlTaskExt == null) {
                        dppEtlTaskExtMapper.insert(dppEtlTaskExtDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", dppEtlTaskExtId, MessageUtils.messageWithFallback("dpp.entity.etl.task.extension", "Data integration task extension")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", dppEtlTaskExtId, MessageUtils.messageWithFallback("dpp.entity.etl.task.extension", "Data integration task extension")));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("dpp.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.fail",
                    "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                    failureNum, failureDetails));
            throw new ServiceException("dpp.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.success",
                    "Congratulations! All data imported! Total: {0} records.", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public DppEtlTaskExtDO getByTaskId(Long taskId) {
        return this.getOne(Wrappers.lambdaQuery(DppEtlTaskExtDO.class)
                .eq(DppEtlTaskExtDO::getTaskId, taskId));
    }
}
