package tech.qiantong.qdata.module.dpp.service.etl.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceLogDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlTaskInstanceLogMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlTaskInstanceLogService;

import javax.annotation.Resource;

/**
 * 数据集成任务实例-日志Service业务层处理
 *
 * @author qdata
 * @date 2025-08-05
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlTaskInstanceLogServiceImpl extends ServiceImpl<DppEtlTaskInstanceLogMapper, DppEtlTaskInstanceLogDO> implements IDppEtlTaskInstanceLogService {
    @Resource
    private DppEtlTaskInstanceLogMapper dppEtlTaskInstanceLogMapper;

    @Override
    public boolean saveOrUpdate(DppEtlTaskInstanceLogDO entity) {
        DppEtlTaskInstanceLogDO old = this.getOne(Wrappers.lambdaQuery(DppEtlTaskInstanceLogDO.class)
                .eq(DppEtlTaskInstanceLogDO::getTaskInstanceId, entity.getTaskInstanceId()));
        if (old != null) {
            old.setTm(entity.getTm());
            old.setLogContent(entity.getLogContent());
            return this.update(old, Wrappers.lambdaUpdate(DppEtlTaskInstanceLogDO.class)
                    .eq(DppEtlTaskInstanceLogDO::getTaskInstanceId, entity.getTaskInstanceId()));
        } else {
            return this.save(entity);
        }
    }

    @Override
    public String getLog(Long taskInstanceId) {
        DppEtlTaskInstanceLogDO dppEtlTaskInstanceLogDO = this.getOne(Wrappers.lambdaQuery(DppEtlTaskInstanceLogDO.class)
                .eq(DppEtlTaskInstanceLogDO::getTaskInstanceId, taskInstanceId));
        if (dppEtlTaskInstanceLogDO != null) {
            return dppEtlTaskInstanceLogDO.getLogContent();
        }
        return null;
    }
}
