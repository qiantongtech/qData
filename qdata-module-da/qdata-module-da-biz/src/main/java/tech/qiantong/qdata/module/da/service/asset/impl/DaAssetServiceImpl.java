/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.ExcelToCsvUtil;
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
 * 数据资产Service业务层处理
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

    //引入脱敏清单关联关系
    @Resource
    private IDgDesensitizeAssetcolumnService dgDesensitizeAssetcolumnService;
    //引入脱敏规则
    @Resource
    private IDgDesensitizeRuleService dgDesensitizeRuleService;

    //引入白名单
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
        //根据模型id查询信息
        DpModelRespDTO dpModelByIdApi = iDpModelApiService.getDpModelByIdApi(daAssetReqDTO.getModelId());
        if (dpModelByIdApi == null) {
            throw new ServiceException("da.error.model.notfound", "模型不存在");
        }
        DaAssetDO daAssetDO = new DaAssetDO();
        daAssetDO.setName(dpModelByIdApi.getModelComment());
        daAssetDO.setCatCode(dpModelByIdApi.getCatCode());
        daAssetDO.setDatasourceId(daAssetReqDTO.getDatasourceId());
        daAssetDO.setSource(daAssetReqDTO.getSource());
        daAssetDO.setTableName(dpModelByIdApi.getTableName());
        daAssetDO.setTableComment(dpModelByIdApi.getModelComment());
        daAssetDO.setFieldCount(daAssetReqDTO.getFieldCount());//字段量

        //读取模型的数据设置进去
        daAssetDO.setTableType(dpModelByIdApi.getTableType());
        daAssetDO.setDataLayerId(dpModelByIdApi.getDataLayerId());
        daAssetDO.setBusinessCategoryId(dpModelByIdApi.getBusinessCategoryId());
        daAssetDO.setBusinessCategoryCode(dpModelByIdApi.getBusinessCategoryCode());
        daAssetDO.setDataDomainId(dpModelByIdApi.getDataDomainId());
        daAssetDO.setThemeDomainId(dpModelByIdApi.getThemeDomainId());
        daAssetDO.setThemeDomainCode(dpModelByIdApi.getThemeDomainCode());
        daAssetDO.setTableCase(dpModelByIdApi.getTableCase());

        //判断是否存在资产
        DaAssetPageReqVO daAssetPageReqVO = new DaAssetPageReqVO();
        daAssetPageReqVO.setTableName(dpModelByIdApi.getTableName());
        daAssetPageReqVO.setDatasourceId(String.valueOf(daAssetReqDTO.getDatasourceId()));
        DaAssetDO assetDO = this.getDaAssetByDaAssetPageReqVO(daAssetPageReqVO);
        if (assetDO != null) {
            daAssetDO.setId(assetDO.getId());
            daAssetMapper.updateById(daAssetDO);//修改资产数据
            // 删除字段缓存
            redisCache.deleteObject(CacheConstants.ASSET_PREVIEW_KEY + daAssetReqDTO.getId() + "_" + dpModelByIdApi.getTableName());
        } else {
            daAssetMapper.insert(daAssetDO);//添加资产数据
        }

        //查询逻辑模型属性
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
        //批量保存数据资产字段
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

        //设置数据元数据资产关联信息
        Set<Long> ids = dpModelColumnListByModelIdApi.stream()
                .map(DpModelColumnRespDTO::getDataElemId)
                .collect(Collectors.toSet());
        //id数据不为空
        if (StringUtils.isNotEmpty(ids)) {
            List<DpDataElemRespDTO> dpDataElemListByIdsApi = iDpModelApiService.getDpDataElemListByIdsApi(ids);
            List<DpDataElemAssetRelReqDTO> dpDataElemAssetRel = new ArrayList<>();
            dpDataElemListByIdsApi.forEach(dpDataElemRespDTO -> {
                DpDataElemAssetRelReqDTO dpDataElemAssetRelReqDTO = new DpDataElemAssetRelReqDTO();
                //设置资产id
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
                    throw new ServiceException("da.error.elem.save", "数据元数据资产关联信息保存失败");
                }
            }
        }
        DaAssetRespDTO result = new DaAssetRespDTO();
        result.setId(daAssetDO.getId());//资产id
        return result;
    }

    /**
     * 使用流处理方式找出 daAssetColumnList 中存在但 daAssetColumnDOList 中不存在的记录，
     * 匹配规则基于 columnName（采用 StringUtils.equals 比较），返回这些记录的 id 集合。
     *
     * @param daAssetColumnDOList 已存在的记录列表
     * @param daAssetColumnList   需要检查的记录列表
     * @return 匹配到的 id 集合
     */
    public static Collection<Long> findNonExistingIdList(List<DaAssetColumnDO> daAssetColumnDOList, List<DaAssetColumnDO> daAssetColumnList) {
        // 提取已存在列表中所有非空的 columnName 到一个 Set 中
        Set<String> existingNames = daAssetColumnDOList == null ? null : daAssetColumnDOList.stream()
                                                                         .filter(asset -> StringUtils.isNotBlank(asset.getColumnName()))
                                                                         .map(DaAssetColumnDO::getColumnName)
                                                                         .collect(Collectors.toSet());

        // 对待匹配列表进行过滤，保留 columnName 不在 existingNames 中的记录，并收集其 id
        return daAssetColumnList == null ? null : daAssetColumnList.stream()
                                                  .filter(asset -> StringUtils.isNotBlank(asset.getColumnName()))
                                                  .filter(asset -> existingNames == null || existingNames.stream()
                                                                                            .noneMatch(name -> StringUtils.equals(name, asset.getColumnName())))
                                                  .map(DaAssetColumnDO::getId)
                                                  .collect(Collectors.toList());
    }

    /**
     * 根据 dpModelColumnRespDTO 的 engName 匹配 daAssetColumnList 中对应的 DaAssetColumnDO 对象
     *
     * @param daAssetColumnList    数据资产字段列表
     * @param dpModelColumnRespDTO 模型列响应 DTO，包含 engName 属性
     * @return 匹配到的 DaAssetColumnDO 对象，未匹配到返回 null
     */
    public static DaAssetColumnDO matchColumn(List<DaAssetColumnDO> daAssetColumnList, DpModelColumnRespDTO dpModelColumnRespDTO) {
        if (daAssetColumnList == null || dpModelColumnRespDTO == null || dpModelColumnRespDTO.getEngName() == null) {
            return null;
        }
        for (DaAssetColumnDO daAssetColumnDO : daAssetColumnList) {
            // 当字段名称匹配时，返回该对象
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
            throw new ServiceException("da.error.datasource.id.empty", "数据源id不能为空");
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
     * 1-数据资产
     * 2-数据研发
     */
    @Override
    public PageResult<DaAssetDO> getDaAssetPage(DaAssetPageReqVO pageReqVO, String daAssetQueryType) {
        PageResult<DaAssetDO> daAssetDOPageResult = daAssetMapper.selectPage(pageReqVO);
        List<DaAssetDO> daAssetDOList = (List<DaAssetDO>) daAssetDOPageResult.getRows();
        for (DaAssetDO daAssetDO : daAssetDOList) {
            //判断是否是api
            if (StringUtils.equals("2", daAssetDO.getType())) {
                DaAssetApiRespVO daAssetApiByAssetId = iDaAssetApiService.getDaAssetApiByAssetId(daAssetDO.getId());
                daAssetDO.setDaAssetApi(daAssetApiByAssetId);
            }
//            //判断是否是数据源
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

        //拼接查询标签列表
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
        //1:数据库表  2:外部API 3: 地理空间服务 4:矢量数据 5:视频数据
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
        // 相关校验

        // 更新数据资产
        DaAssetDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetDO.class);
        return daAssetMapper.updateById(updateObj);
    }

    @Override
    public int removeDaAsset(Collection<Long> idList) {
        ArrayList<Long> assetIdList = new ArrayList<>(idList);
        int asset = dppEtlTaskService.checkTaskIdInAsset(assetIdList);
        if (asset > 0) {
            throw new ServiceException("da.error.delete.project.ref", "删除失败,资产被项目引用!");
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
        // 批量删除数据资产
        return daAssetMapper.deleteBatchIds(idList);
    }

    @Override
    public int removeDaAsset(Long id) {
        ArrayList<Long> assetIdList = new ArrayList<>();
        assetIdList.add(id);
        int asset = dppEtlTaskService.checkTaskIdInAsset(assetIdList);
        if (asset > 0) {
            throw new ServiceException("da.error.delete.project.ref", "删除失败,资产被项目引用!");
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
        //删除项目
        iDaAssetProjectRelService.removeProjectRelByAssetId(id);
        //删除主题
        daAssetThemeRelService.removeThemeRelByAssetId(id);

        daAssetMapper.deleteAssetById(id);
        // 批量删除数据资产

        // 更新标签资产数量
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
                // 保留已存在的值
                (existing, replacement) -> existing));
    }


    /**
     * 导入数据资产数据
     *
     * @param importExcelList 数据资产数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDaAsset(List<DaAssetRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "导入数据不能为空！");
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
                                    "数据更新成功，ID为 " + daAssetId + " 的数据资产记录。", daAssetId, "数据资产"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "数据更新失败，ID为 " + daAssetId + " 的数据资产记录不存在。", daAssetId, "数据资产"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DaAssetDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetId);
                    DaAssetDO existingDaAsset = daAssetMapper.selectOne(queryWrapper);
                    if (existingDaAsset == null) {
                        daAssetMapper.insert(daAssetDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "数据插入成功，ID为 " + daAssetId + " 的数据资产记录。", daAssetId, "数据资产"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "数据插入失败，ID为 " + daAssetId + " 的数据资产记录已存在。", daAssetId, "数据资产"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
        }
        return resultMsg.toString();
    }

    /**
     * 数据资产预览带有脱敏规则后的数据预览
     *
     * @param jsonObject 主键id和条件查询的内容
     * @return
     */
    @Override
    public Map<String, Object> getColumnData(JSONObject jsonObject) {
        String tableName = "";
        Long dataSourceId = null;
        if (StringUtils.isEmpty(jsonObject.getStr("pageNum")) || StringUtils.isEmpty(jsonObject.getStr("pageSize"))) {
            throw new DataQueryException("db.error.pagination.missing", "请携带页码与每页条数！");
        }
        // 查询数据
        Integer pageNum = Integer.valueOf(jsonObject.getStr("pageNum"));
        Integer pageSize = Integer.valueOf(jsonObject.getStr("pageSize"));
        if (StringUtils.isNotEmpty(jsonObject.getStr("taskId")) && StringUtils.isNotEmpty(jsonObject.getStr("tableName"))) {
            DaDiscoveryTaskDO discoveryTaskDO = daDiscoveryTaskService.getById(Long.valueOf(jsonObject.getStr("taskId")));
            tableName = jsonObject.getStr("tableName");
            dataSourceId = discoveryTaskDO.getDatasourceId();
        } else {
            // 获取资产详情
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
        // 获取数据源连接信息
        DaDatasourceDO daDatasourceDO = daDatasourceMapper.selectById(dataSourceId);
        if (daDatasourceDO == null) {
            return null;
        }
        DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceDO.getDatasourceType(), daDatasourceDO.getIp(), daDatasourceDO.getPort(), daDatasourceDO.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        DbDialect dbDialect = DialectFactory.getDialect(DbType.getDbType(dbQueryProperty.getDbType()));
        if (!dbQuery.valid()) {
            dbQuery.close();
            throw new DataQueryException("db.error.connection.fail", "数据库连接失败");
        }
        int existsSQL = dbQuery.generateCheckTableExistsSQL(dbQueryProperty, tableName);
        if (existsSQL == 0) {
            dbQuery.close();
            throw new DataQueryException("db.error.table.missing", "数据库中未获取到该表数据，请确认表是否存在");
        }
        // 获取字段集合
        List<DbColumn> columns = redisCache.getCacheList(CacheConstants.ASSET_PREVIEW_KEY + daDatasourceDO.getId() + "_" + tableName);
        // 获取资产的字段
        List<DbColumn> daAssetColumns = daAssetColumnMapper.findByAssetId(Long.parseLong(jsonObject.getStr("id")))
                .stream()
                .map(e -> e.toDbColumn())
                .collect(Collectors.toList());
        if (columns.isEmpty()) {
            //获取表的字段
            columns = dbQuery.getTableColumns(dbQueryProperty, tableName);
            if (columns.size() == 0) {
                dbQuery.close();
                throw new DataQueryException("db.error.connection.fail", "数据库连接失败");
            }
            redisCache.setCacheList(CacheConstants.ASSET_PREVIEW_KEY + daDatasourceDO.getId() + "_" + tableName, columns);
            redisCache.expire(CacheConstants.ASSET_PREVIEW_KEY + daDatasourceDO.getId() + "_" + tableName, 5, TimeUnit.MINUTES);
        }
        // 拼接查询sql语句
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
        // 1) 字段元数据（按字段名大写匹配）
        List<DaAssetColumnDO> cols = daAssetColumnMapper.findByAssetId(assetId);
        Map<String, DaAssetColumnDO> colMap = cols.stream()
                .collect(Collectors.toMap(c -> c.getColumnName().toUpperCase(), c -> c, (a, b) -> a));

        // 2) 敏感等级（仅在线）
        Map<Long, DaSensitiveLevelDO> levelMap = daSensitiveLevelMapper.selectList(new QueryWrapper<DaSensitiveLevelDO>().eq("online_flag", 1))
                .stream()
                .collect(Collectors.toMap(DaSensitiveLevelDO::getId, x -> x, (a, b) -> a));

        List<Map<String, Object>> out = new ArrayList<>(data.size());

        for (Map<String, Object> row : data) {
            // 用 LinkedHashMap 保持字段顺序，且不修改原 map
            Map<String, Object> masked = new HashMap<>(row.size());

            for (Map.Entry<String, Object> e : row.entrySet()) {
                String key = e.getKey();
                Object val = e.getValue();

                // 保证 _id 始终是字符串
                if ("_id".equalsIgnoreCase(key) && val != null && "org.bson.types.ObjectId".equals(val.getClass()
                        .getName())) {
                    val = val.toString();
                    masked.put(key, val);
                    continue;
                }

                // —— 未匹配到配置 或 无敏感等级 → 原样返回
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


                // 仅对字符串脱敏；其他类型原样返回
                if (!(val instanceof CharSequence)) {
                    masked.put(key, val);
                    continue;
                }

                String s = val == null ? null : val.toString();
                if (s == null || s.isEmpty()) {
                    masked.put(key, s);
                    continue;
                }

                // 起止位置：start/end 为 1 基；null 则全覆盖
                int len = s.length();
                int start = lvl.getStartCharLoc() == null ? 1 : lvl.getStartCharLoc().intValue();
                int end = lvl.getEndCharLoc() == null ? len : lvl.getEndCharLoc().intValue();

                // 规范边界并保证 start<=end
                start = Math.max(1, start);
                end = Math.min(len, end);
                if (start > end) { // 无有效覆盖区间 → 原样
                    masked.put(key, s);
                    continue;
                }

                String maskUnit = lvl.getMaskCharacter();
                if (maskUnit == null || maskUnit.isEmpty()) maskUnit = "*";

                int coverLen = end - start + 1;
                String midMask = repeat(maskUnit, coverLen); // 支持多字符掩码，不会位移

                String res = s.substring(0, start - 1) + midMask + s.substring(end);
                masked.put(key, res);
            }

            out.add(masked);
        }

        return out;
    }

    /**
     * 生成指定长度的掩码字符串（maskUnit 可为多字符）
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
//     * 对数据资产的数据进行脱敏
//     *
//     * @param id   数据资产id
//     * @param data 数据资产的数据
//     * @return
//     */
//    @Override
//    public List<Map<String, Object>> dataMasking(Long id, List<Map<String, Object>> data) {
//        // 根据资产详情进行查询字段属性
//        List<DaAssetColumnDO> assetColumnDOList = daAssetColumnMapper.findByAssetId(id);
//        // 将字段名称转成大写然后转成map类型，key为大写的字段名称，value是实体类
//        Map<String, DaAssetColumnDO> columnDOMap = assetColumnDOList.stream().collect(Collectors.toMap(
//                daAssetColumnDO -> daAssetColumnDO.getColumnName().toUpperCase(), daAssetColumnDO -> daAssetColumnDO));
//        // 查询敏感等级并转成map类型，key为脱敏等级id，value是实体类
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
//                    // 获取起始位置和结束位置
//                    int startCharLoc = daSensitiveLevelDO.getStartCharLoc() == null ? 0 : daSensitiveLevelDO.getStartCharLoc().intValue();
//                    int endCharLoc = daSensitiveLevelDO.getEndCharLoc() == null ? stringBuilder.length() : daSensitiveLevelDO.getEndCharLoc().intValue();
//                    // 把字符串进行替换
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
        //判断是否存在资产
        DaAssetPageReqVO daAssetPageReqVO = new DaAssetPageReqVO();
        daAssetPageReqVO.setTableName(daAssetDO.getTableName());
        daAssetPageReqVO.setDatasourceId(String.valueOf(daAssetDO.getDatasourceId()));
        DaAssetDO assetDO = this.getDaAssetByDaAssetPageReqVO(daAssetPageReqVO);
        if (assetDO != null) {
            daAssetDO.setId(assetDO.getId());
            daAssetMapper.updateById(daAssetDO);//添加资产数据
        } else {
            daAssetMapper.insert(daAssetDO);//添加资产数据
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
        //判断是否存在资产
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
        //1:数据库表  2:外部API 3: 地理空间服务 4:矢量数据 5:视频数据
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
            throw new ServiceException("da.error.type.unsupported", "类型暂不支持！");
        }

        createDaAssetProjectRel(daAsset);
        createDaAssetThemeIdList(daAsset);

        return daAsset.getId();
    }

    @Override
    public Long createDaAssetBindResources(DaAssetSaveReqVO daAsset) {
        //1:数据库表  2:外部API 3: 地理空间服务 4:矢量数据 5:视频数据
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
            throw new ServiceException("da.error.type.unsupported", "类型暂不支持！");
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
        Assert.notNull(daAsset.getFileInfo(), () -> new ServiceException("da.error.file.path.missing", "缺少文件路径"));
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
                throw new ServiceException("da.error.file.column.format", "附件中列名格式有误，请检查!");
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
                throw new ServiceException("da.error.file.column.format", "附件中列名格式有误，请检查!");
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
     * 主题
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
     * 字段
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
        //1:数据库表  2:外部API 3: 地理空间服务 4:矢量数据 5:视频数据
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
                // 如需特殊处理，填写逻辑
            }

            DaDatasourceDO daDatasourceById = iDaDatasourceService.getDaDatasourceById(daAssetById.getDatasourceId());
            DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceById.getDatasourceType(), daDatasourceById.getIp(), daDatasourceById.getPort(), daDatasourceById.getDatasourceConfig());
            if (!isCountSupported(dbQueryProperty.getDbType())) {
                throw new DataQueryException("db.error.datasource.type.unsupported", "暂不支持此类型数据源，请联系管理员！");
            }

            DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
            if (!dbQuery.valid()) {
                throw new DataQueryException("db.error.connection.fail", "数据库连接失败");
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
                        // 记录日志并跳过该数据源
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }

                for (DaAssetDO asset : assets) {
                    try {
                        updateAssetFieldAndDataCount(dbQuery, dbQueryProperty, asset);
                    } catch (Exception e) {
                        log.error("失败：{} ", asset);
                    }
                }

                dbQuery.close();
            }
        }

        return AjaxResult.success("任务完成");
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
        //获取资产信息
        DaAssetDO daAsset = this.getById(id);
        if (!StringUtils.equals("1", daAsset.getType())) {
            throw new ServiceException("da.error.asset.type.wrong", "资产类型错误");
        }
        Long datasourceId = daAsset.getDatasourceId();
        //获取数据源连接信息
        DaDatasourceDO datasource = iDaDatasourceService.getById(datasourceId);
        if (datasource == null) {
            throw new ServiceException("da.error.datasource.notfound", "数据源信息不存在");
        }
        DbQueryProperty dbProperty = new DbQueryProperty(datasource.getDatasourceType(), datasource.getIp(), datasource.getPort(), datasource.getDatasourceConfig());
        DbDialect dbDialect = DialectFactory.getDialect(DbType.getDbType(dbProperty.getDbType()));
        String tableName = dbDialect.getTableName(dbProperty,daAsset.getTableName());

        LineageDTO lineageDTO = lineageDataService.lineage(dbProperty.trainToHostPort(), tableName);
        //根据task查询当前任务最新的状态
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
                .otherData(JSON.parseObject("{\"tooltipStr\":\"主要面向业务与分析人员。按实际业务线或部门职能归类，方便快速定位特定业务场景的数据。\"}"))
                .children(dmBusinessCategoryApiService.getTreeData("1"))
                .build());

        treeData.add(TreeData.builder()
                .name("按主题域")
                .type("0")
                .otherData(JSON.parseObject("{\"tooltipStr\":\"主要面向架构与数据开发人员。按核心业务实体划分全局数据，适用于跨部门的数据探索与模型设计。\"}"))
                .children(dmThemeDomainApiService.getTreeData("1"))
                .build());

        treeData.add(TreeData.builder()
                .name("按数仓分层")
                .type("0")
                .otherData(JSON.parseObject("{\"tooltipStr\":\"主要面向底层数据开发人员。按数据加工深度与流转架构划分，方便溯源血缘链路和进行技术排查。\"}"))
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
        //判断当前元数据是否存在
        if (this.count(Wrappers.lambdaQuery(DaAssetDO.class)
                .in(DaAssetDO::getTableId, daAssetList.stream()
                        .map(e -> e.getTableId())
                        .collect(Collectors.toList()))) > 0) {
            throw new ServiceException("da.error.elem.exists", "当前所选的元数据部分已在资产中存在！");
        }
        for (DaAssetSaveReqVO vo : daAssetList) {
            Long id = this.createDaAssetNew(vo);
            ids.add(id);
        }
        return ids;
    }

    @Override
    public List<Map<String, Object>> dataMaskings(Long assetId, List<Map<String, Object>> data, Long userId, String scene) {

        Map<String, Object>  mt  = new HashMap<>();//存放 最终状态 1：不脱敏 2：脱敏
        List<Map<String, Object>> out = new ArrayList<>(data.size());
        Map<String, Object>  mk  = new HashMap<>(); //存放 替换内容
        List<DaAssetColumnDO> cols = daAssetColumnMapper.findByAssetId(assetId);
        for (DaAssetColumnDO col : cols) {
            //通过DaAssetColumnDO的id获取 脱敏规则
            DgDesensitizeAssetcolumnDO assetcolumnDO = dgDesensitizeAssetcolumnService.getDgDesensitizeAssetcolumnByAid(col.getId());
            //1.判断是否绑定了分类
            if(assetcolumnDO == null){
                mt.put(col.getColumnName(),1);
            }else{
                //获取 关联的规则对象
                DgDesensitizeRuleDO rule = dgDesensitizeRuleService.getDgDesensitizeRuleByDataCategoryId(assetcolumnDO.getDataCategoryId());
                DgDesensitizeWhitelistDO white =  whitelistService.getDgDesensitizeWhitelistByCategoryId(assetcolumnDO.getDataCategoryId());
                //2.判断是否 绑定了 规则
                if(rule == null){
                    mt.put(col.getColumnName(),1);
                }else{
                    //3.判断规则是否 启用 或者 场景要求符合
                    if(!rule.getValidFlag()|| !rule.getApplicationScene().contains(scene)){
                        mt.put(col.getColumnName(),1);
                    }else{
                        mk.put("rp",rule.getReplaceContent());
                        //4.存在规则区间
                        if (rule.getIntervalList().size()>0){
                            mk.put("gz",rule.getIntervalList());
                            mt.put(col.getColumnName(),2);
                        }else{
                            mt.put(col.getColumnName(),1);
                        }
                    }

                }
                //判断是否 有白名单
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
        //根据 最终状态，封装数据
        for (Map<String, Object> row : data) {
            // 用 LinkedHashMap 保持字段顺序，且不修改原 map
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

    //字符串替换
    public static String desensitizeByInterval(String originalStr,
                                               String replaceStr,
                                               List<DgDesensitizeIntervalDO> intervalList) {
        // 空值校验
        if (originalStr == null || originalStr.isEmpty()) return originalStr;
        if (replaceStr == null || replaceStr.isEmpty()) return originalStr;
        if (intervalList == null || intervalList.isEmpty()) return originalStr;

        // 取替换字符
        char replaceChar = replaceStr.charAt(0);

        // 基于原始字符串创建字符数组（所有操作都在这里，长度不变）
        char[] chars = originalStr.toCharArray();
        int len = chars.length;

        // 按 intervalNo 排序（保证顺序）
        intervalList.sort(Comparator.comparing(DgDesensitizeIntervalDO::getIntervalNo));

        // 遍历所有规则，直接替换原始下标
        for (DgDesensitizeIntervalDO interval : intervalList) {
            Long startL = interval.getStartNum();
            Long endL = interval.getEndNum();

            if (startL == null || endL == null) continue;

            int start = startL.intValue()-1;
            int end = endL.intValue()-1;

            // 下标安全处理
            start = Math.max(start, 0);
            end = Math.min(end, len - 1);
            if (start > end) continue;

            // 逐位替换（所有规则都用原始下标）
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

        // 1. 按 intervalNo 排序
        List<DgDesensitizeIntervalDO> sortedList = new ArrayList<>(intervalList);
        sortedList.sort(Comparator.comparing(DgDesensitizeIntervalDO::getIntervalNo));

        // 2. 把所有区间转成 原始字符串的位置
        StringBuilder sb = new StringBuilder(originalStr);
        int offset = 0; // 替换后长度变化的偏移量

        // 3. 遍历替换（关键：永远基于原始下标计算！）
        for (DgDesensitizeIntervalDO interval : sortedList) {
            Long s = interval.getStartNum();
            Long e = interval.getEndNum();
            if (s == null || e == null) continue;

            int start = s.intValue()-1;
            int end = e.intValue()-1;

            // 区间长度
            int len = end - start + 1;
            if (len <= 0) continue;

            // 开始替换（关键：基于原始位置 + 偏移修正）
            int replaceStart = start - offset;
            if (replaceStart < 0) replaceStart = 0;

            // 整个区间替换成 1 个替换符
            sb.replace(replaceStart, replaceStart + len, replaceStr);

            // 偏移量 = 总缩短长度
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
        //通过mcTableId查询资产
        List<DaAssetDO> daAssetDOList = this.list(Wrappers.lambdaQuery(DaAssetDO.class)
                .in(DaAssetDO::getTableId, columnMap.keySet()));
        for (DaAssetDO daAssetDO : daAssetDOList) {
            List<McColumnRespDTO> columnList = columnMap.get(daAssetDO.getTableId());
            if (columnList == null || columnList.size() == 0) {
                continue;
            }
            //columnList List<McColumnRespDTO> 转为 List<DaAssetColumnDO>
            List<DaAssetColumnDO> newAssetColumns = convertMcColumnToDaAssetColumn(daAssetDO,columnList);
            //获取原有字段
            List<DaAssetColumnDO> oldAssetColumns = iDaAssetColumnService.list(Wrappers.lambdaQuery(DaAssetColumnDO.class)
                    .eq(DaAssetColumnDO::getAssetId, daAssetDO.getId()));
            Map<String, List<DaAssetColumnDO>> compareResult = compareAssetColumns(newAssetColumns, oldAssetColumns);
            List<DaAssetColumnDO> addList = compareResult.get("addList");
            List<DaAssetColumnDO> updateList = compareResult.get("updateList");
            List<DaAssetColumnDO> deleteList = compareResult.get("deleteList");
            if (addList != null && addList.size() > 0) {
                //添加字段
                iDaAssetColumnService.saveBatch(addList);
            }
            if (updateList != null && updateList.size() > 0) {
                //修改字段
                iDaAssetColumnService.updateBatchById(updateList);
            }
            if (deleteList != null && deleteList.size() > 0) {
                //删除字段
                iDaAssetColumnService.removeByIds(deleteList
                        .stream()
                        .map(DaAssetColumnDO::getId).collect(Collectors.toList()));
            }
        }
    }

    private List<DaAssetColumnDO> convertMcColumnToDaAssetColumn(DaAssetDO daAssetDO,List<McColumnRespDTO> mcColumnList) {
        if (mcColumnList == null || mcColumnList.isEmpty()) {
            return new ArrayList<>(); // 返回空列表
        }

        return mcColumnList.stream()
                .map(mcColumn -> {
                    DaAssetColumnDO assetColumn = new DaAssetColumnDO();

                    assetColumn.setAssetId(daAssetDO.getId());
                    // 映射基本字段
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

                    // 设置其他可选字段（如果需要可以从元数据中获取或设置默认值）
                    assetColumn.setDataElemCodeFlag("0"); // 默认不是代码
                    assetColumn.setRelDataElmeFlag("0"); // 默认不关联数据元
                    assetColumn.setRelCleanFlag("0"); // 默认不关联清洗规则
                    assetColumn.setRelAuditFlag("0"); // 默认不关联稽查规则
                    return assetColumn;
                })
                .collect(Collectors.toList());
    }

    /**
     * 对比新旧字段列表，返回需要删除、修改和新增的字段
     *
     * @param newAssetColumns 新的字段列表
     * @param oldAssetColumns 旧的字段列表
     * @return Map包含三个列表：deleteList(需删除)、updateList(需修改)、addList(需新增)
     */
    public Map<String, List<DaAssetColumnDO>> compareAssetColumns(List<DaAssetColumnDO> newAssetColumns,
                                                                  List<DaAssetColumnDO> oldAssetColumns) {
        // 初始化结果Map
        Map<String, List<DaAssetColumnDO>> result = new HashMap<>();
        List<DaAssetColumnDO> deleteList = new ArrayList<>();
        List<DaAssetColumnDO> updateList = new ArrayList<>();
        List<DaAssetColumnDO> addList = new ArrayList<>();

        // 使用columnName作为唯一标识进行比较
        // 将旧字段列表转换为以columnName为key的Map，便于查找
        Map<String, DaAssetColumnDO> oldColumnMap = oldAssetColumns.stream()
                .collect(Collectors.toMap(DaAssetColumnDO::getColumnName, column -> column));

        // 将新字段列表转换为以columnName为key的Map，便于查找
        Map<String, DaAssetColumnDO> newColumnMap = newAssetColumns.stream()
                .collect(Collectors.toMap(DaAssetColumnDO::getColumnName, column -> column));

        // 1. 查找需要删除的字段（在old中存在但在new中不存在）
        for (DaAssetColumnDO oldColumn : oldAssetColumns) {
            if (!newColumnMap.containsKey(oldColumn.getColumnName())) {
                deleteList.add(oldColumn);
            }
        }

        // 2. 查找需要新增的字段（在new中存在但在old中不存在）
        for (DaAssetColumnDO newColumn : newAssetColumns) {
            if (!oldColumnMap.containsKey(newColumn.getColumnName())) {
                addList.add(newColumn);
            }
        }

        // 3. 查找需要修改的字段（在两边都存在但属性不同）
        for (DaAssetColumnDO newColumn : newAssetColumns) {
            DaAssetColumnDO oldColumn = oldColumnMap.get(newColumn.getColumnName());
            if (oldColumn != null) {
                // 比较字段的关键属性是否有变化
                if (isColumnChanged(newColumn, oldColumn)) {
                    // 如果需要知道具体哪些字段变了，可以将oldColumn也保存起来
                    // 这里我们添加新版本的字段到更新列表
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
     * 判断字段的属性是否发生变化
     *
     * @param newColumn 新字段
     * @param oldColumn 旧字段
     * @return 是否有变化
     */
    private boolean isColumnChanged(DaAssetColumnDO newColumn, DaAssetColumnDO oldColumn) {
        // 比较字段类型
        if (!Objects.equals(newColumn.getColumnType(), oldColumn.getColumnType())) {
            return true;
        }

        // 比较字段长度
        if (!Objects.equals(newColumn.getColumnLength(), oldColumn.getColumnLength())) {
            return true;
        }

        // 比较小数位数
        if (!Objects.equals(newColumn.getColumnScale(), oldColumn.getColumnScale())) {
            return true;
        }

        // 比较是否主键
        if (!Objects.equals(newColumn.getPkFlag(), oldColumn.getPkFlag())) {
            return true;
        }

        // 比较是否必填
        if (!Objects.equals(newColumn.getNullableFlag(), oldColumn.getNullableFlag())) {
            return true;
        }

        // 比较默认值
        if (!Objects.equals(newColumn.getDefaultValue(), oldColumn.getDefaultValue())) {
            return true;
        }

        // 比较字段注释
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
