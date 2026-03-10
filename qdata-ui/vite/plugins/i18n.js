import VueI18nPlugin from '@intlify/unplugin-vue-i18n/vite';
import { resolve } from 'path';

export default function createVueI18nPlugin() {
    return VueI18nPlugin({
        runtimeOnly: true,
        compositionOnly: true,
        include: [resolve(__dirname, 'src/i18n-config/**')]
    });
}
