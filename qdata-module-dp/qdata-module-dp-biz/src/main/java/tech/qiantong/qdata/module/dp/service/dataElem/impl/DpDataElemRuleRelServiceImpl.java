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

package tech.qiantong.qdata.module.dp.service.dataElem.impl;

import cn.hutool.core.lang.Assert;
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
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRuleRelRespDTO;
import tech.qiantong.qdata.module.dp.api.service.dataElem.IDataElemRuleRelService;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRuleRelSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemRuleRelDO;
import tech.qiantong.qdata.module.dp.dal.mapper.dataElem.DpDataElemRuleRelMapper;
import tech.qiantong.qdata.module.dp.service.dataElem.IDpDataElemRuleRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Element Rule Relation Information Service Business Layer Processing
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpDataElemRuleRelServiceImpl extends ServiceImpl<DpDataElemRuleRelMapper, DpDataElemRuleRelDO> implements IDpDataElemRuleRelService, IDataElemRuleRelService {
    @Resource
    private DpDataElemRuleRelMapper dpDataElemRuleRelMapper;

    @Override
    public PageResult<DpDataElemRuleRelDO> getDpDataElemRuleRelPage(DpDataElemRuleRelPageReqVO pageReqVO) {
        return dpDataElemRuleRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDpDataElemRuleRel(DpDataElemRuleRelSaveReqVO createReqVO) {
        if ("1".equals(createReqVO.getType())) {
            Assert.notNull(createReqVO.getRuleType(), "ruleType null");
        }
        if (createReqVO.getStatus() == null) {
            createReqVO.setStatus("1");
        }
        DpDataElemRuleRelDO dictType = BeanUtils.toBean(createReqVO, DpDataElemRuleRelDO.class);
        dpDataElemRuleRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpDataElemRuleRel(DpDataElemRuleRelSaveReqVO updateReqVO) {
        if ("1".equals(updateReqVO.getType())) {
            Assert.notNull(updateReqVO.getRuleType(), "ruleType null");
        }
        if (updateReqVO.getStatus() == null) {
            updateReqVO.setStatus("1");
        }
        // Update data element rule relation information
        DpDataElemRuleRelDO updateObj = BeanUtils.toBean(updateReqVO, DpDataElemRuleRelDO.class);
        return dpDataElemRuleRelMapper.updateById(updateObj);
    }

    @Override
    public int removeDpDataElemRuleRel(Collection<Long> idList) {
        // Batch delete data element rule relation information
        return dpDataElemRuleRelMapper.deleteBatchIds(idList);
    }

    @Override
    public DpDataElemRuleRelDO getDpDataElemRuleRelById(Long id) {
        return dpDataElemRuleRelMapper.selectById(id);
    }

    @Override
    public List<DpDataElemRuleRelDO> getDpDataElemRuleRelList() {
        return dpDataElemRuleRelMapper.selectList();
    }

    @Override
    public Map<Long, DpDataElemRuleRelDO> getDpDataElemRuleRelMap() {
        List<DpDataElemRuleRelDO> dpDataElemRuleRelList = dpDataElemRuleRelMapper.selectList();
        return dpDataElemRuleRelList.stream()
                .collect(Collectors.toMap(
                        DpDataElemRuleRelDO::getId,
                        dpDataElemRuleRelDO -> dpDataElemRuleRelDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data element rule relation information data
     *
     * @param importExcelList Data element rule relation information data list
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName        Operator
     * @return Result
     */
    @Override
    public String importDpDataElemRuleRel(List<DpDataElemRuleRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dp.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DpDataElemRuleRelRespVO respVO : importExcelList) {
            try {
                DpDataElemRuleRelDO dpDataElemRuleRelDO = BeanUtils.toBean(respVO, DpDataElemRuleRelDO.class);
                Long dpDataElemRuleRelId = respVO.getId();
                if (isUpdateSupport) {
                    if (dpDataElemRuleRelId != null) {
                        DpDataElemRuleRelDO existingDpDataElemRuleRel = dpDataElemRuleRelMapper.selectById(dpDataElemRuleRelId);
                        if (existingDpDataElemRuleRel != null) {
                            dpDataElemRuleRelMapper.updateById(dpDataElemRuleRelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dp.import.update.success",
                                    "数据更新成功，ID为 " + dpDataElemRuleRelId + " 的数据元数据规则关联信息记录。", dpDataElemRuleRelId, "数据元数据规则关联信息"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.fail",
                                    "数据更新失败，ID为 " + dpDataElemRuleRelId + " 的数据元数据规则关联信息记录不存在。", dpDataElemRuleRelId, "数据元数据规则关联信息"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DpDataElemRuleRelDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dpDataElemRuleRelId);
                    DpDataElemRuleRelDO existingDpDataElemRuleRel = dpDataElemRuleRelMapper.selectOne(queryWrapper);
                    if (existingDpDataElemRuleRel == null) {
                        dpDataElemRuleRelMapper.insert(dpDataElemRuleRelDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dp.import.insert.success",
                                "数据插入成功，ID为 " + dpDataElemRuleRelId + " 的数据元数据规则关联信息记录。", dpDataElemRuleRelId, "数据元数据规则关联信息"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dp.import.insert.fail",
                                "数据插入失败，ID为 " + dpDataElemRuleRelId + " 的数据元数据规则关联信息记录已存在。", dpDataElemRuleRelId, "数据元数据规则关联信息"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("dp.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("dp.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("dp.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("dp.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public List<DpDataElemRuleRelRespDTO> listByDataElemIdList(Collection<Long> dataElemIdList, String type) {
        List<DpDataElemRuleRelDO> dpDataElemRuleRelDOS = baseMapper.listByDataElemIdList(dataElemIdList, type);
        return BeanUtils.toBean(dpDataElemRuleRelDOS, DpDataElemRuleRelRespDTO.class);
    }

}
