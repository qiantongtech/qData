package tech.qiantong.qdata.module.att.convert.cat;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttCleanCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttCleanCatDO;

import java.util.List;

/**
 * 清洗规则类目 Convert
 *
 * @author qdata
 * @date 2025-08-11
 */
@Mapper
public interface AttCleanCatConvert {
    AttCleanCatConvert INSTANCE = Mappers.getMapper(AttCleanCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attCleanCatPageReqVO 请求参数
     * @return AttCleanCatDO
     */
     AttCleanCatDO convertToDO(AttCleanCatPageReqVO attCleanCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attCleanCatSaveReqVO 保存请求参数
     * @return AttCleanCatDO
     */
     AttCleanCatDO convertToDO(AttCleanCatSaveReqVO attCleanCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attCleanCatDO 实体对象
     * @return AttCleanCatRespVO
     */
     AttCleanCatRespVO convertToRespVO(AttCleanCatDO attCleanCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param attCleanCatDOList 实体对象列表
     * @return List<AttCleanCatRespVO>
     */
     List<AttCleanCatRespVO> convertToRespVOList(List<AttCleanCatDO> attCleanCatDOList);
}
