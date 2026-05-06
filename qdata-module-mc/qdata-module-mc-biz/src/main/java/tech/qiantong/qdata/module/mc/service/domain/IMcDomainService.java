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
 * 业务域Service接口
 *
 * @author qdata
 * @date 2026-02-12
 */
public interface IMcDomainService extends IService<McDomainDO> {

    /**
     * 获得业务域分页列表
     *
     * @param pageReqVO 分页请求
     * @return 业务域分页列表
     */
    PageResult<McDomainDO> getMcDomainPage(McDomainPageReqVO pageReqVO);

    /**
     * 创建业务域
     *
     * @param createReqVO 业务域信息
     * @return 业务域编号
     */
    Long createMcDomain(McDomainSaveReqVO createReqVO);

    /**
     * 更新业务域
     *
     * @param updateReqVO 业务域信息
     */
    int updateMcDomain(McDomainSaveReqVO updateReqVO);

    /**
     * 删除业务域
     *
     * @param idList 业务域编号
     */
    int removeMcDomain(Collection<Long> idList);

    /**
     * 获得业务域详情
     *
     * @param id 业务域编号
     * @return 业务域
     */
    McDomainDO getMcDomainById(Long id);

    /**
     * 获得全部业务域列表
     *
     * @return 业务域列表
     */
    List<McDomainDO> getMcDomainList(McDomainPageReqVO mcDomain);

    /**
     * 获得全部业务域 Map
     *
     * @return 业务域 Map
     */
    Map<Long, McDomainDO> getMcDomainMap();

    /**
     * 批量删除检查,查询可删除数和不可删除数
     * @param ids
     * @return
     */
    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> ids);
}
