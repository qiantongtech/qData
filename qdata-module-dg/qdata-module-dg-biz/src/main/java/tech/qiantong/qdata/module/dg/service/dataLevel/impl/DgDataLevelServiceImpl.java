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

package tech.qiantong.qdata.module.dg.service.dataLevel.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelRespVO;
import tech.qiantong.qdata.module.dg.controller.admin.dataLevel.vo.DgDataLevelSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.dataLevel.DgDataLevelDO;
import tech.qiantong.qdata.module.dg.dal.mapper.dataLevel.DgDataLevelMapper;
import tech.qiantong.qdata.module.dg.service.dataLevel.IDgDataLevelService;
/**
 * Data Level Service Business Layer Processing
 *
 * @author qdata
 * @date 2026-04-03
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DgDataLevelServiceImpl  extends ServiceImpl<DgDataLevelMapper,DgDataLevelDO> implements IDgDataLevelService {
    @Resource
    private DgDataLevelMapper dgDataLevelMapper;

    @Override
    public PageResult<DgDataLevelDO> getDgDataLevelPage(DgDataLevelPageReqVO pageReqVO) {
        return dgDataLevelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDgDataLevel(DgDataLevelSaveReqVO createReqVO) {
        DgDataLevelDO dictType = BeanUtils.toBean(createReqVO, DgDataLevelDO.class);
        // Sensitive level cannot be duplicated
        if (dgDataLevelMapper.selectCount(new LambdaQueryWrapper<DgDataLevelDO>()
                .eq(DgDataLevelDO::getSensitiveLevel, dictType.getSensitiveLevel())) > 0) {
            throw new ServiceException("dg.error.duplicate.level", "敏感等级不能重复");
        }
        dgDataLevelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDgDataLevel(DgDataLevelSaveReqVO updateReqVO) {
        // Related validation

        // Update data level
        DgDataLevelDO updateObj = BeanUtils.toBean(updateReqVO, DgDataLevelDO.class);
        return dgDataLevelMapper.updateById(updateObj);
    }
    @Override
    public int removeDgDataLevel(Collection<Long> idList) {
        // Batch delete data levels
        return dgDataLevelMapper.deleteBatchIds(idList);
    }

    @Override
    public DgDataLevelDO getDgDataLevelById(Long id) {
        return dgDataLevelMapper.selectById(id);
    }

    @Override
    public List<DgDataLevelDO> getDgDataLevelList() {
        return dgDataLevelMapper.selectList();
    }

    @Override
    public Map<Long, DgDataLevelDO> getDgDataLevelMap() {
        List<DgDataLevelDO> dgDataLevelList = dgDataLevelMapper.selectList();
        return dgDataLevelList.stream()
                .collect(Collectors.toMap(
                        DgDataLevelDO::getId,
                        dgDataLevelDO -> dgDataLevelDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data level data
         *
         * @param importExcelList Data level data list
         * @param isUpdateSupport Whether to update support, if exists then update data
         * @param operName Operator user
         * @return Result
         */
        @Override
        public String importDgDataLevel(List<DgDataLevelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dg.error.import.empty", "导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DgDataLevelRespVO respVO : importExcelList) {
                try {
                    DgDataLevelDO dgDataLevelDO = BeanUtils.toBean(respVO, DgDataLevelDO.class);
                    Long dgDataLevelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dgDataLevelId != null) {
                            DgDataLevelDO existingDgDataLevel = dgDataLevelMapper.selectById(dgDataLevelId);
                            if (existingDgDataLevel != null) {
                                dgDataLevelMapper.updateById(dgDataLevelDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dg.import.update.success",
                                        "数据更新成功，ID为 " + dgDataLevelId + " 的数据分级记录。", dgDataLevelId, "数据分级"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.fail",
                                        "数据更新失败，ID为 " + dgDataLevelId + " 的数据分级记录不存在。", dgDataLevelId, "数据分级"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.update.id.missing",
                                    "数据更新失败，某条记录的ID不存在。"));
                        }
                    } else {
                        QueryWrapper<DgDataLevelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dgDataLevelId);
                        DgDataLevelDO existingDgDataLevel = dgDataLevelMapper.selectOne(queryWrapper);
                        if (existingDgDataLevel == null) {
                            dgDataLevelMapper.insert(dgDataLevelDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dg.import.insert.success",
                                    "数据插入成功，ID为 " + dgDataLevelId + " 的数据分级记录。", dgDataLevelId, "数据分级"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dg.import.insert.fail",
                                    "数据插入失败，ID为 " + dgDataLevelId + " 的数据分级记录已存在。", dgDataLevelId, "数据分级"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("dg.import.error.detail",
                            "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.fail",
                        "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("dg.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("dg.import.result.success",
                        "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
            }
            return resultMsg.toString();
        }

    @Override
    public List<DgDataLevelDO> getDgDataLevelListAll(DgDataLevelPageReqVO dgDataLevel) {
        return dgDataLevelMapper.selectList();
    }
}
