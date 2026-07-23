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

package tech.qiantong.qdata.module.system.controller.admin.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
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
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.ExcelToCsvUtil;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.file.FileUploadUtils;
import tech.qiantong.qdata.common.utils.file.FileUtils;
import tech.qiantong.qdata.config.ServerConfig;
import tech.qiantong.qdata.module.system.domain.vo.CsvColumnReqVO;
import tech.qiantong.qdata.module.system.domain.vo.ExcelColumnReqVO;
import tech.qiantong.qdata.module.system.domain.vo.ColumnRespVO;

/**
 * Common Request Handler
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

    @Value("${ds.hdfs.url}")
    private String hdfsUrl;

    private static final String FILE_DELIMETER = ",";

    /**
     * Common download request
     *
     * @param fileName File name
     * @param delete   Whether to delete
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("File name ({}) is invalid, download not allowed. ", fileName));
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
            log.error("Failed to download file", e);
        }
    }

    /**
     * Common upload request (single)
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // Upload file path
            String filePath = AniviaConfig.getUploadPath();
            // Upload and return new file name
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
     * Common upload request (multiple)
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception {
        try {
            // Upload file path
            String filePath = AniviaConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files) {
                // Upload and return new file name
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
     * Local resource download
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            if (!FileUtils.checkAllowDownload(resource)) {
                throw new Exception(StringUtils.format("Resource file ({}) is invalid, download not allowed. ", resource));
            }
            // Local resource path
            String localPath = AniviaConfig.getProfile();
            // Database resource address
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // Download name
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            log.error("Failed to download file", e);
        }
    }


    /**
     * Get excel column names and convert to csv
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
        String fileName = UUID.randomUUID().toString().replace("-", "") + ".csv";
        String csvFile = resourceUrl + "csv" + File.separator + fileName;
        List<String> columnList = ExcelToCsvUtil.convertExcelToCsv(excelFile, csvFile, startColumn, startData);
        if (columnList.size() > 0) {
            if (!ExcelToCsvUtil.verifyColumn(columnList)) {
                return AjaxResult.error("Column name format in attachment is incorrect, please check!");
            }
        }
        String hdfsPath = "/tmp/etl";
        uploadHdfs(hdfsUrl, hdfsPath, csvFile, fileName);
        return AjaxResult.success(ColumnRespVO.builder()
                .csvFile(hdfsUrl + "/" + hdfsPath + "/" + fileName)
                .columnList(columnList).build());
    }

    /**
     * Get excel column names and convert to csv
     *
     * @return
     */
    @PostMapping("/getCsvColumn")
    public AjaxResult getCsvColumn(@RequestBody CsvColumnReqVO csvColumnReqVO) {
        String file = csvColumnReqVO.getFile();
        file = AniviaConfig.getProfile() + file.replace(Constants.RESOURCE_PREFIX + "/", "");
        file = file.replace("/", File.separator);
        String fileName = UUID.randomUUID().toString().replace("-", "") + ".csv";
        String csvFile = resourceUrl + "csv" + File.separator + fileName;
        List<String> columnList = ExcelToCsvUtil.parseCsv(file, csvFile);
        if (columnList.size() > 0) {
            if (!ExcelToCsvUtil.verifyColumn(columnList)) {
                return AjaxResult.error("Column name format in attachment is incorrect, please check!");
            }
        }
        String hdfsPath = "/tmp/etl";
        uploadHdfs(hdfsUrl, hdfsPath, csvFile, fileName);
        return AjaxResult.success(ColumnRespVO.builder()
                .csvFile(hdfsUrl + "/" + hdfsPath + "/" + fileName)
                .columnList(columnList).build());
    }

    /**
     * Upload file to hdfs
     *
     * @param hdfsUrl  hdfs address
     * @param pathStr  Upload path
     * @param file     File path
     * @param filename File name
     */
    public void uploadHdfs(String hdfsUrl, String pathStr, String file, String filename) {
        pathStr = pathStr == null ? "" : pathStr;
        pathStr = resolvePath(pathStr, filename);
        // 1. Create Hadoop configuration object
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUrl);
        conf.set("dfs.client.use.datanode.hostname", "true");
        // If only 1 DN, ensure replica count does not exceed node count
        conf.set("dfs.replication", "1");
        Path path = new Path(pathStr);
        try (FileSystem fs = FileSystem.get(new URI(hdfsUrl), conf, "hadoop");
             InputStream inputStream = new FileInputStream(file);
             FSDataOutputStream outputStream = fs.create(path)) {
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String resolvePath(String path, String filename) {
        String str = path + "/" + filename;
        return str.replaceAll("/+", "/");
    }
}
