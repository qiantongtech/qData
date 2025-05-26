package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据集成任务-日志Service接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlTaskLogService extends IService<DppEtlTaskLogDO> {

    /**
     * 获得数据集成任务-日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成任务-日志分页列表
     */
    PageResult<DppEtlTaskLogDO> getDppEtlTaskLogPage(DppEtlTaskLogPageReqVO pageReqVO);

    DppEtlTaskLogRespVO getDppEtlTaskLogById(DppEtlTaskLogPageReqVO pageReqVO);

    /**
     * 创建数据集成任务-日志
     *
     * @param createReqVO 数据集成任务-日志信息
     * @return 数据集成任务-日志编号
     */
    Long createDppEtlTaskLog(DppEtlTaskLogSaveReqVO createReqVO);

    /**
     * 更新数据集成任务-日志
     *
     * @param updateReqVO 数据集成任务-日志信息
     */
    int updateDppEtlTaskLog(DppEtlTaskLogSaveReqVO updateReqVO);

    /**
     * 删除数据集成任务-日志
     *
     * @param idList 数据集成任务-日志编号
     */
    int removeDppEtlTaskLog(Collection<Long> idList);

    /**
     * 获得数据集成任务-日志详情
     *
     * @param id 数据集成任务-日志编号
     * @return 数据集成任务-日志
     */
    DppEtlTaskLogDO getDppEtlTaskLogById(Long id);

    /**
     * 获得全部数据集成任务-日志列表
     *
     * @return 数据集成任务-日志列表
     */
    List<DppEtlTaskLogDO> getDppEtlTaskLogList();

    /**
     * 获得全部数据集成任务-日志 Map
     *
     * @return 数据集成任务-日志 Map
     */
    Map<Long, DppEtlTaskLogDO> getDppEtlTaskLogMap();


    /**
     * 导入数据集成任务-日志数据
     *
     * @param importExcelList 数据集成任务-日志数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDppEtlTaskLog(List<DppEtlTaskLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 根据任务编码获取最大版本号
     *
     * @param taskCode
     * @return
     */
    Integer queryMaxVersionByCode(String taskCode);
}
