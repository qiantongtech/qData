package tech.qiantong.qdata.module.mc.service.tableColumnRelLog;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableColumnRelLog.McTableColumnRelLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Metadata database and information and field information relationship - Log Service interface
 *
 * @author qdata
 * @date 2026-03-10
 */
public interface IMcTableColumnRelLogService extends IService<McTableColumnRelLogDO> {

    /**
     * Obtain the relationship between metadata database and information and field information - log paging list
     *
     * @param pageReqVO paging request
     * @return Metadata database and information and field information relationship - log paging list
     */
    PageResult<McTableColumnRelLogDO> getMcTableColumnRelLogPage(McTableColumnRelLogPageReqVO pageReqVO);

    /**
     * Create metadata database and information and field information relationships-log
     *
     * @param createReqVO Metadata database and information and field information relationship - log information
     * @return Metadata database and information and field information relationship - log number
     */
    Long createMcTableColumnRelLog(McTableColumnRelLogSaveReqVO createReqVO);

    /**
     * Update metadata database and information and field information relationships-log
     *
     * @param updateReqVO Metadata database and information and field information relationship - log information
     */
    int updateMcTableColumnRelLog(McTableColumnRelLogSaveReqVO updateReqVO);

    /**
     * Delete metadata database and information and field information relationships-log
     *
     * @param idList Metadata database and information and field information relationship - log number
     */
    int removeMcTableColumnRelLog(Collection<Long> idList);

    /**
     * Obtain metadata database and information and field information relationships - log details
     *
     * @param id Metadata database and information and field information relationship - log number
     * @return Metadata database and information and field information relationship-log
     */
    McTableColumnRelLogDO getMcTableColumnRelLogById(Long id);

    /**
     * Obtain all metadata databases and information and field information relationships - log list
     *
     * @return Metadata database and information and field information relationship - log list
     */
    List<McTableColumnRelLogDO> getMcTableColumnRelLogList();

    /**
     * Obtain all metadata databases and information and field information relationships-Log Map
     *
     * @return Metadata database and information and field information relationship-Log Map
     */
    Map<Long, McTableColumnRelLogDO> getMcTableColumnRelLogMap();


}
