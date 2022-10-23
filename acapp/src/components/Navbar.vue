<script setup lang="ts">
import logo from '~/assets/logo.png'
import avatar from '~/assets/avatar.jpg'

const { user } = storeToRefs(useUserStore())

const pageStore = usePageStore()
const { currentPage } = storeToRefs(pageStore)
const { changePage } = pageStore
</script>

<template>
  <div w-80vw h-100px pt-20px flex justify-between items-center mxa lt-md="w96vw">
    <div flex items-center h-full>
      <div w50px h50px bg="[#6d7083]" rounded-2px cursor-pointer hover:shadow-nav_item>
        <img :src="logo" h-full w-full rounded-full>
      </div>
      <div
        h50px w194px lt-sm:hidden
        flex justify-center items-center
        bg="[#6d7083]" rounded-2px max-w-202px ml-2
        text="22px center ellipsis" font-bold
      >
        哥们一起战斗吧
      </div>
    </div>

    <div flex items-center gap-x-2>
      <div
        v-if="currentPage > 0"
        w60px h40px flex justify-center items-center text-xl
        border="2px solid [#595B6F]" rounded-5px cursor-pointer outline-none
        hover="bg-[#6D7083] border-[#6D7083]"
        @click="changePage(0)"
      >
        返回
      </div>
      <div
        w60px h40px flex justify-center items-center text-2xl
        border="1 solid primary" bg-primary rounded-5px
        cursor-pointer outline-none hover:shadow-nav_item
      >
        <a
          i-ri:github-fill text-2xl bg-white
          href="https://github.com/cosmoscatts/kob"
          target="_blank" title="GitHub"
        />
      </div>
      <div w40px h40px ml-4>
        <n-tooltip placement="bottom" trigger="hover">
          <template #trigger>
            <img :src="avatar" h-full w-full rounded-full cursor-pointer>
          </template>
          <span> {{ user?.name ?? '匿名玩家' }} </span>
        </n-tooltip>
      </div>
    </div>
  </div>
</template>

