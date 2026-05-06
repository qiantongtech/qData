package tech.qiantong.qdata.module.mc.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskScopeDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 采集范围Service接口
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface IMcTaskScopeService extends IService<McTaskScopeDO> {

    /**
     * 获得采集范围分页列表
     *
     * @param pageReqVO 分页请求
     * @return 采集范围分页列表
     */
    PageResult<McTaskScopeDO> getMcTaskScopePage(McTaskScopePageReqVO pageReqVO);

    /**
     * 创建采集范围
     *
     * @param createReqVO 采集范围信息
     * @return 采集范围编号
     */
    Long createMcTaskScope(McTaskScopeSaveReqVO createReqVO);

    /**
     * 更新采集范围
     *
     * @param updateReqVO 采集范围信息
     */
    int updateMcTaskScope(McTaskScopeSaveReqVO updateReqVO);

    /**
     * 删除采集范围
     *
     * @param idList 采集范围编号
     */
    int removeMcTaskScope(Collection<Long> idList);

    /**
     * 获得采集范围详情
     *
     * @param id 采集范围编号
     * @return 采集范围
     */
    McTaskScopeDO getMcTaskScopeById(Long id);

    /**
     * 获得全部采集范围列表
     *
     * @return 采集范围列表
     */
    List<McTaskScopeDO> getMcTaskScopeList();

    List<McTaskScopeDO> getMcTaskScopeListBytaskId(Long taskId);

    /**
     * 获得全部采集范围 Map
     *
     * @return 采集范围 Map
     */
    Map<Long, McTaskScopeDO> getMcTaskScopeMap();


    /**
     * 导入采集范围数据
     *
     * @param importExcelList 采集范围数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importMcTaskScope(List<McTaskScopeRespVO> importExcelList, boolean isUpdateSupport, String operName);

    void removeMcTaskScopeBytaskId(Long taskId);
}
