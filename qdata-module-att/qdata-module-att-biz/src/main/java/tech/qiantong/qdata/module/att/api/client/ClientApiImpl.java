package tech.qiantong.qdata.module.att.api.client;

import org.springframework.stereotype.Service;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.api.client.dto.AttClientRespDTO;
import tech.qiantong.qdata.module.att.service.client.IAttClientService;

import javax.annotation.Resource;

/**
 * 应用 Api 实现类
 * @author Ming
 */
@Service
public class ClientApiImpl implements ClientApi {

    @Resource
    private IAttClientService clientService;

    @Override
    public AttClientRespDTO getClient(Long id) {
        return BeanUtils.toBean(clientService.getAttClientById(id), AttClientRespDTO.class);
    }
}
