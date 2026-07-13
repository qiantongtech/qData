package tech.qiantong.qdata.module.mc.convert.tableLog;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableLog.McTableLogDO;

import java.util.List;

/**
 * Metadata Information - Log Convert
 *
 * @author qdata
 * @date 2026-03-10
 */
@Mapper
public interface McTableLogConvert {
    McTableLogConvert INSTANCE = Mappers.getMapper(McTableLogConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTableLogPageReqVO request parameters
     * @return McTableLogDO
     */
     McTableLogDO convertToDO(McTableLogPageReqVO mcTableLogPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTableLogSaveReqVO save request parameters
     * @return McTableLogDO
     */
     McTableLogDO convertToDO(McTableLogSaveReqVO mcTableLogSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTableLogDO entity object
     * @return McTableLogRespVO
     */
     McTableLogRespVO convertToRespVO(McTableLogDO mcTableLogDO);

    /**
     * DOList to RespVOList
     * @param mcTableLogDOList entity object list
     * @return List<McTableLogRespVO>
     */
     List<McTableLogRespVO> convertToRespVOList(List<McTableLogDO> mcTableLogDOList);
}
