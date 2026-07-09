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

package tech.qiantong.qdata.module.da.utils.video;

import com.alibaba.fastjson2.JSONArray;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.video.DaAssetVideoDO;


@Component
public class VideoHandler {

    /**
     * Get all site information
     *
     * @return
     * @throws Exception
     */
    private static JSONArray queryVideoList(DaAssetVideoDO daAssetVideoDO) throws Exception {
//        Map<String, Object> objectMap = JSONUtils.convertTaskDefinitionJsonMap(daAssetVideoDO.getConfig());
//        String appkey = MapUtils.getString(objectMap, "appkey");
//        String appSecret = MapUtils.getString(objectMap, "appSecret");
//        String artemisPath = MapUtils.getString(objectMap, "artemisPath");
//        String ip = daAssetVideoDO.getIp();
//        Long port = daAssetVideoDO.getPort();
//        String host = ip + String.valueOf(port);
//
//        /**
//         * https://ip:port/artemis/api/resource/v1/regions
//         * By consulting the AI Cloud Open Platform documentation or the gateway portal documentation,
//         * you can see the definition of paginated region list retrieval. This is a POST request REST API,
//         * with JSON string as input parameter, and the API protocol is https.
//         * ArtemisHttpUtil provides doPostStringArtemis method for calling POST requests,
//         * input parameters can pass JSON string. Please read the development guide to understand method parameters,
//         * pass null for parameters that are not available.
//         */
//        ArtemisConfig config = new ArtemisConfig();
//        config.setHost(host); // Proxy API gateway nginx server IP port
//        config.setAppKey(appkey);  // Secret key appkey
//        config.setAppSecret(appSecret);// Secret key appSecret
//        final String getCamsApi = artemisPath + "/api/nms/v1/online/camera/get";
//        Map<String, String> paramMap = new HashMap<String, String>();// POST request form parameters
//        paramMap.put("pageNo", "1");
//        paramMap.put("pageSize", "999999");
//        paramMap.put("treeCode", "0");
//        String body = JSON.toJSON(paramMap).toString();
//        Map<String, String> path = new HashMap<String, String>(2) {
//            {
//                put("https://", getCamsApi);
//            }
//        };
//        JSONArray dataList = null;
//        ArtemisHttpUtil.doPostStringArtemis(config,path,paramMap,null, null, "application/json")
//        String resStr = ArtemisHttpUtil.doPostStringArtemis(config, path, body, null, null, "application/json");
//        if (StringUtils.isNotBlank(resStr)) {
//            JSONObject res = JSONObject.parseObject(resStr);
//            if (StringUtils.equals("0", res.getString("code"))) {
//                dataList = res.getJSONObject("data").getJSONArray("list");
//            }
//        }
//        return dataList;
        return null;
    }
}
