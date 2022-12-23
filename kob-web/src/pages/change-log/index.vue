<script setup lang="ts">
import { BugOutline, BulbOutline, DiamondOutline } from '@vicons/ionicons5'
import { APP_CHANGE_LOG, APP_META } from '~/config'

const getTimelineType = (type: 'add' | 'update' | 'fix') => {
  return {
    add: 'info',
    update: 'warning',
    fix: 'error',
  }[type]
}

const getTimelineIcon = (type: 'add' | 'update' | 'fix') => {
  return {
    add: BulbOutline,
    update: DiamondOutline,
    fix: BugOutline,
  }[type]
}
</script>

<template>
  <div w-70vw mx-a flex="~ col">
    <n-card>
      <div text="1.6rem center" font-800>
        更新日志
      </div>
      <div mt20px>
        <n-list bordered clickable>
          <template #header>
            <n-alert title="King Of Bots" type="info">
              该项目是一个学习项目，可能还会完善，如果实在没事干的话(ヘ･_･)ヘ┳━┳。
            </n-alert>
          </template>
          <template #footer>
            <n-alert :show-icon="false">
              <div text-center>
                上一次更新： {{ APP_META.lastUpdate }} @Cosmoscatts
              </div>
            </n-alert>
          </template>
          <n-list-item v-for="item in APP_CHANGE_LOG" :key="item.date">
            <template #prefix>
              <n-tag> {{ item.date }} </n-tag>
            </template>
            <n-thing :title="item.title" :description="item.description">
              <n-timeline v-if="item.changes?.length" :icon-size="20">
                <n-timeline-item
                  v-for="{ id, type, title, content } in item.changes"
                  :key="id"
                  :type="getTimelineType(type)"
                  :title="title"
                  :content="content"
                >
                  <template #icon>
                    <n-icon>
                      <Component :is="getTimelineIcon(type)" />
                    </n-icon>
                  </template>
                </n-timeline-item>
              </n-timeline>
            </n-thing>
          </n-list-item>
        </n-list>
      </div>
    </n-card>
  </div>
</template>
