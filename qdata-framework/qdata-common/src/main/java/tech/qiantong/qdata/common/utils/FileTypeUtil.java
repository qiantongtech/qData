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
 */

package tech.qiantong.qdata.common.utils;

import java.util.HashMap;
import java.util.Map;

public class FileTypeUtil {
    private static final Map<String, String> FILE_TYPE_MAP = new HashMap<>();
    
    static {
        // 文本类
        FILE_TYPE_MAP.put("txt", "文本文件");
        FILE_TYPE_MAP.put("csv", "CSV文件");
        FILE_TYPE_MAP.put("log", "日志文件");
        
        // 代码类
        FILE_TYPE_MAP.put("java", "Java源文件");
        FILE_TYPE_MAP.put("class", "Java类文件");
        FILE_TYPE_MAP.put("jar", "JAR文件");
        FILE_TYPE_MAP.put("xml", "XML文件");
        FILE_TYPE_MAP.put("html", "HTML文件");
        FILE_TYPE_MAP.put("htm", "HTML文件");
        FILE_TYPE_MAP.put("js", "JavaScript文件");
        FILE_TYPE_MAP.put("css", "CSS文件");
        FILE_TYPE_MAP.put("json", "JSON文件");
        
        // 图片类
        FILE_TYPE_MAP.put("jpg", "JPEG图片");
        FILE_TYPE_MAP.put("jpeg", "JPEG图片");
        FILE_TYPE_MAP.put("png", "PNG图片");
        FILE_TYPE_MAP.put("gif", "GIF图片");
        FILE_TYPE_MAP.put("bmp", "位图文件");
        
        // 压缩类
        FILE_TYPE_MAP.put("zip", "ZIP压缩文件");
        FILE_TYPE_MAP.put("rar", "RAR压缩文件");
        FILE_TYPE_MAP.put("7z", "7-Zip压缩文件");
        FILE_TYPE_MAP.put("tar", "TAR归档文件");
        FILE_TYPE_MAP.put("gz", "GZIP压缩文件");
        
        // 办公文档类
        FILE_TYPE_MAP.put("doc", "Word文档");
        FILE_TYPE_MAP.put("docx", "Word文档");
        FILE_TYPE_MAP.put("xls", "Excel表格");
        FILE_TYPE_MAP.put("xlsx", "Excel表格");
        FILE_TYPE_MAP.put("ppt", "PowerPoint演示文稿");
        FILE_TYPE_MAP.put("pptx", "PowerPoint演示文稿");
        FILE_TYPE_MAP.put("pdf", "PDF文档");
    }
    
    /**
     * 根据文件名获取文件类型描述
     * @param fileName 文件名
     * @return 文件类型描述，如"文本文件(.txt)"，未知类型返回"文件"
     */
    public static String getFileType(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "文件";
        }
        
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return "文件";
        }
        
        String extension = fileName.substring(dotIndex + 1).toLowerCase();
        String type = FILE_TYPE_MAP.getOrDefault(extension, "文件");
        
        return type + "(" + "." + extension + ")";
    }

}
