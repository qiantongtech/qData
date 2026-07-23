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

package tech.qiantong.qdata.module.dp.service.model.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelColumnDO;
import tech.qiantong.qdata.module.dp.dal.mapper.model.DpModelColumnMapper;
import tech.qiantong.qdata.module.dp.service.model.IDpModelColumnService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Logical Model Column Information Service Business Layer Processing
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpModelColumnServiceImpl extends ServiceImpl<DpModelColumnMapper, DpModelColumnDO>
        implements IDpModelColumnService {
    @Resource
    private DpModelColumnMapper dpModelColumnMapper;

    @Override
    public PageResult<DpModelColumnDO> getDpModelColumnPage(DpModelColumnPageReqVO pageReqVO) {
        return dpModelColumnMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDpModelColumn(DpModelColumnSaveReqVO createReqVO) {
        DpModelColumnDO dictType = BeanUtils.toBean(createReqVO, DpModelColumnDO.class);
        dpModelColumnMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpModelColumn(DpModelColumnSaveReqVO updateReqVO) {
        // Related validation

        // Update logical model column information
        DpModelColumnDO updateObj = BeanUtils.toBean(updateReqVO, DpModelColumnDO.class);
        return dpModelColumnMapper.updateById(updateObj);
    }

    @Override
    public int removeDpModelColumn(Collection<Long> idList) {
        // Batch delete logical model column information
        return dpModelColumnMapper.deleteBatchIds(idList);
    }

    @Override
    public int removeDpModelColumnByModelId(Collection<Long> modelIdList) {
        return dpModelColumnMapper.delete(new LambdaQueryWrapperX<DpModelColumnDO>()
                .in(DpModelColumnDO::getModelId, modelIdList));
    }

    @Override
    public DpModelColumnDO getDpModelColumnById(Long id) {
        return dpModelColumnMapper.selectById(id);
    }

    @Override
    public List<DpModelColumnDO> getDpModelColumnList() {
        return dpModelColumnMapper.selectList();
    }

    @Override
    public List<DpModelColumnDO> getDpModelColumnList(DpModelColumnSaveReqVO reqVO) {
        DpModelColumnPageReqVO dpModelColumnPageReqVO = BeanUtils.toBean(reqVO, DpModelColumnPageReqVO.class);
        MPJLambdaWrapper<DpModelColumnDO> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(DpModelColumnDO.class)
                .select("t2.NAME AS dataElemName")
                .leftJoin("DP_DATA_ELEM t2 on t.DATA_ELEM_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .eq(dpModelColumnPageReqVO.getModelId() != null, DpModelColumnDO::getModelId,
                        dpModelColumnPageReqVO.getModelId())
                .like(StringUtils.isNotBlank(dpModelColumnPageReqVO.getEngName()), DpModelColumnDO::getEngName,
                        dpModelColumnPageReqVO.getEngName())
                .like(StringUtils.isNotBlank(dpModelColumnPageReqVO.getCnName()), DpModelColumnDO::getCnName,
                        dpModelColumnPageReqVO.getCnName())
                .eq(StringUtils.isNotBlank(dpModelColumnPageReqVO.getColumnType()), DpModelColumnDO::getColumnType,
                        dpModelColumnPageReqVO.getColumnType())
                .eq(dpModelColumnPageReqVO.getColumnLength() != null, DpModelColumnDO::getColumnLength,
                        dpModelColumnPageReqVO.getColumnLength())
                .eq(dpModelColumnPageReqVO.getColumnScale() != null, DpModelColumnDO::getColumnScale,
                        dpModelColumnPageReqVO.getColumnScale())
                .eq(StringUtils.isNotBlank(dpModelColumnPageReqVO.getDefaultValue()), DpModelColumnDO::getDefaultValue,
                        dpModelColumnPageReqVO.getDefaultValue())
                .eq(dpModelColumnPageReqVO.getPkFlag() != null, DpModelColumnDO::getPkFlag,
                        dpModelColumnPageReqVO.getPkFlag())
                .eq(dpModelColumnPageReqVO.getNullableFlag() != null, DpModelColumnDO::getNullableFlag,
                        dpModelColumnPageReqVO.getNullableFlag())
                .eq(dpModelColumnPageReqVO.getSortOrder() != null, DpModelColumnDO::getSortOrder,
                        dpModelColumnPageReqVO.getSortOrder())
                .eq(StringUtils.isNotBlank(dpModelColumnPageReqVO.getAuthorityDept()),
                        DpModelColumnDO::getAuthorityDept, dpModelColumnPageReqVO.getAuthorityDept())
                .eq(dpModelColumnPageReqVO.getDataElemId() != null, DpModelColumnDO::getDataElemId,
                        dpModelColumnPageReqVO.getDataElemId())
                .eq(dpModelColumnPageReqVO.getCreateTime() != null, DpModelColumnDO::getCreateTime,
                        dpModelColumnPageReqVO.getCreateTime());
        return dpModelColumnMapper.selectJoinList(DpModelColumnDO.class, wrapper);
    }

    @Override
    public long countByDpModelColumn(DpModelColumnSaveReqVO reqVO) {
        LambdaQueryWrapperX<DpModelColumnDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfPresent(DpModelColumnDO::getModelId, reqVO.getModelId())
                .likeIfPresent(DpModelColumnDO::getEngName, reqVO.getEngName())
                .likeIfPresent(DpModelColumnDO::getCnName, reqVO.getCnName())
                .eqIfPresent(DpModelColumnDO::getColumnType, reqVO.getColumnType())
                .eqIfPresent(DpModelColumnDO::getColumnLength, reqVO.getColumnLength())
                .eqIfPresent(DpModelColumnDO::getColumnScale, reqVO.getColumnScale())
                .eqIfPresent(DpModelColumnDO::getDefaultValue, reqVO.getDefaultValue())
                .eqIfPresent(DpModelColumnDO::getPkFlag, reqVO.getPkFlag())
                .eqIfPresent(DpModelColumnDO::getNullableFlag, reqVO.getNullableFlag())
                .eqIfPresent(DpModelColumnDO::getSortOrder, reqVO.getSortOrder())
                .eqIfPresent(DpModelColumnDO::getAuthorityDept, reqVO.getAuthorityDept())
                .eqIfPresent(DpModelColumnDO::getDataElemId, reqVO.getDataElemId())
                .eqIfPresent(DpModelColumnDO::getCreateTime, reqVO.getCreateTime());
        return dpModelColumnMapper.selectCount(queryWrapper);
    }

    @Override
    public Map<Long, DpModelColumnDO> getDpModelColumnMap() {
        List<DpModelColumnDO> dpModelColumnList = dpModelColumnMapper.selectList();
        return dpModelColumnList.stream()
                .collect(Collectors.toMap(
                        DpModelColumnDO::getId,
                        dpModelColumnDO -> dpModelColumnDO,
                        // Keep existing value
                        (existing, replacement) -> existing));
    }

    /**
     * Import logical model column information data
     *
     * @param importExcelList Logical model column information data list
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName        Operator
     * @return Result
     */
    @Override
    public String importDpModelColumn(List<DpModelColumnRespVO> importExcelList, boolean isUpdateSupport,
            String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dp.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DpModelColumnRespVO respVO : importExcelList) {
            try {
                DpModelColumnDO dpModelColumnDO = BeanUtils.toBean(respVO, DpModelColumnDO.class);
                Long dpModelColumnId = respVO.getId();
                if (isUpdateSupport) {
                    if (dpModelColumnId != null) {
                        DpModelColumnDO existingDpModelColumn = dpModelColumnMapper.selectById(dpModelColumnId);
                        if (existingDpModelColumn != null) {
                            dpModelColumnMapper.updateById(dpModelColumnDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dp.import.update.success",
                                    "Data update successful, ID {0} {1} record.", dpModelColumnId, MessageUtils.messageWithFallback("dp.entity.logical.model.attribute", "Logical model attribute")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", dpModelColumnId, MessageUtils.messageWithFallback("dp.entity.logical.model.attribute", "Logical model attribute")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DpModelColumnDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dpModelColumnId);
                    DpModelColumnDO existingDpModelColumn = dpModelColumnMapper.selectOne(queryWrapper);
                    if (existingDpModelColumn == null) {
                        dpModelColumnMapper.insert(dpModelColumnDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dp.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", dpModelColumnId, MessageUtils.messageWithFallback("dp.entity.logical.model.attribute", "Logical model attribute")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dp.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", dpModelColumnId, MessageUtils.messageWithFallback("dp.entity.logical.model.attribute", "Logical model attribute")));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("dp.import.error.detail",
                "Data import failed, error: {0}", e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("dp.import.result.fail",
                    "Import failed! {0} records have incorrect format, errors:<br/>{1}",
                    failureNum, failureDetails));
            throw new ServiceException("dp.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("dp.import.result.success",
                    "Congratulations! All data imported! Total: {0} records.", successNum));
        }
        return resultMsg.toString();
    }

    /**
     * Batch insert logical model column information data
     *
     * @param dpModelColumnList Logical model column information data list
     * @return Result
     */
    @Override
    public Boolean createDpModelColumnList(List<DpModelColumnSaveReqVO> dpModelColumnList) {
        List<DpModelColumnDO> dpModelColumnDOList = BeanUtils.toBean(dpModelColumnList, DpModelColumnDO.class);
        for (DpModelColumnDO dpModelColumnDO : dpModelColumnDOList) {
            dpModelColumnMapper.insert(dpModelColumnDO);
        }
        // Boolean aBoolean = dpModelColumnMapper.insertBatch(dpModelColumnDOList);
        return true;
    }

    /**
     * Batch update and insert logical model column information data
     *
     * @param dpModelColumnList Logical model column information data list
     * @return Result
     */
    @Override
    public Boolean updateDpModelColumnList(List<DpModelColumnSaveReqVO> dpModelColumnList) {
        List<DpModelColumnDO> dpModelColumnDOList = BeanUtils.toBean(dpModelColumnList, DpModelColumnDO.class);
        Long modelId = dpModelColumnDOList.get(0) == null ? null : dpModelColumnDOList.get(0).getModelId();
        DpModelColumnSaveReqVO dpModelColumnSaveReqVO = new DpModelColumnSaveReqVO();
        dpModelColumnSaveReqVO.setModelId(modelId);
        List<DpModelColumnDO> modelColumnList = this.getDpModelColumnList(dpModelColumnSaveReqVO);
        // Used to store all IDs from dpModelColumnDOList
        Set<Long> newIds = new HashSet<>();
        for (DpModelColumnDO dpModelColumnDO : dpModelColumnDOList) {
            if (dpModelColumnDO.getId() != null) {
                dpModelColumnMapper.updateById(dpModelColumnDO);
                newIds.add(dpModelColumnDO.getId());
            } else {
                dpModelColumnMapper.insert(dpModelColumnDO);
            }
        }
        // Delete records that exist in modelColumnList but not in dpModelColumnDOList
        for (DpModelColumnDO existingColumn : modelColumnList) {
            if (!newIds.contains(existingColumn.getId())) {
                dpModelColumnMapper.deleteById(existingColumn.getId());
            }
        }
        return true;
    }
}
