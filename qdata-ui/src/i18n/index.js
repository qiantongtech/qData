import { createI18n } from 'vue-i18n';
import useAppStore from '@/store/system/app';
import { autoImportLangFiles } from '@/i18n-config/index';

export let i18n = null;

const setHtmlPageLang = (locale) => {
    document.querySelector('html')?.setAttribute('lang', locale);
};

const createI18nOptions = async () => {
    const appStore = useAppStore();
    const locale = appStore.getCurrentLocale();
    const localeMap = appStore.getLocaleMap();
    const message = await autoImportLangFiles(locale.lang);
    setHtmlPageLang(locale.lang);
    appStore.setCurrentLocale({
        lang: locale.lang
    });

    return {
        legacy: false,
        locale: locale.lang,
        fallbackLocale: locale.lang,
        messages: {
            [locale.lang]: message
        },
        availableLocales: localeMap.map((v) => v.lang),
        sync: true,
        silentTranslationWarn: true,
        missingWarn: false,
        silentFallbackWarn: true
    };
};

export const setupI18n = async (app) => {
    const options = await createI18nOptions();
    i18n = createI18n(options);
    app.use(i18n);
};

export const setLanguage = async (lang) => {
    const appStore = useAppStore();
    const defaultLocal = await import(`@/i18n-config/${lang}.js`);
    const message = defaultLocal.default ?? {};
    setHtmlPageLang(lang);
    appStore.setCurrentLocale({
        lang
    });
    i18n.global.setLocaleMessage(lang, message);
    if (i18n.mode === 'legacy') {
        i18n.global.locale = lang;
    } else {
        i18n.global.locale.value = lang;
    }
};
