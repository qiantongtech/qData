package tech.qiantong.qdata.module.mc.convert.task;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceLogDO;

import java.util.List;

/**
 * 采集任务实例-日志 Convert
 *
 * @author qdata
 * @date 2025-12-16
 */
@Mapper
public interface McTaskInstanceLogConvert {
    McTaskInstanceLogConvert INSTANCE = Mappers.getMapper(McTaskInstanceLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param mcTaskInstanceLogPageReqVO 请求参数
     * @return McTaskInstanceLogDO
     */
     McTaskInstanceLogDO convertToDO(McTaskInstanceLogPageReqVO mcTaskInstanceLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param mcTaskInstanceLogSaveReqVO 保存请求参数
     * @return McTaskInstanceLogDO
     */
     McTaskInstanceLogDO convertToDO(McTaskInstanceLogSaveReqVO mcTaskInstanceLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param mcTaskInstanceLogDO 实体对象
     * @return McTaskInstanceLogRespVO
     */
     McTaskInstanceLogRespVO convertToRespVO(McTaskInstanceLogDO mcTaskInstanceLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param mcTaskInstanceLogDOList 实体对象列表
     * @return List<McTaskInstanceLogRespVO>
     */
     List<McTaskInstanceLogRespVO> convertToRespVOList(List<McTaskInstanceLogDO> mcTaskInstanceLogDOList);
}
