export function usePagination({
  page = 1,
  pageSize = 10,
  itemCount = 0,
  showSizePicker = true,
  pageSizes = [10, 20, 50, 100],
  onChangeCallback,
  onUpdatePageSizeCallback,
}: {
  page?: number // 当前页码
  pageSize?: number // 每页条数
  itemCount?: number // 总条数
  showSizePicker?: boolean // 是否显示每页条数的选择器
  pageSizes?: number[] // 每页条数选择器选项
  onChangeCallback?: () => void // 改变页码的回调方法
  onUpdatePageSizeCallback?: () => void // 每页条数改变的回调方法
}) {
  const pagination = reactive({
    page,
    pageSize,
    itemCount,
    showSizePicker,
    pageSizes,
    onChange: (page: number) => {
      pagination.page = page
      onChangeCallback?.()
    },
    onUpdatePageSize: (pageSize: number) => {
      pagination.pageSize = pageSize
      pagination.page = 1
      onUpdatePageSizeCallback?.()
    },
    createRowNumber: (rowIndex: number) => {
      const { page, pageSize } = pagination
      return (page - 1) * pageSize + rowIndex + 1
    },
  })

  return pagination
}
