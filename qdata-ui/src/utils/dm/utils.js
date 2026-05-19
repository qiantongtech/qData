/**
 * 查找树节点
 */
export const findInTree = (tree, id) => {
  if (!tree || id === null || id === undefined || id === "") return null;
  for (const node of tree) {
    // 优先搜索子节点，解决父子节点 ID 相同或需要获取更深层节点信息的问题
    if (node.children && node.children.length > 0) {
      const found = findInTree(node.children, id);
      if (found) return found;
    }
    if (node.id == id) return node;
  }
  return null;
};

/**
 * 生成表模型名称
 * @param {Object} options
 * @param {Array} options.dataLayerList 数仓分层列表
 * @param {number|string} options.dataLayerId 选中的分层ID
 * @param {string} options.type 模型类型 (4为应用表)
 * @param {Array} options.themeDomainList 主题域列表 (type=4时使用)
 * @param {number|string} options.themeDomainId 选中的主题域ID
 * @param {Array} options.businessCategoryList 业务分类列表 (type!=4时使用)
 * @param {number|string} options.businessDomainId 选中的业务分类ID
 * @param {Array} options.dataDomainList 数据分域列表
 * @param {number|string} options.dataDomainId 选中的数据分域ID
 * @param {string} options.modelNameSuffix 表名后缀
 * @param {number} options.tableCase 命名大小写 (1:全大写, 2:全小写)
 */
export const formatModelName = (options) => {
  const {
    dataLayerList = [],
    dataLayerId,
    type,
    themeDomainList = [],
    themeDomainId,
    businessCategoryList = [],
    businessDomainId,
    dataDomainList = [],
    dataDomainId,
    modelNameSuffix,
    tableCase = 1,
  } = options;

  let parts = [];

  const currentType = type || options.tableType;

  // 1. 数仓分层
  const layer = findInTree(dataLayerList, dataLayerId);
  if (layer) {
    parts.push(layer.engName || layer.shortName || layer.name);
  } else {
    const layerAbbr = options.dataLayerEngName || options.dataLayerShortName;
    if (layerAbbr) {
      parts.push(layerAbbr);
    }
  }

  if (currentType === "4") {
    // 应用表 (ADS)
    // 2. 所属主题
    const theme = findInTree(themeDomainList, themeDomainId);
    if (theme) {
      parts.push(theme.engName || theme.shortName || theme.name);
    } else {
      const themeAbbr = options.themeDomainEngName || options.themeDomainShortName;
      if (themeAbbr) {
        parts.push(themeAbbr);
      }
    }
  } else {
    // 2. 业务分类
    const biz = findInTree(businessCategoryList, businessDomainId);
    if (biz) {
      parts.push(biz.engName || biz.shortName || biz.name);
    } else {
      const bizAbbr = options.businessCategoryEngName || options.businessCategoryShortName;
      if (bizAbbr) {
        parts.push(bizAbbr);
      }
    }
    // 3. 数据分域
    const domain = findInTree(dataDomainList, dataDomainId);
    if (domain) {
      parts.push(domain.engName || domain.shortName || domain.name);
    } else {
      const domainAbbr = options.dataDomainEngName || options.dataDomainShortName;
      if (domainAbbr) {
        parts.push(domainAbbr);
      }
    }
  }

  // 4. 表名后缀
  const suffix = modelNameSuffix || options.modelName;
  if (suffix) {
    parts.push(suffix);
  }

  let name = parts.join("_");
  const currentTableCase = tableCase || options.tableCase || 1;
  if (name) {
    if (currentTableCase == 1) {
      name = name.toUpperCase();
    } else if (currentTableCase == 2) {
      name = name.toLowerCase();
    }
  }

  return name || "-";
};

/**
 * 列表展示用的归属层级显示格式化方法 (名称 (英文名) / ...)
 * @param {Object} row 列表行数据
 * @param {string} type 模型类型 (4为应用表)
 */
export const formatHierarchyDisplayName = (row, type) => {
  const parts = [];

  // 1. 数仓分层
  if (row.dataLayerName) {
    const eng = row.dataLayerEngName || row.dataLayerShortName;
    parts.push(eng ? `${row.dataLayerName} (${eng})` : row.dataLayerName);
  }

  if (type === "4") {
    // 2. 应用表 (ADS): 所属主题
    if (row.themeDomainName) {
      const eng = row.themeDomainEngName || row.themeDomainShortName;
      parts.push(eng ? `${row.themeDomainName} (${eng})` : row.themeDomainName);
    }
  } else {
    // 2. 业务分类
    if (row.businessCategoryName) {
      const eng = row.businessCategoryEngName || row.businessCategoryShortName;
      parts.push(eng ? `${row.businessCategoryName} (${eng})` : row.businessCategoryName);
    }
    // 3. 数据分域
    if (row.dataDomainName) {
      const eng = row.dataDomainEngName || row.dataDomainShortName;
      parts.push(eng ? `${row.dataDomainName} (${eng})` : row.dataDomainName);
    }
  }

  return parts.join(" / ") || "-";
};
