package tech.qiantong.qdata.module.mc.service.tableLog;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableSaveReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableLog.McTableLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Metadata information - Log Service interface
 *
 * @author qdata
 * @date 2026-03-10
 */
public interface IMcTableLogService extends IService<McTableLogDO> {

    /**
     * Get metadata information - log paginated list
     *
     * @param pageReqVO paging request
     * @return metadata information - log paging list
     */
    PageResult<McTableLogDO> getMcTableLogPage(McTableLogPageReqVO pageReqVO);

    /**
     * Create metadata information - log
     *
     * @param createReqVO metadata information - log information
     * @return metadata information - log number
     */
    Long createMcTableLog(McTableLogSaveReqVO createReqVO);

    /**
     * Update metadata information - log
     *
     * @param updateReqVO metadata information - log information
     */
    int updateMcTableLog(McTableLogSaveReqVO updateReqVO);

    /**
     * Delete metadata information - Log
     *
     * @param idList metadata information - log number
     */
    int removeMcTableLog(Collection<Long> idList);

    /**
     * Get metadata information - log details
     *
     * @param id metadata information - log number
     * @return metadata information - log
     */
    McTableLogDO getMcTableLogById(Long id);

    /**
     * Get all metadata information - log list
     *
     * @return metadata information - log list
     */
    List<McTableLogDO> getMcTableLogList();

    /**
     * Get all metadata information - Log Map
     *
     * @return metadata information - log map
     */
    Map<Long, McTableLogDO> getMcTableLogMap();

    /**
     * Add metadata version change log based on metadata table information
     * @param table metadata table information
     * @return metadata version change log id
     */
    Long createMcTableLog(McTableSaveReqVO table);
}
