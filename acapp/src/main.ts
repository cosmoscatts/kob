import { createPinia } from 'pinia'
import piniaPluginPersist from 'pinia-plugin-persist-uni'
import App from './App.vue'

import 'animate.css'
import 'uno.css'
import './style.css'

// 解决 tailwind 的 preflight 样式覆盖 naive 组件样式的问题
// https://www.naiveui.com/zh-CN/os-theme/docs/style-conflict
const meta = document.createElement('meta')
meta.name = 'naive-ui-style'
document.head.appendChild(meta)

const pinia = createPinia()
pinia.use(piniaPluginPersist)

createApp(App).use(pinia).mount('#app')

