package tech.qiantong.qdata.api.ds.api.service.etl;

import tech.qiantong.qdata.api.ds.api.base.DsStatusRespDTO;
import tech.qiantong.qdata.api.ds.api.etl.DSExecuteDTO;

/**
 * <P>
 * 用途:执行相关相关接口
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-27 14:29
 **/
public interface IDsEtlExecutorService {
    /**
     * 执行命令
     *
     * @param dsExecuteDTO
     * @param projectCode
     * @return
     */
    DsStatusRespDTO execute(DSExecuteDTO dsExecuteDTO, String projectCode);
}
