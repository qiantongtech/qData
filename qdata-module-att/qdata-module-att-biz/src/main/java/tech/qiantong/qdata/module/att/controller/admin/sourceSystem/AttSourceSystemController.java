/*
 * Copyright (c) 2026 Jiangsu Qiantong Technology Co., Ltd.
 *  *
 * Software Name: qData Data Middle Platform (Commercial Edition)
 * Software Copyright Registration No. 16069171
 *  *
 * [RIGHTS AND LICENSE STATEMENT]
 * This file contains non-public commercial source code of which Jiangsu Qiantong
 * Technology Co., Ltd. lawfully possesses complete intellectual property rights.
 *  *
 * Access and use are limited to entities or individuals who have signed a valid
 * commercial license agreement, within the scope stipulated in the agreement.
 * The "accessibility" of this source code is premised on lawful authorization
 * and does not constitute any form of transfer of intellectual property rights
 * or implied licensing.
 *  *
 * [PROHIBITIONS]
 * Unless explicitly agreed in the license agreement, the following acts in any
 * form are strictly prohibited:
 * 1. Copying, disseminating, disclosing, selling, renting, or redistributing
 * this source code;
 * 2. Providing the software's functionality to third parties via SaaS, PaaS,
 * cloud hosting, or other means;
 * 3. Using this software or its derivative versions to develop products that
 * compete with the Right Holder;
 * 4. Providing or displaying this source code or related technical information
 * to unauthorized third parties;
 * 5. Tampering with, circumventing, or destroying copyright notices, license
 * verifications, or other technical protection measures.
 *  *
 * [LEGAL LIABILITY]
 * Any unauthorized use constitutes an infringement of trade secrets and
 * intellectual property rights.
 *  *
 * The Right Holder will strictly pursue liability for breach of contract and
 * infringement in accordance with the commercial agreement and laws such as
 * the "Copyright Law of the People's Republic of China" and the "Anti-Unfair
 * Competition Law".
 *  *
 * ============================================================================
 *  *
 * Copyright (c) 2026 江苏千桐科技有限公司
 *  *
 * 软件名称：qData 数据中台（商业版） | 软著登字第16069171号
 *  *
 * 【权利与授权声明】
 * 本文件属于江苏千桐科技有限公司依法享有完全知识产权的非公开商业源代码。
 * 仅限已签署有效商业授权合同的单位或个人在约定范围内查阅和使用。
 * 源代码的“可访问性”均以合法授权为前提，不构成任何形式的知识产权转让或默示授权。
 *  *
 * 【禁止事项】
 * 除授权合同明确约定外，严禁任何形式的：
 * 1. 复制、传播、披露、出售、出租或再分发本源代码；
 * 2. 通过 SaaS、PaaS、云托管等方式向第三方提供本软件功能；
 * 3. 将本软件或其衍生版本用于开发与权利人构成竞争的产品；
 * 4. 向未授权第三方提供或展示本源代码或相关技术信息；
 * 5. 篡改、规避或破坏版权标识、授权校验及其他技术保护措施。
 *  *
 * 【法律责任】
 * 任何未经授权的利用行为，均构成对商业秘密及知识产权的侵害。
 * 权利人将依据商业合同及《中华人民共和国著作权法》《反不正当竞争法》
 * 等法律法规，严厉追究违约与侵权责任。
 */

package tech.qiantong.qdata.module.att.controller.admin.sourceSystem;

import cn.hutool.core.date.DateUtil;
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
import tech.qiantong.qdata.common.core.page.PageParam;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemRespVO;
import tech.qiantong.qdata.module.att.controller.admin.sourceSystem.vo.AttSourceSystemSaveReqVO;
import tech.qiantong.qdata.module.att.convert.sourceSystem.AttSourceSystemConvert;
import tech.qiantong.qdata.module.att.dal.dataobject.sourceSystem.AttSourceSystemDO;
import tech.qiantong.qdata.module.att.service.sourceSystem.IAttSourceSystemService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 来源系统Controller
 *
 * @author qdata
 * @date 2026-04-03
 */
@Tag(name = "来源系统")
@RestController
@RequestMapping("/att/sourceSystem")
@Validated
public class AttSourceSystemController extends BaseController {
    @Resource
    private IAttSourceSystemService attSourceSystemService;

    @Operation(summary = "查询来源系统列表")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:list')")
    @GetMapping("/list")
    public CommonResult<PageResult<AttSourceSystemRespVO>> list(AttSourceSystemPageReqVO attSourceSystem) {
        PageResult<AttSourceSystemDO> page = attSourceSystemService.getAttSourceSystemPage(attSourceSystem);
        return CommonResult.success(BeanUtils.toBean(page, AttSourceSystemRespVO.class));
    }

    @Operation(summary = "查询来源系统列表")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:list')")
    @GetMapping("/listValid")
    public CommonResult<List<AttSourceSystemRespVO>> list() {
        List<AttSourceSystemDO> attSourceSystemList = attSourceSystemService.getAttSourceSystemListByValidFlag(true);
        return CommonResult.success(BeanUtils.toBean(attSourceSystemList, AttSourceSystemRespVO.class));
    }

    @Operation(summary = "导出来源系统列表")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:export')")
    @Log(title = "来源系统", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttSourceSystemPageReqVO exportReqVO) {
        exportReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AttSourceSystemDO> list = (List<AttSourceSystemDO>) attSourceSystemService.getAttSourceSystemPage(exportReqVO).getRows();
        ExcelUtil<AttSourceSystemRespVO> util = new ExcelUtil<>(AttSourceSystemRespVO.class);
        util.exportExcel(response, AttSourceSystemConvert.INSTANCE.convertToRespVOList(list), "应用管理数据");
    }

    @Operation(summary = "导入来源系统列表")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:import')")
    @Log(title = "来源系统", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<AttSourceSystemRespVO> util = new ExcelUtil<>(AttSourceSystemRespVO.class);
        List<AttSourceSystemRespVO> importExcelList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = attSourceSystemService.importAttSourceSystem(importExcelList, updateSupport, operName);
        return success(message);
    }

    @Operation(summary = "获取来源系统详细信息")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:query')")
    @GetMapping(value = "/{id}")
    public CommonResult<AttSourceSystemRespVO> getInfo(@PathVariable("id") Long id) {
        AttSourceSystemDO attSourceSystemDO = attSourceSystemService.getAttSourceSystemById(id);
        return CommonResult.success(BeanUtils.toBean(attSourceSystemDO, AttSourceSystemRespVO.class));
    }

    @Operation(summary = "新增来源系统")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:add')")
    @Log(title = "来源系统", businessType = BusinessType.INSERT)
    @PostMapping
    public CommonResult<Long> add(@Valid @RequestBody AttSourceSystemSaveReqVO attSourceSystem) {
        attSourceSystem.setCreatorId(getUserId());
        attSourceSystem.setCreateBy(getNickName());
        attSourceSystem.setCreateTime(DateUtil.date());
        return CommonResult.toAjax(attSourceSystemService.createAttSourceSystem(attSourceSystem));
    }

    @Operation(summary = "修改来源系统")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:edit')")
    @Log(title = "来源系统", businessType = BusinessType.UPDATE)
    @PutMapping
    public CommonResult<Integer> edit(@Valid @RequestBody AttSourceSystemSaveReqVO attSourceSystem) {
        attSourceSystem.setUpdatorId(getUserId());
        attSourceSystem.setUpdateBy(getNickName());
        attSourceSystem.setUpdateTime(DateUtil.date());
        return CommonResult.toAjax(attSourceSystemService.updateAttSourceSystem(attSourceSystem));
    }

    @Operation(summary = "删除来源系统")
    @PreAuthorize("@ss.hasPermi('att:sourcesystem:remove')")
    @Log(title = "来源系统", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public CommonResult<Integer> remove(@PathVariable Long[] ids) {
        return CommonResult.toAjax(attSourceSystemService.removeAttSourceSystem(Arrays.asList(ids)));
    }

}
