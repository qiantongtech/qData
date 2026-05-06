package tech.qiantong.qdata.module.mc.convert.domain;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.domain.McDomainDO;

import java.util.List;

/**
 * 业务域 Convert
 *
 * @author qdata
 * @date 2026-02-12
 */
@Mapper
public interface McDomainConvert {
    McDomainConvert INSTANCE = Mappers.getMapper(McDomainConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param mcDomainPageReqVO 请求参数
     * @return McDomainDO
     */
     McDomainDO convertToDO(McDomainPageReqVO mcDomainPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param mcDomainSaveReqVO 保存请求参数
     * @return McDomainDO
     */
     McDomainDO convertToDO(McDomainSaveReqVO mcDomainSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param mcDomainDO 实体对象
     * @return McDomainRespVO
     */
     McDomainRespVO convertToRespVO(McDomainDO mcDomainDO);

    /**
     * DOList 转换为 RespVOList
     * @param mcDomainDOList 实体对象列表
     * @return List<McDomainRespVO>
     */
     List<McDomainRespVO> convertToRespVOList(List<McDomainDO> mcDomainDOList);
}
