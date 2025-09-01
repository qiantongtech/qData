package tech.qiantong.qdata.spark.etl.writer;

import com.alibaba.fastjson2.JSONObject;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

/**
 * <P>
 * 用途:写数据
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-25 09:34
 **/
public interface Writer {

    Boolean writer(JSONObject config,Dataset<Row> dataset, JSONObject writer, LogUtils.Params logParams);

    String code();
}
