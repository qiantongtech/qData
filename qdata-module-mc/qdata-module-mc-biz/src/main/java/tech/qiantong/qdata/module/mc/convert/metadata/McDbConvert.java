package tech.qiantong.qdata.module.mc.convert.metadata;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbPageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McDbSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.metadata.McDbDO;

import java.util.List;

/**
 * 数据库 Convert
 *
 * @author qdata
 * @date 2026-02-11
 */
@Mapper
public interface McDbConvert {
    McDbConvert INSTANCE = Mappers.getMapper(McDbConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param mcDbPageReqVO 请求参数
     * @return McDbDO
     */
     McDbDO convertToDO(McDbPageReqVO mcDbPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param mcDbSaveReqVO 保存请求参数
     * @return McDbDO
     */
     McDbDO convertToDO(McDbSaveReqVO mcDbSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param mcDbDO 实体对象
     * @return McDbRespVO
     */
     McDbRespVO convertToRespVO(McDbDO mcDbDO);

    /**
     * DOList 转换为 RespVOList
     * @param mcDbDOList 实体对象列表
     * @return List<McDbRespVO>
     */
     List<McDbRespVO> convertToRespVOList(List<McDbDO> mcDbDOList);
}
