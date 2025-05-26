package tech.qiantong.qdata.module.att.convert.project;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectRespVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectDO;

import java.util.List;

/**
 * 项目 Convert
 *
 * @author shu
 * @date 2025-01-20
 */
@Mapper
public interface AttProjectConvert {
    AttProjectConvert INSTANCE = Mappers.getMapper(AttProjectConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param attProjectPageReqVO 请求参数
     * @return AttProjectDO
     */
     AttProjectDO convertToDO(AttProjectPageReqVO attProjectPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param attProjectSaveReqVO 保存请求参数
     * @return AttProjectDO
     */
     AttProjectDO convertToDO(AttProjectSaveReqVO attProjectSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param attProjectDO 实体对象
     * @return AttProjectRespVO
     */
     AttProjectRespVO convertToRespVO(AttProjectDO attProjectDO);

    /**
     * DOList 转换为 RespVOList
     * @param attProjectDOList 实体对象列表
     * @return List<AttProjectRespVO>
     */
     List<AttProjectRespVO> convertToRespVOList(List<AttProjectDO> attProjectDOList);
}
