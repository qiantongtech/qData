package tech.qiantong.qdata.module.dpp.service.qa;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.*;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据质量任务Service接口
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface IDppQualityTaskService extends IService<DppQualityTaskDO> {

    /**
     * 获得数据质量任务分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据质量任务分页列表
     */
    PageResult<DppQualityTaskDO> getDppQualityTaskPage(DppQualityTaskPageReqVO pageReqVO);

    /**
     * 创建数据质量任务
     *
     * @param createReqVO 数据质量任务信息
     * @return 数据质量任务编号
     */
    Long createDppQualityTask(DppQualityTaskSaveReqVO createReqVO);

    /**
     * 更新数据质量任务
     *
     * @param updateReqVO 数据质量任务信息
     */
    int updateDppQualityTask(DppQualityTaskSaveReqVO updateReqVO);

    /**
     * 删除数据质量任务
     *
     * @param idList 数据质量任务编号
     */
    int removeDppQualityTask(Collection<Long> idList);

    /**
     * 获得数据质量任务详情
     *
     * @param id 数据质量任务编号
     * @return 数据质量任务
     */
    DppQualityTaskRespVO getDppQualityTaskById(Long id);

    DppQualityTaskRespVO getQualityTaskAsset(DppQualityTaskAssetReqVO dppQualityTaskAssetReqVO);

    /**
     * 获得全部数据质量任务列表
     *
     * @return 数据质量任务列表
     */
    List<DppQualityTaskDO> getDppQualityTaskList();

    /**
     * 获得全部数据质量任务 Map
     *
     * @return 数据质量任务 Map
     */
    Map<Long, DppQualityTaskDO> getDppQualityTaskMap();


    /**
     * 导入数据质量任务数据
     *
     * @param importExcelList 数据质量任务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppQualityTask(List<DppQualityTaskRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 检验数据格式是否有误
     * @param dppQualityTaskEvaluate
     */
    String verifyInterfaceValue(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate);

    AjaxResult startDppQualityTask(Long id);

    boolean updateDppQualityTaskStatus(DppQualityTaskSaveReqVO daDiscoveryTask);

    JSONObject validationErrorDataSql(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate);

    JSONObject validationValidDataSql(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluate);

    boolean updateDaDiscoveryTaskCronExpression(DppQualityTaskSaveReqVO daDiscoveryTask);
}
