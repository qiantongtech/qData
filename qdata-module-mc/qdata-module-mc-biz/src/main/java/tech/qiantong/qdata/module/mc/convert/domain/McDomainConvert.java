package tech.qiantong.qdata.module.mc.convert.domain;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.domain.vo.McDomainSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.domain.McDomainDO;

import java.util.List;

/**
 * Business domain Convert
 *
 * @author qdata
 * @date 2026-02-12
 */
@Mapper
public interface McDomainConvert {
    McDomainConvert INSTANCE = Mappers.getMapper(McDomainConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcDomainPageReqVO request parameters
     * @return McDomainDO
     */
     McDomainDO convertToDO(McDomainPageReqVO mcDomainPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcDomainSaveReqVO Save request parameters
     * @return McDomainDO
     */
     McDomainDO convertToDO(McDomainSaveReqVO mcDomainSaveReqVO);

    /**
     * DO to RespVO
     * @param mcDomainDO entity object
     * @return McDomainRespVO
     */
     McDomainRespVO convertToRespVO(McDomainDO mcDomainDO);

    /**
     * DOList to RespVOList
     * @param mcDomainDOList entity object list
     * @return List<McDomainRespVO>
     */
     List<McDomainRespVO> convertToRespVOList(List<McDomainDO> mcDomainDOList);
}
