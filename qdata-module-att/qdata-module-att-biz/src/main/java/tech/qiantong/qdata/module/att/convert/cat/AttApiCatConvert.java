package tech.qiantong.qdata.module.att.convert.cat;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatRespVO;
import tech.qiantong.qdata.module.att.controller.admin.cat.vo.AttApiCatSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.cat.AttApiCatDO;

import java.util.List;

/**
 * 数据服务类目管理 Convert
 *
 * @author qdata
 * @date 2025-03-11
 */
@Mapper
public interface AttApiCatConvert {
    AttApiCatConvert INSTANCE = Mappers.getMapper(AttApiCatConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attApiCatPageReqVO 请求参数
     * @return AttApiCatDO
     */
     AttApiCatDO convertToDO(AttApiCatPageReqVO attApiCatPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attApiCatSaveReqVO 保存请求参数
     * @return AttApiCatDO
     */
     AttApiCatDO convertToDO(AttApiCatSaveReqVO attApiCatSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attApiCatDO 实体对象
     * @return AttApiCatRespVO
     */
     AttApiCatRespVO convertToRespVO(AttApiCatDO attApiCatDO);

    /**
     * DOList 转换为 RespVOList
     * @param attApiCatDOList 实体对象列表
     * @return List<AttApiCatRespVO>
     */
     List<AttApiCatRespVO> convertToRespVOList(List<AttApiCatDO> attApiCatDOList);
}
