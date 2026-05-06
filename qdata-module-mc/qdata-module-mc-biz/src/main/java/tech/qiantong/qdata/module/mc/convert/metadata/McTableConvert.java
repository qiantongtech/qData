package tech.qiantong.qdata.module.mc.convert.metadata;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTablePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McTableSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McTableDO;

import java.util.List;

/**
 * 元数据信息 Convert
 *
 * @author qdata
 * @date 2026-02-11
 */
@Mapper
public interface McTableConvert {
    McTableConvert INSTANCE = Mappers.getMapper(McTableConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param mcTablePageReqVO 请求参数
     * @return McTableDO
     */
     McTableDO convertToDO(McTablePageReqVO mcTablePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param mcTableSaveReqVO 保存请求参数
     * @return McTableDO
     */
     McTableDO convertToDO(McTableSaveReqVO mcTableSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param mcTableDO 实体对象
     * @return McTableRespVO
     */
     McTableRespVO convertToRespVO(McTableDO mcTableDO);

    /**
     * DOList 转换为 RespVOList
     * @param mcTableDOList 实体对象列表
     * @return List<McTableRespVO>
     */
     List<McTableRespVO> convertToRespVOList(List<McTableDO> mcTableDOList);
}
