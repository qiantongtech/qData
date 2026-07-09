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
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeLogDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlNodeLogMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeLogService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Integration Node Log Service Business Layer Processing
 *
 * @author qdata
 * @date 2025-02-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlNodeLogServiceImpl extends ServiceImpl<DppEtlNodeLogMapper, DppEtlNodeLogDO> implements IDppEtlNodeLogService {
    @Resource
    private DppEtlNodeLogMapper dppEtlNodeLogMapper;

    @Override
    public PageResult<DppEtlNodeLogDO> getDppEtlNodeLogPage(DppEtlNodeLogPageReqVO pageReqVO) {
        return dppEtlNodeLogMapper.selectPage(pageReqVO);
    }

    @Override
    public DppEtlNodeLogDO getDppEtlNodeLogRespVOByReqVO(DppEtlNodeLogPageReqVO reqVO) {

        MPJLambdaWrapper<DppEtlNodeLogDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppEtlNodeLogDO.class)
                .eq(StringUtils.isNotEmpty(reqVO.getCode()), DppEtlNodeLogDO::getCode, reqVO.getCode())
                .eq(reqVO.getVersion() != null, DppEtlNodeLogDO::getVersion, reqVO.getVersion())
                .eq(reqVO.getDsId() != null, DppEtlNodeLogDO::getDsId, reqVO.getDsId());
        DppEtlNodeLogDO dppEtlNodeLogDO = dppEtlNodeLogMapper.selectOne(wrapper);
        return dppEtlNodeLogDO;
    }

    @Override
    public Long createDppEtlNodeLog(DppEtlNodeLogSaveReqVO createReqVO) {
        DppEtlNodeLogDO dictType = BeanUtils.toBean(createReqVO, DppEtlNodeLogDO.class);
        dppEtlNodeLogMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public DppEtlNodeLogDO createDppEtlNodeLogNew(DppEtlNodeLogSaveReqVO dppEtlNodeLogSaveReqVO) {

        DppEtlNodeLogDO dictType = BeanUtils.toBean(dppEtlNodeLogSaveReqVO, DppEtlNodeLogDO.class);
        dppEtlNodeLogMapper.insert(dictType);
        return dictType;
    }

    @Override
    public List<DppEtlNodeLogDO> createDppEtlNodeLogBatch(List<DppEtlNodeLogSaveReqVO> dppEtlNodeLogSaveReqVOS) {
        List<DppEtlNodeLogDO> dppEtlNodeDOList = new ArrayList<>();
        for (DppEtlNodeLogSaveReqVO dppEtlNodeSaveReqVO : dppEtlNodeLogSaveReqVOS) {
            DppEtlNodeLogDO dictType = BeanUtils.toBean(dppEtlNodeSaveReqVO, DppEtlNodeLogDO.class);
            dictType.setId(null);
            dppEtlNodeLogMapper.insert(dictType);
            dppEtlNodeDOList.add(dictType);
        }
        return dppEtlNodeDOList;
    }

    @Override
    public int updateDppEtlNodeLog(DppEtlNodeLogSaveReqVO updateReqVO) {
        // Related validation

        // Update data integration node log
        DppEtlNodeLogDO updateObj = BeanUtils.toBean(updateReqVO, DppEtlNodeLogDO.class);
        return dppEtlNodeLogMapper.updateById(updateObj);
    }

    @Override
    public int removeDppEtlNodeLog(Collection<Long> idList) {
        // Batch delete data integration node log
        return dppEtlNodeLogMapper.deleteBatchIds(idList);
    }

    @Override
    public DppEtlNodeLogDO getDppEtlNodeLogById(Long id) {
        return dppEtlNodeLogMapper.selectById(id);
    }

    @Override
    public List<DppEtlNodeLogDO> getDppEtlNodeLogList() {
        return dppEtlNodeLogMapper.selectList();
    }

    @Override
    public Map<Long, DppEtlNodeLogDO> getDppEtlNodeLogMap() {
        List<DppEtlNodeLogDO> dppEtlNodeLogList = dppEtlNodeLogMapper.selectList();
        return dppEtlNodeLogList.stream()
                .collect(Collectors.toMap(
                        DppEtlNodeLogDO::getId,
                        dppEtlNodeLogDO -> dppEtlNodeLogDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data integration node log data
     *
     * @param importExcelList data integration node log data list
     * @param isUpdateSupport whether update is supported, if already exists, update the data
     * @param operName        operator name
     * @return result
     */
    @Override
    public String importDppEtlNodeLog(List<DppEtlNodeLogRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dpp.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DppEtlNodeLogRespVO respVO : importExcelList) {
            try {
                DppEtlNodeLogDO dppEtlNodeLogDO = BeanUtils.toBean(respVO, DppEtlNodeLogDO.class);
                Long dppEtlNodeLogId = respVO.getId();
                if (isUpdateSupport) {
                    if (dppEtlNodeLogId != null) {
                        DppEtlNodeLogDO existingDppEtlNodeLog = dppEtlNodeLogMapper.selectById(dppEtlNodeLogId);
                        if (existingDppEtlNodeLog != null) {
                            dppEtlNodeLogMapper.updateById(dppEtlNodeLogDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dpp.import.update.success",
                                    "数据更新成功，ID为 " + dppEtlNodeLogId + " 的数据集成节点-日志记录。", dppEtlNodeLogId, "数据集成节点-日志"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.fail",
                                    "数据更新失败，ID为 " + dppEtlNodeLogId + " 的数据集成节点-日志记录不存在。", dppEtlNodeLogId, "数据集成节点-日志"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DppEtlNodeLogDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dppEtlNodeLogId);
                    DppEtlNodeLogDO existingDppEtlNodeLog = dppEtlNodeLogMapper.selectOne(queryWrapper);
                    if (existingDppEtlNodeLog == null) {
                        dppEtlNodeLogMapper.insert(dppEtlNodeLogDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.success",
                                "数据插入成功，ID为 " + dppEtlNodeLogId + " 的数据集成节点-日志记录。", dppEtlNodeLogId, "数据集成节点-日志"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dpp.import.insert.fail",
                                "数据插入失败，ID为 " + dppEtlNodeLogId + " 的数据集成节点-日志记录已存在。", dppEtlNodeLogId, "数据集成节点-日志"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("dpp.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("dpp.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("dpp.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public DppEtlNodeLogDO getByNodeCodeAndVersion(String nodeCode, Integer version) {
        DppEtlNodeLogDO dppEtlNodeLogDO = baseMapper.selectOne(Wrappers.lambdaQuery(DppEtlNodeLogDO.class)
                .eq(DppEtlNodeLogDO::getCode, nodeCode)
                .eq(DppEtlNodeLogDO::getVersion, version));
        return dppEtlNodeLogDO;
    }

    @Override
    public Integer getMaxVersionByNodeCode(String nodeCode) {
        return baseMapper.getMaxVersionByNodeCode(nodeCode);
    }

    @Override
    public List<DppEtlNodeLogDO> listByTaskCode(String taskCode, Integer version) {
        MPJLambdaWrapper<DppEtlNodeLogDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DppEtlNodeLogDO.class)
                .innerJoin("DPP_ETL_TASK_NODE_REL_LOG t2 ON ((t.CODE = t2.PRE_NODE_CODE AND t.VERSION = t2.PRE_NODE_VERSION) OR (t.CODE = t2.POST_NODE_CODE AND t.VERSION = t2.POST_NODE_VERSION)) AND t2.DEL_FLAG = '0' AND t2.TASK_CODE =" + taskCode + " AND  t2.TASK_VERSION  =" + version)
                .distinct();
        List<DppEtlNodeLogDO> dppEtlTaskNodeRelDOS = dppEtlNodeLogMapper.selectList(wrapper);
        return dppEtlTaskNodeRelDOS;
    }
}
