package tech.qiantong.qdata.module.dpp.convert.qa;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskDO;

import java.util.List;

/**
 * 数据质量任务 Convert
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Mapper
public interface DppQualityTaskConvert {
    DppQualityTaskConvert INSTANCE = Mappers.getMapper(DppQualityTaskConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppQualityTaskPageReqVO 请求参数
     * @return DppQualityTaskDO
     */
     DppQualityTaskDO convertToDO(DppQualityTaskPageReqVO dppQualityTaskPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppQualityTaskSaveReqVO 保存请求参数
     * @return DppQualityTaskDO
     */
     DppQualityTaskDO convertToDO(DppQualityTaskSaveReqVO dppQualityTaskSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppQualityTaskDO 实体对象
     * @return DppQualityTaskRespVO
     */
     DppQualityTaskRespVO convertToRespVO(DppQualityTaskDO dppQualityTaskDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppQualityTaskDOList 实体对象列表
     * @return List<DppQualityTaskRespVO>
     */
     List<DppQualityTaskRespVO> convertToRespVOList(List<DppQualityTaskDO> dppQualityTaskDOList);
}
