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
 * 通用返回
 *
 * @author Ming
 * @param <T> 数据泛型
 */
@Schema(description = "通用返回")
@Data
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     *
     * @see ErrorCode#getCode()
     */
    @Schema(description = "错误码")
    private Integer code;
    /**
     * 返回数据
     */
    @Schema(description = "返回数据")
    private T data;
    /**
     * 错误提示，用户可阅读
     *
     * @see ErrorCode#getMsg() ()
     */
    @Schema(description = "返回信息")
    private String msg;

    /**
     * 将传入的 result 对象，转换成另外一个泛型结果的对象
     *
     * 因为 A 方法返回的 CommonResult 对象，不满足调用其的 B 方法的返回，所以需要进行转换。
     *
     * @param result 传入的 result 对象
     * @param <T>    返回的泛型
     * @return 新的 CommonResult 对象
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

    @JsonIgnore // 避免 jackson 序列化
    public boolean isSuccess() {
        return isSuccess(code);
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isError() {
        return !isSuccess();
    }

    // ========= 和 Exception 异常体系集成 =========

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     */
    public void checkError() throws ServiceException {
        if (isSuccess()) {
            return;
        }
        // 业务异常
        throw new ServiceException(msg, code);
    }

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     * 如果没有，则返回 {@link #data} 数据
     */
    @JsonIgnore // 避免 jackson 序列化
    public T getCheckedData() {
        checkError();
        return data;
    }

    public static <T> CommonResult<T> error(ServiceException serviceException) {
        return error(serviceException.getCode(), serviceException.getMessage());
    }

}
