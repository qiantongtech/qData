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
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskObjPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskObjRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskObjSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskObjDO;
import tech.qiantong.qdata.quality.dal.mapper.qa.DppQualityTaskObjMapper;
import tech.qiantong.qdata.quality.service.qa.IDppQualityTaskObjService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * Data quality task-audit object Service business layer processing
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppQualityTaskObjServiceImpl  extends ServiceImpl<DppQualityTaskObjMapper, DppQualityTaskObjDO> implements IDppQualityTaskObjService {
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
        // Related verification

        // Update data quality task-audit object
        DppQualityTaskObjDO updateObj = BeanUtils.toBean(updateReqVO, DppQualityTaskObjDO.class);
        return dppQualityTaskObjMapper.updateById(updateObj);
    }
    @Override
    public int removeDppQualityTaskObj(Collection<Long> idList) {
        // Batch deletion of data quality tasks-audit objects
        return dppQualityTaskObjMapper.deleteBatchIds(idList);
    }

    @Override
    public DppQualityTaskObjDO getDppQualityTaskObjById(Long id) {
        return dppQualityTaskObjMapper.selectById(id);
    }

    @Override
    public List<DppQualityTaskObjDO> getDppQualityTaskObjList(String taskId) {
        LambdaQueryWrapperX<DppQualityTaskObjDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eq(DppQualityTaskObjDO::getTaskId, taskId)
                .eq(DppQualityTaskObjDO::getDelFlag,"0");
        return dppQualityTaskObjMapper.selectList(queryWrapperX);
    }

    @Override
    public List<DppQualityTaskObjDO> getDppQualityTaskObjList() {
        return dppQualityTaskObjMapper.selectList();
    }

    @Override
    public Map<Long, DppQualityTaskObjDO> getDppQualityTaskObjMap() {
        List<DppQualityTaskObjDO> dppQualityTaskObjList = dppQualityTaskObjMapper.selectList();
        return dppQualityTaskObjList.stream()
                .collect(Collectors.toMap(
                        DppQualityTaskObjDO::getId,
                        dppQualityTaskObjDO -> dppQualityTaskObjDO,
                        // Keep existing values
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data quality tasks-audit object data
         *
         * @param importExcelList Data quality task-audit object data list
         * @param isUpdateSupport Whether to update support, if it already exists, update the data
         * @param operName operating user
         * @return result
         */
        @Override
        public String importDppQualityTaskObj(List<DppQualityTaskObjRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("quality.error.import.empty", "导入数据不能为空！");
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
                                successMessages.add(MessageUtils.messageWithFallback("quality.import.update.success",
                                        "数据更新成功，ID为 " + dppQualityTaskObjId + " 的数据质量任务-稽查对象记录。", dppQualityTaskObjId, "数据质量任务-稽查对象"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.fail",
                                        "数据更新失败，ID为 " + dppQualityTaskObjId + " 的数据质量任务-稽查对象记录不存在。", dppQualityTaskObjId, "数据质量任务-稽查对象"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("quality.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DppQualityTaskObjDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dppQualityTaskObjId);
                        DppQualityTaskObjDO existingDppQualityTaskObj = dppQualityTaskObjMapper.selectOne(queryWrapper);
                        if (existingDppQualityTaskObj == null) {
                            dppQualityTaskObjMapper.insert(dppQualityTaskObjDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("quality.import.insert.success",
                                    "数据插入成功，ID为 " + dppQualityTaskObjId + " 的数据质量任务-稽查对象记录。", dppQualityTaskObjId, "数据质量任务-稽查对象"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("quality.import.insert.fail",
                                    "数据插入失败，ID为 " + dppQualityTaskObjId + " 的数据质量任务-稽查对象记录已存在。", dppQualityTaskObjId, "数据质量任务-稽查对象"));
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
