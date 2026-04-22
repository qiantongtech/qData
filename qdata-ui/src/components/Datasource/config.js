/**
 * 数据源类型
 * key: 数据源类型标识跟config关联 不要改...
 * label: 数据源类型名称 用于展示
 * value: 数据源类型标识 用于传给后端
 */
export const DatasourceTypes = {
    MySql: { key: 'MySql', label: 'MySql', value: 'MySql', type: 'primary' },
    DM8: { key: 'DM8', label: 'DM8', value: 'DM8', type: 'info' },
    Oracle: { key: 'Oracle', label: 'Oracle', value: 'Oracle', type: 'primary' },
    Oracle11: { key: 'Oracle11', label: 'Oracle11', value: 'Oracle11', type: 'primary' },
    Kingbase8: { key: 'Kingbase8', label: 'Kingbase8', value: 'Kingbase8', type: 'info' },
    SQL_Server: { key: 'SQL_Server', label: 'SQL_Server', value: 'SQL_Server', type: 'info' },
    SQL_Server2008: {
        key: 'SQL_Server2008',
        label: 'SQL_Server2008',
        value: 'SQL_Server2008',
        type: 'info'
    },
    Hive: { key: 'Hive', label: 'Hive', value: 'Hive', type: 'warning' },
    HDFS: { key: 'HDFS', label: 'HDFS', value: 'HDFS', type: 'success' },
    Doris: { key: 'Doris', label: 'Doris', value: 'Doris', type: 'info' },
    Phoenix: { key: 'Phoenix', label: 'Phoenix', value: 'Phoenix', type: 'warning' },
    PostgreSQL: { key: 'PostgreSQL', label: 'PostgreSQL', value: 'PostgreSQL', type: 'primary' },
    MongoDB: { key: 'MongoDB', label: 'MongoDB', value: 'MongoDB', type: 'primary' },
    FTP: { key: 'FTP', label: 'FTP', value: 'FTP', type: 'success' },
    OSS_ALIYUN: { key: 'OSS_ALIYUN', label: 'OSS(阿里云)', value: 'OSS-ALIYUN', type: 'success' },
    ClickHouse: { key: 'ClickHouse', label: 'ClickHouse', value: 'ClickHouse', type: 'primary' },
    Kafka: { key: 'Kafka', label: 'Kafka', value: 'Kafka', type: 'warning' },
    DB2: { key: 'DB2', label: 'DB2', value: 'DB2', type: 'primary' },
    OSCAR: { key: 'OSCAR', label: 'OSCAR', value: 'OSCAR', type: 'primary' },
    Redis: { key: 'Redis', label: 'Redis', value: 'Redis', type: 'primary' },
    RabbitMQ: { key: 'RabbitMQ', label: 'RabbitMQ', value: 'RabbitMQ', type: 'warning' },
    API: { key: 'API', label: 'API接口', value: 'API接口' },
    File: { key: 'File', label: 'excel、csv文件', value: 'excel、csv文件' },
    FlinkP: { key: 'FlinkP', label: 'Flink批', value: 'Flink批' },
    FlinkL: { key: 'FlinkL', label: 'Flink流', value: 'Flink流' },
    SparkSQL: { key: 'SparkSQL', label: 'SparkSQL', value: 'SparkSQL' }
};

/**
 * 数据源类型配置
 * key: 数据源类型标识
 * value: 1正常 2禁用 3隐藏
 */
const globalConfig = {
    API: 3,
    File: 3,
    FlinkP: 3,
    FlinkL: 3,
    SparkSQL: 3
};
export const config = {
    // 数据连接
    datasource: {
        ...globalConfig
    },
    // 资产地图
    daAsset: {
        Phoenix: 2,
        MongoDB: 2,
        Redis: 2,
        Kafka: 2,
        RabbitMQ: 2,
        HDFS: 3,
        FTP: 3,
        OSS_ALIYUN: 3,
        ...globalConfig,
        ClickHouse: 1
    },
    // 资产地图-非结构化
    daAssetUnstructured: {
        Oracle11: 3,
        MySql: 3,
        Oracle: 3,
        PostgreSQL: 3,
        SQL_Server2008: 3,
        SQL_Server: 3,
        DM8: 3,
        Kingbase8: 3,
        OSCAR: 3,
        DB2: 3,
        Hive: 3,
        ClickHouse: 3,
        Doris: 3,
        Phoenix: 3,
        MongoDB: 3,
        Redis: 3,
        Kafka: 3,
        RabbitMQ: 3,
        ...globalConfig
    },
    // 数据查询
    daDataQuery: {
        Phoenix: 3,
        MongoDB: 3,
        Redis: 3,
        Kafka: 3,
        RabbitMQ: 3,
        HDFS: 3,
        FTP: 3,
        OSS_ALIYUN: 3,
        ...globalConfig
    },
    // 数据发现
    daDiscovery: {
        OSCAR: 2,
        DB2: 2,
        Hive: 2,
        ClickHouse: 2,
        Phoenix: 2,
        MongoDB: 2,
        Redis: 2,
        Kafka: 2,
        RabbitMQ: 2,
        HDFS: 2,
        FTP: 2,
        OSS_ALIYUN: 2,
        ...globalConfig
    },
    // 逻辑模型
    dpModel: {
        Phoenix: 2,
        MongoDB: 2,
        Redis: 2,
        Kafka: 2,
        RabbitMQ: 2,
        HDFS: 2,
        FTP: 2,
        OSS_ALIYUN: 2,
        ...globalConfig
    },
    // 数据质量
    daQuality: {
        // SQL_Server2008: 2,
        // OSCAR: 2,
        // DB2: 2,
        Hive: 2,
        //ClickHouse: 2,
        //Doris: 2,
        Phoenix: 2,
        MongoDB: 2,
        Redis: 2,
        Kafka: 2,
        RabbitMQ: 2,
        HDFS: 2,
        FTP: 2,
        OSS_ALIYUN: 2,
        ...globalConfig
    },
    // 数据集成-输入-Spark
    dppIntegratioTaskInSPARK: {
        Phoenix: 2,
        FTP: 2,
        OSS_ALIYUN: 2,
        ...globalConfig
    },
    // 数据集成-输出-Spark
    dppIntegratioTaskOutSPARK: {
        Phoenix: 2,
        Kafka: 2,
        FTP: 2,
        OSS_ALIYUN: 2,
        ...globalConfig
    },
    // 数据集成-输入-Flink
    dppIntegratioTaskInFLINK: {
        SQL_Server2008: 2,
        OSCAR: 2,
        DB2: 2,
        Hive: 2,
        ClickHouse: 2,
        Doris: 2,
        Phoenix: 2,
        MongoDB: 2,
        Redis: 2,
        Kafka: 2,
        RabbitMQ: 2,
        HDFS: 2,
        FTP: 2,
        OSS_ALIYUN: 2,
        ...globalConfig
    },
    // 数据集成-输出-Flink
    dppIntegratioTaskOutFLINK: {
        SQL_Server2008: 2,
        OSCAR: 2,
        DB2: 2,
        Hive: 2,
        ClickHouse: 2,
        Doris: 2,
        Phoenix: 2,
        MongoDB: 2,
        Redis: 2,
        Kafka: 2,
        RabbitMQ: 2,
        HDFS: 2,
        FTP: 2,
        OSS_ALIYUN: 2,
        ...globalConfig
    },
    dsApi: {
        // OSCAR: 2,
        // DB2: 2,
        Phoenix: 2,
        MongoDB: 2,
        Redis: 2,
        Kafka: 2,
        RabbitMQ: 2,
        HDFS: 2,
        FTP: 2,
        OSS_ALIYUN: 2,
        ...globalConfig
    }
};
