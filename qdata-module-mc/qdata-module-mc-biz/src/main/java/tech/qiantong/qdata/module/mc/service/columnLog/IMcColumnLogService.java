package tech.qiantong.qdata.module.mc.service.columnLog;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.columnLog.McColumnLogDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McColumnDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Metadata field information - Log Service interface
 *
 * @author qdata
 * @date 2026-03-10
 */
public interface IMcColumnLogService extends IService<McColumnLogDO> {

    /**
     * Get metadata field information - log paginated list
     *
     * @param pageReqVO paging request
     * @return metadata field information - log paging list
     */
    PageResult<McColumnLogDO> getMcColumnLogPage(McColumnLogPageReqVO pageReqVO);

    /**
     * Create metadata field information - log
     *
     * @param createReqVO metadata field information - log information
     * @return metadata field information - log number
     */
    Long createMcColumnLog(McColumnLogSaveReqVO createReqVO);

    /**
     * Create metadata field information in batches - Log
     *
     * @param columnDOList metadata field information list
     * @return metadata field information
     */
    Long createMcColumnLog(List<McColumnDO> columnDOList);

    /**
     * Update metadata field information - Log
     *
     * @param updateReqVO metadata field information - log information
     */
    int updateMcColumnLog(McColumnLogSaveReqVO updateReqVO);

    /**
     * Delete metadata field information - Log
     *
     * @param idList metadata field information - log number
     */
    int removeMcColumnLog(Collection<Long> idList);

    /**
     * Get metadata field information - log details
     *
     * @param id metadata field information - log number
     * @return metadata field information - log
     */
    McColumnLogDO getMcColumnLogById(Long id);

    /**
     * Get all metadata field information - log list
     *
     * @return metadata field information - log list
     */
    List<McColumnLogDO> getMcColumnLogList();

    /**
     * Get all metadata field information - Log Map
     *
     * @return metadata field information - Log Map
     */
    Map<Long, McColumnLogDO> getMcColumnLogMap();


}
