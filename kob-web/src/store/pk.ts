import type { User } from '~/types'

type Opponent = Pick<User, 'username' | 'avatar'>

export const usePkStore = defineStore(
  'pkStore',
  () => {
    // `match` - 匹配界面， `play` - 对战界面
    const status = ref<'match' | 'play'>('match')
    const socket = ref()
    const opponent = ref<Opponent>()
    const gameMap = ref<number[][]>()

    return {
      status,
      socket,
      opponent,
      gameMap,
    }
  },
  {
    persist: {
      enabled: true,
    },
  },
)
