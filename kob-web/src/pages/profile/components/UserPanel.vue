<script setup lang="ts">
import type { UploadFileInfo } from 'naive-ui'

const userStore = useUserStore()
function onChange({
  file,
}: {
  file: UploadFileInfo
  fileList: UploadFileInfo[]
}) {
  getBase64(file.file!).then(async (imageAsDateURL) => {
    UserApi
      .updateLoginUserInfo({ avatar: imageAsDateURL as string })
      .then(({ code }) => {
        if (code === 0) {
          $message.success('上传成功')
          userStore.updateUser()
        } else {
          $message.error('上传失败')
        }
      })
  })
}

function getBase64(file: File) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    let imageAsDateURL = ''
    reader.readAsDataURL(file)
    reader.onload = data => imageAsDateURL = data.target?.result as string
    reader.onerror = err => reject(err)
    reader.onloadend = () => resolve(imageAsDateURL)
  })
}

function beforeUpload({
  file,
}: {
  file: UploadFileInfo
  fileList: UploadFileInfo[]
}) {
  if (!file.file?.type.startsWith('image')) {
    $message.error('只能上传图片文件，请重新上传')
    return false
  }
  return true
}
</script>

<template>
  <div ha>
    <n-card hoverable>
      <div flex="~ col" w-full flex-center>
        <n-avatar
          :style="{
            width: '100%',
            maxWidth: '250px',
            height: 'auto',
            cursor: 'pointer',
          }"
          lt-md="max-w-150px"
          size="large"
          :src="userStore.user?.avatar"
          @click="$router.push('/profile')"
        />
        <n-divider />
        <div mb-5px>
          <n-upload
            :show-file-list="false"
            @before-upload="beforeUpload"
            @change="onChange"
          >
            <n-button>
              更换头像
            </n-button>
          </n-upload>
        </div>
      </div>
    </n-card>
  </div>
</template>
