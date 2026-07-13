package tech.qiantong.qdata.module.mc.convert.task;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskDO;

import java.util.List;

/**
 * Collection task Convert
 *
 * @author qdata
 * @date 2025-12-16
 */
@Mapper
public interface McTaskConvert {
    McTaskConvert INSTANCE = Mappers.getMapper(McTaskConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTaskPageReqVO request parameters
     * @return McTaskDO
     */
     McTaskDO convertToDO(McTaskPageReqVO mcTaskPageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTaskSaveReqVO save request parameters
     * @return McTaskDO
     */
     McTaskDO convertToDO(McTaskSaveReqVO mcTaskSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTaskDO entity object
     * @return McTaskRespVO
     */
     McTaskRespVO convertToRespVO(McTaskDO mcTaskDO);

    /**
     * DOList to RespVOList
     * @param mcTaskDOList entity object list
     * @return List<McTaskRespVO>
     */
     List<McTaskRespVO> convertToRespVOList(List<McTaskDO> mcTaskDOList);
}
