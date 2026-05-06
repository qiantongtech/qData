package tech.qiantong.qdata.module.mc.convert.tableColumnRelLog;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.tableColumnRelLog.vo.McTableColumnRelLogSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.tableColumnRelLog.McTableColumnRelLogDO;

import java.util.List;

/**
 * 元数据数据库与信息及字段信息关系-日志 Convert
 *
 * @author qdata
 * @date 2026-03-10
 */
@Mapper
public interface McTableColumnRelLogConvert {
    McTableColumnRelLogConvert INSTANCE = Mappers.getMapper(McTableColumnRelLogConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param mcTableColumnRelLogPageReqVO 请求参数
     * @return McTableColumnRelLogDO
     */
     McTableColumnRelLogDO convertToDO(McTableColumnRelLogPageReqVO mcTableColumnRelLogPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param mcTableColumnRelLogSaveReqVO 保存请求参数
     * @return McTableColumnRelLogDO
     */
     McTableColumnRelLogDO convertToDO(McTableColumnRelLogSaveReqVO mcTableColumnRelLogSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param mcTableColumnRelLogDO 实体对象
     * @return McTableColumnRelLogRespVO
     */
     McTableColumnRelLogRespVO convertToRespVO(McTableColumnRelLogDO mcTableColumnRelLogDO);

    /**
     * DOList 转换为 RespVOList
     * @param mcTableColumnRelLogDOList 实体对象列表
     * @return List<McTableColumnRelLogRespVO>
     */
     List<McTableColumnRelLogRespVO> convertToRespVOList(List<McTableColumnRelLogDO> mcTableColumnRelLogDOList);
}
