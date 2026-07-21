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

package tech.qiantong.qdata.module.dp.service.document.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import tech.qiantong.qdata.module.dp.api.service.document.IDpDocumentApiService;
import tech.qiantong.qdata.module.dp.controller.admin.document.vo.*;
import tech.qiantong.qdata.module.dp.dal.dataobject.document.DpDocumentDO;
import tech.qiantong.qdata.module.dp.dal.mapper.document.DpDocumentMapper;
import tech.qiantong.qdata.module.dp.service.document.IDpDocumentService;
import tech.qiantong.qdata.mybatis.core.util.MyBatisUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Standard Document Registration Service Business Layer Processing
 *
 * @author qdata
 * @date 2025-08-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpDocumentServiceImpl  extends ServiceImpl<DpDocumentMapper,DpDocumentDO> implements IDpDocumentService, IDpDocumentApiService {
    @Resource
    private DpDocumentMapper dpDocumentMapper;

    @Override
    public PageResult<DpDocumentDO> getDpDocumentPage(DpDocumentPageReqVO pageReqVO) {
        return dpDocumentMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DpDocumentDO> getDpDocumentList(DpDocumentPageReqVO reqVO) {
        MPJLambdaWrapper<DpDocumentDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DpDocumentDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("ATT_DOCUMENT_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .like(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getName()), DpDocumentDO::getName, reqVO.getName())
                .like(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getCode()), DpDocumentDO::getCode, reqVO.getCode())
                .and(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getKeyWordParam()),
                        q -> q.like(DpDocumentDO::getCode, reqVO.getKeyWordParam())
                                .or()
                                .like(DpDocumentDO::getName, reqVO.getKeyWordParam()))
                .like(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getKeyWordParam()), DpDocumentDO::getCode, reqVO.getKeyWordParam())
                .like(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getKeyWordParam()), DpDocumentDO::getName, reqVO.getKeyWordParam())
                .like(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getIssuingAgency()), DpDocumentDO::getIssuingAgency, reqVO.getIssuingAgency())
                .likeRight(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getCatCode()), DpDocumentDO::getCatCode, reqVO.getCatCode())
                .eq(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getType()),DpDocumentDO::getType, reqVO.getType())
                .eq(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getStatus()),DpDocumentDO::getStatus, reqVO.getStatus())
                .eq(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getVersion()),DpDocumentDO::getVersion, reqVO.getVersion());
        return dpDocumentMapper.selectList(lambdaWrapper);
    }

    @Override
    public Long createDpDocument(DpDocumentSaveReqVO createReqVO) {
        DpDocumentDO dictType = BeanUtils.toBean(createReqVO, DpDocumentDO.class);
        dpDocumentMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpDocument(DpDocumentSaveReqVO updateReqVO) {
        // Related validation

        // Update Standard Document Registration
        DpDocumentDO updateObj = BeanUtils.toBean(updateReqVO, DpDocumentDO.class);
        return dpDocumentMapper.updateById(updateObj);
    }
    @Override
    public int removeDpDocument(Collection<Long> idList) {
        // Batch Delete Standard Document Registration
        return dpDocumentMapper.deleteBatchIds(idList);
    }

    @Override
    public DpDocumentDO getDpDocumentById(Long id) {
        MPJLambdaWrapper<DpDocumentDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DpDocumentDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("ATT_DOCUMENT_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .eq( DpDocumentDO::getId, id);
        return dpDocumentMapper.selectOne(lambdaWrapper);
    }

    @Override
    public List<DpDocumentDO> getDpDocumentList() {
        return dpDocumentMapper.selectList();
    }

    @Override
    public Map<Long, DpDocumentDO> getDpDocumentMap() {
        List<DpDocumentDO> dpDocumentList = dpDocumentMapper.selectList();
        return dpDocumentList.stream()
                .collect(Collectors.toMap(
                        DpDocumentDO::getId,
                        dpDocumentDO -> dpDocumentDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import Standard Document Registration Data
         *
         * @param importExcelList Standard Document Registration Data List
         * @param isUpdateSupport Whether to support update, if exists then update the data
         * @param operName Operator
         * @return Result
         */
        @Override
        public String importDpDocument(List<DpDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("dp.error.import.empty", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DpDocumentRespVO respVO : importExcelList) {
                try {
                    DpDocumentDO dpDocumentDO = BeanUtils.toBean(respVO, DpDocumentDO.class);
                    Long dpDocumentId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dpDocumentId != null) {
                            DpDocumentDO existingDpDocument = dpDocumentMapper.selectById(dpDocumentId);
                            if (existingDpDocument != null) {
                                dpDocumentMapper.updateById(dpDocumentDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("dp.import.update.success",
                                        "Data update successful, ID {0} {1} record.", dpDocumentId, MessageUtils.messageWithFallback("dp.entity.standard.registration", "Standard information registration")));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.fail",
                                        "Data update failed, ID {0} {1} record does not exist.", dpDocumentId, MessageUtils.messageWithFallback("dp.entity.standard.registration", "Standard information registration")));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dp.import.update.id.missing",
                                    "Data update failed, record ID does not exist."));
                        }
                    } else {
                        QueryWrapper<DpDocumentDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dpDocumentId);
                        DpDocumentDO existingDpDocument = dpDocumentMapper.selectOne(queryWrapper);
                        if (existingDpDocument == null) {
                            dpDocumentMapper.insert(dpDocumentDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("dp.import.insert.success",
                                    "Data insert successful, ID {0} {1} record.", dpDocumentId, MessageUtils.messageWithFallback("dp.entity.standard.registration", "Standard information registration")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("dp.import.insert.fail",
                                    "Data insert failed, ID {0} {1} record already exists.", dpDocumentId, MessageUtils.messageWithFallback("dp.entity.standard.registration", "Standard information registration")));
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

    @Override
    public PageResult<DpDocumentSearchRespVO> getDpDocumentSearchPage(DpDocumentSearchReqVO dpDocument) {
        IPage<DpDocumentSearchRespVO> mpPage = dpDocumentMapper.getDpDocumentSearchPage(MyBatisUtils.buildPage(dpDocument),dpDocument);//BeanUtils.toBean(dppEtlTaskDOPageResult, DppEtlTaskRespVO.class);
        return new PageResult(mpPage.getRecords(), mpPage.getTotal());
    }

    @Override
    public Long getCountByCatCode(String catCode) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(DpDocumentDO.class)
                .likeRight(DpDocumentDO::getCatCode, catCode));
    }

}
