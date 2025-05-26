package tech.qiantong.qdata.module.system.controller.admin.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.config.AniviaConfig;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.utils.ExcelToCsvUtil;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.file.FileUploadUtils;
import tech.qiantong.qdata.common.utils.file.FileUtils;
import tech.qiantong.qdata.config.ServerConfig;
import tech.qiantong.qdata.module.system.domain.vo.ColumnRespVO;
import tech.qiantong.qdata.module.system.domain.vo.CsvColumnReqVO;
import tech.qiantong.qdata.module.system.domain.vo.ExcelColumnReqVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 通用请求处理
 *
 * @author qdata
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    @Value("${ds.resource_url}")
    private String resourceUrl;

    private static final String FILE_DELIMETER = ",";

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = AniviaConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = AniviaConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception {
        try {
            // 上传文件路径
            String filePath = AniviaConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files) {
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            if (!FileUtils.checkAllowDownload(resource)) {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = AniviaConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }


    /**
     * 获取excel列名并转换为csv
     *
     * @return
     */
    @PostMapping("/getExcelColumn")
    public AjaxResult getExcelColumn(@RequestBody ExcelColumnReqVO excelColumnReqVO) {
        String excelFile = excelColumnReqVO.getExcelFile();
        excelFile = AniviaConfig.getProfile() + excelFile.replace(Constants.RESOURCE_PREFIX + "/", "");
        excelFile = excelFile.replace("/", File.separator);
        Integer startColumn = excelColumnReqVO.getStartColumn();
        Integer startData = excelColumnReqVO.getStartData();
        String csvFile = resourceUrl + "csv" + File.separator + UUID.randomUUID().toString().replace("-", "") + ".csv";
        List<String> columnList = ExcelToCsvUtil.convertExcelToCsv(excelFile, csvFile, startColumn, startData);
        if (columnList.size() > 0) {
            if (!ExcelToCsvUtil.verifyColumn(columnList)) {
                return AjaxResult.error("附件中列名格式有误，请检查!");
            }
        }
        return AjaxResult.success(ColumnRespVO.builder()
                .csvFile(csvFile)
                .columnList(columnList).build());
    }

    /**
     * 获取excel列名并转换为csv
     *
     * @return
     */
    @PostMapping("/getCsvColumn")
    public AjaxResult getCsvColumn(@RequestBody CsvColumnReqVO csvColumnReqVO) {
        String file = csvColumnReqVO.getFile();
        file = AniviaConfig.getProfile() + file.replace(Constants.RESOURCE_PREFIX + "/", "");
        file = file.replace("/", File.separator);
        String csvFile = resourceUrl + "csv" + File.separator + UUID.randomUUID().toString().replace("-", "") + ".csv";
        List<String> columnList = ExcelToCsvUtil.parseCsv(file, csvFile);
        if (columnList.size() > 0) {
            if (!ExcelToCsvUtil.verifyColumn(columnList)) {
                return AjaxResult.error("附件中列名格式有误，请检查!");
            }
        }
        return AjaxResult.success(ColumnRespVO.builder()
                .csvFile(csvFile)
                .columnList(columnList).build());
    }


    public static void main(String[] args) {
        String inputPath = "C:\\Users\\Administrator\\Desktop\\数据中台\\ATT_WR_INFO_BASE.xlsx";
        String outputPath = "C:\\Users\\Administrator\\Desktop\\数据中台\\ATT_WR_INFO_BASE.csv";
        ExcelToCsvUtil.convertExcelToCsv(inputPath, outputPath, 1, 2);
        System.out.println("转换成功！");
    }
}
