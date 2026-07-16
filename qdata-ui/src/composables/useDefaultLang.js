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
        // Use try-catch to ensure that no error is reported even if i18n is not initialized correctly
        try {
            // Use te() to check whether key exists, and return the default value if it does not exist
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
