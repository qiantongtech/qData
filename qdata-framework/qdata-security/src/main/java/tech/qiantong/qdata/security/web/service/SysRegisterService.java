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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.common.constant.CacheConstants;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.constant.UserConstants;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.domain.model.RegisterBody;
import tech.qiantong.qdata.common.core.redis.RedisCache;
import tech.qiantong.qdata.common.exception.user.CaptchaException;
import tech.qiantong.qdata.common.exception.user.CaptchaExpireException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.SecurityUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.module.system.service.ISysConfigService;
import tech.qiantong.qdata.module.system.service.ISysUserService;
import tech.qiantong.qdata.security.manager.AsyncManager;
import tech.qiantong.qdata.security.manager.factory.AsyncFactory;

/**
 * Registration verification method
 *
 * @author qdata
 */
@Component
public class SysRegisterService
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    /**
     * Register
     */
    public String register(RegisterBody registerBody)
    {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);

        // Verification code switch
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username))
        {
            msg = MessageUtils.messageWithFallback("user.register.username.empty", "Username cannot be empty");
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = MessageUtils.messageWithFallback("user.register.password.empty", "Password cannot be empty");
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = MessageUtils.messageWithFallback("user.register.username.length",
                    "Username length must be between 2 and 20 characters");
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = MessageUtils.messageWithFallback("user.register.password.length",
                    "Password length must be between 5 and 20 characters");
        }
        else if (!userService.checkUserNameUnique(sysUser))
        {
            msg = MessageUtils.messageWithFallback("user.register.username.duplicate",
                    "Failed to save user ''{0}'': the account already exists", username);
        }
        else
        {
            sysUser.setNickName(username);
            sysUser.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(sysUser);
            if (!regFlag)
            {
                msg = MessageUtils.messageWithFallback("user.register.fail",
                        "Registration failed; contact the administrator");
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.messageEn("user.register.success")));
            }
        }
        return msg;
    }

    /**
     * Verify verification code
     *
     * @param username username
     * @param code verification code
     * @param uuid unique identifier
     * @return result
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}
