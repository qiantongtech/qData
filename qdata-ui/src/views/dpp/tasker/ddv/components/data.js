export const treeData =
  [
    {
      id: 2,
      label: "DM",
      type: "DM",
      falg: true,
      children: [],
      icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
    },
    {
      id: 3,
      label: "Oracle",
      type: "ORACLE",
      falg: true,
      children: [],
      icon: new URL('@/assets/system/images/dpp/oracle.png', import.meta.url).href,
    },
    {
      id: 4,
      label: "MYSQL",
      type: "MYSQL",
      falg: true,
      children: [],
      icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
    },
    {
      id: 5,
      label: "Kingbase",
      type: "KINGBASE",
      falg: true,
      children: [],
      icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
    },
    // 禁用的节点
    {
      id: 6,
      label: "Sqlerver",
      children: [],
      disabled: true // 禁用该节点
    },
    {
      id: 7,
      label: "PostgreSql",
      children: [],
      disabled: true // 禁用该节点
    },
    // {
    //   id: 8,
    //   label: "Hive",
    //   children: [],
    //   disabled: false // 禁用该节点
    // },
    {
      id: 9,
      label: "SparkSql",
      children: [],
      icon: new URL('@/assets/system/images/dpp/sr.png', import.meta.url).href,
    },

  ];
