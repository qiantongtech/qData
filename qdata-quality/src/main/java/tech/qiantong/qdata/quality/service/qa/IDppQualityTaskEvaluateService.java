package tech.qiantong.qdata.quality.service.qa;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluatePageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluateRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppQualityTaskEvaluateSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppQualityTaskEvaluateDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据质量任务-评测规则Service接口
 *
 * @author Chaos
 * @date 2025-07-21
 */
public interface IDppQualityTaskEvaluateService extends IService<DppQualityTaskEvaluateDO> {

    /**
     * 获得数据质量任务-评测规则分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据质量任务-评测规则分页列表
     */
    PageResult<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluatePage(DppQualityTaskEvaluatePageReqVO pageReqVO);

    /**
     * 创建数据质量任务-评测规则
     *
     * @param createReqVO 数据质量任务-评测规则信息
     * @return 数据质量任务-评测规则编号
     */
    Long createDppQualityTaskEvaluate(DppQualityTaskEvaluateSaveReqVO createReqVO);

    /**
     * 更新数据质量任务-评测规则
     *
     * @param updateReqVO 数据质量任务-评测规则信息
     */
    int updateDppQualityTaskEvaluate(DppQualityTaskEvaluateSaveReqVO updateReqVO);

    /**
     * 删除数据质量任务-评测规则
     *
     * @param idList 数据质量任务-评测规则编号
     */
    int removeDppQualityTaskEvaluate(Collection<Long> idList);

    /**
     * 获得数据质量任务-评测规则详情
     *
     * @param id 数据质量任务-评测规则编号
     * @return 数据质量任务-评测规则
     */
    DppQualityTaskEvaluateDO getDppQualityTaskEvaluateById(Long id);

    List<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateList(List<Long> idList);

    /**
     * 获得全部数据质量任务-评测规则列表
     *
     * @return 数据质量任务-评测规则列表
     */
    List<DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateList();

    /**
     * 获得全部数据质量任务-评测规则 Map
     *
     * @return 数据质量任务-评测规则 Map
     */
    Map<Long, DppQualityTaskEvaluateDO> getDppQualityTaskEvaluateMap();


    /**
     * 导入数据质量任务-评测规则数据
     *
     * @param importExcelList 数据质量任务-评测规则数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppQualityTaskEvaluate(List<DppQualityTaskEvaluateRespVO> importExcelList, boolean isUpdateSupport, String operName);
}
