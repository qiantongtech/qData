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
 * For brand customization, please contact the official team for authorization.
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
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.ds.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import org.springframework.stereotype.Component;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2024-04-18 14:07
 **/
@Component
public class ApiJwtUtil {

    //TOKEN过期时间 默认7天
    public static final long TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 7;

    //Refresh-Token过期时间 默认30天
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30;

    public static final String TOKEN_KEY = "API_TOKEN";

    public static final String REFRESH_TOKEN_KEY = "REFRESH_API_TOKEN";

    private static JwtParser jwtParser;



    /**
     * 通过token获取username
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        Jws<Claims> jws = jwtParser.parseClaimsJws(token);
        if (jws != null) {
            Claims claims = jws.getBody();
            if (claims != null) {
                return claims.getSubject();
            }
        }
        return null;
    }

}
