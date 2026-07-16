/**
 * Data source type
 * key: The data source type identifier is associated with config. Do not change it...
 * label: data source type name for display
 * value: data source type identifier used to pass to the backend
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
    OSS_ALIYUN: { key: 'OSS_ALIYUN', label: '阿里云OSS', value: 'OSS-ALIYUN', type: 'success' },
    ClickHouse: { key: 'ClickHouse', label: 'ClickHouse', value: 'ClickHouse', type: 'primary' },
    Kafka: { key: 'Kafka', label: 'Kafka', value: 'Kafka', type: 'warning' },
    DB2: { key: 'DB2', label: 'DB2', value: 'DB2', type: 'primary' },
    OSCAR: { key: 'OSCAR', label: 'OSCAR', value: 'OSCAR', type: 'primary' },
    Redis: { key: 'Redis', label: 'Redis', value: 'Redis', type: 'primary' },
    RabbitMQ: { key: 'RabbitMQ', label: 'RabbitMQ', value: 'RabbitMQ', type: 'warning' },
    API: { key: 'API', label: 'API接口', value: 'API接口' },
    File: { key: 'File', label: '文件', value: 'excel、csv文件' },
    FlinkP: { key: 'FlinkP', label: 'Flink批处理', value: 'Flink批' },
    FlinkL: { key: 'FlinkL', label: 'Flink流处理', value: 'Flink流' },
    SparkSQL: { key: 'SparkSQL', label: 'SparkSQL', value: 'SparkSQL' }
};

/**
 * Data source type configuration
 * key: data source type identifier
 * value: 1 normal 2 disabled 3 hidden
 */
const globalConfig = {
    API: 3,
    File: 3,
    FlinkP: 3,
    FlinkL: 3,
    SparkSQL: 3
};
export const config = {
    // data connection
    datasource: {
        ...globalConfig
    },
    // Asset Map
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
    // Asset Map - Unstructured
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
    // Data query
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
    // data discovery
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
    // logic model
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
    // Data quality
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
    // Data Integration-Input-Spark
    dppIntegratioTaskInSPARK: {
        Phoenix: 2,
        FTP: 2,
        OSS_ALIYUN: 2,
        ...globalConfig
    },
    // Data Integration-Output-Spark
    dppIntegratioTaskOutSPARK: {
        Phoenix: 2,
        Kafka: 2,
        FTP: 2,
        OSS_ALIYUN: 2,
        ...globalConfig
    },
    // Data integration-input-Flink
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
    // Data integration-output-Flink
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
