<script setup lang="ts">
import { darkTheme, dateZhCN, zhCN } from 'naive-ui'
import Home from '~/pages/home/index.vue'
import PK from '~/pages/pk/index.vue'
import RankList from '~/pages/rank/index.vue'
import RecordList from '~/pages/record/index.vue'
import UserBot from '~/pages/user-bot/index.vue'

const themeOverrides = useThemeOverrides()
// 将 `naive-ui` 自带颜色写入 `body`
writeThemeColorsToBody()

const { currentPage } = storeToRefs(usePageStore())

const refContainer = ref<HTMLElement>()
const { width: containerWidth, height: containerHeight } = useElementSize(refContainer)

provide('containerWidth', containerWidth)
provide('containerHeight', containerHeight)

const ui = reactive({
  scale: 1,
  margin: 0,
})

const setPosition = () => {
  const curHeight = containerHeight.value - 10
  const height = 500
  ui.scale = curHeight / height
  ui.margin = ((ui.scale - 1) * height) / 2 + 5
}

window.onresize = setPosition
watch(containerHeight, (val, old) => {
  if (val !== old)
    setPosition()
})

onMounted(() => {
  useTimeoutFn(setPosition, 500)
})

const userStore = useUserStore()

async function login() {
  const { message } = useGlobalNaiveApi()
  const { code, data } = await UserApi.applyAcCode()
  if (code !== 0) {
    message.error('获取授权登录失败')
    userStore.acWingOS.api.window.close()
    return
  }
  const { appid, redirectUri, scope, state } = data
  userStore.acWingOS.api.oauth2.authorize(
    appid,
    redirectUri,
    scope,
    state,
    (resp: any) => {
      if (resp.code === 0) {
        userStore.setToken(resp.data.token)
        userStore.updateUser()
      }
      else { userStore.acWingOS.api.window.close() }
    },
  )
}
login()
</script>

<template>
  <n-config-provider
    id="container"
    ref="refContainer"
    :theme="darkTheme"
    :theme-overrides="themeOverrides"
    :locale="zhCN"
    :date-locale="dateZhCN"
    flex justify-center
    wfull hfull font-self m0 p0 of-hidden
    text-white bg="[#47485C]"
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
