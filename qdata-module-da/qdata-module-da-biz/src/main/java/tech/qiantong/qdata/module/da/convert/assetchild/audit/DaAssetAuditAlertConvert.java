package tech.qiantong.qdata.module.da.convert.assetchild.audit;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditAlertSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditAlertDO;

import java.util.List;

/**
 * 数据资产-质量预警 Convert
 *
 * @author qdata
 * @date 2025-05-09
 */
@Mapper
public interface DaAssetAuditAlertConvert {
    DaAssetAuditAlertConvert INSTANCE = Mappers.getMapper(DaAssetAuditAlertConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetAuditAlertPageReqVO 请求参数
     * @return DaAssetAuditAlertDO
     */
     DaAssetAuditAlertDO convertToDO(DaAssetAuditAlertPageReqVO daAssetAuditAlertPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetAuditAlertSaveReqVO 保存请求参数
     * @return DaAssetAuditAlertDO
     */
     DaAssetAuditAlertDO convertToDO(DaAssetAuditAlertSaveReqVO daAssetAuditAlertSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetAuditAlertDO 实体对象
     * @return DaAssetAuditAlertRespVO
     */
     DaAssetAuditAlertRespVO convertToRespVO(DaAssetAuditAlertDO daAssetAuditAlertDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetAuditAlertDOList 实体对象列表
     * @return List<DaAssetAuditAlertRespVO>
     */
     List<DaAssetAuditAlertRespVO> convertToRespVOList(List<DaAssetAuditAlertDO> daAssetAuditAlertDOList);
}
