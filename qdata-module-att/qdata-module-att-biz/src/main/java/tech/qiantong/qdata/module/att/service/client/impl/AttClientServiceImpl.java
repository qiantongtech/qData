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

import cn.hutool.core.util.IdUtil;
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
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientDO;
import tech.qiantong.qdata.module.att.dal.mapper.client.AttClientMapper;
import tech.qiantong.qdata.module.att.service.client.IAttClientService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Application management service layer processing
 *
 * @author qdata
 * @date 2025-02-18
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttClientServiceImpl  extends ServiceImpl<AttClientMapper,AttClientDO> implements IAttClientService {
    @Resource
    private AttClientMapper attClientMapper;

    @Override
    public PageResult<AttClientDO> getAttClientPage(AttClientPageReqVO pageReqVO) {
        return attClientMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttClient(AttClientSaveReqVO createReqVO) {
        AttClientDO dictType = BeanUtils.toBean(createReqVO, AttClientDO.class);
        dictType.setSecret(IdUtil.fastSimpleUUID());
        attClientMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttClient(AttClientSaveReqVO updateReqVO) {
        // Validation

        // Update application management
        AttClientDO updateObj = BeanUtils.toBean(updateReqVO, AttClientDO.class);
        return attClientMapper.updateById(updateObj);
    }
    @Override
    public int removeAttClient(Collection<Long> idList) {
        // Batch delete application management
        return attClientMapper.deleteBatchIds(idList);
    }

    @Override
    public AttClientDO getAttClientById(Long id) {
        return attClientMapper.selectById(id);
    }

    @Override
    public List<AttClientDO> getAttClientList() {
        return attClientMapper.selectList();
    }

    @Override
    public Map<Long, AttClientDO> getAttClientMap() {
        List<AttClientDO> attClientList = attClientMapper.selectList();
        return attClientList.stream()
                .collect(Collectors.toMap(
                        AttClientDO::getId,
                        attClientDO -> attClientDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import application management data
         *
         *  importExcelList application management data list
         * @param isUpdateSupport Whether to support update; if already exists, update the data
         *  operName Operator
         *  Result
         */
        @Override
        public String importAttClient(List<AttClientRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("att.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (AttClientRespVO respVO : importExcelList) {
                try {
                    AttClientDO attClientDO = BeanUtils.toBean(respVO, AttClientDO.class);
                    Long attClientId = respVO.getId();
                    if (isUpdateSupport) {
                        if (attClientId != null) {
                            AttClientDO existingAttClient = attClientMapper.selectById(attClientId);
                            if (existingAttClient != null) {
                                attClientMapper.updateById(attClientDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("att.import.update.success",
                                        "Data update successful, ID {0} {1} record.", attClientId, MessageUtils.messageWithFallback("att.entity.application", "Application")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("att.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", attClientId, MessageUtils.messageWithFallback("att.entity.application", "Application")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<AttClientDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", attClientId);
                        AttClientDO existingAttClient = attClientMapper.selectOne(queryWrapper);
                        if (existingAttClient == null) {
                            attClientMapper.insert(attClientDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("att.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", attClientId, MessageUtils.messageWithFallback("att.entity.application", "Application")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("att.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", attClientId, MessageUtils.messageWithFallback("att.entity.application", "Application")));
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
