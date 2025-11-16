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
 * For brand customization, please apply for brand customization authorization via official channels.
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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
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
 * 主体管理Controller
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
     * 查询主体管理列表
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
     * 导出主体管理列表
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:export')")
    @Log(title = "主体管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CaSubject caSubject)
    {
        List<CaSubject> list = caSubjectService.selectCaSubjectList(caSubject);
        ExcelUtil<CaSubject> util = new ExcelUtil<CaSubject>(CaSubject.class);
        util.exportExcel(response, list, "主体管理数据");
    }

    /**
     * 获取主体管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(caSubjectService.selectCaSubjectById(id));
    }

    /**
     * 新增主体管理
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:add')")
    @Log(title = "主体管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CaSubject caSubject)
    {
        // 构建证书的 DN 名称
        String dnNameStr = StringUtils.format("CN={}, OU={}, O={}, L={}, ST={}, C={}",
                caSubject.getCn(), caSubject.getOu(),
                caSubject.getO(), caSubject.getL(),
                caSubject.getSt(), caSubject.getC());

        // 生成并获取根证书和私钥的文件列表
        List<MultipartFile> fileList = CaGenerateRootCertificate.generateRootCertificate(dnNameStr);

        // 上传并获取证书和私钥的文件信息
        FileInfo cert = FileUploadUtil.upload(fileList.get(0), "ca/");
        FileInfo privateKey = FileUploadUtil.upload(fileList.get(1), "ca/");

        // 更新数据信息
        caSubject.setCertificate(Constants.RESOURCE_PREFIX + "/" + cert.getPath() + cert.getFilename());
        caSubject.setPrivateKey(Constants.RESOURCE_PREFIX + "/" + privateKey.getPath() + privateKey.getFilename());
        caSubject.setCreatorId(getUserId());
        caSubject.setCreateBy(getUsername());
        return toAjax(caSubjectService.insertCaSubject(caSubject));
    }

    /**
     * 修改主体管理
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:edit')")
    @Log(title = "主体管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CaSubject caSubject)
    {
        return toAjax(caSubjectService.updateCaSubject(caSubject));
    }

    /**
     * 删除主体管理
     */
    @PreAuthorize("@ss.hasPermi('ca:subject:remove')")
    @Log(title = "主体管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(caSubjectService.deleteCaSubjectByIds(ids));
    }
}
