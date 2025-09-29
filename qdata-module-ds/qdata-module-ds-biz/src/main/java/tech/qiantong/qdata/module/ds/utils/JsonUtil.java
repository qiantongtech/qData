package tech.qiantong.qdata.module.ds.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import tech.qiantong.qdata.common.utils.JSONUtils;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.module.ds.dal.dataobject.dto.ResParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Json数据转换工具类
 * @author chen
 */
public class JsonUtil {

    public static final String TYPE_long = "long";
    public static final String TYPE_double = "double";
    public static final String TYPE_Boolean = "Boolean";
    public static final String TYPE_String = "String";
    public static final String TYPE_Data = "Data";
    public static final String TYPE_Object = "Object";
    public static final String TYPE_Array = "Array";
    public static final String TYPE_ArrayList = "ArrayList";

    /**
     * 将Json字符串转成Map
     *
     * @param jsonString
     * @return map
     */
    public static Map<String,Object> parseJsonToMap(String jsonString) {
        Map<String,Object> map = JSON.parseObject(jsonString, Map.class);
        System.err.println("Json转Map:");
        for (Object obj : map.keySet()) {
            System.err.print(obj + "-" + map.get(obj));
        }
        System.err.println();
        return map;
    }

    /**
     * 将Json字符串转成List<Map<String, Object>>
     *
     * @param jsonString JSON字符串
     * @return 转换后的List<Map<String, Object>>
     */
    public static List<Object>  parseJsonToListMap(String jsonString) {
        List<Object> list = JSON.parseObject(jsonString, new TypeReference<List<Object>>(){});
        System.err.println("Json转List<Map>:");
        System.err.println("jsonString");
        return list;
    }

    /**
     * 将Map转换成Json
     *
     * @param map
     * @return
     */
    public static String parseMapToJson(Map<String, Object> map) {
        String json = JSON.toJSONString(map);
        System.err.println("Map转Json:");
        System.err.println(json);
        return json;
    }

    /**
     * 将Object转换成Json
     *
     * @param map
     * @return
     */
    public static String parseObjectToJson(Object map) {
        String json = JSON.toJSONString(map);
        System.err.println("Map转Json:");
        System.err.println(json);
        return json;
    }



    /**
     * 参数打包过滤
     *
     * @param jsonToMap
     * @param api
     * @return
     */
    public static Map<String, Object> packFilterParameterOrMap(Map<String, Object> jsonToMap, DsApiDO api) {
        //创建返回参数
        Map<String,Object> parameter = new HashMap<>();

        try {
            //获取设置的返回信息
            List<ResParam> resParams = api.getResParamsList();
            //循环
            for (ResParam resParam : resParams) {
                //字段名称
                String fieldName = resParam.getFieldName();
                if(StringUtils.isBlank(fieldName)){
                    continue;
                }
                //获取信息
                Object object = MapUtils.getObject(jsonToMap, fieldName, null);
                //封装参数
                JsonUtil.recursionPackFilterParameter(resParam, object,parameter);
            }
            //返回
            return parameter;
        }catch (Exception e){
            return jsonToMap;
        }

    }

    /**
     * 递归存储信息
     * @param object
     * @param parameter
     * @return
     */
    public static void recursionPackFilterParameter(ResParam resParam, Object object, Map<String, Object> parameter) {
        String dataType = resParam.getDataType();
        //字段名称
        String fieldName = resParam.getFieldName();
        if(object == null){
            parameter.put(fieldName,object);
            return;
        }
        //基本类型
        if(StringUtils.equals( TYPE_long ,dataType)
            || StringUtils.equals( TYPE_double ,dataType)
            || StringUtils.equals( TYPE_Boolean ,dataType)
            || StringUtils.equals( TYPE_String ,dataType)
            || StringUtils.equals( TYPE_Data ,dataType)
            || StringUtils.equals( TYPE_Array ,dataType) ){
            parameter.put(fieldName,object);
            return;
        }
        //map
        if(StringUtils.equals( TYPE_Object ,dataType)) {
            recursionPackFilterMap(resParam,object,parameter);
            return;
        }
        //list
        if(StringUtils.equals( TYPE_ArrayList ,dataType)) {
            List<Map> objects = new ArrayList<>();
            recursionPackFilterList(resParam,object,objects);
            parameter.put(fieldName,objects);
            return;
        }
        //默认
        parameter.put(fieldName,object);
    }

    /**
     * 递归存储信息
     *  Map
     * @param object
     * @param parameter
     * @return
     */
    public static void recursionPackFilterMap(ResParam resParam, Object object, Map<String, Object> parameter) {
        //字段名称
        String fieldName = resParam.getFieldName();
        Map<Object, Object> objectmap = (Map<Object, Object>) object;
        if(MapUtils.isEmpty(objectmap)){
            parameter.put(fieldName,object);
        }

        List<ResParam> resParamList = resParam.getResParamList();

        Map<String, Object> paramMap = new HashMap<>();
        for (ResParam param : resParamList) {
            //字段名称
            String paramName = param.getFieldName();
            if(StringUtils.isBlank(paramName)){
                continue;
            }
            //获取信息
            Object objectparam = MapUtils.getObject(objectmap, paramName, null);
            recursionPackFilterParameter(param,objectparam,paramMap);
        }
        parameter.put(fieldName,paramMap);
    }

    /**
     * 递归存储信息
     *  list
     * @param o
     * @param parameter
     * @return
     */
    public static void recursionPackFilterList(ResParam resParam, Object o, List<Map>  parameter) {
        List<Map> objectList = (List<Map>) o;

        // 获取要返回的字段
        List<ResParam> resParamList = resParam.getResParamList();
        for (Map map : objectList) {
            //创建返回参数
            Map<String,Object> param = new HashMap<>();
            for (ResParam resMap : resParamList) {
                recursionPackFilterMap(resMap,map,param);
            }

            parameter.add(param);

        }
    }

    public static Map<String, Object> buildRequestObject(DsApiDO api,Map<String, Object> params) {
        String headerJson = api.getHeaderJson();
        Map<String, Object> result = new HashMap<>();
        if(StringUtils.isNotEmpty(headerJson)){
            List<Map<String, Object>> maps = JSONUtils.convertTaskDefinitionJson(headerJson);
            List<Map<String, String>> fieldHerderList = maps.stream()
                    .map(m -> {
                        Map<String, String> header = new HashMap<>(2);
                        header.put("name",       Objects.toString(m.get("name"), ""));
                        header.put("defaultValue", Objects.toString(m.get("defaultValue"), ""));
                        return header;
                    })
                    .collect(Collectors.toList());
            result.put("fieldHerderList", fieldHerderList);
        }
        result.put("params", params);
        return result;
    }

}
