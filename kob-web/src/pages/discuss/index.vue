<script setup lang="ts">
import DiscussItem from './components/DiscussItem.vue'
import SayWords from './components/SayWords.vue'
import type { Discuss } from '~/types'

const pagination = usePagination({
  onChangeCallback: fetchDiscussList,
  onUpdatePageSizeCallback: fetchDiscussList,
})

const { loading, startLoading, endLoading } = useLoading()

let list = $ref<Discuss[]>([])
const defaultLoadingData = [{ id: 1 }, { id: 2 }, { id: 3 }]
function fetchDiscussList() {
  startLoading()
  list = [...defaultLoadingData]
  const { page, pageSize } = pagination
  DiscussApi
    .getDiscussList({ page, pageSize })
    .then(({ data: { records = [], total = 0 } }) => {
      list = records
      pagination.itemCount = total
    })
    .catch(() => list = [])
    .finally(() => useTimeoutFn(endLoading, 500))
}
fetchDiscussList()

let likes = $ref<number[]>([])
function fetchCurrentUserLikes() {
  DiscussApi
    .getCurrentUserLikes()
    .then(({ data = [] }) => {
      likes = data
    })
    .catch(() => likes = [])
}
fetchCurrentUserLikes()

const getAuthLike = (remarkId?: number) => !!remarkId && likes?.includes(remarkId)

function likeCallback({ id, type }: { id?: number; type: 'like' | 'dislike' }) {
  if (!id) return
  fetchCurrentUserLikes()
  const item = list.find(i => i.id === id)
  if (item?.likes !== undefined) {
    item.likes = item.likes + [1, -1][type === 'like' ? 0 : 1]
  }
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
            <DiscussItem
              :item="item"
              :loading="loading"
              :is-auth-like="getAuthLike(item?.id)"
              @like-callback="likeCallback"
            />
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
