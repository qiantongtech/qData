package tech.qiantong.qdata.module.dpp.convert.qa;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.qa.vo.DppQualityTaskObjSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.qa.DppQualityTaskObjDO;

import java.util.List;

/**
 * 数据质量任务-稽查对象 Convert
 *
 * @author Chaos
 * @date 2025-07-21
 */
@Mapper
public interface DppQualityTaskObjConvert {
    DppQualityTaskObjConvert INSTANCE = Mappers.getMapper(DppQualityTaskObjConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppQualityTaskObjPageReqVO 请求参数
     * @return DppQualityTaskObjDO
     */
     DppQualityTaskObjDO convertToDO(DppQualityTaskObjPageReqVO dppQualityTaskObjPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppQualityTaskObjSaveReqVO 保存请求参数
     * @return DppQualityTaskObjDO
     */
     DppQualityTaskObjDO convertToDO(DppQualityTaskObjSaveReqVO dppQualityTaskObjSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppQualityTaskObjDO 实体对象
     * @return DppQualityTaskObjRespVO
     */
     DppQualityTaskObjRespVO convertToRespVO(DppQualityTaskObjDO dppQualityTaskObjDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppQualityTaskObjDOList 实体对象列表
     * @return List<DppQualityTaskObjRespVO>
     */
     List<DppQualityTaskObjRespVO> convertToRespVOList(List<DppQualityTaskObjDO> dppQualityTaskObjDOList);
}
