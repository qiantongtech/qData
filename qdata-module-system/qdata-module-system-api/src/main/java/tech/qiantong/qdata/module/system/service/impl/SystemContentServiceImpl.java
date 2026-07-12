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

package tech.qiantong.qdata.module.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.system.domain.SystemContentDO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentPageReqVO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentRespVO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentSaveReqVO;
import tech.qiantong.qdata.module.system.mapper.SystemContentMapper;
import tech.qiantong.qdata.module.system.service.ISystemContentService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * System configuration Service business layer handling
 *
 * @author qdata
 * @date 2024-12-31
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemContentServiceImpl  extends ServiceImpl<SystemContentMapper, SystemContentDO> implements ISystemContentService {
    @Resource
    private SystemContentMapper systemContentMapper;

    @Override
    public PageResult<SystemContentDO> getSystemContentPage(SystemContentPageReqVO pageReqVO) {
        return systemContentMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createSystemContent(SystemContentSaveReqVO createReqVO) {
        SystemContentDO dictType = BeanUtils.toBean(createReqVO, SystemContentDO.class);
        systemContentMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateSystemContent(SystemContentSaveReqVO updateReqVO) {
        // Relevant validation

        // Update system configuration
        SystemContentDO updateObj = BeanUtils.toBean(updateReqVO, SystemContentDO.class);
        return systemContentMapper.updateById(updateObj);
    }
    @Override
    public int removeSystemContent(Collection<Long> idList) {
        // Batch delete system configuration
        return systemContentMapper.deleteBatchIds(idList);
    }

    @Override
    public SystemContentDO getSystemContentById(Long id) {
        return systemContentMapper.selectById(id);
    }

    @Override
    public List<SystemContentDO> getSystemContentList() {
        return systemContentMapper.selectList();
    }

    @Override
    public Map<Long, SystemContentDO> getSystemContentMap() {
        List<SystemContentDO> systemContentList = systemContentMapper.selectList();
        return systemContentList.stream()
                .collect(Collectors.toMap(
                        SystemContentDO::getId,
                        systemContentDO -> systemContentDO,
                        // Keep the existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import system configuration data
         *
         * @param importExcelList system configuration data list
         * @param isUpdateSupport whether to support update, update if already exists
         * @param operName operator name
         * @return result
         */
        @Override
        public String importSystemContent(List<SystemContentRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (SystemContentRespVO respVO : importExcelList) {
                try {
                    SystemContentDO systemContentDO = BeanUtils.toBean(respVO, SystemContentDO.class);
                    Long systemContentId = respVO.getId();
                    if (isUpdateSupport) {
                        if (systemContentId != null) {
                            SystemContentDO existingSystemContent = systemContentMapper.selectById(systemContentId);
                            if (existingSystemContent != null) {
                                systemContentMapper.updateById(systemContentDO);
                                successNum++;
                                successMessages.add("Data updated successfully, system configuration record with ID " + systemContentId + ".");
                            } else {
                                failureNum++;
                                failureMessages.add("Data update failed, system configuration record with ID " + systemContentId + " does not exist.");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("Data update failed, the record ID does not exist.");
                        }
                    } else {
                        QueryWrapper<SystemContentDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", systemContentId);
                        SystemContentDO existingSystemContent = systemContentMapper.selectOne(queryWrapper);
                        if (existingSystemContent == null) {
                            systemContentMapper.insert(systemContentDO);
                            successNum++;
                            successMessages.add("Data inserted successfully, system configuration record with ID " + systemContentId + ".");
                        } else {
                            failureNum++;
                            failureMessages.add("Data insertion failed, system configuration record with ID " + systemContentId + " already exists.");
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = "Data import failed, error message: " + e.getMessage();
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                resultMsg.append("Sorry, import failed! Total " + failureNum + " records have incorrect format, errors as follows:");
                resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
                throw new ServiceException(resultMsg.toString());
            } else {
                resultMsg.append("Congratulations, all data imported successfully! Total " + successNum + " records.");
            }
            return resultMsg.toString();
        }
}
