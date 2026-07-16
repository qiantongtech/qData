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

package tech.qiantong.qdata.file.controller;

import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tech.qiantong.qdata.config.ServerConfig;
import tech.qiantong.qdata.file.util.FileUploadUtil;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * File upload controller
 * Provides a series of API interfaces for file uploading
 * The controller will call the static tool class FileUploadUtil to implement the file upload function
 * Use @RestController annotation to support RESTful API form
 *
 * @author qdata
 */
@RestController
public class FileUploadController {

    /**
     * File storage service
     * Use Spring's @Autowired annotation to automatically inject FileStorageService instances
     */
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ServerConfig serverConfig;
    @Value("${dromara.x-file-storage.local-plus[0].storage-path}")
    private String storagePath;

    /**
     * Initialization method
     * Use the @PostConstruct annotation to indicate that the method will be automatically called after dependency injection is completed.
     * Pass the injected FileStorageService instance to the static utility class FileUploadUtil
     */
    @PostConstruct
    public void init() {
        FileUploadUtil.init(fileStorageService, serverConfig, storagePath);
    }

    /**
     * Upload file interface - available
     * Handle file upload requests and upload files to the default storage platform
     *
     * @param file The file to be uploaded, use MultipartFile to receive the uploaded file
     * @return Returns file information (FileInfo object) after successful upload
     */
    @PostMapping("/upload")
    public FileInfo upload(MultipartFile file, String platForm) {
        return FileUploadUtil.uploadByParam(file, null,platForm);
    }

    /**
     * Upload image interface - temporarily unavailable
     * Process image file upload requests and generate thumbnails
     *
     * @param file The image file to be uploaded, use MultipartFile to receive the uploaded image file
     * @return Returns image file information (FileInfo object) after successful upload
     */
    @PostMapping("/upload-image")
    public FileInfo uploadImage(MultipartFile file) {
        return FileUploadUtil.uploadImage(file);
    }

    /**
     * Interface for uploading files to the specified storage platform - not available yet
     * Process file upload requests and upload files to the designated storage platform
     *
     * @param file The file to be uploaded, use MultipartFile to receive the uploaded file
     * @return Returns file information (FileInfo object) after successful upload
     */
    @PostMapping("/upload-platform")
    public FileInfo uploadPlatform(MultipartFile file) {
        return FileUploadUtil.uploadPlatform(file);
    }

    /**
     * Interface for directly uploading files through HttpServletRequest - not available yet
     * Process file upload requests, read files directly from HttpServletRequest and upload them
     *
     * @param request HttpServletRequest object, containing uploaded file data
     * @return Returns file information (FileInfo object) after successful upload
     */
    @PostMapping("/upload-request")
    public FileInfo uploadPlatform(HttpServletRequest request) {
        return FileUploadUtil.uploadRequest(request);
    }
}
