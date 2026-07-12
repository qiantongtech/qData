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

package tech.qiantong.qdata.file.util;

import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.common.constant.Constants;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.config.ServerConfig;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * File upload utility class
 * Provides static methods for file upload, convenient for calling from other parts of the project.
 * This class operates on the FileStorageService instance using static methods, supporting multiple upload methods.
 *
 * @author qdata
 */
public class FileUploadUtil {

    /**
     * File storage service
     */
    private static FileStorageService fileStorageService;

    /**
     * File configuration
     */
    private static ServerConfig serverConfig;

    /**
     * File storage path
     */
    private static String storagePath;


    /**
     * Initialize the utility class
     * This method is used to initialize the FileStorageService instance.
     * Must be called before using other methods.
     *
     * @param service FileStorageService instance for file upload and storage operations
     */
    public static void init(FileStorageService service, ServerConfig config, String path) {
        fileStorageService = service;
        serverConfig = config;
        storagePath = path;
    }

    /**
     * Upload file
     * Upload MultipartFile file to the default storage platform.
     *
     * @param file file to upload
     * @param basePath no leading / and ending with /
     * @return uploaded file information (FileInfo object)
     */
    public static FileInfo upload(MultipartFile file, String basePath) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/");

        String path;
        if (StringUtils.isNotEmpty(basePath)) {
            path = basePath + formatter.format( new Date());
        } else {
            path = formatter.format( new Date());
        }
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath(path)
                .upload();
        String url = serverConfig.getUrl() + Constants.RESOURCE_PREFIX + fileInfo.getUrl();
        fileInfo.setUrl(url);

        return fileInfo;
    }

    /**
     * Upload file
     * Upload MultipartFile to the specified storage platform based on request parameters.
     *
     * @param file file to upload
     * @param basePath no leading / and ending with /
     * @param platform storage platform name
     * @return uploaded file information (FileInfo object)
     */
    public static FileInfo uploadByParam(MultipartFile file, String basePath, String platform) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/");

        // Build path
        String path;
        if (StringUtils.isNotEmpty(basePath)) {
            path = basePath + formatter.format(new Date());
        } else {
            path = formatter.format(new Date());
        }
        FileInfo fileInfo;
        // If a storage platform is specified, upload to the corresponding platform
        if (StringUtils.isNotEmpty(platform)) {
             fileInfo = fileStorageService.of(file)
                    .setPlatform(platform)
                    .setPath(path)
                    .upload();
        } else {
             fileInfo = fileStorageService.of(file)
                    .setPath(path)
                    .upload();
            String url = Constants.RESOURCE_PREFIX + fileInfo.getUrl();
            fileInfo.setUrl(url);
        }

        return fileInfo;
    }


    /**
     * Upload file and return file URL
     * This method uploads the file to the specified path and returns the uploaded file URL.
     *
     * @param file file to upload
     * @return uploaded file URL, or "Upload failed!" on failure
     */
/*    public static String upload2(MultipartFile file) {
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath("upload/")               // 设置文件保存的相对路径
                .setSaveFilename("image.jpg")     // 设置保存的文件名，如果不设置将随机生成
                .setObjectId("0")                 // 关联对象 ID，用于管理，不需要可以不写
                .setObjectType("0")               // 关联对象类型，用于管理，不需要可以不写
                .putAttr("role", "admin")         // 设置自定义属性，用于在其他地方获取使用
                .upload();
        return fileInfo == null ? "上传失败！" : fileInfo.getUrl();
    }*/

    /**
     * Upload image and generate thumbnail
     * This method uploads an image file and automatically generates a thumbnail.
     *
     * @param file image file to upload
     * @return uploaded file information (FileInfo object)
     */
    public static FileInfo uploadImage(MultipartFile file) {
        return fileStorageService.of(file)
                .image(img -> img.size(1000, 1000))  // Resize image to 1000*1000
                .thumbnail(th -> th.size(200, 200))  // Generate 200*200 thumbnail
                .upload();
    }

    /**
     * Upload file to specified storage platform
     * This method uploads the file to the specified platform, such as Alibaba Cloud OSS.
     *
     * @param file file to upload
     * @return uploaded file information (FileInfo object)
     */
    public static FileInfo uploadPlatform(MultipartFile file) {
        return fileStorageService.of(file)
                .setPlatform("aliyun-oss-1")    // Use specified storage platform
                .upload();
    }

    /**
     * Upload file via HttpServletRequest
     * Directly read and upload file from the HttpServletRequest object.
     *
     * @param request HttpServletRequest object containing the uploaded file data
     * @return uploaded file information (FileInfo object)
     */
    public static FileInfo uploadRequest(HttpServletRequest request) {
        return fileStorageService.of(request).upload();
    }
}
