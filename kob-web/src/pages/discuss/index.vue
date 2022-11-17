<script setup lang="ts">
import DiscussItem from './components/DiscussItem.vue'
import SayWords from './components/SayWords.vue'
import type { Discuss } from '~/types'

const pagination = usePagination({
  onChangeCallback: fetchDiscussList,
  onUpdatePageSizeCallback: fetchDiscussList,
})

let list = $ref<Discuss[]>([])
async function fetchDiscussList() {
  const { page, pageSize } = pagination
  try {
    const { data: { records, total } } = await DiscussApi.getDiscussList({ page, pageSize })
    list = records!
    pagination.itemCount = total!
  }
  catch (err) {
    // 处理异常
  }
}
fetchDiscussList()

let likes = $ref<number[]>([])
async function fetchCurrentUserLikes() {
  try {
    const { data } = await DiscussApi.getCurrentUserLikes()
    likes = data ?? []
  }
  catch (err) {
    likes = []
  }
}
fetchCurrentUserLikes()

const getAuthLike = (remarkId?: number) => !!remarkId && likes?.includes(remarkId)

function likeCallback({ id, type }: { id?: number; type: 'like' | 'dislike' }) {
  if (!id)
    return
  fetchCUrrentUserLikes()
  const item = list.find(i => i.id === id)
  if (item?.likes !== undefined)
    item.likes = item.likes + [1, -1][type === 'like' ? 0 : 1]
}
</script>

<template>
  <div w-70vw mx-a flex="~ col">
    <n-card>
      <div text="1.6rem center" font-800 mb10px>
        我要提 Bug ！！！(╯▔皿▔)╯
      </div>
      <n-alert title="意见栏" type="info">
        @Everyone：欢迎小伙伴们留下自己的建议，如果有时间我会持续完善的。
      </n-alert>

      <div mt20px>
        <n-list bordered clickable>
          <n-list-item>
            <SayWords @refresh="fetchDiscussList" />
          </n-list-item>
          <n-list-item v-for="item in list" :key="item.id">
            <DiscussItem :item="item" :is-auth-like="getAuthLike(item?.id)" @like-callback="likeCallback" />
          </n-list-item>
        </n-list>
        <div v-if="list?.length > 0" mt10px flex justify-end>
          <n-pagination
            :page="pagination.page"
            :item-count="pagination.itemCount"
            :page-sizes="pagination.pageSizes"
            :page-size="pagination.pageSize"
            :show-size-picker="pagination.showSizePicker"
            :on-update:page="pagination.onChange"
            :on-update:page-size="pagination.onUpdatePageSize"
          />
        </div>
      </div>
    </n-card>
  </div>
</template>
