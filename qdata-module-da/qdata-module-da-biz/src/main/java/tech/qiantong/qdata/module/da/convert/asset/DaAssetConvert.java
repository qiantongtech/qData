package tech.qiantong.qdata.module.da.convert.asset;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetRespVO;
import tech.qiantong.qdata.module.da.controller.admin.asset.vo.DaAssetSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.asset.DaAssetDO;

import java.util.List;

/**
 * 数据资产 Convert
 *
 * @author lhs
 * @date 2025-01-21
 */
@Mapper
public interface DaAssetConvert {
    DaAssetConvert INSTANCE = Mappers.getMapper(DaAssetConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetPageReqVO 请求参数
     * @return DaAssetDO
     */
     DaAssetDO convertToDO(DaAssetPageReqVO daAssetPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetSaveReqVO 保存请求参数
     * @return DaAssetDO
     */
     DaAssetDO convertToDO(DaAssetSaveReqVO daAssetSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetDO 实体对象
     * @return DaAssetRespVO
     */
     DaAssetRespVO convertToRespVO(DaAssetDO daAssetDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetDOList 实体对象列表
     * @return List<DaAssetRespVO>
     */
     List<DaAssetRespVO> convertToRespVOList(List<DaAssetDO> daAssetDOList);
}
