package tech.qiantong.qdata.module.system.controller.admin.ca;

import cn.hutool.core.convert.Convert;
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
import tech.qiantong.qdata.common.utils.ca.CaCertificateIssuer;
import tech.qiantong.qdata.common.utils.poi.ExcelUtil;
import tech.qiantong.qdata.config.ServerConfig;
import tech.qiantong.qdata.file.util.FileUploadUtil;
import tech.qiantong.qdata.module.system.ca.domain.CaCert;
import tech.qiantong.qdata.module.system.ca.domain.CaSubject;
import tech.qiantong.qdata.module.system.ca.service.ICaCertService;
import tech.qiantong.qdata.module.system.ca.service.ICaSubjectService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 证书管理Controller
 *
 * @author qdata
 * @date 2024-08-18
 */
@RestController
@RequestMapping("/ca/cert")
public class CaCertController extends BaseController
{
    @Autowired
    private ICaCertService caCertService;

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
     * 查询证书管理列表
     */
    @PreAuthorize("@ss.hasPermi('ca:cert:list')")
    @GetMapping("/list")
    public TableDataInfo list(CaCert caCert)
    {
        startPage();
        List<CaCert> list = caCertService.selectCaCertList(caCert);
        return getDataTable(list);
    }

    /**
     * 导出证书管理列表
     */
    @PreAuthorize("@ss.hasPermi('ca:cert:export')")
    @Log(title = "证书管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CaCert caCert)
    {
        List<CaCert> list = caCertService.selectCaCertList(caCert);
        ExcelUtil<CaCert> util = new ExcelUtil<CaCert>(CaCert.class);
        util.exportExcel(response, list, "证书管理数据");
    }

    /**
     * 获取证书管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('ca:cert:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(caCertService.selectCaCertById(id));
    }

    /**
     * 新增证书管理
     */
    @PreAuthorize("@ss.hasPermi('ca:cert:add')")
    @Log(title = "证书管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CaCert caCert) throws Exception {
        CaSubject subject = caSubjectService.selectCaSubjectById(caCert.getSubjectId());
        // 构建证书的 DN 名称
        String dnNameStr = StringUtils.format("CN={}, OU={}, O={}, L={}, ST={}, C={}",
                caCert.getPossessor(), subject.getOu(),
                subject.getO(), subject.getL(),
                subject.getSt(), subject.getC());

        // 创建证书
        List<MultipartFile> fileList = CaCertificateIssuer.issueCertificate(
                dnNameStr,
                subject.getCertificate(),
                subject.getPrivateKey(),
                Convert.toLong(caCert.getValidTime()));

        // 上传并获取证书和私钥的文件信息
        FileInfo cert = FileUploadUtil.upload(fileList.get(0), "ca/");
        FileInfo privateKey = FileUploadUtil.upload(fileList.get(1), "ca/");

        // 更新数据信息
        caCert.setCertificate(Constants.RESOURCE_PREFIX + "/" + cert.getPath() + cert.getFilename());
        caCert.setPrivateKey(Constants.RESOURCE_PREFIX + "/" + privateKey.getPath() + privateKey.getFilename());
        caCert.setCreatorId(getUserId());
        caCert.setCreateBy(getUsername());
        return toAjax(caCertService.insertCaCert(caCert));
    }

    /**
     * 修改证书管理
     */
    @PreAuthorize("@ss.hasPermi('ca:cert:edit')")
    @Log(title = "证书管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CaCert caCert)
    {
        return toAjax(caCertService.updateCaCert(caCert));
    }

    /**
     * 删除证书管理
     */
    @PreAuthorize("@ss.hasPermi('ca:cert:remove')")
    @Log(title = "证书管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(caCertService.deleteCaCertByIds(ids));
    }
}
