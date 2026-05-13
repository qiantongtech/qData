package tech.qiantong.qdata.module.mc.convert.task;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstancePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskInstanceSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskInstanceDO;

import java.util.List;

/**
 * 采集任务实例 Convert
 *
 * @author qdata
 * @date 2025-12-16
 */
@Mapper
public interface McTaskInstanceConvert {
    McTaskInstanceConvert INSTANCE = Mappers.getMapper(McTaskInstanceConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param mcTaskInstancePageReqVO 请求参数
     * @return McTaskInstanceDO
     */
     McTaskInstanceDO convertToDO(McTaskInstancePageReqVO mcTaskInstancePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param mcTaskInstanceSaveReqVO 保存请求参数
     * @return McTaskInstanceDO
     */
     McTaskInstanceDO convertToDO(McTaskInstanceSaveReqVO mcTaskInstanceSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param mcTaskInstanceDO 实体对象
     * @return McTaskInstanceRespVO
     */
     McTaskInstanceRespVO convertToRespVO(McTaskInstanceDO mcTaskInstanceDO);

    /**
     * DOList 转换为 RespVOList
     * @param mcTaskInstanceDOList 实体对象列表
     * @return List<McTaskInstanceRespVO>
     */
     List<McTaskInstanceRespVO> convertToRespVOList(List<McTaskInstanceDO> mcTaskInstanceDOList);
}
