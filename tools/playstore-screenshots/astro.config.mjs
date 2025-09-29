// @ts-check
import { defineConfig } from 'astro/config';

import tailwindcss from '@tailwindcss/vite';

// https://astro.build/config
export default defineConfig({
  i18n: {
    locales: ['en', 'de'],
    defaultLocale: 'en',
    fallback: {
      de: 'en',
    },
    routing: {
      fallbackType: 'redirect',
    },
  },
  vite: {
    plugins: [tailwindcss()]
  }
});