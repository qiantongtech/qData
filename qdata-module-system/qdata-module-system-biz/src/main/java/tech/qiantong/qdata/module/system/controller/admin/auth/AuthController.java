package tech.qiantong.qdata.module.system.controller.admin.auth;

import cn.dev33.satoken.oauth2.template.SaOAuth2Util;
import cn.hutool.core.convert.Convert;
import com.ejlchina.okhttps.OkHttps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.domain.model.LoginUser;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.SoMap;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.system.dal.dataobject.auth.RelUserAuthProductDO;
import tech.qiantong.qdata.module.system.enums.auth.AuthProductEnums;
import tech.qiantong.qdata.module.system.service.ISysUserService;
import tech.qiantong.qdata.module.system.service.auth.IRelUserAuthProductService;
import tech.qiantong.qdata.redis.service.IRedisService;
import tech.qiantong.qdata.security.web.service.SysPermissionService;
import tech.qiantong.qdata.security.web.service.TokenService;

/**
 * oauth2 Controller
 *
 * @author surge
 * @date 2022-09-16
 */
@RestController
@RequestMapping("/oauth2")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private ISysUserService userService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private IRelUserAuthProductService userAuthProductService;

    @Value("${oauth2.redis-prefix}")
    private String redisPrefix;

    // redis二级文件夹命名
    public static final String accessTokenPrefix = "accessToken";
    public static final String refreshTokenPrefix = "refreshToken";

    // 相关参数配置
    @Value("${oauth2.clientId}")
    private String clientId;            // 应用id
    @Value("${oauth2.clientSecret}")
    private String clientSecret;		// 应用秘钥
    @Value("${oauth2.serverUrl}")
    private String serverUrl;	        // 服务端接口

    // 根据Code码进行登录，获取 Access-Token 和 openid
    @RequestMapping("/codeLogin")
    @Transactional
    public AjaxResult codeLogin(String code) {
        // 调用Server端接口，获取 Access-Token 以及其他信息
        String str = OkHttps.sync(serverUrl + "/oauth2/token")
                .addBodyPara("grant_type", "authorization_code")
                .addBodyPara("code", code)
                .addBodyPara("client_id", clientId)
                .addBodyPara("client_secret", clientSecret)
                .post()
                .getBody()
                .toString();
        SoMap so = SoMap.getSoMap().setJsonString(str);
        System.out.println("返回结果: " + so);

        // code不等于200  代表请求失败
        if(so.getInt("code") != 200) {
            return AjaxResult.error(so.getString("msg"));
        }

        // 根据openid获取其对应的userId
        SoMap data = so.getMap("data");

        // idHubId
        Long idHubId = data.getLong("idHubId");
        // Access-Token值
        String accessToken = data.getString("access_token");
        // Refresh-Token值
        String refreshToken = data.getString("refresh_token");
        // Access-Token剩余有效期，单位秒
        long expiresIn = data.getLong("expires_in");
        // Refresh-Token剩余有效期，单位秒
        long refreshExpiresIn = data.getLong("refresh_expires_in");

        SysUser user = this.getUserByIdHubId(idHubId);

        if (user == null) {
            // 通过openid获取userInfo
            SoMap userinfo = this.getUserinfo(accessToken);
            if (userinfo != null) {
                // 统一身份认证账户手机号
                String phone = userinfo.getString("phone");
                // 通过手机号查找用户
                SysUser userByPhone = userService.findUserByNameOrPhone(phone);

                if (userByPhone != null) {
                    RelUserAuthProductDO productDO = RelUserAuthProductDO.builder()
                            .userId(userByPhone.getUserId())
                            .authId(idHubId.toString())
                            .authProductType(AuthProductEnums.ANIVIA.code)
                            .build();

                    userAuthProductService.save(productDO);
                    user = userByPhone;
                } else {
                    return AjaxResult.error("系统中不存在此用户，请联系管理员！");
                }
            } else {
                return AjaxResult.error("获取统一身份认证平台用户身份信息失败！");
            }
        }

        // 存入redis
        redisService.set(redisPrefix + ":" + accessTokenPrefix + ":" + user.getUserId().toString(), accessToken, expiresIn);
        redisService.set(redisPrefix + ":" + refreshTokenPrefix + ":" + user.getUserId().toString(), refreshToken, refreshExpiresIn);

        // 创建登录用户(直接免密)
        LoginUser loginUser = createLoginUser(user);

        // 获取到免密token
        String token = tokenService.createToken(loginUser);

        log.info("用户：{}，通过统一身份认证平台登录成功！", user.getUserName());
        return AjaxResult.success(token);
    }


    // 刷新 Access-Token
    public String refresh(String refreshToken) {
            // 调用Server端接口，通过 Refresh-Token 刷新出一个新的 Access-Token
            String str = OkHttps.sync(serverUrl + "/oauth2/refresh")
                .addBodyPara("grant_type", "refresh_token")
                .addBodyPara("client_id", clientId)
                .addBodyPara("client_secret", clientSecret)
                .addBodyPara("refresh_token", refreshToken)
                .post()
                .getBody()
                .toString();
        SoMap so = SoMap.getSoMap().setJsonString(str);
        System.out.println("返回结果: " + so);

        // code不等于200  代表请求失败
        if(so.getInt("code") != 200) {
            return null;
        }

        // 返回相关参数 (data=新的Access-Token )
        SoMap data = so.getMap("data");

        // openid
        String openid = data.getString("openid");
        // Access-Token值
        String accessToken = data.getString("access_token");
        // Refresh-Token值
        String refToken = data.getString("refresh_token");
        // Access-Token剩余有效期，单位秒
        long expiresIn = data.getLong("expires_in");
        // Refresh-Token剩余有效期，单位秒
        long refreshExpiresIn = data.getLong("refresh_expires_in");

        Long userId = Convert.toLong(SaOAuth2Util.getLoginIdByAccessToken(accessToken));

        // 存入redis
        redisService.set(redisPrefix + ":" + accessTokenPrefix + ":" + userId.toString(), accessToken, expiresIn);
        redisService.set(redisPrefix + ":" + refreshTokenPrefix + ":" + userId.toString(), refreshToken, refreshExpiresIn);

        return data.getString("access_token");
    }

    public LoginUser createLoginUser(SysUser user)
    {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }

    /**
     * 根据 ddHubId获取 user
     * @param idHubId 统一身份认证 id
     * @return
     */
    private SysUser getUserByIdHubId(Long idHubId) {
        // 认证平台关联关系
        RelUserAuthProductDO authInfo = userAuthProductService.lambdaQuery()
                .eq(RelUserAuthProductDO::getUserId, idHubId)
                .eq(RelUserAuthProductDO::getAuthProductType, AuthProductEnums.ANIVIA.code)
                .one();

        if (authInfo != null) {
            return userService.selectUserById(authInfo.getUserId());
        }
        return null;
    }

    /**
     * 根据 Access-Token 置换相关的资源: 获取账号昵称、头像、性别等信息
     * @param accessToken
     * @return
     */
    @RequestMapping("/getUserinfo")
    public SoMap getUserinfo(String accessToken) {
        // 调用Server端接口，查询开放的资源
        String str = OkHttps.sync(serverUrl + "/oauth2/userinfo")
                .addBodyPara("access_token", accessToken)
                .post()
                .getBody()
                .toString();
        SoMap so = SoMap.getSoMap().setJsonString(str);
        System.out.println("返回结果: " + so);

        // code不等于200  代表请求失败
        if(so.getInt("code") != 200) {
            throw new ServiceException("获取用户账号信息失败！");
        }

        // 返回相关参数 (data=获取到的资源 )
        return so.getMap("data");
    }

    /**
     * 退出登录
     * @param userId
     * @return
     */
    @RequestMapping("/sso/logout")
    public AjaxResult loginOut(String userId) {
        String accessToken = redisService.get(redisPrefix + ":" + accessTokenPrefix + ":" + userId);

        if (StringUtils.isEmpty(accessToken)) {
            String refreshToken = redisService.get(redisPrefix + ":" + refreshTokenPrefix + ":" + userId);
            accessToken = this.refresh(refreshToken);
        }

        // 调用Server端接口，查询开放的资源
        String str = OkHttps.sync(serverUrl + "/oauth2/logout")
                .addBodyPara("access_token", accessToken)
                .post()
                .getBody()
                .toString();
        SoMap so = SoMap.getSoMap().setJsonString(str);
        System.out.println("返回结果: " + so);

        return AjaxResult.success(so);
    }
}
