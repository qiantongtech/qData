import useLocaleStore from '@/store/system/locale'

const modules = import.meta.glob(
  '@/assets/images/locales/**/*.{png,jpg,jpeg,svg,webp,gif}',
  { eager: true, import: 'default' }
)

const findBySuffix = (suffix) => {
  for (const [key, url] of Object.entries(modules)) {
    if (key.endsWith(suffix)) return url
  }
  return ''
}

export function useLocaleImage() {
  const localeStore = useLocaleStore()

  const getImage = (path) => {
    const lang = localeStore.getCurrentLocale.lang
    return findBySuffix(`/${lang}/${path}`) || findBySuffix(`/zh-CN/${path}`) || ''
  }

  return { getImage }
}
