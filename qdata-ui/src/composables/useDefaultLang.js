import { useI18n } from 'vue-i18n';

function useDefaultLang() {
    const i18n = useI18n();

    const interpolate = (str, params) => {
        if (!params || !str) return str;
        return str.replace(/\{(\w+)\}/g, (match, key) => {
            return params[key] !== undefined ? params[key] : match;
        });
    };

    const td = (key, def, params) => {
        // 使用 try-catch 确保即使 i18n 未正确初始化也不会报错
        try {
            // 使用 te() 检查 key 是否存在，如果不存在返回默认值
            const result = i18n.te(key) ? i18n.t(key, params) : def;
            return result;
        } catch (e) {
            return interpolate(def, params);
        }
    };

    return {
        ...i18n,
        td
    };
}

export default useDefaultLang;
