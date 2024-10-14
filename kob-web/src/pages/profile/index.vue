<script setup lang="ts">
import SecuritySettings from './components/SecuritySettings.vue';
import UserAvatarUpload from './components/UserAvatarUpload.vue';
import UserProfileForm from './components/UserProfileForm.vue';
</script>

<template>
  <div
    grid="~ cols-4"
    gap-x-5
    lt-md="grid-cols-1 gap-y-5"
    w-70vw mx-a ha
  >
    <div col-span-1 ha>
      <UserAvatarUpload />
    </div>
    <div col-span-3 lt-md="col-span-1">
      <div flex="~ col" w-full gap-y-5>
        <UserProfileForm />
        <SecuritySettings />
      </div>
    </div>
  </div>
</template>
