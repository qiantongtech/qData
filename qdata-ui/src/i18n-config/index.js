/**
 * 导入所有语言目录下的 json 文件
 */
const allLangModules = import.meta.glob('./*/**/*.json', { eager: true });

/**
 * 自动引入指定语言目录下的所有 json 文件
 * @param {string} lang - 语言代码，如 'en-US' 或 'zh-Hans'
 * @returns {Object} 合并后的语言对象
 */
export function autoImportLangFiles(lang) {
  const result = {};
  const langPrefix = `./${lang}/`;
  
  Object.keys(allLangModules).forEach(filePath => {
    if (filePath.startsWith(langPrefix)) {
      const content = allLangModules[filePath].default;
      const pathParts = filePath
        .replace(langPrefix, '')
        .replace('.json', '')
        .split('/');
      
      let current = result;
      pathParts.forEach((part, index) => {
        if (index === pathParts.length - 1) {
          current[part] = content;
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