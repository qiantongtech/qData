package tech.qiantong.qdata.module.att.api.project;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectReqDTO;
import tech.qiantong.qdata.module.att.api.project.dto.AttProjectRespDTO;

/**
 * <P>
 * 用途:项目相关接口
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-25 14:31
 **/
public interface IAttProjectApi {

    /**
     * 根据项目编码获取项目id
     *
     * @param projectCode
     * @return
     */
    Long getProjectIdByProjectCode(String projectCode);

    /**
     * 获得项目分页列表
     *
     * @param pageReqVO 分页请求
     * @return 项目分页列表
     */
    PageResult<AttProjectRespDTO> getAttProjectPage(AttProjectReqDTO pageReqVO);
}
