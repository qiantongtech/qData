package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskNodeRelLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据集成任务节点关系-日志Service接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlTaskNodeRelLogService extends IService<DppEtlTaskNodeRelLogDO> {

    /**
     * 获得数据集成任务节点关系-日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成任务节点关系-日志分页列表
     */
    PageResult<DppEtlTaskNodeRelLogDO> getDppEtlTaskNodeRelLogPage(DppEtlTaskNodeRelLogPageReqVO pageReqVO);
    List<DppEtlTaskNodeRelLogRespVO> getDppEtlTaskNodeRelLogRespVOList(DppEtlTaskNodeRelLogPageReqVO pageReqVO);
    DppEtlTaskNodeRelLogRespVO getDppEtlTaskNodeRelLogById(DppEtlTaskNodeRelLogPageReqVO pageReqVO);

    /**
     * 创建数据集成任务节点关系-日志
     *
     * @param createReqVO 数据集成任务节点关系-日志信息
     * @return 数据集成任务节点关系-日志编号
     */
    Long createDppEtlTaskNodeRelLog(DppEtlTaskNodeRelLogSaveReqVO createReqVO);

    void createDppEtlTaskNodeRelLogBatch(List<DppEtlTaskNodeRelLogSaveReqVO> dppEtlTaskNodeRelLogSaveReqVOS);

    /**
     * 更新数据集成任务节点关系-日志
     *
     * @param updateReqVO 数据集成任务节点关系-日志信息
     */
    int updateDppEtlTaskNodeRelLog(DppEtlTaskNodeRelLogSaveReqVO updateReqVO);

    /**
     * 删除数据集成任务节点关系-日志
     *
     * @param idList 数据集成任务节点关系-日志编号
     */
    int removeDppEtlTaskNodeRelLog(Collection<Long> idList);

    /**
     * 获得数据集成任务节点关系-日志详情
     *
     * @param id 数据集成任务节点关系-日志编号
     * @return 数据集成任务节点关系-日志
     */
    DppEtlTaskNodeRelLogDO getDppEtlTaskNodeRelLogById(Long id);

    /**
     * 获得全部数据集成任务节点关系-日志列表
     *
     * @return 数据集成任务节点关系-日志列表
     */
    List<DppEtlTaskNodeRelLogDO> getDppEtlTaskNodeRelLogList();

    /**
     * 获得全部数据集成任务节点关系-日志 Map
     *
     * @return 数据集成任务节点关系-日志 Map
     */
    Map<Long, DppEtlTaskNodeRelLogDO> getDppEtlTaskNodeRelLogMap();


    /**
     * 导入数据集成任务节点关系-日志数据
     *
     * @param importExcelList 数据集成任务节点关系-日志数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppEtlTaskNodeRelLog(List<DppEtlTaskNodeRelLogRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
