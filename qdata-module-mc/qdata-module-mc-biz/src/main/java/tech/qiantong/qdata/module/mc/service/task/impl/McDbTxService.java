package tech.qiantong.qdata.module.mc.service.task.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbSaveReqVO;
import tech.qiantong.qdata.module.mc.service.metadata.IMcDbService;

import javax.annotation.Resource;

@Service
public class McDbTxService {

    @Resource
    @Lazy
    private IMcDbService mcDbApiService;

    /**
     * 库级元数据：独立事务，立即提交
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long createDbAndCommit(McDbSaveReqVO dbScope) {
        try {
            return mcDbApiService.createMcDb(dbScope);
        } catch (Exception e) {
            // 只影响当前库，不向外抛
            return null;
        }
    }
}
