package tech.qiantong.qdata.module.att.convert.project;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectUserRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectUserRelDO;

import java.util.List;

/**
 * 项目与用户关联关系 Convert
 *
 * @author qdata
 * @date 2025-02-11
 */
@Mapper
public interface AttProjectUserRelConvert {
    AttProjectUserRelConvert INSTANCE = Mappers.getMapper(AttProjectUserRelConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attProjectUserRelPageReqVO 请求参数
     * @return AttProjectUserRelDO
     */
     AttProjectUserRelDO convertToDO(AttProjectUserRelPageReqVO attProjectUserRelPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attProjectUserRelSaveReqVO 保存请求参数
     * @return AttProjectUserRelDO
     */
     AttProjectUserRelDO convertToDO(AttProjectUserRelSaveReqVO attProjectUserRelSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attProjectUserRelDO 实体对象
     * @return AttProjectUserRelRespVO
     */
     AttProjectUserRelRespVO convertToRespVO(AttProjectUserRelDO attProjectUserRelDO);

    /**
     * DOList 转换为 RespVOList
     * @param attProjectUserRelDOList 实体对象列表
     * @return List<AttProjectUserRelRespVO>
     */
     List<AttProjectUserRelRespVO> convertToRespVOList(List<AttProjectUserRelDO> attProjectUserRelDOList);
}
