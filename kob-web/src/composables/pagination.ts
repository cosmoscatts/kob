export function usePagination({
  page = 1,
  pageSize = 10,
  itemCount = 0,
  showSizePicker = true,
  pageSizes = [10, 20, 50, 100],
  onChangeCallback,
  onUpdatePageSizeCallback,
}: {
  page?: number
  pageSize?: number
  itemCount?: number
  showSizePicker?: boolean
  pageSizes?: number[]
  onChangeCallback?: () => void
  onUpdatePageSizeCallback?: () => void
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
