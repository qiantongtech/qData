package tech.qiantong.qdata.module.dpp.convert.etl;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlSchedulerSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlSchedulerDO;

import java.util.List;

/**
 * 数据集成调度信息 Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlSchedulerConvert {
    DppEtlSchedulerConvert INSTANCE = Mappers.getMapper(DppEtlSchedulerConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlSchedulerPageReqVO 请求参数
     * @return DppEtlSchedulerDO
     */
     DppEtlSchedulerDO convertToDO(DppEtlSchedulerPageReqVO dppEtlSchedulerPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlSchedulerSaveReqVO 保存请求参数
     * @return DppEtlSchedulerDO
     */
     DppEtlSchedulerDO convertToDO(DppEtlSchedulerSaveReqVO dppEtlSchedulerSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlSchedulerDO 实体对象
     * @return DppEtlSchedulerRespVO
     */
     DppEtlSchedulerRespVO convertToRespVO(DppEtlSchedulerDO dppEtlSchedulerDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlSchedulerDOList 实体对象列表
     * @return List<DppEtlSchedulerRespVO>
     */
     List<DppEtlSchedulerRespVO> convertToRespVOList(List<DppEtlSchedulerDO> dppEtlSchedulerDOList);
}
