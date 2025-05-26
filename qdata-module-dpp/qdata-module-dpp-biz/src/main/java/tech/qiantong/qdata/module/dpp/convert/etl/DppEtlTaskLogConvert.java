package tech.qiantong.qdata.module.dpp.convert.etl;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskLogDO;

import java.util.List;

/**
 * 数据集成任务-日志 Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlTaskLogConvert {
    DppEtlTaskLogConvert INSTANCE = Mappers.getMapper(DppEtlTaskLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlTaskLogPageReqVO 请求参数
     * @return DppEtlTaskLogDO
     */
     DppEtlTaskLogDO convertToDO(DppEtlTaskLogPageReqVO dppEtlTaskLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlTaskLogSaveReqVO 保存请求参数
     * @return DppEtlTaskLogDO
     */
     DppEtlTaskLogDO convertToDO(DppEtlTaskLogSaveReqVO dppEtlTaskLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlTaskLogDO 实体对象
     * @return DppEtlTaskLogRespVO
     */
     DppEtlTaskLogRespVO convertToRespVO(DppEtlTaskLogDO dppEtlTaskLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlTaskLogDOList 实体对象列表
     * @return List<DppEtlTaskLogRespVO>
     */
     List<DppEtlTaskLogRespVO> convertToRespVOList(List<DppEtlTaskLogDO> dppEtlTaskLogDOList);
}
