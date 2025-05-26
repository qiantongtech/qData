package tech.qiantong.qdata.module.ds.controller.admin.auth;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.oauth2.processor.SaOAuth2ServerProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.annotation.Anonymous;
import tech.qiantong.qdata.module.ds.annotation.DsCheckClientToken;

/**
 * DS模块 OAuth2 Server端 控制器
 * @author Ming
 */
@RestController
@Slf4j
public class DsAuthController {

    /**
     * 处理 OAuth2 凭证式相关请求
     */
    @Anonymous
    @PostMapping("/oauth2/client_token")
    public Object request() {
        log.info("------- 进入请求: " + SaHolder.getRequest().getUrl());
        return SaOAuth2ServerProcessor.instance.clientToken();
    }

    @Anonymous
    @DsCheckClientToken
    @PostMapping("/oauth2/test")
    public Object test() {
        return "test";
    }
}
