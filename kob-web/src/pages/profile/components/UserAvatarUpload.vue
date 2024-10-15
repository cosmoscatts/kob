<script setup lang="ts">
import type { UploadFileInfo } from 'naive-ui';
import { appOssPrefix } from '~/config';

const userStore = useUserStore();
const router = useRouter();

const isUploading = ref(false);

async function onChange({ file }: { file: UploadFileInfo }) {
  if (!file.file)
    return;

  isUploading.value = true;
  try {
    const uploadResult = await UploadApi.uploadFile(file.file);
    if (uploadResult.data.code !== 0 || !uploadResult.data.data) {
      $message.error('上传失败');
      return;
    }
    const imageUrl = appOssPrefix + uploadResult.data.data;
    const upadteResult = await UserApi.updateLoginUserInfo({ avatar: imageUrl });
    if (upadteResult.data.code === 0) {
      $message.success('上传成功');
      await userStore.fetchAndUpdateUser();
    } else {
      $message.error(upadteResult.data.msg || '更新用户头像失败');
    }
  } catch (error) {
    console.error(error);
    $message.error('上传失败');
  } finally {
    isUploading.value = false;
  }
}

function beforeUpload({ file }: { file: UploadFileInfo }): boolean {
  if (!file.file?.type.startsWith('image')) {
    $message.error('只能上传图片文件，请重新上传');
    return false;
  }
  return true;
}
</script>

<template>
  <div>
    <n-card hoverable>
      <div flex flex-col w-full items-center>
        <n-avatar
          :style="{
            width: '100%',
            maxWidth: '250px',
            height: 'auto',
            cursor: 'pointer',
          }"
          lt-md:max-w-150px
          size="large"
          :src="userStore.user?.avatar"
          @click="router.push('/profile')"
        />
        <n-divider />
        <div class="mb-5px">
          <n-upload
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-change="onChange"
          >
            <n-button :loading="isUploading">
              {{ isUploading ? '上传中...' : '更换头像' }}
            </n-button>
          </n-upload>
        </div>
      </div>
    </n-card>
  </div>
</template>
