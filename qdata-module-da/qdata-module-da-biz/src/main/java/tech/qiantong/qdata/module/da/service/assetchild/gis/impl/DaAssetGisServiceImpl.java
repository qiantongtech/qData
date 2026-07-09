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

package tech.qiantong.qdata.module.da.service.assetchild.gis.impl;

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
import tech.qiantong.qdata.module.da.api.service.assetchild.gis.IDaAssetGisOutService;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.gis.DaAssetGisDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.gis.DaAssetGisMapper;
import tech.qiantong.qdata.module.da.service.assetchild.gis.IDaAssetGisService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data Asset - Geospatial Service business layer processing
 *
 * @author qdata
 * @date 2025-04-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetGisServiceImpl  extends ServiceImpl<DaAssetGisMapper,DaAssetGisDO> implements IDaAssetGisService, IDaAssetGisOutService {
    @Resource
    private DaAssetGisMapper daAssetGisMapper;

    @Override
    public PageResult<DaAssetGisDO> getDaAssetGisPage(DaAssetGisPageReqVO pageReqVO) {
        return daAssetGisMapper.selectPage(pageReqVO);
    }

    @Override
    public DaAssetGisRespVO getDaAssetGisByAssetId(Long assetId) {
        LambdaQueryWrapperX<DaAssetGisDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eqIfPresent(DaAssetGisDO::getAssetId,assetId);
        DaAssetGisDO daAssetApiDO = daAssetGisMapper.selectOne(queryWrapperX);
        return BeanUtils.toBean(daAssetApiDO, DaAssetGisRespVO.class);
    }

    @Override
    public Long createDaAssetGis(DaAssetGisSaveReqVO createReqVO) {
        DaAssetGisDO dictType = BeanUtils.toBean(createReqVO, DaAssetGisDO.class);
        daAssetGisMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetGis(DaAssetGisSaveReqVO updateReqVO) {
        // Related validation

        // Update data asset - geospatial service
        DaAssetGisDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetGisDO.class);
        return daAssetGisMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetGis(Collection<Long> idList) {
        // Batch delete data asset - geospatial service
        return daAssetGisMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetGisDO getDaAssetGisById(Long id) {
        return daAssetGisMapper.selectById(id);
    }

    @Override
    public List<DaAssetGisDO> getDaAssetGisList() {
        return daAssetGisMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetGisDO> getDaAssetGisMap() {
        List<DaAssetGisDO> daAssetGisList = daAssetGisMapper.selectList();
        return daAssetGisList.stream()
                .collect(Collectors.toMap(
                        DaAssetGisDO::getId,
                        daAssetGisDO -> daAssetGisDO,
                        // Keep existing value
                        (existing, replacement) -> existing
                ));
    }


        /**
         * Import data asset - geospatial service data
         *
         * @param importExcelList Data asset - geospatial service data list
         * @param isUpdateSupport Whether to support update, if already exists, update the data
         * @param operName Operator user
         * @return Result
         */
        @Override
        public String importDaAssetGis(List<DaAssetGisRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("da.error.import.empty", "Import data cannot be empty!", "Import data cannot be empty!");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DaAssetGisRespVO respVO : importExcelList) {
                try {
                    DaAssetGisDO daAssetGisDO = BeanUtils.toBean(respVO, DaAssetGisDO.class);
                    Long daAssetGisId = respVO.getId();
                    if (isUpdateSupport) {
                        if (daAssetGisId != null) {
                            DaAssetGisDO existingDaAssetGis = daAssetGisMapper.selectById(daAssetGisId);
                            if (existingDaAssetGis != null) {
                                daAssetGisMapper.updateById(daAssetGisDO);
                                successNum++;
                                successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                        "Data updated successfully, data asset - geospatial service record with ID " + daAssetGisId + ".", daAssetGisId, "Data Asset - Geospatial Service"));
                            } else {
                                failureNum++;
                                failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                        "Data update failed, data asset - geospatial service record with ID " + daAssetGisId + " does not exist.", daAssetGisId, "Data Asset - Geospatial Service"));
                            }
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                    "Data update failed, ID of a record is missing."));
                        }
                    } else {
                        QueryWrapper<DaAssetGisDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daAssetGisId);
                        DaAssetGisDO existingDaAssetGis = daAssetGisMapper.selectOne(queryWrapper);
                        if (existingDaAssetGis == null) {
                            daAssetGisMapper.insert(daAssetGisDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                    "Data inserted successfully, data asset - geospatial service record with ID " + daAssetGisId + ".", daAssetGisId, "Data Asset - Geospatial Service"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                    "Data insert failed, data asset - geospatial service record with ID " + daAssetGisId + " already exists.", daAssetGisId, "Data Asset - Geospatial Service"));
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "Data import failed, error message: " + e.getMessage(), e.getMessage());
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                String failureDetails = String.join("<br/>", failureMessages);
                resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                        "Sorry, import failed! A total of " + failureNum + " records had incorrect format, errors are as follows: <br/>" + failureDetails,
                        failureNum, failureDetails));
                throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
            } else {
                resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                        "Congratulations! All data has been imported successfully! A total of " + successNum + " records.", successNum));
            }
            return resultMsg.toString();
        }

    @Override
    public void queryServiceForwarding(HttpServletResponse response, DaAssetGisReqVO daAssetGisReqVO) {
        this.executeServiceForwarding(response,daAssetGisReqVO.getId(),daAssetGisReqVO.getQueryParams());
    }

    @Override
    public void executeServiceForwarding(HttpServletResponse response, Long gisId, Map<String, Object> queryParams) {
        // Get third-party API config by ID
        DaAssetGisDO daAssetGisDO = this.getDaAssetGisById(gisId);

        // Validate API info, e.g. whether it is enabled
        chackYapiConfig(daAssetGisDO);

        // Extract URL
        String url = daAssetGisDO.getUrl();

        // Package headers
        List<HeaderEntity> headerEntities = packHeadersOrYApiField(queryParams);
        // Invoke third-party API
        try {
            // Extract request method
            String reqMethod = daAssetGisDO.getHttpMethod();
            // Extract input parameters
            Map<String, Object> params = ( Map<String, Object>) MapUtils.getMap(queryParams, "params", new HashMap<>());
            this.fillDefaultWmtsParams(params,reqMethod);
            // GET
            if (StringUtils.equals(HttpUtils.GET, reqMethod)) {// Package GET request
                HttpUtils.sendGet(HttpUtils.packGetRequestURL(url, params), response, headerEntities);
            } else if (StringUtils.equals(HttpUtils.POST, reqMethod)) {// POST
                HttpUtils.sendPost(url, params, response, headerEntities);
            } else {// Unknown
                throw new DataQueryException("db.error.api.type", "API type error");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DataQueryException("db.error.api.http", "HTTP invocation failed");
        }
    }

    private void chackYapiConfig(DaAssetGisDO daAssetGisDO) {
        // Check if null
        if (daAssetGisDO == null) {
            throw new DataQueryException("db.error.api.config.missing", "API call, API config not found");
        }
    }

    /**
     * Explicitly check for missing WMTS parameters and fill defaults
     * @param params Input parameter Map, will be modified
     */
    private void fillDefaultWmtsParams(Map<String, Object> params,String reqMethod ) {
        String service = MapUtils.getString(params, "service");
        if (StringUtils.isBlank(service)) {
            params.put("service", "WMTS");
        }

        String request = MapUtils.getString(params, "request");
        if (StringUtils.isBlank(request)) {
            params.put("request", "GetCapabilities");
        }

        String version = MapUtils.getString(params, "version");
        if (StringUtils.isBlank(version)) {
            params.put("version", "1.0.0");
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
                    throw new DataQueryException("db.error.api.header.null", "Header value cannot be null");
                }
                headerEntity.setValue(defaultValue);
                headerEntityList.add(headerEntity);
            }
        }
        return headerEntityList;
    }
}
