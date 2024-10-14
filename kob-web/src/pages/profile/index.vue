<script setup lang="ts">
import UserAvatarUpload from './components/UserAvatarUpload.vue';
import UserProfileForm from './components/UserProfileForm.vue';
import UserSecuritySettings from './components/UserSecuritySettings.vue';
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
        <UserSecuritySettings />
      </div>
    </div>
  </div>
</template>
