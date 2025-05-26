package tech.qiantong.qdata.module.da.convert.assetColumn;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetColumn.vo.DaAssetColumnSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetColumn.DaAssetColumnDO;

import java.util.List;

/**
 * 数据资产字段 Convert
 *
 * @author lhs
 * @date 2025-01-21
 */
@Mapper
public interface DaAssetColumnConvert {
    DaAssetColumnConvert INSTANCE = Mappers.getMapper(DaAssetColumnConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetColumnPageReqVO 请求参数
     * @return DaAssetColumnDO
     */
     DaAssetColumnDO convertToDO(DaAssetColumnPageReqVO daAssetColumnPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetColumnSaveReqVO 保存请求参数
     * @return DaAssetColumnDO
     */
     DaAssetColumnDO convertToDO(DaAssetColumnSaveReqVO daAssetColumnSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetColumnDO 实体对象
     * @return DaAssetColumnRespVO
     */
     DaAssetColumnRespVO convertToRespVO(DaAssetColumnDO daAssetColumnDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetColumnDOList 实体对象列表
     * @return List<DaAssetColumnRespVO>
     */
     List<DaAssetColumnRespVO> convertToRespVOList(List<DaAssetColumnDO> daAssetColumnDOList);
}
