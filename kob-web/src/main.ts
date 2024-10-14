import type { AppContext, UserModule } from './types';
import { createApp } from 'vue';
import App from './App.vue';
import SplashScreen from './components/SplashScreen.vue';

import 'animate.css';
import 'uno.css';
import './style.css';

const setupModules = async (app: AppContext) => {
  const splashScreenDuration = 1600; // ms

  // 创建 SplashScreen 应用
  const splashScreenApp = createApp(SplashScreen);
  const splashScreenContainer = document.createElement('div');
  document.body.appendChild(splashScreenContainer);
  const splashScreenInstance = splashScreenApp.mount(splashScreenContainer) as unknown as { hide: () => void };

  // 加载模块
  Object.values(import.meta.glob<{ install: UserModule }>('./modules/*.ts', { eager: true }))
    .forEach(i => i.install?.(app));

  // 设置 SplashScreen 结束时间
  useTimeoutFn(() => {
    splashScreenInstance.hide();
    // 给予一些时间让 SplashScreen 完成淡出动画
    setTimeout(() => {
      splashScreenApp.unmount();
      document.body.removeChild(splashScreenContainer);
    }, 300);
  }, splashScreenDuration);

  // 等待 SplashScreen 结束后挂载主应用
  await new Promise(resolve => setTimeout(resolve, splashScreenDuration + 300));
  app.mount('#app');
};

// 解决 tailwind 的 preflight 样式覆盖 naive 组件样式的问题
const meta = document.createElement('meta');
meta.name = 'naive-ui-style';
document.head.appendChild(meta);

setupModules(createApp(App));
