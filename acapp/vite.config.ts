import { resolve } from 'path'
import { defineConfig } from 'vite'
import Vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite'
import AutoImport from 'unplugin-auto-import/vite'
import DefineOptions from 'unplugin-vue-define-options/vite'
import { NaiveUiResolver } from 'unplugin-vue-components/resolvers'
import UnoCSS from 'unocss/vite'

export default defineConfig({
  resolve: {
    alias: {
      '~/': `${resolve(__dirname, 'src')}/`,
    },
  },

  plugins: [
    UnoCSS(),

    Vue({
      reactivityTransform: true,
    }),

    DefineOptions(),

    AutoImport({
      imports: [
        'vue',
        'vue/macros',
        '@vueuse/core',
        'pinia',
      ],
      dts: 'src/autoImports.d.ts',
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
})
