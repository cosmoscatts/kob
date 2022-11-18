import type { DataTableColumns } from 'naive-ui'
import { NButton, NIcon } from 'naive-ui'
import { TrashBinOutline } from '@vicons/ionicons5'
import { EditOutlined } from '@vicons/antd'
import type { Bot } from '~/types'

/**
 * 创建表格列
 */
export function createColumns({
  createRowNumber,
  onUpdateBot,
  onRemoveBot,
}: {
  createRowNumber?: (rowIndex: number) => number
  onUpdateBot?: (bot: Bot) => void | Promise<void>
  onRemoveBot?: (bot: Bot) => void | Promise<void>
}): DataTableColumns<Bot> {
  return [
    {
      key: 'id',
      title: '序号',
      align: 'center',
      render(_row, rowIndex) {
        return createRowNumber?.(rowIndex)
      },
    },
    {
      title: '标题',
      key: 'title',
      align: 'center',
    },
    {
      title: '描述',
      key: 'description',
      align: 'center',
    },
    {
      title: '创建时间',
      key: 'description',
      align: 'center',
      render({ createTime }) {
        return formatDate({ date: createTime })
      },
    },
    {
      title: '操作',
      key: 'actions',
      align: 'center',
      render(row) {
        return h(
          'div',
          {},
          [
            h(
              NButton,
              {
                size: 'small',
                type: 'warning',
                textColor: 'white',
                style: { marginRight: '10px' },
                onClick: () => onUpdateBot?.(row),
              },
              {
                icon: () => h(
                  NIcon,
                  {
                    component: EditOutlined,
                  },
                ),
              },
            ),
            h(
              NButton,
              {
                size: 'small',
                type: 'error',
                textColor: 'white',
                onClick: () => onRemoveBot?.(row),
              },
              {
                icon: () => h(
                  NIcon,
                  {
                    component: TrashBinOutline,
                  },
                ),
              },
            ),
          ],
        )
      },
    },
  ]
}

/**
 * 统一处理保存 `bot` 功能
 */
export function handleSaveBot({
  type = 'add',
  data = {},
}: {
  type?: 'add' | 'edit'
  data?: Bot
}) {
  const { message } = useGlobalNaiveApi()

  const actionMap = {
    add: async () => {
      const { code, msg } = await BotApi.addBot(data)
      if (code !== 0)
        message.error(msg ?? '添加失败')

      else
        message.success('添加成功')

      return code === 0
    },
    edit: async () => {
      const { code, msg } = await BotApi.updateBot(data)
      if (code !== 0)
        message.error(msg ?? '编辑失败')

      else
        message.success('编辑成功')

      return code === 0
    },
  }

  return actionMap[type]()
}

/**
 * 生成表单校验规则
 */
export function createRules() {
  return {
    title: [
      {
        required: true,
        message: '请输入标题',
      },
    ],
    content: [
      {
        required: true,
        message: '请输入代码',
      },
    ],
  }
}
