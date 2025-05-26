package tech.qiantong.qdata.module.da.convert.assetchild.geo;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.geo.vo.DaAssetGeoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.geo.DaAssetGeoDO;

import java.util.List;

/**
 * 数据资产-矢量 Convert
 *
 * @author qdata
 * @date 2025-04-14
 */
@Mapper
public interface DaAssetGeoConvert {
    DaAssetGeoConvert INSTANCE = Mappers.getMapper(DaAssetGeoConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetGeoPageReqVO 请求参数
     * @return DaAssetGeoDO
     */
     DaAssetGeoDO convertToDO(DaAssetGeoPageReqVO daAssetGeoPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetGeoSaveReqVO 保存请求参数
     * @return DaAssetGeoDO
     */
     DaAssetGeoDO convertToDO(DaAssetGeoSaveReqVO daAssetGeoSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetGeoDO 实体对象
     * @return DaAssetGeoRespVO
     */
     DaAssetGeoRespVO convertToRespVO(DaAssetGeoDO daAssetGeoDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetGeoDOList 实体对象列表
     * @return List<DaAssetGeoRespVO>
     */
     List<DaAssetGeoRespVO> convertToRespVOList(List<DaAssetGeoDO> daAssetGeoDOList);
}
