package tech.qiantong.qdata.api.ds.api.service.etl;

import tech.qiantong.qdata.api.ds.api.etl.DsNodeGenCodeRespDTO;

/**
 * <P>
 * 用途:ds数据集成节点相关接口
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 16:47
 **/
public interface IDsEtlNodeService {
    /**
     * 生成节点编码
     */
    DsNodeGenCodeRespDTO genCode(Long projectCode);
}
