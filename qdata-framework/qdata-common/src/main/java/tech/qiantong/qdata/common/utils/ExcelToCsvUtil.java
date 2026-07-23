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

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.text.csv.CsvParser;
import cn.hutool.core.text.csv.CsvReadConfig;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tech.qiantong.qdata.common.exception.ServiceException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <P>
 * Purpose:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-13 17:28
 **/
public class ExcelToCsvUtil {

    /**
     * Convert excel to csv and parse out the fields
     *
     * @param excelPath
     * @param csvPath
     * @param startColumn field name line (the first line here is 1, not 0)
     * @param startData data start row
     * @return
     * @throws IOException
     */
    public static List<String> convertExcelToCsv(String excelPath, String csvPath, Integer startColumn, Integer startData) {
        List<String> columnList = new ArrayList<>();
        Workbook workbook;
        try {
            InputStream inputStream = new FileInputStream(excelPath);
            if (excelPath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new HSSFWorkbook(inputStream);
            }

            Sheet sheet = workbook.getSheetAt(0); // Select the first worksheet
            List<String> csvLines = new ArrayList<>();

            if (startColumn > sheet.getLastRowNum() + 1) {
                throw new ServiceException("sys.error.excel.start.column.invalid",
                        "startColumn exceeds the last row index; check the startColumn value");
            }

            //Read columns
            Row columnRow = sheet.getRow(startColumn - 1);
            String columnStr = toStr(columnRow);
            csvLines.add(columnStr);

            //Read data
            for (int i = startData - 1; i <= sheet.getLastRowNum(); i++) {
                csvLines.add(toStr(sheet.getRow(i)));
            }

            File csvFile = new File(csvPath);

            // Check if the parent directory exists, if not create it
            if (!csvFile.getParentFile().exists()) {
                csvFile.getParentFile().mkdirs(); // Create all necessary parent directories
            }

            // Write to CSV file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath))) {
                for (String line : csvLines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            //Parse fields
            columnList = Arrays.asList(columnStr.split(","));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("sys.error.excel.to.csv.fail", "Failed to convert Excel to CSV");
        }
        return columnList;
    }

    /**
     * Read a row of data
     *
     * @param row
     * @return
     */
    public static String toStr(Row row) {
        DataFormatter dataFormatter = new DataFormatter();
        StringBuilder csvLine = new StringBuilder();
        int lastCellNum = row.getLastCellNum();
        for (int cellIdx = 0; cellIdx < row.getLastCellNum(); cellIdx++) {
            Cell cell = row.getCell(cellIdx, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String cellValue = dataFormatter.formatCellValue(cell);

            // Handles cases containing commas, newlines, or double quotes
            if (cellValue.contains(",") || cellValue.contains("\"") || cellValue.contains("\n")) {
                cellValue = "\"" + cellValue.replace("\"", "\"\"") + "\"";
            }

            csvLine.append(cellValue);
            if (cellIdx < lastCellNum - 1) {
                csvLine.append(",");
            }
        }
        return csvLine.toString();
    }


    /**
     * csv parses out fields
     *
     * @param csvPath
     * @return
     * @throws IOException
     */
    public static List<String> parseCsv(String path, String csvPath) {
        FileUtil.copy(path, csvPath, true);
        File file = FileUtil.file(path);
        BufferedReader reader = PathUtil.getReader(file.toPath(), StandardCharsets.UTF_8);
        CsvReadConfig csvReadConfig = new CsvReadConfig()
                .setHeaderLineNo(0L);
        CsvParser parser = new CsvParser(reader, csvReadConfig);
        if (!parser.hasNext()) {
            throw new ServiceException("sys.error.csv.empty", "CSV is empty and cannot be parsed");
        }
        try {
            parser.next();
            parser.getHeader();
        } catch (Exception e) {
            throw new ServiceException("sys.error.csv.parse.fail", "Failed to parse CSV");
        }
        return parser.getHeader();
    }

    /**
     * Check whether the field meets the conditions
     *
     * @param columnList
     * @return
     */
    public static Boolean verifyColumn(List<String> columnList) {
        String regex = "^(?!\\d+$)[\\u4e00-\\u9fffA-Za-z0-9_]+$";
        for (String column : columnList) {
            if (!column.matches(regex)) {
                return false;
            }
        }
        return true;
    }
}
