/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
