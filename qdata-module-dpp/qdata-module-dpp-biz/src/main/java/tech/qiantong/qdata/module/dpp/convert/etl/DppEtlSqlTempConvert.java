package tech.qiantong.qdata.module.dpp.convert.etl;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSqlTempSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSqlTempDO;

import java.util.List;

/**
 * 数据集成SQL模版 Convert
 *
 * @author FXB
 * @date 2025-06-25
 */
@Mapper
public interface DppEtlSqlTempConvert {
    DppEtlSqlTempConvert INSTANCE = Mappers.getMapper(DppEtlSqlTempConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlSqlTempPageReqVO 请求参数
     * @return DppEtlSqlTempDO
     */
     DppEtlSqlTempDO convertToDO(DppEtlSqlTempPageReqVO dppEtlSqlTempPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlSqlTempSaveReqVO 保存请求参数
     * @return DppEtlSqlTempDO
     */
     DppEtlSqlTempDO convertToDO(DppEtlSqlTempSaveReqVO dppEtlSqlTempSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlSqlTempDO 实体对象
     * @return DppEtlSqlTempRespVO
     */
     DppEtlSqlTempRespVO convertToRespVO(DppEtlSqlTempDO dppEtlSqlTempDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlSqlTempDOList 实体对象列表
     * @return List<DppEtlSqlTempRespVO>
     */
     List<DppEtlSqlTempRespVO> convertToRespVOList(List<DppEtlSqlTempDO> dppEtlSqlTempDOList);
}
