/**
 * 导入所有语言目录下的 json 文件
 */
const allLangModules = import.meta.glob('./**/*.json', { eager: true });

/**
 * 自动引入指定语言目录下的所有 json 文件
 * @param {string} lang - 语言代码，如 'en-US' 或 'zh-Hans'
 * @returns {Object} 合并后的语言对象
 */
export function autoImportLangFiles(lang) {
  const result = {};
  // 匹配 /lang/，兼容 windows 和 linux 路径分隔符
  // 注意：import.meta.glob 返回的 key 通常都是 / 分隔的
  const langSegment = `/${lang}/`;

  Object.keys(allLangModules).forEach(filePath => {
    // filePath 例如 ./zh-Hans/login.json
    if (filePath.includes(langSegment)) {
      const content = allLangModules[filePath].default;
      
      // 截取 langSegment 之后的部分
      // 例如 login.json 或 att/rule/auditRule.json
      const index = filePath.indexOf(langSegment);
      const relativePath = filePath.substring(index + langSegment.length);
      
      const parts = relativePath.split('/');
      
      let current = result;
      parts.forEach((part, index) => {
        if (index === parts.length - 1) {
          // 最后一个部分是文件名，去掉 .json
          const fileName = part.replace('.json', '');
          current[fileName] = content;
        } else {
          if (!current[part]) {
            current[part] = {};
          }
          current = current[part];
        }
      });
    }
  });

  return result;
}