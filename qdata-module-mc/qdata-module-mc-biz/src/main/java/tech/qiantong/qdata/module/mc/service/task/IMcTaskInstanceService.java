package tech.qiantong.qdata.module.mc.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstancePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 采集任务实例Service接口
 *
 * @author qdata
 * @date 2025-12-16
 */
public interface IMcTaskInstanceService extends IService<McTaskInstanceDO> {

    /**
     * 获得采集任务实例分页列表
     *
     * @param pageReqVO 分页请求
     * @return 采集任务实例分页列表
     */
    PageResult<McTaskInstanceDO> getMcTaskInstancePage(McTaskInstancePageReqVO pageReqVO);

    /**
     * 创建采集任务实例
     *
     * @param createReqVO 采集任务实例信息
     * @return 采集任务实例编号
     */
    Long createMcTaskInstance(McTaskInstanceSaveReqVO createReqVO);
    Long createMcTaskInstance(McTaskInstanceDO createReqVO);

    /**
     * 更新采集任务实例
     *
     * @param updateReqVO 采集任务实例信息
     */
    int updateMcTaskInstance(McTaskInstanceSaveReqVO updateReqVO);

    /**
     * 删除采集任务实例
     *
     * @param idList 采集任务实例编号
     */
    int removeMcTaskInstance(Collection<Long> idList);

    /**
     * 获得采集任务实例详情
     *
     * @param id 采集任务实例编号
     * @return 采集任务实例
     */
    McTaskInstanceDO getMcTaskInstanceById(Long id);

    /**
     * 获得采集任务实例详情
     *
     * @param taskId
     * @return 采集任务实例
     */
    McTaskInstanceDO getMcTaskInstanceByTaskId(Long taskId);

    /**
     * 获得全部采集任务实例列表
     *
     * @return 采集任务实例列表
     */
    List<McTaskInstanceDO> getMcTaskInstanceList();

    /**
     * 获得全部采集任务实例 Map
     *
     * @return 采集任务实例 Map
     */
    Map<Long, McTaskInstanceDO> getMcTaskInstanceMap();


    /**
     * 导入采集任务实例数据
     *
     * @param importExcelList 采集任务实例数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importMcTaskInstance(List<McTaskInstanceRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
