export const treeData =
  [
    {
      id: 2,
      label: "DM",
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
      label: "MYSQL",
      value: "MYSQL",
      type: "MYSQL",
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
    },
    // 禁用的节点
    {
      id: 6,
      label: "Sqlerver",
      value: "Sqlerver",
      type: "SQLSERVER",
      children: [],
      disabled: true // 禁用该节点
    },
    {
      id: 7,
      label: "PostgreSql",
      value: "PostgreSql",
      type: "POSTGRESQL",
      children: [],
      disabled: true // 禁用该节点
    }
  ];
