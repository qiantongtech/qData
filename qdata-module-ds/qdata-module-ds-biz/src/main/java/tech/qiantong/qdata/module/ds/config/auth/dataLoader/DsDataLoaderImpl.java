/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
