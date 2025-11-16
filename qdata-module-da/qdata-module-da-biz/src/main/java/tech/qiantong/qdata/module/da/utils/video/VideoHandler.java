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

package tech.qiantong.qdata.module.da.utils.video;

import com.alibaba.fastjson2.JSONArray;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.video.DaAssetVideoDO;


@Component
public class VideoHandler {

    /**
     * 获取所有站点信息
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
//         * 过查阅AI Cloud开放平台文档或网关门户的文档可以看到分页获取区域列表的定义,这是一个POST请求的Rest接口, 入参为JSON字符串，接口协议为https。
//         * ArtemisHttpUtil工具类提供了doPostStringArtemis调用POST请求的方法，入参可传JSON字符串, 请阅读开发指南了解方法入参，没有的参数可传null
//         */
//        ArtemisConfig config = new ArtemisConfig();
//        config.setHost(host); // 代理API网关nginx服务器ip端口
//        config.setAppKey(appkey);  // 秘钥appkey
//        config.setAppSecret(appSecret);// 秘钥appSecret
//        final String getCamsApi = artemisPath + "/api/nms/v1/online/camera/get";
//        Map<String, String> paramMap = new HashMap<String, String>();// post请求Form表单参数
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
