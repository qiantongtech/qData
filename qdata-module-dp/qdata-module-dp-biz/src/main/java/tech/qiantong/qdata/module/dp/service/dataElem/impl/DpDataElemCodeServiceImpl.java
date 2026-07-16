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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodePageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodeRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemCodeSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemCodeDO;
import tech.qiantong.qdata.module.dp.dal.mapper.dataElem.DpDataElemCodeMapper;
import tech.qiantong.qdata.module.dp.service.dataElem.IDpDataElemCodeService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Element Code Service Business Layer Processing
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpDataElemCodeServiceImpl extends ServiceImpl<DpDataElemCodeMapper, DpDataElemCodeDO> implements IDpDataElemCodeService {
    @Resource
    private DpDataElemCodeMapper dpDataElemCodeMapper;

    @Override
    public PageResult<DpDataElemCodeDO> getDpDataElemCodePage(DpDataElemCodePageReqVO pageReqVO) {
        return dpDataElemCodeMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDpDataElemCode(DpDataElemCodeSaveReqVO createReqVO) {
        DpDataElemCodeDO dictType = BeanUtils.toBean(createReqVO, DpDataElemCodeDO.class);
        dpDataElemCodeMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpDataElemCode(DpDataElemCodeSaveReqVO updateReqVO) {
        // Related validation

        // Update data element code
        DpDataElemCodeDO updateObj = BeanUtils.toBean(updateReqVO, DpDataElemCodeDO.class);
        return dpDataElemCodeMapper.updateById(updateObj);
    }

    @Override
    public int removeDpDataElemCode(Collection<Long> idList) {
        // Batch delete data element code
        return dpDataElemCodeMapper.deleteBatchIds(idList);
    }

    @Override
    public DpDataElemCodeDO getDpDataElemCodeById(Long id) {
        return dpDataElemCodeMapper.selectById(id);
    }

    @Override
    public List<DpDataElemCodeDO> getDpDataElemCodeList() {
        return dpDataElemCodeMapper.selectList();
    }

    @Override
    public Map<Long, DpDataElemCodeDO> getDpDataElemCodeMap() {
        List<DpDataElemCodeDO> dpDataElemCodeList = dpDataElemCodeMapper.selectList();
        return dpDataElemCodeList.stream()
                .collect(Collectors.toMap(
                        DpDataElemCodeDO::getId,
                        dpDataElemCodeDO -> dpDataElemCodeDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data element code data
     *
     * @param importExcelList Data element code data list
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName        Operator
     * @return Result
     */
    @Override
    public String importDpDataElemCode(List<DpDataElemCodeRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dp.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DpDataElemCodeRespVO respVO : importExcelList) {
            try {
                DpDataElemCodeDO dpDataElemCodeDO = BeanUtils.toBean(respVO, DpDataElemCodeDO.class);
                Long dpDataElemCodeId = respVO.getId();
                if (isUpdateSupport) {
                    if (dpDataElemCodeId != null) {
                        DpDataElemCodeDO existingDpDataElemCode = dpDataElemCodeMapper.selectById(dpDataElemCodeId);
                        if (existingDpDataElemCode != null) {
                            dpDataElemCodeMapper.updateById(dpDataElemCodeDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dp.import.update.success",
                                    "数据更新成功，ID为 " + dpDataElemCodeId + " 的数据元代码记录。", dpDataElemCodeId, "数据元代码"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.fail",
                                    "数据更新失败，ID为 " + dpDataElemCodeId + " 的数据元代码记录不存在。", dpDataElemCodeId, "数据元代码"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DpDataElemCodeDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dpDataElemCodeId);
                    DpDataElemCodeDO existingDpDataElemCode = dpDataElemCodeMapper.selectOne(queryWrapper);
                    if (existingDpDataElemCode == null) {
                        dpDataElemCodeMapper.insert(dpDataElemCodeDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dp.import.insert.success",
                                "数据插入成功，ID为 " + dpDataElemCodeId + " 的数据元代码记录。", dpDataElemCodeId, "数据元代码"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dp.import.insert.fail",
                                "数据插入失败，ID为 " + dpDataElemCodeId + " 的数据元代码记录已存在。", dpDataElemCodeId, "数据元代码"));
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
    public Integer validateCodeValue(String dataElemId, String codeValue, String id) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(DpDataElemCodeDO.class)
                .ne(StringUtils.isNotBlank(id), DpDataElemCodeDO::getId, id)
                .eq(DpDataElemCodeDO::getDataElemId, dataElemId)
                .eq(DpDataElemCodeDO::getCodeValue, codeValue)) > 0 ? 0 : 1;
    }
}
