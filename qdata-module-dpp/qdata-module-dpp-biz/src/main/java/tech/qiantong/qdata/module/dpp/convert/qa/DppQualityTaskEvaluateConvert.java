package tech.qiantong.qdata.module.dpp.convert.qa;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluatePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluateRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskEvaluateSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskEvaluateDO;

import java.util.List;

/**
 * 数据质量任务-评测规则 Convert
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Mapper
public interface DppQualityTaskEvaluateConvert {
    DppQualityTaskEvaluateConvert INSTANCE = Mappers.getMapper(DppQualityTaskEvaluateConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppQualityTaskEvaluatePageReqVO 请求参数
     * @return DppQualityTaskEvaluateDO
     */
     DppQualityTaskEvaluateDO convertToDO(DppQualityTaskEvaluatePageReqVO dppQualityTaskEvaluatePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppQualityTaskEvaluateSaveReqVO 保存请求参数
     * @return DppQualityTaskEvaluateDO
     */
     DppQualityTaskEvaluateDO convertToDO(DppQualityTaskEvaluateSaveReqVO dppQualityTaskEvaluateSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppQualityTaskEvaluateDO 实体对象
     * @return DppQualityTaskEvaluateRespVO
     */
     DppQualityTaskEvaluateRespVO convertToRespVO(DppQualityTaskEvaluateDO dppQualityTaskEvaluateDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppQualityTaskEvaluateDOList 实体对象列表
     * @return List<DppQualityTaskEvaluateRespVO>
     */
     List<DppQualityTaskEvaluateRespVO> convertToRespVOList(List<DppQualityTaskEvaluateDO> dppQualityTaskEvaluateDOList);
}
