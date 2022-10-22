<script setup lang="ts">
import {
  TrashBinOutline as TrashBinOutlineIcon,
} from '@vicons/ionicons5'
import type { User } from '~/types'

const { message } = useGlobalNaiveApi()
const { loading, startLoading, endLoading } = useLoading()

const userStore = useUserStore()
const { updateUser } = userStore
const { user } = storeToRefs(userStore)

type FormModel = Pick<User, 'id' | 'name' | 'email' >
function getBaseFormModel(): FormModel {
  const { value: _user } = user
  return {
    id: undefined,
    name: _user?.name ?? '',
    email: _user?.email ?? '',
  }
}

const formModel = reactive<FormModel>({
  ...getBaseFormModel(),
})

async function onSubmit() {
  startLoading()
  const { code } = await UserApi.updateLoginUserInfo(JSON.parse(JSON.stringify(formModel)))
  if (code === 0) {
    message.success('修改成功')
    updateUser()
  }

  else { message.error('修改失败') }

  useTimeoutFn(endLoading, 1000)
}
</script>

<template>
  <div w-full>
    <n-card
      title="个人信息"
      hoverable
      :segmented="{
        content: 'soft',
      }"
    >
      <n-form
        :model="formModel"
        label-placement="left"
        label-width="auto"
        :show-require-mark="false"
        :style="{
          width: '70%',
          margin: '0 auto',
        }"
        lt-md="!w-full"
        @submit.prevent="onSubmit"
      >
        <n-form-item label="用户名" path="name">
          <n-input v-model:value="formModel.name" placeholder="请输入用户名" clearable>
            <template #clear-icon>
              <n-icon :component="TrashBinOutlineIcon" />
            </template>
          </n-input>
        </n-form-item>
        <n-form-item label="邮箱" path="email">
          <n-input v-model:value="formModel.email" placeholder="请输入邮箱" clearable>
            <template #clear-icon>
              <n-icon :component="TrashBinOutlineIcon" />
            </template>
          </n-input>
        </n-form-item>
        <div flex-center>
          <n-button type="primary" :loading="loading" text-color="white" attr-type="submit">
            更新信息
          </n-button>
        </div>
      </n-form>
    </n-card>
  </div>
</template>
