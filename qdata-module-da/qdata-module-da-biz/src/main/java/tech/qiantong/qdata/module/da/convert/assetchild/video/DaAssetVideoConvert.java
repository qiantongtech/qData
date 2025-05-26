package tech.qiantong.qdata.module.da.convert.assetchild.video;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.video.DaAssetVideoDO;

import java.util.List;

/**
 * 数据资产-视频数据 Convert
 *
 * @author qdata
 * @date 2025-04-14
 */
@Mapper
public interface DaAssetVideoConvert {
    DaAssetVideoConvert INSTANCE = Mappers.getMapper(DaAssetVideoConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetVideoPageReqVO 请求参数
     * @return DaAssetVideoDO
     */
     DaAssetVideoDO convertToDO(DaAssetVideoPageReqVO daAssetVideoPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetVideoSaveReqVO 保存请求参数
     * @return DaAssetVideoDO
     */
     DaAssetVideoDO convertToDO(DaAssetVideoSaveReqVO daAssetVideoSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetVideoDO 实体对象
     * @return DaAssetVideoRespVO
     */
     DaAssetVideoRespVO convertToRespVO(DaAssetVideoDO daAssetVideoDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetVideoDOList 实体对象列表
     * @return List<DaAssetVideoRespVO>
     */
     List<DaAssetVideoRespVO> convertToRespVOList(List<DaAssetVideoDO> daAssetVideoDOList);
}
