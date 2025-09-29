package tech.qiantong.qdata.module.ds.controller.admin.auth;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.oauth2.processor.SaOAuth2ServerProcessor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.qiantong.qdata.common.annotation.Anonymous;
import tech.qiantong.qdata.module.ds.annotation.DsCheckClientToken;

/**
 * DS模块 OAuth2 Server端 控制器
 * @author Ming
 */
@Tag(name = "API服务-鉴权")
@RestController
@Slf4j
public class DsAuthController {

    /**
     * 处理 OAuth2 凭证式相关请求
     */
    @Anonymous
    @Operation(
            summary = "应用鉴权接口",
            description = "该接口用于验证应用的身份，确保接口调用者具备足够的权限进行操作。"
    )
    @PostMapping("/oauth2/client_token")
    public Object request(
            @RequestParam(name = "grant_type") String grantType,
            @RequestParam(name = "client_id") String clientId,
            @RequestParam(name = "client_secret") String clientSecret
    ) {
        log.info("------- 进入请求: " + SaHolder.getRequest().getUrl());
        return SaOAuth2ServerProcessor.instance.clientToken();
    }
}
