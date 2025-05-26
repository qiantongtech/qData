package tech.qiantong.qdata.api.ds.api.service.project;

import tech.qiantong.qdata.api.ds.api.project.DsProjectCreateReqDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectDeleteRespDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectRespDTO;
import tech.qiantong.qdata.api.ds.api.project.DsProjectUpdateReqDTO;

/**
 * <P>
 * 用途:ds项目相关接口
 * </p>
 *
 * @author: FXB
 * @create: 2025-02-18 14:26
 **/
public interface IDsProjectService {

    /**
     * 新增项目
     *
     * @param dsProjectCreateReqDTO
     * @return
     */
    DsProjectRespDTO saveProject(DsProjectCreateReqDTO dsProjectCreateReqDTO);

    /**
     * 修改项目
     *
     * @param dsProjectUpdateReqDTO
     * @return
     */
    DsProjectRespDTO updateProject(DsProjectUpdateReqDTO dsProjectUpdateReqDTO);

    /**
     * 删除项目
     *
     * @param projectCode 项目编码
     * @return
     */
    DsProjectDeleteRespDTO deleteProject(Long projectCode);
}
