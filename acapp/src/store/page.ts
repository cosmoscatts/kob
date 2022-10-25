export const usePageStore = defineStore(
  'pageStore',
  () => {
    const currentPage = ref(0)

    const changePage = (page: number) => {
      if (page < 0 || page > 4)
        return
      currentPage.value = page
    }

    return {
      currentPage,
      changePage,
    }
  },
)
