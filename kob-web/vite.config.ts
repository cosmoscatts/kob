import { resolve } from 'node:path';
import Vue from '@vitejs/plugin-vue';
import UnoCSS from 'unocss/vite';
import AutoImport from 'unplugin-auto-import/vite';
import { NaiveUiResolver } from 'unplugin-vue-components/resolvers';
import Components from 'unplugin-vue-components/vite';
import { defineConfig } from 'vite';

export default defineConfig({
  resolve: {
    alias: {
      '~/': `${resolve(__dirname, 'src')}/`,
    },
  },
  plugins: [
    UnoCSS(),
    Vue(),
    AutoImport({
      imports: [
        'vue',
        'vue-router',
        '@vueuse/head',
        '@vueuse/core',
        'pinia',
      ],
      dts: 'src/auto-imports.d.ts',
      dirs: [
        'src/server/api',
        'src/composables',
        'src/store',
      ],
      vueTemplate: true,
    }),
    Components({
      resolvers: [
        NaiveUiResolver(),
      ],
      dts: 'src/components.d.ts',
    }),
  ],
});
