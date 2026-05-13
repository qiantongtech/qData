package tech.qiantong.qdata.module.mc.service.task.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 采集任务Service业务层处理
 *
 * @author qdata
 * @date 2025-12-16
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class McTaskServiceTemporary {
//    @Resource
//    private McTaskMapper mcTaskMapper;
//
//    @Resource
//    private IMcTaskSchedulerService mcTaskSchedulerService;
//    @Resource
//    private IMcTaskScopeService mcTaskScopeService;
//    @Resource
//    @Lazy
//    private IMcDatasourceService mcDatasourceService;
//    @Resource
//    private IMcTaskInstanceService mcTaskInstanceService;
//    @Resource
//    private IMcTaskInstanceLogService mcTaskInstanceLogService;
//
//    @Autowired
//    @Lazy
//    private DataSourceFactory dataSourceFactory;
//
//    @Resource
//    @Lazy
//    private IRedisService redisService;
//
//    //外部api
//    @Resource
//    @Lazy
//    private IMcDbService mcDbApiService;
//    @Resource
//    private IMcTableService mcTableApiService;
//    @Resource
//    private IMcColumnService mcColumnApiService;
//
//    @Resource
//    private McDbTxService mcDbTxService;
//    @Resource
//    private McTableTxService mcTableTxService;
//
//
//    /**
//     * @param taskId
//     * @return
//     */
//    public boolean runDaDiscoveryTask(Long taskId) {
//
//        String redisKey = buildRunLockKey(taskId);
//        if (!acquireRunLock(redisKey)) {
//            throw new RuntimeException("历史任务未执行完毕，请稍后重试");
//        }
//        McTaskRespVO task = loadTask(taskId);
//        try {
//            TableProcessResult tableProcessResult = executeTaskSafely(task);
//            return true;
//        } catch (Exception e) {
//            return false;
//        } finally {
//            finalizeTask(redisKey);
//        }
//    }
//
//
//    public McTaskRespVO getMcTaskByIdNew(Long id) {
//        McTaskRespVO bean = BeanUtils.toBean(mcTaskMapper.selectById(id), McTaskRespVO.class);
//
//        McTaskSchedulerDO scheduler = mcTaskSchedulerService.getMcTaskSchedulerBytaskId(id);
//        if (scheduler != null) {
//            bean.setCronExpression(scheduler.getCronExpression());
//            bean.setSchedulerStatus(scheduler.getStatus());
//        }
//
//        List<McTaskScopeDO> mcTaskScopeDOS = mcTaskScopeService.getMcTaskScopeListBytaskId(id);
//        bean.setScopeSaveReqVOS(mcTaskScopeDOS);
//
//        McDatasourceDO mcDatasourceById = mcDatasourceService.getMcDatasourceById(bean.getDatasourceId());
//        bean.setDatasourceDO(mcDatasourceById);
//
//        McTaskInstanceDO mcTaskInstanceByTaskId = mcTaskInstanceService.getMcTaskInstanceByTaskId(id);
//        if (mcTaskInstanceByTaskId != null) {
//            bean.setLastExecuteTime(
//                    DateUtil.format(mcTaskInstanceByTaskId.getCreateTime(), "yyyy-MM-dd HH:mm:ss")
//            );
//        }
//
//        return bean;
//    }
//
//    private String buildRunLockKey(Long taskId) {
//        // 统一前缀，避免与其它模块 key 冲突
//        return "mc:taskTemporary:run:" + taskId;
//    }
//
//    private McTaskRespVO loadTask(Long taskId) {
//        McTaskRespVO task = this.getMcTaskByIdNew(taskId);
//        if (task == null) {
//            throw new DataQueryException("采集任务不存在，taskId=" + taskId);
//        }
//        return task;
//    }
//
//    private boolean acquireRunLock(String redisKey) {
//        String status = redisService.get(redisKey);
//        if (StringUtils.isNotBlank(status) && "1".equals(status)) {
//            return false;
//        }
//        redisService.set(redisKey, "1", 60 * 60 * 12);
//        return true;
//    }
//
//
//    private void finalizeTask(String redisKey) {
//        redisService.set(redisKey, "2", 300);
//    }
//
//    private McDatasourceDO prepareDatasource(McTaskRespVO task) {
//        Long datasourceId = task.getDatasourceId();
//        if (datasourceId == null) {
//            throw new DataQueryException("数据源ID为空");
//        }
//
//        McDatasourceDO datasource;
//        try {
//            datasource = mcDatasourceService.getMcDatasourceById(datasourceId);
//        } catch (Exception e) {
//            throw e;
//        }
//
//        if (datasource == null) {
//            throw new DataQueryException("数据源详情信息查询失败");
//        }
//        return datasource;
//    }
//
//    /**
//     * 主流程
//     *
//     * @param task
//     */
//    private TableProcessResult executeTaskSafely(McTaskRespVO task) {
//        McDatasourceDO datasource = prepareDatasource(task);
//        Long taskId = task.getId();
//
//        // 1. 根据采集范围，获取“库级”范围
//        List<McTaskScopeDO> databaseScopes;
//        if ("2".equalsIgnoreCase(task.getCollectionScope())) {
//            // 全量：实时从数据源加载数据库
//            databaseScopes = loadDatabaseScopesFromDatasource(task, datasource);
//        } else {
//            // 增量：直接使用任务配置的采集范围
//            databaseScopes = loadDatabaseScopesFromTask(task);
//        }
//
//        if (CollectionUtils.isEmpty(databaseScopes)) {
//            return null;
//        }
//
//        // 2. 库级比对（是否新增 / 变更 / 删除）
//        List<McDbSaveReqVO> dbReqDTOList = compareAndRecordDatabaseScope(task, databaseScopes, datasource);
//
//        List<McDbSaveReqVO> mcDbByTaskId = mcDbApiService.getMcDbByTaskId(taskId, "1");
//
//
//        Long addCount = 0L;
//        Long delCount = 0L;
//        Long updateCount = 0L;
//
//        Long totalCount = 0L;
//        Long successCount = 0L;
//
//        int dbIndex = 1;
//        // 3. 循环每个库
//        for (McDbSaveReqVO dbScope : dbReqDTOList) {
//
//            McDbSaveReqVO matchedDb = findMatchedDb(dbScope, datasource, mcDbByTaskId);
//            if (matchedDb == null) {
//                Long mcDbId = mcDbTxService.createDbAndCommit(dbScope);
//                dbScope.setId(mcDbId);
//            } else {
//                dbScope.setId(matchedDb.getId());
//            }
//
//            DbQueryContext dbQuery = createDbQueryForScope(datasource, dbScope, task);
//            try {
//                TableProcessResult tableProcessResult = executeSingleDatabase(dbQuery, task, dbScope, datasource);
//
//                if (tableProcessResult != null) {
//                    addCount = addCount + tableProcessResult.getAddCount();
//                    updateCount = updateCount + tableProcessResult.getUpdateCount();
//                    delCount = delCount + tableProcessResult.getDelCount();
//                    totalCount = totalCount + tableProcessResult.getTotalCount();
//                    successCount = successCount + tableProcessResult.getSuccessCount();
//                }
//            } finally {
//                closeDbQuerySafely(dbQuery, task, dbScope);
//            }
//        }
//        //目前不做删除
////        List<McDbRespDTO> dbsOnlyInResp = findDbsOnlyInResp(databaseScopes, datasource, mcDbByTaskId);
////        if (CollectionUtils.isNotEmpty(dbsOnlyInResp)) {
////
////            List<Long> collect = mcDbByTaskId.stream().map(a -> a.getId())
////                    .collect(Collectors.toList());
////
////            List<McTableRespDTO> mcTableByDbId = mcTableApiService.getMcTableByDbId(collect, "1");
////            if (CollectionUtils.isNotEmpty(mcTableByDbId)) {
////                delCount = delCount + mcTableByDbId.size();
////            }
////
////            List<Long> tableIds = mcTableByDbId.stream().map(a -> a.getId())
////                    .collect(Collectors.toList());
////            mcTableApiService.removeApiMcTable(tableIds, "1");
////
////            mcDbApiService.removeApiMcDbById(collect);
////        }
//        return new TableProcessResult(addCount, delCount, updateCount, totalCount, successCount);
//
//    }
//
//    private List<McDbRespDTO> findDbsOnlyInResp(List<McTaskScopeDO> databaseScopes,
//                                                McDatasourceDO datasource,
//                                                List<McDbRespDTO> mcDbByTaskId) {
//
//        List<McDbRespDTO> result = new ArrayList<>();
//        if (CollectionUtils.isEmpty(mcDbByTaskId)) {
//            return result;
//        }
//
//        for (McDbRespDTO resp : mcDbByTaskId) {
//            boolean exists = false;
//
//            if (CollectionUtils.isNotEmpty(databaseScopes)) {
//                for (McTaskScopeDO scope : databaseScopes) {
//                    if (Objects.equals(resp.getIp(), datasource.getIp())
//                            && Objects.equals(resp.getPort(), datasource.getPort() == null ? null : datasource.getPort().intValue())
//                            && Objects.equals(resp.getDatasourceConfig(), datasource.getDatasourceConfig())
//                            && Objects.equals(resp.getDbType(), datasource.getDatasourceType())
//                            && Objects.equals(resp.getDbName(), scope.getDbName())
//                            && Objects.equals(resp.getSchemaName(), scope.getSchemaName())) {
//                        exists = true;
//                        break;
//                    }
//                }
//            }
//
//            if (!exists) {
//                result.add(resp);
//            }
//        }
//        return result;
//    }
//
//    private McDbSaveReqVO findMatchedDb(McDbSaveReqVO dbScope,
//                                      McDatasourceDO datasource,
//                                      List<McDbSaveReqVO> mcDbByTaskId) {
//
//        if (dbScope == null || CollectionUtils.isEmpty(mcDbByTaskId)) {
//            return null;
//        }
//
//        for (McDbSaveReqVO resp : mcDbByTaskId) {
//            if (Objects.equals(resp.getIp(), datasource.getIp())
//                    && Objects.equals(resp.getPort(), datasource.getPort() == null ? null : datasource.getPort().intValue())
//                    && Objects.equals(resp.getDatasourceConfig(), datasource.getDatasourceConfig())
//                    && Objects.equals(resp.getDbType(), datasource.getDatasourceType())
//                    && Objects.equals(resp.getDbName(), dbScope.getDbName())
//                    && Objects.equals(resp.getSchemaName(), dbScope.getSchemaName())) {
//                return resp;
//            }
//        }
//        return null;
//    }
//
//    private DbQueryContext createDbQueryForScope(McDatasourceDO datasource,
//                                                 McDbReqDTO dbScope,
//                                                 McTaskRespVO task) {
//        DbQueryProperty property = new DbQueryProperty(
//                datasource.getDatasourceType(),
//                datasource.getIp(),
//                datasource.getPort(),
//                datasource.getDatasourceConfig()
//        );
//
//        // PG / Kingbase 切库 + schema
//        if (DbType.KINGBASE8.getDb().equals(property.getDbType())
//                || DbType.POSTGRE_SQL.getDb().equals(property.getDbType())) {
//            property.setDbName(dbScope.getDbName());
//            property.setSid(dbScope.getSchemaName());
//        }
//
//        DbQuery dbQuery = dataSourceFactory.createDbQuery(property);
//        if (!dbQuery.valid()) {
//            throw new DataQueryException("数据库连接失败");
//        }
//
//        property.setDbName(dbScope.getDbName());
//        property.setSid(dbScope.getSchemaName());
//        return new DbQueryContext(dbQuery, property);
//    }
//
//    private TableProcessResult executeSingleDatabase(DbQueryContext dbQuery,
//                                                     McTaskRespVO task,
//                                                     McDbReqDTO dbScope,
//                                                     McDatasourceDO datasource) {
//
//        Long taskId = task.getId();
//        // 1. 表列表（不再建连接）
//        List<DbTable> tables = loadTablesByDatabase(dbQuery, task, dbScope);
//        if (CollectionUtils.isEmpty(tables)) {
//            return null;
//        }
//        int size = tables.size();
//        List<McTableRespDTO> tableRespDTOList = getMcTableById(task, dbScope);
//
//        // 2. 表级比对
//        List<McTableReqDTO> mcTables = compareAndRecordTables(task, dbScope, tables);
//
//        List<DbColumn> columns =
//                loadColumnsByTable(dbQuery, task, dbScope);
//
//        Map<String, List<DbColumn>> tableColumnMap =
//                columns.stream()
//                        .collect(Collectors.groupingBy(DbColumn::getTableName));
//
//        List<McColumnReqDTO> mcColumnReqDTOList = new ArrayList<>();
//        // 3. 表循环
//        for (McTableReqDTO table : mcTables) {
//            List<DbColumn> dbColumns = tableColumnMap.get(table.getTableName());
//            if (CollectionUtils.isEmpty(dbColumns)) {
//                continue;
//            }
//
//            try {
//
//                TableProcessResult result =
//                        mcTableTxService.runInNewTx(() ->
//                                doProcessSingleTable(task, dbScope, table, tableRespDTOList, dbColumns)
//                        );
//
////                TableProcessResult result = doProcessSingleTable(task, instance, dbScope, table, tableRespDTOList, dbColumns);
//                //
//                if (result != null) {
//                    mcColumnReqDTOList.addAll(result.getMcColumnReqDTOList());
//                }
//
//            } catch (Exception e) {
//
//            }
//        }
//        if (CollectionUtils.isNotEmpty(mcColumnReqDTOList)) {
//            mcColumnApiService.createMcColumnList(mcColumnReqDTOList);
//        }
//
//        //目前不做删除
////        List<McTableRespDTO> tablesOnlyInResp = findTablesOnlyInResp(mcTables, tableRespDTOList);
////        if (CollectionUtils.isNotEmpty(tablesOnlyInResp)) {
////
////            List<Long> collect = tablesOnlyInResp.stream().map(a -> a.getId())
////                    .collect(Collectors.toList());
////            mcTableApiService.removeApiMcTable(collect, "1");
////        }
//
//
//        return new TableProcessResult(0L, 0L, 0L, 0L, 0L);
//    }
//
//    private TableProcessResult doProcessSingleTable(McTaskRespVO task,
//                                                    McDbReqDTO dbScope,
//                                                    McTableReqDTO table,
//                                                    List<McTableRespDTO> tableRespDTOList,
//                                                    List<DbColumn> columns) {
//        if (CollectionUtils.isEmpty(columns)) {
//            return null;
//        }
//
//        List<McColumnReqDTO> columnReqDTOS =
//                compareAndRecordColumns(task, dbScope, table, columns);
//
//
//        McTableRespDTO matched =
//                findMatchedTable(table, tableRespDTOList);
//
//        if (matched != null) {
//
//
//            table.setId(matched.getId());
//
//            List<McColumnRespDTO> mcColumnRespDTOList =
//                    getMcColumnByTaskId(table, dbScope);
//
//            boolean updated = isTableUpdated(
//                    table, matched, columnReqDTOS, mcColumnRespDTOList);
//
//            if (updated) {
//
//                mcTableApiService.updateMcTable(table);
//
//                removeMcColumn(table, dbScope);
//
//            } else {
//                return null;
//            }
//
//        } else {
//            Long mcTableId =
//                    mcTableApiService.createMcTable(table);
//
//            table.setId(mcTableId);
//        }
//
//        for (McColumnReqDTO columnReqDTO : columnReqDTOS) {
//            columnReqDTO.setTableId(table.getId());
//        }
//
//
//        return new TableProcessResult(0L, 0L, 0L, columnReqDTOS);
//    }
//
//
//    private void removeMcColumn(McTableReqDTO table, McDbReqDTO dbScope) {
//
//        McColumnReqDTO createReqVO = new McColumnReqDTO();
//        createReqVO.setTaskId(table.getTaskId());
//        createReqVO.setTableId(table.getId());
//        createReqVO.setDataType("1");
//        mcColumnApiService.removeMcColumn(createReqVO);
//    }
//
//    private boolean isTableUpdated(McTableReqDTO reqTable,
//                                   McTableRespDTO respTable,
//                                   List<McColumnReqDTO> reqColumns,
//                                   List<McColumnRespDTO> respColumns) {
//
//        // 1️⃣ 表注释不一致 → 更新
//        String reqComment = StringUtils.defaultString(reqTable.getTableComment());
//        String respComment = StringUtils.defaultString(respTable.getTableComment());
//        if (!reqComment.equals(respComment)) {
//            return true;
//        }
//
//        // 2️⃣ 字段数量不一致 → 更新
//        int reqSize = reqColumns == null ? 0 : reqColumns.size();
//        int respSize = respColumns == null ? 0 : respColumns.size();
//        if (reqSize != respSize) {
//            return true;
//        }
//
//        // 3️⃣ 构建 respColumns 的 Map（columnName 唯一）
//        Map<String, McColumnRespDTO> respColumnMap = new HashMap<>();
//        if (respColumns != null) {
//            for (McColumnRespDTO respCol : respColumns) {
//                respColumnMap.put(respCol.getColumnName(), respCol);
//            }
//        }
//
//        // 4️⃣ 循环 reqColumns，逐字段判断
//        if (reqColumns != null) {
//            for (McColumnReqDTO reqCol : reqColumns) {
//
//                McColumnRespDTO respCol =
//                        respColumnMap.get(reqCol.getColumnName());
//
//                // 字段不存在 → 更新
//                if (respCol == null) {
//                    return true;
//                }
//
//                // 字段属性不一致 → 更新
//                if (isColumnUpdated(reqCol, respCol)) {
//                    return true;
//                }
//            }
//        }
//
//        // 全部一致
//        return false;
//    }
//
//    private boolean isColumnUpdated(McColumnReqDTO req, McColumnRespDTO resp) {
//
//        // String 类型：null == ""
//        if (!StringUtils.defaultString(req.getColumnComment())
//                .equals(StringUtils.defaultString(resp.getColumnComment()))) {
//            return true;
//        }
//
//        if (!StringUtils.defaultString(req.getColumnType())
//                .equals(StringUtils.defaultString(resp.getColumnType()))) {
//            return true;
//        }
//
//        // 数值类型：直接 Objects.equals
//        if (!Objects.equals(req.getColumnLength(), resp.getColumnLength())) {
//            return true;
//        }
//
//        if (!Objects.equals(req.getColumnPrecision(), resp.getColumnPrecision())) {
//            return true;
//        }
//
//        if (!Objects.equals(req.getColumnScale(), resp.getColumnScale())) {
//            return true;
//        }
//
//        // String 类型：null == ""
//        if (!StringUtils.defaultString(req.getDefaultValue())
//                .equals(StringUtils.defaultString(resp.getDefaultValue()))) {
//            return true;
//        }
//
//        if (!StringUtils.defaultString(req.getPkFlag())
//                .equals(StringUtils.defaultString(resp.getPkFlag()))) {
//            return true;
//        }
//
//        if (!StringUtils.defaultString(req.getFkFlag())
//                .equals(StringUtils.defaultString(resp.getFkFlag()))) {
//            return true;
//        }
//
//        if (!StringUtils.defaultString(req.getNullableFlag())
//                .equals(StringUtils.defaultString(resp.getNullableFlag()))) {
//            return true;
//        }
//
//        return false;
//    }
//
//    private List<McColumnRespDTO> getMcColumnByTaskId(McTableReqDTO table, McDbReqDTO dbScope) {
//        McColumnReqDTO createReqVO = new McColumnReqDTO();
//        createReqVO.setTaskId(table.getTaskId());
//        createReqVO.setTableId(table.getId());
//        createReqVO.setDataType("1");
//        return mcColumnApiService.getMcColumnByTaskId(createReqVO);
//    }
//
//    private List<McTableRespDTO> findTablesOnlyInResp(List<McTableReqDTO> mcTables,
//                                                      List<McTableRespDTO> tableRespDTOList) {
//
//        List<McTableRespDTO> result = new ArrayList<>();
//        if (CollectionUtils.isEmpty(tableRespDTOList)) {
//            return result;
//        }
//
//        for (McTableRespDTO resp : tableRespDTOList) {
//            boolean exists = false;
//            if (CollectionUtils.isNotEmpty(mcTables)) {
//                for (McTableReqDTO req : mcTables) {
//                    if (Objects.equals(req.getDbName(), resp.getDbName())
//                            && Objects.equals(req.getSchemaName(), resp.getSchemaName())
//                            && Objects.equals(req.getTableName(), resp.getTableName())) {
//                        exists = true;
//                        break;
//                    }
//                }
//            }
//            if (!exists) {
//                result.add(resp);
//            }
//        }
//        return result;
//    }
//
//
//    private McTableRespDTO findMatchedTable(McTableReqDTO req,
//                                            List<McTableRespDTO> tableRespDTOList) {
//        if (req == null || CollectionUtils.isEmpty(tableRespDTOList)) {
//            return null;
//        }
//
//        for (McTableRespDTO resp : tableRespDTOList) {
//            if (Objects.equals(req.getDbId(), resp.getDbId())
//                    && Objects.equals(req.getTableName(), resp.getTableName())) {
//                return resp;
//            }
//        }
//        return null;
//    }
//
//
//    private List<McTableRespDTO> getMcTableById(McTaskRespVO task, McDbReqDTO dbScope) {
//        McTableReqDTO mcTableReqDTO = new McTableReqDTO();
//        mcTableReqDTO.setTaskId(task.getId());
//        mcTableReqDTO.setDbId(dbScope.getId());
//        mcTableReqDTO.setDataType("1");
//        return mcTableApiService.getMcTableById(mcTableReqDTO);
//    }
//
//    private List<DbTable> loadTablesByDatabase(DbQueryContext dbQuery,
//                                               McTaskRespVO task,
//                                               McDbReqDTO dbScope) {
//
//        try {
//            List<DbTable> tables = dbQuery.getDbQuery().getTables(dbQuery.getProperty());
//            return tables == null ? new ArrayList<>() : tables;
//        } catch (Exception e) {
//            return new ArrayList<>();
//        }
//    }
//
//    private List<DbColumn> loadColumnsByTable(DbQueryContext dbQuery,
//                                              McTaskRespVO task,
//                                              McDbReqDTO dbScope) {
//
//        try {
//            List<DbColumn> tableColumns = dbQuery.getDbQuery().getDbColumns(dbQuery.getProperty());
//            return tableColumns == null ? new ArrayList<>() : tableColumns;
//        } catch (Exception e) {
//            return new ArrayList<>();
//        }
//    }
//
//    private void closeDbQuerySafely(DbQueryContext ctx,
//                                    McTaskRespVO task,
//                                    McDbReqDTO dbScope) {
//        try {
//            ctx.getDbQuery().close();
//        } catch (Exception e) {
//        }
//    }
//
//
//    private List<McTaskScopeDO> loadDatabaseScopesFromDatasource(McTaskRespVO task,
//                                                                 McDatasourceDO datasource) {
//        // 1. 构建 DbQueryProperty
//        DbQueryProperty baseProperty = new DbQueryProperty(
//                datasource.getDatasourceType(),
//                datasource.getIp(),
//                datasource.getPort(),
//                datasource.getDatasourceConfig()
//        );
//
//        // 2. 获取数据库列表（含层级）
//        List<DbName> dbNames;
//        DbQuery rootQuery = dataSourceFactory.createDbQuery(baseProperty);
//        try {
//            if (!rootQuery.valid()) {
//                throw new DataQueryException("数据库连接失败");
//            }
//            dbNames = rootQuery.getDbNames(null);
//        } finally {
//            rootQuery.close();
//        }
//
//        List<McTaskScopeDO> scopeList = new ArrayList<>();
//        if (CollectionUtils.isEmpty(dbNames)) {
//            return scopeList;
//        }
//
//        // 3. 单层结构：直接映射 dbName
//        if (dbNames.get(0).getLevel() == 1 && dbNames.get(0).getTotalLevels() == 1) {
//            for (DbName dbName : dbNames) {
//                McTaskScopeDO scope = new McTaskScopeDO();
//                scope.setDbName(dbName.getDbName());
//                scopeList.add(scope);
//            }
//            return scopeList;
//        }
//
//        // 4. 多层结构：加载下级并映射 db + schema
//        for (DbName dbName : dbNames) {
//
//            DbQueryProperty childProperty = baseProperty;
//            if (DbType.KINGBASE8.getDb().equals(baseProperty.getDbType())
//                    || DbType.POSTGRE_SQL.getDb().equals(baseProperty.getDbType())) {
//
//                childProperty = baseProperty.copy();
//                childProperty.setDbName(dbName.getDbName());
//            }
//
//            DbQuery childQuery = dataSourceFactory.createDbQuery(childProperty);
//            try {
//                if (!childQuery.valid()) {
//                    continue;
//                }
//                List<DbName> children = childQuery.getDbNames(dbName);
//                dbName.setChildren(children);
//            } finally {
//                childQuery.close();
//            }
//
//            List<DbName> children = dbName.getChildren();
//            if (CollectionUtils.isNotEmpty(children)) {
//                for (DbName child : children) {
//                    McTaskScopeDO scope = new McTaskScopeDO();
//                    scope.setDbName(dbName.getDbName());
//                    scope.setSchemaName(child.getDbName());
//                    scopeList.add(scope);
//                }
//            } else {
//                McTaskScopeDO scope = new McTaskScopeDO();
//                scope.setDbName(dbName.getDbName());
//                scopeList.add(scope);
//            }
//        }
//
//        return scopeList;
//    }
//
//
//    private List<McTaskScopeDO> loadDatabaseScopesFromTask(McTaskRespVO task) {
//        return task.getScopeSaveReqVOS();
//    }
//
//    private List<McDbSaveReqVO> compareAndRecordDatabaseScope(McTaskRespVO task, List<McTaskScopeDO> databaseScopes,
//                                                           McDatasourceDO datasource) {
//        List<McDbSaveReqVO> dbReqDTOList = new ArrayList<>();
//
//        //TODO 逻辑待完善
//        for (McTaskScopeDO databaseScope : databaseScopes) {
//
//            McDbSaveReqVO createReqVO = new McDbSaveReqVO();
//            //采集标识
//            createReqVO.setTaskId(task.getId());
//
//            // ====== 业务域 ======
//            createReqVO.setDomainId(task.getDomainId());
//            createReqVO.setDomainCode(task.getDomainCode());
//
//            // ====== 数据源基础信息 ======
//            createReqVO.setDatasourceId(datasource.getId());
//            createReqVO.setDbType(datasource.getDatasourceType());
//            createReqVO.setIp(datasource.getIp());
//            createReqVO.setPort(datasource.getPort() != null
//                    ? datasource.getPort().intValue()
//                    : null);
//            createReqVO.setDatasourceConfig(datasource.getDatasourceConfig());
//
//            // ====== 库 / 模式 ======
//            createReqVO.setDbName(databaseScope.getDbName());
//            createReqVO.setSchemaName(databaseScope.getSchemaName());
//
//            // ====== 描述 ======
//            createReqVO.setDescription(databaseScope.getDescription());
//
//            createReqVO.setCreateBy("System Collection Task");
//            createReqVO.setCreatorId(1L);
//
//            // ====== 状态与标志位（后端可统一兜底，这里显式给） ======
//            createReqVO.setStatus("0");      // 未发布
//            createReqVO.setAuditStatus("2");
//            createReqVO.setVersion(1);
//            createReqVO.setAuditTime(new Date());
//
//            dbReqDTOList.add(createReqVO);
//        }
//
//        return dbReqDTOList;
//    }
//
//    private List<McTableReqDTO> compareAndRecordTables(McTaskRespVO task,
//                                                       McDbReqDTO dbScope,
//                                                       List<DbTable> tables) {
//        List<McTableReqDTO> mcTableReqDTOList = new ArrayList<>();
//        for (DbTable table : tables) {
//
//            McTableReqDTO mcTableReqDTO = new McTableReqDTO();
//
//            // ====== 关联关系 ======
//            mcTableReqDTO.setDataType("1");
//            mcTableReqDTO.setTaskId(task.getId());
//            mcTableReqDTO.setDbId(dbScope.getId());
//            mcTableReqDTO.setDatasourceId(task.getDatasourceId());
//
//            // ====== 表基础信息 ======
//            mcTableReqDTO.setTableName(table.getTableName());
//            mcTableReqDTO.setTableComment(StringUtils.isEmpty(table.getTableComment()) ? "" : table.getTableComment());
//
//            // ====== 库 / 模式 ======
//            mcTableReqDTO.setDbName(dbScope.getDbName());
//            mcTableReqDTO.setSchemaName(dbScope.getSchemaName());
//
//            mcTableReqDTO.setCreateBy("System Collection Task");
//            mcTableReqDTO.setCreatorId(1L);
//
//            // ====== 状态与标志位 ======
//            mcTableReqDTO.setStatus("0");     // 未发布
//            mcTableReqDTO.setVersion(1);
//            mcTableReqDTO.setMasterFlag("1");
//            mcTableReqDTO.setTempFlag("0");
//            mcTableReqDTO.setAuditStatus("2");
//            mcTableReqDTO.setAuditTime(new Date());
//            mcTableReqDTO.setValidFlag(true);
//            mcTableReqDTO.setDelFlag(false);
//
//            // ====== 描述 ======
//            mcTableReqDTO.setDescription(table.getTableComment());
//
//            // ====== 调用元数据服务 ======
////            Long mcTableId = mcTableApiService.createMcTable(mcTableReqDTO);
////
////            mcTableReqDTO.setId(mcTableId);
//            mcTableReqDTOList.add(mcTableReqDTO);
//        }
//
//        return mcTableReqDTOList;
//    }
//
//    private List<McColumnReqDTO> compareAndRecordColumns(McTaskRespVO task,
//                                                         McDbReqDTO dbScope,
//                                                         McTableReqDTO table,
//                                                         List<DbColumn> columns) {
//
//        List<McColumnReqDTO> columnReqDTOS = new ArrayList<>();
//        for (DbColumn column : columns) {
//
//            McColumnReqDTO createReqVO = new McColumnReqDTO();
//
//            // ====== 关联信息 ======
//            createReqVO.setTaskId(task.getId());
//            createReqVO.setDbId(dbScope.getId());
//            createReqVO.setTableId(table.getId());
//            createReqVO.setDatasourceId(task.getDatasourceId());
//
//            // ====== 字段基础信息 ======
//            createReqVO.setColumnName(column.getColName());
//            createReqVO.setColumnComment(StringUtils.isEmpty(column.getColName()) ? "" : column.getColName());
//            createReqVO.setColumnType(column.getDataType());
//
//            // ====== 长度 / 精度 ======
//            createReqVO.setColumnLength(parseInt(column.getDataLength()));
//            createReqVO.setColumnPrecision(parseInt(column.getDataPrecision()));
//            createReqVO.setColumnScale(parseInt(column.getDataScale()));
//
//            // ====== 默认值 ======
//            createReqVO.setDefaultValue(column.getDataDefault());
//
//            // ====== 主键 / 可空 ======
//            createReqVO.setPkFlag(Boolean.TRUE.equals(column.getColKey()) ? "1" : "0");
//            createReqVO.setNullableFlag(Boolean.FALSE.equals(column.getNullable()) ? "1" : "0");
//            createReqVO.setFkFlag("0");
//
//            createReqVO.setCreateBy("System Collection Task");
//            createReqVO.setCreatorId(1L);
//
//            // ====== 状态与标志位 ======
//            createReqVO.setStatus("0");     // 未发布
//            createReqVO.setValidFlag(true);
//            createReqVO.setDelFlag(false);
//            createReqVO.setVersion(1);
//            createReqVO.setDataType("1");
//            createReqVO.setAuditStatus("2");
//            createReqVO.setAuditTime(new Date());
//
//            // ====== 描述 ======
//            createReqVO.setDescription(column.getColComment());
//
//
//            columnReqDTOS.add(createReqVO);
//            // ====== 调用字段元数据服务 ======
//
//            // 如需回写 columnId，可在 DbColumn 中扩展字段
//        }
//        return columnReqDTOS;
//    }
//
//    /**
//     * 安全的 String -> Integer 转换
//     */
//    private Integer parseInt(String val) {
//        if (val == null || val.trim().isEmpty()) {
//            return null;
//        }
//        try {
//            return Integer.valueOf(val.trim());
//        } catch (NumberFormatException e) {
//            return null;
//        }
//    }
}


