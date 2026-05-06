package tech.qiantong.qdata.module.mc.convert.task;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskSchedulerSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskSchedulerDO;

import java.util.List;

/**
 * 数据集成调度信息 Convert
 *
 * @author qdata
 * @date 2025-12-16
 */
@Mapper
public interface McTaskSchedulerConvert {
    McTaskSchedulerConvert INSTANCE = Mappers.getMapper(McTaskSchedulerConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param mcTaskSchedulerPageReqVO 请求参数
     * @return McTaskSchedulerDO
     */
     McTaskSchedulerDO convertToDO(McTaskSchedulerPageReqVO mcTaskSchedulerPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param mcTaskSchedulerSaveReqVO 保存请求参数
     * @return McTaskSchedulerDO
     */
     McTaskSchedulerDO convertToDO(McTaskSchedulerSaveReqVO mcTaskSchedulerSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param mcTaskSchedulerDO 实体对象
     * @return McTaskSchedulerRespVO
     */
     McTaskSchedulerRespVO convertToRespVO(McTaskSchedulerDO mcTaskSchedulerDO);

    /**
     * DOList 转换为 RespVOList
     * @param mcTaskSchedulerDOList 实体对象列表
     * @return List<McTaskSchedulerRespVO>
     */
     List<McTaskSchedulerRespVO> convertToRespVOList(List<McTaskSchedulerDO> mcTaskSchedulerDOList);
}
