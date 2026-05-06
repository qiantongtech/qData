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
 * 元数据信息Service接口
 *
 * @author qdata
 * @date 2026-02-11
 */
public interface IMcTableService extends IService<McTableDO> {

    /**
     * 获得元数据信息分页列表
     *
     * @param pageReqVO 分页请求
     * @return 元数据信息分页列表
     */
    PageResult<McTableDO> getMcTablePage(McTablePageReqVO pageReqVO);

    PageResult<McTableRespVO> getMcTablePageAsset(McTablePageReqVO mcTable);

    List<McTableRespVO> getMcTableListAsset(McTablePageReqVO mcTable);

    /**
     * 创建元数据信息
     *
     * @param createReqVO 元数据信息信息
     * @return 元数据信息编号
     */
    Long createMcTable(McTableSaveReqVO createReqVO);

    /**
     * 更新元数据信息
     *
     * @param updateReqVO 元数据信息信息
     */
    int updateMcTable(McTableSaveReqVO updateReqVO);

    /**
     * 删除元数据信息
     *
     * @param idList 元数据信息编号
     */
    int removeMcTable(Collection<Long> idList);

    /**
     * 获得元数据信息详情
     *
     * @param id 元数据信息编号
     * @return 元数据信息
     */
    McTableRespVO getMcTableById(Long id);
  //  McTableDO getMcTableById(Long id);
   List<McTableRespVO> getMcTableById(McTableRespVO mcTableRespVO);

    /**
     * 获得全部元数据信息列表
     *
     * @return 元数据信息列表
     */
    List<McTableDO> getMcTableList();

    /**
     * 获得全部元数据信息 Map
     *
     * @return 元数据信息 Map
     */
    Map<Long, McTableDO> getMcTableMap();


    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> list);
    List<McTableRespVO> getMcTableByDbId(Collection<Long> idList);

    Long saveDraft(McTableSaveReqVO saveReqVO);


    /**
     * 停启用
     *
     * @param id
     * @param status
     * @return
     */
    Integer toggle(Long id, String status);
}
