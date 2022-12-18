const GAME_OBJECTS: Game[] = []

// ----- 游戏对象基类 -----

export class Game {
  timeDelta = 0 // 距离上一帧的时间间隔（ms）
  hasCalledStart = false

  constructor() {
    GAME_OBJECTS.push(this)
  }

  start() {}

  update() {}

  beforeDestory() {}

  destory() {
    this.beforeDestory()
    const idx = GAME_OBJECTS.findIndex(i => i === this)
    if (~idx) {
      GAME_OBJECTS.splice(idx) // 删除之后的所有对象
    }
  }
}

let lastTimestamp = 0 // 上一次执行的时间
const step = (timestamp: number) => {
  GAME_OBJECTS.forEach((game) => {
    if (!game.hasCalledStart) {
      game.hasCalledStart = true
      game.start()
    } else {
      game.timeDelta = timestamp - lastTimestamp
      game.update()
    }
  })
  lastTimestamp = timestamp
  requestAnimationFrame(step) // 递归调用
}
requestAnimationFrame(step)
