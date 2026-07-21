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

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dp.api.service.dataElem.IDataElemApiService;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemDO;
import tech.qiantong.qdata.module.dp.dal.dataobject.document.DpDocumentDO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelDO;
import tech.qiantong.qdata.module.dp.dal.mapper.dataElem.DpDataElemMapper;
import tech.qiantong.qdata.module.dp.service.dataElem.IDpDataElemService;
import tech.qiantong.qdata.module.dp.service.document.IDpDocumentService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * Data Element Service Business Layer Processing
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpDataElemServiceImpl extends ServiceImpl<DpDataElemMapper, DpDataElemDO> implements IDpDataElemService, IDataElemApiService {
    @Resource
    private DpDataElemMapper dpDataElemMapper;

    @Resource
    private IDpDocumentService dpDocumentService;

    @Override
    public PageResult<DpDataElemDO> getDpDataElemPage(DpDataElemPageReqVO pageReqVO) {
        return dpDataElemMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DpDataElemDO> getDpDataElemList(DpDataElemPageReqVO reqVO) {
        LambdaQueryWrapperX<DpDataElemDO> queryWrapper = new LambdaQueryWrapperX<>();
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        queryWrapper.likeIfPresent(DpDataElemDO::getName, reqVO.getName())
                .likeIfPresent(DpDataElemDO::getEngName, reqVO.getEngName())
                .eqIfPresent(DpDataElemDO::getCatCode, reqVO.getCatCode())
                .eqIfPresent(DpDataElemDO::getType, reqVO.getType())
                .eqIfPresent(DpDataElemDO::getPersonCharge, reqVO.getPersonCharge())
                .eqIfPresent(DpDataElemDO::getContactNumber, reqVO.getContactNumber())
                .eqIfPresent(DpDataElemDO::getColumnType, reqVO.getColumnType())
                .eqIfPresent(DpDataElemDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DpDataElemDO::getDocumentId, reqVO.getDocumentId())
                .eqIfPresent(DpDataElemDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DpDataElemDO::getCreateTime, reqVO.getCreateTime())
                // If reqVO.getName() is not empty, add an exact match condition for name (name = '<name>')
                // .likeIfPresent(DpDataElemDO::getName, reqVO.getName())
                // Sort descending by createTime field
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns);

        return dpDataElemMapper.selectList(queryWrapper);
    }

    @Override
    public Long createDpDataElem(DpDataElemSaveReqVO createReqVO) {
        DpDataElemDO dictType = BeanUtils.toBean(createReqVO, DpDataElemDO.class);
        dpDataElemMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpDataElem(DpDataElemSaveReqVO updateReqVO) {
        // Related validation

        // Update data element
        DpDataElemDO updateObj = BeanUtils.toBean(updateReqVO, DpDataElemDO.class);
        return dpDataElemMapper.updateById(updateObj);
    }

    @Override
    public int removeDpDataElem(List<Long> idList) {
        // Check whether the current data element is used by models or assets
        Long count = dpDataElemMapper.checkHasRel(idList);
        if (count > 0) {
            throw new ServiceException("dp.error.elem.ref", "Data element is referenced by model or asset, please remove the association first");
        }
        // Batch delete data element
        return dpDataElemMapper.deleteBatchIds(idList);
    }

    @Override
    public DpDataElemDO getDpDataElemById(Long id) {
        MPJLambdaWrapper<DpDataElemDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DpDataElemDO.class)
                .select("t2.NAME AS catName","t3.NICK_NAME AS personChargeName")
                .leftJoin("ATT_DATA_ELEM_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER t3 on t.PERSON_CHARGE = t3.USER_ID AND t3.DEL_FLAG = '0'")
                .eq(DpDataElemDO::getId, id);
        DpDataElemDO dpDataElemDO = dpDataElemMapper.selectJoinOne(DpDataElemDO.class, lambdaWrapper);

        if(dpDataElemDO.getDocumentId() != null){
            DpDocumentDO dpDocumentById = dpDocumentService.getDpDocumentById(dpDataElemDO.getDocumentId());
            dpDocumentById = dpDocumentById == null ? new DpDocumentDO():dpDocumentById;

            dpDataElemDO.setDocumentCode(dpDocumentById.getCode());
            dpDataElemDO.setDocumentName(dpDocumentById.getName());
            dpDataElemDO.setDocumentType(dpDocumentById.getType());
        }

        return dpDataElemDO;
    }

    @Override
    public List<DpDataElemDO> getDpDataElemList() {
        return dpDataElemMapper.selectList();
    }

    @Override
    public Map<Long, DpDataElemDO> getDpDataElemMap() {
        List<DpDataElemDO> dpDataElemList = dpDataElemMapper.selectList();
        return dpDataElemList.stream()
                .collect(Collectors.toMap(
                        DpDataElemDO::getId,
                        dpDataElemDO -> dpDataElemDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data element data
     *
     * @param importExcelList Data element data list
     * @param isUpdateSupport Whether to support update, if exists then update the data
     * @param operName        Operator
     * @return Result
     */
    @Override
    public String importDpDataElem(List<DpDataElemRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("dp.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DpDataElemRespVO respVO : importExcelList) {
            try {
                DpDataElemDO dpDataElemDO = BeanUtils.toBean(respVO, DpDataElemDO.class);
                Long dpDataElemId = respVO.getId();
                if (isUpdateSupport) {
                    if (dpDataElemId != null) {
                        DpDataElemDO existingDpDataElem = dpDataElemMapper.selectById(dpDataElemId);
                        if (existingDpDataElem != null) {
                            dpDataElemMapper.updateById(dpDataElemDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dp.import.update.success",
                                    "Data update successful, ID {0} {1} record.", dpDataElemId, MessageUtils.messageWithFallback("dp.entity.data.element", "Data element")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", dpDataElemId, MessageUtils.messageWithFallback("dp.entity.data.element", "Data element")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DpDataElemDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dpDataElemId);
                    DpDataElemDO existingDpDataElem = dpDataElemMapper.selectOne(queryWrapper);
                    if (existingDpDataElem == null) {
                        dpDataElemMapper.insert(dpDataElemDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("dp.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", dpDataElemId, MessageUtils.messageWithFallback("dp.entity.data.element", "Data element")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("dp.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", dpDataElemId, MessageUtils.messageWithFallback("dp.entity.data.element", "Data element")));
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateStatus(Long id, Long status) {
        return this.update(Wrappers.lambdaUpdate(DpDataElemDO.class)
                .eq(DpDataElemDO::getId, id)
                .set(DpDataElemDO::getStatus, status));
    }

    @Override
    public Long getCountByCatCode(String catCode) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(DpDataElemDO.class)
                .likeRight(DpDataElemDO::getCatCode, catCode));
    }
}
