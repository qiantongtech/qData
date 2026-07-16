/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.system.controller.admin.ca;

import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.TableDataInfo;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.ca.CaGenerateRootCertificate;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.config.ServerConfig;
import tech.qiantong.qdata.file.util.FileUploadUtil;
import tech.qiantong.qdata.module.system.ca.domain.CaSubject;
import tech.qiantong.qdata.module.system.ca.service.ICaSubjectService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Subject Management Controller
 *
 * @author qdata
 * @date 2024-08-18
 */
@RestController
@RequestMapping("/ca/subject")
public class CaSubjectController extends BaseController
{
    @Autowired
    private ICaSubjectService caSubjectService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ServerConfig serverConfig;

    @Value("${dromara.x-file-storage.local-plus[0].storage-path}")
    private String storagePath;

    @PostConstruct
    public void init() {
        FileUploadUtil.init(fileStorageService, serverConfig, storagePath);
    }
    /**
     * Query subject management list
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:list')")
    @GetMapping("/list")
    public TableDataInfo list(CaSubject caSubject)
    {
        startPage();
        List<CaSubject> list = caSubjectService.selectCaSubjectList(caSubject);
        return getDataTable(list);
    }

    /**
     * Export subject management list
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:export')")
    @Log(title = "log.op.title.system.subject", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CaSubject caSubject)
    {
        List<CaSubject> list = caSubjectService.selectCaSubjectList(caSubject);
        ExcelUtil<CaSubject> util = new ExcelUtil<CaSubject>(CaSubject.class);
        util.exportExcel(response, list, "Subject Management Data");
    }

    /**
     * Get subject management detail info
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(caSubjectService.selectCaSubjectById(id));
    }

    /**
     * Add subject management
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:add')")
    @Log(title = "log.op.title.system.subject", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CaSubject caSubject)
    {
        // Build certificate DN name
        String dnNameStr = StringUtils.format("CN={}, OU={}, O={}, L={}, ST={}, C={}",
                caSubject.getCn(), caSubject.getOu(),
                caSubject.getO(), caSubject.getL(),
                caSubject.getSt(), caSubject.getC());

        // Generate and get root certificate and private key file list
        List<MultipartFile> fileList = CaGenerateRootCertificate.generateRootCertificate(dnNameStr);

        // Upload and get certificate and private key file info
        FileInfo cert = FileUploadUtil.upload(fileList.get(0), "ca/");
        FileInfo privateKey = FileUploadUtil.upload(fileList.get(1), "ca/");

        // Update data info
        caSubject.setCertificate(Constants.RESOURCE_PREFIX + "/" + cert.getPath() + cert.getFilename());
        caSubject.setPrivateKey(Constants.RESOURCE_PREFIX + "/" + privateKey.getPath() + privateKey.getFilename());
        caSubject.setCreatorId(getUserId());
        caSubject.setCreateBy(getUsername());
        return toAjax(caSubjectService.insertCaSubject(caSubject));
    }

    /**
     * Update subject management
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:edit')")
    @Log(title = "log.op.title.system.subject", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CaSubject caSubject)
    {
        return toAjax(caSubjectService.updateCaSubject(caSubject));
    }

    /**
     * Delete subject management
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:remove')")
    @Log(title = "log.op.title.system.subject", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(caSubjectService.deleteCaSubjectByIds(ids));
    }
}
