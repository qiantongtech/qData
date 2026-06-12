import { useI18n } from 'vue-i18n';

function useDefaultLang() {
    const i18n = useI18n();

    const td = (key, def) => {
        // 使用 try-catch 确保即使 i18n 未正确初始化也不会报错
        try {
            // 使用 te() 检查 key 是否存在，如果不存在返回默认值
            if (!i18n.te(key)) return def;
            return i18n.t(key);
        } catch (e) {
            return def;
        }
    };

    return {
        ...i18n,
        td
    };
}

export default useDefaultLang;
