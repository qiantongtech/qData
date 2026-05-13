package tech.qiantong.qdata.module.mc.convert.tableLog;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableLog.vo.McTableLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableLog.McTableLogDO;

import java.util.List;

/**
 * 元数据信息 - 日志 Convert
 *
 * @author qdata
 * @date 2026-03-10
 */
@Mapper
public interface McTableLogConvert {
    McTableLogConvert INSTANCE = Mappers.getMapper(McTableLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param mcTableLogPageReqVO 请求参数
     * @return McTableLogDO
     */
     McTableLogDO convertToDO(McTableLogPageReqVO mcTableLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param mcTableLogSaveReqVO 保存请求参数
     * @return McTableLogDO
     */
     McTableLogDO convertToDO(McTableLogSaveReqVO mcTableLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param mcTableLogDO 实体对象
     * @return McTableLogRespVO
     */
     McTableLogRespVO convertToRespVO(McTableLogDO mcTableLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param mcTableLogDOList 实体对象列表
     * @return List<McTableLogRespVO>
     */
     List<McTableLogRespVO> convertToRespVOList(List<McTableLogDO> mcTableLogDOList);
}
