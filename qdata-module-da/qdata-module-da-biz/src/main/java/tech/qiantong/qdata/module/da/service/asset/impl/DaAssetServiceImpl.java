package tech.qiantong.qdata.module.da.service.asset.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.constant.CacheConstants;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.core.redis.RedisCache;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.PageUtil;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.asset.DaAssetDO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.module.da.dal.dataobject.daAssetApply.DaAssetApplyDO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.module.da.dal.dataobject.sensitiveLevel.DaSensitiveLevelDO;
import tech.qiantong.qdata.module.da.dal.mapper.asset.DaAssetMapper;
import tech.qiantong.qdata.module.da.dal.mapper.assetColumn.DaAssetColumnMapper;
import tech.qiantong.qdata.module.da.dal.mapper.daAssetApply.DaAssetApplyMapper;
import tech.qiantong.qdata.module.da.dal.mapper.datasource.DaDatasourceMapper;
import tech.qiantong.qdata.module.da.dal.mapper.sensitiveLevel.DaSensitiveLevelMapper;
import tech.qiantong.qdata.module.da.service.asset.IDaAssetService;
import tech.qiantong.qdata.module.da.service.assetColumn.IDaAssetColumnService;
import tech.qiantong.qdata.module.da.service.assetchild.api.IDaAssetApiParamService;
import tech.qiantong.qdata.module.da.service.assetchild.api.IDaAssetApiService;
import tech.qiantong.qdata.module.da.service.assetchild.geo.IDaAssetGeoService;
import tech.qiantong.qdata.module.da.service.assetchild.gis.IDaAssetGisService;
import tech.qiantong.qdata.module.da.service.assetchild.projectRel.IDaAssetProjectRelService;
import tech.qiantong.qdata.module.da.service.assetchild.theme.IDaAssetThemeRelService;
import tech.qiantong.qdata.module.da.service.assetchild.video.IDaAssetVideoService;
import tech.qiantong.qdata.module.da.service.datasource.IDaDatasourceService;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelReqDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemAssetRelRespDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRespDTO;
import tech.qiantong.qdata.module.dp.api.dataElem.dto.DpDataElemRuleRelRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelColumnRespDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelRespDTO;
import tech.qiantong.qdata.module.dp.api.service.dataElem.IDataElemRuleRelService;
import tech.qiantong.qdata.module.dp.api.service.model.IDpModelApiService;
import tech.qiantong.qdata.module.dpp.api.service.etl.DppEtlTaskService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
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
    @Resource
    private DaAssetMapper daAssetMapper;

    @Resource
    private DaDatasourceMapper daDatasourceMapper;

    @Resource
    private IDpModelApiService iDpModelApiService;

    @Resource
    private IDataElemRuleRelService elemRuleRelService;

    @Resource
    private IDaAssetColumnService iDaAssetColumnService;

    @Resource
    private IDaDatasourceService iDaDatasourceService;

    @Resource
    private DaAssetColumnMapper daAssetColumnMapper;

    @Resource
    private DaSensitiveLevelMapper daSensitiveLevelMapper;

    @Autowired
    private DataSourceFactory dataSourceFactory;
    @Resource
    private RedisCache redisCache;

    @Resource
    private DaAssetApplyMapper daAssetApplyMapper;
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
            throw new ServiceException("模型不存在");
        }
        DaAssetDO daAssetDO = new DaAssetDO();
        daAssetDO.setName(dpModelByIdApi.getModelComment());
        daAssetDO.setCatCode(dpModelByIdApi.getCatCode());
        daAssetDO.setDatasourceId(daAssetReqDTO.getDatasourceId());
        daAssetDO.setSource(daAssetReqDTO.getSource());
        daAssetDO.setTableName(dpModelByIdApi.getModelName());
        daAssetDO.setTableComment(dpModelByIdApi.getModelComment());
        daAssetDO.setFieldCount(daAssetReqDTO.getFieldCount());//字段量

        //判断是否存在资产
        DaAssetPageReqVO daAssetPageReqVO = new DaAssetPageReqVO();
        daAssetPageReqVO.setTableName(dpModelByIdApi.getModelName());
        daAssetPageReqVO.setDatasourceId(String.valueOf(daAssetReqDTO.getDatasourceId()));
        DaAssetDO assetDO = this.getDaAssetByDaAssetPageReqVO(daAssetPageReqVO);
        if(assetDO != null){
            daAssetDO.setId(assetDO.getId());
            daAssetMapper.updateById(daAssetDO);//添加资产数据
        }else {
            daAssetMapper.insert(daAssetDO);//添加资产数据
        }

        //查询逻辑模型属性
        List<DpModelColumnRespDTO> dpModelColumnListByModelIdApi = iDpModelApiService.getDpModelColumnListByModelIdApi(daAssetReqDTO.getModelId());
        List<DaAssetColumnDO> daAssetColumnDOList = new ArrayList<>();

        List<DaAssetColumnDO> daAssetColumnList = new ArrayList<>();
        if(assetDO != null){
            DaAssetColumnPageReqVO daAssetColumnPageReqVO = new DaAssetColumnPageReqVO();
            daAssetColumnPageReqVO.setAssetId(String.valueOf(assetDO.getId()));
            List<DaAssetColumnDO> daAssetColumnList1 = iDaAssetColumnService.getDaAssetColumnList(daAssetColumnPageReqVO);
            daAssetColumnList = CollectionUtils.isEmpty(daAssetColumnList1) ? daAssetColumnList:daAssetColumnList1;
        }
        if (StringUtils.isNotEmpty(dpModelColumnListByModelIdApi)) {
            for (DpModelColumnRespDTO dpModelColumnRespDTO : dpModelColumnListByModelIdApi) {
                DaAssetColumnDO daAssetColumnDO = new DaAssetColumnDO();

                DaAssetColumnDO columnDO = matchColumn(daAssetColumnList, dpModelColumnRespDTO);
                if(columnDO != null){
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
            if(daAssetColumnDO.getId() == null){
                iDaAssetColumnService.save(daAssetColumnDO);
            }else {
                iDaAssetColumnService.updateById(daAssetColumnDO);
            }
        }
        Collection<Long> nonExistingIdList = this.findNonExistingIdList(daAssetColumnDOList, daAssetColumnList);
        if(CollectionUtils.isNotEmpty(nonExistingIdList)){
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
                        .filter(daAssetColumnDO -> daAssetColumnDO.getDataElemCodeId() != null &&
                                daAssetColumnDO.getDataElemCodeId().equals(dpDataElemRespDTO.getId()))
                        .findFirst();
                first.ifPresent(daAssetColumnDO -> dpDataElemAssetRelReqDTO.setColumnId(daAssetColumnDO.getId()));

                dpDataElemAssetRel.add(dpDataElemAssetRelReqDTO);
            });
            if (StringUtils.isNotEmpty(dpDataElemListByIdsApi)) {
                boolean b = iDpModelApiService.insertElementAssetRelation(dpDataElemAssetRel);
                if (!b) {
                    throw new ServiceException("数据元数据资产关联信息保存失败");
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
    public static Collection<Long> findNonExistingIdList(List<DaAssetColumnDO> daAssetColumnDOList,
                                                         List<DaAssetColumnDO> daAssetColumnList) {
        // 提取已存在列表中所有非空的 columnName 到一个 Set 中
        Set<String> existingNames = daAssetColumnDOList == null ?
                null : daAssetColumnDOList.stream()
                .filter(asset -> StringUtils.isNotBlank(asset.getColumnName()))
                .map(DaAssetColumnDO::getColumnName)
                .collect(Collectors.toSet());

        // 对待匹配列表进行过滤，保留 columnName 不在 existingNames 中的记录，并收集其 id
        return daAssetColumnList == null ?
                null : daAssetColumnList.stream()
                .filter(asset -> StringUtils.isNotBlank(asset.getColumnName()))
                .filter(asset -> existingNames == null ||
                        existingNames.stream().noneMatch(name -> StringUtils.equals(name, asset.getColumnName())))
                .map(DaAssetColumnDO::getId)
                .collect(Collectors.toList());
    }

    /**
     * 根据 dpModelColumnRespDTO 的 engName 匹配 daAssetColumnList 中对应的 DaAssetColumnDO 对象
     *
     * @param daAssetColumnList 数据资产字段列表
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
        return baseMapper.selectCount(Wrappers.lambdaQuery(DaAssetDO.class)
                .likeRight(DaAssetDO::getCatCode, catCode));
    }


    @Override
    public List<DaAssetDO> getTablesByDataSourceId(DaAssetPageReqVO pageReqVO) {
        if (StringUtils.isEmpty(pageReqVO.getDatasourceId())) {
            throw new ServiceException("数据源id不能为空");
        }
        return this.lambdaQuery()
                .eq(DaAssetDO::getDatasourceId, pageReqVO.getDatasourceId())
                .eq(DaAssetDO::getDelFlag, "0")
                .list();
    }

    @Override
    public DaAssetDO getDaAssetByDaAssetPageReqVO(DaAssetPageReqVO pageReqVO) {
        MPJLambdaWrapper<DaAssetDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.eq(StringUtils.isNotEmpty(pageReqVO.getName()),DaAssetDO::getName,pageReqVO.getName())
                .eq(pageReqVO.getId() != null,DaAssetDO::getId,pageReqVO.getId())
                .eq(StringUtils.isNotEmpty(pageReqVO.getTableName()),DaAssetDO::getTableName,pageReqVO.getTableName())
                .eq(StringUtils.isNotEmpty(pageReqVO.getDatasourceId()),DaAssetDO::getDatasourceId,pageReqVO.getDatasourceId())
                .eq(StringUtils.isNotEmpty(pageReqVO.getTableComment()),DaAssetDO::getTableComment,pageReqVO.getTableComment());
        return baseMapper.selectOne(lambdaWrapper);
    }
    public List<Long> extractDistinctAssetIds(List<DaAssetThemeRelRespVO> vos) {
        if(CollectionUtils.isEmpty(vos)){
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
    public PageResult<DaAssetDO> getDaAssetPage(DaAssetPageReqVO pageReqVO,String daAssetQueryType) {
        List<String> themeIdList = pageReqVO.getThemeIdList();
        if(CollectionUtils.isNotEmpty(themeIdList)){
            DaAssetThemeRelPageReqVO daAssetThemeRel= new DaAssetThemeRelPageReqVO();
            daAssetThemeRel.setThemeIdList(themeIdList);
            List<DaAssetThemeRelRespVO> daAssetThemeRelRespVOS = daAssetThemeRelService.getDaAssetThemeRelList(daAssetThemeRel);
            List<Long> strings = this.extractDistinctAssetIds(daAssetThemeRelRespVOS);
            if(CollectionUtils.isEmpty(strings)){
                PageResult<DaAssetDO> daAssetDOPageResult= new PageResult<>();
                return daAssetDOPageResult;
            }
            pageReqVO.setThemeAssetIdList(strings);
        }

        PageResult<DaAssetDO> daAssetDOPageResult;
        if(StringUtils.equals("1",daAssetQueryType)){
            daAssetDOPageResult = daAssetMapper.selectPage(pageReqVO);
        }else {
            daAssetDOPageResult = daAssetMapper.selectPageDpp(pageReqVO);
        }
        List<DaAssetDO> daAssetDOList = (List<DaAssetDO> )daAssetDOPageResult.getRows();
        for (DaAssetDO daAssetDO : daAssetDOList) {
            DaAssetThemeRelPageReqVO daAssetThemeRelPageReqVO= new DaAssetThemeRelPageReqVO();
            daAssetThemeRelPageReqVO.setAssetId(daAssetDO.getId());
            List<DaAssetThemeRelRespVO> daAssetThemeRelList = daAssetThemeRelService.getDaAssetThemeRelList(daAssetThemeRelPageReqVO);
            daAssetDO.setDaAssetThemeRelList(daAssetThemeRelList);
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
                .in(reqVO.getThemeAssetIdList() != null && !reqVO.getThemeAssetIdList().isEmpty(), DaAssetDO::getId, reqVO.getThemeAssetIdList())
                .orderByStr(StringUtils.isNotBlank(reqVO.getOrderByColumn()), StringUtils.equals("asc", reqVO.getIsAsc()), StringUtils.isNotBlank(reqVO.getOrderByColumn()) ? Arrays.asList(reqVO.getOrderByColumn().split(",")) : null);

        return daAssetMapper.selectJoinList(DaAssetDO.class, lambdaWrapper);
    }


    @Override
    public DaAssetRespVO getDaAssetById(Long id) {
        MPJLambdaWrapper<DaAssetDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DaAssetDO.class)
                .select("t2.NAME AS catName",  "t4.DATASOURCE_NAME as datasourceName", "t4.IP as datasourceIp", "t4.DATASOURCE_TYPE as datasourceType")
                .leftJoin("ATT_ASSET_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .leftJoin("DA_DATASOURCE t4 on t.DATASOURCE_ID = t4.ID")
                .eq(DaAssetDO::getId, id);
        DaAssetDO daAssetDO = daAssetMapper.selectJoinOne(DaAssetDO.class, lambdaWrapper);

        DaAssetThemeRelPageReqVO daAssetThemeRelPageReqVO= new DaAssetThemeRelPageReqVO();
        daAssetThemeRelPageReqVO.setAssetId(daAssetDO.getId());
        List<DaAssetThemeRelRespVO> daAssetThemeRelList = daAssetThemeRelService.getDaAssetThemeRelList(daAssetThemeRelPageReqVO);

        daAssetDO.setDaAssetThemeRelList(daAssetThemeRelList);

        DaAssetRespVO bean = BeanUtils.toBean(daAssetDO, DaAssetRespVO.class);
        queryDaAssetchild(bean);

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
        if(StringUtils.equals("1",type)){
            return;
        } else if (StringUtils.equals("2",type)){
            DaAssetApiRespVO daAssetApiByAssetId = iDaAssetApiService.getDaAssetApiByAssetId(assetId);
            daAsset.setDaAssetApi(daAssetApiByAssetId);
            List<DaAssetApiParamRespVO> daAssetApiParamList = iDaAssetApiParamService.getDaAssetApiParamList(daAssetApiByAssetId.getId());
            daAsset.setDaAssetApiParamList(daAssetApiParamList);
        } else if (StringUtils.equals("3",type)){
            DaAssetGisRespVO daAssetGisByAssetId = iDaAssetGisService.getDaAssetGisByAssetId(assetId);
            daAsset.setDaAssetGis(daAssetGisByAssetId);
        } else if (StringUtils.equals("4",type)){
            DaAssetGeoRespVO daAssetGeoByAssetId = iDaAssetGeoService.getDaAssetGeoByAssetId(assetId);
            daAsset.setDaAssetGeo(daAssetGeoByAssetId);
        } else if (StringUtils.equals("5",type)){
            DaAssetVideoRespVO daAssetVideoByAssetId = iDaAssetVideoService.getDaAssetVideoByAssetId(assetId);
            daAsset.setDaAssetVideo(daAssetVideoByAssetId);
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
        if (asset > 0){
            throw new ServiceException("删除失败,资产被项目引用!");
        }
        List<DaAssetDO> daAssetDOList = daAssetMapper.selectList("ID", idList);
        DaAssetDO daAssetDO = daAssetDOList != null ? daAssetDOList.get(0) : null;
        // 批量删除数据资产
        return daAssetMapper.deleteBatchIds(idList);
    }

    @Override
    public int removeDaAsset(Long id) {
        ArrayList<Long> assetIdList = new ArrayList<>();
        assetIdList.add(id);
        int asset = dppEtlTaskService.checkTaskIdInAsset(assetIdList);
        if (asset > 0){
            throw new ServiceException("删除失败,资产被项目引用!");
        }
        DaAssetDO daAssetDO = daAssetMapper.selectById(id);
        if(daAssetDO == null){
            return 1;
        }
        //删除项目
        iDaAssetProjectRelService.removeProjectRelByAssetId(id);
        //删除主题
        daAssetThemeRelService.removeThemeRelByAssetId(id);

        daAssetMapper.deleteAssetById(id);
        // 批量删除数据资产
        return 1;
    }


    @Override
    public List<DaAssetDO> getDaAssetList() {
        return daAssetMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetDO> getDaAssetMap() {
        List<DaAssetDO> daAssetList = daAssetMapper.selectList();
        return daAssetList.stream()
                .collect(Collectors.toMap(
                        DaAssetDO::getId,
                        daAssetDO -> daAssetDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
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
            throw new ServiceException("导入数据不能为空！");
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
                            successMessages.add("数据更新成功，ID为 " + daAssetId + " 的数据资产记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daAssetId + " 的数据资产记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaAssetDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetId);
                    DaAssetDO existingDaAsset = daAssetMapper.selectOne(queryWrapper);
                    if (existingDaAsset == null) {
                        daAssetMapper.insert(daAssetDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daAssetId + " 的数据资产记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daAssetId + " 的数据资产记录已存在。");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
            throw new ServiceException(resultMsg.toString());
        } else {
            resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
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
            throw new DataQueryException("请携带页码与每页条数！");
        }
        // 查询数据
        Integer pageNum = Integer.valueOf(jsonObject.getStr("pageNum"));
        Integer pageSize = Integer.valueOf(jsonObject.getStr("pageSize"));
        DaAssetRespVO daAssetDO = this.getDaAssetById(Long.valueOf(jsonObject.getStr("id")));
        tableName = daAssetDO.getTableName();
        dataSourceId = daAssetDO.getDatasourceId();
        // 获取数据源连接信息
        DaDatasourceDO daDatasourceDO = daDatasourceMapper.selectById(dataSourceId);
        if (daDatasourceDO == null) {
            return null;
        }
        DbQueryProperty dbQueryProperty = new DbQueryProperty(
                daDatasourceDO.getDatasourceType(),
                daDatasourceDO.getIp(),
                daDatasourceDO.getPort(),
                daDatasourceDO.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            dbQuery.close();
            throw new DataQueryException("数据库连接失败");
        }
        int existsSQL = dbQuery.generateCheckTableExistsSQL(dbQueryProperty, tableName);
        if (existsSQL == 0) {
            dbQuery.close();
            throw new DataQueryException("数据库中未获取到该表数据，请确认表是否存在!");
        }
        // 获取字段集合
        List<DbColumn> columns = redisCache.getCacheList(CacheConstants.ASSET_PREVIEW_KEY + daDatasourceDO.getId() + "_" + tableName);
        if (columns.isEmpty()) {
            columns = dbQuery.getTableColumns(dbQueryProperty, tableName);
            if (columns.size() == 0) {
                dbQuery.close();
                throw new DataQueryException("数据库连接失败");
            }
            redisCache.setCacheList(CacheConstants.ASSET_PREVIEW_KEY + daDatasourceDO.getId() + "_" + tableName, columns);
            redisCache.expire(CacheConstants.ASSET_PREVIEW_KEY + daDatasourceDO.getId() + "_" + tableName, 1, TimeUnit.DAYS);
        }
        // 拼接查询sql语句
        List<Map<String, Object>> columnTable = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            Map<String, Object> column = new HashMap<>();
            column.put("field", columns.get(i).getColName());
            column.put("en", columns.get(i).getColName());
            column.put("cn", columns.get(i).getColComment());
            column.put("columnNullable", columns.get(i).getNullable());
            column.put("columnKey", columns.get(i).getColKey());
            columnTable.add(column);
        }
        List<Map> orderByList = jsonObject.getBeanList("orderBy", Map.class);

        PageUtil pageUtil = new PageUtil(pageNum, pageSize);
        List<Map<String, Object>> queryList;

        queryList = dbQuery.queryDbColumnByList(columns, tableName, dbQueryProperty,jsonObject.getStr("filter"),orderByList, pageUtil.getOffset(), pageSize);
        int total = dbQuery.countNew(tableName, dbQueryProperty, jsonObject.getStr("filter"));

        Map<String, Object> data = new HashMap<>();
        data.put("columns", columnTable);
        data.put("tableData", queryList);
        data.put("total", total);
        dbQuery.close();
        return data;
    }

    /**
     * 对数据资产的数据进行脱敏
     *
     * @param assetId   数据资产id
     * @param data 数据资产的数据
     * @return
     */
    @Override
    public List<Map<String, Object>> dataMasking(Long assetId, List<Map<String, Object>> data) {
        // 1) 字段元数据（按字段名大写匹配）
        List<DaAssetColumnDO> cols = daAssetColumnMapper.findByAssetId(assetId);
        Map<String, DaAssetColumnDO> colMap = cols.stream()
                .collect(Collectors.toMap(c -> c.getColumnName().toUpperCase(), c -> c, (a,b)->a));

        // 2) 敏感等级（仅在线）
        Map<Long, DaSensitiveLevelDO> levelMap = daSensitiveLevelMapper
                .selectList(new QueryWrapper<DaSensitiveLevelDO>().eq("online_flag", 1))
                .stream().collect(Collectors.toMap(DaSensitiveLevelDO::getId, x -> x, (a,b)->a));

        List<Map<String, Object>> out = new ArrayList<>(data.size());

        for (Map<String, Object> row : data) {
            // 用 LinkedHashMap 保持字段顺序，且不修改原 map
            Map<String, Object> masked = new HashMap<>(row.size());

            for (Map.Entry<String, Object> e : row.entrySet()) {
                String key = e.getKey();
                Object val = e.getValue();

                // 保证 _id 始终是字符串
                if ("_id".equalsIgnoreCase(key) && val != null &&
                        "org.bson.types.ObjectId".equals(val.getClass().getName())) {
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
                int end   = lvl.getEndCharLoc()   == null ? len : lvl.getEndCharLoc().intValue();

                // 规范边界并保证 start<=end
                start = Math.max(1, start);
                end   = Math.min(len, end);
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

    /** 生成指定长度的掩码字符串（maskUnit 可为多字符） */
    private static String repeat(String maskUnit, int targetLen) {
        if (targetLen <= 0) return "";
        if (maskUnit == null || maskUnit.isEmpty()) maskUnit = "*";
        StringBuilder sb = new StringBuilder(targetLen);
        while (sb.length() + maskUnit.length() <= targetLen) sb.append(maskUnit);
        int remain = targetLen - sb.length();
        if (remain > 0) sb.append(maskUnit, 0, remain);
        return sb.toString();
    }

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
        if(CollectionUtils.isNotEmpty(themeIdList)){
            daAssetThemeRelService.createDaAssetThemeRelList(themeIdList,daAssetDO.getId());
        }

        List<DaAssetColumnDO> daAssetColumnList = new ArrayList<>();
        if(assetDO != null){
            DaAssetColumnPageReqVO daAssetColumnPageReqVO = new DaAssetColumnPageReqVO();
            daAssetColumnPageReqVO.setAssetId(String.valueOf(assetDO.getId()));
            List<DaAssetColumnDO> daAssetColumnList1 = iDaAssetColumnService.getDaAssetColumnList(daAssetColumnPageReqVO);
            daAssetColumnList = CollectionUtils.isEmpty(daAssetColumnList1) ? daAssetColumnList:daAssetColumnList1;
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
        if(CollectionUtils.isNotEmpty(nonExistingIdList)){
            iDaAssetColumnService.removeDaAssetColumn(nonExistingIdList);
        }
        Long daAssetDOId = daAssetDO.getId();
        for (DaAssetColumnSaveReqVO daAssetColumnSaveReqVO : columnSaveReqVOList) {
            daAssetColumnSaveReqVO.setAssetId(String.valueOf(daAssetDOId));
            if(daAssetColumnSaveReqVO.getId() == null){
                iDaAssetColumnService.createDaAssetColumn(daAssetColumnSaveReqVO);
            }else {
                iDaAssetColumnService.updateDaAssetColumn(daAssetColumnSaveReqVO);
            }
        }
    }

    public Collection<Long> findMissingColumnIds(List<DaAssetColumnDO> daAssetColumnList, List<DaAssetColumnSaveReqVO> columnSaveReqVOList) {
        if (daAssetColumnList == null) {
            return Collections.emptyList();
        }
        Set<String> existingColumnNames = columnSaveReqVOList == null ? Collections.emptySet() :
                columnSaveReqVOList.stream()
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
        String[] sourceTypeArr = daAsset.getParams().get("sourceType") == null ? null : daAsset.getParams().get("sourceType").toString() .split(",");
        queryWrapperX.eqIfPresent(DaAssetApplyDO::getStatus,3);
        queryWrapperX.eqIfPresent(DaAssetApplyDO::getProjectId,daAsset.getProjectId());
        queryWrapperX.eqIfPresent(DaAssetApplyDO::getProjectCode,daAsset.getProjectCode());
        queryWrapperX.inIfPresent(DaAssetApplyDO::getSourceType,sourceTypeArr);
        List<DaAssetApplyDO> applyDOList = daAssetApplyMapper.selectList(queryWrapperX);
        List<Long> assetIdList;
        Map<Long, DaAssetApplyDO> daAssetApplyDOMap;
        if (applyDOList.isEmpty()) {
            assetIdList =  new ArrayList<>();
            daAssetApplyDOMap = new HashMap<>();
        }else {
            daAssetApplyDOMap = applyDOList.stream().collect(Collectors.toMap(DaAssetApplyDO::getAssetId, daAssetApplyDO -> daAssetApplyDO));
            assetIdList = daAssetApplyDOMap.keySet().stream().collect(Collectors.toList());
        }
        daAsset.setAssetIdList(assetIdList);
        PageResult<DaAssetDO> daAssetPage = this.getDaAssetPage(daAsset,"2");
        if(CollectionUtils.isEmpty(daAssetPage.getRows())){
            return daAssetPage;
        }
        for (Object assetPageRow : daAssetPage.getRows()) {
            DaAssetDO daAssetDO = (DaAssetDO) assetPageRow;
            DaAssetApplyDO daAssetApplyDO = daAssetApplyDOMap.get(daAssetDO.getId()) == null ? new DaAssetApplyDO() : daAssetApplyDOMap.get(daAssetDO.getId());
            if (assetIdList.contains(daAssetDO.getId())) {
                daAssetDO.setSourceType(daAssetApplyDO.getSourceType());
            }else {
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
        queryWrapperX.eqIfPresent(DaAssetApplyDO::getStatus,3);
        queryWrapperX.eqIfPresent(DaAssetApplyDO::getProjectId,daAsset.getProjectId());
        queryWrapperX.eqIfPresent(DaAssetApplyDO::getProjectCode,daAsset.getProjectCode());
        List<DaAssetApplyDO> applyDOList = daAssetApplyMapper.selectList(queryWrapperX);
        if (applyDOList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> assetIdList = applyDOList.stream().collect(Collectors.toMap(DaAssetApplyDO::getAssetId, daAssetApplyDO -> daAssetApplyDO)).keySet().stream().collect(Collectors.toList());
        LambdaQueryWrapperX<DaAssetDO> daAssetQueryWrapper = new LambdaQueryWrapperX<>();
        daAssetQueryWrapper.inIfPresent(DaAssetDO::getId,assetIdList);
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
        } else {
            throw new ServiceException("类型暂不支持！");
        }

        createDaAssetProjectRel(daAsset);
        createDaAssetThemeIdList(daAsset);

        return daAsset.getId();
    }


    private void createDaAssetVideoNew(DaAssetSaveReqVO daAsset) {
        Long assetId = createDaAsset(daAsset);
        daAsset.setId(assetId);

        DaAssetVideoSaveReqVO daAssetVideo = daAsset.getDaAssetVideo();
        daAssetVideo.setAssetId(assetId);
        iDaAssetVideoService.createDaAssetVideo(daAssetVideo);
    }

    private void createDaAssetGisNew(DaAssetSaveReqVO daAsset) {
        Long assetId = createDaAsset(daAsset);
        daAsset.setId(assetId);

        DaAssetGisSaveReqVO daAssetGis = daAsset.getDaAssetGis();
        daAssetGis.setAssetId(assetId);
        iDaAssetGisService.createDaAssetGis(daAssetGis);
    }

    private void setDaAssetDefaultValues(DaAssetSaveReqVO daAsset) {
        daAsset.setDatasourceId("-1");
        daAsset.setTableName("-1");
        daAsset.setDataCount(0L);
        daAsset.setFieldCount(0L);
    }

    private void createDaAssetProjectRel(DaAssetSaveReqVO daAsset) {
        if(daAsset.getProjectId() == null){
            return;
        }
        DaAssetProjectRelSaveReqVO daAssetProjectRelSaveReqVO = new DaAssetProjectRelSaveReqVO();
        daAssetProjectRelSaveReqVO.setProjectCode(daAsset.getProjectCode());
        daAssetProjectRelSaveReqVO.setProjectId(daAsset.getProjectId());
        daAssetProjectRelSaveReqVO.setAssetId(daAsset.getId());
        iDaAssetProjectRelService.createDaAssetProjectRel(daAssetProjectRelSaveReqVO);
    }

    /**
     *
     * @param daAsset
     */
    private void createDaAssetGeoNew(DaAssetSaveReqVO daAsset) {
        Long assetId = createDaAsset(daAsset);
        daAsset.setId(assetId);

        DaAssetGeoSaveReqVO daAssetGeo = daAsset.getDaAssetGeo();
        daAssetGeo.setAssetId(assetId);
        iDaAssetGeoService.createDaAssetGeo(daAssetGeo);
    }

    private void createDaAssetApiNew(DaAssetSaveReqVO daAsset) {
        Long assetId = createDaAsset(daAsset);
        daAsset.setId(assetId);

        DaAssetApiSaveReqVO daAssetApi = daAsset.getDaAssetApi();
        daAssetApi.setAssetId(assetId);
        Long daAssetApiId = iDaAssetApiService.createDaAssetApi(daAssetApi);

        List<DaAssetApiParamSaveReqVO> daAssetApiParamList = daAsset.getDaAssetApiParamList();
        iDaAssetApiParamService.createDaAssetApiParamDeep(daAssetApiParamList,daAssetApiId);
    }

    /**
     * 主题
     * @param daAsset
     */
    private void createDaAssetThemeIdList(DaAssetSaveReqVO daAsset) {
        List<String> themeIdList = daAsset.getThemeIdList();
        if(CollectionUtils.isEmpty(themeIdList)){
            return;
        }
        daAssetThemeRelService.createDaAssetThemeRelList(themeIdList,daAsset.getId());
    }

    /**
     * 字段
     * @param daAsset
     */
    private void createDaAssetColumnNew(DaAssetSaveReqVO daAsset) {
        JSONObject entries = new JSONObject();
        entries.put("id",daAsset.getDatasourceId());
        entries.put("tableName",daAsset.getTableName());
        List<DaAssetColumnDO> daAssetColumnDOS = iDaDatasourceService.columnsAsAssetColumnList(entries);

        daAsset.setFieldCount(Long.valueOf(daAssetColumnDOS.size()));

        Long assetId = daAsset.getId();
        if ( assetId == null) {
             assetId = createDaAsset(daAsset);
            daAsset.setId(assetId);
        }
        List<DaAssetColumnSaveReqVO> daAssetColumnSaveReqVOList = BeanUtils.toBean(daAssetColumnDOS, DaAssetColumnSaveReqVO.class);
        for (DaAssetColumnSaveReqVO daAssetColumnSaveReqVO : daAssetColumnSaveReqVOList) {
            daAssetColumnSaveReqVO.setAssetId(String.valueOf(assetId));
            iDaAssetColumnService.createDaAssetColumn(daAssetColumnSaveReqVO);
        }

    }

    @Override
    public int updateDaAssetNew(DaAssetSaveReqVO daAsset) {
        //1:数据库表  2:外部API 3: 地理空间服务 4:矢量数据 5:视频数据
        String type = daAsset.getType();
        if(StringUtils.equals("1",type)){
            DaAssetRespVO daAssetById = getDaAssetById(daAsset.getId());
            if(StringUtils.equals("1",daAssetById.getCreateType()) && StringUtils.equals("2",daAsset.getCreateType())){
                createDaAssetColumnNew(daAsset);
            }
        } else if (StringUtils.equals("2",type)){
            setDaAssetDefaultValues(daAsset);
            updateDaAssetApiNew(daAsset);
        } else if (StringUtils.equals("3",type)){
            setDaAssetDefaultValues(daAsset);
            updateDaAssetGisNew(daAsset);
        } else if (StringUtils.equals("4",type)){
            setDaAssetDefaultValues(daAsset);
            updateDaAssetGeoNew(daAsset);
        } else if (StringUtils.equals("5",type)){
            setDaAssetDefaultValues(daAsset);
            updateDaAssetVideoNew(daAsset);
        }


//        createDaAssetProjectRel(daAsset);
        createDaAssetThemeIdList(daAsset);
        updateDaAsset(daAsset);
        return 1;
    }

    private void updateDaAssetVideoNew(DaAssetSaveReqVO daAsset) {
        DaAssetVideoSaveReqVO daAssetVideo = daAsset.getDaAssetVideo();
        if(daAssetVideo == null){
            return;
        }
        daAssetVideo.setAssetId(daAsset.getId());
        iDaAssetVideoService.updateDaAssetVideo(daAssetVideo);
    }

    private void updateDaAssetGisNew(DaAssetSaveReqVO daAsset) {
        DaAssetGisSaveReqVO daAssetGis = daAsset.getDaAssetGis();
        if(daAssetGis == null){
            return;
        }
        daAssetGis.setAssetId(daAsset.getId());
        iDaAssetGisService.updateDaAssetGis(daAssetGis);
    }

    private void updateDaAssetGeoNew(DaAssetSaveReqVO daAsset) {
        DaAssetGeoSaveReqVO daAssetGeo = daAsset.getDaAssetGeo();
        if(daAssetGeo == null){
            return;
        }
        daAssetGeo.setAssetId(daAsset.getId());
        iDaAssetGeoService.updateDaAssetGeo(daAssetGeo);
    }

    private void updateDaAssetApiNew(DaAssetSaveReqVO daAsset) {
        DaAssetApiSaveReqVO daAssetApi = daAsset.getDaAssetApi();
        if(daAssetApi == null){
            return;
        }
        daAssetApi.setAssetId(daAsset.getId());
        iDaAssetApiService.updateDaAssetApi(daAssetApi);

        List<DaAssetApiParamSaveReqVO> daAssetApiParamList = daAsset.getDaAssetApiParamList();
        iDaAssetApiParamService.createDaAssetApiParamDeep(daAssetApiParamList,daAssetApi.getId());
    }

    private void updateDaAssetColumnNew(DaAssetSaveReqVO daAsset) {
        return;
    }



    @Override
    public AjaxResult startDaAssetDatasourceTask(Long id) {
        if (id != null) {
            DaAssetRespVO daAssetById = this.getDaAssetById(id);
            if (StringUtils.equals("1", daAssetById.getType())) {
                // 如需特殊处理，填写逻辑
            }

            DaDatasourceDO daDatasourceById = iDaDatasourceService.getDaDatasourceById(daAssetById.getDatasourceId());
            DbQueryProperty dbQueryProperty = new DbQueryProperty(
                    daDatasourceById.getDatasourceType(),
                    daDatasourceById.getIp(),
                    daDatasourceById.getPort(),
                    daDatasourceById.getDatasourceConfig()
            );
            if (!isCountSupported(dbQueryProperty.getDbType())) {
                throw new DataQueryException("暂不支持此类型数据源，请联系管理员！");
            }

            DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
            if (!dbQuery.valid()) {
                throw new DataQueryException("数据库连接失败");
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
                DbQueryProperty dbQueryProperty = new DbQueryProperty(
                        datasource.getDatasourceType(),
                        datasource.getIp(),
                        datasource.getPort(),
                        datasource.getDatasourceConfig()
                );
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
                    updateAssetFieldAndDataCount(dbQuery, dbQueryProperty, asset);
                }

                dbQuery.close();
            }
        }

        return AjaxResult.success("任务完成");
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
        return StringUtils.isNotBlank(datasourceType)
                && COUNT_SUPPORTED_TYPES.contains(datasourceType);
    }

    private static final Set<String> COUNT_SUPPORTED_TYPES = new HashSet<>(Arrays.asList(
            DbType.MYSQL.getDb()
            , DbType.ORACLE.getDb()
            , DbType.ORACLE_12C.getDb()
            , DbType.SQL_SERVER.getDb()
            , DbType.DM8.getDb()
            , DbType.KINGBASE8.getDb()
    ));



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
        Set<Long> dataElemIds = assetRelRespDTOS.stream().map(DpDataElemAssetRelRespDTO::getDataElemId)
                .map(Long::valueOf)
                .collect(Collectors.toSet());
        List<DpDataElemRuleRelRespDTO> ruleRelRespDTOS = elemRuleRelService.listByDataElemIdList(dataElemIds, type);
        if (ruleRelRespDTOS.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, List<Long>> map = assetRelRespDTOS.stream()
                .filter(i -> StringUtils.isNotEmpty(i.getColumnId()))
                .collect(Collectors.groupingBy(i -> Long.valueOf(i.getColumnId()),
                        Collectors.mapping(i -> Long.valueOf(i.getDataElemId()), Collectors.toList())));
        return assetColumns.stream()
                .filter(assetColumn -> CollectionUtils.isNotEmpty(map.get(assetColumn.getId())))
                .map(assetColumn -> {
                    List<Long> temp = map.get(assetColumn.getId());
                    return ruleRelRespDTOS.stream().filter(i -> temp.contains(i.getDataElemId()))
                            .map(i -> new DaAssetColumnRelRuleVO(assetColumn, i))
                            .collect(Collectors.toList());
                }).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public List<DaAssetColumnRelRuleVO> listRelRule(Long datasourceId, String tableName, String type) {
        List<DaAssetDO> daAssetDOS = daAssetMapper.findByDatasourceIdAndTableName(datasourceId, tableName);
        if(daAssetDOS.isEmpty()){
            return Collections.emptyList();
        }
        return daAssetDOS.stream().map(i->listRelRule(i.getId(), type)).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
