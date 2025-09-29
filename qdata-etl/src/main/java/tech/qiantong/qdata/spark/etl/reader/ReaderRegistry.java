package tech.qiantong.qdata.spark.etl.reader;

import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>
 * 用途:输入组件注册
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-21 13:38
 **/
public class ReaderRegistry {

    private final Map<String, Reader> readerMap = new HashMap<>();

    public ReaderRegistry() {
        this.readerMap.put(TaskComponentTypeEnum.DB_READER.getCode(), new DBReader());
        this.readerMap.put(TaskComponentTypeEnum.EXCEL_READER.getCode(), new ExcelReader());
        this.readerMap.put(TaskComponentTypeEnum.CSV_READER.getCode(), new CsvReader());
    }

    public Reader getReader(String code) {
        return this.readerMap.get(code);
    }
}
