package tech.qiantong.qdata.module.mc.service.task.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
public class McTableTxService {

    /**
     * 单表事务：
     * - 每次调用 = 一个新事务
     * - 抛异常 = 只回滚当前表
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T runInNewTx(Supplier<T> supplier) {
        return supplier.get(); // 抛异常 = 回滚
    }
}
