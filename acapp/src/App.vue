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
const { width: containerWidth, height: containerHeight } = useElementSize(refContainer)

provide('containerWidth', containerWidth)
provide('containerHeight', containerHeight)

const ui = reactive({
  scale: 1,
  margin: 0,
})

const setPosition = () => {
  const curHeight = window.innerHeight - 10
  const height = 500
  ui.scale = curHeight / height
  ui.margin = ((ui.scale - 1) * height) / 2 + 5
}

window.onresize = setPosition
setPosition()
</script>

<template>
  <n-config-provider
    ref="refContainer"
    :theme="darkTheme"
    :theme-overrides="themeOverrides"
    :locale="zhCN"
    :date-locale="dateZhCN"
    flex justify-center
    wfull hfull font-self m0 p0 of-hidden
    rounded-2px text-white bg="[#47485C]"
    :style="{ transformStyle: 'preserve-3d' }"
  >
    <div
      w830px h500px
      :style="{
        marginTop: `${ui.margin}px`,
        transform: `scale(${ui.scale}) translateZ(1px)`,
      }"
    >
      <Navbar />
      <div of-hidden>
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

