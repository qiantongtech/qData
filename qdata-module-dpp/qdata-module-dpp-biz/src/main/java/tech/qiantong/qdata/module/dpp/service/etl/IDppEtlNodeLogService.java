package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据集成节点-日志Service接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlNodeLogService extends IService<DppEtlNodeLogDO> {

    /**
     * 获得数据集成节点-日志分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成节点-日志分页列表
     */
    PageResult<DppEtlNodeLogDO> getDppEtlNodeLogPage(DppEtlNodeLogPageReqVO pageReqVO);

    DppEtlNodeLogDO getDppEtlNodeLogRespVOByReqVO(DppEtlNodeLogPageReqVO reqVO);

    /**
     * 创建数据集成节点-日志
     *
     * @param createReqVO 数据集成节点-日志信息
     * @return 数据集成节点-日志编号
     */
    Long createDppEtlNodeLog(DppEtlNodeLogSaveReqVO createReqVO);

    DppEtlNodeLogDO createDppEtlNodeLogNew(DppEtlNodeLogSaveReqVO dppEtlNodeLogSaveReqVO);

    List<DppEtlNodeLogDO> createDppEtlNodeLogBatch(List<DppEtlNodeLogSaveReqVO> dppEtlNodeLogSaveReqVOS);

    /**
     * 更新数据集成节点-日志
     *
     * @param updateReqVO 数据集成节点-日志信息
     */
    int updateDppEtlNodeLog(DppEtlNodeLogSaveReqVO updateReqVO);

    /**
     * 删除数据集成节点-日志
     *
     * @param idList 数据集成节点-日志编号
     */
    int removeDppEtlNodeLog(Collection<Long> idList);

    /**
     * 获得数据集成节点-日志详情
     *
     * @param id 数据集成节点-日志编号
     * @return 数据集成节点-日志
     */
    DppEtlNodeLogDO getDppEtlNodeLogById(Long id);

    /**
     * 获得全部数据集成节点-日志列表
     *
     * @return 数据集成节点-日志列表
     */
    List<DppEtlNodeLogDO> getDppEtlNodeLogList();

    /**
     * 获得全部数据集成节点-日志 Map
     *
     * @return 数据集成节点-日志 Map
     */
    Map<Long, DppEtlNodeLogDO> getDppEtlNodeLogMap();


    /**
     * 导入数据集成节点-日志数据
     *
     * @param importExcelList 数据集成节点-日志数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDppEtlNodeLog(List<DppEtlNodeLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 根据编码和版本获取节点信息
     *
     * @param nodeCode
     * @param version
     * @return
     */
    DppEtlNodeLogDO getByNodeCodeAndVersion(String nodeCode, Integer version);

    /**
     * 通过节点编码获取最大版本号
     *
     * @param nodeCode
     * @return
     */
    Integer getMaxVersionByNodeCode(String nodeCode);


    /**
     * 根据任务编码及任务版本获取节点信息
     *
     * @param taskCode
     * @param version
     * @return
     */
    List<DppEtlNodeLogDO> listByTaskCode(String taskCode, Integer version);
}
