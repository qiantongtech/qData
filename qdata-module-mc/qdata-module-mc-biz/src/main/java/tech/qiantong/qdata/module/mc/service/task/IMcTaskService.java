package tech.qiantong.qdata.module.mc.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.BatchDeleteCheck;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSaveReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSourceTreeRespVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskDO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskScopeDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 采集任务Service接口
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface IMcTaskService extends IService<McTaskDO> {

    /**
     * 获得采集任务分页列表
     *
     * @param pageReqVO 分页请求
     * @return 采集任务分页列表
     */
    PageResult<McTaskDO> getMcTaskPage(McTaskPageReqVO pageReqVO);

    /**
     * 创建采集任务
     *
     * @param createReqVO 采集任务信息
     * @return 采集任务编号
     */
    Long createMcTask(McTaskSaveReqVO createReqVO);

    /**
     * 更新采集任务
     *
     * @param updateReqVO 采集任务信息
     */
    int updateMcTask(McTaskSaveReqVO updateReqVO);

    /**
     * 删除采集任务
     *
     * @param idList 采集任务编号
     */
    int removeMcTask(Collection<Long> idList);

    /**
     * 获得采集任务详情
     *
     * @param id 采集任务编号
     * @return 采集任务
     */
    McTaskDO getMcTaskById(Long id);
    McTaskRespVO getMcTaskByIdNew(Long id);

    /**
     * 获得全部采集任务列表
     *
     * @return 采集任务列表
     */
    List<McTaskDO> getMcTaskList();

    /**
     * 获得全部采集任务 Map
     *
     * @return 采集任务 Map
     */
    Map<Long, McTaskDO> getMcTaskMap();


    /**
     * 导入采集任务数据
     *
     * @param importExcelList 采集任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importMcTask(List<McTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);


    /**
     * 定时任务触发
     * @param taskId
     * @return
     */
    boolean runDaDiscoveryTask(Long taskId);

    /**
     * 获取实时采集范围
     *
     * @param id 数据源id
     * @return 实时采集范围列表
     */
    List<McTaskScopeDO> getRealtimeMcTaskScopeList(Long id);

    Map<String, Object> updateReleaseJobTask(McTaskSaveReqVO mcTask);

    Map<String, Object> updateReleaseSchedule(McTaskSaveReqVO mcTask);

    BatchDeleteCheck<Long> batchDeleteCheck(List<Long> list);

    Map<String, Object> runJobOnce(McTaskSaveReqVO mcTask);

    /**
     * 获取来源系统树形结构
     * 一级: 来源系统
     * 二级: 数据源
     * 三级: 数据库(根据采集模式展示)
     *
     * @return 树形结构列表
     */
    List<McTaskSourceTreeRespVO> getSourceSystemTree();
}
