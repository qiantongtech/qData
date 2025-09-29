package tech.qiantong.qdata.module.att.service.project;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectRespVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectSaveReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttSysUserReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 项目Service接口
 *
 * @author shu
 * @date 2025-01-20
 */
public interface IAttProjectService extends IService<AttProjectDO> {

    /**
     * 获得项目分页列表
     *
     * @param pageReqVO 分页请求
     * @return 项目分页列表
     */
    PageResult<AttProjectDO> getAttProjectPage(AttProjectPageReqVO pageReqVO);

    /**
     * 创建项目
     *
     * @param createReqVO 项目信息
     * @return 项目编号
     */
    Long createAttProject(AttProjectSaveReqVO createReqVO);

    /**
     * 更新项目
     *
     * @param updateReqVO 项目信息
     */
    int updateAttProject(AttProjectSaveReqVO updateReqVO);

    /**
     * 删除项目
     *
     * @param idList 项目编号
     */
    int removeAttProject(Collection<Long> idList);

    /**
     * 获得项目详情
     *
     * @param id 项目编号
     * @return 项目
     */
    AttProjectDO getAttProjectById(Long id);

    /**
     * 获得全部项目列表
     *
     * @return 项目列表
     */
    List<AttProjectDO> getAttProjectList();

    /**
     * 获得全部项目 Map
     *
     * @return 项目 Map
     */
    Map<Long, AttProjectDO> getAttProjectMap();


    /**
     * 导入项目数据
     *
     * @param importExcelList 项目数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importAttProject(List<AttProjectRespVO> importExcelList, boolean isUpdateSupport, String operName);

    /**
     * 获取当前用户是非具备用户添加和项目管理员
     *
     * @param userId 用户ID
     * @return
     */
    JSONObject addUserAndProjectIsOk(Long userId, Long id);

    /**
     * 查询当前用户所属的项目列表
     *
     * @param userId 用户id
     * @return
     */
    List<AttProjectDO> getCurrentUserList(Long userId);

    /**
     * 获取用户列表排除当前项目已经存在的用户
     */
    List<SysUser> selectNoProjectUserList(AttSysUserReqVO user);

    Boolean editProjectStatus(Long id,Long status);
}
