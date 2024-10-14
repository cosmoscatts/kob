<script setup lang="ts">
import { Heart, HeartOutline } from '@vicons/ionicons5';
import defaultAvatar from '~/assets/default-avatar.png';
import type { Discuss } from '~/types';

interface Props {
  item?: Discuss
  loading?: boolean
  isAuthLike?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  isAuthLike: false,
});

const emits = defineEmits(['likeCallback']);
const { width } = useWindowSize();

const handleLikeAction = async (action: 'like' | 'dislike') => {
  const apiMethod = action === 'like' ? DiscussApi.likeDiscuss : DiscussApi.dislikeDiscuss;
  const successMessage = action === 'like' ? '已支持该意见' : '已取消支持该意见';
  const errorMessage = `${action === 'like' ? '支持' : '取消支持'}失败，请重试`;

  try {
    const result = await apiMethod({ remarkId: props.item?.id });
    const { code, msg } = result.data;
    if (code !== 0) {
      $message.error(msg || errorMessage);
      return;
    }
    $message.success(successMessage);
    emits('likeCallback', { id: props.item?.id, type: action });
  } catch (e) {
    console.error(e);
    $message.error(errorMessage);
  }
};

const like = useThrottleFn(() => handleLikeAction('like'), 500);
const dislike = useThrottleFn(() => handleLikeAction('dislike'), 500);
</script>

<template>
  <div>
    <n-space v-if="loading">
      <n-skeleton height="40px" circle />
      <n-space vertical>
        <n-skeleton height="30px" width="150px" />
        <n-skeleton height="60px" :width="width > 768 ? '58vw' : '52vw'" :sharp="false" />
        <n-space justify="space-between">
          <n-skeleton height="30px" width="200px" />
          <n-skeleton height="30px" width="50px" round />
        </n-space>
      </n-space>
    </n-space>
    <n-thing v-else content-indented>
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
            <n-icon v-if="isAuthLike" size="18" color="red" @click="dislike">
              <Heart />
            </n-icon>
            <n-icon v-else size="18" @click="like">
              <HeartOutline />
            </n-icon>
            <span ml2>{{ item?.likes ?? 0 }}</span>
          </div>
        </div>
      </template>
    </n-thing>
  </div>
</template>
