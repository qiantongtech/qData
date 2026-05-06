package tech.qiantong.qdata.module.mc.api.service.column;


import tech.qiantong.qdata.module.mc.api.column.dto.McColumnRespDTO;

import java.util.Collection;
import java.util.List;

/**
 * 元数据字段信息Service接口
 *
 * @author qdata
 * @date 2025-12-18
 */
public interface McColumnApiService {

    boolean existsByDataElemIds(Collection<Long> dataElemIds);

    /**
     * 根据表id查询字段信息
     * @param tableId
     * @return
     */
    List<McColumnRespDTO> listByTableId(Long tableId);

}
