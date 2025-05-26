package tech.qiantong.qdata.module.da.convert.assetchild.gis;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.gis.DaAssetGisDO;

import java.util.List;

/**
 * 数据资产-地理空间服务 Convert
 *
 * @author qdata
 * @date 2025-04-14
 */
@Mapper
public interface DaAssetGisConvert {
    DaAssetGisConvert INSTANCE = Mappers.getMapper(DaAssetGisConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetGisPageReqVO 请求参数
     * @return DaAssetGisDO
     */
     DaAssetGisDO convertToDO(DaAssetGisPageReqVO daAssetGisPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetGisSaveReqVO 保存请求参数
     * @return DaAssetGisDO
     */
     DaAssetGisDO convertToDO(DaAssetGisSaveReqVO daAssetGisSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetGisDO 实体对象
     * @return DaAssetGisRespVO
     */
     DaAssetGisRespVO convertToRespVO(DaAssetGisDO daAssetGisDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetGisDOList 实体对象列表
     * @return List<DaAssetGisRespVO>
     */
     List<DaAssetGisRespVO> convertToRespVOList(List<DaAssetGisDO> daAssetGisDOList);
}
