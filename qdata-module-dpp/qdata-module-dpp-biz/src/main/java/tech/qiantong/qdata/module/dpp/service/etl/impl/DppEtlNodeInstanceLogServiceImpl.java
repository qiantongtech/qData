package tech.qiantong.qdata.module.dpp.service.etl.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceLogDO;
import tech.qiantong.qdata.module.dpp.dal.mapper.etl.DppEtlNodeInstanceLogMapper;
import tech.qiantong.qdata.module.dpp.service.etl.IDppEtlNodeInstanceLogService;

import javax.annotation.Resource;

/**
 * 数据集成节点实例-日志Service业务层处理
 *
 * @author qdata
 * @date 2025-08-05
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DppEtlNodeInstanceLogServiceImpl extends ServiceImpl<DppEtlNodeInstanceLogMapper, DppEtlNodeInstanceLogDO> implements IDppEtlNodeInstanceLogService {
    @Resource
    private DppEtlNodeInstanceLogMapper dppEtlNodeInstanceLogMapper;

    @Override
    public String getLog(Long nodeInstanceId) {
        DppEtlNodeInstanceLogDO dppEtlNodeInstanceLogDO = dppEtlNodeInstanceLogMapper.selectOne(Wrappers.lambdaQuery(DppEtlNodeInstanceLogDO.class)
                .eq(DppEtlNodeInstanceLogDO::getNodeInstanceId, nodeInstanceId));
        if (dppEtlNodeInstanceLogDO != null) {
            return dppEtlNodeInstanceLogDO.getLogContent();
        }
        return null;
    }
}
