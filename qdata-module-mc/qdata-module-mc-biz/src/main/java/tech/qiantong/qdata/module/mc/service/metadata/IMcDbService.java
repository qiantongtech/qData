package tech.qiantong.qdata.module.mc.service.metadata;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbSaveReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McMetaSearchRespDTO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Database Service interface
 *
 * @author qdata
 * @date 2026-02-11
 */
public interface IMcDbService extends IService<McDbDO> {

    /**
     * Get the database paging list
     *
     * @param pageReqVO paging request
     * @return database paging list
     */
    PageResult<McDbDO> getMcDbPage(McDbPageReqVO pageReqVO);

    /**
     * Create database
     *
     * @param createReqVO database information
     * @return database number
     */
    Long createMcDb(McDbSaveReqVO createReqVO);

    /**
     * Update database
     *
     * @param updateReqVO database information
     */
    int updateMcDb(McDbSaveReqVO updateReqVO);

    /**
     * Delete database
     *
     * @param idList database number
     */
    int removeMcDb(Collection<Long> idList);

    /**
     * Get database details
     *
     * @param id database number
     * @return database
     */
    McDbRespVO getMcDbById(Long id);

    /**
     * Get a list of all databases
     *
     * @return database list
     */
    List<McDbDO> getMcDbList(McDbPageReqVO mcDb);

    /**
     * Get all database maps
     *
     * @return database map
     */
    Map<Long, McDbDO> getMcDbMap();


    Integer toggle(Long id, String status);

    Integer editPortalVisible(Long id, String portalVisible);
    List<McDbRespVO> getMcDbByTaskId(Long taskId);

    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> list);

    PageResult<McMetaSearchRespDTO> selectMetaSearchPage(McMetaSearchRespDTO mdMetaSearchRespDTO);
}
