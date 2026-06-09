export const setHtmlPageLang = (locale) => {
  document.querySelector('html')?.setAttribute('lang', locale)
}
