package tech.qiantong.qdata.module.mc.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskSchedulerDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据集成调度信息Service接口
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface IMcTaskSchedulerService extends IService<McTaskSchedulerDO> {

    /**
     * 获得数据集成调度信息分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成调度信息分页列表
     */
    PageResult<McTaskSchedulerDO> getMcTaskSchedulerPage(McTaskSchedulerPageReqVO pageReqVO);

    /**
     * 创建数据集成调度信息
     *
     * @param createReqVO 数据集成调度信息信息
     * @return 数据集成调度信息编号
     */
    Long createMcTaskScheduler(McTaskSchedulerSaveReqVO createReqVO);

    /**
     * 更新数据集成调度信息
     *
     * @param updateReqVO 数据集成调度信息信息
     */
    int updateMcTaskScheduler(McTaskSchedulerSaveReqVO updateReqVO);

    /**
     * 删除数据集成调度信息
     *
     * @param idList 数据集成调度信息编号
     */
    int removeMcTaskScheduler(Collection<Long> idList);

    /**
     * 获得数据集成调度信息详情
     *
     * @param id 数据集成调度信息编号
     * @return 数据集成调度信息
     */
    McTaskSchedulerDO getMcTaskSchedulerById(Long id);

    McTaskSchedulerDO getMcTaskSchedulerBytaskId(Long taskId);

    /**
     * 获得全部数据集成调度信息列表
     *
     * @return 数据集成调度信息列表
     */
    List<McTaskSchedulerDO> getMcTaskSchedulerList();

    /**
     * 获得全部数据集成调度信息 Map
     *
     * @return 数据集成调度信息 Map
     */
    Map<Long, McTaskSchedulerDO> getMcTaskSchedulerMap();


    /**
     * 导入数据集成调度信息数据
     *
     * @param importExcelList 数据集成调度信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importMcTaskScheduler(List<McTaskSchedulerRespVO> importExcelList, boolean isUpdateSupport, String operName);


    void updateReleaseSchedule(McTaskSchedulerSaveReqVO updateReqVO);
}
