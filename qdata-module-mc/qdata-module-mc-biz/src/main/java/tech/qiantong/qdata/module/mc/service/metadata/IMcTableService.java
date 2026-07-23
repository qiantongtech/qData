package tech.qiantong.qdata.module.mc.service.metadata;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTablePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McTableDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Metadata information Service interface
 *
 * @author qdata
 * @date 2026-02-11
 */
public interface IMcTableService extends IService<McTableDO> {

    /**
     * Get a paginated list of metadata information
     *
     * @param pageReqVO paging request
     * @return metadata information paginated list
     */
    PageResult<McTableDO> getMcTablePage(McTablePageReqVO pageReqVO);

    PageResult<McTableRespVO> getMcTablePageAsset(McTablePageReqVO mcTable);

    List<McTableRespVO> getMcTableListAsset(McTablePageReqVO mcTable);

    /**
     * Create metadata information
     *
     * @param createReqVO metadata information
     * @return metadata information number
     */
    Long createMcTable(McTableSaveReqVO createReqVO);

    /**
     * Update metadata information
     *
     * @param updateReqVO metadata information
     */
    int updateMcTable(McTableSaveReqVO updateReqVO);

    /**
     * Delete metadata information
     *
     * @param idList metadata information number
     */
    int removeMcTable(Collection<Long> idList);

    /**
     * Get metadata information details
     *
     * @param id metadata information number
     * @return metadata information
     */
    McTableRespVO getMcTableById(Long id);
  //  McTableDO getMcTableById(Long id);
   List<McTableRespVO> getMcTableById(McTableRespVO mcTableRespVO);

    /**
     * Get a list of all metadata information
     *
     * @return metadata information list
     */
    List<McTableDO> getMcTableList();

    /**
     * Get all metadata information Map
     *
     * @return metadata information Map
     */
    Map<Long, McTableDO> getMcTableMap();


    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> list);
    List<McTableRespVO> getMcTableByDbId(Collection<Long> idList);

    Long saveDraft(McTableSaveReqVO saveReqVO);


    /**
     * Disable
     *
     * @param id
     * @param status
     * @return
     */
    Integer toggle(Long id, String status);
}
