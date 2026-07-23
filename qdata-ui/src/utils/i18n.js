import { i18n } from "@/plugins/vueI18n";

export const interpolate = (str, params) => {
  if (!params || !str) return str;
  return str.replace(/\{(\w+)\}/g, (match, key) => {
    return params[key] !== undefined ? params[key] : match;
  });
};

export const td = (key, def = key, params) => {
  try {
    return i18n?.global?.te(key) ? i18n.global.t(key, params) : interpolate(def, params);
  } catch {
    return interpolate(def, params);
  }
};
