export const treeData = [
  {
    id: 2,
    label: "DM8",
    value: "DM",
    type: "DM",
    falg: true,
    children: [],
    icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
  },
  {
    id: 3,
    label: "Oracle",
    value: "Oracle",
    type: "ORACLE",
    falg: true,
    children: [],
    icon: new URL('@/assets/system/images/dpp/oracle.png', import.meta.url).href,
  },
  {
    id: 4,
    label: "MySQL",
    value: "MySql",
    type: "MySql",
    falg: true,
    children: [],
    icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
  },
  {
    id: 5,
    label: "Kingbase",
    value: "Kingbase",
    type: "KINGBASE",
    falg: true,
    children: [],
    icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
    elTagType: "info", // 紫色
  },
  {
    id: 6,
    label: "Sqlerver",
    value: "Sqlerver",
    type: "SQLSERVER",
    children: [],
    disabled: true,
    elTagType: "warning", // 黄色
  },
  {
    id: 7,
    label: "PostgreSql",
    value: "PostgreSql",
    type: "POSTGRESQL",
    children: [],
    disabled: true,
    elTagType: "info", // 紫色
  },
  {
    id: 8,
    label: "Hive",
    value: "Hive",
    type: "HIVE",
    children: [],
    disabled: false,
    elTagType: "warning", // 黄色
  },
  {
    id: 9,
    label: "SparkSql",
    value: "SparkSql",
    type: "SPARKSQL",
    children: [],
    icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
  },
  {
    id: 10,
    label: "Flink批",
    value: "FlinkBatch",
    type: "FLINKBATCH",
    children: [],
    elTagType: "success", // 绿色
    icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
  },
  {
    id: 11,
    label: "Flink流",
    value: "FlinkStream",
    type: "FLINKSTREAM",
    children: [],
    icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
    elTagType: "success", // 绿色
  },
];
