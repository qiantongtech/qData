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

package tech.qiantong.qdata.module.da.service.assetColumn.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.asset.DaAssetDO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetColumn.DaAssetColumnMapper;
import tech.qiantong.qdata.module.da.service.asset.IDaAssetService;
import tech.qiantong.qdata.module.da.service.assetColumn.IDaAssetColumnService;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelReqDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRespDTO;
import tech.qiantong.qdata.module.dp.api.service.dataElem.IDataElemRuleRelService;
import tech.qiantong.qdata.module.dp.api.service.model.IDpModelApiService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data Asset Column Service Business Layer Processing
 *
 * @author lhs
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetColumnServiceImpl extends ServiceImpl<DaAssetColumnMapper, DaAssetColumnDO> implements IDaAssetColumnService {
    @Resource
    private DaAssetColumnMapper daAssetColumnMapper;
    @Resource
    private IDpModelApiService iDpModelApiService;
    @Resource
    private IDataElemRuleRelService dataElemRuleRelService;
    @Resource
    private IDaAssetService daAssetService;


    @Override
    public AjaxResult getColumnByAssetId(DaAssetColumnPageReqVO pageReqVO) {
        if (StringUtils.isEmpty(pageReqVO.getAssetId())) {//Asset ID cannot be empty
            return AjaxResult.error(MessageUtils.messageWithFallback("da.error.asset.id.empty", "Asset ID cannot be empty"));
        }
        List<DaAssetColumnDO> list = this.lambdaQuery()
                .eq(DaAssetColumnDO::getAssetId, pageReqVO.getAssetId())
                .eq(DaAssetColumnDO::getDelFlag, 0)
                .orderByAsc(DaAssetColumnDO::getId)
                .list();

        for (DaAssetColumnDO daAssetColumnDO : list) {
            Set<Long> dpDataElemListByAssetIdApi = iDpModelApiService.getDpDataElemListByAssetIdAndColumnId(daAssetColumnDO.getAssetId(), daAssetColumnDO.getId());
            daAssetColumnDO.setElementId(dpDataElemListByAssetIdApi);
            if (dpDataElemListByAssetIdApi.size() > 0) {
                daAssetColumnDO.setCleanRuleList(dataElemRuleRelService.listByDataElemIdList(new ArrayList<>(dpDataElemListByAssetIdApi), "2"));
            }
        }
        return AjaxResult.success(list);
    }

    @Override
    public List<DaAssetColumnDO> getDaAssetColumnList(DaAssetColumnPageReqVO pageReqVO) {
        MPJLambdaWrapper<DaAssetColumnDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.eq(StringUtils.isNotEmpty(pageReqVO.getAssetId()), DaAssetColumnDO::getAssetId, pageReqVO.getAssetId())
                .eq(StringUtils.isNotEmpty(pageReqVO.getSensitiveLevelId()), DaAssetColumnDO::getSensitiveLevelId, pageReqVO.getSensitiveLevelId());
        return daAssetColumnMapper.selectList(lambdaWrapper);
    }

    @Override
    public PageResult<DaAssetColumnDO> getDaAssetColumnPage(DaAssetColumnPageReqVO pageReqVO) {
        if (StringUtils.isEmpty(pageReqVO.getAssetId())) {
            return PageResult.empty();
        }
        PageResult<DaAssetColumnDO> daAssetColumnDOPageResult = daAssetColumnMapper.selectPage(pageReqVO);
        Set<Long> ids = new HashSet<>();
        List<?> rows = daAssetColumnDOPageResult.getRows();
        for (Object row : rows) {
            DaAssetColumnDO daAssetColumnDO = (DaAssetColumnDO) row;
            ids.add(daAssetColumnDO.getDataElemCodeId());
        }
        List<DpDataElemRespDTO> dpDataElemListByAssetId = iDpModelApiService.getDpDataElemListByAssetId(Long.valueOf(pageReqVO.getAssetId()), ids);
        for (Object row : rows) {
            DaAssetColumnDO daAssetColumnDO = (DaAssetColumnDO) row;
            String elementName = "";
            for (DpDataElemRespDTO dpDataElemRespDTO : dpDataElemListByAssetId) {
                if (dpDataElemRespDTO.getId().equals(daAssetColumnDO.getDataElemCodeId())) {
                    daAssetColumnDO.setDataElemCodeName(dpDataElemRespDTO.getName());
                }
                if (dpDataElemRespDTO.getColumnId() != null && dpDataElemRespDTO.getColumnId().contains(daAssetColumnDO.getId())) {
            elementName = MessageUtils.messageWithFallback(
                    "da.label.multiple.items", "{0} and others", dpDataElemRespDTO.getName());
                }
            }
            daAssetColumnDO.setRelDataElmeName(elementName);
        }
        return daAssetColumnDOPageResult;
    }

    @Override
    public Long createDaAssetColumn(DaAssetColumnSaveReqVO createReqVO) {
        DaAssetColumnDO dictType = BeanUtils.toBean(createReqVO, DaAssetColumnDO.class);
        daAssetColumnMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateDaAssetColumn(DaAssetColumnSaveReqVO updateReqVO) {
        DaAssetDO daAssetDO = daAssetService.getById(updateReqVO.getAssetId());
        if (daAssetDO == null) {
            throw new ServiceException("da.error.asset.notfound", "Data asset does not exist");
        }
        // Maintain data element asset relation info table
        DpDataElemAssetRelReqDTO dpDataElemAssetRelReqDTO = new DpDataElemAssetRelReqDTO();
        dpDataElemAssetRelReqDTO.setTableName(daAssetDO.getTableName());
        dpDataElemAssetRelReqDTO.setColumnName(updateReqVO.getColumnName());
        dpDataElemAssetRelReqDTO.setColumnId(updateReqVO.getId());
        dpDataElemAssetRelReqDTO.setAssetId(daAssetDO.getId());
        dpDataElemAssetRelReqDTO.setElementIds(updateReqVO.getElementId());
        boolean b = iDpModelApiService.updateElementAssetRelation(dpDataElemAssetRelReqDTO);
//        if(!b){
//            throw new ServiceException("Data element and asset relation data update failed");
//        }
        // Not a code table, set the code table relation ID to null
        DaAssetColumnDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetColumnDO.class);
        if (StringUtils.isEmpty(updateObj.getDataElemCodeFlag()) || "0".equals(updateObj.getDataElemCodeFlag())) {
            updateObj.setDataElemCodeId(null);
        }
        // Update data asset column
        return daAssetColumnMapper.updateDaAssetColumn(updateObj);
    }

    @Override
    public int removeDaAssetColumn(Collection<Long> idList) {
        // Batch delete data asset columns
        return daAssetColumnMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetColumnDO getDaAssetColumnById(Long id) {
        DaAssetColumnDO daAssetColumnDO = daAssetColumnMapper.selectById(id);
        // Query data element id
        Set<Long> dpDataElemListByAssetIdApi = iDpModelApiService.getDpDataElemListByAssetIdApi(daAssetColumnDO.getId());
        daAssetColumnDO.setElementId(dpDataElemListByAssetIdApi);
        return daAssetColumnDO;
    }

    @Override
    public List<DaAssetColumnDO> getDaAssetColumnList() {
        return daAssetColumnMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetColumnDO> getDaAssetColumnMap() {
        List<DaAssetColumnDO> daAssetColumnList = daAssetColumnMapper.selectList();
        return daAssetColumnList.stream()
                .collect(Collectors.toMap(
                        DaAssetColumnDO::getId,
                        daAssetColumnDO -> daAssetColumnDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import data asset column data
     *
     * @param importExcelList Data asset column data list
     * @param isUpdateSupport Whether to update if already exists
     * @param operName        Operator user
     * @return Result
     */
    @Override
    public String importDaAssetColumn(List<DaAssetColumnRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetColumnRespVO respVO : importExcelList) {
            try {
                DaAssetColumnDO daAssetColumnDO = BeanUtils.toBean(respVO, DaAssetColumnDO.class);
                Long daAssetColumnId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetColumnId != null) {
                        DaAssetColumnDO existingDaAssetColumn = daAssetColumnMapper.selectById(daAssetColumnId);
                        if (existingDaAssetColumn != null) {
                            daAssetColumnMapper.updateById(daAssetColumnDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "Data update successful, ID {0} {1} record.", daAssetColumnId, MessageUtils.messageWithFallback("da.entity.asset.column", "Data asset column")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", daAssetColumnId, MessageUtils.messageWithFallback("da.entity.asset.column", "Data asset column")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DaAssetColumnDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetColumnId);
                    DaAssetColumnDO existingDaAssetColumn = daAssetColumnMapper.selectOne(queryWrapper);
                    if (existingDaAssetColumn == null) {
                        daAssetColumnMapper.insert(daAssetColumnDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", daAssetColumnId, MessageUtils.messageWithFallback("da.entity.asset.column", "Data asset column")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", daAssetColumnId, MessageUtils.messageWithFallback("da.entity.asset.column", "Data asset column")));
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
}
