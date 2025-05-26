package tech.qiantong.qdata.spark.etl.reader;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import tech.qiantong.qdata.common.enums.TaskComponentTypeEnum;
import tech.qiantong.qdata.spark.etl.utils.EtlUtils;
import tech.qiantong.qdata.spark.etl.utils.LogUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.PrettyFormat;

/**
 * <P>
 * 用途:kafka输入
 * </p>
 *
 * @author: FXB
 * @create: 2025-04-21 13:33
 **/
@Slf4j
public class KafkaReader implements Reader {
    @Override
    public Dataset<Row> read(SparkSession spark, JSONObject reader, List<String> readerColumns, String logPath) {
        LogUtils.writeLog(logPath, "*********************************  Initialize task context  ***********************************");
        LogUtils.writeLog(logPath, "开始Kafka输入节点");
        LogUtils.writeLog(logPath, "开始任务时间: " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        LogUtils.writeLog(logPath, "任务参数：" + reader.toJSONString(PrettyFormat));
        //参数信息
        JSONObject parameter = reader.getJSONObject("parameter");
        //连接信息
        String bootstrapServers = parameter.getString("bootstrapServers");
        //主题
        String topic = parameter.getString("topic");
        //配置
        JSONObject config = parameter.getJSONObject("config");
        //字段
        JSONArray column = parameter.getJSONArray("column");

        StructType schema = DataTypes.createStructType(createStructType(column)); // 创建Schema

        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);

        if (config.size() > 0) {
            config.forEach((key, value) -> {
                props.put(key, String.valueOf(value));
            });
        }
        //判断是否存在配置组
        if (props.size() == 0 || !props.containsKey(ConsumerConfig.GROUP_ID_CONFIG)) {
            props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        }

        Consumer<String, String> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Arrays.asList(topic));
        int pollTimeoutMs = 2000;
        int retries = 5;
        /**
         * consumer 每次都是新创建，第一次poll时会重新加入消费者组，加入过程会进行Rebalance，而 Rebalance 会导致同一 Group 内的所有消费者都不能工作
         * 所以 poll 拉取的过程中，即使topic中有数据也不一定能拉到，因为 consumer 正在加入消费者组中
         * kafka-clients 没有对应的API、事件机制来知道 consumer 成功加入消费者组的确切时间
         * 故增加重试
         */
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(pollTimeoutMs));
        int i = 0;

        if (records.isEmpty()) {
            for (; i < retries; i++) {
                records = consumer.poll(Duration.ofMillis(pollTimeoutMs));
                log.info("第 {} 次重试，获取消息记录数[{}]", i + 1, records.count());
                if (!records.isEmpty()) {
                    break;
                }
            }
        }
        if (i >= retries) {
            log.info("重试 {} 次后，仍未获取到消息，请确认是否有数据、配置是否正确", retries);
            Dataset<Row> dataset = spark.createDataFrame(new ArrayList<>(), schema); // 创建DataFrame
            return dataset;
        }
        List<Row> rows = new ArrayList<>();
        readData(consumer, column.toJavaList(KafkaColumn.class), rows, records);
        do {
            records = consumer.poll(Duration.ofMillis(pollTimeoutMs));
            readData(consumer, column.toJavaList(KafkaColumn.class), rows, records);
        } while (!records.isEmpty() && records.count() >= 500);
        Dataset<Row> dataset = spark.createDataFrame(rows, schema); // 创建DataFrame
        dataset = dataset.select(column.stream().map(c -> new Column(((JSONObject) c).getString("name"))).toArray(Column[]::new));
        readerColumns.addAll(column.stream().map(c -> ((JSONObject) c).getString("name")).collect(Collectors.toList()));
        dataset.na().fill("Unknown").show(10);
        LogUtils.writeLog(logPath, "输入数据量为：" + rows.size());
        log.info("部分数据如下>>>>>>>>>>>>>>");
        dataset.na().fill("Unknown").show(10);
        LogUtils.writeLog(logPath, "部分数据：\n" + dataset.na().fill("Unknown").showString(10, 0, false));
        return dataset;
    }

    private void readData(Consumer<String, String> consumer, List<KafkaColumn> column, List<Row> rows, ConsumerRecords<String, String> records) {
        if (records.isEmpty()) {
            return;
        }
        for (ConsumerRecord<String, String> record : records) {
            List<Object> rowData = new ArrayList<>();
            String msgValue = record.value();
            List<Map<String, Object>> dataList = EtlUtils.processSourceData(JSONUtil.parse(msgValue), column);
            Map<String, Object> data = dataList.get(0);
            for (int i = 0; i < column.size(); i++) {
                KafkaColumn kafkaColumn = column.get(i);
                switch (kafkaColumn.getType()) {
                    case "STRING":
//                        rowData.add(Long.parseLong(cell.toString()));
                        rowData.add(data.get(kafkaColumn.getName()) != null ? String.valueOf(data.get(kafkaColumn.getName())) : null);
                        break;
                    case "LONG":
                        rowData.add(data.get(kafkaColumn.getName()) != null ? String.valueOf(data.get(kafkaColumn.getName())) : null);
                        break;
                    case "DOUBLE":
                        rowData.add(data.get(kafkaColumn.getName()) != null ? String.valueOf(data.get(kafkaColumn.getName())) : null);
                        break;
                    case "DATE":
                        // 暂只支持时间戳
                        rowData.add(data.get(kafkaColumn.getName()) != null ? Long.parseLong(String.valueOf(data.get(kafkaColumn.getName()))) : null);
                        break;
                    case "BOOL":
                        rowData.add(data.get(kafkaColumn.getName()) != null ? String.valueOf(data.get(kafkaColumn.getName())) : null);
                        break;
                    case "BYTES":
                        rowData.add(data.get(kafkaColumn.getName()) != null ? String.valueOf(data.get(kafkaColumn.getName())).getBytes(StandardCharsets.UTF_8) : null);
                        break;
                    default:
                        rowData.add(data.get(kafkaColumn.getName()) != null ? String.valueOf(data.get(kafkaColumn.getName())) : null);
                        break;
                }
            }
            rows.add(RowFactory.create(rowData.toArray())); // 转换为Spark的Row类型
        }
        consumer.commitAsync();
    }

    @Override
    public String code() {
        return TaskComponentTypeEnum.KAFKA_READER.getCode();
    }

    /**
     * 生成sparksql所需字段
     *
     * @param column
     * @return
     */
    private List<StructField> createStructType(JSONArray column) {
        log.info("column:{}", column);
        List<StructField> fields = new ArrayList<>(); // 定义字段结构，例如：fields.add(DataTypes.createStructField("column1", DataTypes.StringType, false));
        for (Object obj : column) {
            JSONObject jsonObject = (JSONObject) obj;
            //字段类型
            String type = jsonObject.getString("type");
            //字段名称
            String name = jsonObject.getString("name");
            if (StringUtils.isBlank(name)) {
                name = jsonObject.getString("columnName");
            }

            DataType dataType = null;
            switch (type) {
                case "LONG":
                    dataType = DataTypes.LongType;
                    break;
                case "STRING":
                    dataType = DataTypes.StringType;
                    break;
                case "DATE":
                    dataType = DataTypes.DateType;
                    break;
                case "BOOL":
                    dataType = DataTypes.BooleanType;
                    break;
                case "DOUBLE":
                    dataType = DataTypes.DoubleType;
                    break;
                case "BYTES":
                    dataType = DataTypes.ByteType;
                    break;
            }
            // 定义字段结构，例如：
            fields.add(DataTypes.createStructField(name, dataType, false));
        }
        return fields;
    }

    @Data
    public class KafkaColumn {

        private String name;

        private String type;

        private String key;

        /**
         * 切割循环键
         */
        private List<String> splitKeyList;

        /**
         * 切割循环键个数
         */
        private Integer splitKeyListSize;
    }
}
