package tech.qiantong.qdata.module.da.convert.assetchild.projectRel;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.projectRel.vo.DaAssetProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.projectRel.DaAssetProjectRelDO;

import java.util.List;

/**
 * 数据资产与项目关联关系 Convert
 *
 * @author qdata
 * @date 2025-04-18
 */
@Mapper
public interface DaAssetProjectRelConvert {
    DaAssetProjectRelConvert INSTANCE = Mappers.getMapper(DaAssetProjectRelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetProjectRelPageReqVO 请求参数
     * @return DaAssetProjectRelDO
     */
     DaAssetProjectRelDO convertToDO(DaAssetProjectRelPageReqVO daAssetProjectRelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetProjectRelSaveReqVO 保存请求参数
     * @return DaAssetProjectRelDO
     */
     DaAssetProjectRelDO convertToDO(DaAssetProjectRelSaveReqVO daAssetProjectRelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetProjectRelDO 实体对象
     * @return DaAssetProjectRelRespVO
     */
     DaAssetProjectRelRespVO convertToRespVO(DaAssetProjectRelDO daAssetProjectRelDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetProjectRelDOList 实体对象列表
     * @return List<DaAssetProjectRelRespVO>
     */
     List<DaAssetProjectRelRespVO> convertToRespVOList(List<DaAssetProjectRelDO> daAssetProjectRelDOList);
}
