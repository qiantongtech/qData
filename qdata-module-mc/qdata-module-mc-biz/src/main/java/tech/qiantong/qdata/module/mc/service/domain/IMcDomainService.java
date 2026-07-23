package tech.qiantong.qdata.module.mc.service.domain;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.domain.McDomainDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Business domain Service interface
 *
 * @author qdata
 * @date 2026-02-12
 */
public interface IMcDomainService extends IService<McDomainDO> {

    /**
     * Get a paginated list of business domains
     *
     * @param pageReqVO paging request
     * @return Business domain paging list
     */
    PageResult<McDomainDO> getMcDomainPage(McDomainPageReqVO pageReqVO);

    /**
     * Create business domain
     *
     * @param createReqVO business domain information
     * @return business domain number
     */
    Long createMcDomain(McDomainSaveReqVO createReqVO);

    /**
     * Update business domain
     *
     * @param updateReqVO business domain information
     */
    int updateMcDomain(McDomainSaveReqVO updateReqVO);

    /**
     * Delete business domain
     *
     * @param idList business domain number
     */
    int removeMcDomain(Collection<Long> idList);

    /**
     * Get business domain details
     *
     * @param id business domain number
     * @return business domain
     */
    McDomainDO getMcDomainById(Long id);

    /**
     * Get a list of all business domains
     *
     * @return business domain list
     */
    List<McDomainDO> getMcDomainList(McDomainPageReqVO mcDomain);

    /**
     * Get all business domain maps
     *
     * @return business domain map
     */
    Map<Long, McDomainDO> getMcDomainMap();

    /**
     * Batch deletion check, query the number that can be deleted and the number that cannot be deleted
     * @param ids
     * @return
     */
    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> ids);
}
