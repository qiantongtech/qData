package tech.qiantong.qdata.module.mc.convert.metadata;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McColumnDO;

import java.util.List;

/**
 * Metadata field information Convert
 *
 * @author qdata
 * @date 2026-02-11
 */
@Mapper
public interface McColumnConvert {
    McColumnConvert INSTANCE = Mappers.getMapper(McColumnConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcColumnPageReqVO request parameters
     * @return McColumnDTO
     */
     McColumnDO convertToDO(McColumnPageReqVO mcColumnPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcColumnSaveReqVO save request parameters
     * @return McColumnDTO
     */
     McColumnDO convertToDO(McColumnSaveReqVO mcColumnSaveReqVO);

    /**
     * DO to RespVO
     * @param mcColumnDO entity object
     * @return McColumnRespDTO
     */
     McColumnRespVO convertToRespVO(McColumnDO mcColumnDO);

    /**
     * DOList to RespVOList
     * @param mcColumnDOList entity object list
     * @return List<McColumnRespDTO>
     */
     List<McColumnRespVO> convertToRespVOList(List<McColumnDO> mcColumnDOList);
}
