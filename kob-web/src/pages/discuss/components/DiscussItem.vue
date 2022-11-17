<script setup lang="ts">
import { Heart, HeartOutline } from '@vicons/ionicons5'
import type { Discuss } from '~/types'
import defaultAvatar from '~/assets/default-avatar.png'

const {
  item,
  isAuthLike = false,
} = defineProps<{
  item?: Discuss
  isAuthLike?: boolean
}>()

const emits = defineEmits(['likeCallback'])

const { message } = useGlobalNaiveApi()

const like = useThrottleFn(async (remarkId?: number) => {
  const { code, msg } = await DiscussApi.likeDiscuss({ remarkId })
  if (code !== 0) {
    message.error(msg || '支持失败，请重试')
    return
  }
  message.success('已支持该意见')
  emits('likeCallback', { id: remarkId, type: 'like' })
}, 500)

const dislike = useThrottleFn(async (remarkId?: number) => {
  const { code, msg } = await DiscussApi.dislikeDiscuss({ remarkId })
  if (code !== 0) {
    message.error(msg || '取消支持失败，请重试')
    return
  }
  message.success('已取消支持该意见')
  emits('likeCallback', { id: remarkId, type: 'dislike' })
}, 500)
</script>

<template>
  <div>
    <n-thing content-indented>
      <template #avatar>
        <n-avatar :src="item?.user?.avatar ?? defaultAvatar" />
      </template>
      <template #header>
        {{ item?.user?.name ?? '某不愿透露姓名的小可爱' }}
      </template>
      {{ item?.remark ?? '' }}
      <template #action>
        <div flex-y-center justify-between>
          <div>发表于 {{ formatDate({ date: item?.createTime }) }}</div>
          <div flex-y-center>
            <n-icon v-if="isAuthLike" size="18" color="red" @click="dislike(item?.id)">
              <Heart />
            </n-icon>
            <n-icon v-else size="18" @click="like(item?.id)">
              <HeartOutline />
            </n-icon>
            <span ml2>{{ item?.likes ?? 0 }}</span>
          </div>
        </div>
      </template>
    </n-thing>
  </div>
</template>
