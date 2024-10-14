<script setup lang="ts">
import type { UploadFileInfo } from 'naive-ui';

const userStore = useUserStore();
const router = useRouter();

const isUploading = ref(false);

async function onChange({ file }: { file: UploadFileInfo }) {
  if (!file.file)
    return;

  isUploading.value = true;
  try {
    const imageAsDataURL = await getFileBase64(file.file);
    const result = await UserApi.updateLoginUserInfo({ avatar: imageAsDataURL });
    if (result.data.code === 0) {
      $message.success('上传成功');
      await userStore.fetchAndUpdateUser();
    } else {
      $message.error(result.data.msg || '上传失败');
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
