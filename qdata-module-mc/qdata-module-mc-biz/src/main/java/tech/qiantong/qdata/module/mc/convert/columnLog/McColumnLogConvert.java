package tech.qiantong.qdata.module.mc.convert.columnLog;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.columnLog.McColumnLogDO;

import java.util.List;

/**
 * Metadata field information - Log Convert
 *
 * @author qdata
 * @date 2026-03-10
 */
@Mapper
public interface McColumnLogConvert {
    McColumnLogConvert INSTANCE = Mappers.getMapper(McColumnLogConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcColumnLogPageReqVO request parameters
     * @return McColumnLogDO
     */
     McColumnLogDO convertToDO(McColumnLogPageReqVO mcColumnLogPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcColumnLogSaveReqVO save request parameters
     * @return McColumnLogDO
     */
     McColumnLogDO convertToDO(McColumnLogSaveReqVO mcColumnLogSaveReqVO);

    /**
     * DO to RespVO
     * @param mcColumnLogDO entity object
     * @return McColumnLogRespVO
     */
     McColumnLogRespVO convertToRespVO(McColumnLogDO mcColumnLogDO);

    /**
     * DOList to RespVOList
     * @param mcColumnLogDOList entity object list
     * @return List<McColumnLogRespVO>
     */
     List<McColumnLogRespVO> convertToRespVOList(List<McColumnLogDO> mcColumnLogDOList);
}
