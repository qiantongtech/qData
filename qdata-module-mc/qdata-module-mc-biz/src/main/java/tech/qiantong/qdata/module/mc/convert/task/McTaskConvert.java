package tech.qiantong.qdata.module.mc.convert.task;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskDO;

import java.util.List;

/**
 * 采集任务 Convert
 *
 * @author qdata
 * @date 2025-12-16
 */
@Mapper
public interface McTaskConvert {
    McTaskConvert INSTANCE = Mappers.getMapper(McTaskConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param mcTaskPageReqVO 请求参数
     * @return McTaskDO
     */
     McTaskDO convertToDO(McTaskPageReqVO mcTaskPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param mcTaskSaveReqVO 保存请求参数
     * @return McTaskDO
     */
     McTaskDO convertToDO(McTaskSaveReqVO mcTaskSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param mcTaskDO 实体对象
     * @return McTaskRespVO
     */
     McTaskRespVO convertToRespVO(McTaskDO mcTaskDO);

    /**
     * DOList 转换为 RespVOList
     * @param mcTaskDOList 实体对象列表
     * @return List<McTaskRespVO>
     */
     List<McTaskRespVO> convertToRespVOList(List<McTaskDO> mcTaskDOList);
}
