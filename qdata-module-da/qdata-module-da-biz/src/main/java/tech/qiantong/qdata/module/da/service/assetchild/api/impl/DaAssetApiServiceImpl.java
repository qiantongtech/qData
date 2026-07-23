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

package tech.qiantong.qdata.module.da.service.assetchild.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.httpClient.HeaderEntity;
import tech.qiantong.qdata.common.httpClient.HttpUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.api.service.assetchild.api.IDaApiOutService;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.api.DaAssetApiMapper;
import tech.qiantong.qdata.module.da.service.assetchild.api.IDaAssetApiService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data Asset - External API Service Business Layer
 *
 * @author qdata
 * @date 2025-04-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetApiServiceImpl  extends ServiceImpl<DaAssetApiMapper,DaAssetApiDO> implements IDaAssetApiService, IDaApiOutService {
    @Resource
    private DaAssetApiMapper daAssetApiMapper;

    @Override
    public PageResult<DaAssetApiDO> getDaAssetApiPage(DaAssetApiPageReqVO pageReqVO) {
        return daAssetApiMapper.selectPage(pageReqVO);
    }

    @Override
    public DaAssetApiRespVO getDaAssetApiByAssetId(Long assetId) {
        LambdaQueryWrapperX<DaAssetApiDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eqIfPresent(DaAssetApiDO::getAssetId,assetId);
        DaAssetApiDO daAssetApiDO = daAssetApiMapper.selectOne(queryWrapperX);
        return BeanUtils.toBean(daAssetApiDO, DaAssetApiRespVO.class);
    }

    @Override
    public Long createDaAssetApi(DaAssetApiSaveReqVO createReqVO) {
        DaAssetApiDO dictType = BeanUtils.toBean(createReqVO, DaAssetApiDO.class);
        daAssetApiMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetApi(DaAssetApiSaveReqVO updateReqVO) {
        // Validation

        // Update Data Asset - External API
        DaAssetApiDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetApiDO.class);
        return daAssetApiMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetApi(Collection<Long> idList) {
        // Batch delete Data Asset - External API
        return daAssetApiMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetApiDO getDaAssetApiById(Long id) {
        return daAssetApiMapper.selectById(id);
    }

    @Override
    public List<DaAssetApiDO> getDaAssetApiList() {
        return daAssetApiMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetApiDO> getDaAssetApiMap() {
        List<DaAssetApiDO> daAssetApiList = daAssetApiMapper.selectList();
        return daAssetApiList.stream()
                .collect(Collectors.toMap(
                        DaAssetApiDO::getId,
                        daAssetApiDO -> daAssetApiDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import Data Asset - External API data
     *
     * @param importExcelList Data Asset - External API data list
     * @param isUpdateSupport Whether to support update; if already exists, update the data
     * @param operName Operating user
     * @return Result
     */
    @Override
    public String importDaAssetApi(List<DaAssetApiRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetApiRespVO respVO : importExcelList) {
            try {
                DaAssetApiDO daAssetApiDO = BeanUtils.toBean(respVO, DaAssetApiDO.class);
                Long daAssetApiId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetApiId != null) {
                        DaAssetApiDO existingDaAssetApi = daAssetApiMapper.selectById(daAssetApiId);
                        if (existingDaAssetApi != null) {
                            daAssetApiMapper.updateById(daAssetApiDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "Data update successful, ID {0} {1} record.", daAssetApiId, MessageUtils.messageWithFallback("da.entity.asset.external.api", "Data asset external API")));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "Data update failed, ID {0} {1} record does not exist.", daAssetApiId, MessageUtils.messageWithFallback("da.entity.asset.external.api", "Data asset external API")));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "Data update failed, record ID does not exist."));
                    }
                } else {
                    QueryWrapper<DaAssetApiDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetApiId);
                    DaAssetApiDO existingDaAssetApi = daAssetApiMapper.selectOne(queryWrapper);
                    if (existingDaAssetApi == null) {
                        daAssetApiMapper.insert(daAssetApiDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "Data insert successful, ID {0} {1} record.", daAssetApiId, MessageUtils.messageWithFallback("da.entity.asset.external.api", "Data asset external API")));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "Data insert failed, ID {0} {1} record already exists.", daAssetApiId, MessageUtils.messageWithFallback("da.entity.asset.external.api", "Data asset external API")));
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

    @Override
    public void queryServiceForwarding(HttpServletResponse response, DaAssetApiReqVO daAssetApi) {
        this.executeServiceForwarding(response,daAssetApi.getId(),daAssetApi.getQueryParams());
    }

    @Override
    public void executeServiceForwarding(HttpServletResponse response, Long apiId, Map<String, Object> queryParams) {
        // Get third-party API configuration by ID
        DaAssetApiDO daAssetApiById = this.getDaAssetApiById(apiId);

        // Check API info, e.g. whether it is enabled
        chackYapiConfig(daAssetApiById);

        // Extract URL
        String url = daAssetApiById.getUrl();

        // Package headers
        List<HeaderEntity> headerEntities = packHeadersOrYApiField(queryParams);
        // Call the third-party API
        try {
            // Extract the request method
            String reqMethod = daAssetApiById.getHttpMethod();
            // Extract input parameters
            Map<String, Object> params = ( Map<String, Object>)MapUtils.getMap(queryParams, "params", new HashMap<>());
            // GET
            if (StringUtils.equals(HttpUtils.GET, reqMethod)) {// Package GET request
                HttpUtils.sendGet(HttpUtils.packGetRequestURL(url, params), response, headerEntities);
            } else if (StringUtils.equals(HttpUtils.POST, reqMethod)) {// POST
                HttpUtils.sendPost(url, params, response, headerEntities);
            } else {// Unknown
                throw new DataQueryException("db.error.api.type", "Wrong API type");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DataQueryException("db.error.api.http", "HTTP request failed");
        }
    }

    private void chackYapiConfig(DaAssetApiDO daAssetApiById) {
        // Check if null
        if (daAssetApiById == null) {
            throw new DataQueryException("db.error.api.config.missing", "API call failed, API configuration not found");
        }
    }


    /**
     * Package Headers
     *
     * @param queryParams
     * @return
     */
    public static List<HeaderEntity> packHeadersOrYApiField(Map<String, Object> queryParams) {
        List<Map<String,Object>> fieldHerderList = (List<Map<String,Object>>)MapUtils.getObject(queryParams, "fieldHerderList", new ArrayList<>());

        // Package headers
        List<HeaderEntity> headerEntityList = new ArrayList<>();
        if(CollectionUtils.isEmpty(fieldHerderList)){
            return headerEntityList;
        }

        for (Map<String, Object> stringObjectMap : fieldHerderList) {
            if(MapUtils.isNotEmpty(stringObjectMap)){
                HeaderEntity headerEntity = new HeaderEntity();
                headerEntity.setKey(MapUtils.getString(stringObjectMap,"name"));
                String defaultValue = MapUtils.getString(stringObjectMap, "defaultValue");
                if(defaultValue == null){
                    throw new DataQueryException("db.error.api.header.null", "Header cannot be null");
                }
                headerEntity.setValue(defaultValue);
                headerEntityList.add(headerEntity);
            }
        }
        return headerEntityList;
    }

}
