package tech.qiantong.qdata.module.att.convert.cat;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttAssetCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttAssetCatDO;

import java.util.List;

/**
 * 数据资产类目管理 Convert
 *
 * @author qdata
 * @date 2025-01-20
 */
@Mapper
public interface AttAssetCatConvert {
    AttAssetCatConvert INSTANCE = Mappers.getMapper(AttAssetCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attAssetCatPageReqVO 请求参数
     * @return AttAssetCatDO
     */
     AttAssetCatDO convertToDO(AttAssetCatPageReqVO attAssetCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attAssetCatSaveReqVO 保存请求参数
     * @return AttAssetCatDO
     */
     AttAssetCatDO convertToDO(AttAssetCatSaveReqVO attAssetCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attAssetCatDO 实体对象
     * @return AttAssetCatRespVO
     */
     AttAssetCatRespVO convertToRespVO(AttAssetCatDO attAssetCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param attAssetCatDOList 实体对象列表
     * @return List<AttAssetCatRespVO>
     */
     List<AttAssetCatRespVO> convertToRespVOList(List<AttAssetCatDO> attAssetCatDOList);
}
