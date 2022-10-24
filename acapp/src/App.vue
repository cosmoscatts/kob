<script setup lang="ts">
import { darkTheme, dateZhCN, zhCN } from 'naive-ui'
import Home from '~/pages/home/index.vue'
import PK from '~/pages/pk/index.vue'
import RankList from '~/pages/rank/index.vue'
import RecordList from '~/pages/record/index.vue'
import UserBot from '~/pages/user-bot/index.vue'
import { setToken } from '~/utils'

const themeOverrides = useThemeOverrides()
// 将 `naive-ui` 自带颜色写入 `body`
writeThemeColorsToBody()

const { currentPage } = storeToRefs(usePageStore())

setToken('eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5OGI2NTUyZjQ4YzM0ZTQ2YTIzMGVmNzk3YzY1NTczMCIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2NjUxNTM5OCwiZXhwIjoxNjY3NzI0OTk4fQ.ZO1SLIwTWtmOYPLLo0MdU9uC-l-SFTWDC9aczVzMraA')

const { updateUser } = useUserStore()
updateUser()

const refContainer = ref<HTMLElement>()
const { width: containerWidth } = useElementSize(refContainer)

provide('containerWidth', containerWidth)
</script>

<template>
  <n-config-provider
    :theme="darkTheme"
    :theme-overrides="themeOverrides"
    :locale="zhCN"
    :date-locale="dateZhCN"
  >
    <div ref="refContainer" wfull hfull m0 p0 of-hidden text-white bg="[#47485C]" font-self>
      <Navbar />
      <div of-hidden :style="{ height: 'calc(100% - 100px)' }">
        <Transition name="fade-slide" mode="out-in" appear>
          <Home v-if="currentPage === 0" />
          <PK v-else-if="currentPage === 1" />
          <RankList v-else-if="currentPage === 2" />
          <RecordList v-else-if="currentPage === 3" />
          <UserBot v-else-if="currentPage === 4" />
        </Transition>
      </div>
    </div>
  </n-config-provider>
</template>

