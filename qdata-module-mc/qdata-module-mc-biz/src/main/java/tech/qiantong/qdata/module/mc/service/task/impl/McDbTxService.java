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
     * Library-level metadata: independent transaction, commit immediately
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long createDbAndCommit(McDbSaveReqVO dbScope) {
        try {
            return mcDbApiService.createMcDb(dbScope);
        } catch (Exception e) {
            // Only affects the current library and does not throw it out.
            return null;
        }
    }
}
