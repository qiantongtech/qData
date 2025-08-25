package tech.qiantong.qdata.quality.service.qa;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogPageReqVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogRespVO;
import tech.qiantong.qdata.quality.controller.qa.vo.DppEvaluateLogSaveReqVO;
import tech.qiantong.qdata.quality.dal.dataobject.qa.DppEvaluateLogDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 评测规则结果Service接口
 *
 * @author qdata
 * @date 2025-07-21
 */
public interface IDppEvaluateLogService extends IService<DppEvaluateLogDO> {

    /**
     * 获得评测规则结果分页列表
     *
     * @param pageReqVO 分页请求
     * @return 评测规则结果分页列表
     */
    PageResult<DppEvaluateLogDO> getDppEvaluateLogPage(DppEvaluateLogPageReqVO pageReqVO);

    /**
     * 创建评测规则结果
     *
     * @param createReqVO 评测规则结果信息
     * @return 评测规则结果编号
     */
    Long createDppEvaluateLog(DppEvaluateLogSaveReqVO createReqVO);

    /**
     * 更新评测规则结果
     *
     * @param updateReqVO 评测规则结果信息
     */
    int updateDppEvaluateLog(DppEvaluateLogSaveReqVO updateReqVO);

    /**
     * 删除评测规则结果
     *
     * @param idList 评测规则结果编号
     */
    int removeDppEvaluateLog(Collection<Long> idList);

    /**
     * 获得评测规则结果详情
     *
     * @param id 评测规则结果编号
     * @return 评测规则结果
     */
    DppEvaluateLogDO getDppEvaluateLogById(Long id);

    /**
     * 获得全部评测规则结果列表
     *
     * @return 评测规则结果列表
     */
    List<DppEvaluateLogDO> getDppEvaluateLogList();

    /**
     * 获得全部评测规则结果 Map
     *
     * @return 评测规则结果 Map
     */
    Map<Long, DppEvaluateLogDO> getDppEvaluateLogMap();


    /**
     * 导入评测规则结果数据
     *
     * @param importExcelList 评测规则结果数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDppEvaluateLog(List<DppEvaluateLogRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
