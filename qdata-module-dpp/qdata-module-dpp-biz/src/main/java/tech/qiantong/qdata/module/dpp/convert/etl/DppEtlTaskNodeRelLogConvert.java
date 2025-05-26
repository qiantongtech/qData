package tech.qiantong.qdata.module.dpp.convert.etl;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogPageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskNodeRelLogSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskNodeRelLogDO;

import java.util.List;

/**
 * 数据集成任务节点关系-日志 Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlTaskNodeRelLogConvert {
    DppEtlTaskNodeRelLogConvert INSTANCE = Mappers.getMapper(DppEtlTaskNodeRelLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlTaskNodeRelLogPageReqVO 请求参数
     * @return DppEtlTaskNodeRelLogDO
     */
     DppEtlTaskNodeRelLogDO convertToDO(DppEtlTaskNodeRelLogPageReqVO dppEtlTaskNodeRelLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlTaskNodeRelLogSaveReqVO 保存请求参数
     * @return DppEtlTaskNodeRelLogDO
     */
     DppEtlTaskNodeRelLogDO convertToDO(DppEtlTaskNodeRelLogSaveReqVO dppEtlTaskNodeRelLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlTaskNodeRelLogDO 实体对象
     * @return DppEtlTaskNodeRelLogRespVO
     */
     DppEtlTaskNodeRelLogRespVO convertToRespVO(DppEtlTaskNodeRelLogDO dppEtlTaskNodeRelLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlTaskNodeRelLogDOList 实体对象列表
     * @return List<DppEtlTaskNodeRelLogRespVO>
     */
     List<DppEtlTaskNodeRelLogRespVO> convertToRespVOList(List<DppEtlTaskNodeRelLogDO> dppEtlTaskNodeRelLogDOList);
}
