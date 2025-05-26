package tech.qiantong.qdata.module.dpp.convert.etl;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstancePageReqVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstanceRespVO;
import tech.qiantong.qdata.module.dpp.controller.admin.etl.vo.DppEtlTaskInstanceSaveReqVO;
import tech.qiantong.qdata.module.dpp.dal.dataobject.etl.DppEtlTaskInstanceDO;

import java.util.List;

/**
 * 数据集成任务实例 Convert
 *
 * @author qdata
 * @date 2025-02-13
 */
@Mapper
public interface DppEtlTaskInstanceConvert {
    DppEtlTaskInstanceConvert INSTANCE = Mappers.getMapper(DppEtlTaskInstanceConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dppEtlTaskInstancePageReqVO 请求参数
     * @return DppEtlTaskInstanceDO
     */
     DppEtlTaskInstanceDO convertToDO(DppEtlTaskInstancePageReqVO dppEtlTaskInstancePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dppEtlTaskInstanceSaveReqVO 保存请求参数
     * @return DppEtlTaskInstanceDO
     */
     DppEtlTaskInstanceDO convertToDO(DppEtlTaskInstanceSaveReqVO dppEtlTaskInstanceSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dppEtlTaskInstanceDO 实体对象
     * @return DppEtlTaskInstanceRespVO
     */
     DppEtlTaskInstanceRespVO convertToRespVO(DppEtlTaskInstanceDO dppEtlTaskInstanceDO);

    /**
     * DOList 转换为 RespVOList
     * @param dppEtlTaskInstanceDOList 实体对象列表
     * @return List<DppEtlTaskInstanceRespVO>
     */
     List<DppEtlTaskInstanceRespVO> convertToRespVOList(List<DppEtlTaskInstanceDO> dppEtlTaskInstanceDOList);
}
