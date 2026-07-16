package tech.qiantong.qdata.module.mc.convert.task;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstancePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceDO;

import java.util.List;

/**
 * Collection task instance Convert
 *
 * @author qdata
 * @date 2025-12-16
 */
@Mapper
public interface McTaskInstanceConvert {
    McTaskInstanceConvert INSTANCE = Mappers.getMapper(McTaskInstanceConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTaskInstancePageReqVO request parameters
     * @return McTaskInstanceDO
     */
     McTaskInstanceDO convertToDO(McTaskInstancePageReqVO mcTaskInstancePageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTaskInstanceSaveReqVO Save request parameters
     * @return McTaskInstanceDO
     */
     McTaskInstanceDO convertToDO(McTaskInstanceSaveReqVO mcTaskInstanceSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTaskInstanceDO entity object
     * @return McTaskInstanceRespVO
     */
     McTaskInstanceRespVO convertToRespVO(McTaskInstanceDO mcTaskInstanceDO);

    /**
     * DOList to RespVOList
     * @param mcTaskInstanceDOList entity object list
     * @return List<McTaskInstanceRespVO>
     */
     List<McTaskInstanceRespVO> convertToRespVOList(List<McTaskInstanceDO> mcTaskInstanceDOList);
}
