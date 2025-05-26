package tech.qiantong.qdata.module.dpp.convert.etl;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeDO;

import java.util.List;

/**
 * 数据集成节点 Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlNodeConvert {
    DppEtlNodeConvert INSTANCE = Mappers.getMapper(DppEtlNodeConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlNodePageReqVO 请求参数
     * @return DppEtlNodeDO
     */
     DppEtlNodeDO convertToDO(DppEtlNodePageReqVO dppEtlNodePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlNodeSaveReqVO 保存请求参数
     * @return DppEtlNodeDO
     */
     DppEtlNodeDO convertToDO(DppEtlNodeSaveReqVO dppEtlNodeSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlNodeDO 实体对象
     * @return DppEtlNodeRespVO
     */
     DppEtlNodeRespVO convertToRespVO(DppEtlNodeDO dppEtlNodeDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlNodeDOList 实体对象列表
     * @return List<DppEtlNodeRespVO>
     */
     List<DppEtlNodeRespVO> convertToRespVOList(List<DppEtlNodeDO> dppEtlNodeDOList);
}
