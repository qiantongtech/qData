import { useI18n } from 'vue-i18n';

function useDefaultLang() {
    const i18n = useI18n();

    const td = (key, def) => {
        if (!i18n.te(key)) return def;
        return i18n.t(key);
    };

    return {
        ...i18n,
        td
    };
}

export default useDefaultLang;
