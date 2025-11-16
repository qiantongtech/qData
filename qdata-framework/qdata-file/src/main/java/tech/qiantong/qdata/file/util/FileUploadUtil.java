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
 * 文件上传工具类
 * 提供文件上传的静态方法，便于在项目的其他部分调用。
 * 该类使用静态方法操作 FileStorageService 实例，支持多种上传方式。
 *
 * @author qdata
 */
public class FileUploadUtil {

    /**
     * 文件存储服务
     */
    private static FileStorageService fileStorageService;

    /**
     * 文件配置
     */
    private static ServerConfig serverConfig;

    /**
     * 文件存储地址
     */
    private static String storagePath;


    /**
     * 初始化工具类
     * 该方法用于初始化 FileStorageService 实例。必须在使用其他方法之前调用该方法。
     *
     * @param service FileStorageService 实例，用于文件的上传和存储操作
     */
    public static void init(FileStorageService service, ServerConfig config, String path) {
        fileStorageService = service;
        serverConfig = config;
        storagePath = path;
    }

    /**
     * 上传文件
     * 将 MultipartFile 文件上传到默认的存储平台。
     *
     * @param file 要上传的文件
     * @param basePath 最前面没有 / 结尾带有 /
     * @return 返回上传后的文件信息（FileInfo 对象）
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
     * 上传文件
     * 将 MultipartFile 根据请求参数上传到入参指定的存储平台。
     *
     * @param file 要上传的文件
     * @param basePath 最前面没有 / 结尾带有 /
     * @param platform 存储平台名称
     * @return 返回上传后的文件信息（FileInfo 对象）
     */
    public static FileInfo uploadByParam(MultipartFile file, String basePath, String platform) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/");

        // 构造路径
        String path;
        if (StringUtils.isNotEmpty(basePath)) {
            path = basePath + formatter.format(new Date());
        } else {
            path = formatter.format(new Date());
        }
        FileInfo fileInfo;
        // 如果指定了存储平台，上传到对应平台
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
     * 上传文件并返回文件 URL
     * 该方法将文件上传到指定路径，并返回上传后的文件 URL。
     *
     * @param file 要上传的文件
     * @return 上传成功后返回文件的 URL，失败则返回 "上传失败！"
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
     * 上传图片并生成缩略图
     * 该方法将图片文件上传并自动生成缩略图。
     *
     * @param file 要上传的图片文件
     * @return 返回上传后的文件信息（FileInfo 对象）
     */
    public static FileInfo uploadImage(MultipartFile file) {
        return fileStorageService.of(file)
                .image(img -> img.size(1000, 1000))  // 调整图片大小到 1000*1000
                .thumbnail(th -> th.size(200, 200))  // 生成 200*200 的缩略图
                .upload();
    }

    /**
     * 上传文件到指定存储平台
     * 该方法将文件上传到指定的平台，如阿里云 OSS。
     *
     * @param file 要上传的文件
     * @return 返回上传后的文件信息（FileInfo 对象）
     */
    public static FileInfo uploadPlatform(MultipartFile file) {
        return fileStorageService.of(file)
                .setPlatform("aliyun-oss-1")    // 使用指定的存储平台
                .upload();
    }

    /**
     * 通过 HttpServletRequest 上传文件
     * 直接从 HttpServletRequest 对象中读取文件并上传。
     *
     * @param request HttpServletRequest 对象，包含上传的文件数据
     * @return 返回上传后的文件信息（FileInfo 对象）
     */
    public static FileInfo uploadRequest(HttpServletRequest request) {
        return fileStorageService.of(request).upload();
    }
}
