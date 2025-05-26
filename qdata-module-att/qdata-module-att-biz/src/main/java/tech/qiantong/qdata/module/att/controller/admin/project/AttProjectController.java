package tech.qiantong.qdata.module.att.controller.admin.project;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.api.ds.api.etl.DsStartTaskReqDTO;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlNodeService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlSchedulerService;
import tech.qiantong.qdata.api.ds.api.service.etl.IDsEtlTaskService;
import tech.qiantong.qdata.api.ds.api.service.project.IDsProjectService;
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
@RequestMapping("/att/attProject")
@Validated
public class AttProjectController extends BaseController {
    @Resource
    private IAttProjectService attProjectService;

    @Resource
    private IDsProjectService dsProjectService;

    @Operation(summary = "查询项目列表")
    @PreAuthorize("@ss.hasPermi('att:project:project:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AttProjectRespVO>> list(AttProjectPageReqVO attProject) {
        PageResult<AttProjectDO> page = attProjectService.getAttProjectPage(attProject);
        return CommonResult.success(BeanUtils.toBean(page, AttProjectRespVO.class));
    }

    @Operation(summary = "查询当前用户所属的项目列表")
    @PreAuthorize("@ss.hasPermi('att:project:project:list')")
    @GetMapping("/currentUser/list")
    public CommonResult<List<AttProjectRespVO>> currentUser() {
        List<AttProjectDO> list = attProjectService.getCurrentUserList(getUserId());
        return CommonResult.success(BeanUtils.toBean(list, AttProjectRespVO.class));
    }

    /**
     * 获取用户列表排除当前项目已经存在的用户
     */
    @PreAuthorize("@ss.hasPermi('att:project:project:list')")
    @PostMapping("/noProjectUser/list")
    public TableDataInfo list(@RequestBody JSONObject jsonObject) {
        startPage();
        List<SysUser> list = attProjectService.selectNoProjectUserList(jsonObject);
        return getDataTable(list);
    }

    @Operation(summary = "导入项目列表")
    @PreAuthorize("@ss.hasPermi('att:project:project:import')")
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
    @PreAuthorize("@ss.hasPermi('att:project:project:query')")
    @GetMapping(value = "/{ID}")
    public CommonResult<AttProjectRespVO> getInfo(@PathVariable("ID") Long ID) {
        AttProjectDO attProjectDO = attProjectService.getAttProjectById(ID);
        return CommonResult.success(BeanUtils.toBean(attProjectDO, AttProjectRespVO.class));
    }

    @Operation(summary = "获取当前用户是非具备用户添加和项目管理员")
    @PreAuthorize("@ss.hasPermi('att:project:project:query')")
    @GetMapping(value = "/addUserAndProject/{id}")
    public CommonResult<JSONObject> addUserAndProjectIsOk(@PathVariable("id") Long id) {
        return CommonResult.success(attProjectService.addUserAndProjectIsOk(getUserId(), id));
    }

    @Operation(summary = "新增项目")
    @PreAuthorize("@ss.hasPermi('att:project:project:add')")
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
    @PreAuthorize("@ss.hasPermi('att:project:project:edit')")
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
    @PreAuthorize("@ss.hasPermi('att:project:project:query')")
    @GetMapping(value = "/editProjectStatus/{id}/{status}")
    public AjaxResult editProjectStatus(@PathVariable Long id, @PathVariable Long status) {
        Boolean isOk = attProjectService.editProjectStatus(id, status);
        if (!isOk) {
            return AjaxResult.error("任务状态修改失败，请联系系统管理员");
        }
        return AjaxResult.success("修改成功");
    }

    @Operation(summary = "删除项目")
    @PreAuthorize("@ss.hasPermi('att:project:project:remove')")
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


    @Resource
    private IDsEtlNodeService dsEtlNodeService;

    @Resource
    private IDsEtlTaskService dsEtlTaskService;

    @Resource
    private IDsEtlSchedulerService dsEtlSchedulerService;

    @Operation(summary = "test")
    @DeleteMapping("/test")
    public CommonResult test() {
//项目保存
//        return CommonResult.success(dsProjectService.saveProject(DsProjectSaveReqDTO.builder()
//                .projectName("test22")
//                .description("test").build()));

        //项目修改
//        return CommonResult.success(dsProjectService.updateProject(DsProjectUpdateReqDTO.builder()
//                .projectCode(133566764912288l)
//                .projectName("test666")
//                .description("test").build()));

        //项目删除
//        return CommonResult.success(dsProjectService.deleteProject(133566764912288l));

//        return CommonResult.success(dsEtlNodeService.genCode(133143833067680l));
//        DsTaskSaveReqDTO params = DsTaskSaveReqDTO.builder()
//                .name("test")
//                .taskDefinitionJson("[{\"code\":3633901554624,\"description\":\"\",\"environmentCode\":133155949418208,\"isCache\":\"NO\",\"name\":\"spark2\",\"taskParams\":{\"localParams\":[],\"rawScript\":\"\",\"resourceList\":[],\"programType\":\"SCALA\",\"mainClass\":\"com\",\"mainJar\":{\"resourceName\":\"file:/dolphinscheduler/default/resources/spart-demo-1.0.jar\"},\"deployMode\":\"client\",\"yarnQueue\":\"\",\"master\":\"11\",\"driverCores\":1,\"driverMemory\":\"512M\",\"numExecutors\":2,\"executorMemory\":\"2G\",\"executorCores\":2,\"sqlExecutionType\":\"SCRIPT\"},\"taskPriority\":\"MEDIUM\",\"taskType\":\"SPARK\"}]")
//                .taskRelationJson("[{\"preTaskCode\":0,\"preTaskVersion\":0,\"postTaskCode\":3633901554624,\"postTaskVersion\":0,\"conditionType\":\"NONE\",\"conditionParams\":{}}]")
//                .locations("[{\"taskCode\":3633901554624,\"x\":294,\"y\":229}]")
//                .executionType("PARALLEL")
//                .build();
//        return CommonResult.success(dsEtlTaskService.createTask(params, 133143833067680L));


//        DsSchedulerSaveReqDTO params = DsSchedulerSaveReqDTO.builder()
//                .processDefinitionCode("133811896195008")
//                .schedule("{\"startTime\":\"2025-02-21 00:00:00\",\"endTime\":\"2125-02-21 00:00:00\",\"crontab\":\"0 0 * * * ? *\",\"timezoneId\":\"Asia/Shanghai\"}")
//                .failureStrategy("CONTINUE")
//                .workerGroup("default")
//                .tenantCode("default")
//                .build();
//        return CommonResult.success(dsEtlSchedulerService
//                .saveScheduler(params, "133143833067680"));

//        DsSchedulerUpdateReqDTO params = DsSchedulerUpdateReqDTO.builder()
//                .processDefinitionCode("133811896195008")
//                .schedule("{\"startTime\":\"2025-02-21 00:00:00\",\"endTime\":\"2125-02-21 00:00:00\",\"crontab\":\"0 0 * * * ? *\",\"timezoneId\":\"Asia/Shanghai\"}")
//                .failureStrategy("CONTINUE")
//                .workerGroup("default")
//                .tenantCode("default")
//                .id(6l)
//                .build();
//        return CommonResult.success(dsEtlSchedulerService
//                .updateScheduler(params, "133143833067680"));

//        return CommonResult.success(dsEtlTaskService
//                .releaseTask("ONLINE", "133143833067680", "133813745088448"));

//        return CommonResult.success(dsEtlSchedulerService
//                .onlineScheduler("133143833067680", 5l));
//        return CommonResult.success(dsEtlTaskService
//                .deleteTask("133143833067680", "133813745088448"));

//        DsTaskSaveReqDTO params = DsTaskSaveReqDTO.builder()
//                .name("spark")
//                .taskDefinitionJson("[{\"id\":20,\"code\":133455210749664,\"name\":\"test\",\"version\":15,\"description\":\"\",\"projectCode\":133143833067680,\"userId\":1,\"taskType\":\"SPARK\",\"taskParams\":{\"localParams\":[],\"rawScript\":\"\",\"resourceList\":[],\"programType\":\"JAVA\",\"mainClass\":\"com.demo.WordCount\",\"mainJar\":{\"resourceName\":\"file:/dolphinscheduler/default/resources/spart-demo-1.0.jar\"},\"deployMode\":\"client\",\"mainArgs\":\"/dolphinscheduler/default/resources/xx.txt /dolphinscheduler/default/resources/data\",\"others\":\"\",\"yarnQueue\":\"\",\"master\":\"spark://qiantong100:7077\",\"driverCores\":1,\"driverMemory\":\"512M\",\"numExecutors\":1,\"executorMemory\":\"1G\",\"executorCores\":1,\"sqlExecutionType\":\"SCRIPT\"},\"taskParamList\":[],\"taskParamMap\":null,\"flag\":\"YES\",\"isCache\":\"NO\",\"taskPriority\":\"MEDIUM\",\"userName\":null,\"projectName\":null,\"workerGroup\":\"default\",\"environmentCode\":133155949418208,\"failRetryTimes\":0,\"failRetryInterval\":1,\"timeoutFlag\":\"CLOSE\",\"timeoutNotifyStrategy\":null,\"timeout\":0,\"delayTime\":0,\"resourceIds\":null,\"createTime\":\"2025-02-17 10:12:54\",\"updateTime\":\"2025-02-19 10:43:48\",\"modifyBy\":null,\"taskGroupId\":0,\"taskGroupPriority\":0,\"cpuQuota\":-1,\"memoryMax\":-1,\"taskExecuteType\":null,\"operator\":1,\"operateTime\":\"2025-02-19 10:43:48\",\"dependence\":\"\"}]")
//                .taskRelationJson("[{\"name\":\"\",\"preTaskCode\":0,\"preTaskVersion\":0,\"postTaskCode\":133455210749664,\"postTaskVersion\":15,\"conditionType\":\"NONE\",\"conditionParams\":{}}]")
//                .locations("[{\"taskCode\":133455210749664,\"x\":225,\"y\":302}]")
//                .executionType("PARALLEL")
//                .build();
//        return CommonResult.success(dsEtlTaskService.updateTask(params, "133143833067680", "133455845807840"));

//        return CommonResult.success(dsEtlSchedulerService
//                .getByTaskCode("133143833067680", "134264726197728"));
//        return CommonResult.success(dsEtlNodeService.genCode(133143833067680l));

        DsStartTaskReqDTO params = DsStartTaskReqDTO.builder()
                .processDefinitionCode(136730279808576l)
                .failureStrategy("CONTINUE")
                .warningType("NONE")
                .processInstancePriority("MEDIUM")
                .scheduleTime("{\"complementStartDate\":\"2025-03-26 00:00:00\",\"complementEndDate\":\"2025-03-26 00:00:00\"}")
                .build();
        return CommonResult.success(dsEtlTaskService
                .startTask(params, "133143833067680"));
    }

}
