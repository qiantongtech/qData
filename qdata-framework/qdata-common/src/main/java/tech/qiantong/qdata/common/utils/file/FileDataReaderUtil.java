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

package tech.qiantong.qdata.common.utils.file;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;

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
 * File data reading tool class
 * Supports reading Excel (xlsx, xls) and CSV file data
 * The return format is consistent with the database query results
 *
 * @author system
 * @date 2025-01-21
 */
@Slf4j
public class FileDataReaderUtil {

    /**
     * Supported file extensions
     */
    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList(".xlsx", ".xls", ".csv");

    /**
     * Read file data and return data in the same format as getColumnData
     *
     * @param filePath file path
     * @param pageNum page number (starting from 1)
     * @param pageSize Number of items per page
     * @param startRow starting row (exclusive for Excel files, starting from 1)
     * @param startColumn starting column (exclusive for Excel files, starting from 1)
     * @param filter filter conditions (optional)
     * @return Returns a data structure with the same format as getColumnData
     *         {
     * "columns": [{"field": "column name", "en": "English name", "cn": "Chinese name",
     *         "columnNullable": true, "columnKey": false}],
     * "tableData": [{"Column name 1": "Value 1", "Column name 2": "Value 2"}],
     * "total": total number of records
     *         }
     */
    public static Map<String, Object> readFileData(String filePath, Long pageNum, Long pageSize,
            Integer startRow, Integer startColumn, String filter) {
        // Parameter verification
        validateParams(filePath, pageNum, pageSize);

        // Check if the file exists
        if (!FileUtil.exist(filePath)) {
            throw new RuntimeException(MessageUtils.messageWithFallback(
                    "sys.error.file.notfound.path", "File does not exist: {0}", filePath));
        }

        // Get file extension
        String extension = getFileExtension(filePath);
        if (!SUPPORTED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new RuntimeException(MessageUtils.messageWithFallback(
                    "sys.error.file.format.unsupported", "Unsupported file format: {0}; supported formats: {1}",
                    extension, SUPPORTED_EXTENSIONS));
        }

        try {
            // Call different read methods based on file type
            if (extension.equalsIgnoreCase(".csv")) {
                return readCsvFile(filePath, pageNum, pageSize, filter);
            } else {
                return readExcelFile(filePath, pageNum, pageSize, startRow, startColumn, filter);
            }
        } catch (Exception e) {
            log.error("Failed to read file data: {}", filePath, e);
            throw new RuntimeException(MessageUtils.messageWithFallback(
                    "sys.error.file.read.fail", "Failed to read file data: {0}", e.getMessage()));
        }
    }

    /**
     * Read file data (simplified version, using default parameters)
     */
    public static Map<String, Object> readFileData(String filePath, Long pageNum, Long pageSize) {
        return readFileData(filePath, pageNum, pageSize, 1, 1, null);
    }

    /**
     * Read file data (using JSONObject parameters)
     */
    public static Map<String, Object> readFileData(JSONObject jsonObject) {
        if (jsonObject == null) {
            throw new ServiceException("sys.error.param.empty", "Parameter cannot be empty");
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
     * Get the total number of lines in the file
     */
    public static Long getFileTotalRows(String filePath) {
        if (!FileUtil.exist(filePath)) {
            throw new RuntimeException(MessageUtils.messageWithFallback(
                    "sys.error.file.notfound.path", "File does not exist: {0}", filePath));
        }

        String extension = getFileExtension(filePath);
        if (extension.equalsIgnoreCase(".csv")) {
            return getCsvTotalRows(filePath);
        } else {
            return getExcelTotalRows(filePath);
        }
    }

    /**
     * Get file column information
     */
    public static List<Map<String, Object>> getFileColumns(String filePath, Integer startRow, Integer startColumn) {
        if (!FileUtil.exist(filePath)) {
            throw new RuntimeException(MessageUtils.messageWithFallback(
                    "sys.error.file.notfound.path", "File does not exist: {0}", filePath));
        }

        String extension = getFileExtension(filePath);
        if (extension.equalsIgnoreCase(".csv")) {
            return getCsvColumns(filePath);
        } else {
            return getExcelColumns(filePath, startRow, startColumn);
        }
    }

    /**
     * Get file column information (simplified version)
     */
    public static List<Map<String, Object>> getFileColumns(String filePath) {
        return getFileColumns(filePath, 1, 1);
    }

    /**
     * Read CSV file data
     */
    private static Map<String, Object> readCsvFile(String filePath, Long pageNum, Long pageSize, String filter) {
        List<String> lines = FileUtil.readLines(filePath, StandardCharsets.UTF_8);
        if (CollectionUtils.isEmpty(lines)) {
            return createEmptyResult();
        }

        // Get column information (first row as column name)
        List<Map<String, Object>> columns = getCsvColumns(filePath);

        // Read data row
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

        // Apply filters
        if (StrUtil.isNotBlank(filter)) {
            allData = applyFilter(allData, columns, filter);
        }

        // Pagination
        return applyPagination(allData, columns, pageNum, pageSize);
    }

    /**
     * Read Excel file data
     */
    private static Map<String, Object> readExcelFile(String filePath, Long pageNum, Long pageSize,
            Integer startRow, Integer startColumn, String filter) {
        final List<Map<String, Object>> allData = new ArrayList<>();
        final List<Map<String, Object>> columns = new ArrayList<>();
        int headerRowIndex = startRow - 1; // The user passes 1, which is actually line 0
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
     * Processing Excel headers
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
     * Process Excel data rows
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
     * Parse CSV row data
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
     * Parsing CSV values (handling commas within quotes)
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
     * Get CSV column information
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
     * Get Excel column information
     */
    private static List<Map<String, Object>> getExcelColumns(String filePath, Integer startRow, Integer startColumn) {
        List<Map<String, Object>> columns = new ArrayList<>();
        int headerRowIndex = startRow - 1; // The user passes 1, which is actually line 0
        try {
            EasyExcel.read(filePath, new ReadListener<Map<Integer, String>>() {
                private int currentRow = 0;

                @Override
                public void invoke(Map<Integer, String> rowData, AnalysisContext context) {
                    // Print debugging
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
     * Get the total number of rows in a CSV file
     */
    private static Long getCsvTotalRows(String filePath) {
        List<String> lines = FileUtil.readLines(filePath, StandardCharsets.UTF_8);
        return lines.size() > 1 ? (long) (lines.size() - 1) : 0L;
    }

    /**
     * Get the total number of rows in an Excel file
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
                    // Processing after reading is completed
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
     * Apply filters
     * Simple filtering logic can be implemented here
     * Currently returns all data, filtering functionality can be expanded as needed
     */
    private static List<Map<String, Object>> applyFilter(List<Map<String, Object>> data,
            List<Map<String, Object>> columns, String filter) {
        // TODO: Simple filtering logic can be implemented based on filter parameters
        return data;
    }

    /**
     * Apply paging
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
     * Create empty results
     */
    private static Map<String, Object> createEmptyResult() {
        Map<String, Object> result = new HashMap<>();
        result.put("columns", new ArrayList<>());
        result.put("tableData", new ArrayList<>());
        result.put("total", 0L);
        return result;
    }

    /**
     * Get file extension
     */
    private static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        return lastDotIndex > 0 ? filePath.substring(lastDotIndex) : "";
    }

    /**
     * Parameter verification
     */
    private static void validateParams(String filePath, Long pageNum, Long pageSize) {
        if (StrUtil.isBlank(filePath)) {
            throw new ServiceException("sys.error.file.path.empty", "File path cannot be empty");
        }

        if (pageNum == null || pageNum < 1) {
            throw new ServiceException("sys.error.page.num.invalid", "Page number cannot be empty and must be greater than 0");
        }

        if (pageSize == null || pageSize < 1) {
            throw new ServiceException("sys.error.page.size.invalid", "Page size cannot be empty and must be greater than 0");
        }
    }

    /**
     * Check if the file supports
     */
    public static boolean isSupportedFile(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            return false;
        }
        String extension = getFileExtension(filePath);
        return SUPPORTED_EXTENSIONS.contains(extension.toLowerCase());
    }

    /**
     * Get a list of supported file extensions
     */
    public static List<String> getSupportedExtensions() {
        return new ArrayList<>(SUPPORTED_EXTENSIONS);
    }

    /**
     * Read file data in batches
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
                log.error("Failed to read file: {}", filePath, e);
                results.put(filePath, createEmptyResult());
            }
        }

        return results;
    }

    /**
     * Get basic file information
     */
    public static Map<String, Object> getFileInfo(String filePath) {
        if (!FileUtil.exist(filePath)) {
            throw new RuntimeException(MessageUtils.messageWithFallback(
                    "sys.error.file.notfound.path", "File does not exist: {0}", filePath));
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
