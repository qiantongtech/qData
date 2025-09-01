package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.api.ds.api.etl.ds.TaskInstance;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据集成节点实例Service接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlNodeInstanceService extends IService<DppEtlNodeInstanceDO> {

    /**
     * 获得数据集成节点实例分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成节点实例分页列表
     */
    PageResult<DppEtlNodeInstanceDO> getDppEtlNodeInstancePage(DppEtlNodeInstancePageReqVO pageReqVO);

    /**
     * 创建数据集成节点实例
     *
     * @param createReqVO 数据集成节点实例信息
     * @return 数据集成节点实例编号
     */
    Long createDppEtlNodeInstance(DppEtlNodeInstanceSaveReqVO createReqVO);

    /**
     * 更新数据集成节点实例
     *
     * @param updateReqVO 数据集成节点实例信息
     */
    int updateDppEtlNodeInstance(DppEtlNodeInstanceSaveReqVO updateReqVO);

    /**
     * 删除数据集成节点实例
     *
     * @param idList 数据集成节点实例编号
     */
    int removeDppEtlNodeInstance(Collection<Long> idList);

    /**
     * 获得数据集成节点实例详情
     *
     * @param id 数据集成节点实例编号
     * @return 数据集成节点实例
     */
    DppEtlNodeInstanceDO getDppEtlNodeInstanceById(Long id);

    /**
     * 获得全部数据集成节点实例列表
     *
     * @return 数据集成节点实例列表
     */
    List<DppEtlNodeInstanceDO> getDppEtlNodeInstanceList();

    /**
     * 获得全部数据集成节点实例 Map
     *
     * @return 数据集成节点实例 Map
     */
    Map<Long, DppEtlNodeInstanceDO> getDppEtlNodeInstanceMap();


    /**
     * 导入数据集成节点实例数据
     *
     * @param importExcelList 数据集成节点实例数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDppEtlNodeInstance(List<DppEtlNodeInstanceRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 创建任务节点实例
     *
     * @param taskInstance
     * @return
     */
    Boolean createNodeInstance(TaskInstance taskInstance);

    /**
     * 更细任务节点实例
     *
     * @param taskInstance
     * @return
     */
    Boolean updateNodeInstance(TaskInstance taskInstance);

    /**
     * 根据dsId获取数据
     *
     * @param dsId
     * @return
     */
    DppEtlNodeInstanceDO getByDsId(Long dsId);

    /**
     * 节点实例日志的处理
     *
     * @param taskInstanceId
     * @param processInstanceId
     * @param logStr
     */
    void taskInstanceLogInsert(String taskInstanceId, String processInstanceId, String logStr);

    String getLogByNodeInstanceId(Long nodeInstanceId);
}
