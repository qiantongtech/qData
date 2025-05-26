package tech.qiantong.qdata.spark.etl.reader;

import com.alibaba.fastjson2.JSONObject;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.List;

/**
 * <P>
 * 用途:读数据
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-21 13:34
 **/
public interface Reader {

    Dataset<Row> read(SparkSession spark, JSONObject reader, List<String> readerColumns,String logPath);

    String code();
}
