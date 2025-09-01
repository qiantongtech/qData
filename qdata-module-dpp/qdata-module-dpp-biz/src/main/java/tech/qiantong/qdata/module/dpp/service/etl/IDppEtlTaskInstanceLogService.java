package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceLogDO;

/**
 * 数据集成任务实例-日志Service接口
 *
 * @author qdata
 * @date 2025-08-05
 */
public interface IDppEtlTaskInstanceLogService extends IService<DppEtlTaskInstanceLogDO> {


    String getLog(Long taskInstanceId);
}
