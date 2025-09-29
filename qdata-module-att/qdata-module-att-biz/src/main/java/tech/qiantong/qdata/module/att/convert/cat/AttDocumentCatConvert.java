package tech.qiantong.qdata.module.att.convert.cat;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttDocumentCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttDocumentCatDO;

import java.util.List;

/**
 * 标准信息分类管理 Convert
 *
 * @author qdata
 * @date 2025-08-21
 */
@Mapper
public interface AttDocumentCatConvert {
    AttDocumentCatConvert INSTANCE = Mappers.getMapper(AttDocumentCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attDocumentCatPageReqVO 请求参数
     * @return AttDocumentCatDO
     */
     AttDocumentCatDO convertToDO(AttDocumentCatPageReqVO attDocumentCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attDocumentCatSaveReqVO 保存请求参数
     * @return AttDocumentCatDO
     */
     AttDocumentCatDO convertToDO(AttDocumentCatSaveReqVO attDocumentCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attDocumentCatDO 实体对象
     * @return AttDocumentCatRespVO
     */
     AttDocumentCatRespVO convertToRespVO(AttDocumentCatDO attDocumentCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param attDocumentCatDOList 实体对象列表
     * @return List<AttDocumentCatRespVO>
     */
     List<AttDocumentCatRespVO> convertToRespVOList(List<AttDocumentCatDO> attDocumentCatDOList);
}
