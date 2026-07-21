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

package tech.qiantong.qdata.module.da.service.discovery.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogRespVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTaskLogDO;
import tech.qiantong.qdata.module.da.dal.mapper.discovery.DaDiscoveryTaskLogMapper;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryLogBodyService;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryTaskLogService;
import tech.qiantong.qdata.redis.service.IRedisService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Discovery Task Log Service business layer processing
 *
 * @author qdata
 * @date 2025-02-17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaDiscoveryTaskLogServiceImpl  extends ServiceImpl<DaDiscoveryTaskLogMapper,DaDiscoveryTaskLogDO> implements IDaDiscoveryTaskLogService {
    @Resource
    private DaDiscoveryTaskLogMapper daDiscoveryTaskLogMapper;
    @Resource
    private IDaDiscoveryLogBodyService iDaDiscoveryLogBodyService;
    @Resource
    @Lazy
    private IRedisService redisService;

    @Override
    public PageResult<DaDiscoveryTaskLogDO> getDaDiscoveryTaskLogPage(DaDiscoveryTaskLogPageReqVO pageReqVO) {
        return daDiscoveryTaskLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaDiscoveryTaskLog(DaDiscoveryTaskLogSaveReqVO createReqVO) {
        DaDiscoveryTaskLogDO dictType = BeanUtils.toBean(createReqVO, DaDiscoveryTaskLogDO.class);
        daDiscoveryTaskLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaDiscoveryTaskLog(DaDiscoveryTaskLogSaveReqVO updateReqVO) {
        // Related validation

        // Update data discovery task log
        DaDiscoveryTaskLogDO updateObj = BeanUtils.toBean(updateReqVO, DaDiscoveryTaskLogDO.class);
        return daDiscoveryTaskLogMapper.updateById(updateObj);
    }
    @Override
    public int removeDaDiscoveryTaskLog(Collection<Long> idList) {
        // Batch delete data discovery task logs
        int i = daDiscoveryTaskLogMapper.deleteBatchIds(idList);
        for (Long id : idList) {
            iDaDiscoveryLogBodyService.deleteByPk(id);
        }
        return 1;
    }

    @Override
    public DaDiscoveryTaskLogDO getDaDiscoveryTaskLogById(Long id) {
        return daDiscoveryTaskLogMapper.selectById(id);
    }

    @Override
    public List<DaDiscoveryTaskLogDO> getDaDiscoveryTaskLogList() {
        return daDiscoveryTaskLogMapper.selectList();
    }

    @Override
    public Map<Long, DaDiscoveryTaskLogDO> getDaDiscoveryTaskLogMap() {
        List<DaDiscoveryTaskLogDO> daDiscoveryTaskLogList = daDiscoveryTaskLogMapper.selectList();
        return daDiscoveryTaskLogList.stream()
                .collect(Collectors.toMap(
                        DaDiscoveryTaskLogDO::getId,
                        daDiscoveryTaskLogDO -> daDiscoveryTaskLogDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data discovery task log data
         *
         * @param importExcelList Data discovery task log data list
         * @param isUpdateSupport Whether to support update, if already exists, update the data
         * @param operName Operating user
         * @return result
         */
        @Override
        public String importDaDiscoveryTaskLog(List<DaDiscoveryTaskLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DaDiscoveryTaskLogRespVO respVO : importExcelList) {
                try {
                    DaDiscoveryTaskLogDO daDiscoveryTaskLogDO = BeanUtils.toBean(respVO, DaDiscoveryTaskLogDO.class);
                    Long daDiscoveryTaskLogId = respVO.getId();
                    if (isUpdateSupport) {
                        if (daDiscoveryTaskLogId != null) {
                            DaDiscoveryTaskLogDO existingDaDiscoveryTaskLog = daDiscoveryTaskLogMapper.selectById(daDiscoveryTaskLogId);
                            if (existingDaDiscoveryTaskLog != null) {
                                daDiscoveryTaskLogMapper.updateById(daDiscoveryTaskLogDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                        "Data update successful, ID {0} {1} record.", daDiscoveryTaskLogId, MessageUtils.messageWithFallback("da.entity.discovery.task.log", "Data discovery task log")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", daDiscoveryTaskLogId, MessageUtils.messageWithFallback("da.entity.discovery.task.log", "Data discovery task log")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DaDiscoveryTaskLogDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daDiscoveryTaskLogId);
                        DaDiscoveryTaskLogDO existingDaDiscoveryTaskLog = daDiscoveryTaskLogMapper.selectOne(queryWrapper);
                        if (existingDaDiscoveryTaskLog == null) {
                            daDiscoveryTaskLogMapper.insert(daDiscoveryTaskLogDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", daDiscoveryTaskLogId, MessageUtils.messageWithFallback("da.entity.discovery.task.log", "Data discovery task log")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", daDiscoveryTaskLogId, MessageUtils.messageWithFallback("da.entity.discovery.task.log", "Data discovery task log")));
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

    @Override
    public String getLogInfo(Long id) {
        String content = "";
        final String taskInstanceLogKey = DaDiscoveryLogBodyServiceImpl.DISCOVERY_TASK_LOG_KEY_PREFIX + id;

        if (redisService.hasKey(taskInstanceLogKey)) {
            content += redisService.get(taskInstanceLogKey) + "\n";
        } else {
            //Fetch log from table
            String logContent = iDaDiscoveryLogBodyService.getLog(id);
            if (logContent != null) {
                content += logContent + "\n";
            }
        }
        return content;
    }
}
