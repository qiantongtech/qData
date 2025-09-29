package tech.qiantong.qdata.module.att.controller.admin.project;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.domain.CommonResult;
import tech.qiantong.qdata.common.core.domain.entity.SysUser;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.core.page.TableDataInfo;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectRespVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttProjectSaveReqVO;
import tech.qiantong.qdata.module.att.controller.admin.project.vo.AttSysUserReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.project.AttProjectDO;
import tech.qiantong.qdata.module.att.service.project.IAttProjectService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 项目Controller
 *
 * @author shu
 * @date 2025-01-20
 */
@Tag(name = "项目")
@RestController
@RequestMapping("/att/project")
@Validated
public class AttProjectController extends BaseController {
    @Resource
    private IAttProjectService attProjectService;

    @Operation(summary = "查询项目列表")
    @PreAuthorize("@ss.hasPermi('att:project:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AttProjectRespVO>> list(AttProjectPageReqVO attProject) {
        PageResult<AttProjectDO> page = attProjectService.getAttProjectPage(attProject);
        return CommonResult.success(BeanUtils.toBean(page, AttProjectRespVO.class));
    }

    @Operation(summary = "查询当前用户所属的项目列表")
    @GetMapping("/currentUser/list")
    public CommonResult<List<AttProjectRespVO>> currentUser() {
        List<AttProjectDO> list = attProjectService.getCurrentUserList(getUserId());
        return CommonResult.success(BeanUtils.toBean(list, AttProjectRespVO.class));
    }

    /**
     * 获取用户列表排除当前项目已经存在的用户
     */
    @PreAuthorize("@ss.hasPermi('att:project:list')")
    @PostMapping("/noProjectUser/list")
    public TableDataInfo list(AttSysUserReqVO user) {
        List<SysUser> list = attProjectService.selectNoProjectUserList(user);
        return getDataTable(list);
    }

    @Operation(summary = "导入项目列表")
    @PreAuthorize("@ss.hasPermi('att:project:import')")
    @Log(title = "项目", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttProjectRespVO> util = new ExcelUtil<>(AttProjectRespVO.class);
        List<AttProjectRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attProjectService.importAttProject(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取项目详细信息")
    @PreAuthorize("@ss.hasPermi('att:project:query')")
    @GetMapping(value = "/{ID}")
    public CommonResult<AttProjectRespVO> getInfo(@PathVariable("ID") Long ID) {
        AttProjectDO attProjectDO = attProjectService.getAttProjectById(ID);
        return CommonResult.success(BeanUtils.toBean(attProjectDO, AttProjectRespVO.class));
    }

    @Operation(summary = "获取当前用户是非具备用户添加和项目管理员")
    @PreAuthorize("@ss.hasPermi('att:project:query')")
    @GetMapping(value = "/addUserAndProject/{id}")
    public CommonResult<JSONObject> addUserAndProjectIsOk(@PathVariable("id") Long id) {
        return CommonResult.success(attProjectService.addUserAndProjectIsOk(getUserId(), id));
    }

    @Operation(summary = "新增项目")
    @PreAuthorize("@ss.hasPermi('att:project:add')")
    @Log(title = "项目", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttProjectSaveReqVO attProject) {
        attProject.setCreatorId(getUserId());
        attProject.setCreateBy(getNickName());
        attProject.setCreateTime(DateUtil.date());
        Long serviceAttProject = attProjectService.createAttProject(attProject);
        if (serviceAttProject == -1) {
            return CommonResult.error(serviceAttProject.intValue(), "创建失败，请检查海豚调度器是否宕机或者是否存在该数据!");
        }
        if (serviceAttProject == -2) {
            return CommonResult.error(serviceAttProject.intValue(), "创建失败!");
        }
        return CommonResult.toAjax(serviceAttProject);
    }

    @Operation(summary = "修改项目")
    @PreAuthorize("@ss.hasPermi('att:project:edit')")
    @Log(title = "项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttProjectSaveReqVO attProject) {
        attProject.setUpdatorId(getUserId());
        attProject.setUpdateBy(getNickName());
        attProject.setUpdateTime(DateUtil.date());
        int i = attProjectService.updateAttProject(attProject);
        if (i == -1) {
            return CommonResult.error(i, "修改失败！");
        }
        return CommonResult.toAjax(i);
    }

    @Operation(summary = "修改项目状态")
    @PreAuthorize("@ss.hasPermi('att:project:query')")
    @GetMapping(value = "/editProjectStatus/{id}/{status}")
    public AjaxResult editProjectStatus(@PathVariable Long id, @PathVariable Long status) {
        Boolean isOk = attProjectService.editProjectStatus(id, status);
        if (!isOk) {
            return AjaxResult.error("任务状态修改失败，请联系系统管理员");
        }
        return AjaxResult.success("修改成功");
    }

    @Operation(summary = "删除项目")
    @PreAuthorize("@ss.hasPermi('att:project:remove')")
    @Log(title = "项目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        int project = attProjectService.removeAttProject(Arrays.asList(ids));
        if (project == -1) {
            return CommonResult.error(500, "删除失败，项目有人员存在!");
        } else if (project == -2) {
            return CommonResult.error(500, "删除失败，检查海豚调度器是否宕机!");
        }
        return CommonResult.toAjax(project);
    }

}
