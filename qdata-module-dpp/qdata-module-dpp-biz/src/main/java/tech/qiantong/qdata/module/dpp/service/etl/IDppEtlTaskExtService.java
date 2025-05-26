package tech.qiantong.qdata.module.dpp.service.etl;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskExtPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskExtRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskExtSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskExtDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据集成任务-扩展数据Service接口
 *
 * @author qdata
 * @date 2025-04-16
 */
public interface IDppEtlTaskExtService extends IService<DppEtlTaskExtDO> {

    /**
     * 获得数据集成任务-扩展数据分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据集成任务-扩展数据分页列表
     */
    PageResult<DppEtlTaskExtDO> getDppEtlTaskExtPage(DppEtlTaskExtPageReqVO pageReqVO);

    /**
     * 创建数据集成任务-扩展数据
     *
     * @param createReqVO 数据集成任务-扩展数据信息
     * @return 数据集成任务-扩展数据编号
     */
    Long createDppEtlTaskExt(DppEtlTaskExtSaveReqVO createReqVO);

    /**
     * 更新数据集成任务-扩展数据
     *
     * @param updateReqVO 数据集成任务-扩展数据信息
     */
    int updateDppEtlTaskExt(DppEtlTaskExtSaveReqVO updateReqVO);

    /**
     * 删除数据集成任务-扩展数据
     *
     * @param idList 数据集成任务-扩展数据编号
     */
    int removeDppEtlTaskExt(Collection<Long> idList);

    /**
     * 获得数据集成任务-扩展数据详情
     *
     * @param id 数据集成任务-扩展数据编号
     * @return 数据集成任务-扩展数据
     */
    DppEtlTaskExtDO getDppEtlTaskExtById(Long id);

    /**
     * 获得全部数据集成任务-扩展数据列表
     *
     * @return 数据集成任务-扩展数据列表
     */
    List<DppEtlTaskExtDO> getDppEtlTaskExtList();

    /**
     * 获得全部数据集成任务-扩展数据 Map
     *
     * @return 数据集成任务-扩展数据 Map
     */
    Map<Long, DppEtlTaskExtDO> getDppEtlTaskExtMap();


    /**
     * 导入数据集成任务-扩展数据数据
     *
     * @param importExcelList 数据集成任务-扩展数据数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importDppEtlTaskExt(List<DppEtlTaskExtRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 根据任务id获取信息
     *
     * @param taskId
     * @return
     */
    DppEtlTaskExtDO getByTaskId(Long taskId);

}
