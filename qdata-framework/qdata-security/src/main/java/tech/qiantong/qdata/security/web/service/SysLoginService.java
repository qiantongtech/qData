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

package tech.qiantong.qdata.security.web.service;

import cn.hutool.core.lang.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.constant.CacheConstants;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.constant.UserConstants;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.domain.model.LoginUser;
import tech.qiantong.qdata.common.core.redis.RedisCache;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.exception.user.*;
import tech.qiantong.qdata.common.utils.DateUtils;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.ip.IpUtils;
import tech.qiantong.qdata.module.system.service.ISysConfigService;
import tech.qiantong.qdata.module.system.service.ISysUserService;
import tech.qiantong.qdata.security.context.AuthenticationContextHolder;
import tech.qiantong.qdata.security.manager.AsyncManager;
import tech.qiantong.qdata.security.manager.factory.AsyncFactory;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Login verification method
 *
 * @author qdata
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SysPermissionService permissionService;

    @Resource
    private UserDetailsServiceImpl detailsService;

    //Universal password
    @Value(value = "${user.password.universalPassword}")
    private String universalPassword;  // Universal password

// private final String universalPassword = "gfh78h23789#$gfdy845"; // Universal password

    /**
     * Login verification
     *
     * @param username username
     * @param password password
     * @param code verification code
     * @param uuid unique identifier
     * @return result
     */
    public Map login(String username, String password, String code, String uuid) {
        // Verification code verification
        validateCaptcha(username, code, uuid);
        // Login pre-verification
        loginPreCheck(username, password);
        // User verification
        Authentication authentication = null;

        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            //Universal password
//            universalPassword = StringUtils.isBlank(universalPassword) ? "gfh78h23789#$gfdy845" : universalPassword;
            // If you enter a universal password, you will be allowed to log in directly.
            SysUser user = userService.selectUserByUserName(username);
            if (StringUtils.isNotBlank(universalPassword) && universalPassword.equals(password) && user != null) {
                LoginUser loginUser = new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));

                UserDetails userDetails = detailsService.loadUserByUsernameUniversalPassword(username);

                // Record login information
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.messageEn("user.login.success")));
                recordLoginInfo(loginUser.getUserId());  // Record login information
                String token = tokenService.createToken(loginUser);  // Generate token

                return Dict.create().set("token", token).set("userId", loginUser.getUserId());  // Return token and userId
            }

            // If it is not a universal password, perform regular login verification
            // This method will call UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.messageEn("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.messageEn("user.login.success")));
        //AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.messageEn("user.login.success")).run();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // Generate token
        String token = tokenService.createToken(loginUser);
        return Dict.create().set("token", token).set("userId", loginUser.getUserId());
    }

    /**
     * Verify verification code
     *
     * @param username username
     * @param code verification code
     * @param uuid unique identifier
     * @return result
     */
    public void validateCaptcha(String username, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            if (captcha == null) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.messageEn("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            redisCache.deleteObject(verifyKey);
            if (!code.equalsIgnoreCase(captcha)) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.messageEn("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     * Login pre-verification
     *
     * @param username username
     * @param password user password
     */
    public void loginPreCheck(String username, String password) {
        // Username or password is empty Error
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.messageEn("not.null")));
            throw new UserNotExistsException();
        }
        // If the password is not within the specified range, it is an error.
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.messageEn("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // Username is not within the specified range error
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.messageEn("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP blacklist verification
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.messageEn("login.blocked")));
            throw new BlackListException();
        }
    }

    /**
     * Record login information
     *
     * @param userId user ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
