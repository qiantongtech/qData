package tech.qiantong.qdata.module.mc.convert.task;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopePageReqVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeRespVO;
import tech.qiantong.qdata.module.mc.controller.admin.task.vo.McTaskScopeSaveReqVO;
import tech.qiantong.qdata.module.mc.dal.dataobject.task.McTaskScopeDO;

import java.util.List;

/**
 * Collection range Convert
 *
 * @author qdata
 * @date 2025-12-16
 */
@Mapper
public interface McTaskScopeConvert {
    McTaskScopeConvert INSTANCE = Mappers.getMapper(McTaskScopeConvert.class);

    /**
     * PageReqVO converted to DO
     * @param mcTaskScopePageReqVO request parameters
     * @return McTaskScopeDO
     */
     McTaskScopeDO convertToDO(McTaskScopePageReqVO mcTaskScopePageReqVO);

    /**
     * SaveReqVO converted to DO
     * @param mcTaskScopeSaveReqVO save request parameters
     * @return McTaskScopeDO
     */
     McTaskScopeDO convertToDO(McTaskScopeSaveReqVO mcTaskScopeSaveReqVO);

    /**
     * DO to RespVO
     * @param mcTaskScopeDO entity object
     * @return McTaskScopeRespVO
     */
     McTaskScopeRespVO convertToRespVO(McTaskScopeDO mcTaskScopeDO);

    /**
     * DOList to RespVOList
     * @param mcTaskScopeDOList entity object list
     * @return List<McTaskScopeRespVO>
     */
     List<McTaskScopeRespVO> convertToRespVOList(List<McTaskScopeDO> mcTaskScopeDOList);
}
