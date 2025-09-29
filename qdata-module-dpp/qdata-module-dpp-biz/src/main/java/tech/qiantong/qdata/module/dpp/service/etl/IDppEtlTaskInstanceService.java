package tech.qiantong.qdata.module.dpp.service.etl;

import java.util.List;
import java.util.Map;
import java.util.Collection;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.api.ds.api.etl.ds.ProcessInstance;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.ExecuteType;
import tech.qiantong.qdata.module.dpp.api.etl.dto.DppEtlTaskInstanceLogStatusRespDTO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.*;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceDO;

/**
 * 数据集成任务实例Service接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlTaskInstanceService extends IService<DppEtlTaskInstanceDO> {

    /**
     * 获得数据集成任务实例分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成任务实例分页列表
     */
    PageResult<DppEtlTaskInstanceDO> getDppEtlTaskInstancePage(DppEtlTaskInstancePageReqVO pageReqVO);

    DppEtlTaskInstanceRespVO getDppEtlTaskInstanceById(DppEtlTaskInstancePageReqVO pageReqVO);

    /**
     * 创建数据集成任务实例
     *
     * @param createReqVO 数据集成任务实例信息
     * @return 数据集成任务实例编号
     */
    Long createDppEtlTaskInstance(DppEtlTaskInstanceSaveReqVO createReqVO);

    /**
     * 更新数据集成任务实例
     *
     * @param updateReqVO 数据集成任务实例信息
     */
    int updateDppEtlTaskInstance(DppEtlTaskInstanceSaveReqVO updateReqVO);

    /**
     * 删除数据集成任务实例
     *
     * @param idList 数据集成任务实例编号
     */
    int removeDppEtlTaskInstance(Collection<Long> idList);

    /**
     * 获得数据集成任务实例详情
     *
     * @param id 数据集成任务实例编号
     * @return 数据集成任务实例
     */
    DppEtlTaskInstanceDO getDppEtlTaskInstanceById(Long id);

    /**
     * 获得全部数据集成任务实例列表
     *
     * @return 数据集成任务实例列表
     */
    List<DppEtlTaskInstanceDO> getDppEtlTaskInstanceList();

    /**
     * 获得全部数据集成任务实例 Map
     *
     * @return 数据集成任务实例 Map
     */
    Map<Long, DppEtlTaskInstanceDO> getDppEtlTaskInstanceMap();


    /**
     * 导入数据集成任务实例数据
     *
     * @param importExcelList 数据集成任务实例数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDppEtlTaskInstance(List<DppEtlTaskInstanceRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 创建任务实例
     *
     * @param processInstance
     */
    Boolean createTaskInstance(ProcessInstance processInstance);

    /**
     * 更新任务实例
     *
     * @param processInstance
     * @return
     */
    Boolean updateTaskInstance(ProcessInstance processInstance);

    /**
     * 通过dsId获取数据
     *
     * @param dsId
     * @return
     */
    DppEtlTaskInstanceDO getByDsId(Long dsId);

    /**
     * 根据ds流程实例id获取中任务实例的id
     *
     * @param dsId
     * @return
     */
    Long getIdByDsId(Long dsId);

    PageResult<DppEtlTaskInstanceTreeListRespVO> treeList(DppEtlTaskInstanceTreeListReqVO reqVO);

    /**
     * 执行命令
     *
     * @param taskInstanceId
     * @param executeType
     * @return
     */
    AjaxResult execute(Long taskInstanceId, ExecuteType executeType);

    /**
     * 获取子任务列表
     *
     * @param taskInstanceId
     * @param nodeInstanceId
     * @return
     */
    List<DppEtlTaskInstanceTreeListRespVO> subNodelist(Long taskInstanceId, Long nodeInstanceId);

    /**
     * 通过实例id获取日志
     * @param taskInstanceId
     * @return
     */
    DppEtlTaskInstanceLogStatusRespDTO getLogByTaskInstanceId(Long taskInstanceId);

    /**
     * 获取正在运行的任务实例
     * @param taskId
     * @return
     */
    Long getRunTaskInstance(Long taskId);

    /**
     * 根据任务实例id获取数据集成任务详细信息
     * @param id
     * @return
     */
    DppEtlTaskUpdateQueryRespVO getTaskInfo(Long id);

    /**
     * 根据任务code获取最后一次任务实例
     * @param code
     * @return
     */
    DppEtlTaskInstanceDO getLastTaskInstanceByTaskCode(String code);
}
