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

package tech.qiantong.qdata.security.web.exception;

import cn.hutool.core.convert.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tech.qiantong.qdata.common.constant.HttpStatus;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.exception.DemoModeException;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.html.EscapeUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Global exception handler
 * System exceptions and business exceptions uniformly return i18n messages (corresponding copy is returned according to the request language zh_CN / en_US / ja_JP)
 *
 * @author qdata
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Permission verification exception
     */
    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error(MessageUtils.messageEn("log.exception.access.denied"), requestURI, e.getMessage());
        String message = MessageUtils.messageWithFallback("sys.error", "没有权限，请联系管理员授权");
        return AjaxResult.error(HttpStatus.FORBIDDEN, message);
    }

    /**
     * The request method is not supported
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
            HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error(MessageUtils.messageEn("log.exception.method.not.supported"), requestURI, e.getMethod());
        String message = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() :
                MessageUtils.messageWithFallback("sys.error.method", e.getMessage());
        return AjaxResult.error(message);
    }

    /**
     * Business abnormality
     * Prioritize using the i18nCode set in ServiceException to obtain internationalized messages.
     * If i18nCode is not set, the original message will be returned directly (backwards compatible)
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request)
    {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        String message = e.getMessage();
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, message) : AjaxResult.error(message);
    }

    /**
     * A required path variable is missing from the request path
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        String variableName = e.getVariableName();
        log.error(MessageUtils.messageEn("log.exception.path.variable.missing"), requestURI, e);
        String message = StringUtils.isNotEmpty(variableName) ? MessageUtils.messageWithFallback("sys.error.path.missing", String.format(MessageUtils.message("log.exception.path.variable.missing"), variableName))
                : MessageUtils.messageWithFallback("sys.error.path.missing", String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
        return AjaxResult.error(message);
    }

    /**
     * Request parameter type mismatch
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        String value = Convert.toStr(e.getValue());
        if (StringUtils.isNotEmpty(value))
        {
            value = EscapeUtil.clean(value);
        }
        log.error(MessageUtils.message("log.exception.param.type.mismatch"), requestURI, e);
        String tips = String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), e.getRequiredType().getName(), value);
        String message = StringUtils.isNotEmpty(e.getName()) ? tips :
                MessageUtils.messageWithFallback("sys.error.param.type", tips);
        return AjaxResult.error(message);
    }

    /**
     * Intercept unknown runtime exceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error(MessageUtils.message("log.exception.unknown"), requestURI, e);
        String message = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() :
                MessageUtils.messageWithFallback("sys.error.unknown", e.getMessage());
        return AjaxResult.error(message);
    }

    /**
     * System exception
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error(MessageUtils.message("log.exception.system"), requestURI, e);
        String message = StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() :
                MessageUtils.messageWithFallback("sys.error", e.getMessage());
        return AjaxResult.error(message);
    }

    /**
     * Custom validation exception
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * Custom validation exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * Demo mode exception
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult handleDemoModeException(DemoModeException e)
    {
        String message = MessageUtils.messageWithFallback("biz.error.demo", "演示模式，不允许操作");
        return AjaxResult.error(message);
    }
}
