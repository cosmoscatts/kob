import App from './App.vue'
import PreLoadPage from './components/PreLoadPage.vue'
import type { AppContext, UserModule } from './types'

import 'animate.css'
import 'uno.css'
import './style.css'

const setupModules = async (app: AppContext) => {
  const flag = ref(true)
  useTimeoutFn(() => flag.value = false, 1600)

  const preload = createApp(PreLoadPage)
  preload.mount('#preload')

  Object.values(import.meta.glob<{ install: UserModule }>('./modules/*.ts', { eager: true }))
    .forEach(i => i.install?.(app))

  await until(flag).not.toBeTruthy()
  app.mount('#app')
}

// 解决 tailwind 的 preflight 样式覆盖 naive 组件样式的问题
// https://www.naiveui.com/zh-CN/os-theme/docs/style-conflict
const meta = document.createElement('meta')
meta.name = 'naive-ui-style'
document.head.appendChild(meta)

setupModules(createApp(App))
