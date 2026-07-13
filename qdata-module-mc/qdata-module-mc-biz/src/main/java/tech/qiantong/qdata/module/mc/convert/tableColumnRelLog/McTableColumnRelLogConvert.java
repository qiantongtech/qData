package tech.qiantong.qdata.module.mc.convert.tableColumnRelLog;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableColumnRelLog.McTableColumnRelLogDO;

import java.util.List;

/**
 * Metadata database and information and field information relationship-Log Convert
 *
 * @author qdata
 * @date 2026-03-10
 */
@Mapper
public interface McTableColumnRelLogConvert {
    McTableColumnRelLogConvert INSTANCE = Mappers.getMapper(McTableColumnRelLogConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTableColumnRelLogPageReqVO request parameters
     * @return McTableColumnRelLogDO
     */
     McTableColumnRelLogDO convertToDO(McTableColumnRelLogPageReqVO mcTableColumnRelLogPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTableColumnRelLogSaveReqVO Save request parameters
     * @return McTableColumnRelLogDO
     */
     McTableColumnRelLogDO convertToDO(McTableColumnRelLogSaveReqVO mcTableColumnRelLogSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTableColumnRelLogDO entity object
     * @return McTableColumnRelLogRespVO
     */
     McTableColumnRelLogRespVO convertToRespVO(McTableColumnRelLogDO mcTableColumnRelLogDO);

    /**
     * DOList to RespVOList
     * @param mcTableColumnRelLogDOList entity object list
     * @return List<McTableColumnRelLogRespVO>
     */
     List<McTableColumnRelLogRespVO> convertToRespVOList(List<McTableColumnRelLogDO> mcTableColumnRelLogDOList);
}
