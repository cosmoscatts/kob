<script setup lang="ts">
import type { FormInst, FormValidationError } from 'naive-ui'
import {
  TrashBinOutline as TrashBinOutlineIcon,
} from '@vicons/ionicons5'
import type { User } from '~/types'

const userStore = useUserStore()
const { loading, startLoading, endLoading } = useLoading()

const refForm = ref<FormInst | null>(null)
type FormModel = Pick<User, 'id' | 'name' | 'email' >
const getBaseFormModel: () => FormModel = () => ({
  id: undefined,
  name: userStore.user?.name ?? '',
  email: userStore.user?.email ?? '',
})
const formModel = reactive<FormModel>({
  ...getBaseFormModel(),
})

function onSubmit(e: MouseEvent) {
  e.preventDefault()
  refForm.value?.validate((errors?: FormValidationError[]) => {
    if (errors) return
    startLoading()
    UserApi
      .updateLoginUserInfo(useClone(formModel))
      .then(({ code, msg }) => {
        if (code === 0) {
          $message.success('修改成功')
          userStore.updateUser()
        }
        else { $message.error(msg || '修改失败') }
      })
      .finally(() => useTimeoutFn(endLoading, 1000))
  })
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
        ref="refForm"
        :model="formModel"
        :rules="{
          name: [
            {
              required: true,
              message: '请输入用户名',
            },
          ],
        }"
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
