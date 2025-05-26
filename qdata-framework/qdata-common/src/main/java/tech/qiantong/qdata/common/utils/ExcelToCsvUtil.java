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
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2025-03-13 17:28
 **/
public class ExcelToCsvUtil {

    /**
     * excel转csv并解析出字段
     *
     * @param excelPath
     * @param csvPath
     * @param startColumn 字段名行 （这里第一行就是1 不是0）
     * @param startData   数据开始行
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

            Sheet sheet = workbook.getSheetAt(0); // 选择第一个工作表
            List<String> csvLines = new ArrayList<>();

            if (startColumn > sheet.getLastRowNum() + 1) {
                throw new ServiceException("startColumn大于最后一行的行号，请检查startColumn的值");
            }

            //读取列
            Row columnRow = sheet.getRow(startColumn - 1);
            String columnStr = toStr(columnRow);
            csvLines.add(columnStr);

            //读取数据
            for (int i = startData - 1; i <= sheet.getLastRowNum(); i++) {
                csvLines.add(toStr(sheet.getRow(i)));
            }

            File csvFile = new File(csvPath);

            // 检查父目录是否存在，如果不存在则创建
            if (!csvFile.getParentFile().exists()) {
                csvFile.getParentFile().mkdirs(); // 创建所有必要的父目录
            }

            // 写入CSV文件
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath))) {
                for (String line : csvLines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            //解析字段
            columnList = Arrays.asList(columnStr.split(","));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("excel转csv失败");
        }
        return columnList;
    }

    /**
     * 读取一行数据
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

            // 处理包含逗号、换行或双引号的情况
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
     * csv解析出字段
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
            throw new ServiceException("csv为空无法解析");
        }
        try {
            parser.next();
            parser.getHeader();
        } catch (Exception e) {
            throw new ServiceException("csv解析失败");
        }
        return parser.getHeader();
    }

    /**
     * 校验字段是否符合条件
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
