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

package tech.qiantong.qdata.quality.service.qa.impl;

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
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityLogDO;
import tech.qiantong.qdata.quality.dal.mapper.qa.DppQualityLogMapper;
import tech.qiantong.qdata.quality.service.qa.IDppQualityLogService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 数据质量日志Service业务层处理
 *
 * @author qdata
 * @date 2025-07-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppQualityLogServiceImpl  extends ServiceImpl<DppQualityLogMapper, DppQualityLogDO> implements IDppQualityLogService {
    @Resource
    private DppQualityLogMapper dppQualityLogMapper;

    @Override
    public PageResult<DppQualityLogDO> getDppQualityLogPage(DppQualityLogPageReqVO pageReqVO) {
        return dppQualityLogMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDppQualityLog(DppQualityLogSaveReqVO createReqVO) {
        DppQualityLogDO dictType = BeanUtils.toBean(createReqVO, DppQualityLogDO.class);
        dppQualityLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDppQualityLog(DppQualityLogSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据质量日志
        DppQualityLogDO updateObj = BeanUtils.toBean(updateReqVO, DppQualityLogDO.class);
        return dppQualityLogMapper.updateById(updateObj);
    }
    @Override
    public int removeDppQualityLog(Collection<Long> idList) {
        // 批量删除数据质量日志
        return dppQualityLogMapper.deleteBatchIds(idList);
    }

    @Override
    public DppQualityLogDO getDppQualityLogById(Long id) {
        return dppQualityLogMapper.selectById(id);
    }

    @Override
    public List<DppQualityLogDO> getDppQualityLogList() {
        return dppQualityLogMapper.selectList();
    }

    @Override
    public Map<Long, DppQualityLogDO> getDppQualityLogMap() {
        List<DppQualityLogDO> dppQualityLogList = dppQualityLogMapper.selectList();
        return dppQualityLogList.stream()
                .collect(Collectors.toMap(
                        DppQualityLogDO::getId,
                        dppQualityLogDO -> dppQualityLogDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据质量日志数据
         *
         * @param importExcelList 数据质量日志数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDppQualityLog(List<DppQualityLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("quality.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DppQualityLogRespVO respVO : importExcelList) {
                try {
                    DppQualityLogDO dppQualityLogDO = BeanUtils.toBean(respVO, DppQualityLogDO.class);
                    Long dppQualityLogId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dppQualityLogId != null) {
                            DppQualityLogDO existingDppQualityLog = dppQualityLogMapper.selectById(dppQualityLogId);
                            if (existingDppQualityLog != null) {
                                dppQualityLogMapper.updateById(dppQualityLogDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("quality.import.update.success",
                                        "数据更新成功，ID为 " + dppQualityLogId + " 的数据质量日志记录。", dppQualityLogId, "数据质量日志"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.fail",
                                        "数据更新失败，ID为 " + dppQualityLogId + " 的数据质量日志记录不存在。", dppQualityLogId, "数据质量日志"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DppQualityLogDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppQualityLogId);
                        DppQualityLogDO existingDppQualityLog = dppQualityLogMapper.selectOne(queryWrapper);
                        if (existingDppQualityLog == null) {
                            dppQualityLogMapper.insert(dppQualityLogDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("quality.import.insert.success",
                                    "数据插入成功，ID为 " + dppQualityLogId + " 的数据质量日志记录。", dppQualityLogId, "数据质量日志"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("quality.import.insert.fail",
                                    "数据插入失败，ID为 " + dppQualityLogId + " 的数据质量日志记录已存在。", dppQualityLogId, "数据质量日志"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("quality.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("quality.import.result.fail",
                        "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("quality.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("quality.import.result.success",
                        "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
            }
            return resultMsg.toString();
        }
}
