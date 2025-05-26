package tech.qiantong.qdata.module.ds.service.api.impl;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.SelectUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.qiantong.qdata.common.database.DataSourceFactory;
import tech.qiantong.qdata.common.database.DbQuery;
import tech.qiantong.qdata.common.database.constants.DbQueryProperty;
import tech.qiantong.qdata.common.database.core.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.common.utils.PageUtil;
import tech.qiantong.qdata.module.da.api.datasource.dto.DaDatasourceRespDTO;
import tech.qiantong.qdata.module.da.api.service.asset.IDaDatasourceApiService;
import tech.qiantong.qdata.module.da.api.service.assetchild.api.IDaApiOutService;
import tech.qiantong.qdata.module.da.api.service.assetchild.gis.IDaAssetGisOutService;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.ExecuteConfig;
import tech.qiantong.qdata.module.ds.dal.dataobject.dto.ReqParam;
import tech.qiantong.qdata.module.ds.dal.dataobject.dto.ResParam;
import tech.qiantong.qdata.module.ds.utils.JsonUtil;
import tech.qiantong.qdata.module.ds.utils.SqlBuilderUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ApiMappingEngine {

    @Autowired
    private DataSourceFactory dataSourceFactory;

    @Resource
    private IDaDatasourceApiService iDaDatasourceApiService;

    @Resource
    private IDaApiOutService iDaApiOutService;
    @Resource
    private IDaAssetGisOutService iDaAssetGisOutService;


    public Object execute(DsApiDO dataApi, Map<String, Object> params) {
        DaDatasourceRespDTO dataSource;

        //返回结果类型 1:分页 2:列表 3:详情
        String resDataType = dataApi.getResDataType();
        //根据数据源id查询数据源信息
        String configJson = dataApi.getConfigJson();
        ExecuteConfig executeConfig = JSONObject.parseObject(configJson, ExecuteConfig.class);
        dataApi.setExecuteConfig(executeConfig);
        dataSource = iDaDatasourceApiService.getDatasourceById(Long.valueOf(executeConfig.getSourceId()));

        tech.qiantong.qdata.common.database.constants.DbQueryProperty dbQueryProperty = new DbQueryProperty(
                dataSource.getDatasourceType(),
                dataSource.getIp(),
                dataSource.getPort(),
                dataSource.getDatasourceConfig()
        );
        DbQuery dbQuery = dataSourceFactory.createDbQuery(dbQueryProperty);

        // 参数
        //Integer pageNum = Integer.parseInt((String) params.getOrDefault("pageNum", 1));
        //Integer pageSize = Integer.parseInt((String) params.getOrDefault("pageSize", 20));
        Integer pageNum = Integer.parseInt(MapUtils.getString(params, "pageNum", "1"));
        Integer pageSize = Integer.parseInt(MapUtils.getString(params, "pageSize", "20"));
        PageUtil pageUtil = new PageUtil(pageNum, pageSize);
        Integer offset = pageUtil.getOffset();

        if(tech.qiantong.qdata.common.utils.StringUtils.isEmpty(executeConfig.getSqlText())){
            try {
                String s = sqlJdbcNamedParameterBuild(dataApi);
                executeConfig.setSqlText(s);
            } catch (JSQLParserException e) {
                throw new RuntimeException(e);
            }
        }

        SqlBuilderUtil.SqlFilterResult sqlFilterResult;
        try {

            sqlFilterResult = SqlBuilderUtil.getInstance().applyFilters(dataApi.getExecuteConfig().getSqlText(), params);
        } catch (Exception e) {
            throw new ServiceException("API调用动态构造SQL语句出错");
        }
        Map<String, Object> acceptedFilters = sqlFilterResult.getAcceptedFilters();


        Object result = null;
        try {
//            Integer cacheSwitch = Integer.parseInt(dataApi.getCacheSwitch());
            Integer cacheSwitch = 0;
            switch (resDataType) {
                case "3":
                    PageResult<Map<String, Object>> pageResult = dbQuery.queryByPage(sqlFilterResult.getSql(), acceptedFilters, offset, pageSize, cacheSwitch);
                    List<Map<String, Object>> data = pageResult.getData();
                    List<Map<String, Object>> list = this.encryptQueryResultList(data, String.valueOf(dataApi.getId()));

                    pageResult.setPageNum(pageNum).setPageSize(pageSize).setData(list);
                    result = pageResult;
                    break;
                case "2":
                    List<Map<String, Object>> listResult = dbQuery.queryList(sqlFilterResult.getSql(), acceptedFilters, cacheSwitch);
                    result = this.encryptQueryResultList(listResult, String.valueOf(dataApi.getId()));
//                    result = listResult;
                    break;
                case "1":
                    Map<String, Object> mapResult = dbQuery.queryOne(sqlFilterResult.getSql(), acceptedFilters, cacheSwitch);
                    result = encryptQueryResultMap(mapResult, String.valueOf(dataApi.getId()));
//                    result = mapResult;
                    break;
            }
        } catch (Exception e) {
            throw new ServiceException("API调用查询结果集出错");
        }finally {
            dbQuery.close();
        }
        return result;
    }

    private String sqlJdbcNamedParameterBuild(DsApiDO dataApi) throws JSQLParserException {
        String tableName = dataApi.getExecuteConfig().getTableName();
        String resParams1 = dataApi.getResParams();
        String reqParams = dataApi.getReqParams();
        //转成
        List<ReqParam> reqParams1 = JSONArray.parseArray(reqParams, ReqParam.class);
        dataApi.setReqParamsList(reqParams1);
        dataApi.setResParamsList(JSONArray.parseArray(resParams1, ResParam.class));
        if (org.apache.commons.lang3.StringUtils.isNotBlank(dataApi.getExecuteConfig().getDbName())) {
            tableName = dataApi.getExecuteConfig().getDbName() + "." + tableName;
        }
        Table table = new Table(tableName);
        String[] resParams = dataApi.getResParamsList().stream().map(s -> s.getFieldName()).toArray(String[]::new);
        Select select = SelectUtils.buildSelectFromTableAndExpressions(table, resParams);
        return SqlBuilderUtil.getInstance().buildHql(select.toString(), dataApi.getReqParamsList());
    }

    private Map<String, Object> encryptQueryResultMap(Map<String, Object> mapResult, String apiId) {
        if (MapUtils.isEmpty(mapResult)) {
            return mapResult;
        }

//        List<MetadataDsnRuleLinkEntity> metadataDsnRuleLinkList = new ArrayList<>();
//        try {
//            metadataDsnRuleLinkList = metadataSourceServiceFeign.getMetadataDsnRuleLinkList(apiId);
//        } catch (Exception e) {
//            throw new ServiceException("API调用查询脱敏规则出错");
//        }
//
//        if (CollectionUtils.isEmpty(metadataDsnRuleLinkList)) {
//            return mapResult;
//        }
//
//        metadataDsnRuleLinkList.stream()
//                .filter(columnEntity -> mapResult.containsKey(columnEntity.getColumnName()))
//                .forEach(columnEntity -> {
//                    String columnName = columnEntity.getColumnName();
//                    Map.Entry<String, Object> item = getIgnoreCaseData(mapResult, columnName);
//
//                    if (item != null) {
//                        MaskRuleUtil.MaskRule maskRule = MaskRuleUtil.mapToMaskRule(columnEntity);
//                        Object object = MaskRuleUtil.processRule(item.getValue(), maskRule);
//                        mapResult.put(item.getKey(), object);
//                    }
//                });

        return mapResult;
    }

    /**
     * 数据脱敏
     *
     * @param data
     * @param apiId
     * @return
     */
    private List<Map<String, Object>> encryptQueryResultList(List<Map<String, Object>> data, String apiId) {
//        if (CollectionUtils.isEmpty(data)) {
//            return data;
//        }
//        List<MetadataDsnRuleLinkEntity> metadataDsnRuleLinkList = new ArrayList<>();
//        try {
//            metadataDsnRuleLinkList = metadataSourceServiceFeign.getMetadataDsnRuleLinkList(apiId);
//        } catch (Exception e) {
//            throw new ServiceException("API调用查询脱敏规则出错");
//        }
//
//        if (CollectionUtils.isEmpty(metadataDsnRuleLinkList)) {
//            return data;
//        }
//
//        for (Map<String, Object> datum : data) {
//            metadataDsnRuleLinkList.stream()
//                    .filter(columnEntity -> datum.containsKey(columnEntity.getColumnName()))
//                    .forEach(columnEntity -> {
//                        String columnName = columnEntity.getColumnName();
//                        Map.Entry<String, Object> item = getIgnoreCaseData(datum, columnName);
//
//                        if (item != null) {
//                            MaskRuleUtil.MaskRule maskRule = MaskRuleUtil.mapToMaskRule(columnEntity);
//                            Object object = MaskRuleUtil.processRule(item.getValue(), maskRule);
//                            datum.put(item.getKey(), object);
//                        }
//                    });
//        }
        return data;
    }


    public static <K, V> Map.Entry<K, V> getIgnoreCaseData(Map<K, V> map, K key) {
        if (map == null || key == null) {
            return null;
        }

        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getKey() != null && entry.getKey().toString().equalsIgnoreCase(key.toString())) {
                return entry;
            }
        }
        return null;
    }

    /**
     * 文件的访问（支持上传的文件和指定目录下的文件）
     *
     * @param api
     * @return
     */
    @SneakyThrows
    public void executeFileService(DsApiDO api, HttpServletResponse response) {
        //文件名称
//        String fileName = api.getFileName();
//        //文件路径或目录路径(目录时需将整个目录压缩返回)
//        String filePath = api.getFilePath();
//        // 设置响应内容类型
//        response.setContentType("application/octet-stream;charset=UTF-8");
//        File file = new File(filePath);
//        if (!file.exists()) {
//            throw new DataException("文件不存在！");
//        }
//        boolean delFlag = false;
//        if (file.isFile()) {
//            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//        } else if (file.isDirectory()) {
//            String zipFileName = UUID.fastUUID().toString() + ".zip";
//            response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + "\"");
//            String zipPath = file.getParentFile().getPath() + File.separator + zipFileName;
//            file = ZipUtil.zip(filePath, zipPath, true);
//            delFlag = true;
//        }
//        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
//        byte[] fileBytes = FileUtil.readBytes(file);
//        response.addHeader("Content-Length", "" + fileBytes.length);
//        IoUtil.write(response.getOutputStream(), true, fileBytes);
//        //删除文件
//        if (delFlag) {
//            FileUtil.del(file);
//        }
    }


    /**
     * 进行三方服务的转发
     *
     * @param api
     * @param params
     * @return
     */
    public void executeServiceForwarding(DsApiDO api, Map<String, Object> params, HttpServletResponse response) {
        Map<String, Object> result = JsonUtil.buildRequestObject(api, params);
        String transmitType = api.getTransmitType();
        if(StringUtils.equals("1",transmitType)){
            iDaApiOutService.executeServiceForwarding(response, JSONUtils.convertToLong(api.getApiId()),result);
        }else if(StringUtils.equals("1",transmitType)){
            iDaAssetGisOutService.executeServiceForwarding(response, JSONUtils.convertToLong(api.getApiId()),result);
        }else {
            return;
        }
    }
//
//    public static Map<String, Object> buildRequestObject(DsApiDO api,Map<String, Object> params) {
//        String headerJson = api.getHeaderJson();
//        Map<String, Object> result = new HashMap<>();
//        if(StringUtils.isNotEmpty(headerJson)){
//            List<Map<String, Object>> maps = JSONUtils.convertTaskDefinitionJson(headerJson);
//            List<Map<String, String>> fieldHerderList = maps.stream()
//                    .map(m -> {
//                        Map<String, String> header = new HashMap<>(2);
//                        header.put("name",       Objects.toString(m.get("name"), ""));
//                        header.put("defaultValue", Objects.toString(m.get("defaultValue"), ""));
//                        return header;
//                    })
//                    .collect(Collectors.toList());
//            result.put("fieldHerderList", fieldHerderList);
//        }
//        result.put("params", params);
//        return result;
//    }

        /**
         * 校验、打包、封装参数
         *
         * @param mapData
         * @param resType 回参type JSON、Map、List、不处理
         * @param api
         * @return
         */
    private static Object chackPackHttpData(String mapData, String resType, DsApiDO api) {
        //判断类型封装
        if (StringUtils.equals("不处理", resType)) {
            return mapData;
        }
        if (StringUtils.equals("JSON", resType)) {
            return mapData;
        }

        try {
            if (StringUtils.equals("Map", resType) && !StringUtils.isBlank(mapData)) {
                Map<String, Object> stringObjectMap = JsonUtil.parseJsonToMap(mapData);
                //判断是否有api的是否返回限制
                return JsonUtil.packFilterParameterOrMap(stringObjectMap, api);
            }

            if (StringUtils.equals("List", resType) && !StringUtils.isBlank(mapData)) {
                List<Object> maps = JsonUtil.parseJsonToListMap(mapData);
                return maps;
            }
        } catch (Exception e) {
            return mapData;
        }
        return mapData;

    }

    /**
     * 对于api进行校验，查看api是否禁用或者未查询到 等。。。
     *
     * @param yApiConfigEntity
     */
//    private static void chackYapiConfig(YApiConfigEntity yApiConfigEntity) {
//        //判断是否为null
//        if (yApiConfigEntity == null) {
//            throw new ServiceException("API调用，未查询到api配置");
//        }
//        //状态（0不启用，1启用）
//        if (!StringUtils.equals("1", yApiConfigEntity.getStatus())) {
//            throw new ServiceException("API调用，未查询到api未启用");
//        }
//    }

}
