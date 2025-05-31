import { DE } from "./de";
import { EN } from "./en";
import type { i18nKeys } from "./keys";
import type { Language } from "./language";

export function getLanguage(currentLocale: string | undefined): Language {
  if (currentLocale === "de") {
    return "de";
  }

  return "en";
}

export function i18n(key: i18nKeys, language: Language): string {
  switch (language) {
    case "de":
      return DE[key];
    default:
      return EN[key];
  }
}
