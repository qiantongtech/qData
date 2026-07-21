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

package tech.qiantong.qdata.module.dpp.service.qa.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskObjDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.qa.DppQualityTaskObjMapper;
import tech.qiantong.qdata.module.dpp.service.qa.IDppQualityTaskObjService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * Data Quality Task - Audit Object Service business layer processing
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppQualityTaskObjServiceImpl  extends ServiceImpl<DppQualityTaskObjMapper,DppQualityTaskObjDO> implements IDppQualityTaskObjService {
    @Resource
    private DppQualityTaskObjMapper dppQualityTaskObjMapper;

    @Override
    public PageResult<DppQualityTaskObjDO> getDppQualityTaskObjPage(DppQualityTaskObjPageReqVO pageReqVO) {
        return dppQualityTaskObjMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppQualityTaskObj(DppQualityTaskObjSaveReqVO createReqVO) {
        DppQualityTaskObjDO dictType = BeanUtils.toBean(createReqVO, DppQualityTaskObjDO.class);
        dppQualityTaskObjMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppQualityTaskObj(DppQualityTaskObjSaveReqVO updateReqVO) {
        // Validate

        // Update Data Quality Task - Audit Object
        DppQualityTaskObjDO updateObj = BeanUtils.toBean(updateReqVO, DppQualityTaskObjDO.class);
        return dppQualityTaskObjMapper.updateById(updateObj);
    }
    @Override
    public int removeDppQualityTaskObj(Collection<Long> idList) {
        // Batch delete Data Quality Task - Audit Object
        return dppQualityTaskObjMapper.deleteBatchIds(idList);
    }

    @Override
    public DppQualityTaskObjDO getDppQualityTaskObjById(Long id) {
        return dppQualityTaskObjMapper.selectById(id);
    }

    @Override
    public List<DppQualityTaskObjDO> getDppQualityTaskObjList() {
        return dppQualityTaskObjMapper.selectList();
    }

    @Override
    public List<DppQualityTaskObjDO> getDppQualityTaskObjList(DppQualityTaskObjPageReqVO pageReqVO) {
        LambdaQueryWrapperX<DppQualityTaskObjDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eq(pageReqVO.getTaskId() != null,DppQualityTaskObjDO::getTaskId, pageReqVO.getTaskId());
        List<DppQualityTaskObjDO> dppQualityTaskObjDOS = dppQualityTaskObjMapper.selectList(queryWrapperX);
        return dppQualityTaskObjDOS;
    }


    @Override
    public Map<Long, DppQualityTaskObjDO> getDppQualityTaskObjMap() {
        List<DppQualityTaskObjDO> dppQualityTaskObjList = dppQualityTaskObjMapper.selectList();
        return dppQualityTaskObjList.stream()
                .collect(Collectors.toMap(
                        DppQualityTaskObjDO::getId,
                        dppQualityTaskObjDO -> dppQualityTaskObjDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import Data Quality Task - Audit Object data
     *
     * @param importExcelList Data Quality Task - Audit Object data list
     * @param isUpdateSupport whether to support update; if already exists, update the data
     * @param operName operator user
     * @return result
     */
    @Override
    public String importDppQualityTaskObj(List<DppQualityTaskObjRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dpp.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DppQualityTaskObjRespVO respVO : importExcelList) {
            try {
                DppQualityTaskObjDO dppQualityTaskObjDO = BeanUtils.toBean(respVO, DppQualityTaskObjDO.class);
                Long dppQualityTaskObjId = respVO.getId();
                if (isUpdateSupport) {
                    if (dppQualityTaskObjId != null) {
                        DppQualityTaskObjDO existingDppQualityTaskObj = dppQualityTaskObjMapper.selectById(dppQualityTaskObjId);
                        if (existingDppQualityTaskObj != null) {
                            dppQualityTaskObjMapper.updateById(dppQualityTaskObjDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dpp.import.update.success",
                                    "Data update successful, ID {0} {1} record.", dppQualityTaskObjId, MessageUtils.messageWithFallback("dpp.entity.quality.task.audit.object", "Data quality task audit object")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", dppQualityTaskObjId, MessageUtils.messageWithFallback("dpp.entity.quality.task.audit.object", "Data quality task audit object")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DppQualityTaskObjDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppQualityTaskObjId);
                    DppQualityTaskObjDO existingDppQualityTaskObj = dppQualityTaskObjMapper.selectOne(queryWrapper);
                    if (existingDppQualityTaskObj == null) {
                        dppQualityTaskObjMapper.insert(dppQualityTaskObjDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", dppQualityTaskObjId, MessageUtils.messageWithFallback("dpp.entity.quality.task.audit.object", "Data quality task audit object")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", dppQualityTaskObjId, MessageUtils.messageWithFallback("dpp.entity.quality.task.audit.object", "Data quality task audit object")));
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
}
