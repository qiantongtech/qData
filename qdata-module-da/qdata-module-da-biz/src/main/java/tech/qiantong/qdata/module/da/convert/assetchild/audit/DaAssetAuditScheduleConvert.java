package tech.qiantong.qdata.module.da.convert.assetchild.audit;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditSchedulePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditScheduleRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditScheduleSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditScheduleDO;

import java.util.List;

/**
 * 资产稽查调度 Convert
 *
 * @author qdata
 * @date 2025-05-09
 */
@Mapper
public interface DaAssetAuditScheduleConvert {
    DaAssetAuditScheduleConvert INSTANCE = Mappers.getMapper(DaAssetAuditScheduleConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetAuditSchedulePageReqVO 请求参数
     * @return DaAssetAuditScheduleDO
     */
     DaAssetAuditScheduleDO convertToDO(DaAssetAuditSchedulePageReqVO daAssetAuditSchedulePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetAuditScheduleSaveReqVO 保存请求参数
     * @return DaAssetAuditScheduleDO
     */
     DaAssetAuditScheduleDO convertToDO(DaAssetAuditScheduleSaveReqVO daAssetAuditScheduleSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetAuditScheduleDO 实体对象
     * @return DaAssetAuditScheduleRespVO
     */
     DaAssetAuditScheduleRespVO convertToRespVO(DaAssetAuditScheduleDO daAssetAuditScheduleDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetAuditScheduleDOList 实体对象列表
     * @return List<DaAssetAuditScheduleRespVO>
     */
     List<DaAssetAuditScheduleRespVO> convertToRespVOList(List<DaAssetAuditScheduleDO> daAssetAuditScheduleDOList);
}
