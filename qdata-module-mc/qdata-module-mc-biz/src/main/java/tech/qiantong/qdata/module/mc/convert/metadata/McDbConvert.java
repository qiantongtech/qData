package tech.qiantong.qdata.module.mc.convert.metadata;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

import java.util.List;

/**
 * Database Convert
 *
 * @author qdata
 * @date 2026-02-11
 */
@Mapper
public interface McDbConvert {
    McDbConvert INSTANCE = Mappers.getMapper(McDbConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcDbPageReqVO request parameters
     * @return McDbDO
     */
     McDbDO convertToDO(McDbPageReqVO mcDbPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcDbSaveReqVO save request parameters
     * @return McDbDO
     */
     McDbDO convertToDO(McDbSaveReqVO mcDbSaveReqVO);

    /**
     * DO to RespVO
     * @param mcDbDO entity object
     * @return McDbRespVO
     */
     McDbRespVO convertToRespVO(McDbDO mcDbDO);

    /**
     * DOList to RespVOList
     * @param mcDbDOList entity object list
     * @return List<McDbRespVO>
     */
     List<McDbRespVO> convertToRespVOList(List<McDbDO> mcDbDOList);
}
