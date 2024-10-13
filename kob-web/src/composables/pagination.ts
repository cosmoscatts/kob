interface PaginationOptions {
  page?: number
  pageSize?: number
  itemCount?: number
  showSizePicker?: boolean
  pageSizes?: number[]
  onChangeCallback?: () => void
  onUpdatePageSizeCallback?: () => void
}

interface Pagination {
  page: number
  pageSize: number
  itemCount: number
  showSizePicker: boolean
  pageSizes: number[]
  totalPages: number
  onChange: (page: number) => void
  onUpdatePageSize: (pageSize: number) => void
  createRowNumber: (rowIndex: number) => number
}

export function usePagination(options: PaginationOptions = {}): Pagination {
  const {
    page: initialPage = 1,
    pageSize: initialPageSize = 10,
    itemCount: initialItemCount = 0,
    showSizePicker: initialShowSizePicker = true,
    pageSizes: initialPageSizes = [10, 20, 50, 100],
    onChangeCallback,
    onUpdatePageSizeCallback,
  } = options;

  const state = reactive({
    page: initialPage,
    pageSize: initialPageSize,
    itemCount: initialItemCount,
    showSizePicker: initialShowSizePicker,
    pageSizes: initialPageSizes,
    get totalPages() {
      return Math.ceil(this.itemCount / this.pageSize);
    },
    onChange(newPage: number) {
      this.page = newPage;
      onChangeCallback?.();
    },
    onUpdatePageSize(newPageSize: number) {
      this.pageSize = newPageSize;
      this.page = 1;
      onUpdatePageSizeCallback?.();
    },
    createRowNumber(rowIndex: number) {
      return (this.page - 1) * this.pageSize + rowIndex + 1;
    },
  });

  return state as Pagination;
}
