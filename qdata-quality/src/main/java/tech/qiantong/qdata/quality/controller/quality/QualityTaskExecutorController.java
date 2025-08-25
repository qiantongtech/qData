package tech.qiantong.qdata.quality.controller.quality;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.quality.controller.quality.vo.CheckErrorDataReqDTO;
import tech.qiantong.qdata.quality.controller.quality.vo.QualityRuleQueryReqDTO;
import tech.qiantong.qdata.quality.service.quality.QualityTaskExecutorService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 数据服务类目管理Controller
 *
 * @author qdata
 * @date 2025-03-11
 */
@Tag(name = "数据服务类目管理")
@RestController
@RequestMapping("/quality/qualityTaskExecutor")
@Validated
public class QualityTaskExecutorController extends BaseController {
    @Resource
    private QualityTaskExecutorService qualityTaskExecutorService;


    /**
     * 错误数据分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param ruleId   规则id
     * @param reportId
     * @return
     */
    @PostMapping("/pageErrorData")
    public AjaxResult pageErrorData(@RequestBody CheckErrorDataReqDTO checkErrorDataReqDTO) {
        if (StringUtils.isBlank(checkErrorDataReqDTO.getReportId())) {
            return AjaxResult.error("参数不能为空");
        }
        return AjaxResult.success(qualityTaskExecutorService.pageErrorData(PageRequest.of(checkErrorDataReqDTO.getPageNum() - 1, checkErrorDataReqDTO.getPageSize()), checkErrorDataReqDTO));
    }

    /**
     * 修改错误数据
     */
    @PostMapping("/updateErrorData")
    public AjaxResult updateErrorData(@RequestBody CheckErrorDataReqDTO dto) {
        boolean success = qualityTaskExecutorService.updateErrorData(dto);
        return AjaxResult.success(success); // data: true / false
    }


    /**
     * 正确数据分页查询
     *
     * @return
     */
    @PostMapping("/generateValidationValidDataSql")
    public AjaxResult generateValidationValidDataSql(@RequestBody QualityRuleQueryReqDTO queryReqDTO) {
        return AjaxResult.success(qualityTaskExecutorService.generateValidationValidDataSql(queryReqDTO));
    }

    /**
     * 错误数据
     *
     * @return
     */
    @PostMapping("/generateValidationErrorDataSql")
    public AjaxResult generateValidationErrorDataSql(@RequestBody   QualityRuleQueryReqDTO queryReqDTO) {
        return AjaxResult.success(qualityTaskExecutorService.generateValidationErrorDataSql(queryReqDTO));
    }


    /**
     *
     * @param id
     * @return
     */
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/runExecuteTask/{id}")
    public AjaxResult runExecuteTask(@PathVariable("id") String id) {
        qualityTaskExecutorService.executeTask(id);
        return success() ;
    }

}
