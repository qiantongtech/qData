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

package tech.qiantong.qdata.module.att.service.client.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientApiRelDO;
import tech.qiantong.qdata.module.att.dal.mapper.client.AttClientApiRelMapper;
import tech.qiantong.qdata.module.att.service.client.IAttClientApiRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * App API Service Relation service layer processing
 *
 * @author FXB
 * @date 2025-08-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttClientApiRelServiceImpl  extends ServiceImpl<AttClientApiRelMapper,AttClientApiRelDO> implements IAttClientApiRelService {
    @Resource
    private AttClientApiRelMapper attClientApiRelMapper;

    @Override
    public PageResult<AttClientApiRelDO> getAttClientApiRelPage(AttClientApiRelPageReqVO pageReqVO) {
        return attClientApiRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttClientApiRel(AttClientApiRelSaveReqVO createReqVO) {
        AttClientApiRelDO dictType = BeanUtils.toBean(createReqVO, AttClientApiRelDO.class);
        attClientApiRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttClientApiRel(AttClientApiRelSaveReqVO updateReqVO) {
        // Validation

        // Update App API Service Relation
        AttClientApiRelDO updateObj = BeanUtils.toBean(updateReqVO, AttClientApiRelDO.class);
        return attClientApiRelMapper.updateById(updateObj);
    }
    @Override
    public int removeAttClientApiRel(Collection<Long> idList) {
        // Batch delete App API Service Relation
        return attClientApiRelMapper.deleteBatchIds(idList);
    }

    @Override
    public AttClientApiRelDO getAttClientApiRelById(Long id) {
        return attClientApiRelMapper.selectById(id);
    }

    @Override
    public List<AttClientApiRelDO> getAttClientApiRelList() {
        return attClientApiRelMapper.selectList();
    }

    @Override
    public Map<Long, AttClientApiRelDO> getAttClientApiRelMap() {
        List<AttClientApiRelDO> attClientApiRelList = attClientApiRelMapper.selectList();
        return attClientApiRelList.stream()
                .collect(Collectors.toMap(
                        AttClientApiRelDO::getId,
                        attClientApiRelDO -> attClientApiRelDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import App API Service Relation data
         *
         *  importExcelList App API Service Relation data list
         * @param isUpdateSupport Whether to support update; if already exists, update the data
         * @param operName Operator
         * @return Result
         */
        @Override
        public String importAttClientApiRel(List<AttClientApiRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (AttClientApiRelRespVO respVO : importExcelList) {
                try {
                    AttClientApiRelDO attClientApiRelDO = BeanUtils.toBean(respVO, AttClientApiRelDO.class);
                    Long attClientApiRelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (attClientApiRelId != null) {
                            AttClientApiRelDO existingAttClientApiRel = attClientApiRelMapper.selectById(attClientApiRelId);
                            if (existingAttClientApiRel != null) {
                                attClientApiRelMapper.updateById(attClientApiRelDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                        "Data update successful, ID {0} {1} record.", attClientApiRelId, MessageUtils.messageWithFallback("att.entity.application.api.relation", "Application API service relation")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", attClientApiRelId, MessageUtils.messageWithFallback("att.entity.application.api.relation", "Application API service relation")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<AttClientApiRelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", attClientApiRelId);
                        AttClientApiRelDO existingAttClientApiRel = attClientApiRelMapper.selectOne(queryWrapper);
                        if (existingAttClientApiRel == null) {
                            attClientApiRelMapper.insert(attClientApiRelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", attClientApiRelId, MessageUtils.messageWithFallback("att.entity.application.api.relation", "Application API service relation")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", attClientApiRelId, MessageUtils.messageWithFallback("att.entity.application.api.relation", "Application API service relation")));
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
