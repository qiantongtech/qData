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

package tech.qiantong.qdata.common.utils.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 文件数据读取工具类
 * 支持读取Excel(xlsx, xls)和CSV文件数据
 * 返回格式与数据库查询结果一致
 *
 * @author system
 * @date 2025-01-21
 */
@Slf4j
public class FileDataReaderUtil {

    /**
     * 支持的文件扩展名
     */
    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(".xlsx", ".xls", ".csv");

    /**
     * 读取文件数据并返回与getColumnData相同格式的数据
     *
     * @param filePath    文件路径
     * @param pageNum     页码（从1开始）
     * @param pageSize    每页条数
     * @param startRow    起始行（Excel文件专用，从1开始）
     * @param startColumn 起始列（Excel文件专用，从1开始）
     * @param filter      过滤条件（可选）
     * @return 返回格式与getColumnData相同的数据结构
     *         {
     *         "columns": [{"field": "列名", "en": "英文名", "cn": "中文名",
     *         "columnNullable": true, "columnKey": false}],
     *         "tableData": [{"列名1": "值1", "列名2": "值2"}],
     *         "total": 总记录数
     *         }
     */
    public static Map<String, Object> readFileData(String filePath, Long pageNum, Long pageSize,
            Integer startRow, Integer startColumn, String filter) {
        // 参数校验
        validateParams(filePath, pageNum, pageSize);

        // 检查文件是否存在
        if (!FileUtil.exist(filePath)) {
            throw new RuntimeException("文件不存在: " + filePath);
        }

        // 获取文件扩展名
        String extension = getFileExtension(filePath);
        if (!SUPPORTED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new RuntimeException("不支持的文件格式: " + extension + "，支持格式: " + SUPPORTED_EXTENSIONS);
        }

        try {
            // 根据文件类型调用不同的读取方法
            if (extension.equalsIgnoreCase(".csv")) {
                return readCsvFile(filePath, pageNum, pageSize, filter);
            } else {
                return readExcelFile(filePath, pageNum, pageSize, startRow, startColumn, filter);
            }
        } catch (Exception e) {
            log.error("读取文件数据失败: {}", filePath, e);
            throw new RuntimeException("读取文件数据失败: " + e.getMessage());
        }
    }

    /**
     * 读取文件数据（简化版本，使用默认参数）
     */
    public static Map<String, Object> readFileData(String filePath, Long pageNum, Long pageSize) {
        return readFileData(filePath, pageNum, pageSize, 1, 1, null);
    }

    /**
     * 读取文件数据（使用JSONObject参数）
     */
    public static Map<String, Object> readFileData(JSONObject jsonObject) {
        if (jsonObject == null) {
            throw new RuntimeException("参数不能为空");
        }

        String filePath = jsonObject.getStr("filePath");
        Long pageNum = jsonObject.getLong("pageNum");
        Long pageSize = jsonObject.getLong("pageSize");
        Integer startRow = jsonObject.getInt("startRow", 1);
        Integer startColumn = jsonObject.getInt("startColumn", 1);
        String filter = jsonObject.getStr("filter");

        return readFileData(filePath, pageNum, pageSize, startRow, startColumn, filter);
    }

    /**
     * 获取文件总行数
     */
    public static Long getFileTotalRows(String filePath) {
        if (!FileUtil.exist(filePath)) {
            throw new RuntimeException("文件不存在: " + filePath);
        }

        String extension = getFileExtension(filePath);
        if (extension.equalsIgnoreCase(".csv")) {
            return getCsvTotalRows(filePath);
        } else {
            return getExcelTotalRows(filePath);
        }
    }

    /**
     * 获取文件列信息
     */
    public static List<Map<String, Object>> getFileColumns(String filePath, Integer startRow, Integer startColumn) {
        if (!FileUtil.exist(filePath)) {
            throw new RuntimeException("文件不存在: " + filePath);
        }

        String extension = getFileExtension(filePath);
        if (extension.equalsIgnoreCase(".csv")) {
            return getCsvColumns(filePath);
        } else {
            return getExcelColumns(filePath, startRow, startColumn);
        }
    }

    /**
     * 获取文件列信息（简化版本）
     */
    public static List<Map<String, Object>> getFileColumns(String filePath) {
        return getFileColumns(filePath, 1, 1);
    }

    /**
     * 读取CSV文件数据
     */
    private static Map<String, Object> readCsvFile(String filePath, Long pageNum, Long pageSize, String filter) {
        List<String> lines = FileUtil.readLines(filePath, StandardCharsets.UTF_8);
        if (CollectionUtils.isEmpty(lines)) {
            return createEmptyResult();
        }

        // 获取列信息（第一行作为列名）
        List<Map<String, Object>> columns = getCsvColumns(filePath);

        // 读取数据行
        List<Map<String, Object>> allData = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (StrUtil.isBlank(line)) {
                continue;
            }

            Map<String, Object> rowData = parseCsvLine(line, columns);
            if (rowData != null) {
                allData.add(rowData);
            }
        }

        // 应用过滤条件
        if (StrUtil.isNotBlank(filter)) {
            allData = applyFilter(allData, columns, filter);
        }

        // 分页处理
        return applyPagination(allData, columns, pageNum, pageSize);
    }

    /**
     * 读取Excel文件数据
     */
    private static Map<String, Object> readExcelFile(String filePath, Long pageNum, Long pageSize,
            Integer startRow, Integer startColumn, String filter) {
        final List<Map<String, Object>> allData = new ArrayList<>();
        final List<Map<String, Object>> columns = new ArrayList<>();
        int headerRowIndex = startRow - 1; // 用户传1，实际是第0行
        try {
            EasyExcel.read(filePath, new ReadListener<Map<Integer, String>>() {
                private int currentRow = 0;

                @Override
                public void invoke(Map<Integer, String> rowData, AnalysisContext context) {
                    if (currentRow == headerRowIndex) {
                        columns.addAll(processExcelHeader(rowData, startColumn));
                    } else if (currentRow > headerRowIndex) {
                        Map<String, Object> processedRow = processExcelRow(rowData, columns, startColumn);
                        if (processedRow != null) {
                            allData.add(processedRow);
                        }
                    }
                    currentRow++;
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet().headRowNumber(0).doRead();
        } catch (RuntimeException e) {
            if (!"STOP_READING".equals(e.getMessage())) {
                throw e;
            }
        }

        if (StrUtil.isNotBlank(filter)) {
            return applyPagination(applyFilter(allData, columns, filter), columns, pageNum, pageSize);
        }
        return applyPagination(allData, columns, pageNum, pageSize);
    }

    /**
     * 处理Excel表头
     */
    private static List<Map<String, Object>> processExcelHeader(Map<Integer, String> rowData, Integer startColumn) {
        List<Map<String, Object>> columns = new ArrayList<>();
        int columnIndex = 0;

        for (int i = startColumn - 1; i < rowData.size(); i++) {
            String columnName = rowData.get(i);
            if (StrUtil.isBlank(columnName)) {
                columnName = "Column_" + (columnIndex + 1);
            }

            Map<String, Object> column = new HashMap<>();
            column.put("field", columnName);
            column.put("en", columnName);
            column.put("cn", null);
            column.put("columnNullable", true);
            column.put("columnKey", false);
            columns.add(column);
            columnIndex++;
        }

        return columns;
    }

    /**
     * 处理Excel数据行
     */
    private static Map<String, Object> processExcelRow(Map<Integer, String> rowData, List<Map<String, Object>> columns,
            Integer startColumn) {
        Map<String, Object> processedRow = new HashMap<>();
        int columnIndex = 0;

        for (int i = startColumn - 1; i < rowData.size() && columnIndex < columns.size(); i++) {
            String columnName = (String) columns.get(columnIndex).get("field");
            String cellValue = rowData.get(i);
            processedRow.put(columnName, cellValue);
            columnIndex++;
        }

        return processedRow;
    }

    /**
     * 解析CSV行数据
     */
    private static Map<String, Object> parseCsvLine(String line, List<Map<String, Object>> columns) {
        String[] values = parseCsvValues(line);
        Map<String, Object> rowData = new HashMap<>();

        for (int i = 0; i < Math.min(values.length, columns.size()); i++) {
            String columnName = (String) columns.get(i).get("field");
            rowData.put(columnName, values[i]);
        }

        return rowData;
    }

    /**
     * 解析CSV值（处理引号内的逗号）
     */
    private static String[] parseCsvValues(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString().trim());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }

        values.add(currentValue.toString().trim());
        return values.toArray(new String[0]);
    }

    /**
     * 获取CSV列信息
     */
    private static List<Map<String, Object>> getCsvColumns(String filePath) {
        List<String> lines = FileUtil.readLines(filePath, StandardCharsets.UTF_8);
        if (CollectionUtils.isEmpty(lines)) {
            return new ArrayList<>();
        }

        String headerLine = lines.get(0);
        String[] columnNames = parseCsvValues(headerLine);

        List<Map<String, Object>> columns = new ArrayList<>();
        for (String columnName : columnNames) {
            if (StrUtil.isBlank(columnName)) {
                columnName = "Column_" + (columns.size() + 1);
            }

            Map<String, Object> column = new HashMap<>();
            column.put("field", columnName);
            column.put("en", columnName);
            column.put("cn", null);
            column.put("columnNullable", true);
            column.put("columnKey", false);
            columns.add(column);
        }

        return columns;
    }

    /**
     * 获取Excel列信息
     */
    private static List<Map<String, Object>> getExcelColumns(String filePath, Integer startRow, Integer startColumn) {
        List<Map<String, Object>> columns = new ArrayList<>();
        int headerRowIndex = startRow - 1; // 用户传1，实际是第0行
        try {
            EasyExcel.read(filePath, new ReadListener<Map<Integer, String>>() {
                private int currentRow = 0;

                @Override
                public void invoke(Map<Integer, String> rowData, AnalysisContext context) {
                    // 打印调试
                    // System.out.println("currentRow=" + currentRow + ", rowData=" + rowData);
                    if (currentRow == headerRowIndex) {
                        columns.addAll(processExcelHeader(rowData, startColumn));
                        throw new RuntimeException("STOP_READING");
                    }
                    currentRow++;
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet().doRead();
        } catch (RuntimeException e) {
            if (!"STOP_READING".equals(e.getMessage())) {
                throw e;
            }
        }
        return columns;
    }

    /**
     * 获取CSV文件总行数
     */
    private static Long getCsvTotalRows(String filePath) {
        List<String> lines = FileUtil.readLines(filePath, StandardCharsets.UTF_8);
        return lines.size() > 1 ? (long) (lines.size() - 1) : 0L;
    }

    /**
     * 获取Excel文件总行数
     */
    private static Long getExcelTotalRows(String filePath) {
        AtomicLong rowCount = new AtomicLong(0);
        try {
            EasyExcel.read(filePath, new ReadListener<Map<Integer, String>>() {
                @Override
                public void invoke(Map<Integer, String> rowData, AnalysisContext context) {
                    rowCount.incrementAndGet();
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    // 读取完成后的处理
                }
            }).sheet().doRead();
        } catch (RuntimeException e) {
            if (!"STOP_READING".equals(e.getMessage())) {
                throw e;
            }
        }
        return rowCount.get();
    }

    /**
     * 应用过滤条件
     * 这里可以实现简单的过滤逻辑
     * 目前返回所有数据，可以根据需要扩展过滤功能
     */
    private static List<Map<String, Object>> applyFilter(List<Map<String, Object>> data,
            List<Map<String, Object>> columns, String filter) {
        // TODO: 可根据filter参数实现简单的过滤逻辑
        return data;
    }

    /**
     * 应用分页
     */
    private static Map<String, Object> applyPagination(List<Map<String, Object>> allData,
            List<Map<String, Object>> columns,
            Long pageNum, Long pageSize) {
        long total = allData.size();
        long startIndex = (pageNum - 1) * pageSize;
        long endIndex = Math.min(startIndex + pageSize, total);

        List<Map<String, Object>> pageData = new ArrayList<>();
        if (startIndex < total) {
            pageData = allData.subList((int) startIndex, (int) endIndex);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("columns", columns);
        result.put("tableData", pageData);
        result.put("total", total);

        return result;
    }

    /**
     * 创建空结果
     */
    private static Map<String, Object> createEmptyResult() {
        Map<String, Object> result = new HashMap<>();
        result.put("columns", new ArrayList<>());
        result.put("tableData", new ArrayList<>());
        result.put("total", 0L);
        return result;
    }

    /**
     * 获取文件扩展名
     */
    private static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        return lastDotIndex > 0 ? filePath.substring(lastDotIndex) : "";
    }

    /**
     * 参数校验
     */
    private static void validateParams(String filePath, Long pageNum, Long pageSize) {
        if (StrUtil.isBlank(filePath)) {
            throw new RuntimeException("文件路径不能为空");
        }

        if (pageNum == null || pageNum < 1) {
            throw new RuntimeException("页码不能为空且必须大于0");
        }

        if (pageSize == null || pageSize < 1) {
            throw new RuntimeException("每页条数不能为空且必须大于0");
        }
    }

    /**
     * 检查文件是否支持
     */
    public static boolean isSupportedFile(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            return false;
        }
        String extension = getFileExtension(filePath);
        return SUPPORTED_EXTENSIONS.contains(extension.toLowerCase());
    }

    /**
     * 获取支持的文件扩展名列表
     */
    public static List<String> getSupportedExtensions() {
        return new ArrayList<>(SUPPORTED_EXTENSIONS);
    }

    /**
     * 批量读取文件数据
     */
    public static Map<String, Map<String, Object>> batchReadFileData(List<String> filePaths,
            Long pageNum, Long pageSize,
            Integer startRow, Integer startColumn) {
        Map<String, Map<String, Object>> results = new HashMap<>();

        for (String filePath : filePaths) {
            try {
                Map<String, Object> data = readFileData(filePath, pageNum, pageSize, startRow, startColumn, null);
                results.put(filePath, data);
            } catch (Exception e) {
                log.error("读取文件失败: {}", filePath, e);
                results.put(filePath, createEmptyResult());
            }
        }

        return results;
    }

    /**
     * 获取文件基本信息
     */
    public static Map<String, Object> getFileInfo(String filePath) {
        if (!FileUtil.exist(filePath)) {
            throw new RuntimeException("文件不存在: " + filePath);
        }

        File file = new File(filePath);
        Map<String, Object> info = new HashMap<>();
        info.put("fileName", file.getName());
        info.put("filePath", filePath);
        info.put("fileSize", file.length());
        info.put("lastModified", file.lastModified());
        info.put("extension", getFileExtension(filePath));
        info.put("isSupported", isSupportedFile(filePath));
        info.put("totalRows", getFileTotalRows(filePath));
        info.put("columns", getFileColumns(filePath));

        return info;
    }
}
