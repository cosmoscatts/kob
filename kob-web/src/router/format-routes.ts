import type { RouteRecordRaw } from 'vue-router';

const modules = import.meta.glob('./routes/*.ts', { eager: true });

const formatModules = (modules: Record<string, any>): RouteRecordRaw[] => {
  return Object.values(modules)
    .flatMap(module => module.default)
    .filter((route): route is RouteRecordRaw => route !== undefined);
};

const routes: RouteRecordRaw[] = formatModules(modules);

export default routes;
