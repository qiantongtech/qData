package tech.qiantong.qdata.module.mc.convert.columnLog;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.columnLog.vo.McColumnLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.columnLog.McColumnLogDO;

import java.util.List;

/**
 * 元数据字段信息 - 日志 Convert
 *
 * @author qdata
 * @date 2026-03-10
 */
@Mapper
public interface McColumnLogConvert {
    McColumnLogConvert INSTANCE = Mappers.getMapper(McColumnLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param mcColumnLogPageReqVO 请求参数
     * @return McColumnLogDO
     */
     McColumnLogDO convertToDO(McColumnLogPageReqVO mcColumnLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param mcColumnLogSaveReqVO 保存请求参数
     * @return McColumnLogDO
     */
     McColumnLogDO convertToDO(McColumnLogSaveReqVO mcColumnLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param mcColumnLogDO 实体对象
     * @return McColumnLogRespVO
     */
     McColumnLogRespVO convertToRespVO(McColumnLogDO mcColumnLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param mcColumnLogDOList 实体对象列表
     * @return List<McColumnLogRespVO>
     */
     List<McColumnLogRespVO> convertToRespVOList(List<McColumnLogDO> mcColumnLogDOList);
}
