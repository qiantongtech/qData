package tech.qiantong.qdata.module.mc.convert.task;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskSchedulerDO;

import java.util.List;

/**
 * Data integration scheduling information Convert
 *
 * @author qdata
 * @date 2025-12-16
 */
@Mapper
public interface McTaskSchedulerConvert {
    McTaskSchedulerConvert INSTANCE = Mappers.getMapper(McTaskSchedulerConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTaskSchedulerPageReqVO request parameters
     * @return McTaskSchedulerDO
     */
     McTaskSchedulerDO convertToDO(McTaskSchedulerPageReqVO mcTaskSchedulerPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTaskSchedulerSaveReqVO save request parameters
     * @return McTaskSchedulerDO
     */
     McTaskSchedulerDO convertToDO(McTaskSchedulerSaveReqVO mcTaskSchedulerSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTaskSchedulerDO entity object
     * @return McTaskSchedulerRespVO
     */
     McTaskSchedulerRespVO convertToRespVO(McTaskSchedulerDO mcTaskSchedulerDO);

    /**
     * DOList to RespVOList
     * @param mcTaskSchedulerDOList entity object list
     * @return List<McTaskSchedulerRespVO>
     */
     List<McTaskSchedulerRespVO> convertToRespVOList(List<McTaskSchedulerDO> mcTaskSchedulerDOList);
}
