package tech.qiantong.qdata.module.mc.service.task.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
public class McTableTxService {

    /**
     * Single table transaction:
     * - each call = a new transaction
     * - Throw exception = rollback only current table
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> T runInNewTx(Supplier<T> supplier) {
        return supplier.get(); // Throw exception = rollback
    }
}
