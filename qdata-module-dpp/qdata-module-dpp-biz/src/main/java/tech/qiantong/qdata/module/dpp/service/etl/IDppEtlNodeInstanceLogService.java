package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceLogDO;

/**
 * 数据集成节点实例-日志Service接口
 *
 * @author qdata
 * @date 2025-08-05
 */
public interface IDppEtlNodeInstanceLogService extends IService<DppEtlNodeInstanceLogDO> {


    String getLog(Long nodeInstanceId);
}
