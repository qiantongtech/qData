package tech.qiantong.qdata.module.da.convert.assetchild.audit;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRulePageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.audit.vo.DaAssetAuditRuleSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.audit.DaAssetAuditRuleDO;

import java.util.List;

/**
 * 数据资产质量结果记录 Convert
 *
 * @author qdata
 * @date 2025-05-09
 */
@Mapper
public interface DaAssetAuditRuleConvert {
    DaAssetAuditRuleConvert INSTANCE = Mappers.getMapper(DaAssetAuditRuleConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetAuditRulePageReqVO 请求参数
     * @return DaAssetAuditRuleDO
     */
     DaAssetAuditRuleDO convertToDO(DaAssetAuditRulePageReqVO daAssetAuditRulePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetAuditRuleSaveReqVO 保存请求参数
     * @return DaAssetAuditRuleDO
     */
     DaAssetAuditRuleDO convertToDO(DaAssetAuditRuleSaveReqVO daAssetAuditRuleSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetAuditRuleDO 实体对象
     * @return DaAssetAuditRuleRespVO
     */
     DaAssetAuditRuleRespVO convertToRespVO(DaAssetAuditRuleDO daAssetAuditRuleDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetAuditRuleDOList 实体对象列表
     * @return List<DaAssetAuditRuleRespVO>
     */
     List<DaAssetAuditRuleRespVO> convertToRespVOList(List<DaAssetAuditRuleDO> daAssetAuditRuleDOList);
}
