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
 * 元数据字段信息Service接口
 *
 * @author qdata
 * @date 2026-02-11
 */
public interface IMcColumnService extends IService<McColumnDO> {

    /**
     * 获得元数据字段信息分页列表
     *
     * @param pageReqVO 分页请求
     * @return 元数据字段信息分页列表
     */
    PageResult<McColumnDO> getMcColumnPage(McColumnPageReqVO pageReqVO);

    /**
     * 创建元数据字段信息
     *
     * @param createReqVO 元数据字段信息信息
     * @return 元数据字段信息编号
     */
    Long createMcColumn(McColumnSaveReqVO createReqVO);

    List<McColumnDO>  createMcColumnList(List<McColumnSaveReqVO> createReqVO);

    /**
     * 更新元数据字段信息
     *
     * @param updateReqVO 元数据字段信息信息
     */
    int updateMcColumn(McColumnSaveReqVO updateReqVO);

    /**
     * 删除元数据字段信息
     *
     * @param idList 元数据字段信息编号
     */
    int removeMcColumn(Collection<Long> idList);
    int removeMcColumn(McColumnRespVO mcColumnRespVO);

    /**
     * 获得元数据字段信息详情
     *
     * @param id 元数据字段信息编号
     * @return 元数据字段信息
     */
    McColumnRespVO getMcColumnById(Long id);

    /**
     * 获得全部元数据字段信息列表
     *
     * @return 元数据字段信息列表
     */
    List<McColumnDO> getMcColumnList();
    List<McColumnRespVO> getMcColumnList(McColumnRespVO mcColumnRespVO);

    /**
     * 获得全部元数据字段信息 Map
     *
     * @return 元数据字段信息 Map
     */
    Map<Long, McColumnDO> getMcColumnMap();


    List<McColumnDO> getMdColumnList(McColumnPageReqVO mdColumn);

    Integer createMdColumn(List<McColumnSaveReqVO> mdColumn);

    Integer saveDraft(List<McColumnSaveReqVO> saveReqVO);

    Integer toggle(Long id, String status);

    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> list);
}
