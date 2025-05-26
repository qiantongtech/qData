package tech.qiantong.qdata.module.dp.convert.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelMaterializedSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelMaterializedDO;

import java.util.List;

/**
 * 物化模型记录 Convert
 *
 * @author qdata
 * @date 2025-01-21
 */
@Mapper
public interface DpModelMaterializedConvert {
    DpModelMaterializedConvert INSTANCE = Mappers.getMapper(DpModelMaterializedConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dpModelMaterializedPageReqVO 请求参数
     * @return DpModelMaterializedDO
     */
     DpModelMaterializedDO convertToDO(DpModelMaterializedPageReqVO dpModelMaterializedPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dpModelMaterializedSaveReqVO 保存请求参数
     * @return DpModelMaterializedDO
     */
     DpModelMaterializedDO convertToDO(DpModelMaterializedSaveReqVO dpModelMaterializedSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dpModelMaterializedDO 实体对象
     * @return DpModelMaterializedRespVO
     */
     DpModelMaterializedRespVO convertToRespVO(DpModelMaterializedDO dpModelMaterializedDO);

    /**
     * DOList 转换为 RespVOList
     * @param dpModelMaterializedDOList 实体对象列表
     * @return List<DpModelMaterializedRespVO>
     */
     List<DpModelMaterializedRespVO> convertToRespVOList(List<DpModelMaterializedDO> dpModelMaterializedDOList);
}
