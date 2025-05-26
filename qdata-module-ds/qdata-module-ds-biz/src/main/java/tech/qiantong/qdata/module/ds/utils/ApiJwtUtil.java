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
