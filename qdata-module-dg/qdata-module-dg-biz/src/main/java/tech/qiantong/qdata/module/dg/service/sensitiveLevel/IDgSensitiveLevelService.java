package tech.qiantong.qdata.module.dg.service.sensitiveLevel;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dg.controller.admin.sensitiveLevel.vo.DgSensitiveLevelPageReqVO;
import tech.qiantong.qdata.module.dg.controller.admin.sensitiveLevel.vo.DgSensitiveLevelSaveReqVO;
import tech.qiantong.qdata.module.dg.dal.dataobject.sensitiveLevel.DgSensitiveLevelDO;

import java.util.Collection;

/**
 * Sensitive Level Service Interface
 *
 * @author Chaos
 * @date 2025-01-21
 */
public interface IDgSensitiveLevelService extends IService<DgSensitiveLevelDO> {

    /**
     * Get sensitive level paginated list
     *
     * @param pageReqVO Pagination request
     * @return Sensitive level paginated list
     */
    PageResult<DgSensitiveLevelDO> getDgSensitiveLevelPage(DgSensitiveLevelPageReqVO pageReqVO);

    /**
     * Create sensitive level
     *
     * @param createReqVO Sensitive level information
     * @return Sensitive level ID
     */
    Long createDgSensitiveLevel(DgSensitiveLevelSaveReqVO createReqVO);

    /**
     * Update sensitive level
     *
     * @param updateReqVO Sensitive level information
     */
    int updateDgSensitiveLevel(DgSensitiveLevelSaveReqVO updateReqVO);

    /**
     * Delete sensitive level
     *
     * @param idList Sensitive level IDs
     */
    int removeDgSensitiveLevel(Collection<Long> idList);

    /**
     * Get sensitive level details
     *
     * @param id Sensitive level ID
     * @return Sensitive level
     */
    DgSensitiveLevelDO getDgSensitiveLevelById(Long id);

    /**
     * Update status
     *
     * @param id     Primary key
     * @param status Status value
     * @return
     */
    Boolean updateStatus(Long id, Long status);
}
