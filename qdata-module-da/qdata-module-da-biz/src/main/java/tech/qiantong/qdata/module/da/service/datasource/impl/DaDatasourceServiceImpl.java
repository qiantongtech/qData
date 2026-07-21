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

package tech.qiantong.qdata.module.da.service.datasource.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import tech.qiantong.qdata.common.utils.MessageUtils;
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
 * Datasource Service business layer processing
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
     * Initialize the datasource cache in Redis after project startup.
     *
     * Functionality:
     * 1. Load all datasource records from the database;
     * 2. Extract key fields using the DaDatasourceDO.simplify() method;
     * 3. Write the results to a Redis Hash:
     *
     * Scenarios:
     * - After project startup, warm up the datasource cache so that task scheduling
     *   and Worker nodes can directly obtain configuration info from Redis;
     * - Avoid strong runtime dependencies on the database or middle-platform services.
     *
     * Note:
     * - Optional: the existing "datasource" cache in Redis can be cleared before initialization.
     */
    @PostConstruct
    public void initDatasourceCache() {
        try {
            List<DaDatasourceDO> list = daDatasourceMapper.selectList();
            if (list == null || list.isEmpty()) {
                log.info("[Datasource Cache Init] No datasource records found, skipping initialization.");
                return;
            }

            // Optional: clear cache before initialization
            // redisService.del("datasource");

            for (DaDatasourceDO ds : list) {
                if (ds == null || ds.getId() == null) {
                    continue;
                }

                try {
                    // Latest datasource connection info
                    String field = String.valueOf(ds.getId());
                    String value = com.alibaba.fastjson2.JSONObject.toJSONString(ds.simplify());
                    redisService.hashPut("datasource", field, value);

                    // TODO: historical datasource connection info (temporary handling, can be removed later)
                    DbQueryProperty property = new DbQueryProperty(
                            ds.getDatasourceType(),
                            ds.getIp(),
                            ds.getPort(),
                            ds.getDatasourceConfig());
                    String key = property.trainToJdbcUrl();

                    // Check if already exists
                    Boolean exists = redisService.hashHasKey("datasource-old", key);
                    if (Boolean.FALSE.equals(exists)) {
                        redisService.hashPut("datasource-old", key, ds.getId().toString());
                        log.info("Stored new historical datasource: key={}, value={}", key, ds.getId().toString());
                    } else {
                        log.info("Historical datasource already exists: key={}, skipping insert", key);
                    }
                } catch (Exception e) {
                    log.warn("Unsupported datasource conversion");
                }
            }

            log.info("[Datasource Cache Init] Successfully loaded {} datasources into Redis.", list.size());
        } catch (Exception e) {
            log.error("[Datasource Cache Init] Failed to load Redis cache:", e);
        }
    }

    /**
     * Query datasource connection info for a data asset
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
        normalizeDatasource(createReqVO);
        validateDatasourceConfig(createReqVO);
        checkDuplicateDatasource(createReqVO);
        if (!Boolean.TRUE.equals(createReqVO.getSkipConnectionValidation())) {
            ensureEnabledDatasourceConnectable(createReqVO);
        }

        DaDatasourceDO dictType = BeanUtils.toBean(createReqVO, DaDatasourceDO.class);
        daDatasourceMapper.insert(dictType);
        delAndSaveDaDataSourceProject(dictType);

        redisService.hashPut("datasource", dictType.getId().toString(), com.alibaba.fastjson2.JSONObject.toJSONString(this.getDaDatasourceById(dictType.getId()).simplify()));

        return dictType.getId();
    }

    @Override
    public int updateDaDatasource(DaDatasourceSaveReqVO updateReqVO) {
        normalizeDatasource(updateReqVO);
        validateDatasourceConfig(updateReqVO);
        checkDuplicateDatasource(updateReqVO);
        ensureEnabledDatasourceConnectable(updateReqVO);

        Long datasourceId = updateReqVO.getId();

        // Update datasource
        DaDatasourceDO updateObj = BeanUtils.toBean(updateReqVO, DaDatasourceDO.class);
        delAndSaveDaDataSourceProject(updateObj);

        int i = daDatasourceMapper.updateById(updateObj);
        redisService.hashPut("datasource", datasourceId.toString(), com.alibaba.fastjson2.JSONObject.toJSONString(this.getDaDatasourceById(datasourceId).simplify()));
        return i;
    }

    @Override
    public boolean testDatasourceConnection(DaDatasourceSaveReqVO datasource) {
        try {
            normalizeDatasource(datasource);
            validateDatasourceConfig(datasource);
            DbQueryProperty property = new DbQueryProperty(
                    datasource.getDatasourceType(),
                    datasource.getIp(),
                    datasource.getPort(),
                    datasource.getDatasourceConfig()
            );
            return clientTest(property);
        } catch (Exception exception) {
            log.warn("Datasource connection validation failed before creation", exception);
            return false;
        }
    }

    private void normalizeDatasource(DaDatasourceSaveReqVO datasource) {
        datasource.setDatasourceName(StrUtil.trim(datasource.getDatasourceName()));
        datasource.setDatasourceType(StrUtil.trim(datasource.getDatasourceType()));
        datasource.setIp(StrUtil.trim(datasource.getIp()));

        if (StrUtil.isBlank(datasource.getDatasourceConfig()) || !JSONUtil.isJsonObj(datasource.getDatasourceConfig())) {
            return;
        }
        JSONObject config = JSONUtil.parseObj(datasource.getDatasourceConfig());
        trimConfigValue(config, "username");
        trimConfigValue(config, "password");
        trimConfigValue(config, "dbname");
        trimConfigValue(config, "sid");
        datasource.setDatasourceConfig(config.toString());
    }

    private void trimConfigValue(JSONObject config, String key) {
        Object value = config.get(key);
        if (value instanceof String) {
            config.set(key, StrUtil.trim((String) value));
        }
    }

    private void validateDatasourceConfig(DaDatasourceSaveReqVO datasource) {
        if (StrUtil.isBlank(datasource.getDatasourceConfig()) || !JSONUtil.isJsonObj(datasource.getDatasourceConfig())) {
            throw new ServiceException("数据源配置格式不正确");
        }
        JSONObject config = JSONUtil.parseObj(datasource.getDatasourceConfig());
        String type = datasource.getDatasourceType();
        if (!"OSS-ALIYUN".equals(type) && !"Kafka".equals(type) && !"HDFS".equals(type)
                && StrUtil.isBlank(config.getStr("username"))) {
            throw new ServiceException("账号不能为空或仅包含空格");
        }
    }

    private void checkDuplicateDatasource(DaDatasourceSaveReqVO datasource) {
        LambdaQueryWrapper<DaDatasourceDO> nameWrapper = new LambdaQueryWrapper<>();
        nameWrapper.eq(DaDatasourceDO::getDatasourceName, datasource.getDatasourceName());
        excludeCurrentDatasource(nameWrapper, datasource.getId());
        if (this.count(nameWrapper) > 0) {
            throw new ServiceException("已存在同名数据源，请修改数据源名称");
        }

        LambdaQueryWrapper<DaDatasourceDO> connectionWrapper = new LambdaQueryWrapper<>();
        connectionWrapper.eq(DaDatasourceDO::getDatasourceType, datasource.getDatasourceType())
                .eq(DaDatasourceDO::getIp, datasource.getIp())
                .eq(DaDatasourceDO::getPort, datasource.getPort())
                .select(DaDatasourceDO::getId, DaDatasourceDO::getDatasourceConfig);
        excludeCurrentDatasource(connectionWrapper, datasource.getId());

        JSONObject targetConfig = JSONUtil.parseObj(datasource.getDatasourceConfig());
        String targetDbName = StrUtil.nullToEmpty(targetConfig.getStr("dbname"));
        String targetUsername = StrUtil.nullToEmpty(targetConfig.getStr("username"));
        for (DaDatasourceDO existing : this.list(connectionWrapper)) {
            if (StrUtil.isBlank(existing.getDatasourceConfig()) || !JSONUtil.isJsonObj(existing.getDatasourceConfig())) {
                continue;
            }
            JSONObject existingConfig = JSONUtil.parseObj(existing.getDatasourceConfig());
            if (targetDbName.equals(StrUtil.nullToEmpty(existingConfig.getStr("dbname")))
                    && targetUsername.equals(StrUtil.nullToEmpty(existingConfig.getStr("username")))) {
                throw new ServiceException("已存在相同连接信息的数据源，请勿重复创建");
            }
        }
    }

    private void excludeCurrentDatasource(LambdaQueryWrapper<DaDatasourceDO> wrapper, Long datasourceId) {
        if (datasourceId != null) {
            wrapper.ne(DaDatasourceDO::getId, datasourceId);
        }
    }

    private void ensureEnabledDatasourceConnectable(DaDatasourceSaveReqVO datasource) {
        if (!Boolean.TRUE.equals(datasource.getValidFlag())) {
            return;
        }
        DbQueryProperty property = new DbQueryProperty(
                datasource.getDatasourceType(), datasource.getIp(), datasource.getPort(), datasource.getDatasourceConfig());
        if (!clientTest(property)) {
            throw new ServiceException("当前数据源未测试通过，不能启用。");
        }
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
        // Batch delete datasources
        return daDatasourceMapper.deleteBatchIds(idList);
    }

    @Override
    public int removeDaDatasourceDppOrDa(List<Long> idList, Long type) {
        int datasource = dppEtlTaskService.checkTaskIdInDatasource(idList, null);
        if (datasource > 0) {
            throw new ServiceException("da.error.delete.datasource.ref", "删除失败,数据源被项目引用!");
        }
        if (!idList.isEmpty()) {
            QueryWrapper<DaDatasourceProjectRelDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("DATASOURCE_ID", idList);
            daDatasourceProjectRelService.remove(queryWrapper);
        }
        // Batch delete datasources
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
                        // Preserve existing value
                        (existing, replacement) -> existing
                ));
    }


    /**
     * Import datasource data
     *
     * @param importExcelList list of datasource data
     * @param isUpdateSupport whether to update existing records
     * @param operName        operator name
     * @return result
     */
    @Override
    public String importDaDatasource(List<DaDatasourceRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "导入数据不能为空！");
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
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "数据更新成功，ID为 " + daDatasourceId + " 的数据源记录。", daDatasourceId, "数据源"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "数据更新失败，ID为 " + daDatasourceId + " 的数据源记录不存在。", daDatasourceId, "数据源"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DaDatasourceDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daDatasourceId);
                    DaDatasourceDO existingDaDatasource = daDatasourceMapper.selectOne(queryWrapper);
                    if (existingDaDatasource == null) {
                        daDatasourceMapper.insert(daDatasourceDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "数据插入成功，ID为 " + daDatasourceId + " 的数据源记录。", daDatasourceId, "数据源"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "数据插入失败，ID为 " + daDatasourceId + " 的数据源记录已存在。", daDatasourceId, "数据源"));
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


    @Override
    public AjaxResult clientsTest(Long id) {
        DbQuery dbQuery = this.buildDbQuery(id);
        if (dbQuery.valid()) {
            dbQuery.close();
            return AjaxResult.success(MessageUtils.messageWithFallback("da.error.connection.success", "数据库连接成功"));
        }
        dbQuery.close();
        return AjaxResult.error(MessageUtils.messageWithFallback("da.error.connection.fail", "数据库连接失败"));

    }

    @Override
    public Boolean clientTest(Long id) {
        DbQuery dbQuery = null;
        try {
            dbQuery = this.buildDbQuery(id);
            return dbQuery.valid();
        } catch (Exception exception) {
            log.warn("Datasource connection test failed, datasourceId={}", id, exception);
            return false;
        } finally {
            if (dbQuery != null) {
                dbQuery.close();
            }
        }
    }

    @Override
    public Boolean clientTest(DbQueryProperty dbQueryProperty) {
        DbQuery dbQuery = null;
        try {
            dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
            return dbQuery.valid();
        } catch (Exception exception) {
            log.warn("Datasource connection test failed", exception);
            return false;
        } finally {
            if (dbQuery != null) {
                dbQuery.close();
            }
        }
    }

    public DbQuery buildDbQuery(Long id) {
        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
        if (daDatasourceBy == null) {
            throw new DataQueryException("db.error.datasource.detail.fail", "数据源详情信息查询失败");
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
     * @param id datasource id
     * @return
     */
    @Override
    public List<DbTable> getDbTables(Long id) {
        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
        if (daDatasourceBy == null) {
            throw new DataQueryException("db.error.datasource.detail.fail", "数据源详情信息查询失败");
        }

        DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceBy.getDatasourceType()
                , daDatasourceBy.getIp(), daDatasourceBy.getPort(), daDatasourceBy.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            throw new DataQueryException("db.error.connection.fail", "数据库连接失败");
        }
        List<DbTable> tables = dbQuery.getTables(dbQueryProperty);
        dbQuery.close();
        return tables;
    }

    /**
     * Get the data fields inside a data table
     *
     * @param jsonObject datasource id and data table
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
        // Get the database type
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
     * Convert List<DpModelColumnReqDTO> to List<DaAssetColumnDO>
     *
     * @param columnsList list of DbColumn objects
     * @return converted DaAssetColumnDO list; returns an empty ArrayList if input is empty or null
     */
    public static List<DaAssetColumnDO> convertDbColumns(List<DbColumn> columnsList) {
        if (columnsList == null || columnsList.isEmpty()) {
            return new ArrayList<>();
        }
        List<DaAssetColumnDO> assetColumns = new ArrayList<>(columnsList.size());
        for (DbColumn dbColumn : columnsList) {
            // Use DpModelColumnReqDTO constructor to wrap DbColumn into a DTO object
            DpModelColumnReqDTO dto = new DpModelColumnReqDTO(dbColumn);
            // Use DTO data mapping to generate a DaAssetColumnDO object
            DaAssetColumnDO assetColumn = DaAssetColumnDO.builder()
                    // engName maps to field name
                    .columnName(dto.getEngName())
                    // cnName maps to field comment
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
            throw new DataQueryException("db.error.connection.fail", "数据库连接失败");
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
        // In the paging array: paging[0] is offset, paging[1] is pageSize
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
        // Remove the ROW_ID field from each record
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
            throw new ServiceException("da.error.sql.parse", "SQL语法有问题，解析出错");
        }

        // Query datasource info
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
     * Decrypt the incoming SQL statement
     */
    private String decryptSqlText(String encryptedSqlText) {
        String sqlText = "";
        try {
//            sqlText =  encryptedSqlText;
            sqlText = AesEncryptUtil.desEncrypt(encryptedSqlText).trim();
        } catch (Exception e) {
            throw new ServiceException("da.error.decrypt.fail", "执行语句解密异常，请联系管理员！");
        }

        if (sqlText == null || sqlText.isEmpty()) {
            throw new DataQueryException("db.error.sql.empty", "SQL语句不能为空");
        }

        // Check if the separator ';' is present
        int semicolonCount = sqlText.length() - sqlText.replace(";", "").length();
        if (semicolonCount > 0) {
            int firstIndex = sqlText.indexOf(";");
            int lastIndex = sqlText.lastIndexOf(";");
            // If ';' does not appear only at the end, consider it as multiple SQL statements
            if (firstIndex != lastIndex || lastIndex != sqlText.length() - 1) {
                throw new DataQueryException("db.error.sql.multi", "仅支持单个查询SQL语句，不允许存在多个语句");
            }
            // Remove the trailing ';'
            sqlText = sqlText.substring(0, sqlText.length() - 1).trim();
            if (sqlText.contains(";")) {
                throw new DataQueryException("db.error.sql.multi", "仅支持单个查询SQL语句，不允许存在多个语句");
            }
        }

        // Ensure the SQL starts with "select" (case-insensitive)
        if (!sqlText.toLowerCase().startsWith("select")) {
            throw new DataQueryException("db.error.sql.query.only", "仅允许执行查询操作的SQL语句");
        }

        // Further check for non-query SQL identifiers
        validateQueryOnly(sqlText);

        return sqlText;
    }


    /**
     * Check the SQL statement for non-query operation keywords.
     * To avoid false positives, strip string literals from the SQL first, then check via regex.
     */
    private void validateQueryOnly(String sqlText) {
        // Remove string constants from the SQL to avoid false positives caused by sensitive words in strings
        String withoutStringLiterals = sqlText.replaceAll("'[^']*'", "");
        // Define disallowed keywords (whole-word match, case-insensitive)
        String[] forbiddenKeywords = {"insert", "update", "delete", "create", "drop", "alter", "truncate", "exec", "execute", "merge"};
        String lowerSql = withoutStringLiterals.toLowerCase();

        for (String keyword : forbiddenKeywords) {
            // \b ensures keyword boundary matching, avoiding partial field name matches
            if (Pattern.compile("\\b" + keyword + "\\b").matcher(lowerSql).find()) {
                throw new DataQueryException("db.error.sql.keyword",
                    "SQL语句中包含非查询操作标识: " + keyword, keyword);
            }
        }
    }

    /**
     * Get a DbQuery object based on the datasource ID in the request
     */
    private DbQuery getDbQuery(DaDatasourcePageReqVO daDatasource) {
        DaDatasourceDO datasource = this.getDaDatasourceById(daDatasource.getId());
        if (datasource == null) {
            throw new DataQueryException("db.error.datasource.detail.fail", "数据源详情信息查询失败");
        }
        DbQueryProperty property = new DbQueryProperty(
                datasource.getDatasourceType(),
                datasource.getIp(),
                datasource.getPort(),
                datasource.getDatasourceConfig()
        );
        DbQuery dbQuery = dataSourceFactory.createDbQuery(property);
        if (!dbQuery.valid()) {
            throw new DataQueryException("db.error.connection.fail", "数据库连接失败");
        }
        return dbQuery;
    }

    /**
     * Get paging parameters: returns an int array where
     * paging[0] is offset,
     * paging[1] is pageSize,
     * paging[2] is pageNum (used for displaying page number during export)
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
            throw new ServiceException("da.error.form.notfound", "暂无表单信息");
        }

        // Get all column names from the first row as the order
        Map<String, Object> firstRow = dataList.get(0);
        // Use a Set to ensure column name uniqueness
        List<String> order = new ArrayList<>(firstRow.keySet());

        //1. Create workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Header field font
        XSSFFont headFont = workbook.createFont();
        // Font height
        headFont.setFontHeightInPoints((short) 24);
        // Font
        headFont.setFontName("宋体");
        headFont.setBold(true);
        // Set cell style
        XSSFCellStyle headCellStyle = workbook.createCellStyle();
        headCellStyle.setFont(headFont);
        // Horizontal alignment: center
        headCellStyle.setAlignment(HorizontalAlignment.CENTER);
        // Vertical alignment: center
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headCellStyle.setWrapText(true);

        // Annotation/markup font
        XSSFFont font = workbook.createFont();
        // Font height
        font.setFontHeightInPoints((short) 11);
        // Font
        font.setFontName("宋体");

        // Column style
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        // Horizontal alignment: center
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // Vertical alignment: center
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);

        // 2. Create worksheet
        XSSFSheet sheet = workbook.createSheet(tableName);

        // Freeze the first row
        sheet.createFreezePane(0, 1, 0, 1);

        // 3. Create header row
        XSSFRow row = sheet.createRow(0);
        row.setHeight((short) (2 * 200));

        int index = 0;
        for (String key : order) {
            // Set default width
            sheet.setColumnWidth(index, 25 * 256);
            XSSFCell labelCell = row.createCell(index);
            labelCell.setCellStyle(cellStyle);
            labelCell.setCellValue(key);
            index++;
        }

        // 4. Data rows
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, Object> map = dataList.get(i);
            // Data row
            XSSFRow dataRow = sheet.createRow(i + 1);
            dataRow.setHeight((short) (4 * 200));
            int columnIndex = 0;
            for (String key : order) {
                Object valueObj = map.get(key);
                String value = "";
                if (valueObj instanceof Date) {
                    // If it is a date type, convert to a fixed-format string
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
            // 5. Output stream - write to output
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
                throw new ServiceException("da.error.system", "系统错误");
            } finally {
                if (response.getOutputStream() != null) {
                    response.getOutputStream().flush();
                }
                // 6. Flush the stream and release resources
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
                // Close resources
                workbook.close();
            }
        }
    }


    @Override
    public Boolean editDatasourceStatus(Long datasourceId, Long status) {
        if (Objects.equals(status, 1L) && !clientTest(datasourceId)) {
            throw new ServiceException("当前数据源未测试通过，不能启用。");
        }
        return this.update(Wrappers.lambdaUpdate(DaDatasourceDO.class)
                .eq(DaDatasourceDO::getId, datasourceId)
                .set(DaDatasourceDO::getValidFlag, status));
    }

    /**
     * @param id        datasource id
     * @param tableName table name
     * @return
     */
    @Override
    public List<DbColumn> getDbTableColumns(Long id, String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new DataQueryException("db.error.table.empty", "表名不能为空");
        }

        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(id);
        if (daDatasourceBy == null) {
            throw new DataQueryException("db.error.datasource.detail.fail", "数据源详情信息查询失败");
        }

        DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceBy.getDatasourceType()
                , daDatasourceBy.getIp(), daDatasourceBy.getPort(), daDatasourceBy.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            throw new DataQueryException("db.error.connection.fail", "数据库连接失败");
        }
        List<DbColumn> tableColumns = dbQuery.getTableColumns(dbQueryProperty, tableName);
        dbQuery.close();

        return tableColumns;
    }

    @Override
    public DbTable getDbTable(Long datasourceId, String tableName) {
        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(datasourceId);
        if (daDatasourceBy == null) {
            throw new DataQueryException("db.error.datasource.detail.fail", "数据源详情信息查询失败");
        }

        DbQueryProperty dbQueryProperty = new DbQueryProperty(daDatasourceBy.getDatasourceType()
                , daDatasourceBy.getIp(), daDatasourceBy.getPort(), daDatasourceBy.getDatasourceConfig());
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);
        if (!dbQuery.valid()) {
            throw new DataQueryException("db.error.connection.fail", "数据库连接失败");
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
            throw new DataQueryException("db.error.datasource.detail.fail", "数据源详情信息查询失败");
        }

        DbQueryProperty baseProperty = new DbQueryProperty(
                datasource.getDatasourceType(),
                datasource.getIp(),
                datasource.getPort(),
                datasource.getDatasourceConfig()
        );

        // 1. First get the top-level databases
        List<DbName> dbNames;
        DbQuery rootQuery = dataSourceFactory.createDbQuery(baseProperty);
        try {
            if (!rootQuery.valid()) {
                throw new DataQueryException("db.error.connection.fail", "数据库连接失败");
            }
            dbNames = rootQuery.getDbNames(null);
        } finally {
            rootQuery.close();
        }

        if (CollectionUtils.isEmpty(dbNames)) {
            return dbNames;
        }

        // Single-level structure, return directly
        if (dbNames.get(0).getLevel() == 1 && dbNames.get(0).getTotalLevels() == 1) {
            return dbNames;
        }

        // 2. Iteratively load child databases
        for (DbName dbName : dbNames) {

            // Kingbase / PostgreSQL need to switch dbName
            DbQueryProperty childProperty = baseProperty;
            if (DbType.KINGBASE8.getDb().equals(baseProperty.getDbType())
                    || DbType.POSTGRE_SQL.getDb().equals(baseProperty.getDbType())) {

                childProperty = baseProperty.copy();
                childProperty.setDbName(dbName.getDbName());
            }

            DbQuery childQuery = dataSourceFactory.createDbQuery(childProperty);
            try {
                if (!childQuery.valid()) {
                    // Does not affect the whole, skip current node directly
                    continue;
                }
                List<DbName> children = childQuery.getDbNames(dbName);
                dbName.setChildren(children);
            } catch (Exception e) {
                // Optional: log the error
                // log.warn("Failed to get children for database {}", dbName.getDbName(), e);
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
            // 1: new, 2: modified, 3: deleted, 4: unchanged
            table.setChangeFlag("2");
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Updated table status to modified: " + matchedTable.getTableName());

            iDaDiscoveryTableService.updateDaDiscoveryTable(table);
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Update completed");
            return 1;
        } else {
            // 1: new, 2: modified, 3: deleted, 4: unchanged
            matchedTable.setChangeFlag("4");
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Updated table status to unchanged: " + matchedTable.getTableName());

            iDaDiscoveryTableService.updateDaDiscoveryTable(matchedTable);
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Update completed");
            return 0;
        }

    }
    private void saveNewTable(DaDiscoveryTableDO table, List<DbColumn> columns, DbQuery dbQuery, DbQueryProperty dbQueryProperty, Long daDiscoveryTaskLog) {
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Saving new table: " + table.getTableName());

        updateTableDataCount(dbQuery, table, columns.size());

        // 1: new, 2: modified, 3: deleted, 4: unchanged
        table.setChangeFlag("1");
        iDaDiscoveryTableService.createDaDiscoveryTable(table);

        if (CollUtil.isNotEmpty(columns)) {
            List<DaDiscoveryColumnDO> metadataColumnEntityList = columns.stream()
                    .map(column -> new DaDiscoveryColumnDO(table.getTaskId(), table.getId(), column))
                    .collect(Collectors.toList());
            metadataColumnEntityList.forEach(iDaDiscoveryColumnService::createDaDiscoveryColumn);
        }
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Save completed");
    }

    public DaDiscoveryTableDO findMatchedTable(DaDiscoveryTableDO table, List<DaDiscoveryTableDO> daDiscoveryTableDOList) {
        return daDiscoveryTableDOList.stream()
                .filter(existingTable -> existingTable.getTableName().equals(table.getTableName()) &&
                        existingTable.getTaskId().equals(table.getTaskId()))
                .findFirst()
                .orElse(null);  // Return null if no match found
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
                .orElse(null);  // Return null if no match found
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
                .collect(Collectors.toList()); // Return tables in daDiscoveryTableDOList that are not present in metadataTableEntityList
    }


    private int updateExistingTable(DbQuery dbQuery, DaDiscoveryTableDO matchedTable, DaDiscoveryTableDO table, List<DbColumn> columns, Long daDiscoveryTaskLog) {
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Updating table: " + table.getTableName());

        boolean modifiedTablesBoolean = false;
        // Query the snapshot field structure stored in the table
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
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Starting schema modification operation, task ID: " + daDiscoveryTaskById.getId());

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
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Fetching table column count info from datasource in real-time: " + totalTables);

            metadataTableEntityList = mapToMetadataTableList(tables, daDiscoveryTaskById.getId());
            if (CollUtil.isNotEmpty(metadataTableEntityList)) {
                for (DaDiscoveryTableDO table : metadataTableEntityList) {
                    DaDiscoveryTableDO matchedTable = findMatchedTable(table, daDiscoveryTableDOList);
                    iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Processing table: " + table.getTableName());

                    List<DbColumn> columns = dbQuery.getTableColumns(dbQueryProperty, table.getTableName());

                    columns = columns == null ? new ArrayList<>() : columns;
                    iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Fetching column count info from datasource in real-time: " + columns.size());
                    if (matchedTable == null) {
                        newTables++;
                        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "New table discovered, table: " + table.getTableName() + ", starting to save");
                        saveNewTable(table, columns, dbQuery, dbQueryProperty, daDiscoveryTaskLog);
                    } else {
                        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Checking table [" + table.getTableName() + "] configuration info in database");
                        // Whether to ignore; 0: no, 1: yes
                        String ignoreFlag = matchedTable.getIgnoreFlag();
                        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Checking table [" + table.getTableName() + "] configuration info in database, found ignoreFlag: " + ignoreFlag);
                        if (StringUtils.equals("1", ignoreFlag)) {
                            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Checking table [" + table.getTableName() + "] configuration info in database, found config: ignored. Scanning of this table ends!");
                            continue;
                        }
                        table.setId(matchedTable.getId());
                        modifiedTables += updateExistingTable(dbQuery, matchedTable, table, columns, daDiscoveryTaskLog);
                        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Existing table updated, table: " + table.getTableName());

                    }
                }
            }
        }

        deletedTables = deleteUnmatchedTables(daDiscoveryTableDOList, metadataTableEntityList, daDiscoveryTaskLog);
        String executionTime = DateUtils.getExecutionTime();

        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Schema modification operation completed, total tables: " + totalTables + ". Among them, new tables: " + newTables + ", modified tables: " + modifiedTables + ", deleted tables: " + deletedTables);

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
                .collect(Collectors.toList()); // Return tables in daDiscoveryTableDOList that are not present in metadataTableEntityList
    }

    private int deleteUnmatchedTables(List<DaDiscoveryTableDO> daDiscoveryTableDOList, List<DaDiscoveryTableDO> metadataTableEntityList, Long daDiscoveryTaskLog) {
        List<DaDiscoveryTableDO> notInMetadataTable = findNotInMetadataTable(daDiscoveryTableDOList, metadataTableEntityList);
        if (CollectionUtils.isEmpty(notInMetadataTable)) return 0;

        for (DaDiscoveryTableDO daDiscoveryTableDO : notInMetadataTable) {
            daDiscoveryTableDO.setUpdateBy("超级管理员");
            daDiscoveryTableDO.setUpdatorId(1L);
            // 1: new, 2: modified, 3: deleted, 4: unchanged
            daDiscoveryTableDO.setChangeFlag("3");
            iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Deleting unmatched table: " + daDiscoveryTableDO.getTableName());
            iDaDiscoveryTableService.updateDaDiscoveryTable(daDiscoveryTableDO);
        }

        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Deletion completed");
        return notInMetadataTable.size();
    }



    private Map<String, Object> runJobTableSchemaUpdates(DaDiscoveryTaskRespVO daDiscoveryTaskById, Long daDiscoveryTaskLog) {

        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Task execution - getting datasource details for discovery task by datasource ID");
        DaDatasourceDO daDatasourceBy = this.getDaDatasourceById(daDiscoveryTaskById.getDatasourceId());
        if (daDatasourceBy == null) {
            throw new DataQueryException("db.error.task.datasource.detail",
                    "Task execution - failed to get datasource details for discovery task by datasource ID!");
        }
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Task execution - successfully got datasource details for discovery task by datasource ID");
        iDaDiscoveryLogBodyService.taskLogAppend(daDiscoveryTaskLog, "Task execution - establishing real-time datasource connection by datasource connection info");
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
     * Detect table schema updates
     *
     * @param id
     */
    @Override
    public void detectTableSchemaUpdates(Long id) {
        String key = "detectTableSchemaUpdates-" + id;
        String status = redisService.get(key);
        if (StringUtils.isEmpty(status) && StringUtils.equals("1", status)) {
            throw new ServiceException("da.error.task.running", "历史任务未执行完毕，请稍后重试");
        }
        DaDiscoveryTaskRespVO daDiscoveryTaskById = iDaDiscoveryTaskService.getDaDiscoveryTaskById(id);
        if (daDiscoveryTaskById == null) {
            throw new DataQueryException("db.error.task.discovery.detail",
                    "任务执行-根据发现任务编号，获取发现任务详细信息失败!");
        }
        redisService.set(key, "1", 1200);
        // Create a log record table
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
