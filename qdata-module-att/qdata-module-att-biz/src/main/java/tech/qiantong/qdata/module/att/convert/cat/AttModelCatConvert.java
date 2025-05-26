package tech.qiantong.qdata.module.att.convert.cat;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttModelCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttModelCatDO;

import java.util.List;

/**
 * 逻辑模型类目管理 Convert
 *
 * @author qdata
 * @date 2025-01-20
 */
@Mapper
public interface AttModelCatConvert {
    AttModelCatConvert INSTANCE = Mappers.getMapper(AttModelCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attModelCatPageReqVO 请求参数
     * @return AttModelCatDO
     */
     AttModelCatDO convertToDO(AttModelCatPageReqVO attModelCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attModelCatSaveReqVO 保存请求参数
     * @return AttModelCatDO
     */
     AttModelCatDO convertToDO(AttModelCatSaveReqVO attModelCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attModelCatDO 实体对象
     * @return AttModelCatRespVO
     */
     AttModelCatRespVO convertToRespVO(AttModelCatDO attModelCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param attModelCatDOList 实体对象列表
     * @return List<AttModelCatRespVO>
     */
     List<AttModelCatRespVO> convertToRespVOList(List<AttModelCatDO> attModelCatDOList);
}
