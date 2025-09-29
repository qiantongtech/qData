package tech.qiantong.qdata.module.ds.controller.admin.api;


import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.core.domain.AjaxResult;

/**
 * <p>
 * 开放服务拦截下线、不存在、请求方式错误请求，返回错误信息
 * </p>
 * @author lhs
 */

@RestController
@RequestMapping
@CrossOrigin
public class CommonServicesController {

    /**
     * 拦截services开头的所有请求
     *
     * @return
     */
    @RequestMapping("services/**")
    public AjaxResult services() {
        return AjaxResult.error("服务不存在、或者已下线、或者请求方式错误（GET、POST）");
    }

}
