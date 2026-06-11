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
 */

package tech.qiantong.qdata.module.da.service.datasource.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.constants.DbType;
import tech.qiantong.qdata.common.database.core.DbColumn;
import tech.qiantong.qdata.common.database.core.DbName;
import tech.qiantong.qdata.common.database.core.DbTable;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.enums.KingbaseColumnTypeEnum;
import tech.qiantong.qdata.common.enums.MySqlColumnTypeEnum;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.AesEncryptUtil;
import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.project.IAttProjectApi;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectReqDTO;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectRespDTO;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.module.da.api.datasource.dto.DatasourceCreaTeTableListReqDTO;
import tech.qiantong.qdata.module.da.api.datasource.dto.DatasourceCreaTeTableReqDTO;
import tech.qiantong.qdata.module.da.api.service.asset.IDaDatasourceApiService;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourcePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceRespVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTablePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskLogSaveReqVO;
import tech.qiantong.qdata.module.da.controller.admin.discovery.vo.DaDiscoveryTaskRespVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceDO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceProjectRelDO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryColumnDO;
import tech.qiantong.qdata.module.da.dal.dataobject.discovery.DaDiscoveryTableDO;
import tech.qiantong.qdata.module.da.dal.mapper.datasource.DaDatasourceMapper;
import tech.qiantong.qdata.module.da.service.datasource.IDaDatasourceProjectRelService;
import tech.qiantong.qdata.module.da.service.datasource.IDaDatasourceService;
import tech.qiantong.qdata.module.da.service.discovery.*;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelColumnReqDTO;
import tech.qiantong.qdata.module.dp.api.model.dto.DpModelColumnRespDTO;
import tech.qiantong.qdata.module.dp.api.service.model.IDpModelApiService;
import tech.qiantong.qdata.module.dpp.api.service.etl.DppEtlTaskService;
import tech.qiantong.qdata.module.system.service.ISysMessageService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;
import tech.qiantong.qdata.redis.service.IRedisService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 数据源Service业务层处理
 *
 * @author lhs
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaDatasourceServiceImpl extends ServiceImpl<DaDatasourceMapper, DaDatasourceDO> implements IDaDatasourceService, IDaDatasourceApiService {
    @Resource
    private DaDatasourceMapper daDatasourceMapper;

    @Autowired
    private DataSourceFactory dataSourceFactory;

    @Resource
    private IDpModelApiService dpModelApiService;

    @Resource
    private IDaDatasourceProjectRelService daDatasourceProjectRelService;

    @Resource
    private IAttProjectApi attProjectApi;

    @Resource
    private DppEtlTaskService dppEtlTaskService;

    @Autowired
    private IRedisService redisService;

    @Autowired
    private ISysMessageService iSysMessageService;

    @Autowired
    @Lazy
    private IDaDiscoveryTaskService iDaDiscoveryTaskService;

    @Autowired
    @Lazy
    private IDaDiscoveryColumnService iDaDiscoveryColumnService;

    @Autowired
    @Lazy
    private IDaDiscoveryTableService iDaDiscoveryTableService;

    @Autowired
    @Lazy
    private IDaDiscoveryTaskLogService iDaDiscoveryTaskLogService;

    @Resource
    private IDaDiscoveryLogBodyService iDaDiscoveryLogBodyService;


    /**
     * 项目启动后初始化 Redis 中的数据源缓存。
     *
     * 功能：
     * 1. 从数据库中加载所有数据源记录；
     * 2. 使用 DaDatasourceDO.simplify() 方法提取关键字段；
     * 3. 将结果写入 Redis Hash：
     *
     * 场景：
     * - 项目启动后预热数据源缓存，任务调度和 Worker 节点可直接从 Redis 获取配置信息；
     * - 避免运行时对数据库或中台服务的强依赖。
     *
     * 注意：
     * - 可选：初始化前可清空 Redis 中已有的 "datasource" 缓存。
     */
    @PostConstruct
    public void initDatasourceCache() {
        try {
            List<DaDatasourceDO> list = daDatasourceMapper.selectList();
            if (list == null || list.isEmpty()) {
                log.info("【数据源缓存初始化】无数据源记录，跳过初始化。");
                return;
            }

            // 可选：初始化前清空缓存
            // redisService.del("datasource");

            for (DaDatasourceDO ds : list) {
                if (ds == null || ds.getId() == null) {
                    continue;
                }

                try {
                    // 最新数据源连接信息
                    String field = String.valueOf(ds.getId());
                    String value = com.alibaba.fastjson2.JSONObject.toJSONString(ds.simplify());
                    redisService.hashPut("datasource", field, value);

                    // todo 历史数据源连接信息 （临时处理，后续可以移除）
                    DbQueryProperty property = new DbQueryProperty(
                            ds.getDatasourceType(),
                            ds.getIp(),
                            ds.getPort(),
                            ds.getDatasourceConfig());
                    String key = property.trainToJdbcUrl();

                    // 判断是否已存在
                    Boolean exists = redisService.hashHasKey("datasource-old", key);
                    if (Boolean.FALSE.equals(exists)) {
                        redisService.hashPut("datasource-old", key, ds.getId().toString());
                        log.info("存入新历史数据源: key={}, value={}", key, ds.getId().toString());
                    } else {
                        log.info("已存在历史数据源: key={}，跳过存入", key);
                    }
                } catch (Exception e) {
                    log.warn("不支持转化的数据源");
                }
            }

            log.info("【数据源缓存初始化】成功加载 {} 条数据源到 Redis。", list.size());
        } catch (Exception e) {
            log.error("【数据源缓存初始化】加载 Redis 缓存失败：", e);
        }
    }

    /**
     * 查询数据资产的数据源连接信息
     *
     * @param daAsset
     * @return
     */
    @Override
    public List<DaDatasourceDO> getDataSourceByAsset(DaDatasourceRespVO daAsset) {
        return daDatasourceMapper.selectList();
    }

    @Override
    public PageResult<DaDatasourceDO> getDaDatasourcePage(DaDatasourcePageReqVO pageReqVO) {
        return daDatasourceMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<DaDatasourceDO> getDaDatasourceDppPage(DaDatasourcePageReqVO pageReqVO) {
        if (StringUtils.isEmpty(pageReqVO.getProjectCode())) {
            return new PageResult<DaDatasourceDO>();
        }
        DaDatasourceProjectRelDO daDatasourceProjectRelDO = new DaDatasourceProjectRelDO();
        daDatasourceProjectRelDO.setProjectCode(pageReqVO.getProjectCode());
        List<DaDatasourceProjectRelDO> daDatasourceProjectRelList = daDatasourceProjectRelService.getJoinProjectAndDatasource(daDatasourceProjectRelDO);
        if (daDatasourceProjectRelList.isEmpty()) {
            return new PageResult<DaDatasourceDO>();
        }
        Map<Long, DaDatasourceProjectRelDO> datasourceProjectRelDOMap = daDatasourceProjectRelList.stream().collect(Collectors.toMap(DaDatasourceProjectRelDO::getDatasourceId, daDatasourceProjectRelDO1 -> daDatasourceProjectRelDO1));
        List<Long> idList = datasourceProjectRelDOMap.keySet().stream().collect(Collectors.toList());
        pageReqVO.setIdList(idList);
        PageResult<DaDatasourceDO> daDatasourceDOPageResult = daDatasourceMapper.selectPage(pageReqVO);
        for (Object row : daDatasourceDOPageResult.getRows()) {
            DaDatasourceDO daDatasourceDO = (DaDatasourceDO) row;
            DaDatasourceProjectRelDO datasourceProjectRelDO = datasourceProjectRelDOMap.get(daDatasourceDO.getId()) == null ? new DaDatasourceProjectRelDO() : datasourceProjectRelDOMap.get(daDatasourceDO.getId());
            if (idList.contains(daDatasourceDO.getId()) && !datasourceProjectRelDO.getDppAssigned()) {
                daDatasourceDO.setIsAdminAddTo(false);
                daDatasourceDO.setProjectName(datasourceProjectRelDO.getProjectName());
            }
        }
        return daDatasourceDOPageResult;
    }

    @Override
    public List<DaDatasourceDO> getDaDatasourceList(DaDatasourcePageReqVO reqVO) {
        LambdaQueryWrapperX<DaDatasourceDO> daDatasourceDOLambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        daDatasourceDOLambdaQueryWrapperX.likeIfPresent(DaDatasourceDO::getDatasourceName, reqVO.getDatasourceName())
                .like(StringUtils.isNotEmpty(reqVO.getDatasourceType()), DaDatasourceDO::getDatasourceType, reqVO.getDatasourceType())
                .eq(StringUtils.isNotEmpty(reqVO.getDatasourceConfig()), DaDatasourceDO::getDatasourceConfig, reqVO.getDatasourceConfig())
                .eq(StringUtils.isNotEmpty(reqVO.getIp()), DaDatasourceDO::getIp, reqVO.getIp());

        return daDatasourceMapper.selectList(daDatasourceDOLambdaQueryWrapperX);
    }

    @Override
    public Long createDaDatasource(DaDatasourceSaveReqVO createReqVO) {
        DaDatasourceDO dictType = BeanUtils.toBean(createReqVO, DaDatasourceDO.class);
        daDatasourceMapper.insert(dictType);
        delAndSaveDaDataSourceProject(dictType);

        redisService.hashPut("datasource", dictType.getId().toString(), com.alibaba.fastjson2.JSONObject.toJSONString(this.getDaDatasourceById(dictType.getId()).simplify()));

        return dictType.getId();
    }

    @Override
    public int updateDaDatasource(DaDatasourceSaveReqVO updateReqVO) {
        Long datasourceId = updateReqVO.getId();

        // 更新数据源
        DaDatasourceDO updateObj = BeanUtils.toBean(updateReqVO, DaDatasourceDO.class);
        delAndSaveDaDataSourceProject(updateObj);

        int i = daDatasourceMapper.updateById(updateObj);
        redisService.hashPut("datasource", datasourceId.toString(), com.alibaba.fastjson2.JSONObject.toJSONString(this.getDaDatasourceById(datasourceId).simplify()));
        return i;
    }

    private void delAndSaveDaDataSourceProject(DaDatasourceDO daDatasourceDO) {
        QueryWrapper<DaDatasourceProjectRelDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DATASOURCE_ID", daDatasourceDO.getId());
        daDatasourceProjectRelService.remove(queryWrapper);
        if (!daDatasourceDO.getProjectList().isEmpty()) {
            for (DaDatasourceProjectRelDO daDatasourceProjectRelDO : daDatasourceDO.getProjectList()) {
                daDatasourceProjectRelDO.setDatasourceId(daDatasourceDO.getId());
                daDatasourceProjectRelDO.setId(null);
            }
            daDatasourceProjectRelService.saveBatch(daDatasourceDO.getProjectList());
        }
    }

    @Override
    public int removeDaDatasource(Collection<Long> idList) {
        // 批量删除数据源
        return daDatasourceMapper.deleteBatchIds(idList);
    }

    @Override
    public int removeDaDatasourceDppOrDa(List<Long> idList, Long type) {
        int datasource = dppEtlTaskService.checkTaskIdInDatasource(idList, null);
        if (datasource > 0) {
            throw new ServiceException("删除失败,数据源被项目引用!");
        }
        if (!idList.isEmpty()) {
            QueryWrapper<DaDatasourceProjectRelDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("DATASOURCE_ID", idList);
            daDatasourceProjectRelService.remove(queryWrapper);
        }
        // 批量删除数据源
        return daDatasourceMapper.deleteBatchIds(idList);
    }


    @Override
    public DaDatasourceRespDTO getDatasourceById(Long id) {
        DaDatasourceRespDTO dto = new DaDatasourceRespDTO();
        DaDatasourceDO daDatasourceDO = daDatasourceMapper.selectById(id);
        org.springframework.beans.BeanUtils.copyProperties(daDatasourceDO, dto);
        return dto;
    }

    @Override
    public DaDatasourceDO getDaDatasourceById(Long id) {
        DaDatasourceDO daDatasourceDO = daDatasourceMapper.selectById(id);
        if (daDatasourceDO == null) {
            return null;
        }
        DaDatasourceProjectRelDO daDatasourceProjectRelDO = new DaDatasourceProjectRelDO();
        daDatasourceProjectRelDO.setDatasourceId(daDatasourceDO.getId());
        List<DaDatasourceProjectRelDO> daDatasourceProjectRelList = daDatasourceProjectRelService.getJoinProjectAndDatasource(daDatasourceProjectRelDO);
        daDatasourceDO.setProjectList(daDatasourceProjectRelList);
        return daDatasourceDO;
    }

    @Override
    public DaDatasourceRespVO getDaDatasourceByIdSimple(Long id) {
        return BeanUtils.toBean(daDatasourceMapper.selectById(id), DaDatasourceRespVO.class);
    }

    @Override
    public List<DaDatasourceDO> getDaDatasourceList() {
        return daDatasourceMapper.selectList();
    }

    @Override
    public Map<Long, DaDatasourceDO> getDaDatasourceMap() {
        List<DaDatasourceDO> daDatasourceList = daDatasourceMapper.selectList();
        return daDatasourceList.stream()
                .collect(Collectors.toMap(
                        DaDatasourceDO::getId,
                        daDatasourceDO -> daDatasourceDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据源数据
     *
     * @param importExcelList 数据源数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDaDatasource(List<DaDatasourceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaDatasourceRespVO respVO : importExcelList) {
            try {
                DaDatasourceDO daDatasourceDO = BeanUtils.toBean(respVO, DaDatasourceDO.class);
                Long daDatasourceId = respVO.getId();
                if (isUpdateSupport) {
                    if (daDatasourceId != null) {
                        DaDatasourceDO existingDaDatasource = daDatasourceMapper.selectById(daDatasourceId);
                        if (existingDaDatasource != null) {
                            daDatasourceMapper.updateById(daDatasourceDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + daDatasourceId + " 的数据源记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daDatasourceId + " 的数据源记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaDatasourceDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daDatasourceId);
                    DaDatasourceDO existingDaDatasource = daDatasourceMapper.selectOne(queryWrapper);
                    if (existingDaDatasource == null) {
                        daDatasourceMapper.insert(daDatasourceDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daDatasourceId + " 的数据源记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daDatasourceId + " 的数据源记录已存在。");
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


    @Override
    public AjaxResult clientsTest(Long id) {
        DbQuery dbQuery = this.buildDbQuery(id);
        if (dbQuery.valid()) {
            dbQuery.close();
            return AjaxResult.success("数据库连接成功");
        }
        dbQuery.close();
        return AjaxResult.error("数据库连接失败");

    }

    public DbQuery buildDbQuery(Long id) {
        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
        if (daDatasourceBy == null) {
            throw new DataQueryException("数据源详情信息查询失败");
        }
        DbQueryProperty dbQueryProperty = new DbQueryProperty(
                daDatasourceBy.getDatasourceType(),
                daDatasourceBy.getIp(),
                daDatasourceBy.getPort(),
                daDatasourceBy.getDatasourceConfig()
        );
        return dataSourceFactory.createDbQuery(dbQueryProperty);
    }

    /**
     * @param id 数据源id
     * @return
     */
    @Override
    public List<DbTable> getDbTables(Long id) {
        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
        if (daDatasourceBy == null) {
            throw new DataQueryException("数据源详情信息查询失败");
        }

        DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceBy.getDatasourceType()
                , daDatasourceBy.getIp(), daDatasourceBy.getPort(), daDatasourceBy.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            throw new DataQueryException("数据库连接失败");
        }
        List<DbTable> tables = dbQuery.getTables(dbQueryProperty);
        dbQuery.close();
        return tables;
    }

    /**
     * 获取数据表里面的数据字段
     *
     * @param jsonObject 数据源id和数据表
     * @return
     */
    @Override
    public List<DpModelColumnReqDTO> getColumnsList(JSONObject jsonObject) {
        List<DpModelColumnRespDTO> modelIdColumnList = new ArrayList<>();
        Boolean isOld = jsonObject.getStr("isOld") == null ? null : Boolean.valueOf(jsonObject.getStr("isOld"));
        if (isOld != null && !isOld && jsonObject.getStr("modelId") != null) {
            modelIdColumnList = dpModelApiService.getModelIdColumnList(Long.valueOf(jsonObject.getStr("modelId")));
        }
        if (modelIdColumnList.size() > 0) {
            List<DpModelColumnReqDTO> columnReqDTOList = BeanUtils.toBean(modelIdColumnList, DpModelColumnReqDTO.class);
            return columnReqDTOList;
        }
        // 获取数据库类型
        DbType dbTypeEnum = DbType.getDbType(jsonObject.getStr("type"));
        List<DbColumn> columnList = this.getDbTableColumns(Long.valueOf(jsonObject.getStr("id")), jsonObject.getStr("tableName"));
        List<DpModelColumnReqDTO> columnReqDTOList = new ArrayList<>();
        for (DbColumn column : columnList) {
            String dataType = column.getDataType();
            switch (dbTypeEnum) {
                case DM8:
                case ORACLE:
                    break;
                case MYSQL:
                    column.setDataType(MySqlColumnTypeEnum.convertToDmType(dataType));
                    break;
                case KINGBASE8:
                    column.setDataType(KingbaseColumnTypeEnum.convertToDmType(dataType));
                    break;
            }
            DpModelColumnReqDTO dpModelColumnReqDTO = new DpModelColumnReqDTO(column);
            columnReqDTOList.add(dpModelColumnReqDTO);
        }
        return columnReqDTOList;
    }

    @Override
    public List<DaAssetColumnDO> columnsAsAssetColumnList(JSONObject jsonObject) {
        List<DbColumn> columnsList = this.getDbTableColumns(Long.valueOf(jsonObject.getStr("id")), jsonObject.getStr("tableName"));

        return this.convertDbColumns(columnsList);
    }

    @Override
    public List<DaAssetColumnDO> columnsAsAssetColumnList(Long id, String tableName) {
        List<DbColumn> columnsList = this.getDbTableColumns(id, tableName);
        return convertDbColumns(columnsList);
    }


    /**
     * 将 List<DpModelColumnReqDTO> 转换为 List<DaAssetColumnDO>
     *
     * @param columnsList DpModelColumnReqDTO 列表
     * @return 转换后的 DaAssetColumnDO 列表；若输入为空或 null，则返回一个空的 ArrayList
     */
    public static List<DaAssetColumnDO> convertDbColumns(List<DbColumn> columnsList) {
        if (columnsList == null || columnsList.isEmpty()) {
            return new ArrayList<>();
        }
        List<DaAssetColumnDO> assetColumns = new ArrayList<>(columnsList.size());
        for (DbColumn dbColumn : columnsList) {
            // 利用 DpModelColumnReqDTO 的构造方法封装 DbColumn 到 DTO 对象
            DpModelColumnReqDTO dto = new DpModelColumnReqDTO(dbColumn);
            // 利用 DTO 数据映射生成 DaAssetColumnDO 对象
            DaAssetColumnDO assetColumn = DaAssetColumnDO.builder()
                    // engName 映射为字段名称
                    .columnName(dto.getEngName())
                    // cnName 映射为字段注释
                    .columnComment(dto.getCnName())
                    .columnType(dto.getColumnType())
                    .columnLength(dto.getColumnLength())
                    .columnScale(dto.getColumnScale())
                    .nullableFlag(dto.getNullableFlag())
                    .pkFlag(dto.getPkFlag())
                    .defaultValue(dto.getDefaultValue())
                    .build();
            assetColumns.add(assetColumn);
        }
        return assetColumns;
    }


    @Override
    public boolean creaDatasourceTeTable(DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO) {
        DbQueryProperty dbQueryProperty = new DbQueryProperty(datasourceCreaTeTableReqDTO.getDatasourceType()
                , datasourceCreaTeTableReqDTO.getIp(), datasourceCreaTeTableReqDTO.getPort(), datasourceCreaTeTableReqDTO.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            throw new DataQueryException("数据库连接失败");
        }

        int tableStatus = dbQuery.generateCheckTableExistsSQL(dbQueryProperty, datasourceCreaTeTableReqDTO.getTableName());
        if (tableStatus > 0) {
            dbQuery.close();
            return false;
        }

        List<String> tableSQLList = dbQuery.generateCreateTableSQL(dbQueryProperty, datasourceCreaTeTableReqDTO.getTableName(), datasourceCreaTeTableReqDTO.getTableComment(), datasourceCreaTeTableReqDTO.getColumnsList());

        for (String sql : tableSQLList) {
            dbQuery.execute(sql);
        }
        dbQuery.close();

        return true;
    }


    @Override
    public boolean creaDatasourceTeTable(DbQuery dbQuery, DbQueryProperty dbQueryProperty, DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO) {

        int tableStatus = dbQuery.generateCheckTableExistsSQL(dbQueryProperty, datasourceCreaTeTableReqDTO.getTableName());
        if (tableStatus > 0) {
            return false;
        }

        List<String> tableSQLList = dbQuery.generateCreateTableSQL(dbQueryProperty, datasourceCreaTeTableReqDTO.getTableName(), datasourceCreaTeTableReqDTO.getTableComment(), datasourceCreaTeTableReqDTO.getColumnsList());

        for (String sql : tableSQLList) {
            dbQuery.execute(sql);
        }
        return true;
    }


    @Override
    public boolean creaDatasourceTeTableApi(DatasourceCreaTeTableReqDTO datasourceCreaTeTableReqDTO) {
        return this.creaDatasourceTeTable(datasourceCreaTeTableReqDTO);
    }


    @Override
    public boolean creaDatasourceTeTableApi(DbQuery dbQuery, DbQueryProperty dbQueryProperty, DatasourceCreaTeTableReqDTO creaTeTableReqDTO) {
        return this.creaDatasourceTeTable(dbQuery, dbQueryProperty, creaTeTableReqDTO);
    }


    @Override
    public boolean creaDatasourceTeTableListApi(DatasourceCreaTeTableListReqDTO datasourceCreaTeTableReqDTO) {

        DbQueryProperty dbQueryProperty = new DbQueryProperty(datasourceCreaTeTableReqDTO.getDatasourceType()
                , datasourceCreaTeTableReqDTO.getIp(), datasourceCreaTeTableReqDTO.getPort(), datasourceCreaTeTableReqDTO.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        List<DatasourceCreaTeTableReqDTO> dtoList = datasourceCreaTeTableReqDTO.getDtoList();
        if (CollectionUtils.isNotEmpty(dtoList)) {
            for (DatasourceCreaTeTableReqDTO creaTeTableReqDTO : dtoList) {
                this.creaDatasourceTeTable(dbQuery, dbQueryProperty, creaTeTableReqDTO);
            }
        }
        return true;
    }

    @Override
    public PageResult<AttProjectRespDTO> getNoDppAddList(AttProjectReqDTO pageReqVO) {
        PageResult<AttProjectRespDTO> attProjectPage = attProjectApi.getAttProjectPage(pageReqVO);
        Map<Long, DaDatasourceProjectRelDO> datasourceProjectRelDOMap = new HashMap<>();
        if (pageReqVO.getDatasourceId() != null) {
            DaDatasourceProjectRelDO daDatasourceProjectRelDO = new DaDatasourceProjectRelDO();
            daDatasourceProjectRelDO.setDatasourceId(pageReqVO.getDatasourceId());
            List<DaDatasourceProjectRelDO> daDatasourceProjectRelList = daDatasourceProjectRelService.getDaDatasourceProjectRelList(daDatasourceProjectRelDO);
            datasourceProjectRelDOMap = daDatasourceProjectRelList.stream().collect(Collectors.toMap(DaDatasourceProjectRelDO::getProjectId, daDatasourceProjectRelDO1 -> daDatasourceProjectRelDO1));
        }
        for (Object row : attProjectPage.getRows()) {
            AttProjectRespDTO attProjectRespDTO = (AttProjectRespDTO) row;
            Boolean dppAssigned = datasourceProjectRelDOMap.get(attProjectRespDTO.getId()) != null && datasourceProjectRelDOMap.get(attProjectRespDTO.getId()).getDppAssigned();
            attProjectRespDTO.setDppAssigned(dppAssigned);
        }
        return attProjectPage;
    }

    @Override
    public List<DaDatasourceDO> getDaDatasourceDppNoKafka(DaDatasourcePageReqVO daDatasource) {
        List<Long> idList = new ArrayList<>();
        Map<Long, DaDatasourceProjectRelDO> datasourceProjectRelDOMap = new HashMap<>();
        if (StringUtils.isNotEmpty(daDatasource.getProjectCode())) {
            DaDatasourceProjectRelDO daDatasourceProjectRelDO = new DaDatasourceProjectRelDO();
            daDatasourceProjectRelDO.setProjectCode(daDatasource.getProjectCode());
            List<DaDatasourceProjectRelDO> daDatasourceProjectRelList = daDatasourceProjectRelService.getJoinProjectAndDatasource(daDatasourceProjectRelDO);
            if (daDatasourceProjectRelList.isEmpty()) {
                return new ArrayList<>();
            }
            datasourceProjectRelDOMap = daDatasourceProjectRelList.stream().collect(Collectors.toMap(DaDatasourceProjectRelDO::getDatasourceId, daDatasourceProjectRelDO1 -> daDatasourceProjectRelDO1));
            idList = datasourceProjectRelDOMap.keySet().stream().collect(Collectors.toList());
            daDatasource.setIdList(idList);
        }

        LambdaQueryWrapperX<DaDatasourceDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.inIfPresent(DaDatasourceDO::getId, idList)
                .neIfPresent(DaDatasourceDO::getDatasourceType, "Kafka")
                .likeIfPresent(DaDatasourceDO::getDatasourceType, daDatasource.getDatasourceType())
                .likeIfPresent(DaDatasourceDO::getDatasourceName, daDatasource.getDatasourceName());
        List<DaDatasourceDO> datasourceDOList = daDatasourceMapper.selectList(queryWrapperX);
        for (DaDatasourceDO daDatasourceDO : datasourceDOList) {
            DaDatasourceProjectRelDO datasourceProjectRelDO = datasourceProjectRelDOMap.get(daDatasourceDO.getId()) == null ? new DaDatasourceProjectRelDO() : datasourceProjectRelDOMap.get(daDatasourceDO.getId());
            if (idList.contains(daDatasourceDO.getId()) && !datasourceProjectRelDO.getDppAssigned()) {
                daDatasourceDO.setIsAdminAddTo(false);
                daDatasourceDO.setProjectName(datasourceProjectRelDO.getProjectName());
            }
        }
        return datasourceDOList;
    }

    @Override
    public tech.qiantong.qdata.common.database.core.PageResult<Map<String, Object>> executeSqlQuery(DaDatasourcePageReqVO daDatasource) {
        String sqlText = decryptSqlText(daDatasource.getSqlText());
        DbQuery dbQuery = getDbQuery(daDatasource);
        int[] paging = getPagingParameters(daDatasource);
        // paging 数组中：paging[0] 为 offset，paging[1] 为 pageSize
        tech.qiantong.qdata.common.database.core.PageResult<Map<String, Object>> mapPageResult = dbQuery.queryByPage(sqlText, paging[0], paging[1]);
        dbQuery.close();
        return mapPageResult;
    }

    @SneakyThrows
    @Override
    public void exportSqlQueryResult(HttpServletResponse response, DaDatasourcePageReqVO daDatasource) {
        String sqlText = decryptSqlText(daDatasource.getSqlText());
        DbQuery dbQuery = getDbQuery(daDatasource);
        int[] paging = getPagingParameters(daDatasource);
        tech.qiantong.qdata.common.database.core.PageResult<Map<String, Object>> result = dbQuery.queryByPage(sqlText, paging[0], paging[1]);
        dbQuery.close();
        List<Map<String, Object>> dataList = result.getData();
        // 移除每条记录中的 ROW_ID 字段
        dataList.forEach(map -> map.remove("ROW_ID"));
        String schemeName = "导出第" + paging[2] + "页数据-" + IdUtil.simpleUUID();
        exportByList(response, dataList, schemeName);
    }

    @Override
    public List<DbColumn> sqlParse(String sourceId, String sqlText) {
        Statement stmt;
        try {
            stmt = CCJSqlParserUtil.parse(sqlText);
        } catch (JSQLParserException e) {
            throw new ServiceException("SQL语法有问题，解析出错");
        }

        // 查询数据源信息
        DaDatasourceRespDTO datasourceById = this.getDatasourceById(Long.valueOf(sourceId));
        DbQueryProperty dbQueryProperty = new DbQueryProperty(
                datasourceById.getDatasourceType(),
                datasourceById.getIp(),
                datasourceById.getPort(),
                datasourceById.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        return dbQuery.getColumnsByQuerySql(sqlText);
    }

    /**
     * 解密传入的 SQL 语句
     */
    private String decryptSqlText(String encryptedSqlText) {
        String sqlText = "";
        try {
//            sqlText =  encryptedSqlText;
            sqlText = AesEncryptUtil.desEncrypt(encryptedSqlText).trim();
        } catch (Exception e) {
            throw new RuntimeException("执行语句解密异常，请联系管理员！", e);
        }

        if (sqlText == null || sqlText.isEmpty()) {
            throw new DataQueryException("SQL语句不能为空");
        }

        // 检查是否包含分隔符';'
        int semicolonCount = sqlText.length() - sqlText.replace(";", "").length();
        if (semicolonCount > 0) {
            int firstIndex = sqlText.indexOf(";");
            int lastIndex = sqlText.lastIndexOf(";");
            // 若';'不只出现在末尾，则视为存在多个SQL语句
            if (firstIndex != lastIndex || lastIndex != sqlText.length() - 1) {
                throw new DataQueryException("仅支持单个查询SQL语句，不允许存在多个语句或中间使用';'分隔");
            }
            // 移除末尾的';'
            sqlText = sqlText.substring(0, sqlText.length() - 1).trim();
            if (sqlText.contains(";")) {
                throw new DataQueryException("仅支持单个查询SQL语句，不允许存在多个语句或中间使用';'分隔");
            }
        }

        // 确保SQL以"select"开头（忽略大小写）
        if (!sqlText.toLowerCase().startsWith("select")) {
            throw new DataQueryException("仅允许执行查询操作的SQL语句");
        }

        // 进一步检测是否包含非查询的SQL标识
        validateQueryOnly(sqlText);

        return sqlText;
    }


    /**
     * 检查SQL语句中是否存在非查询操作的关键字。
     * 为避免误报，先将SQL语句中位于字符串中的内容去除，再通过正则表达式判断
     */
    private void validateQueryOnly(String sqlText) {
        // 移除SQL中的字符串常量，避免因字符串中包含敏感词导致误判
        String withoutStringLiterals = sqlText.replaceAll("'[^']*'", "");
        // 定义不允许出现的关键词（全词匹配，忽略大小写）
        String[] forbiddenKeywords = {"insert", "update", "delete", "create", "drop", "alter", "truncate", "exec", "execute", "merge"};
        String lowerSql = withoutStringLiterals.toLowerCase();

        for (String keyword : forbiddenKeywords) {
            // \b 确保匹配关键字边界，避免匹配到部分字段
            if (Pattern.compile("\\b" + keyword + "\\b").matcher(lowerSql).find()) {
                throw new DataQueryException("SQL语句中包含非查询操作标识: " + keyword);
            }
        }
    }

    /**
     * 根据请求中的数据源 ID 获取 DbQuery 对象
     */
    private DbQuery getDbQuery(DaDatasourcePageReqVO daDatasource) {
        DaDatasourceDO datasource = this.getDaDatasourceById(daDatasource.getId());
        if (datasource == null) {
            throw new DataQueryException("数据源详情信息查询失败");
        }
        DbQueryProperty property = new DbQueryProperty(
                datasource.getDatasourceType(),
                datasource.getIp(),
                datasource.getPort(),
                datasource.getDatasourceConfig()
        );
        DbQuery dbQuery = dataSourceFactory.createDbQuery(property);
        if (!dbQuery.valid()) {
            throw new DataQueryException("数据库连接失败");
        }
        return dbQuery;
    }

    /**
     * 获取分页参数：返回一个 int 数组，其中
     * paging[0] 为 offset，
     * paging[1] 为 pageSize，
     * paging[2] 为 pageNum（用于导出时显示页码）
     */
    private int[] getPagingParameters(DaDatasourcePageReqVO daDatasource) {
        int pageSize = daDatasource.getPageSize() != null ? daDatasource.getPageSize() : 20;
        int pageNum = daDatasource.getPageNum() != null ? daDatasource.getPageNum() : 1;
        int offset = pageNum > 0 ? (pageNum - 1) * pageSize : 0;
        return new int[]{offset, pageSize, pageNum};
    }

    @SneakyThrows
    private static void exportByList(HttpServletResponse response, List<Map<String, Object>> dataList, String tableName) {
        if (dataList == null) {
            throw new RuntimeException("暂无表单信息");
        }

        // 获取第一行数据的所有列名作为 order
        Map<String, Object> firstRow = dataList.get(0);
        // 使用 Set 可以确保列名唯一性
        List<String> order = new ArrayList<>(firstRow.keySet());

        //1.创建工作博
        XSSFWorkbook workbook = new XSSFWorkbook();

        //头部字段字体
        XSSFFont headFont = workbook.createFont();
        //字体高度
        headFont.setFontHeightInPoints((short) 24);
        //字体
        headFont.setFontName("宋体");
        headFont.setBold(true);
        // 设置单元格类型
        XSSFCellStyle headCellStyle = workbook.createCellStyle();
        headCellStyle.setFont(headFont);
        //水平布局：居中
        headCellStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headCellStyle.setWrapText(true);

        //标注划字体
        XSSFFont font = workbook.createFont();
        //字体高度
        font.setFontHeightInPoints((short) 11);
        //字体
        font.setFontName("宋体");

        //列样式
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        //水平布局：居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);

        //2。创建工作表
        XSSFSheet sheet = workbook.createSheet(tableName);

        //冻结第一行
        sheet.createFreezePane(0, 1, 0, 1);

        //3。创建字段行
        XSSFRow row = sheet.createRow(0);
        row.setHeight((short) (2 * 200));

        int index = 0;
        for (String key : order) {
            //设置默认宽度
            sheet.setColumnWidth(index, 25 * 256);
            XSSFCell labelCell = row.createCell(index);
            labelCell.setCellStyle(cellStyle);
            labelCell.setCellValue(key);
            index++;
        }

        //4。数据行
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> map = dataList.get(i);
            //数据行
            XSSFRow dataRow = sheet.createRow(i + 1);
            dataRow.setHeight((short) (4 * 200));
            int columnIndex = 0;
            for (String key : order) {
                Object valueObj = map.get(key);
                String value = "";
                if (valueObj instanceof Date) {
                    // 如果是日期类型，转换为固定格式的字符串
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    value = dateFormat.format((Date) valueObj);
                } else {
                    value = String.valueOf(valueObj);
                }
                XSSFCell labelCell = dataRow.createCell(columnIndex);
                labelCell.setCellStyle(cellStyle);
                labelCell.setCellValue(value);
                columnIndex++;
            }
        }

        if (response != null) {
            //5.输出流 输出
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                workbook.write(baos);
                baos.flush();
                byte[] aa = baos.toByteArray();
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(tableName + ".xls", "UTF-8"));
                response.setHeader("Content-Type", "application/vnd.ms-excel");
                response.setCharacterEncoding("UTF-8");
                response.getOutputStream().write(aa);
                response.setContentLength(aa.length);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("系统错误");
            } finally {
                if (response.getOutputStream() != null) {
                    response.getOutputStream().flush();
                }
                //6.刷新流，释放资源
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
                //关闭资源
                workbook.close();
            }
        }
    }


    @Override
    public Boolean editDatasourceStatus(Long datasourceId, Long status) {
        return this.update(Wrappers.lambdaUpdate(DaDatasourceDO.class)
                .eq(DaDatasourceDO::getId, datasourceId)
                .set(DaDatasourceDO::getValidFlag, status));
    }

    /**
     * @param id        数据源id
     * @param tableName 表名称
     * @return
     */
    @Override
    public List<DbColumn> getDbTableColumns(Long id, String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new DataQueryException("表名不能为空");
        }

        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
        if (daDatasourceBy == null) {
            throw new DataQueryException("数据源详情信息查询失败");
        }

        DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceBy.getDatasourceType()
                , daDatasourceBy.getIp(), daDatasourceBy.getPort(), daDatasourceBy.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            throw new DataQueryException("数据库连接失败");
        }
        List<DbColumn> tableColumns = dbQuery.getTableColumns(dbQueryProperty, tableName);
        dbQuery.close();

        return tableColumns;
    }

    @Override
    public DbTable getDbTable(Long datasourceId, String tableName) {
        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(datasourceId);
        if (daDatasourceBy == null) {
            throw new DataQueryException("数据源详情信息查询失败");
        }

        DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceBy.getDatasourceType()
                , daDatasourceBy.getIp(), daDatasourceBy.getPort(), daDatasourceBy.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            throw new DataQueryException("数据库连接失败");
        }
        List<DbTable> tables = dbQuery.getTables(dbQueryProperty);
        if (StringUtils.isNotEmpty(tableName)) {
            tables = tables.stream()
                    .filter(dbTable -> org.apache.commons.lang3.StringUtils.indexOfIgnoreCase(dbTable.getTableName(), tableName) > -1)
                    .collect(Collectors.toList());
        }
        dbQuery.close();
        if (tables.size() > 0) {
            return tables.get(0);
        }
        return null;
    }

    @Override
    public List<DbName> getDatabaseListByDatasourceId(Long id) {
        DaDatasourceRespDTO datasource = this.getDatasourceById(id);
        if (datasource == null) {
            throw new DataQueryException("数据源详情信息查询失败");
        }

        DbQueryProperty baseProperty = new DbQueryProperty(
                datasource.getDatasourceType(),
                datasource.getIp(),
                datasource.getPort(),
                datasource.getDatasourceConfig()
        );

        // 1. 先获取顶层数据库
        List<DbName> dbNames;
        DbQuery rootQuery = dataSourceFactory.createDbQuery(baseProperty);
        try {
            if (!rootQuery.valid()) {
                throw new DataQueryException("数据库连接失败");
            }
            dbNames = rootQuery.getDbNames(null);
        } finally {
            rootQuery.close();
        }

        if (CollectionUtils.isEmpty(dbNames)) {
            return dbNames;
        }

        // 单层结构，直接返回
        if (dbNames.get(0).getLevel() == 1 && dbNames.get(0).getTotalLevels() == 1) {
            return dbNames;
        }

        // 2. 逐个加载下级
        for (DbName dbName : dbNames) {

            // Kingbase / PostgreSQL 需要切换 dbName
            DbQueryProperty childProperty = baseProperty;
            if (DbType.KINGBASE8.getDb().equals(baseProperty.getDbType())
                    || DbType.POSTGRE_SQL.getDb().equals(baseProperty.getDbType())) {

                childProperty = baseProperty.copy();
                childProperty.setDbName(dbName.getDbName());
            }

            DbQuery childQuery = dataSourceFactory.createDbQuery(childProperty);
            try {
                if (!childQuery.valid()) {
                    // 不影响整体，直接跳过当前节点
                    continue;
                }
                List<DbName> children = childQuery.getDbNames(dbName);
                dbName.setChildren(children);
            } catch (Exception e) {
                // 可选：记录日志
                // log.warn("获取数据库 {} 子级失败", dbName.getDbName(), e);
            } finally {
                childQuery.close();
            }
        }

        return dbNames;
    }

    @Override
    public List<DaDatasourceRespDTO> getDatabaseListByIds(List<Long> ids) {
        List<DaDatasourceDO> daDatasourceDOS = daDatasourceMapper.selectBatchIds(ids);
        return BeanUtils.toBean(daDatasourceDOS, DaDatasourceRespDTO.class);
    }

    private List<DaDiscoveryTableDO> fetchDiscoveryTableList(DaDiscoveryTaskRespVO daDiscoveryTaskDO, Long daDiscoveryTaskLog) {
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "开始从本地库中获取发现任务表快照列表，任务ID：" + daDiscoveryTaskDO.getId());
        DaDiscoveryTablePageReqVO daDiscoveryTablePageReqVO = new DaDiscoveryTablePageReqVO();
        daDiscoveryTablePageReqVO.setTaskId(daDiscoveryTaskDO.getId());

        List<DaDiscoveryTableDO> result = iDaDiscoveryTableService.getDaDiscoveryTableList(daDiscoveryTablePageReqVO);
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "从本地库中获取发现任务表快照列表成功，表数量：" + (result != null ? result.size() : 0));
        return result;
    }

    private List<DaDiscoveryColumnDO> fetchDaDiscoveryColumnDOList(DaDiscoveryTableDO matchedTable, Long daDiscoveryTaskLog) {
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "开始从本地库中获取表的列快照信息，表ID：" + matchedTable.getId() + "，任务ID：" + matchedTable.getTaskId());

        DaDiscoveryColumnPageReqVO daDiscoveryTablePageReqVO = new DaDiscoveryColumnPageReqVO();
        daDiscoveryTablePageReqVO.setTaskId(matchedTable.getTaskId());
        daDiscoveryTablePageReqVO.setTableId(matchedTable.getId());

        List<DaDiscoveryColumnDO> result = iDaDiscoveryColumnService.getDaDiscoveryColumnList(daDiscoveryTablePageReqVO);
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "从本地库中获取表的列快照信息成功，列数量：" + (result != null ? result.size() : 0));
        return result;
    }

    private List<DaDiscoveryTableDO> mapToMetadataTableList(List<DbTable> tables, Long taskId) {
        return tables.stream().map(table -> {
            DaDiscoveryTableDO metadataTable = new DaDiscoveryTableDO();
            metadataTable.setTaskId(taskId);
            metadataTable.setTableName(table.getTableName());
            metadataTable.setTableComment(table.getTableComment());
            return metadataTable;
        }).collect(Collectors.toList());
    }

    private void updateTableDataCount(DbQuery dbQuery, DaDiscoveryTableDO table, int fieldCount) {
        int dataCount = dbQuery.countNew(table.getTableName(), new HashMap<>());
        table.setDataCount((long) dataCount);
        table.setFieldCount((long) fieldCount);
        table.setCreateBy("超级管理员");
        table.setCreatorId(1L);
    }


    private int updateTableStatus(DaDiscoveryTableDO matchedTable, DaDiscoveryTableDO table, boolean modifiedTablesBoolean, Long daDiscoveryTaskLog) {
        if (modifiedTablesBoolean) {
            //1:新增，2:修改，3:删除，4:无变化
            table.setChangeFlag("2");
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "更新表状态为修改：" + matchedTable.getTableName());

            iDaDiscoveryTableService.updateDaDiscoveryTable(table);
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "更新完毕");
            return 1;
        } else {
            //1:新增，2:修改，3:删除，4:无变化
            matchedTable.setChangeFlag("4");
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "更新表状态为无变化：" + matchedTable.getTableName());

            iDaDiscoveryTableService.updateDaDiscoveryTable(matchedTable);
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "更新完毕");
            return 0;
        }

    }
    private void saveNewTable(DaDiscoveryTableDO table, List<DbColumn> columns, DbQuery dbQuery, DbQueryProperty dbQueryProperty, Long daDiscoveryTaskLog) {
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "保存新表：" + table.getTableName());

        updateTableDataCount(dbQuery, table, columns.size());

        //1:新增，2:修改，3:删除，4:无变化
        table.setChangeFlag("1");
        iDaDiscoveryTableService.createDaDiscoveryTable(table);

        if (CollUtil.isNotEmpty(columns)) {
            List<DaDiscoveryColumnDO> metadataColumnEntityList = columns.stream()
                    .map(column -> new DaDiscoveryColumnDO(table.getTaskId(), table.getId(), column))
                    .collect(Collectors.toList());
            metadataColumnEntityList.forEach(iDaDiscoveryColumnService::createDaDiscoveryColumn);
        }
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "保存完毕");
    }

    public DaDiscoveryTableDO findMatchedTable(DaDiscoveryTableDO table, List<DaDiscoveryTableDO> daDiscoveryTableDOList) {
        return daDiscoveryTableDOList.stream()
                .filter(existingTable -> existingTable.getTableName().equals(table.getTableName()) &&
                        existingTable.getTaskId().equals(table.getTaskId()))
                .findFirst()
                .orElse(null);  // 如果没有匹配到，返回null
    }

    private boolean isTableCommentModified(DaDiscoveryTableDO table, DaDiscoveryTableDO matchedTable) {
        return !StringUtils.equals(table.getTableComment(), matchedTable.getTableComment());
    }

    private List<DaDiscoveryColumnDO> generateMetadataColumnList(List<DbColumn> columns, DaDiscoveryTableDO matchedTable) {
        if (CollUtil.isEmpty(columns)) {
            return new ArrayList<>();
        }

        return columns.stream()
                .map(column -> new DaDiscoveryColumnDO(matchedTable.getTaskId(), matchedTable.getId(), column))
                .collect(Collectors.toList());
    }

    private boolean compareColumnsAndUpdate(List<DaDiscoveryColumnDO> metadataColumnEntityList, List<DaDiscoveryColumnDO> discoveryColumnDOList) {
        boolean modifiedTablesBoolean = false;

        for (DaDiscoveryColumnDO column : metadataColumnEntityList) {
            DaDiscoveryColumnDO matchedColumn = findMatchedColumn(column, discoveryColumnDOList);
            if (matchedColumn == null) {
                modifiedTablesBoolean = true;
                iDaDiscoveryColumnService.createDaDiscoveryColumn(column);
            } else if (!column.isEqual(matchedColumn)) {
                modifiedTablesBoolean = true;
                iDaDiscoveryColumnService.updateDaDiscoveryColumn(column);
            }
        }
        return modifiedTablesBoolean;
    }

    public DaDiscoveryColumnDO findMatchedColumn(DaDiscoveryColumnDO table, List<DaDiscoveryColumnDO> daDiscoveryTableDOList) {
        return daDiscoveryTableDOList.stream()
                .filter(existingTable -> StringUtils.equals(existingTable.getColumnName(), table.getColumnName()))
                .findFirst()
                .orElse(null);  // 如果没有匹配到，返回null
    }


    private boolean deleteUnmatchedColumns(List<DaDiscoveryColumnDO> discoveryColumnDOList, List<DaDiscoveryColumnDO> metadataColumnEntityList) {
        List<DaDiscoveryColumnDO> notInMetadataTable = findNotInDaDiscoveryColumn(discoveryColumnDOList, metadataColumnEntityList);
        if (CollectionUtils.isEmpty(notInMetadataTable)) {
            return false;
        }

        Collection<Long> idList = notInMetadataTable.stream()
                .map(DaDiscoveryColumnDO::getId)
                .collect(Collectors.toList());

        iDaDiscoveryColumnService.removeDaDiscoveryColumn(idList);
        return true;
    }

    public List<DaDiscoveryColumnDO> findNotInDaDiscoveryColumn(List<DaDiscoveryColumnDO> discoveryColumnDOList, List<DaDiscoveryColumnDO> metadataTableEntityList) {
        return discoveryColumnDOList.stream()
                .filter(table -> metadataTableEntityList.stream()
                        .noneMatch(existingTable -> {
                            return StringUtils.equals(existingTable.getColumnName(), table.getColumnName());
                        }))
                .collect(Collectors.toList()); // 返回daDiscoveryTableDOList中在metadataTableEntityList中不存在的table
    }


    private int updateExistingTable(DbQuery dbQuery, DaDiscoveryTableDO matchedTable, DaDiscoveryTableDO table, List<DbColumn> columns, Long daDiscoveryTaskLog) {
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "更新表：" + table.getTableName());

        boolean modifiedTablesBoolean = false;
        //查询表存的快照字段结构
        List<DaDiscoveryColumnDO> discoveryColumnDOList = this.fetchDaDiscoveryColumnDOList(matchedTable, daDiscoveryTaskLog);
        discoveryColumnDOList = discoveryColumnDOList == null ? new ArrayList<>() : discoveryColumnDOList;

        if (isTableCommentModified(table, matchedTable)) {
            modifiedTablesBoolean = true;
        }

        List<DaDiscoveryColumnDO> metadataColumnEntityList = generateMetadataColumnList(columns, matchedTable);

        modifiedTablesBoolean |= compareColumnsAndUpdate(metadataColumnEntityList, discoveryColumnDOList);

        modifiedTablesBoolean |= deleteUnmatchedColumns(discoveryColumnDOList, metadataColumnEntityList);

        updateTableDataCount(dbQuery, table, columns.size());

        return updateTableStatus(matchedTable, table, modifiedTablesBoolean, daDiscoveryTaskLog);
    }

    private Map<String, Object> logSchemaModifications(DbQueryProperty dbQueryProperty, DbQuery dbQuery, DaDiscoveryTaskRespVO daDiscoveryTaskById, Long daDiscoveryTaskLog) {
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "开始执行模式修改操作，任务ID：" + daDiscoveryTaskById.getId());

        int newTables = 0;
        int modifiedTables = 0;
        int deletedTables = 0;
        int totalTables = 0;

        List<DaDiscoveryTableDO> daDiscoveryTableDOList = this.fetchDiscoveryTableList(daDiscoveryTaskById, daDiscoveryTaskLog);
        daDiscoveryTableDOList = daDiscoveryTableDOList == null ? new ArrayList<>() : daDiscoveryTableDOList;

        List<DbTable> tables = dbQuery.getTables(dbQueryProperty);
        List<DaDiscoveryTableDO> metadataTableEntityList = new ArrayList<>();
        if (CollUtil.isNotEmpty(tables)) {
            totalTables = tables.size();
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "从数据源中，实时获取表列数量信息：" + totalTables);

            metadataTableEntityList = mapToMetadataTableList(tables, daDiscoveryTaskById.getId());
            if (CollUtil.isNotEmpty(metadataTableEntityList)) {
                for (DaDiscoveryTableDO table : metadataTableEntityList) {
                    DaDiscoveryTableDO matchedTable = findMatchedTable(table, daDiscoveryTableDOList);
                    iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "正在处理表：" + table.getTableName());

                    List<DbColumn> columns = dbQuery.getTableColumns(dbQueryProperty, table.getTableName());

                    columns = columns == null ? new ArrayList<>() : columns;
                    iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "从数据源中，实时获取列数量信息：" + columns.size());
                    if (matchedTable == null) {
                        newTables++;
                        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "新表发现，表：" + table.getTableName() + "，开始保存");
                        saveNewTable(table, columns, dbQuery, dbQueryProperty, daDiscoveryTaskLog);
                    } else {
                        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "查看表[ " + table.getTableName() + " ]库中配置信息");
                        //是否忽略;0:否，1：是
                        String ignoreFlag = matchedTable.getIgnoreFlag();
                        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "查看表[ " + table.getTableName() + " ]库中配置信息,发现配置ignoreFlag为：" + ignoreFlag);
                        if (StringUtils.equals("1", ignoreFlag)) {
                            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "查看表[ " + table.getTableName() + " ]库中配置信息,发现配置为：忽略。该表结束扫描！");
                            continue;
                        }
                        table.setId(matchedTable.getId());
                        modifiedTables += updateExistingTable(dbQuery, matchedTable, table, columns, daDiscoveryTaskLog);
                        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "已存在表更新，表：" + table.getTableName());

                    }
                }
            }
        }

        deletedTables = deleteUnmatchedTables(daDiscoveryTableDOList, metadataTableEntityList, daDiscoveryTaskLog);
        String executionTime = DateUtils.getExecutionTime();

        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "模式修改操作完成，总表数：" + totalTables + "。其中，新增表数：" + newTables + "，修改表数：" + modifiedTables + "，删除表数：" + deletedTables);

        Map<String, Object> map = new HashMap<>();
        map.put("taskName", daDiscoveryTaskById.getName());
        map.put("executionTime", executionTime);
        map.put("totalTables", totalTables);
        map.put("newTables", newTables);
        map.put("modifiedTables", modifiedTables);
        map.put("deletedTables", deletedTables);

        daDiscoveryTaskById.setLastTableCount((long) (newTables + modifiedTables + deletedTables));
        return map;
    }


    public List<DaDiscoveryTableDO> findNotInMetadataTable(List<DaDiscoveryTableDO> daDiscoveryTableDOList, List<DaDiscoveryTableDO> metadataTableEntityList) {
        return daDiscoveryTableDOList.stream()
                .filter(table -> metadataTableEntityList.stream()
                        .noneMatch(existingTable -> existingTable.getTableName().equals(table.getTableName()) &&
                                existingTable.getTaskId().equals(table.getTaskId())))
                .collect(Collectors.toList()); // 返回daDiscoveryTableDOList中在metadataTableEntityList中不存在的table
    }

    private int deleteUnmatchedTables(List<DaDiscoveryTableDO> daDiscoveryTableDOList, List<DaDiscoveryTableDO> metadataTableEntityList, Long daDiscoveryTaskLog) {
        List<DaDiscoveryTableDO> notInMetadataTable = findNotInMetadataTable(daDiscoveryTableDOList, metadataTableEntityList);
        if (CollectionUtils.isEmpty(notInMetadataTable)) return 0;

        for (DaDiscoveryTableDO daDiscoveryTableDO : notInMetadataTable) {
            daDiscoveryTableDO.setUpdateBy("超级管理员");
            daDiscoveryTableDO.setUpdatorId(1L);
            //1:新增，2:修改，3:删除，4:无变化
            daDiscoveryTableDO.setChangeFlag("3");
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "删除未匹配表：" + daDiscoveryTableDO.getTableName());
            iDaDiscoveryTableService.updateDaDiscoveryTable(daDiscoveryTableDO);
        }

        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "删除完毕");
        return notInMetadataTable.size();
    }



    private Map<String, Object> runJobTableSchemaUpdates(DaDiscoveryTaskRespVO daDiscoveryTaskById, Long daDiscoveryTaskLog) {

        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "任务执行-根据数据源编号，获取发现任务的 数据源详细信息");
        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(daDiscoveryTaskById.getDatasourceId());
        if (daDatasourceBy == null) {
            throw new DataQueryException("任务执行-根据数据源编号，获取发现任务的 数据源详情信息查询失败！");
        }
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "任务执行-根据数据源编号，获取发现任务的 数据源详细信息成功");
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "任务执行-根据数据源链接信息，建立实时数据源链接");
        DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceBy.getDatasourceType()
                , daDatasourceBy.getIp(), daDatasourceBy.getPort(), daDatasourceBy.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            throw new DataQueryException("任务执行-根据数据源链接信息，建立实时数据源链接 失败！");
        }
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "任务执行-根据数据源链接信息，建立实时数据源链接 成功");

        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "任务执行-根据数据源链接，开始进入获取实时库中信息方法");
        try {
            Map<String, Object> map = logSchemaModifications(dbQueryProperty, dbQuery, daDiscoveryTaskById, daDiscoveryTaskLog);
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "任务执行-根据数据源链接，获取实时库中信息方法结束");
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "任务执行-信息如下 map:" + map.toString());
            iDaDiscoveryTaskService.updateDaDiscoveryTask(daDiscoveryTaskById);
            return map;
        } catch (Exception e) {
            throw e;
        } finally {
            dbQuery.close();
        }
    }


    /**
     * @param id
     */
    @Override
    public void detectTableSchemaUpdates(Long id) {
        String key = "detectTableSchemaUpdates-" + id;
        String status = redisService.get(key);
        if (StringUtils.isEmpty(status) && StringUtils.equals("1", status)) {
            throw new RuntimeException("历史任务未执行完毕，请稍后重试");
        }
        DaDiscoveryTaskRespVO daDiscoveryTaskById = iDaDiscoveryTaskService.getDaDiscoveryTaskById(id);
        if (daDiscoveryTaskById == null) {
            throw new DataQueryException("任务执行-根据发现任务编号，获取发现任务详细信息 失败!");
        }
        redisService.set(key, "1", 1200);
        //创建日志记录表
        DaDiscoveryTaskLogSaveReqVO createReqVO = new DaDiscoveryTaskLogSaveReqVO();
        Date executionDate = DateUtils.getExecutionDate();
        createReqVO.setStartTime(executionDate);
        createReqVO.populateFromTask(daDiscoveryTaskById);
        Long daDiscoveryTaskLog = iDaDiscoveryTaskLogService.createDaDiscoveryTaskLog(createReqVO);
        createReqVO.setId(daDiscoveryTaskLog);
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "任务执行-根据发现任务编号，获取发现任务详细信息成功");

        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "任务开始执行");

        try {
            daDiscoveryTaskById.setLastExecuteTime(executionDate);
            //
            Map<String, Object> map = runJobTableSchemaUpdates(daDiscoveryTaskById, daDiscoveryTaskLog);

            int newTables = MapUtils.getIntValue(map, "newTables");
            int modifiedTables = MapUtils.getIntValue(map, "modifiedTables");
            int deletedTables = MapUtils.getIntValue(map, "deletedTables");
            createReqVO.setNewTableCount((long) newTables);
            createReqVO.setModifiedTableCount((long) modifiedTables);
            createReqVO.setDeletedTableCount((long) deletedTables);

            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "任务执行-根据任务执行信息，开始对本次任务发放站内信");
            iSysMessageService.sendDbChangeMessage(daDiscoveryTaskById.getContactId(), map);
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "任务执行-根据任务执行信息，对本次任务站内信发放 完毕");
            createReqVO.setStatus("2");
        } catch (Exception e) {
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "任务失败");
            createReqVO.setStatus("3");
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, e.getMessage().toString());
            redisService.set(key, "3", 300);
        } finally {
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "FINALIZE_SESSION");
            createReqVO.setPath("");
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "任务结束");
            createReqVO.setEndTime(DateUtils.getExecutionDate());
            iDaDiscoveryTaskLogService.updateDaDiscoveryTaskLog(createReqVO);
            redisService.set(key, "2", 300);
        }
    }
}
