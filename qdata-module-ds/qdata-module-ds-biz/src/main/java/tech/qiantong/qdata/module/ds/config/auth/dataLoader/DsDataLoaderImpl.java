package tech.qiantong.qdata.module.ds.config.auth.dataLoader;

import cn.dev33.satoken.oauth2.consts.GrantType;
import cn.dev33.satoken.oauth2.data.loader.SaOAuth2DataLoader;
import cn.dev33.satoken.oauth2.data.model.loader.SaClientModel;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;
import tech.qiantong.qdata.module.att.api.client.ClientApi;
import tech.qiantong.qdata.module.att.api.client.dto.AttClientRespDTO;

import javax.annotation.Resource;

/**
 * Sa-Token OAuth2：自定义数据加载器
 * @author Ming
 */
@Component
public class DsDataLoaderImpl implements SaOAuth2DataLoader {

    @Resource
    private ClientApi clientApi;

    /**
     * 根据 clientId 获取 Client 信息
     * @param clientId 应用id
     * @return Client 应用信息 Model
     */
    @Override
    public SaClientModel getClientModel(String clientId) {
        AttClientRespDTO client = clientApi.getClient(Convert.toLong(clientId));

        if (client != null) {
            return new SaClientModel()
                    // client id
                    .setClientId(client.getId().toString())
                    // client 秘钥
                    .setClientSecret(client.getSecret())
                    // 所有允许授权的 url
                    .addAllowRedirectUris("*")
                    // 所有签约的权限
                    .addContractScopes("openid", "userid", "userinfo")
                    // 所有允许的授权模式
                    .addAllowGrantTypes(
                            GrantType.authorization_code,
                            GrantType.implicit,
                            GrantType.refresh_token,
                            GrantType.password,
                            GrantType.client_credentials
                    );
        } else {
            return null;
        }
    }

    /**
     * 根据 clientId 和 loginId 获取 openid
     * @param clientId 应用id
     * @param loginId 账号id
     * @return openid
     */
    @Override
    public String getOpenid(String clientId, Object loginId) {
        // todo 暂时随机生成
        return IdUtil.fastSimpleUUID();
    }

}
