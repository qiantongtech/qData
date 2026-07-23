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

package tech.qiantong.qdata.common.utils;

import java.util.HashMap;
import java.util.Map;

public class FileTypeUtil {
    private static final Map<String, String> FILE_TYPE_MAP = new HashMap<>();

    static {
        // Text class
        FILE_TYPE_MAP.put("txt", "Text file");
        FILE_TYPE_MAP.put("csv", "CSV file");
        FILE_TYPE_MAP.put("log", "Log file");

        // Code class
        FILE_TYPE_MAP.put("java", "Java source file");
        FILE_TYPE_MAP.put("class", "Java class file");
        FILE_TYPE_MAP.put("jar", "JAR file");
        FILE_TYPE_MAP.put("xml", "XML file");
        FILE_TYPE_MAP.put("html", "HTML file");
        FILE_TYPE_MAP.put("htm", "HTML file");
        FILE_TYPE_MAP.put("js", "JavaScript file");
        FILE_TYPE_MAP.put("css", "CSS file");
        FILE_TYPE_MAP.put("json", "JSON file");

        // Picture category
        FILE_TYPE_MAP.put("jpg", "JPEG image");
        FILE_TYPE_MAP.put("jpeg", "JPEG image");
        FILE_TYPE_MAP.put("png", "PNG image");
        FILE_TYPE_MAP.put("gif", "GIF image");
        FILE_TYPE_MAP.put("bmp", "Bitmap image");

        // Compression type
        FILE_TYPE_MAP.put("zip", "ZIP archive");
        FILE_TYPE_MAP.put("rar", "RAR archive");
        FILE_TYPE_MAP.put("7z", "7-Zip archive");
        FILE_TYPE_MAP.put("tar", "TAR archive");
        FILE_TYPE_MAP.put("gz", "GZIP archive");

        // Office documents
        FILE_TYPE_MAP.put("doc", "Word document");
        FILE_TYPE_MAP.put("docx", "Word document");
        FILE_TYPE_MAP.put("xls", "Excel spreadsheet");
        FILE_TYPE_MAP.put("xlsx", "Excel spreadsheet");
        FILE_TYPE_MAP.put("ppt", "PowerPoint presentation");
        FILE_TYPE_MAP.put("pptx", "PowerPoint presentation");
        FILE_TYPE_MAP.put("pdf", "PDF document");
    }

    /**
     * Get file type description based on file name
     * @param fileName file name
     * @return file type description, such as "text file (.txt)", unknown type returns "file"
     */
    public static String getFileType(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return MessageUtils.messageWithFallback("file.type.generic", "File");
        }

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            return MessageUtils.messageWithFallback("file.type.generic", "File");
        }

        String extension = fileName.substring(dotIndex + 1).toLowerCase();
        String defaultType = FILE_TYPE_MAP.getOrDefault(extension, "File");
        String type = MessageUtils.messageWithFallback("file.type." + extension, defaultType);

        return type + "(" + "." + extension + ")";
    }

}
