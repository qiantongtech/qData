/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
 * 数据发现任务日志Service业务层处理
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
        // 相关校验

        // 更新数据发现任务日志
        DaDiscoveryTaskLogDO updateObj = BeanUtils.toBean(updateReqVO, DaDiscoveryTaskLogDO.class);
        return daDiscoveryTaskLogMapper.updateById(updateObj);
    }
    @Override
    public int removeDaDiscoveryTaskLog(Collection<Long> idList) {
        // 批量删除数据发现任务日志
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
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据发现任务日志数据
         *
         * @param importExcelList 数据发现任务日志数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDaDiscoveryTaskLog(List<DaDiscoveryTaskLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("da.error.import.empty", "导入数据不能为空！");
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
                                        "数据更新成功，ID为 " + daDiscoveryTaskLogId + " 的数据发现任务日志记录。", daDiscoveryTaskLogId, "数据发现任务日志"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                        "数据更新失败，ID为 " + daDiscoveryTaskLogId + " 的数据发现任务日志记录不存在。", daDiscoveryTaskLogId, "数据发现任务日志"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DaDiscoveryTaskLogDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daDiscoveryTaskLogId);
                        DaDiscoveryTaskLogDO existingDaDiscoveryTaskLog = daDiscoveryTaskLogMapper.selectOne(queryWrapper);
                        if (existingDaDiscoveryTaskLog == null) {
                            daDiscoveryTaskLogMapper.insert(daDiscoveryTaskLogDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                    "数据插入成功，ID为 " + daDiscoveryTaskLogId + " 的数据发现任务日志记录。", daDiscoveryTaskLogId, "数据发现任务日志"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                    "数据插入失败，ID为 " + daDiscoveryTaskLogId + " 的数据发现任务日志记录已存在。", daDiscoveryTaskLogId, "数据发现任务日志"));
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

    @Override
    public String getLogInfo(Long id) {
        String content = "";
        final String taskInstanceLogKey = DaDiscoveryLogBodyServiceImpl.DISCOVERY_TASK_LOG_KEY_PREFIX + id;

        if (redisService.hasKey(taskInstanceLogKey)) {
            content += redisService.get(taskInstanceLogKey) + "\n";
        } else {
            //获取表中的日志
            String logContent = iDaDiscoveryLogBodyService.getLog(id);
            if (logContent != null) {
                content += logContent + "\n";
            }
        }
        return content;
    }
}
