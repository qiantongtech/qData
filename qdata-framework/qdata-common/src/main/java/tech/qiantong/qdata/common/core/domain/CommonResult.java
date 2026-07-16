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

package tech.qiantong.qdata.common.core.domain;

import cn.hutool.core.convert.Convert;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.Assert;
import tech.qiantong.qdata.common.exception.ErrorCode;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.exception.enums.GlobalErrorCodeConstants;

import java.io.Serializable;
import java.util.Objects;

/**
 * Generic return
 *
 * @author Ming
 * @param <T> Data generics
 */
@Schema(description = "通用返回")
@Data
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Error code
     *
     * @see ErrorCode#getCode()
     */
    @Schema(description = "错误码")
    private Integer code;
    /**
     * Return data
     */
    @Schema(description = "返回数据")
    private T data;
    /**
     * Error message, user can read
     *
     * @see ErrorCode#getMsg() ()
     */
    @Schema(description = "返回信息")
    private String msg;

    /**
     * Convert the incoming result object into another generic result object
     *
     * Because the CommonResult object returned by method A does not satisfy the return of method B that calls it, conversion needs to be performed.
     *
     * @param result the incoming result object
     * @param <T> the returned generic
     * @return new CommonResult object
     */
    public static <T> CommonResult<T> error(CommonResult<?> result) {
        return error(result.getCode(), result.getMsg());
    }

    public static <T> CommonResult<T> error(Integer code, String message) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getCode().equals(code), "code 必须是错误的！");
        CommonResult<T> result = new CommonResult<>();
        result.code = code;
        result.msg = message;
        return result;
    }

    public static <T> CommonResult<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
        result.data = data;
        result.msg = GlobalErrorCodeConstants.SUCCESS.getMsg();
        return result;
    }

    public static <T>  CommonResult<T> toAjax(T data) {
        CommonResult<T> result = new CommonResult<>();
        if (Convert.toBool(data)) {
            result.code = GlobalErrorCodeConstants.SUCCESS.getCode();
            result.msg = GlobalErrorCodeConstants.SUCCESS.getMsg();
        } else {
            result.code = GlobalErrorCodeConstants.ERROR.getCode();
            result.msg = GlobalErrorCodeConstants.ERROR.getMsg();
        }
        return result;
    }

    public static boolean isSuccess(Integer code) {
        return Objects.equals(code, GlobalErrorCodeConstants.SUCCESS.getCode());
    }

    @JsonIgnore // Avoid jackson serialization
    public boolean isSuccess() {
        return isSuccess(code);
    }

    @JsonIgnore // Avoid jackson serialization
    public boolean isError() {
        return !isSuccess();
    }

    // ========= Integrated with Exception exception system =========

    /**
     * Determine whether there is any abnormality. If so, throw {@link ServiceException} exception
     */
    public void checkError() throws ServiceException {
        if (isSuccess()) {
            return;
        }
        // Business abnormality
        throw new ServiceException(msg, code);
    }

    /**
     * Determine whether there is any abnormality. If so, throw {@link ServiceException} exception
     * If not, return {@link #data} data
     */
    @JsonIgnore // Avoid jackson serialization
    public T getCheckedData() {
        checkError();
        return data;
    }

    public static <T> CommonResult<T> error(ServiceException serviceException) {
        return error(serviceException.getCode(), serviceException.getMessage());
    }

}
