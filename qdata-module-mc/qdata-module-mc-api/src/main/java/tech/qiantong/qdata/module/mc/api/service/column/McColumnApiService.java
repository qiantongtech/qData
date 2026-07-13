package tech.qiantong.qdata.module.mc.api.service.column;


import tech.qiantong.qdata.module.mc.api.column.dto.McColumnRespDTO;

import java.util.Collection;
import java.util.List;

/**
 * Metadata field information Service interface
 *
 * @author qdata
 * @date 2025-12-18
 */
public interface McColumnApiService {

    boolean existsByDataElemIds(Collection<Long> dataElemIds);

    /**
     * Query field information based on table id
     * @param tableId
     * @return
     */
    List<McColumnRespDTO> listByTableId(Long tableId);

}
