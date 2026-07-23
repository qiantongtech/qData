/**
 * Find tree nodes
 */
export const findInTree = (tree, id) => {
  if (!tree || id === null || id === undefined || id === "") return null;
  for (const node of tree) {
    // Search child nodes first to solve the problem of the same parent and child node IDs or the need to obtain deeper node information.
    if (node.children && node.children.length > 0) {
      const found = findInTree(node.children, id);
      if (found) return found;
    }
    if (node.id == id) return node;
  }
  return null;
};

/**
 * Generate table model name
 * @param {Object} options
 * @param {Array} options.dataLayerList Data warehouse hierarchical list
 * @param {number|string} options.dataLayerId selected layer ID
 * @param {string} options.type model type (4 is the application table)
 * @param {Array} options.themeDomainList theme domain list (used when type=4)
 * @param {number|string} options.themeDomainId selected theme domain ID
 * @param {Array} options.businessCategoryList Business category list (used when type!=4)
 * @param {number|string} options.businessDomainId selected business category ID
 * @param {Array} options.dataDomainList Data domain list
 * @param {number|string} options.dataDomainId selected data domain ID
 * @param {string} options.modelNameSuffix table name suffix
 * @param {number} options.tableCase naming case (1: all uppercase, 2: all lowercase)
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

  // 1. Data warehouse stratification
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
    // Application Table (ADS)
    // 2. Topic
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
    // 2. Business classification
    const biz = findInTree(businessCategoryList, businessDomainId);
    if (biz) {
      parts.push(biz.engName || biz.shortName || biz.name);
    } else {
      const bizAbbr = options.businessCategoryEngName || options.businessCategoryShortName;
      if (bizAbbr) {
        parts.push(bizAbbr);
      }
    }
    // 3. Data segmentation
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

  // 4. Table name suffix
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
 * Attribution hierarchy display formatting method for list display (name (English name) / ...)
 * @param {Object} row list row data
 * @param {string} type model type (4 is the application table)
 */
export const formatHierarchyDisplayName = (row, type) => {
  const parts = [];

  // 1. Data warehouse stratification
  if (row.dataLayerName) {
    const eng = row.dataLayerEngName || row.dataLayerShortName;
    parts.push(eng ? `${row.dataLayerName} (${eng})` : row.dataLayerName);
  }

  if (type === "4") {
    // 2. Application Sheet (ADS): Subject
    if (row.themeDomainName) {
      const eng = row.themeDomainEngName || row.themeDomainShortName;
      parts.push(eng ? `${row.themeDomainName} (${eng})` : row.themeDomainName);
    }
  } else {
    // 2. Business classification
    if (row.businessCategoryName) {
      const eng = row.businessCategoryEngName || row.businessCategoryShortName;
      parts.push(eng ? `${row.businessCategoryName} (${eng})` : row.businessCategoryName);
    }
    // 3. Data segmentation
    if (row.dataDomainName) {
      const eng = row.dataDomainEngName || row.dataDomainShortName;
      parts.push(eng ? `${row.dataDomainName} (${eng})` : row.dataDomainName);
    }
  }

  return parts.join(" / ") || "-";
};
