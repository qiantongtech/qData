package tech.qiantong.qdata.module.mc.service.metadata;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McColumnDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Metadata field information Service interface
 *
 * @author qdata
 * @date 2026-02-11
 */
public interface IMcColumnService extends IService<McColumnDO> {

    /**
     * Get a paginated list of metadata field information
     *
     * @param pageReqVO paging request
     * @return metadata field information paginated list
     */
    PageResult<McColumnDO> getMcColumnPage(McColumnPageReqVO pageReqVO);

    /**
     * Create metadata field information
     *
     * @param createReqVO metadata field information
     * @return metadata field information number
     */
    Long createMcColumn(McColumnSaveReqVO createReqVO);

    List<McColumnDO>  createMcColumnList(List<McColumnSaveReqVO> createReqVO);

    /**
     * Update metadata field information
     *
     * @param updateReqVO metadata field information information
     */
    int updateMcColumn(McColumnSaveReqVO updateReqVO);

    /**
     * Delete metadata field information
     *
     * @param idList metadata field information number
     */
    int removeMcColumn(Collection<Long> idList);
    int removeMcColumn(McColumnRespVO mcColumnRespVO);

    /**
     * Get metadata field information details
     *
     * @param id metadata field information number
     * @return metadata field information
     */
    McColumnRespVO getMcColumnById(Long id);

    /**
     * Get a list of all metadata field information
     *
     * @return metadata field information list
     */
    List<McColumnDO> getMcColumnList();
    List<McColumnRespVO> getMcColumnList(McColumnRespVO mcColumnRespVO);

    /**
     * Get all metadata field information Map
     *
     * @return Metadata field information Map
     */
    Map<Long, McColumnDO> getMcColumnMap();


    List<McColumnDO> getMdColumnList(McColumnPageReqVO mdColumn);

    Integer createMdColumn(List<McColumnSaveReqVO> mdColumn);

    Integer saveDraft(List<McColumnSaveReqVO> saveReqVO);

    Integer toggle(Long id, String status);

    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> list);
}
