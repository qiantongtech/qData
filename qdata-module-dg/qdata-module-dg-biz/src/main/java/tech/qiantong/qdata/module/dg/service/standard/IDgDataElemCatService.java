package tech.qiantong.qdata.module.dg.service.standard;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemCatPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.standard.vo.DgDataElemCatSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.standard.DgDataElemCatDO;

import java.util.Collection;
import java.util.List;

/**
 * Data Element Category Management Service Interface
 *
 * @author qdata
 * @date 2025-01-20
 */
public interface IDgDataElemCatService extends IService<DgDataElemCatDO> {

    /**
     * Create data element category management
     *
     * @param createReqVO Data element category management information
     * @return Data element category management ID
     */
    Long createDgDataElemCat(DgDataElemCatSaveReqVO createReqVO);

    /**
     * Update data element category management
     *
     * @param updateReqVO Data element category management information
     */
    int updateDgDataElemCat(DgDataElemCatSaveReqVO updateReqVO);

    /**
     * Delete data element category management
     *
     * @param idList Data element category management IDs
     */
    int removeDgDataElemCat(Collection<Long> idList);

    /**
     * Change all codes under the specified pid
     *
     * @param pid
     */
    void changeCodeByPid(Long pid, String parentCode);

    /**
     * Get data element category management details
     *
     * @param id Data element category management ID
     * @return Data element category management
     */
    DgDataElemCatDO getDgDataElemCatById(Long id);

    /**
     * Get all data element category management list
     *
     * @return Data element category management list
     */
    List<DgDataElemCatDO> getDgDataElemCatList(DgDataElemCatPageReqVO reqVO);

    /**
     * Generate code
     *
     * @param parentId
     * @param parentCode
     * @return
     */
    String createCode(Long parentId, String parentCode);

    /**
     * Batch delete check, query the number of deletable and non-deletable items
     *
     * @param ids
     * @return
     */
    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> ids);
}
