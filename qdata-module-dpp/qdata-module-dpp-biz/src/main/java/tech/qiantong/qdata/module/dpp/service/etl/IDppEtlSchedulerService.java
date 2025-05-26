package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSchedulerDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据集成调度信息Service接口
 *
 * @author qdata
 * @date 2025-02-13
 */
public interface IDppEtlSchedulerService extends IService<DppEtlSchedulerDO> {

    /**
     * 获得数据集成调度信息分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成调度信息分页列表
     */
    PageResult<DppEtlSchedulerDO> getDppEtlSchedulerPage(DppEtlSchedulerPageReqVO pageReqVO);

    /**
     * 创建数据集成调度信息
     *
     * @param createReqVO 数据集成调度信息信息
     * @return 数据集成调度信息编号
     */
    Long createDppEtlScheduler(DppEtlSchedulerSaveReqVO createReqVO);
    DppEtlSchedulerDO createDppEtlSchedulerNew(DppEtlSchedulerSaveReqVO createReqVO);

    /**
     * 更新数据集成调度信息
     *
     * @param updateReqVO 数据集成调度信息信息
     */
    int updateDppEtlScheduler(DppEtlSchedulerSaveReqVO updateReqVO);

    /**
     * 删除数据集成调度信息
     *
     * @param idList 数据集成调度信息编号
     */
    int removeDppEtlScheduler(Collection<Long> idList);

    /**
     * 获得数据集成调度信息详情
     *
     * @param id 数据集成调度信息编号
     * @return 数据集成调度信息
     */
    DppEtlSchedulerDO getDppEtlSchedulerById(Long id);

    DppEtlSchedulerDO getDppEtlSchedulerById(DppEtlSchedulerPageReqVO pageReqVO);

    /**
     * 获得全部数据集成调度信息列表
     *
     * @return 数据集成调度信息列表
     */
    List<DppEtlSchedulerDO> getDppEtlSchedulerList();

    /**
     * 获得全部数据集成调度信息 Map
     *
     * @return 数据集成调度信息 Map
     */
    Map<Long, DppEtlSchedulerDO> getDppEtlSchedulerMap();


    /**
     * 导入数据集成调度信息数据
     *
     * @param importExcelList 数据集成调度信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppEtlScheduler(List<DppEtlSchedulerRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
