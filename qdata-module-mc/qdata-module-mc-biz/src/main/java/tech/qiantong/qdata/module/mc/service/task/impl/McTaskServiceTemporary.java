package tech.qiantong.qdata.module.mc.service.task.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Collection task Service business layer processing
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
// //external api
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
// throw new RuntimeException("Historical tasks have not been completed, please try again later");
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
// // Unify the prefix to avoid key conflicts with other modules
//        return "mc:taskTemporary:run:" + taskId;
//    }
//
//    private McTaskRespVO loadTask(Long taskId) {
//        McTaskRespVO task = this.getMcTaskByIdNew(taskId);
//        if (task == null) {
// throw new DataQueryException("The collection task does not exist, taskId=" + taskId);
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
// throw new DataQueryException("Data source ID is empty");
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
// throw new DataQueryException("Data source details query failed");
//        }
//        return datasource;
//    }
//
//    /**
// * Main process
//     *
//     * @param task
//     */
//    private TableProcessResult executeTaskSafely(McTaskRespVO task) {
//        McDatasourceDO datasource = prepareDatasource(task);
//        Long taskId = task.getId();
//
// // 1. According to the collection range, obtain the "library level" range
//        List<McTaskScopeDO> databaseScopes;
//        if ("2".equalsIgnoreCase(task.getCollectionScope())) {
// // Full volume: Load the database from the data source in real time
//            databaseScopes = loadDatabaseScopesFromDatasource(task, datasource);
//        } else {
// // Increment: directly use the collection range configured by the task
//            databaseScopes = loadDatabaseScopesFromTask(task);
//        }
//
//        if (CollectionUtils.isEmpty(databaseScopes)) {
//            return null;
//        }
//
// // 2. Library-level comparison (whether to add/change/delete)
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
// // 3. Loop through each library
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
// //No deletion currently
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
// // PG / Kingbase Cheku + schema
//        if (DbType.KINGBASE8.getDb().equals(property.getDbType())
//                || DbType.POSTGRE_SQL.getDb().equals(property.getDbType())) {
//            property.setDbName(dbScope.getDbName());
//            property.setSid(dbScope.getSchemaName());
//        }
//
//        DbQuery dbQuery = dataSourceFactory.createDbQuery(property);
//        if (!dbQuery.valid()) {
// throw new DataQueryException("Database connection failed");
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
// // 1. Table list (no more connections)
//        List<DbTable> tables = loadTablesByDatabase(dbQuery, task, dbScope);
//        if (CollectionUtils.isEmpty(tables)) {
//            return null;
//        }
//        int size = tables.size();
//        List<McTableRespDTO> tableRespDTOList = getMcTableById(task, dbScope);
//
// // 2. Table-level comparison
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
// // 3. Table loop
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
// //No deletion currently
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
// // 1️⃣ Table comments are inconsistent → Update
//        String reqComment = StringUtils.defaultString(reqTable.getTableComment());
//        String respComment = StringUtils.defaultString(respTable.getTableComment());
//        if (!reqComment.equals(respComment)) {
//            return true;
//        }
//
// // 2️⃣ The number of fields is inconsistent → Update
//        int reqSize = reqColumns == null ? 0 : reqColumns.size();
//        int respSize = respColumns == null ? 0 : respColumns.size();
//        if (reqSize != respSize) {
//            return true;
//        }
//
// // 3️⃣ Build a Map of respColumns (columnName is unique)
//        Map<String, McColumnRespDTO> respColumnMap = new HashMap<>();
//        if (respColumns != null) {
//            for (McColumnRespDTO respCol : respColumns) {
//                respColumnMap.put(respCol.getColumnName(), respCol);
//            }
//        }
//
// // 4️⃣ Loop reqColumns and judge field by field
//        if (reqColumns != null) {
//            for (McColumnReqDTO reqCol : reqColumns) {
//
//                McColumnRespDTO respCol =
//                        respColumnMap.get(reqCol.getColumnName());
//
// // Field does not exist → update
//                if (respCol == null) {
//                    return true;
//                }
//
// // Field attributes are inconsistent → Update
//                if (isColumnUpdated(reqCol, respCol)) {
//                    return true;
//                }
//            }
//        }
//
// // All consistent
//        return false;
//    }
//
//    private boolean isColumnUpdated(McColumnReqDTO req, McColumnRespDTO resp) {
//
// // String type: null == ""
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
// // Numeric type: direct Objects.equals
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
// // String type: null == ""
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
// // 1. Build DbQueryProperty
//        DbQueryProperty baseProperty = new DbQueryProperty(
//                datasource.getDatasourceType(),
//                datasource.getIp(),
//                datasource.getPort(),
//                datasource.getDatasourceConfig()
//        );
//
// // 2. Get the database list (including level)
//        List<DbName> dbNames;
//        DbQuery rootQuery = dataSourceFactory.createDbQuery(baseProperty);
//        try {
//            if (!rootQuery.valid()) {
// throw new DataQueryException("Database connection failed");
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
// // 3. Single-layer structure: direct mapping dbName
//        if (dbNames.get(0).getLevel() == 1 && dbNames.get(0).getTotalLevels() == 1) {
//            for (DbName dbName : dbNames) {
//                McTaskScopeDO scope = new McTaskScopeDO();
//                scope.setDbName(dbName.getDbName());
//                scopeList.add(scope);
//            }
//            return scopeList;
//        }
//
// // 4. Multi-layer structure: load subordinates and map db + schema
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
// //TODO logic needs to be improved
//        for (McTaskScopeDO databaseScope : databaseScopes) {
//
//            McDbSaveReqVO createReqVO = new McDbSaveReqVO();
// //Collect identification
//            createReqVO.setTaskId(task.getId());
//
// // ====== Business domain ======
//            createReqVO.setDomainId(task.getDomainId());
//            createReqVO.setDomainCode(task.getDomainCode());
//
// // ====== Basic information of data source ======
//            createReqVO.setDatasourceId(datasource.getId());
//            createReqVO.setDbType(datasource.getDatasourceType());
//            createReqVO.setIp(datasource.getIp());
//            createReqVO.setPort(datasource.getPort() != null
//                    ? datasource.getPort().intValue()
//                    : null);
//            createReqVO.setDatasourceConfig(datasource.getDatasourceConfig());
//
// // ====== Library / Pattern ======
//            createReqVO.setDbName(databaseScope.getDbName());
//            createReqVO.setSchemaName(databaseScope.getSchemaName());
//
// // ====== Description ======
//            createReqVO.setDescription(databaseScope.getDescription());
//
//            createReqVO.setCreateBy("System Collection Task");
//            createReqVO.setCreatorId(1L);
//
// // ====== Status and flag bits (the backend can provide the details in a unified manner, and they are given explicitly here) ======
// createReqVO.setStatus("0"); // Not released
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
// // ====== Association ======
//            mcTableReqDTO.setDataType("1");
//            mcTableReqDTO.setTaskId(task.getId());
//            mcTableReqDTO.setDbId(dbScope.getId());
//            mcTableReqDTO.setDatasourceId(task.getDatasourceId());
//
// // ====== Table basic information ======
//            mcTableReqDTO.setTableName(table.getTableName());
//            mcTableReqDTO.setTableComment(StringUtils.isEmpty(table.getTableComment()) ? "" : table.getTableComment());
//
// // ====== Library / Pattern ======
//            mcTableReqDTO.setDbName(dbScope.getDbName());
//            mcTableReqDTO.setSchemaName(dbScope.getSchemaName());
//
//            mcTableReqDTO.setCreateBy("System Collection Task");
//            mcTableReqDTO.setCreatorId(1L);
//
// // ====== Status and flags ======
// mcTableReqDTO.setStatus("0"); // Not released
//            mcTableReqDTO.setVersion(1);
//            mcTableReqDTO.setMasterFlag("1");
//            mcTableReqDTO.setTempFlag("0");
//            mcTableReqDTO.setAuditStatus("2");
//            mcTableReqDTO.setAuditTime(new Date());
//            mcTableReqDTO.setValidFlag(true);
//            mcTableReqDTO.setDelFlag(false);
//
// // ====== Description ======
//            mcTableReqDTO.setDescription(table.getTableComment());
//
// // ====== Call metadata service ======
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
// // ====== Related information ======
//            createReqVO.setTaskId(task.getId());
//            createReqVO.setDbId(dbScope.getId());
//            createReqVO.setTableId(table.getId());
//            createReqVO.setDatasourceId(task.getDatasourceId());
//
// // ====== Field basic information ======
//            createReqVO.setColumnName(column.getColName());
//            createReqVO.setColumnComment(StringUtils.isEmpty(column.getColName()) ? "" : column.getColName());
//            createReqVO.setColumnType(column.getDataType());
//
// // ====== Length / Precision ======
//            createReqVO.setColumnLength(parseInt(column.getDataLength()));
//            createReqVO.setColumnPrecision(parseInt(column.getDataPrecision()));
//            createReqVO.setColumnScale(parseInt(column.getDataScale()));
//
// // ====== Default value ======
//            createReqVO.setDefaultValue(column.getDataDefault());
//
// // ====== Primary key / nullable ======
//            createReqVO.setPkFlag(Boolean.TRUE.equals(column.getColKey()) ? "1" : "0");
//            createReqVO.setNullableFlag(Boolean.FALSE.equals(column.getNullable()) ? "1" : "0");
//            createReqVO.setFkFlag("0");
//
//            createReqVO.setCreateBy("System Collection Task");
//            createReqVO.setCreatorId(1L);
//
// // ====== Status and flags ======
// createReqVO.setStatus("0"); // Not released
//            createReqVO.setValidFlag(true);
//            createReqVO.setDelFlag(false);
//            createReqVO.setVersion(1);
//            createReqVO.setDataType("1");
//            createReqVO.setAuditStatus("2");
//            createReqVO.setAuditTime(new Date());
//
// // ====== Description ======
//            createReqVO.setDescription(column.getColComment());
//
//
//            columnReqDTOS.add(createReqVO);
// // ====== Call field metadata service ======
//
// // If you need to write back columnId, you can extend the field in DbColumn
//        }
//        return columnReqDTOS;
//    }
//
//    /**
// * Safe String -> Integer conversion
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


