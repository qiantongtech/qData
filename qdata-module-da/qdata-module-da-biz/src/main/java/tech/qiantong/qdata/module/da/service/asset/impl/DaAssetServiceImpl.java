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

package tech.qiantong.qdata.module.da.service.asset.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.config.AniviaConfig;
import tech.qiantong.qdata.common.constant.CacheConstants;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.TreeData;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.core.redis.RedisCache;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbDialect;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.DialectFactory;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.FileInfo;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.ExcelToCsvUtil;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.PageUtil;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.file.FileDataReaderUtil;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.service.cat.tagRel.IAttTagAssetRelApiService;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetReqDTO;
import tech.qiantong.qdata.module.da.api.asset.dto.DaAssetRespDTO;
import tech.qiantong.qdata.module.da.api.service.asset.IDaAssetApiOutService;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetRespVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnRelRuleVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiParamSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.files.vo.DaAssetFilesSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTableSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.asset.DaAssetDO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.file.DaAssetFileDO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.files.DaAssetFilesDO;
import tech.qiantong.qdata.module.da.dal.dataobject.daAssetApply.DaAssetApplyDO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTaskDO;
import tech.qiantong.qdata.module.da.dal.dataobject.sensitiveLevel.DaSensitiveLevelDO;
import tech.qiantong.qdata.module.da.dal.mapper.asset.DaAssetMapper;
import tech.qiantong.qdata.module.da.dal.mapper.assetColumn.DaAssetColumnMapper;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.file.DaAssetFileMapper;
import tech.qiantong.qdata.module.da.dal.mapper.daAssetApply.DaAssetApplyMapper;
import tech.qiantong.qdata.module.da.dal.mapper.datasource.DaDatasourceMapper;
import tech.qiantong.qdata.module.da.dal.mapper.sensitiveLevel.DaSensitiveLevelMapper;
import tech.qiantong.qdata.module.da.service.asset.IDaAssetService;
import tech.qiantong.qdata.module.da.service.assetColumn.IDaAssetColumnService;
import tech.qiantong.qdata.module.da.service.assetchild.api.IDaAssetApiParamService;
import tech.qiantong.qdata.module.da.service.assetchild.api.IDaAssetApiService;
import tech.qiantong.qdata.module.da.service.assetchild.files.IDaAssetFilesService;
import tech.qiantong.qdata.module.da.service.assetchild.geo.IDaAssetGeoService;
import tech.qiantong.qdata.module.da.service.assetchild.gis.IDaAssetGisService;
import tech.qiantong.qdata.module.da.service.assetchild.projectRel.IDaAssetProjectRelService;
import tech.qiantong.qdata.module.da.service.assetchild.theme.IDaAssetThemeRelService;
import tech.qiantong.qdata.module.da.service.assetchild.video.IDaAssetVideoService;
import tech.qiantong.qdata.module.da.service.datasource.IDaDatasourceService;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryTableService;
import tech.qiantong.qdata.module.da.service.discovery.IDaDiscoveryTaskService;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeList.DgDesensitizeAssetcolumnDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeIntervalDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.desensitizeRules.DgDesensitizeRuleDO;
import tech.qiantong.qdata.module.dg.dal.dataobject.whitelist.DgDesensitizeWhitelistDO;
import tech.qiantong.qdata.module.dg.service.desensitizeList.IDgDesensitizeAssetcolumnService;
import tech.qiantong.qdata.module.dg.service.desensitizeRules.IDgDesensitizeRuleService;
import tech.qiantong.qdata.module.dg.service.whitelist.IDgDesensitizeWhitelistService;
import tech.qiantong.qdata.module.dm.api.service.businessCategory.IDmBusinessCategoryApiService;
import tech.qiantong.qdata.module.dm.api.service.dataLayer.IDmDataLayerApiService;
import tech.qiantong.qdata.module.dm.api.service.themeDomain.IDmThemeDomainApiService;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelReqDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelRespDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRespDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRuleRelRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelColumnRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelRespDTO;
import tech.qiantong.qdata.module.dp.api.service.dataElem.IDataElemRuleRelService;
import tech.qiantong.qdata.module.dp.api.service.model.IDpModelApiService;
import tech.qiantong.qdata.module.dpp.api.service.etl.DppEtlTaskInstanceService;
import tech.qiantong.qdata.module.dpp.api.service.etl.DppEtlTaskService;
import tech.qiantong.qdata.module.mc.api.column.dto.McColumnRespDTO;
import tech.qiantong.qdata.module.mc.api.service.column.McColumnApiService;
import tech.qiantong.qdata.module.system.domain.vo.ColumnRespVO;
import tech.qiantong.qdata.mybatis.config.MasterDataSourceConfig;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.neo4j.dto.LineageDTO;
import tech.qiantong.qdata.neo4j.node.TaskNode;
import tech.qiantong.qdata.neo4j.service.LineageDataService;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Data Asset Service - Business Layer Processing
 *
 * @author lhs
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetServiceImpl extends ServiceImpl<DaAssetMapper, DaAssetDO> implements IDaAssetService, IDaAssetApiOutService {
    @Value("${ds.resource_url:}")
    private String resourceUrl;
    @Value("${qdata.profile}")
    private String profile;
    @Resource
    private DaAssetMapper daAssetMapper;

    @Resource
    private DaDatasourceMapper daDatasourceMapper;

    @Resource
    private IDpModelApiService iDpModelApiService;

    @Resource
    private IDaAssetColumnService iDaAssetColumnService;

    @Resource
    private IDataElemRuleRelService elemRuleRelService;

    @Resource
    private IDaDatasourceService iDaDatasourceService;

    @Resource
    private DaAssetColumnMapper daAssetColumnMapper;

    @Resource
    private DaSensitiveLevelMapper daSensitiveLevelMapper;
    @Resource
    private DaAssetFileMapper assetFileMapper;

    @Autowired
    private DataSourceFactory dataSourceFactory;

    @Resource
    private IDaDiscoveryTaskService daDiscoveryTaskService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private DaAssetApplyMapper daAssetApplyMapper;

    @Resource
    private IDaDiscoveryTableService daDiscoveryTableService;

    @Resource
    private DppEtlTaskService dppEtlTaskService;
    @Resource
    private IDaAssetThemeRelService daAssetThemeRelService;
    @Resource
    private IDaAssetApiService iDaAssetApiService;
    @Resource
    private IDaAssetApiParamService iDaAssetApiParamService;
    @Resource
    private IDaAssetProjectRelService iDaAssetProjectRelService;
    @Resource
    private IDaAssetGeoService iDaAssetGeoService;
    @Resource
    private IDaAssetGisService iDaAssetGisService;
    @Resource
    private IDaAssetVideoService iDaAssetVideoService;
    @Resource
    private IDaAssetFilesService daAssetFilesService;
    @Resource
    private IAttTagAssetRelApiService attTagAssetRelApiService;

    @Resource
    private LineageDataService lineageDataService;

    @Resource
    private DppEtlTaskInstanceService dppEtlTaskInstanceService;

    @Resource
    private IDmThemeDomainApiService dmThemeDomainApiService;

    @Resource
    private IDmBusinessCategoryApiService dmBusinessCategoryApiService;

    @Resource
    private IDmDataLayerApiService dmDataLayerApiService;

    @Resource
    private McColumnApiService mcColumnApiService;

    // Import desensitization list association relationship
    @Resource
    private IDgDesensitizeAssetcolumnService dgDesensitizeAssetcolumnService;
    // Import desensitization rules
    @Resource
    private IDgDesensitizeRuleService dgDesensitizeRuleService;

    // Import whitelist
    @Resource
    private IDgDesensitizeWhitelistService whitelistService;

    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(".xlsx", ".xls", ".csv");

    /**
     * @param daAssetReqDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DaAssetRespDTO insertDaAsset(DaAssetReqDTO daAssetReqDTO) {
        // Query info by model id
        DpModelRespDTO dpModelByIdApi = iDpModelApiService.getDpModelByIdApi(daAssetReqDTO.getModelId());
        if (dpModelByIdApi == null) {
            throw new ServiceException("da.error.model.notfound", "Model does not exist");
        }
        DaAssetDO daAssetDO = new DaAssetDO();
        daAssetDO.setName(dpModelByIdApi.getModelComment());
        daAssetDO.setCatCode(dpModelByIdApi.getCatCode());
        daAssetDO.setDatasourceId(daAssetReqDTO.getDatasourceId());
        daAssetDO.setSource(daAssetReqDTO.getSource());
        daAssetDO.setTableName(dpModelByIdApi.getTableName());
        daAssetDO.setTableComment(dpModelByIdApi.getModelComment());
        daAssetDO.setFieldCount(daAssetReqDTO.getFieldCount());// Field count

        // Read model data and set it
        daAssetDO.setTableType(dpModelByIdApi.getTableType());
        daAssetDO.setDataLayerId(dpModelByIdApi.getDataLayerId());
        daAssetDO.setBusinessCategoryId(dpModelByIdApi.getBusinessCategoryId());
        daAssetDO.setBusinessCategoryCode(dpModelByIdApi.getBusinessCategoryCode());
        daAssetDO.setDataDomainId(dpModelByIdApi.getDataDomainId());
        daAssetDO.setThemeDomainId(dpModelByIdApi.getThemeDomainId());
        daAssetDO.setThemeDomainCode(dpModelByIdApi.getThemeDomainCode());
        daAssetDO.setTableCase(dpModelByIdApi.getTableCase());

        // Check if asset exists
        DaAssetPageReqVO daAssetPageReqVO = new DaAssetPageReqVO();
        daAssetPageReqVO.setTableName(dpModelByIdApi.getTableName());
        daAssetPageReqVO.setDatasourceId(String.valueOf(daAssetReqDTO.getDatasourceId()));
        DaAssetDO assetDO = this.getDaAssetByDaAssetPageReqVO(daAssetPageReqVO);
        if (assetDO != null) {
            daAssetDO.setId(assetDO.getId());
            daAssetMapper.updateById(daAssetDO);// Update asset data
            // Delete field cache
            redisCache.deleteObject(CacheConstants.ASSET_PREVIEW_KEY + daAssetReqDTO.getId() + "_" + dpModelByIdApi.getTableName());
        } else {
            daAssetMapper.insert(daAssetDO);// Add asset data
        }

        // Query logical model attributes
        List<DpModelColumnRespDTO> dpModelColumnListByModelIdApi = iDpModelApiService.getDpModelColumnListByModelIdApi(daAssetReqDTO.getModelId());
        List<DaAssetColumnDO> daAssetColumnDOList = new ArrayList<>();

        List<DaAssetColumnDO> daAssetColumnList = new ArrayList<>();
        if (assetDO != null) {
            DaAssetColumnPageReqVO daAssetColumnPageReqVO = new DaAssetColumnPageReqVO();
            daAssetColumnPageReqVO.setAssetId(String.valueOf(assetDO.getId()));
            List<DaAssetColumnDO> daAssetColumnList1 = iDaAssetColumnService.getDaAssetColumnList(daAssetColumnPageReqVO);
            daAssetColumnList = CollectionUtils.isEmpty(daAssetColumnList1) ? daAssetColumnList : daAssetColumnList1;
        }
        if (StringUtils.isNotEmpty(dpModelColumnListByModelIdApi)) {
            for (DpModelColumnRespDTO dpModelColumnRespDTO : dpModelColumnListByModelIdApi) {
                DaAssetColumnDO daAssetColumnDO = new DaAssetColumnDO();

                DaAssetColumnDO columnDO = matchColumn(daAssetColumnList, dpModelColumnRespDTO);
                if (columnDO != null) {
                    daAssetColumnDO.setId(columnDO.getId());
                }

                daAssetColumnDO.setAssetId(daAssetDO.getId());
                daAssetColumnDO.setDataElemCodeId(dpModelColumnRespDTO.getDataElemId());
                daAssetColumnDO.setColumnName(dpModelColumnRespDTO.getEngName());
                daAssetColumnDO.setColumnLength(dpModelColumnRespDTO.getColumnLength());
                daAssetColumnDO.setColumnScale(dpModelColumnRespDTO.getColumnScale());
                daAssetColumnDO.setColumnType(dpModelColumnRespDTO.getColumnType());
                daAssetColumnDO.setColumnComment(dpModelColumnRespDTO.getCnName());
                daAssetColumnDO.setDefaultValue(dpModelColumnRespDTO.getDefaultValue());
                daAssetColumnDO.setNullableFlag(dpModelColumnRespDTO.getNullableFlag());
                daAssetColumnDO.setPkFlag(dpModelColumnRespDTO.getPkFlag());
                daAssetColumnDOList.add(daAssetColumnDO);
            }
        }
        // Batch save data asset fields
//        iDaAssetColumnService.saveBatch(daAssetColumnDOList);
        for (DaAssetColumnDO daAssetColumnDO : daAssetColumnDOList) {
            if (daAssetColumnDO.getId() == null) {
                iDaAssetColumnService.save(daAssetColumnDO);
            } else {
                iDaAssetColumnService.updateById(daAssetColumnDO);
            }
        }
        Collection<Long> nonExistingIdList = this.findNonExistingIdList(daAssetColumnDOList, daAssetColumnList);
        if (CollectionUtils.isNotEmpty(nonExistingIdList)) {
            iDaAssetColumnService.removeDaAssetColumn(nonExistingIdList);
        }

        // Set data element and asset association info
        Set<Long> ids = dpModelColumnListByModelIdApi.stream()
                .map(DpModelColumnRespDTO::getDataElemId)
                .collect(Collectors.toSet());
        // ID data is not empty
        if (StringUtils.isNotEmpty(ids)) {
            List<DpDataElemRespDTO> dpDataElemListByIdsApi = iDpModelApiService.getDpDataElemListByIdsApi(ids);
            List<DpDataElemAssetRelReqDTO> dpDataElemAssetRel = new ArrayList<>();
            dpDataElemListByIdsApi.forEach(dpDataElemRespDTO -> {
                DpDataElemAssetRelReqDTO dpDataElemAssetRelReqDTO = new DpDataElemAssetRelReqDTO();
                // Set asset id
                dpDataElemAssetRelReqDTO.setAssetId(daAssetDO.getId());
                dpDataElemAssetRelReqDTO.setDataElemType(dpDataElemRespDTO.getType());
                dpDataElemAssetRelReqDTO.setTableName(dpModelByIdApi.getModelName());
                dpDataElemAssetRelReqDTO.setColumnName(dpDataElemRespDTO.getEngName());
                dpDataElemAssetRelReqDTO.setDataElemId(dpDataElemRespDTO.getId());
                Optional<DaAssetColumnDO> first = daAssetColumnDOList.stream()
                        .filter(daAssetColumnDO -> daAssetColumnDO.getDataElemCodeId() != null && daAssetColumnDO.getDataElemCodeId()
                                .equals(dpDataElemRespDTO.getId()))
                        .findFirst();
                first.ifPresent(daAssetColumnDO -> dpDataElemAssetRelReqDTO.setColumnId(daAssetColumnDO.getId()));

                dpDataElemAssetRel.add(dpDataElemAssetRelReqDTO);
            });
            if (StringUtils.isNotEmpty(dpDataElemListByIdsApi)) {
                boolean b = iDpModelApiService.insertElementAssetRelation(dpDataElemAssetRel);
                if (!b) {
                    throw new ServiceException("da.error.elem.save", "Data element and asset association info save failed");
                }
            }
        }
        DaAssetRespDTO result = new DaAssetRespDTO();
        result.setId(daAssetDO.getId());// Asset id
        return result;
    }

    /**
     * Use stream processing to find records that exist in daAssetColumnList but not in daAssetColumnDOList.
     * Matching is based on columnName (using StringUtils.equals comparison), returning the id set of those records.
     *
     * @param daAssetColumnDOList Existing record list
     * @param daAssetColumnList   Record list to check
     * @return Matched id collection
     */
    public static Collection<Long> findNonExistingIdList(List<DaAssetColumnDO> daAssetColumnDOList, List<DaAssetColumnDO> daAssetColumnList) {
        // Extract all non-null columnNames from the existing list into a Set
        Set<String> existingNames = daAssetColumnDOList == null ? null : daAssetColumnDOList.stream()
                                                                         .filter(asset -> StringUtils.isNotBlank(asset.getColumnName()))
                                                                         .map(DaAssetColumnDO::getColumnName)
                                                                         .collect(Collectors.toSet());

        // Filter the pending match list, keeping records whose columnName is not in existingNames, and collect their ids
        return daAssetColumnList == null ? null : daAssetColumnList.stream()
                                                  .filter(asset -> StringUtils.isNotBlank(asset.getColumnName()))
                                                  .filter(asset -> existingNames == null || existingNames.stream()
                                                                                            .noneMatch(name -> StringUtils.equals(name, asset.getColumnName())))
                                                  .map(DaAssetColumnDO::getId)
                                                  .collect(Collectors.toList());
    }

    /**
     * Match the corresponding DaAssetColumnDO object in daAssetColumnList based on dpModelColumnRespDTO's engName
     *
     * @param daAssetColumnList    Data asset field list
     * @param dpModelColumnRespDTO Model column response DTO, containing engName attribute
     * @return Matched DaAssetColumnDO object, returns null if no match
     */
    public static DaAssetColumnDO matchColumn(List<DaAssetColumnDO> daAssetColumnList, DpModelColumnRespDTO dpModelColumnRespDTO) {
        if (daAssetColumnList == null || dpModelColumnRespDTO == null || dpModelColumnRespDTO.getEngName() == null) {
            return null;
        }
        for (DaAssetColumnDO daAssetColumnDO : daAssetColumnList) {
            // When field names match, return the object
            if (dpModelColumnRespDTO.getEngName().equals(daAssetColumnDO.getColumnName())) {
                return daAssetColumnDO;
            }
        }
        return null;
    }

    @Override
    public Long getCountByCatCode(String catCode) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(DaAssetDO.class).likeRight(DaAssetDO::getCatCode, catCode));
    }

    @Override
    public PageResult<DaAssetRespDTO> daAssetListPage(DaAssetReqDTO daAssetReqDTO) {
        DaAssetPageReqVO daAssetPageReqVO = BeanUtils.toBean(daAssetReqDTO, DaAssetPageReqVO.class);
        return BeanUtils.toBean(this.getDaAssetPage(daAssetPageReqVO, "1"), DaAssetRespDTO.class);
    }


    @Override
    public List<DaAssetDO> getTablesByDataSourceId(DaAssetPageReqVO pageReqVO) {
        if (StringUtils.isEmpty(pageReqVO.getDatasourceId())) {
            throw new ServiceException("da.error.datasource.id.empty", "Datasource ID cannot be empty");
        }
        return this.lambdaQuery()
                .eq(DaAssetDO::getDatasourceId, pageReqVO.getDatasourceId())
                .eq(DaAssetDO::getDelFlag, "0")
                .list();
    }

    @Override
    public DaAssetDO getDaAssetByDaAssetPageReqVO(DaAssetPageReqVO pageReqVO) {
        MPJLambdaWrapper<DaAssetDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.eq(StringUtils.isNotEmpty(pageReqVO.getName()), DaAssetDO::getName, pageReqVO.getName())
                .eq(pageReqVO.getId() != null, DaAssetDO::getId, pageReqVO.getId())
                .eq(StringUtils.isNotEmpty(pageReqVO.getTableName()), DaAssetDO::getTableName, pageReqVO.getTableName())
                .eq(StringUtils.isNotEmpty(pageReqVO.getDatasourceId()), DaAssetDO::getDatasourceId, pageReqVO.getDatasourceId())
                .eq(StringUtils.isNotEmpty(pageReqVO.getTableComment()), DaAssetDO::getTableComment, pageReqVO.getTableComment());
        return baseMapper.selectOne(lambdaWrapper);
    }

    public List<Long> extractDistinctAssetIds(List<DaAssetThemeRelRespVO> vos) {
        if (CollectionUtils.isEmpty(vos)) {
            return new ArrayList<>();
        }
        return vos.stream()
                .map(DaAssetThemeRelRespVO::getAssetId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 1-Data Asset
     * 2-Data Development
     */
    @Override
    public PageResult<DaAssetDO> getDaAssetPage(DaAssetPageReqVO pageReqVO, String daAssetQueryType) {
        PageResult<DaAssetDO> daAssetDOPageResult = daAssetMapper.selectPage(pageReqVO);
        List<DaAssetDO> daAssetDOList = (List<DaAssetDO>) daAssetDOPageResult.getRows();
        for (DaAssetDO daAssetDO : daAssetDOList) {
            // Check if it's an API
            if (StringUtils.equals("2", daAssetDO.getType())) {
                DaAssetApiRespVO daAssetApiByAssetId = iDaAssetApiService.getDaAssetApiByAssetId(daAssetDO.getId());
                daAssetDO.setDaAssetApi(daAssetApiByAssetId);
            }
//            // Check if it's a datasource
//            if (StringUtils.equals("1", daAssetDO.getType())) {
//                DaDatasourceDO daDatasourceById = iDaDatasourceService.getDaDatasourceById(daAssetDO.getDatasourceId());
//                daDatasourceById = daDatasourceById == null ? new DaDatasourceDO() : daDatasourceById;
//
//                daAssetDO.setDatasourceName(daDatasourceById.getDatasourceName());
//                daAssetDO.setDatasourceType(daDatasourceById.getDatasourceType());
//            }
        }
        daAssetDOPageResult.setRows(daAssetDOList);
        return daAssetDOPageResult;
    }

    @Override
    public List<DaAssetDO> getDaAssetList(DaAssetPageReqVO reqVO) {
        MPJLambdaWrapper<DaAssetDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DaAssetDO.class)
                .select("t2.NAME AS catName")
                .select("t3.PROJECT_ID AS projectId,t3.PROJECT_CODE AS projectCode")
                .leftJoin("ATT_ASSET_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .leftJoin("DA_ASSET_PROJECT_REL t3 on t.id = t3.ASSET_ID AND t3.DEL_FLAG = '0'")
                .likeRight(StringUtils.isNotBlank(reqVO.getCatCode()), DaAssetDO::getCatCode, reqVO.getCatCode())
                .like(StringUtils.isNotBlank(reqVO.getName()), DaAssetDO::getName, reqVO.getName())
                .eq(StringUtils.isNotBlank(reqVO.getDatasourceId()), DaAssetDO::getDatasourceId, reqVO.getDatasourceId())
                .like(StringUtils.isNotBlank(reqVO.getTableName()), DaAssetDO::getTableName, reqVO.getTableName())
                .eq(StringUtils.isNotBlank(reqVO.getTableComment()), DaAssetDO::getTableComment, reqVO.getTableComment())
                .eq(StringUtils.isNotBlank(reqVO.getStatus()), DaAssetDO::getStatus, reqVO.getStatus())
                .eq(StringUtils.isNotBlank(reqVO.getType()), DaAssetDO::getType, reqVO.getType())
                .eq(StringUtils.isNotBlank(reqVO.getDescription()), DaAssetDO::getDescription, reqVO.getDescription())
                .in(reqVO.getThemeAssetIdList() != null && !reqVO.getThemeAssetIdList()
                        .isEmpty(), DaAssetDO::getId, reqVO.getThemeAssetIdList())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn()
                                                                                                                                                                                            .split(",")) : null);

        return daAssetMapper.selectJoinList(DaAssetDO.class, lambdaWrapper);
    }


    @Override
    public DaAssetRespVO getDaAssetById(Long id) {
        MPJLambdaWrapper<DaAssetDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DaAssetDO.class)
                .select("t2.NAME AS catName",
                        "dd.DATASOURCE_NAME as datasourceName",
                        "dd.IP as datasourceIp",
                        "dd.DATASOURCE_TYPE as datasourceType",
                        "t3.NAME AS dataLayerName",
                        "t3.ENG_NAME AS dataLayerEngName",
                        "t4.NAME AS businessCategoryName",
                        "t4.ENG_NAME AS businessCategoryEngName",
                        "t5.NAME AS dataDomainName",
                        "t5.ENG_NAME AS dataDomainEngName",
                        "t6.NAME AS themeDomainName",
                        "t6.ENG_NAME AS themeDomainEngName",
                        "u.PHONENUMBER AS createUserPhoneNumber",
                        "u2.PHONENUMBER AS updateUserPhoneNumber")
                .leftJoin("SYSTEM_USER u on t.CREATOR_ID = u.USER_ID AND u.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER u2 on t.UPDATER_ID = u2.USER_ID AND u2.DEL_FLAG = '0'")
                .leftJoin("ATT_ASSET_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .leftJoin("DA_DATASOURCE dd on t.DATASOURCE_ID = dd.ID")

                .leftJoin("DM_DATA_LAYER t3 ON t.DATA_LAYER_ID = t3.id AND t3.DEL_FLAG = '0'")
                .leftJoin("DM_BUSINESS_CATEGORY t4 ON t.BUSINESS_CATEGORY_ID = t4.id AND t4.DEL_FLAG = '0'")
                .leftJoin("DM_DATA_DOMAIN t5 ON t.DATA_DOMAIN_ID = t5.id AND t5.DEL_FLAG = '0'")
                .leftJoin("DM_THEME_DOMAIN t6 ON t.THEME_DOMAIN_ID = t6.id AND t6.DEL_FLAG = '0'")
                .eq(DaAssetDO::getId, id);

        // Concatenate tag list query
        String subSelectSql = "SELECT\n" +
                "'['|| WM_CONCAT(DISTINCT '{\"tagId\":\"' || d.ID || '\",\"tagName\":\"' || d.name || '\"}' ) ||']'\n" +
                "FROM \n" +
                "     ATT_TAG d \n" +
                "JOIN ATT_TAG_ASSET_REL rel ON d.ID = rel.TAG_ID \n" +
                "WHERE \n" +
                "    d.DEL_FLAG ='0' \n" +
                "    AND rel.ASSET_ID = t.ID \n" +
                "HAVING COUNT(d.ID) > 0";

        if (org.apache.commons.lang3.StringUtils.equals("mysql", MasterDataSourceConfig.getDatabaseType())) {
            subSelectSql = "SELECT \n" +
                    "    CONCAT(\n" +
                    "        '[', \n" +
                    "        GROUP_CONCAT(\n" +
                    "            DISTINCT CONCAT(\n" +
                    "                '{\"tagId\":\"', d.ID, \n" +
                    "                '\",\"tagName\":\"', d.name, \n" +
                    "                '\"}'\n" +
                    "            )\n" +
                    "        ), \n" +
                    "        ']'\n" +
                    "    ) AS json_result\n" +
                    "FROM \n" +
                    "     ATT_TAG d \n" +
                    "JOIN ATT_TAG_ASSET_REL rel ON d.ID = rel.TAG_ID \n" +
                    "WHERE \n" +
                    "    d.DEL_FLAG ='0' \n" +
                    "    AND rel.ASSET_ID = t.ID \n" +
                    "HAVING COUNT(d.ID) > 0";
        } else if (org.apache.commons.lang3.StringUtils.equals("kingbase8", MasterDataSourceConfig.getDatabaseType())) {
            subSelectSql = "SELECT \n" +
                    "    CONCAT_WS('','[' , STRING_AGG(DISTINCT CONCAT_WS('', '{\"tagId\":\"', d.ID, '\",\"tagName\":\"', d.name, '\"}'), ',') , ']')\n" +
                    "FROM \n" +
                    "     ATT_TAG d \n" +
                    "JOIN ATT_TAG_ASSET_REL rel ON d.ID = rel.TAG_ID \n" +
                    "WHERE \n" +
                    "    d.DEL_FLAG ='0' \n" +
                    "    AND rel.ASSET_ID = t.ID \n" +
                    "HAVING COUNT(d.ID) > 0";
        }
        lambdaWrapper.select("(" + subSelectSql + ") AS tags");
        DaAssetDO daAssetDO = daAssetMapper.selectJoinOne(DaAssetDO.class, lambdaWrapper);

        DaAssetThemeRelPageReqVO daAssetThemeRelPageReqVO = new DaAssetThemeRelPageReqVO();
        daAssetThemeRelPageReqVO.setAssetId(daAssetDO.getId());
        List<DaAssetThemeRelRespVO> daAssetThemeRelList = daAssetThemeRelService.getDaAssetThemeRelList(daAssetThemeRelPageReqVO);

        daAssetDO.setDaAssetThemeRelList(daAssetThemeRelList);

        DaAssetRespVO bean = BeanUtils.toBean(daAssetDO, DaAssetRespVO.class);
        queryDaAssetchild(bean);

        if (StringUtils.isNotBlank(bean.getTags())) {
            JSONArray tags = JSONArray.parse(bean.getTags());
            bean.setTagIds(
                    tags.stream()
                            .map(tag -> ((com.alibaba.fastjson2.JSONObject) tag).getString("tagId"))
                            .collect(Collectors.toList())
            );
            bean.setTagNames(
                    tags.stream()
                            .map(tag -> ((com.alibaba.fastjson2.JSONObject) tag).getString("tagName"))
                            .collect(Collectors.toList())
            );
        }

        return bean;
    }

    @Override
    public DaAssetRespVO getDaAssetByIdSimple(Long id) {
        return BeanUtils.toBean(daAssetMapper.selectById(id), DaAssetRespVO.class);
    }

    private void queryDaAssetchild(DaAssetRespVO daAsset) {
        Long assetId = daAsset.getId();
        //1: database table  2: external API 3: geospatial service 4: vector data 5: video data
        String type = daAsset.getType();
        if (StringUtils.equals("1", type)) {
            return;
        } else if (StringUtils.equals("2", type)) {
            DaAssetApiRespVO daAssetApiByAssetId = iDaAssetApiService.getDaAssetApiByAssetId(assetId);
            daAsset.setDaAssetApi(daAssetApiByAssetId);
            if (daAssetApiByAssetId == null) {
                daAsset.setDaAssetApiParamList(new ArrayList<>());
                return;
            }
            List<DaAssetApiParamRespVO> daAssetApiParamList = iDaAssetApiParamService.getDaAssetApiParamList(daAssetApiByAssetId.getId());
            daAsset.setDaAssetApiParamList(daAssetApiParamList);
        } else if (StringUtils.equals("3", type)) {
            DaAssetGisRespVO daAssetGisByAssetId = iDaAssetGisService.getDaAssetGisByAssetId(assetId);
            daAsset.setDaAssetGis(daAssetGisByAssetId);
        } else if (StringUtils.equals("4", type)) {
            DaAssetGeoRespVO daAssetGeoByAssetId = iDaAssetGeoService.getDaAssetGeoByAssetId(assetId);
            daAsset.setDaAssetGeo(daAssetGeoByAssetId);
        } else if (StringUtils.equals("5", type)) {
            DaAssetVideoRespVO daAssetVideoByAssetId = iDaAssetVideoService.getDaAssetVideoByAssetId(assetId);
            daAsset.setDaAssetVideo(daAssetVideoByAssetId);
        } else if (StringUtils.equals("6", type)) {
            DaAssetFilesDO serviceById = daAssetFilesService.getOne(new LambdaQueryWrapperX<DaAssetFilesDO>().eq(DaAssetFilesDO::getAssetId, assetId));
            DaAssetFilesSaveReqVO filesSaveReqVO = BeanUtils.toBean(serviceById, DaAssetFilesSaveReqVO.class);
            daAsset.setDaAssetFiles(filesSaveReqVO);
        } else if (StringUtils.equals("7", type)) {
            DaAssetFileDO fileDO = assetFileMapper.selectByAssetId(assetId);
            if (fileDO != null) {
                daAsset.setFileInfo(fileDO.toFileInfo());
            }
        } else {
            return;
        }
    }

    @Override
    public Long createDaAsset(DaAssetSaveReqVO createReqVO) {
        DaAssetDO dictType = BeanUtils.toBean(createReqVO, DaAssetDO.class);
        daAssetMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAsset(DaAssetSaveReqVO updateReqVO) {
        // Related validation

        // Update data asset
        DaAssetDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetDO.class);
        return daAssetMapper.updateById(updateObj);
    }

    @Override
    public int removeDaAsset(Collection<Long> idList) {
        ArrayList<Long> assetIdList = new ArrayList<>(idList);
        int asset = dppEtlTaskService.checkTaskIdInAsset(assetIdList);
        if (asset > 0) {
            throw new ServiceException("da.error.delete.project.ref", "Delete failed, asset is referenced by a project!");
        }
        List<DaAssetDO> daAssetDOList = daAssetMapper.selectList("ID", idList);
        DaAssetDO daAssetDO = daAssetDOList != null ? daAssetDOList.get(0) : null;
        if ("1".equals(daAssetDO.getSource())) {
            LambdaQueryWrapperX<DaDiscoveryTaskDO> queryWrapperX = new LambdaQueryWrapperX<>();
            queryWrapperX.eqIfPresent(DaDiscoveryTaskDO::getDatasourceId, daAssetDO.getDatasourceId());
            List<DaDiscoveryTaskDO> taskDOList = daDiscoveryTaskService.list(queryWrapperX);
            List<Long> taskIdList = taskDOList.stream().map(DaDiscoveryTaskDO::getId).collect(Collectors.toList());
            DaDiscoveryTableSaveReqVO daDiscoveryTableSaveReqVO = new DaDiscoveryTableSaveReqVO();
            daDiscoveryTableSaveReqVO.setTaskIdList(taskIdList);
            daDiscoveryTableSaveReqVO.setTableName(daAssetDO.getTableName());
            daDiscoveryTableSaveReqVO.setIgnoreFlag("0");
            daDiscoveryTableSaveReqVO.setStatus("1");
            daDiscoveryTableService.updateByTaskIdListAndTableNameStatus(daDiscoveryTableSaveReqVO);
        }
        // Batch delete data assets
        return daAssetMapper.deleteBatchIds(idList);
    }

    @Override
    public int removeDaAsset(Long id) {
        ArrayList<Long> assetIdList = new ArrayList<>();
        assetIdList.add(id);
        int asset = dppEtlTaskService.checkTaskIdInAsset(assetIdList);
        if (asset > 0) {
            throw new ServiceException("da.error.delete.project.ref", "Delete failed, asset is referenced by a project!");
        }
        DaAssetDO daAssetDO = daAssetMapper.selectById(id);
        if (daAssetDO == null) {
            return 1;
        }
        if ("1".equals(daAssetDO.getSource())) {
            LambdaQueryWrapperX<DaDiscoveryTaskDO> queryWrapperX = new LambdaQueryWrapperX<>();
            queryWrapperX.eqIfPresent(DaDiscoveryTaskDO::getDatasourceId, daAssetDO.getDatasourceId());
            List<DaDiscoveryTaskDO> taskDOList = daDiscoveryTaskService.list(queryWrapperX);
            List<Long> taskIdList = taskDOList.stream().map(DaDiscoveryTaskDO::getId).collect(Collectors.toList());
            DaDiscoveryTableSaveReqVO daDiscoveryTableSaveReqVO = new DaDiscoveryTableSaveReqVO();
            daDiscoveryTableSaveReqVO.setTaskIdList(taskIdList);
            daDiscoveryTableSaveReqVO.setTableName(daAssetDO.getTableName());
            daDiscoveryTableSaveReqVO.setIgnoreFlag("0");
            daDiscoveryTableSaveReqVO.setStatus("1");
            daDiscoveryTableService.updateByTaskIdListAndTableNameStatus(daDiscoveryTableSaveReqVO);
        }
        // Delete project
        iDaAssetProjectRelService.removeProjectRelByAssetId(id);
        // Delete theme
        daAssetThemeRelService.removeThemeRelByAssetId(id);

        daAssetMapper.deleteAssetById(id);
        // Batch delete data assets

        // Update tag asset count
        attTagAssetRelApiService.deleteRelByUpdateTag(id);
        return 1;
    }


    @Override
    public List<DaAssetDO> getDaAssetList() {
        return daAssetMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetDO> getDaAssetMap() {
        List<DaAssetDO> daAssetList = daAssetMapper.selectList();
        return daAssetList.stream().collect(Collectors.toMap(DaAssetDO::getId, daAssetDO -> daAssetDO,
                // Keep existing values
                (existing, replacement) -> existing));
    }


    /**
     * Import data asset data
     *
     * @param importExcelList Data asset data list
     * @param isUpdateSupport Whether update is supported. If a record already exists, update it.
     * @param operName        Operating user
     * @return Result
     */
    @Override
    public String importDaAsset(List<DaAssetRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "Import data cannot be empty!");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetRespVO respVO : importExcelList) {
            try {
                DaAssetDO daAssetDO = BeanUtils.toBean(respVO, DaAssetDO.class);
                Long daAssetId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetId != null) {
                        DaAssetDO existingDaAsset = daAssetMapper.selectById(daAssetId);
                        if (existingDaAsset != null) {
                            daAssetMapper.updateById(daAssetDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "Data updated successfully, data asset record with ID " + daAssetId + ".", daAssetId, "Data Asset"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "Data update failed, data asset record with ID " + daAssetId + " does not exist.", daAssetId, "Data Asset"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "Data update failed, the ID of a certain record does not exist."));
                    }
                } else {
                    QueryWrapper<DaAssetDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetId);
                    DaAssetDO existingDaAsset = daAssetMapper.selectOne(queryWrapper);
                    if (existingDaAsset == null) {
                        daAssetMapper.insert(daAssetDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "Data inserted successfully, data asset record with ID " + daAssetId + ".", daAssetId, "Data Asset"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "Data insert failed, data asset record with ID " + daAssetId + " already exists.", daAssetId, "Data Asset"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "Data import failed, error info: " + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                    "Sorry, import failed! A total of " + failureNum + " records have incorrect format, errors as follows:<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                    "Congratulations, all data has been imported successfully! A total of " + successNum + " records.", successNum));
        }
        return resultMsg.toString();
    }

    /**
     * Data asset preview with desensitization rules applied
     *
     * @param jsonObject Primary key id and query condition content
     * @return
     */
    @Override
    public Map<String, Object> getColumnData(JSONObject jsonObject) {
        String tableName = "";
        Long dataSourceId = null;
        if (StringUtils.isEmpty(jsonObject.getStr("pageNum")) || StringUtils.isEmpty(jsonObject.getStr("pageSize"))) {
            throw new DataQueryException("db.error.pagination.missing", "Please include page number and page size!");
        }
        // Query data
        Integer pageNum = Integer.valueOf(jsonObject.getStr("pageNum"));
        Integer pageSize = Integer.valueOf(jsonObject.getStr("pageSize"));
        if (StringUtils.isNotEmpty(jsonObject.getStr("taskId")) && StringUtils.isNotEmpty(jsonObject.getStr("tableName"))) {
            DaDiscoveryTaskDO discoveryTaskDO = daDiscoveryTaskService.getById(Long.valueOf(jsonObject.getStr("taskId")));
            tableName = jsonObject.getStr("tableName");
            dataSourceId = discoveryTaskDO.getDatasourceId();
        } else {
            // Get asset details
            DaAssetRespVO daAssetDO = this.getDaAssetById(Long.valueOf(jsonObject.getStr("id")));
            if (StringUtils.equals("6", daAssetDO.getType())) {
                DaAssetFilesDO filesServiceOne = daAssetFilesService.getOne(new LambdaQueryWrapperX<DaAssetFilesDO>().eq(DaAssetFilesDO::getAssetId, daAssetDO.getId()));
                if (SUPPORTED_EXTENSIONS.contains(filesServiceOne.getType())) {
                    String fixedResourceUrl = profile.replace("\\", "/")
                            .replaceAll("/+$", "")
                            .replaceAll("/profile", "");
                    String url = filesServiceOne.getUrl().replaceAll("/profile", "");
                    Map<String, Object> fileData = FileDataReaderUtil.readFileData(fixedResourceUrl + url, pageNum.longValue(), pageSize.longValue(), filesServiceOne.getStartData(), filesServiceOne.getStartColumn(), jsonObject.getStr("filter"));
                    return fileData;
                }
            }
            tableName = daAssetDO.getTableName();
            dataSourceId = daAssetDO.getDatasourceId();
        }
        // Get datasource connection info
        DaDatasourceDO daDatasourceDO = daDatasourceMapper.selectById(dataSourceId);
        if (daDatasourceDO == null) {
            return null;
        }
        DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceDO.getDatasourceType(), daDatasourceDO.getIp(), daDatasourceDO.getPort(), daDatasourceDO.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        DbDialect dbDialect = DialectFactory.getDialect(DbType.getDbType(dbQueryProperty.getDbType()));
        if (!dbQuery.valid()) {
            dbQuery.close();
            throw new DataQueryException("db.error.connection.fail", "Database connection failed");
        }
        int existsSQL = dbQuery.generateCheckTableExistsSQL(dbQueryProperty, tableName);
        if (existsSQL == 0) {
            dbQuery.close();
            throw new DataQueryException("db.error.table.missing", "Table data not found in the database. Please confirm if the table exists.");
        }
        // Get field collection
        List<DbColumn> columns = redisCache.getCacheList(CacheConstants.ASSET_PREVIEW_KEY + daDatasourceDO.getId() + "_" + tableName);
        // Get asset fields
        List<DbColumn> daAssetColumns = daAssetColumnMapper.findByAssetId(Long.parseLong(jsonObject.getStr("id")))
                .stream()
                .map(e -> e.toDbColumn())
                .collect(Collectors.toList());
        if (columns.isEmpty()) {
            // Get table fields
            columns = dbQuery.getTableColumns(dbQueryProperty, tableName);
            if (columns.size() == 0) {
                dbQuery.close();
                throw new DataQueryException("db.error.connection.fail", "Database connection failed");
            }
            redisCache.setCacheList(CacheConstants.ASSET_PREVIEW_KEY + daDatasourceDO.getId() + "_" + tableName, columns);
            redisCache.expire(CacheConstants.ASSET_PREVIEW_KEY + daDatasourceDO.getId() + "_" + tableName, 5, TimeUnit.MINUTES);
        }
        // Concatenate query SQL statement
        List<Map<String, Object>> columnTable = new ArrayList<>();
        for (DbColumn column : daAssetColumns) {
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("field", column.getColName());
            columnMap.put("en", column.getColName());
            columnMap.put("cn", column.getColComment());
            columnMap.put("columnNullable", column.getNullable());
            columnMap.put("columnKey", column.getColKey());
            columnTable.add(columnMap);
        }
        List<Map> orderByList = jsonObject.getBeanList("orderBy", Map.class);

        PageUtil pageUtil = new PageUtil(pageNum, pageSize);
        List<Map<String, Object>> queryList;

        queryList = dbQuery.queryDbColumnByList(columns, tableName, dbQueryProperty, jsonObject.getStr("filter"), orderByList, pageUtil.getOffset(), pageSize);
        int total = dbQuery.countNew(tableName, dbQueryProperty, jsonObject.getStr("filter"));

        Map<String, Object> data = new HashMap<>();
        data.put("columns", columnTable);
        data.put("tableData", queryList);
        data.put("total", total);
        dbQuery.close();
        return data;
    }

    @Override
    public List<Map<String, Object>> dataMasking(Long assetId, List<Map<String, Object>> data) {
        // 1) Field metadata (matched by uppercase field name)
        List<DaAssetColumnDO> cols = daAssetColumnMapper.findByAssetId(assetId);
        Map<String, DaAssetColumnDO> colMap = cols.stream()
                .collect(Collectors.toMap(c -> c.getColumnName().toUpperCase(), c -> c, (a, b) -> a));

        // 2) Sensitivity level (online only)
        Map<Long, DaSensitiveLevelDO> levelMap = daSensitiveLevelMapper.selectList(new QueryWrapper<DaSensitiveLevelDO>().eq("online_flag", 1))
                .stream()
                .collect(Collectors.toMap(DaSensitiveLevelDO::getId, x -> x, (a, b) -> a));

        List<Map<String, Object>> out = new ArrayList<>(data.size());

        for (Map<String, Object> row : data) {
            // Use LinkedHashMap to maintain field order, without modifying the original map
            Map<String, Object> masked = new HashMap<>(row.size());

            for (Map.Entry<String, Object> e : row.entrySet()) {
                String key = e.getKey();
                Object val = e.getValue();

                // Ensure _id is always a string
                if ("_id".equalsIgnoreCase(key) && val != null && "org.bson.types.ObjectId".equals(val.getClass()
                        .getName())) {
                    val = val.toString();
                    masked.put(key, val);
                    continue;
                }

                // -- No matching config or no sensitivity level -> return as-is
                DaAssetColumnDO meta = colMap.get(key.toUpperCase());
                if (meta == null || meta.getSensitiveLevelId() == null) {
                    masked.put(key, val);
                    continue;
                }

                DaSensitiveLevelDO lvl = levelMap.get(meta.getSensitiveLevelId());
                if (lvl == null) {
                    masked.put(key, val);
                    continue;
                }


                // Only desensitize strings; return other types as-is
                if (!(val instanceof CharSequence)) {
                    masked.put(key, val);
                    continue;
                }

                String s = val == null ? null : val.toString();
                if (s == null || s.isEmpty()) {
                    masked.put(key, s);
                    continue;
                }

                // Start/end positions: start/end is 1-based; null means full coverage
                int len = s.length();
                int start = lvl.getStartCharLoc() == null ? 1 : lvl.getStartCharLoc().intValue();
                int end = lvl.getEndCharLoc() == null ? len : lvl.getEndCharLoc().intValue();

                // Normalize boundaries and ensure start<=end
                start = Math.max(1, start);
                end = Math.min(len, end);
                if (start > end) { // No valid coverage range -> return as-is
                    masked.put(key, s);
                    continue;
                }

                String maskUnit = lvl.getMaskCharacter();
                if (maskUnit == null || maskUnit.isEmpty()) maskUnit = "*";

                int coverLen = end - start + 1;
                String midMask = repeat(maskUnit, coverLen); // Support multi-character mask, no displacement

                String res = s.substring(0, start - 1) + midMask + s.substring(end);
                masked.put(key, res);
            }

            out.add(masked);
        }

        return out;
    }

    /**
     * Generates a mask string of specified length (maskUnit can be multi-character)
     */
    private static String repeat(String maskUnit, int targetLen) {
        if (targetLen <= 0) return "";
        if (maskUnit == null || maskUnit.isEmpty()) maskUnit = "*";
        StringBuilder sb = new StringBuilder(targetLen);
        while (sb.length() + maskUnit.length() <= targetLen) sb.append(maskUnit);
        int remain = targetLen - sb.length();
        if (remain > 0) sb.append(maskUnit, 0, remain);
        return sb.toString();
    }


//
//    /**
//     * Desensitize data of a data asset
//     *
//     * @param id   Data asset id
//     * @param data Data asset data
//     * @return
//     */
//    @Override
//    public List<Map<String, Object>> dataMasking(Long id, List<Map<String, Object>> data) {
//        // Query field properties based on asset details
//        List<DaAssetColumnDO> assetColumnDOList = daAssetColumnMapper.findByAssetId(id);
//        // Convert field names to uppercase and transform into map type, key is uppercase field name, value is the entity
//        Map<String, DaAssetColumnDO> columnDOMap = assetColumnDOList.stream().collect(Collectors.toMap(
//                daAssetColumnDO -> daAssetColumnDO.getColumnName().toUpperCase(), daAssetColumnDO -> daAssetColumnDO));
//        // Query sensitivity levels and transform into map type, key is desensitization level id, value is the entity
//        QueryWrapper<DaSensitiveLevelDO> queryWrapper = new QueryWrapper<DaSensitiveLevelDO>().eq("online_flag", 1);
//        Map<Long, DaSensitiveLevelDO> daSensitiveLevelDOMap = daSensitiveLevelMapper.selectList(queryWrapper).stream()
//                .collect(Collectors.toMap(DaSensitiveLevelDO::getId, daSensitiveLevelDO -> daSensitiveLevelDO));
//        List<Map<String, Object>> columnData = new ArrayList<>();
//        for (Map<String, Object> datum : data) {
//            Map<String, Object> map = new HashMap<>();
//            for (String key : datum.keySet()) {
//                if (datum.get(key) == null) {
//                    continue;
//                }
//                StringBuilder stringBuilder = new StringBuilder(datum.get(key).toString());
//                if (columnDOMap.get(key.toUpperCase()) == null) {
//                    return null;
//                }
//                Long sensitiveLevelId = columnDOMap.get(key.toUpperCase()).getSensitiveLevelId();
//                if (sensitiveLevelId == null) {
//                    map.put(key, stringBuilder);
//                    continue;
//                }
//                DaSensitiveLevelDO daSensitiveLevelDO = daSensitiveLevelDOMap.get(sensitiveLevelId);
//
//                if (daSensitiveLevelDOMap.get(sensitiveLevelId) != null) {
//                    // Get start and end positions
//                    int startCharLoc = daSensitiveLevelDO.getStartCharLoc() == null ? 0 : daSensitiveLevelDO.getStartCharLoc().intValue();
//                    int endCharLoc = daSensitiveLevelDO.getEndCharLoc() == null ? stringBuilder.length() : daSensitiveLevelDO.getEndCharLoc().intValue();
//                    // Replace the string
//                    String maskChar = daSensitiveLevelDO.getMaskCharacter();
//                    startCharLoc = startCharLoc > 0 ? startCharLoc - 1 : startCharLoc;
//                    endCharLoc = Math.min(endCharLoc, stringBuilder.length());
//                    int index = startCharLoc;
//                    for (int i = startCharLoc; i < endCharLoc; i++) {
//                        stringBuilder.replace(index, index + 1, maskChar);
//                        index += maskChar.length();
//                    }
//                }
//                map.put(key, stringBuilder);
//            }
//            columnData.add(map);
//        }
//        return columnData;
//    }

    @Override
    public void insertAssetByDiscoveryInfo(DaAssetPageReqVO daAssetReqVO, List<DaAssetColumnSaveReqVO> columnSaveReqVOList) {

        DaAssetDO daAssetDO = BeanUtils.toBean(daAssetReqVO, DaAssetDO.class);
        // Check if asset exists
        DaAssetPageReqVO daAssetPageReqVO = new DaAssetPageReqVO();
        daAssetPageReqVO.setTableName(daAssetDO.getTableName());
        daAssetPageReqVO.setDatasourceId(String.valueOf(daAssetDO.getDatasourceId()));
        DaAssetDO assetDO = this.getDaAssetByDaAssetPageReqVO(daAssetPageReqVO);
        if (assetDO != null) {
            daAssetDO.setId(assetDO.getId());
            daAssetMapper.updateById(daAssetDO);// Add asset data
        } else {
            daAssetMapper.insert(daAssetDO);// Add asset data
        }
        List<String> themeIdList = daAssetReqVO.getThemeIdList();
        if (CollectionUtils.isNotEmpty(themeIdList)) {
            daAssetThemeRelService.createDaAssetThemeRelList(themeIdList, daAssetDO.getId());
        }

        List<DaAssetColumnDO> daAssetColumnList = new ArrayList<>();
        if (assetDO != null) {
            DaAssetColumnPageReqVO daAssetColumnPageReqVO = new DaAssetColumnPageReqVO();
            daAssetColumnPageReqVO.setAssetId(String.valueOf(assetDO.getId()));
            List<DaAssetColumnDO> daAssetColumnList1 = iDaAssetColumnService.getDaAssetColumnList(daAssetColumnPageReqVO);
            daAssetColumnList = CollectionUtils.isEmpty(daAssetColumnList1) ? daAssetColumnList : daAssetColumnList1;
        }

        Map<String, Long> columnNameToIdMap = daAssetColumnList.stream()
                .filter(columnDO -> columnDO.getColumnName() != null)
                .collect(Collectors.toMap(DaAssetColumnDO::getColumnName, DaAssetColumnDO::getId, (id1, id2) -> id1));


        for (DaAssetColumnSaveReqVO reqVO : columnSaveReqVOList) {
            if (reqVO.getColumnName() != null) {
                Long id = columnNameToIdMap.get(reqVO.getColumnName());
                if (id != null) {
                    reqVO.setId(id);
                }
            }
        }

        Collection<Long> nonExistingIdList = this.findMissingColumnIds(daAssetColumnList, columnSaveReqVOList);
        if (CollectionUtils.isNotEmpty(nonExistingIdList)) {
            iDaAssetColumnService.removeDaAssetColumn(nonExistingIdList);
        }
        Long daAssetDOId = daAssetDO.getId();
        for (DaAssetColumnSaveReqVO daAssetColumnSaveReqVO : columnSaveReqVOList) {
            daAssetColumnSaveReqVO.setAssetId(String.valueOf(daAssetDOId));
            if (daAssetColumnSaveReqVO.getId() == null) {
                iDaAssetColumnService.createDaAssetColumn(daAssetColumnSaveReqVO);
            } else {
                iDaAssetColumnService.updateDaAssetColumn(daAssetColumnSaveReqVO);
            }
        }
    }

    public Collection<Long> findMissingColumnIds(List<DaAssetColumnDO> daAssetColumnList, List<DaAssetColumnSaveReqVO> columnSaveReqVOList) {
        if (daAssetColumnList == null) {
            return Collections.emptyList();
        }
        Set<String> existingColumnNames = columnSaveReqVOList == null ? Collections.emptySet() : columnSaveReqVOList.stream()
                                                                                                 .filter(vo -> vo.getColumnName() != null)
                                                                                                 .map(DaAssetColumnSaveReqVO::getColumnName)
                                                                                                 .collect(Collectors.toSet());
        return daAssetColumnList.stream()
                .filter(doObj -> doObj.getColumnName() != null && !existingColumnNames.contains(doObj.getColumnName()))
                .map(DaAssetColumnDO::getId)
                .collect(Collectors.toList());
    }

    @Override
    public void updateAssetByDiscoveryInfo(DaAssetPageReqVO daAssetReqVO) {
        DaAssetDO daAssetDO = BeanUtils.toBean(daAssetReqVO, DaAssetDO.class);
        // Check if asset exists
        DaAssetPageReqVO daAssetPageReqVO = new DaAssetPageReqVO();
        daAssetPageReqVO.setTableName(daAssetDO.getTableName());
        daAssetPageReqVO.setDatasourceId(String.valueOf(daAssetDO.getDatasourceId()));
        DaAssetDO assetDO = this.getDaAssetByDaAssetPageReqVO(daAssetPageReqVO);
        if (assetDO == null) {
            return;
        }

        daAssetMapper.deleteAssetById(assetDO.getId());
        daAssetColumnMapper.deleteAssetColumnByAssetId(assetDO.getId());
        daAssetThemeRelService.removeThemeRelByAssetId(assetDO.getId());
    }

    @Override
    public PageResult<DaAssetDO> getDppAssetPage(DaAssetPageReqVO daAsset) {
        if (StringUtils.isEmpty(daAsset.getProjectCode()) || daAsset.getProjectId() == null) {
            return new PageResult<DaAssetDO>();
        }
        LambdaQueryWrapperX<DaAssetApplyDO> queryWrapperX = new LambdaQueryWrapperX();
        String[] sourceTypeArr = daAsset.getParams().get("sourceType") == null ? null : daAsset.getParams()
                                                                                        .get("sourceType")
                                                                                        .toString()
                                                                                        .split(",");
        queryWrapperX.eqIfPresent(DaAssetApplyDO::getStatus, 3);
        queryWrapperX.eqIfPresent(DaAssetApplyDO::getProjectId, daAsset.getProjectId());
        queryWrapperX.eqIfPresent(DaAssetApplyDO::getProjectCode, daAsset.getProjectCode());
        queryWrapperX.inIfPresent(DaAssetApplyDO::getSourceType, sourceTypeArr);
        List<DaAssetApplyDO> applyDOList = daAssetApplyMapper.selectList(queryWrapperX);
        List<Long> assetIdList;
        Map<Long, DaAssetApplyDO> daAssetApplyDOMap;
        if (applyDOList.isEmpty()) {
            assetIdList = new ArrayList<>();
            daAssetApplyDOMap = new HashMap<>();
        } else {
            daAssetApplyDOMap = applyDOList.stream()
                    .collect(Collectors.toMap(DaAssetApplyDO::getAssetId, daAssetApplyDO -> daAssetApplyDO));
            assetIdList = daAssetApplyDOMap.keySet().stream().collect(Collectors.toList());
        }
        daAsset.setAssetIdList(assetIdList);
        PageResult<DaAssetDO> daAssetPage = this.getDaAssetPage(daAsset, "2");
        if (CollectionUtils.isEmpty(daAssetPage.getRows())) {
            return daAssetPage;
        }
        for (Object assetPageRow : daAssetPage.getRows()) {
            DaAssetDO daAssetDO = (DaAssetDO) assetPageRow;
            DaAssetApplyDO daAssetApplyDO = daAssetApplyDOMap.get(daAssetDO.getId()) == null ? new DaAssetApplyDO() : daAssetApplyDOMap.get(daAssetDO.getId());
            if (assetIdList.contains(daAssetDO.getId())) {
                daAssetDO.setSourceType(daAssetApplyDO.getSourceType());
            } else {
                daAssetDO.setSourceType("1");
            }
        }
        return daAssetPage;
    }

    @Override
    public List<DaAssetDO> getDppAssetNoPageList(DaAssetPageReqVO daAsset) {
        if (StringUtils.isEmpty(daAsset.getProjectCode()) || daAsset.getProjectId() == null) {
            return new ArrayList<>();
        }
        LambdaQueryWrapperX<DaAssetApplyDO> queryWrapperX = new LambdaQueryWrapperX();
        queryWrapperX.eqIfPresent(DaAssetApplyDO::getStatus, 3);
        queryWrapperX.eqIfPresent(DaAssetApplyDO::getProjectId, daAsset.getProjectId());
        queryWrapperX.eqIfPresent(DaAssetApplyDO::getProjectCode, daAsset.getProjectCode());
        List<DaAssetApplyDO> applyDOList = daAssetApplyMapper.selectList(queryWrapperX);
        if (applyDOList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> assetIdList = applyDOList.stream()
                .collect(Collectors.toMap(DaAssetApplyDO::getAssetId, daAssetApplyDO -> daAssetApplyDO))
                .keySet()
                .stream()
                .collect(Collectors.toList());
        LambdaQueryWrapperX<DaAssetDO> daAssetQueryWrapper = new LambdaQueryWrapperX<>();
        daAssetQueryWrapper.inIfPresent(DaAssetDO::getId, assetIdList);
        List<DaAssetDO> daAssetDOList = daAssetMapper.selectList(daAssetQueryWrapper);
        return daAssetDOList;
    }

    @Override
    public Long createDaAssetNew(DaAssetSaveReqVO daAsset) {
        if (StringUtils.equals("1", daAsset.getCreateType())) {
            setDaAssetDefaultValues(daAsset);
            Long assetId = createDaAsset(daAsset);
            daAsset.setId(assetId);
            createDaAssetProjectRel(daAsset);
            createDaAssetThemeIdList(daAsset);
            return daAsset.getId();
        }
        //1: database table  2: external API 3: geospatial service 4: vector data 5: video data
        String type = daAsset.getType();
        if (StringUtils.equals("1", type)) {
            createDaAssetColumnNew(daAsset);
        } else if (StringUtils.equals("2", type)) {
            setDaAssetDefaultValues(daAsset);
            createDaAssetApiNew(daAsset);
        } else if (StringUtils.equals("3", type)) {
            setDaAssetDefaultValues(daAsset);
            createDaAssetGisNew(daAsset);
        } else if (StringUtils.equals("4", type)) {
            setDaAssetDefaultValues(daAsset);
            createDaAssetGeoNew(daAsset);
        } else if (StringUtils.equals("5", type)) {
            setDaAssetDefaultValues(daAsset);
            createDaAssetVideoNew(daAsset);
        } else if (StringUtils.equals("6", type)) {
            setDaAssetDefaultValues(daAsset);
            createDaAssetFilesNew(daAsset);
        } else if ("7".equals(type)) {
            daAsset.setTableName("-1");
            daAsset.setDataCount(0L);
            daAsset.setFieldCount(0L);
            createDaAssetFileNew(daAsset);
        } else {
            throw new ServiceException("da.error.type.unsupported", "Type not supported!");
        }

        createDaAssetProjectRel(daAsset);
        createDaAssetThemeIdList(daAsset);

        return daAsset.getId();
    }

    @Override
    public Long createDaAssetBindResources(DaAssetSaveReqVO daAsset) {
        //1: database table  2: external API 3: geospatial service 4: vector data 5: video data
        String type = daAsset.getType();
        if (StringUtils.equals("1", type)) {
            createDaAssetColumnNew(daAsset);
        } else if (StringUtils.equals("2", type)) {
            setDaAssetDefaultValues(daAsset);
            createDaAssetApiNew(daAsset);
        } else if (StringUtils.equals("3", type)) {
            setDaAssetDefaultValues(daAsset);
            createDaAssetGisNew(daAsset);
        } else if (StringUtils.equals("4", type)) {
            setDaAssetDefaultValues(daAsset);
            createDaAssetGeoNew(daAsset);
        } else if (StringUtils.equals("5", type)) {
            setDaAssetDefaultValues(daAsset);
            createDaAssetVideoNew(daAsset);
        } else if (StringUtils.equals("6", type)) {
            setDaAssetDefaultValues(daAsset);
            createDaAssetFilesNew(daAsset);
        } else {
            throw new ServiceException("da.error.type.unsupported", "Type not supported!");
        }


//        createDaAssetProjectRel(daAsset);
        createDaAssetThemeIdList(daAsset);
        daAsset.setCreateType("2");
        updateDaAsset(daAsset);
        return 1L;
    }

    private void createDaAssetFilesNew(DaAssetSaveReqVO daAsset) {
        if (daAsset.getId() == null) {
            Long assetId = createDaAsset(daAsset);
            daAsset.setId(assetId);
        }

        DaAssetFilesSaveReqVO daAssetFiles = daAsset.getDaAssetFiles();
        daAssetFiles.setAssetId(daAsset.getId());
        int lastDot = daAssetFiles.getUrl().lastIndexOf('.');
        String type = daAssetFiles.getUrl().substring(lastDot);
        daAssetFiles.setType(type);
        daAssetFilesService.createDaAssetFiles(daAssetFiles);

        if (StringUtils.equalsIgnoreCase(".xls", daAssetFiles.getType()) || StringUtils.equalsIgnoreCase(".xlsx", daAssetFiles.getType())) {
            List<DaAssetColumnDO> daAssetColumnDOS = getExcelColumn(daAssetFiles.getUrl(), daAssetFiles.getStartColumn(), daAssetFiles.getStartData(), daAsset.getId());
            iDaAssetColumnService.saveBatch(daAssetColumnDOS);
        }
        if (StringUtils.equalsIgnoreCase(".csv", daAssetFiles.getType())) {
            List<DaAssetColumnDO> daAssetColumnDOS = getCsvColumn(daAssetFiles.getUrl(), daAsset.getId());
            iDaAssetColumnService.saveBatch(daAssetColumnDOS);
        }
    }

    private void createDaAssetFileNew(DaAssetSaveReqVO daAsset) {
        Assert.notNull(daAsset.getFileInfo(), () -> new ServiceException("da.error.file.path.missing", "Missing file path"));
        DaDatasourceDO daDatasourceDO = daDatasourceMapper.selectById(daAsset.getDatasourceId());

        if (daAsset.getId() == null) {
            Long assetId = createDaAsset(daAsset);
            daAsset.setId(assetId);
        }

        FileInfo fileInfo = daAsset.getFileInfo();
        DaAssetFileDO fileDO = new DaAssetFileDO();
        fileDO.setAssetId(daAsset.getId());
        fileDO.setFileCreateTime(null);
        fileDO.setFileSource(daDatasourceDO.getDatasourceType());
        fileDO.setFileName(fileInfo.getName());
        fileDO.setFileUpdateTime(fileInfo.getLastModified());
        fileDO.setFileUrl(fileInfo.getPath());
        fileDO.setFileSize(fileInfo.getSize());
        fileDO.setFileType(fileInfo.getType());
        assetFileMapper.insert(fileDO);
    }

    private List<DaAssetColumnDO> getExcelColumn(String excelFile, Integer startColumn, Integer startData, Long assetId) {
        excelFile = AniviaConfig.getProfile() + excelFile.replace(Constants.RESOURCE_PREFIX + "/", "");
        excelFile = excelFile.replace("/", File.separator);
        String csvFile = resourceUrl + "csv" + File.separator + UUID.randomUUID().toString().replace("-", "") + ".csv";
        List<String> columnList = ExcelToCsvUtil.convertExcelToCsv(excelFile, csvFile, startColumn, startData);
        if (columnList.size() > 0) {
            if (!ExcelToCsvUtil.verifyColumn(columnList)) {
                throw new ServiceException("da.error.file.column.format", "The column name format in the attachment is incorrect, please check!");
            }
        }
        ColumnRespVO columnRespVO = ColumnRespVO.builder().csvFile(csvFile).columnList(columnList).build();
        List<DaAssetColumnDO> daAssetColumnDOS = new ArrayList<>();
        for (String name : columnRespVO.getColumnList()) {
            DaAssetColumnDO daAssetColumnDO = new DaAssetColumnDO();
            daAssetColumnDO.setColumnName(name);
            daAssetColumnDO.setColumnType("VARCHAR2");
            daAssetColumnDO.setAssetId(assetId);
            daAssetColumnDOS.add(daAssetColumnDO);
        }
        return daAssetColumnDOS;
    }

    private List<DaAssetColumnDO> getCsvColumn(String file, Long assetId) {
        file = AniviaConfig.getProfile() + file.replace(Constants.RESOURCE_PREFIX + "/", "");
        file = file.replace("/", File.separator);
        String csvFile = resourceUrl + "csv" + File.separator + UUID.randomUUID().toString().replace("-", "") + ".csv";
        List<String> columnList = ExcelToCsvUtil.parseCsv(file, csvFile);
        if (columnList.size() > 0) {
            if (!ExcelToCsvUtil.verifyColumn(columnList)) {
                throw new ServiceException("da.error.file.column.format", "The column name format in the attachment is incorrect, please check!");
            }
        }
        ColumnRespVO columnRespVO = ColumnRespVO.builder().csvFile(csvFile).columnList(columnList).build();
        List<DaAssetColumnDO> daAssetColumnDOS = new ArrayList<>();
        for (String name : columnRespVO.getColumnList()) {
            DaAssetColumnDO daAssetColumnDO = new DaAssetColumnDO();
            daAssetColumnDO.setColumnName(name);
            daAssetColumnDO.setColumnType("VARCHAR2");
            daAssetColumnDO.setAssetId(assetId);
            daAssetColumnDOS.add(daAssetColumnDO);
        }
        return daAssetColumnDOS;
    }

    private void createDaAssetVideoNew(DaAssetSaveReqVO daAsset) {
        if (daAsset.getId() == null) {
            Long assetId = createDaAsset(daAsset);
            daAsset.setId(assetId);
        }

        DaAssetVideoSaveReqVO daAssetVideo = daAsset.getDaAssetVideo();
        daAssetVideo.setAssetId(daAsset.getId());
        iDaAssetVideoService.createDaAssetVideo(daAssetVideo);
    }

    private void createDaAssetGisNew(DaAssetSaveReqVO daAsset) {
        if (daAsset.getId() == null) {
            Long assetId = createDaAsset(daAsset);
            daAsset.setId(assetId);
        }

        DaAssetGisSaveReqVO daAssetGis = daAsset.getDaAssetGis();
        daAssetGis.setAssetId(daAsset.getId());
        iDaAssetGisService.createDaAssetGis(daAssetGis);
    }

    private void setDaAssetDefaultValues(DaAssetSaveReqVO daAsset) {
        daAsset.setDatasourceId("-1");
        daAsset.setTableName("-1");
        daAsset.setDataCount(0L);
        daAsset.setFieldCount(0L);
    }

    private void createDaAssetProjectRel(DaAssetSaveReqVO daAsset) {
        if (daAsset.getProjectId() == null) {
            return;
        }
        DaAssetProjectRelSaveReqVO daAssetProjectRelSaveReqVO = new DaAssetProjectRelSaveReqVO();
        daAssetProjectRelSaveReqVO.setProjectCode(daAsset.getProjectCode());
        daAssetProjectRelSaveReqVO.setProjectId(daAsset.getProjectId());
        daAssetProjectRelSaveReqVO.setAssetId(daAsset.getId());
        iDaAssetProjectRelService.createDaAssetProjectRel(daAssetProjectRelSaveReqVO);
    }

    /**
     * @param daAsset
     */
    private void createDaAssetGeoNew(DaAssetSaveReqVO daAsset) {
        if (daAsset.getId() == null) {
            Long assetId = createDaAsset(daAsset);
            daAsset.setId(assetId);
        }

        DaAssetGeoSaveReqVO daAssetGeo = daAsset.getDaAssetGeo();
        daAssetGeo.setAssetId(daAsset.getId());
        iDaAssetGeoService.createDaAssetGeo(daAssetGeo);
    }

    private void createDaAssetApiNew(DaAssetSaveReqVO daAsset) {
        if (daAsset.getId() == null) {
            Long assetId = createDaAsset(daAsset);
            daAsset.setId(assetId);
        }

        DaAssetApiSaveReqVO daAssetApi = daAsset.getDaAssetApi();
        daAssetApi.setAssetId(daAsset.getId());
        Long daAssetApiId = iDaAssetApiService.createDaAssetApi(daAssetApi);

        List<DaAssetApiParamSaveReqVO> daAssetApiParamList = daAsset.getDaAssetApiParamList();
        iDaAssetApiParamService.createDaAssetApiParamDeep(daAssetApiParamList, daAssetApiId);
    }

    /**
     * Theme
     *
     * @param daAsset
     */
    private void createDaAssetThemeIdList(DaAssetSaveReqVO daAsset) {
        List<String> themeIdList = daAsset.getThemeIdList();
        if (CollectionUtils.isEmpty(themeIdList)) {
            return;
        }
        daAssetThemeRelService.createDaAssetThemeRelList(themeIdList, daAsset.getId());
    }

    /**
     * Fields
     *
     * @param daAsset
     */
    private void createDaAssetColumnNew(DaAssetSaveReqVO daAsset) {
        List<McColumnRespDTO> mcColumnRespDTOList = mcColumnApiService.listByTableId(daAsset.getTableId());
        List<DaAssetColumnDO> daAssetColumnDOS = mcColumnRespDTOList.stream()
                .map(mcColumnRespDTO -> new DaAssetColumnDO(mcColumnRespDTO))
                .collect(Collectors.toList());
        daAsset.setFieldCount(Long.valueOf(daAssetColumnDOS.size()));

        if (daAsset.getId() == null) {
            Long assetId = createDaAsset(daAsset);
            daAsset.setId(assetId);
        }
        List<DaAssetColumnSaveReqVO> daAssetColumnSaveReqVOList = BeanUtils.toBean(daAssetColumnDOS, DaAssetColumnSaveReqVO.class);
        for (DaAssetColumnSaveReqVO daAssetColumnSaveReqVO : daAssetColumnSaveReqVOList) {
            daAssetColumnSaveReqVO.setAssetId(String.valueOf(daAsset.getId()));
            iDaAssetColumnService.createDaAssetColumn(daAssetColumnSaveReqVO);
        }

    }

    @Override
    public int updateDaAssetNew(DaAssetSaveReqVO daAsset) {
        //1: database table  2: external API 3: geospatial service 4: vector data 5: video data
        String type = daAsset.getType();
        if (StringUtils.equals("1", type)) {
            DaAssetRespVO daAssetById = getDaAssetById(daAsset.getId());
            if (StringUtils.equals("1", daAssetById.getCreateType()) && StringUtils.equals("2", daAsset.getCreateType())) {
                createDaAssetColumnNew(daAsset);
            }
        } else if (StringUtils.equals("2", type)) {
            setDaAssetDefaultValues(daAsset);
            updateDaAssetApiNew(daAsset);
        } else if (StringUtils.equals("3", type)) {
            setDaAssetDefaultValues(daAsset);
            updateDaAssetGisNew(daAsset);
        } else if (StringUtils.equals("4", type)) {
            setDaAssetDefaultValues(daAsset);
            updateDaAssetGeoNew(daAsset);
        } else if (StringUtils.equals("5", type)) {
            setDaAssetDefaultValues(daAsset);
            updateDaAssetVideoNew(daAsset);
        } else if (StringUtils.equals("6", type)) {
            setDaAssetDefaultValues(daAsset);
            updateDaAssetFilesNew(daAsset);
        }


//        createDaAssetProjectRel(daAsset);
        createDaAssetThemeIdList(daAsset);
        updateDaAsset(daAsset);
        return 1;
    }

    private void updateDaAssetVideoNew(DaAssetSaveReqVO daAsset) {
        DaAssetVideoSaveReqVO daAssetVideo = daAsset.getDaAssetVideo();
        if (daAssetVideo == null) {
            return;
        }
        daAssetVideo.setAssetId(daAsset.getId());
        iDaAssetVideoService.updateDaAssetVideo(daAssetVideo);
    }

    private void updateDaAssetGisNew(DaAssetSaveReqVO daAsset) {
        DaAssetGisSaveReqVO daAssetGis = daAsset.getDaAssetGis();
        if (daAssetGis == null) {
            return;
        }
        daAssetGis.setAssetId(daAsset.getId());
        iDaAssetGisService.updateDaAssetGis(daAssetGis);
    }

    private void updateDaAssetGeoNew(DaAssetSaveReqVO daAsset) {
        DaAssetGeoSaveReqVO daAssetGeo = daAsset.getDaAssetGeo();
        if (daAssetGeo == null) {
            return;
        }
        daAssetGeo.setAssetId(daAsset.getId());
        iDaAssetGeoService.updateDaAssetGeo(daAssetGeo);
    }

    private void updateDaAssetApiNew(DaAssetSaveReqVO daAsset) {
        DaAssetApiSaveReqVO daAssetApi = daAsset.getDaAssetApi();
        if (daAssetApi == null) {
            return;
        }
        daAssetApi.setAssetId(daAsset.getId());
        iDaAssetApiService.updateDaAssetApi(daAssetApi);

        List<DaAssetApiParamSaveReqVO> daAssetApiParamList = daAsset.getDaAssetApiParamList();
        iDaAssetApiParamService.createDaAssetApiParamDeep(daAssetApiParamList, daAssetApi.getId());
    }

    private void updateDaAssetColumnNew(DaAssetSaveReqVO daAsset) {
        return;
    }

    private void updateDaAssetFilesNew(DaAssetSaveReqVO daAsset) {
        DaAssetFilesSaveReqVO daAssetFiles = daAsset.getDaAssetFiles();
        int lastDot = daAssetFiles.getUrl().lastIndexOf('.');
        String type = daAssetFiles.getUrl().substring(lastDot);
        daAssetFiles.setType(type);
        daAssetFiles.setAssetId(daAsset.getId());
        daAssetFilesService.updateDaAssetFiles(daAssetFiles);

        if (StringUtils.equalsIgnoreCase("xls", daAssetFiles.getType()) || StringUtils.equalsIgnoreCase("xlsx", daAssetFiles.getType())) {
            LambdaQueryWrapperX<DaAssetColumnDO> queryWrapperX = new LambdaQueryWrapperX<>();
            queryWrapperX.eq(DaAssetColumnDO::getAssetId, daAsset.getId());
            iDaAssetColumnService.remove(queryWrapperX);
            List<DaAssetColumnDO> daAssetColumnDOS = getExcelColumn(daAssetFiles.getUrl(), daAssetFiles.getStartColumn(), daAssetFiles.getStartData(), daAsset.getId());
            iDaAssetColumnService.saveBatch(daAssetColumnDOS);
        }
        if (StringUtils.equalsIgnoreCase("csv", daAssetFiles.getType())) {
            LambdaQueryWrapperX<DaAssetColumnDO> queryWrapperX = new LambdaQueryWrapperX<>();
            queryWrapperX.eq(DaAssetColumnDO::getAssetId, daAsset.getId());
            iDaAssetColumnService.remove(queryWrapperX);
            List<DaAssetColumnDO> daAssetColumnDOS = getCsvColumn(daAssetFiles.getUrl(), daAsset.getId());
            iDaAssetColumnService.saveBatch(daAssetColumnDOS);
        }
    }

    @Override
    public void startDaAssetDatasourceTaskNull() {
        this.startDaAssetDatasourceTask(null);
    }

    @Override
    public AjaxResult startDaAssetDatasourceTask(Long id) {
        if (id != null) {
            DaAssetRespVO daAssetById = this.getDaAssetById(id);
            if (StringUtils.equals("1", daAssetById.getType())) {
                // For special handling, fill in logic here
            }

            DaDatasourceDO daDatasourceById = iDaDatasourceService.getDaDatasourceById(daAssetById.getDatasourceId());
            DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceById.getDatasourceType(), daDatasourceById.getIp(), daDatasourceById.getPort(), daDatasourceById.getDatasourceConfig());
            if (!isCountSupported(dbQueryProperty.getDbType())) {
                throw new DataQueryException("db.error.datasource.type.unsupported", "This datasource type is currently not supported, please contact the administrator!");
            }

            DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
            if (!dbQuery.valid()) {
                throw new DataQueryException("db.error.connection.fail", "Database connection failed");
            }

            updateAssetFieldAndDataCount(dbQuery, dbQueryProperty, daAssetById);
            dbQuery.close();

        } else {
            DaAssetPageReqVO daAsset = new DaAssetPageReqVO();
            daAsset.setType("1");
            List<DaAssetDO> daAssetList = this.getDaAssetList(daAsset);

            Map<Long, List<DaAssetDO>> datasourceGroupMap = daAssetList.stream()
                    .collect(Collectors.groupingBy(DaAssetDO::getDatasourceId));

            for (Map.Entry<Long, List<DaAssetDO>> entry : datasourceGroupMap.entrySet()) {
                Long datasourceId = entry.getKey();
                List<DaAssetDO> assets = entry.getValue();

                DaDatasourceDO datasource = iDaDatasourceService.getDaDatasourceById(datasourceId);
                if (datasource == null) {
                    continue;
                }
                DbQueryProperty dbQueryProperty = new DbQueryProperty(datasource.getDatasourceType(), datasource.getIp(), datasource.getPort(), datasource.getDatasourceConfig());
                if (!isCountSupported(dbQueryProperty.getDbType())) {
                    continue;
                }

                DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
                try {
                    if (!dbQuery.valid()) {
                        // Log and skip this datasource
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }

                for (DaAssetDO asset : assets) {
                    try {
                        updateAssetFieldAndDataCount(dbQuery, dbQueryProperty, asset);
                    } catch (Exception e) {
                        log.error("Failed: {} ", asset);
                    }
                }

                dbQuery.close();
            }
        }

        return AjaxResult.success(MessageUtils.messageWithFallback("da.error.task.complete", "Task completed"));
    }

    @Override
    public PageResult<DaAssetDO> getDaAssetByIds(List<Long> ids) {
        DaAssetPageReqVO daAssetPageReqVO = new DaAssetPageReqVO();
        daAssetPageReqVO.setAssetIdList(ids);
        return daAssetMapper.selectPage(daAssetPageReqVO);
    }

    @Override
    public List<DaAssetColumnRelRuleVO> listRelRule(Long id, String type) {
        List<DaAssetColumnDO> assetColumns = daAssetColumnMapper.findByAssetId(id);
        if (assetColumns.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> columnIds = assetColumns.stream().map(DaAssetColumnDO::getId).collect(Collectors.toSet());
        List<DpDataElemAssetRelRespDTO> assetRelRespDTOS = iDpModelApiService.getDpDataElemListByColumnIdInApi(columnIds);
        if (assetRelRespDTOS.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> dataElemIds = assetRelRespDTOS.stream()
                .map(DpDataElemAssetRelRespDTO::getDataElemId)
                .map(Long::valueOf)
                .collect(Collectors.toSet());
        List<DpDataElemRuleRelRespDTO> ruleRelRespDTOS = elemRuleRelService.listByDataElemIdList(dataElemIds, type);
        if (ruleRelRespDTOS.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, List<Long>> map = assetRelRespDTOS.stream()
                .filter(i -> StringUtils.isNotEmpty(i.getColumnId()))
                .collect(Collectors.groupingBy(i -> Long.valueOf(i.getColumnId()), Collectors.mapping(i -> Long.valueOf(i.getDataElemId()), Collectors.toList())));
        return assetColumns.stream()
                .filter(assetColumn -> CollectionUtils.isNotEmpty(map.get(assetColumn.getId())))
                .map(assetColumn -> {
                    List<Long> temp = map.get(assetColumn.getId());
                    return ruleRelRespDTOS.stream()
                            .filter(i -> temp.contains(i.getDataElemId()))
                            .map(i -> new DaAssetColumnRelRuleVO(assetColumn, i))
                            .collect(Collectors.toList());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<DaAssetColumnRelRuleVO> listRelRule(Long datasourceId, String tableName, String type) {
        List<DaAssetDO> daAssetDOS = daAssetMapper.findByDatasourceIdAndTableName(datasourceId, tableName);
        if (daAssetDOS.isEmpty()) {
            return Collections.emptyList();
        }
        return daAssetDOS.stream()
                .map(i -> listRelRule(i.getId(), type))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public LineageDTO dataLineage(Long id) {
        // Get asset info
        DaAssetDO daAsset = this.getById(id);
        if (!StringUtils.equals("1", daAsset.getType())) {
            throw new ServiceException("da.error.asset.type.wrong", "Asset type error");
        }
        Long datasourceId = daAsset.getDatasourceId();
        // Get datasource connection info
        DaDatasourceDO datasource = iDaDatasourceService.getById(datasourceId);
        if (datasource == null) {
            throw new ServiceException("da.error.datasource.notfound", "Datasource info does not exist");
        }
        DbQueryProperty dbProperty = new DbQueryProperty(datasource.getDatasourceType(), datasource.getIp(), datasource.getPort(), datasource.getDatasourceConfig());
        DbDialect dbDialect = DialectFactory.getDialect(DbType.getDbType(dbProperty.getDbType()));
        String tableName = dbDialect.getTableName(dbProperty,daAsset.getTableName());

        LineageDTO lineageDTO = lineageDataService.lineage(dbProperty.trainToHostPort(), tableName);
        // Query the latest status of the current task based on task
        if (lineageDTO.getTasks() != null && lineageDTO.getTasks().size() > 0) {
            List<Long> ipList = lineageDTO.getTasks().stream().map(TaskNode::getTaskId).collect(Collectors.toList());
            Map<Long, TaskNode> taskNodeMap = lineageDTO.getTasks()
                    .stream()
                    .collect(Collectors.toMap(k -> k.getTaskId(), v -> v));
            dppEtlTaskInstanceService.getLastTaskInstance(ipList).forEach(taskInstance -> {
                TaskNode taskNode = taskNodeMap.get(taskInstance.getTaskId());
                if (taskNode != null) {
                    taskNode.setTaskStatus(taskInstance.getStatus());
                    taskNode.setTaskTime(taskInstance.getStartTime());
                }
            });
        }
        return lineageDTO;
    }

    @Override
    public List<DaAssetDO> getDaAssetListAll(DaAssetPageReqVO daAsset, String number) {
        List<DaAssetDO> daAssetDOPageResult = daAssetMapper.selectList();
        return daAssetDOPageResult;
    }

    @Override
    public List<TreeData> getTreeData() {
        List<TreeData> treeData = new ArrayList<>();

        treeData.add(TreeData.builder()
                .name("按业务分类")
                .type("0")
                .otherData(JSON.parseObject("{\"tooltipStr\":\"Primarily for business and analytics personnel. Categorized by actual business lines or departmental functions, facilitating quick identification of data for specific business scenarios.\"}"))
                .children(dmBusinessCategoryApiService.getTreeData("1"))
                .build());

        treeData.add(TreeData.builder()
                .name("按主题域")
                .type("0")
                .otherData(JSON.parseObject("{\"tooltipStr\":\"Primarily for architects and data developers. Divides global data by core business entities, suitable for cross-departmental data exploration and model design.\"}"))
                .children(dmThemeDomainApiService.getTreeData("1"))
                .build());

        treeData.add(TreeData.builder()
                .name("按数仓分层")
                .type("0")
                .otherData(JSON.parseObject("{\"tooltipStr\":\"Primarily for underlying data developers. Divided by data processing depth and flow architecture, facilitating lineage tracing and technical troubleshooting.\"}"))
                .children(dmDataLayerApiService.getTreeData("1"))
                .build());
        return treeData;
    }

    @Override
    public List<Long> createDaAssetBatchNew(List<DaAssetSaveReqVO> daAssetList) {
        if (daAssetList == null || daAssetList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> ids = new ArrayList<>(daAssetList.size());
        // Check if current metadata already exists
        if (this.count(Wrappers.lambdaQuery(DaAssetDO.class)
                .in(DaAssetDO::getTableId, daAssetList.stream()
                        .map(e -> e.getTableId())
                        .collect(Collectors.toList()))) > 0) {
            throw new ServiceException("da.error.elem.exists", "Some of the selected metadata already exists in assets!");
        }
        for (DaAssetSaveReqVO vo : daAssetList) {
            Long id = this.createDaAssetNew(vo);
            ids.add(id);
        }
        return ids;
    }

    @Override
    public List<Map<String, Object>> dataMaskings(Long assetId, List<Map<String, Object>> data, Long userId, String scene) {

        Map<String, Object>  mt  = new HashMap<>();// Stores final status: 1: no desensitization 2: desensitize
        List<Map<String, Object>> out = new ArrayList<>(data.size());
        Map<String, Object>  mk  = new HashMap<>(); // Stores replacement content
        List<DaAssetColumnDO> cols = daAssetColumnMapper.findByAssetId(assetId);
        for (DaAssetColumnDO col : cols) {
            // Get desensitization rule via DaAssetColumnDO's id
            DgDesensitizeAssetcolumnDO assetcolumnDO = dgDesensitizeAssetcolumnService.getDgDesensitizeAssetcolumnByAid(col.getId());
            // 1. Check if a category is bound
            if(assetcolumnDO == null){
                mt.put(col.getColumnName(),1);
            }else{
                // Get the associated rule object
                DgDesensitizeRuleDO rule = dgDesensitizeRuleService.getDgDesensitizeRuleByDataCategoryId(assetcolumnDO.getDataCategoryId());
                DgDesensitizeWhitelistDO white =  whitelistService.getDgDesensitizeWhitelistByCategoryId(assetcolumnDO.getDataCategoryId());
                // 2. Check if a rule is bound
                if(rule == null){
                    mt.put(col.getColumnName(),1);
                }else{
                    // 3. Check if rule is enabled or if scene requirement matches
                    if(!rule.getValidFlag()|| !rule.getApplicationScene().contains(scene)){
                        mt.put(col.getColumnName(),1);
                    }else{
                        mk.put("rp",rule.getReplaceContent());
                        // 4. Rule interval exists
                        if (rule.getIntervalList().size()>0){
                            mk.put("gz",rule.getIntervalList());
                            mt.put(col.getColumnName(),2);
                        }else{
                            mt.put(col.getColumnName(),1);
                        }
                    }

                }
                // Check if there is a whitelist
                if(white != null){
                    Date currtime = new Date();
                    boolean b= (!currtime.before(white.getStartTime()))  && (!currtime.after(white.getEndTime()));
                    boolean c= white.getUserList().stream()
                            .anyMatch(userRelDO -> userRelDO
                                    .getUserId() == userId);

                    if(white.getValidFlag()&& c && b){
                        mt.put(col.getColumnName(),1);
                    }
                }
            }


        }
        // Based on final status, assemble the data
        for (Map<String, Object> row : data) {
            // Use LinkedHashMap to maintain field order, without modifying the original map
            Map<String, Object> masked = new LinkedHashMap<>(row.size());
            for (Map.Entry<String, Object> e : row.entrySet()) {
                String key = e.getKey();
                Object val = e.getValue();
                if(mt.get(key).toString().equals("1")){
                    masked.put(key, val);
                }else if(mt.get(key).toString().equals("2")){
                    String s=desensitizeByInterval2((String) val, (String) mk.get("rp"),(List<DgDesensitizeIntervalDO>) mk.get("gz"));
                    masked.put(key, s);
                }
            }
            out.add(masked);
        }
        return out;
    }

    @Override
    public List<DaAssetDO> getDaAssetByDataSourceId(Long dataSourceId, String tableName) {
        return this.list(Wrappers.lambdaQuery(DaAssetDO.class)
                .eq(DaAssetDO::getDatasourceId, dataSourceId)
                .eq(DaAssetDO::getTableName, tableName));
    }

    // String replacement
    public static String desensitizeByInterval(String originalStr,
                                               String replaceStr,
                                               List<DgDesensitizeIntervalDO> intervalList) {
        // Null value validation
        if (originalStr == null || originalStr.isEmpty()) return originalStr;
        if (replaceStr == null || replaceStr.isEmpty()) return originalStr;
        if (intervalList == null || intervalList.isEmpty()) return originalStr;

        // Take the replacement character
        char replaceChar = replaceStr.charAt(0);

        // Create a character array based on the original string (all operations happen here, length unchanged)
        char[] chars = originalStr.toCharArray();
        int len = chars.length;

        // Sort by intervalNo (ensure order)
        intervalList.sort(Comparator.comparing(DgDesensitizeIntervalDO::getIntervalNo));

        // Iterate through all rules, directly replace at original indices
        for (DgDesensitizeIntervalDO interval : intervalList) {
            Long startL = interval.getStartNum();
            Long endL = interval.getEndNum();

            if (startL == null || endL == null) continue;

            int start = startL.intValue()-1;
            int end = endL.intValue()-1;

            // Index safety handling
            start = Math.max(start, 0);
            end = Math.min(end, len - 1);
            if (start > end) continue;

            // Replace character by character (all rules use original indices)
            for (int i = start; i <= end; i++) {
                chars[i] = replaceChar;
            }
        }
        return new String(chars);
    }

    public static String desensitizeByInterval2(String originalStr,
                                               String replaceStr,
                                               List<DgDesensitizeIntervalDO> intervalList) {
        if (originalStr == null || originalStr.isEmpty()) return originalStr;
        if (replaceStr == null || replaceStr.isEmpty()) return originalStr;
        if (intervalList == null || intervalList.isEmpty()) return originalStr;

        // 1. Sort by intervalNo
        List<DgDesensitizeIntervalDO> sortedList = new ArrayList<>(intervalList);
        sortedList.sort(Comparator.comparing(DgDesensitizeIntervalDO::getIntervalNo));

        // 2. Convert all intervals to original string positions
        StringBuilder sb = new StringBuilder(originalStr);
        int offset = 0; // Offset of length change after replacement

        // 3. Iterate and replace (key: always calculate based on original indices!)
        for (DgDesensitizeIntervalDO interval : sortedList) {
            Long s = interval.getStartNum();
            Long e = interval.getEndNum();
            if (s == null || e == null) continue;

            int start = s.intValue()-1;
            int end = e.intValue()-1;

            // Interval length
            int len = end - start + 1;
            if (len <= 0) continue;

            // Start replacement (key: based on original position + offset correction)
            int replaceStart = start - offset;
            if (replaceStart < 0) replaceStart = 0;

            // Replace entire interval with a single replacement character
            sb.replace(replaceStart, replaceStart + len, replaceStr);

            // Offset = total shortening length
            offset += (len - 1);
        }
        return sb.toString();
    }

    private void updateAssetFieldAndDataCount(DbQuery dbQuery, DbQueryProperty dbQueryProperty, DaAssetDO assetDO) {
        List<DbColumn> tableColumns = dbQuery.getTableColumns(dbQueryProperty, assetDO.getTableName());
        int tableColumnsSize = CollectionUtils.isEmpty(tableColumns) ? 0 : tableColumns.size();

        int dataCount = dbQuery.countNew(assetDO.getTableName(), new HashMap<>());

        DaAssetSaveReqVO updateObj = BeanUtils.toBean(assetDO, DaAssetSaveReqVO.class);
        updateObj.setFieldCount((long) tableColumnsSize);
        updateObj.setDataCount((long) dataCount);

        this.updateDaAsset(updateObj);
    }

    private void updateAssetFieldAndDataCount(DbQuery dbQuery, DbQueryProperty dbQueryProperty, DaAssetRespVO assetVO) {
        DaAssetDO assetDO = BeanUtils.toBean(assetVO, DaAssetDO.class);
        updateAssetFieldAndDataCount(dbQuery, dbQueryProperty, assetDO);
    }

    private boolean isCountSupported(String datasourceType) {
        return StringUtils.isNotBlank(datasourceType) && COUNT_SUPPORTED_TYPES.contains(datasourceType);
    }

    private static final Set<String> COUNT_SUPPORTED_TYPES = new HashSet<>(Arrays.asList(DbType.MYSQL.getDb(), DbType.ORACLE.getDb(), DbType.ORACLE_12C.getDb(), DbType.SQL_SERVER.getDb(), DbType.POSTGRE_SQL.getDb(), DbType.DM8.getDb(), DbType.KINGBASE8.getDb(), DbType.DORIS.getDb(), DbType.HIVE.getDb()));


    @Override
    public Map<String, Object> getDaAssetOverviewStatistics() {
        Map<String, Object> map = daAssetMapper.getDaAssetOverviewStatistics();

        int diffCount = MapUtils.getIntValue(map, "diffCount");
        int prevCount = MapUtils.getIntValue(map, "prevCount");

        BigDecimal growthRate = BigDecimal.ZERO;
        if (prevCount > 0) {
            growthRate = BigDecimal.valueOf(diffCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(prevCount), 2, RoundingMode.HALF_UP);
        } else if (diffCount != 0) {
            growthRate = BigDecimal.valueOf(diffCount);
        }
        map.put("growthRate", growthRate);
        return map;
    }


    @Override
    public int updateCatCode(String oldCatCode, String newCatCode) {
        return daAssetMapper.updateCatCode(oldCatCode, newCatCode);
    }

    @Override
    public List<Long> getMcTableInDaAsset(List<Long> mcTableIds) {
        List<DaAssetDO> daAssetDOList = this.list(Wrappers.lambdaQuery(DaAssetDO.class)
                .select(DaAssetDO::getTableId)
                .in(DaAssetDO::getTableId, mcTableIds));
        if (daAssetDOList != null || daAssetDOList.size() > 0) {
            return daAssetDOList.stream().map(DaAssetDO::getTableId)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void mcTableColumnUpdateToDaAssetColumn(Map<Long, List<McColumnRespDTO>> columnMap) {
        if (columnMap.isEmpty()) {
            return;
        }
        // Query asset by mcTableId
        List<DaAssetDO> daAssetDOList = this.list(Wrappers.lambdaQuery(DaAssetDO.class)
                .in(DaAssetDO::getTableId, columnMap.keySet()));
        for (DaAssetDO daAssetDO : daAssetDOList) {
            List<McColumnRespDTO> columnList = columnMap.get(daAssetDO.getTableId());
            if (columnList == null || columnList.size() == 0) {
                continue;
            }
            // Convert columnList List<McColumnRespDTO> to List<DaAssetColumnDO>
            List<DaAssetColumnDO> newAssetColumns = convertMcColumnToDaAssetColumn(daAssetDO,columnList);
            // Get existing fields
            List<DaAssetColumnDO> oldAssetColumns = iDaAssetColumnService.list(Wrappers.lambdaQuery(DaAssetColumnDO.class)
                    .eq(DaAssetColumnDO::getAssetId, daAssetDO.getId()));
            Map<String, List<DaAssetColumnDO>> compareResult = compareAssetColumns(newAssetColumns, oldAssetColumns);
            List<DaAssetColumnDO> addList = compareResult.get("addList");
            List<DaAssetColumnDO> updateList = compareResult.get("updateList");
            List<DaAssetColumnDO> deleteList = compareResult.get("deleteList");
            if (addList != null && addList.size() > 0) {
                // Add fields
                iDaAssetColumnService.saveBatch(addList);
            }
            if (updateList != null && updateList.size() > 0) {
                // Modify fields
                iDaAssetColumnService.updateBatchById(updateList);
            }
            if (deleteList != null && deleteList.size() > 0) {
                // Delete fields
                iDaAssetColumnService.removeByIds(deleteList
                        .stream()
                        .map(DaAssetColumnDO::getId).collect(Collectors.toList()));
            }
        }
    }

    private List<DaAssetColumnDO> convertMcColumnToDaAssetColumn(DaAssetDO daAssetDO,List<McColumnRespDTO> mcColumnList) {
        if (mcColumnList == null || mcColumnList.isEmpty()) {
            return new ArrayList<>(); // Return empty list
        }

        return mcColumnList.stream()
                .map(mcColumn -> {
                    DaAssetColumnDO assetColumn = new DaAssetColumnDO();

                    assetColumn.setAssetId(daAssetDO.getId());
                    // Map basic fields
                    assetColumn.setColumnName(mcColumn.getColumnName());
                    assetColumn.setColumnComment(mcColumn.getColumnComment());
                    assetColumn.setColumnType(mcColumn.getColumnType());
                    assetColumn.setColumnLength(mcColumn.getColumnLength() != null ? mcColumn.getColumnLength()
                                                                                     .longValue() : null);
                    assetColumn.setColumnScale(mcColumn.getColumnScale() != null ? mcColumn.getColumnScale()
                                                                                   .longValue() : null);
                    assetColumn.setNullableFlag(mcColumn.getNullableFlag());
                    assetColumn.setPkFlag(mcColumn.getPkFlag());
                    assetColumn.setDefaultValue(mcColumn.getDefaultValue());

                    // Set other optional fields (can be obtained from metadata or set to defaults if needed)
                    assetColumn.setDataElemCodeFlag("0"); // Default is not a code
                    assetColumn.setRelDataElmeFlag("0"); // Default: not associated with a data element
                    assetColumn.setRelCleanFlag("0"); // Default: not associated with a cleansing rule
                    assetColumn.setRelAuditFlag("0"); // Default: not associated with an audit rule
                    return assetColumn;
                })
                .collect(Collectors.toList());
    }

    /**
     * Compare new and old field lists, returning fields to delete, modify, and add
     *
     * @param newAssetColumns New field list
     * @param oldAssetColumns Old field list
     * @return Map containing three lists: deleteList (to delete), updateList (to modify), addList (to add)
     */
    public Map<String, List<DaAssetColumnDO>> compareAssetColumns(List<DaAssetColumnDO> newAssetColumns,
                                                                  List<DaAssetColumnDO> oldAssetColumns) {
        // Initialize result Map
        Map<String, List<DaAssetColumnDO>> result = new HashMap<>();
        List<DaAssetColumnDO> deleteList = new ArrayList<>();
        List<DaAssetColumnDO> updateList = new ArrayList<>();
        List<DaAssetColumnDO> addList = new ArrayList<>();

        // Use columnName as the unique identifier for comparison
        // Convert the old field list to a Map keyed by columnName for easy lookup
        Map<String, DaAssetColumnDO> oldColumnMap = oldAssetColumns.stream()
                .collect(Collectors.toMap(DaAssetColumnDO::getColumnName, column -> column));

        // Convert the new field list to a Map keyed by columnName for easy lookup
        Map<String, DaAssetColumnDO> newColumnMap = newAssetColumns.stream()
                .collect(Collectors.toMap(DaAssetColumnDO::getColumnName, column -> column));

        // 1. Find fields to delete (present in old but not in new)
        for (DaAssetColumnDO oldColumn : oldAssetColumns) {
            if (!newColumnMap.containsKey(oldColumn.getColumnName())) {
                deleteList.add(oldColumn);
            }
        }

        // 2. Find fields to add (present in new but not in old)
        for (DaAssetColumnDO newColumn : newAssetColumns) {
            if (!oldColumnMap.containsKey(newColumn.getColumnName())) {
                addList.add(newColumn);
            }
        }

        // 3. Find fields to modify (present in both but with different properties)
        for (DaAssetColumnDO newColumn : newAssetColumns) {
            DaAssetColumnDO oldColumn = oldColumnMap.get(newColumn.getColumnName());
            if (oldColumn != null) {
                // Compare whether key properties of the field have changed
                if (isColumnChanged(newColumn, oldColumn)) {
                    // If you need to know exactly which fields changed, you can also save oldColumn
                    // Here we add the new version to the update list
                    updateList.add(newColumn);
                }
            }
        }

        result.put("deleteList", deleteList);
        result.put("updateList", updateList);
        result.put("addList", addList);
        return result;
    }

    /**
     * Determine whether the properties of a field have changed
     *
     * @param newColumn New field
     * @param oldColumn Old field
     * @return Whether there is a change
     */
    private boolean isColumnChanged(DaAssetColumnDO newColumn, DaAssetColumnDO oldColumn) {
        // Compare column type
        if (!Objects.equals(newColumn.getColumnType(), oldColumn.getColumnType())) {
            return true;
        }

        // Compare column length
        if (!Objects.equals(newColumn.getColumnLength(), oldColumn.getColumnLength())) {
            return true;
        }

        // Compare decimal places
        if (!Objects.equals(newColumn.getColumnScale(), oldColumn.getColumnScale())) {
            return true;
        }

        // Compare primary key
        if (!Objects.equals(newColumn.getPkFlag(), oldColumn.getPkFlag())) {
            return true;
        }

        // Compare required flag
        if (!Objects.equals(newColumn.getNullableFlag(), oldColumn.getNullableFlag())) {
            return true;
        }

        // Compare default value
        if (!Objects.equals(newColumn.getDefaultValue(), oldColumn.getDefaultValue())) {
            return true;
        }

        // Compare column comment
        if (!Objects.equals(newColumn.getColumnComment(), oldColumn.getColumnComment())) {
            return true;
        }
        return false;
    }


    @Override
    public boolean existsByTableId(Long tableId) {
        if (tableId == null) {
            return false;
        }
        Long count = baseMapper.selectCount(Wrappers.lambdaQuery(DaAssetDO.class)
                .eq(DaAssetDO::getTableId, tableId)
                .eq(DaAssetDO::getDelFlag, "0"));
        return count != null && count > 0;
    }
}
