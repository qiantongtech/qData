package tech.qiantong.qdata.module.mc.convert.metadata;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McColumnDO;

import java.util.List;

/**
 * 元数据字段信息 Convert
 *
 * @author qdata
 * @date 2026-02-11
 */
@Mapper
public interface McColumnConvert {
    McColumnConvert INSTANCE = Mappers.getMapper(McColumnConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param mcColumnPageReqVO 请求参数
     * @return McColumnDTO
     */
     McColumnDO convertToDO(McColumnPageReqVO mcColumnPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param mcColumnSaveReqVO 保存请求参数
     * @return McColumnDTO
     */
     McColumnDO convertToDO(McColumnSaveReqVO mcColumnSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param mcColumnDO 实体对象
     * @return McColumnRespDTO
     */
     McColumnRespVO convertToRespVO(McColumnDO mcColumnDO);

    /**
     * DOList 转换为 RespVOList
     * @param mcColumnDOList 实体对象列表
     * @return List<McColumnRespDTO>
     */
     List<McColumnRespVO> convertToRespVOList(List<McColumnDO> mcColumnDOList);
}
