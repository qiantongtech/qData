package tech.qiantong.qdata.module.system.api.flowMyBusiness.service;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.system.api.flowMyBusiness.dto.SystemFlowMyBusinessReqDTO;
import tech.qiantong.qdata.module.system.api.flowMyBusiness.dto.SystemFlowMyBusinessRespDTO;

import java.util.List;

public interface SysFlowMyBusinessService {

    /**
     * 创建审批记录
     *
     * @param flowMyBusinessReqDTO 审批记录信息
     * @return 审批记录编号
     */
    Long createSysFlowMyBusiness(SystemFlowMyBusinessReqDTO flowMyBusinessReqDTO);

    /**
     * 更新审批记录
     *
     * @param updateReqVO 审批记录信息
     */
    int updateSysFlowMyBusiness(SystemFlowMyBusinessReqDTO updateReqVO);

    /**
     * 分页查询服务申请记录
     *
     * @param updateReqVO 查询服务申请记录
     */
    PageResult<SystemFlowMyBusinessRespDTO> listPage(SystemFlowMyBusinessReqDTO updateReqVO);

    /**
     * 查询服务申请记录
     *
     * @param updateReqVO 查询服务申请记录条件
     */
    List<SystemFlowMyBusinessRespDTO> list(SystemFlowMyBusinessReqDTO updateReqVO);

    /**
     * 删除数据
     *
     * @param updateReqVO 删除条件
     */
    int removeFlowMyBusiness(SystemFlowMyBusinessReqDTO updateReqVO);
}
