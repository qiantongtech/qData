package tech.qiantong.qdata.module.att.convert.cat;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDataDevCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDataDevCatDO;

import java.util.List;

/**
 * 数据开发类目管理 Convert
 *
 * @author qdata
 * @date 2025-03-11
 */
@Mapper
public interface AttDataDevCatConvert {
    AttDataDevCatConvert INSTANCE = Mappers.getMapper(AttDataDevCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attDataDevCatPageReqVO 请求参数
     * @return AttDataDevCatDO
     */
     AttDataDevCatDO convertToDO(AttDataDevCatPageReqVO attDataDevCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attDataDevCatSaveReqVO 保存请求参数
     * @return AttDataDevCatDO
     */
     AttDataDevCatDO convertToDO(AttDataDevCatSaveReqVO attDataDevCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attDataDevCatDO 实体对象
     * @return AttDataDevCatRespVO
     */
     AttDataDevCatRespVO convertToRespVO(AttDataDevCatDO attDataDevCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param attDataDevCatDOList 实体对象列表
     * @return List<AttDataDevCatRespVO>
     */
     List<AttDataDevCatRespVO> convertToRespVOList(List<AttDataDevCatDO> attDataDevCatDOList);
}
