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

package tech.qiantong.qdata.module.att.service.sourceSystem.impl;

import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemRespVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.sourceSystem.AttSourceSystemDO;
import tech.qiantong.qdata.module.att.dal.mapper.sourceSystem.AttSourceSystemMapper;
import tech.qiantong.qdata.module.att.service.sourceSystem.IAttSourceSystemService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Source System Service business layer processing
 *
 * @author qdata
 * @date 2026-04-03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttSourceSystemServiceImpl  extends ServiceImpl<AttSourceSystemMapper,AttSourceSystemDO> implements IAttSourceSystemService {
    @Resource
    private AttSourceSystemMapper attSourceSystemMapper;

    @Override
    public PageResult<AttSourceSystemDO> getAttSourceSystemPage(AttSourceSystemPageReqVO pageReqVO) {
        return attSourceSystemMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttSourceSystem(AttSourceSystemSaveReqVO createReqVO) {
        AttSourceSystemDO dictType = BeanUtils.toBean(createReqVO, AttSourceSystemDO.class);
        attSourceSystemMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttSourceSystem(AttSourceSystemSaveReqVO updateReqVO) {
        // Validate

        // Update Source System
        AttSourceSystemDO updateObj = BeanUtils.toBean(updateReqVO, AttSourceSystemDO.class);
        return attSourceSystemMapper.updateById(updateObj);
    }
    @Override
    public int removeAttSourceSystem(Collection<Long> idList) {
        // Check if validFlag is true
        if (idList.stream().anyMatch(id -> attSourceSystemMapper.selectById(id).getValidFlag() == true)) {
            throw new ServiceException("att.error.source.system.enabled", "Enabled source system cannot be deleted!");
        }
        // Batch Delete Source System
        return attSourceSystemMapper.deleteBatchIds(idList);
    }

    @Override
    public AttSourceSystemDO getAttSourceSystemById(Long id) {
        return attSourceSystemMapper.selectById(id);
    }

    @Override
    public List<AttSourceSystemDO> getAttSourceSystemList() {
        return attSourceSystemMapper.selectList();
    }

    @Override
    public List<AttSourceSystemDO> getAttSourceSystemListByValidFlag(Boolean validFlag) {
        return attSourceSystemMapper.selectList(new LambdaQueryWrapperX<AttSourceSystemDO>().eqIfPresent(AttSourceSystemDO::getValidFlag, validFlag));
    }

    @Override
    public Map<Long, AttSourceSystemDO> getAttSourceSystemMap() {
        List<AttSourceSystemDO> attSourceSystemList = attSourceSystemMapper.selectList();
        return attSourceSystemList.stream()
                .collect(Collectors.toMap(
                        AttSourceSystemDO::getId,
                        attSourceSystemDO -> attSourceSystemDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import Source System data
         *
         *  importExcelList Source System data list
         * @param isUpdateSupport Whether to support update; if already exists, update the data
         *  operName Operator
         *  @return Result
         */
        @Override
        public String importAttSourceSystem(List<AttSourceSystemRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (AttSourceSystemRespVO respVO : importExcelList) {
                try {
                    AttSourceSystemDO attSourceSystemDO = BeanUtils.toBean(respVO, AttSourceSystemDO.class);
                    Long attSourceSystemId = respVO.getId();
                    if (isUpdateSupport) {
                        if (attSourceSystemId != null) {
                            AttSourceSystemDO existingAttSourceSystem = attSourceSystemMapper.selectById(attSourceSystemId);
                            if (existingAttSourceSystem != null) {
                                attSourceSystemMapper.updateById(attSourceSystemDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                        "Data update successful, ID {0} {1} record.", attSourceSystemId, MessageUtils.messageWithFallback("att.entity.source.system", "Source system")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", attSourceSystemId, MessageUtils.messageWithFallback("att.entity.source.system", "Source system")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<AttSourceSystemDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", attSourceSystemId);
                        AttSourceSystemDO existingAttSourceSystem = attSourceSystemMapper.selectOne(queryWrapper);
                        if (existingAttSourceSystem == null) {
                            attSourceSystemMapper.insert(attSourceSystemDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", attSourceSystemId, MessageUtils.messageWithFallback("att.entity.source.system", "Source system")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", attSourceSystemId, MessageUtils.messageWithFallback("att.entity.source.system", "Source system")));
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
