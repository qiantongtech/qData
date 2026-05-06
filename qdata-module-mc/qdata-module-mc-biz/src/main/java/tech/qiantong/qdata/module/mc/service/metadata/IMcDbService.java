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
 * 数据库Service接口
 *
 * @author qdata
 * @date 2026-02-11
 */
public interface IMcDbService extends IService<McDbDO> {

    /**
     * 获得数据库分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据库分页列表
     */
    PageResult<McDbDO> getMcDbPage(McDbPageReqVO pageReqVO);

    /**
     * 创建数据库
     *
     * @param createReqVO 数据库信息
     * @return 数据库编号
     */
    Long createMcDb(McDbSaveReqVO createReqVO);

    /**
     * 更新数据库
     *
     * @param updateReqVO 数据库信息
     */
    int updateMcDb(McDbSaveReqVO updateReqVO);

    /**
     * 删除数据库
     *
     * @param idList 数据库编号
     */
    int removeMcDb(Collection<Long> idList);

    /**
     * 获得数据库详情
     *
     * @param id 数据库编号
     * @return 数据库
     */
    McDbRespVO getMcDbById(Long id);

    /**
     * 获得全部数据库列表
     *
     * @return 数据库列表
     */
    List<McDbDO> getMcDbList(McDbPageReqVO mcDb);

    /**
     * 获得全部数据库 Map
     *
     * @return 数据库 Map
     */
    Map<Long, McDbDO> getMcDbMap();


    Integer toggle(Long id, String status);

    Integer editPortalVisible(Long id, String portalVisible);
    List<McDbRespVO> getMcDbByTaskId(Long taskId);

    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> list);

    PageResult<McMetaSearchRespDTO> selectMetaSearchPage(McMetaSearchRespDTO mdMetaSearchRespDTO);
}
