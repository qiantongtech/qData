package tech.qiantong.qdata.module.mc.convert.task;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskScopeDO;

import java.util.List;

/**
 * 采集范围 Convert
 *
 * @author qdata
 * @date 2025-12-16
 */
@Mapper
public interface McTaskScopeConvert {
    McTaskScopeConvert INSTANCE = Mappers.getMapper(McTaskScopeConvert.class);

    /**
     * PageReqVO 转换为 DO
     * @param mcTaskScopePageReqVO 请求参数
     * @return McTaskScopeDO
     */
     McTaskScopeDO convertToDO(McTaskScopePageReqVO mcTaskScopePageReqVO);

    /**
     * SaveReqVO 转换为 DO
     * @param mcTaskScopeSaveReqVO 保存请求参数
     * @return McTaskScopeDO
     */
     McTaskScopeDO convertToDO(McTaskScopeSaveReqVO mcTaskScopeSaveReqVO);

    /**
     * DO 转换为 RespVO
     * @param mcTaskScopeDO 实体对象
     * @return McTaskScopeRespVO
     */
     McTaskScopeRespVO convertToRespVO(McTaskScopeDO mcTaskScopeDO);

    /**
     * DOList 转换为 RespVOList
     * @param mcTaskScopeDOList 实体对象列表
     * @return List<McTaskScopeRespVO>
     */
     List<McTaskScopeRespVO> convertToRespVOList(List<McTaskScopeDO> mcTaskScopeDOList);
}
