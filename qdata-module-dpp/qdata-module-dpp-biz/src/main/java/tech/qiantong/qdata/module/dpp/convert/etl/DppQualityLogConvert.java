package tech.qiantong.qdata.module.dpp.convert.etl;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppQualityLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppQualityLogDO;

import java.util.List;

/**
 * 数据质量日志 Convert
 *
 * @author qdata
 * @date 2025-07-19
 */
@Mapper
public interface DppQualityLogConvert {
    DppQualityLogConvert INSTANCE = Mappers.getMapper(DppQualityLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppQualityLogPageReqVO 请求参数
     * @return DppQualityLogDO
     */
     DppQualityLogDO convertToDO(DppQualityLogPageReqVO dppQualityLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppQualityLogSaveReqVO 保存请求参数
     * @return DppQualityLogDO
     */
     DppQualityLogDO convertToDO(DppQualityLogSaveReqVO dppQualityLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppQualityLogDO 实体对象
     * @return DppQualityLogRespVO
     */
     DppQualityLogRespVO convertToRespVO(DppQualityLogDO dppQualityLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppQualityLogDOList 实体对象列表
     * @return List<DppQualityLogRespVO>
     */
     List<DppQualityLogRespVO> convertToRespVOList(List<DppQualityLogDO> dppQualityLogDOList);
}
