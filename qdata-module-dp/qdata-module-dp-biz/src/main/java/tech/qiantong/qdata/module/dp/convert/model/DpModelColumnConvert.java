package tech.qiantong.qdata.module.dp.convert.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.model.vo.DpModelColumnSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelColumnDO;

import java.util.List;

/**
 * 逻辑模型属性信息 Convert
 *
 * @author qdata
 * @date 2025-01-21
 */
@Mapper
public interface DpModelColumnConvert {
    DpModelColumnConvert INSTANCE = Mappers.getMapper(DpModelColumnConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param dpModelColumnPageReqVO 请求参数
     * @return DpModelColumnDO
     */
     DpModelColumnDO convertToDO(DpModelColumnPageReqVO dpModelColumnPageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param dpModelColumnSaveReqVO 保存请求参数
     * @return DpModelColumnDO
     */
     DpModelColumnDO convertToDO(DpModelColumnSaveReqVO dpModelColumnSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param dpModelColumnDO 实体对象
     * @return DpModelColumnRespVO
     */
     DpModelColumnRespVO convertToRespVO(DpModelColumnDO dpModelColumnDO);

    /**
     * DOList 转换为 RespVOList
     * @param dpModelColumnDOList 实体对象列表
     * @return List<DpModelColumnRespVO>
     */
     List<DpModelColumnRespVO> convertToRespVOList(List<DpModelColumnDO> dpModelColumnDOList);
}
