package tech.qiantong.qdata.spark.etl.writer;

import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * <P>
 * 用途:输出组件注册
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-25 09:35
 **/
public class WriterRegistry {
    private final Map<String, Writer> readerMap = new HashMap<>();

    public WriterRegistry() {
        this.readerMap.put(TaskComponentTypeEnum.DB_WRITER.getCode(), new DBWriter());
    }
    public Writer getWriter(String code) {
        return this.readerMap.get(code);
    }
}
