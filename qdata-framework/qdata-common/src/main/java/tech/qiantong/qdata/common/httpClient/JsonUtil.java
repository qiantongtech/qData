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

package tech.qiantong.qdata.common.httpClient;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * Json data conversion tool class
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
     * Convert Json string to Map
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
     * Convert Json string to List<Map<String, Object>>
     *
     * @param jsonString JSON string
     * @return converted List<Map<String, Object>>
     */
    public static List<Object>  parseJsonToListMap(String jsonString) {
        List<Object> list = JSON.parseObject(jsonString, new TypeReference<List<Object>>(){});
        System.err.println("Json转List<Map>:");
        System.err.println("jsonString");
        return list;
    }

    /**
     * Convert Map to Json
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
     * Convert Object to Json
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



//    /**
// * Parameter packaging filtering
//     *
//     * @param jsonToMap
//     * @param api
//     * @return
//     */
//    public static Map<String, Object> packFilterParameterOrMap(Map<String, Object> jsonToMap, DataApiEntity api) {
// //Create return parameters
//        Map<String,Object> parameter = new HashMap<>();
//
//        try {
// //Get the set return information
//            List<ResParam> resParams = api.getResParams();
// //Loop
//            for (ResParam resParam : resParams) {
// //Field name
//                String fieldName = resParam.getFieldName();
//                if(StringUtils.isBlank(fieldName)){
//                    continue;
//                }
// //Get information
//                Object object = MapUtils.getObject(jsonToMap, fieldName, null);
// //Encapsulation parameters
//                JsonUtil.recursionPackFilterParameter(resParam, object,parameter);
//            }
// //return
//            return parameter;
//        }catch (Exception e){
//            return jsonToMap;
//        }
//
//    }

//    /**
// * Store information recursively
//     * @param object
//     * @param parameter
//     * @return
//     */
//    public static void recursionPackFilterParameter(ResParam resParam, Object object, Map<String, Object> parameter) {
//        String dataType = resParam.getDataType();
// //Field name
//        String fieldName = resParam.getFieldName();
//        if(object == null){
//            parameter.put(fieldName,object);
//            return;
//        }
// //basic type
//        if(StringUtils.equals( TYPE_long ,dataType)
//            || StringUtils.equals( TYPE_double ,dataType)
//            || StringUtils.equals( TYPE_Boolean ,dataType)
//            || StringUtils.equals( TYPE_String ,dataType)
//            || StringUtils.equals( TYPE_Data ,dataType)
//            || StringUtils.equals( TYPE_Array ,dataType) ){
//            parameter.put(fieldName,object);
//            return;
//        }
//        //map
//        if(StringUtils.equals( TYPE_Object ,dataType)) {
//            recursionPackFilterMap(resParam,object,parameter);
//            return;
//        }
//        //list
//        if(StringUtils.equals( TYPE_ArrayList ,dataType)) {
//            List<Map> objects = new ArrayList<>();
//            recursionPackFilterList(resParam,object,objects);
//            parameter.put(fieldName,objects);
//            return;
//        }
// //Default
//        parameter.put(fieldName,object);
//    }
//
//    /**
// * Store information recursively
//     *  Map
//     * @param object
//     * @param parameter
//     * @return
//     */
//    public static void recursionPackFilterMap(ResParam resParam, Object object, Map<String, Object> parameter) {
// //Field name
//        String fieldName = resParam.getFieldName();
//        Map<Object, Object> objectmap = (Map<Object, Object>) object;
//        if(MapUtils.isEmpty(objectmap)){
//            parameter.put(fieldName,object);
//        }
//
//        List<ResParam> resParamList = resParam.getResParamList();
//
//        Map<String, Object> paramMap = new HashMap<>();
//        for (ResParam param : resParamList) {
// //Field name
//            String paramName = param.getFieldName();
//            if(StringUtils.isBlank(paramName)){
//                continue;
//            }
// //Get information
//            Object objectparam = MapUtils.getObject(objectmap, paramName, null);
//            recursionPackFilterParameter(param,objectparam,paramMap);
//        }
//        parameter.put(fieldName,paramMap);
//    }
//
//    /**
// * Store information recursively
//     *  list
//     * @param o
//     * @param parameter
//     * @return
//     */
//    public static void recursionPackFilterList(ResParam resParam, Object o, List<Map>  parameter) {
//        List<Map> objectList = (List<Map>) o;
//
// // Get the fields to be returned
//        List<ResParam> resParamList = resParam.getResParamList();
//        for (Map map : objectList) {
// //Create return parameters
//            Map<String,Object> param = new HashMap<>();
//            for (ResParam resMap : resParamList) {
//                recursionPackFilterMap(resMap,map,param);
//            }
//
//            parameter.add(param);
//
//        }
//    }


}
