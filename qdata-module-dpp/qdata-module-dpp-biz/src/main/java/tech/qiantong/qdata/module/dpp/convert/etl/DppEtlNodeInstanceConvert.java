package tech.qiantong.qdata.module.dpp.convert.etl;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlNodeInstanceSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlNodeInstanceDO;

import java.util.List;

/**
 * 数据集成节点实例 Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlNodeInstanceConvert {
    DppEtlNodeInstanceConvert INSTANCE = Mappers.getMapper(DppEtlNodeInstanceConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlNodeInstancePageReqVO 请求参数
     * @return DppEtlNodeInstanceDO
     */
     DppEtlNodeInstanceDO convertToDO(DppEtlNodeInstancePageReqVO dppEtlNodeInstancePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlNodeInstanceSaveReqVO 保存请求参数
     * @return DppEtlNodeInstanceDO
     */
     DppEtlNodeInstanceDO convertToDO(DppEtlNodeInstanceSaveReqVO dppEtlNodeInstanceSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlNodeInstanceDO 实体对象
     * @return DppEtlNodeInstanceRespVO
     */
     DppEtlNodeInstanceRespVO convertToRespVO(DppEtlNodeInstanceDO dppEtlNodeInstanceDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlNodeInstanceDOList 实体对象列表
     * @return List<DppEtlNodeInstanceRespVO>
     */
     List<DppEtlNodeInstanceRespVO> convertToRespVOList(List<DppEtlNodeInstanceDO> dppEtlNodeInstanceDOList);
}
