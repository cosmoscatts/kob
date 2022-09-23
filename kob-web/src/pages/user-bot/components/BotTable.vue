<script setup lang="ts">
import { NButton, NTag } from 'naive-ui'
import type { DataTableColumns } from 'naive-ui'

import { breakpointsTailwind } from '@vueuse/core'

interface RowData {
  key: number
  name: string
  age: number
  address: string
  tags: string[]
}

const createColumns = ({
  sendMail,
}: {
  sendMail: (rowData: RowData) => void
}): DataTableColumns<RowData> => {
  return [
    {
      title: 'Name',
      key: 'name',
    },
    {
      title: 'Age',
      key: 'age',
    },
    {
      title: 'Address',
      key: 'address',
    },
    {
      title: 'Tags',
      key: 'tags',
      render(row) {
        const tags = row.tags.map((tagKey) => {
          return h(
            NTag,
            {
              style: {
                marginRight: '6px',
              },
              type: 'info',
              bordered: false,
            },
            {
              default: () => tagKey,
            },
          )
        })
        return tags
      },
    },
    {
      title: 'Action',
      key: 'actions',
      render(row) {
        return h(
          NButton,
          {
            size: 'small',
            onClick: () => sendMail(row),
          },
          { default: () => 'Send Email' },
        )
      },
    },
  ]
}

const createData = (): RowData[] => [
  {
    key: 0,
    name: 'John Brown',
    age: 32,
    address: 'New York No. 1 Lake Park',
    tags: ['nice', 'developer'],
  },
  {
    key: 1,
    name: 'Jim Green',
    age: 42,
    address: 'London No. 1 Lake Park',
    tags: ['wow'],
  },
  {
    key: 2,
    name: 'Joe Black',
    age: 32,
    address: 'Sidney No. 1 Lake Park',
    tags: ['cool', 'teacher'],
  },
]

const data = createData()
const columns = createColumns({
  sendMail(rowData) {
    // message.info(`send mail to ${rowData.name}`)
  },
})
const pagination = {
  pageSize: 10,
}
// 是否为移动端（包含 `PC` 端宽度过小的情况）
const breakpoints = useBreakpoints(breakpointsTailwind)
const isMobile = breakpoints.smaller('sm')
</script>

<template>
  <div h-full border="red 1">
    <n-card title="我的Bot" hoverable>
      <template #header-extra>
        <NButton type="primary" text-color="white">
          添加Bot
        </NButton>
      </template>
      <n-data-table
        v-if="!isMobile"
        size="small"
        :columns="columns"
        :data="data"
        :pagination="pagination"
      />
    </n-card>
  </div>
</template>
