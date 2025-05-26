package tech.qiantong.qdata.module.da.convert.assetchild.operate;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplySaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateApplyDO;

import java.util.List;

/**
 * 数据资产操作申请 Convert
 *
 * @author qdata
 * @date 2025-05-09
 */
@Mapper
public interface DaAssetOperateApplyConvert {
    DaAssetOperateApplyConvert INSTANCE = Mappers.getMapper(DaAssetOperateApplyConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param daAssetOperateApplyPageReqVO 请求参数
     * @return DaAssetOperateApplyDO
     */
     DaAssetOperateApplyDO convertToDO(DaAssetOperateApplyPageReqVO daAssetOperateApplyPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param daAssetOperateApplySaveReqVO 保存请求参数
     * @return DaAssetOperateApplyDO
     */
     DaAssetOperateApplyDO convertToDO(DaAssetOperateApplySaveReqVO daAssetOperateApplySaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param daAssetOperateApplyDO 实体对象
     * @return DaAssetOperateApplyRespVO
     */
     DaAssetOperateApplyRespVO convertToRespVO(DaAssetOperateApplyDO daAssetOperateApplyDO);

    /**
     * DOList 转换为 RespVOList
     * @param daAssetOperateApplyDOList 实体对象列表
     * @return List<DaAssetOperateApplyRespVO>
     */
     List<DaAssetOperateApplyRespVO> convertToRespVOList(List<DaAssetOperateApplyDO> daAssetOperateApplyDOList);
}
