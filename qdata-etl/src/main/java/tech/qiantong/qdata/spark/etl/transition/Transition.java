package tech.qiantong.qdata.spark.etl.transition;

import com.alibaba.fastjson2.JSONObject;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

/**
 * <P>
 * 用途:转换器接口
 * </p>
 *
 * @author: FXB
 * @create: 2025-06-20 09:04
 **/
public interface Transition {

    Dataset<Row> transition(SparkSession spark, Dataset<Row> dataset, JSONObject transition, LogUtils.Params logParams);

    String code();
}
