package tech.qiantong.qdata.module.mc.convert.metadata;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTablePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McTableDO;

import java.util.List;

/**
 * Metadata information Convert
 *
 * @author qdata
 * @date 2026-02-11
 */
@Mapper
public interface McTableConvert {
    McTableConvert INSTANCE = Mappers.getMapper(McTableConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTablePageReqVO request parameters
     * @return McTableDO
     */
     McTableDO convertToDO(McTablePageReqVO mcTablePageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTableSaveReqVO save request parameters
     * @return McTableDO
     */
     McTableDO convertToDO(McTableSaveReqVO mcTableSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTableDO entity object
     * @return McTableRespVO
     */
     McTableRespVO convertToRespVO(McTableDO mcTableDO);

    /**
     * DOList to RespVOList
     * @param mcTableDOList entity object list
     * @return List<McTableRespVO>
     */
     List<McTableRespVO> convertToRespVOList(List<McTableDO> mcTableDOList);
}
