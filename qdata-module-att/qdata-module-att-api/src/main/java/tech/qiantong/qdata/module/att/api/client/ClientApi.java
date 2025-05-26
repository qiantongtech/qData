package tech.qiantong.qdata.module.att.api.client;

import tech.qiantong.qdata.module.att.api.client.dto.AttClientRespDTO;

/**
 * 应用 API 接口
 *
 * @author Ming
 */
public interface ClientApi {

    /**
     * 获得应用信息
     *
     * @param id 应用编号
     * @return 应用信息
     */
    AttClientRespDTO getClient(Long id);
}
