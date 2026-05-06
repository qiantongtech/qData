package tech.qiantong.qdata.module.mc.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceLogDO;

import java.util.Collection;
import java.util.List;

/**
 * 采集任务实例-日志Service接口
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface IMcTaskInstanceLogService extends IService<McTaskInstanceLogDO> {

    /**
     * 获得采集任务实例-日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return 采集任务实例-日志分页列表
     */
    PageResult<McTaskInstanceLogDO> getMcTaskInstanceLogPage(McTaskInstanceLogPageReqVO pageReqVO);

    /**
     * 创建采集任务实例-日志
     *
     * @param createReqVO 采集任务实例-日志信息
     * @return 采集任务实例-日志编号
     */
    Long createMcTaskInstanceLog(McTaskInstanceLogSaveReqVO createReqVO);

    /**
     * 更新采集任务实例-日志
     *
     * @param updateReqVO 采集任务实例-日志信息
     */
    int updateMcTaskInstanceLog(McTaskInstanceLogSaveReqVO updateReqVO);

    /**
     * 删除采集任务实例-日志
     *
     * @param idList 采集任务实例-日志编号
     */
    int removeMcTaskInstanceLog(Collection<Long> idList);

    /**
     * 获得采集任务实例-日志详情
     *
     * @param id 采集任务实例-日志编号
     * @return 采集任务实例-日志
     */
    McTaskInstanceLogDO getMcTaskInstanceLogById(Long id);

    /**
     * 获得全部采集任务实例-日志列表
     *
     * @return 采集任务实例-日志列表
     */
    List<McTaskInstanceLogDO> getMcTaskInstanceLogList();

    void taskInstanceLogAppend(Long taskInstanceId, Long taskId, String logStr);

    int saveOrUpdateByPk(McTaskInstanceLogDO entity);
}
