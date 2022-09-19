const GAME_OBJECTS: Game[] = []

export class Game {
  timeDelta: number
  hasCalledStart: boolean

  constructor() {
    GAME_OBJECTS.push(this)
    this.timeDelta = 0
    this.hasCalledStart = false
  }

  /**
   * 只执行一次
   */
  start() {

  }

  /**
   * 除了第一次不执行，每一帧执行一次
   */
  update() {

  }

  /**
   * 删除之前执行
   */
  beforeDestory() {

  }

  /**
   * 删除
   */
  destory() {
    this.beforeDestory()

    for (let i = 0; i < GAME_OBJECTS.length; i++) {
      const game = GAME_OBJECTS[i]
      if (game === this) {
        GAME_OBJECTS.splice(i)
        break
      }
    }
  }
}

// 上一次执行的时间
let lastTimestamp = 0

const step = (timestamp: number) => {
  for (const game of GAME_OBJECTS) {
    if (!game.hasCalledStart) {
      game.hasCalledStart = true
      game.start()
    }
    else {
      game.timeDelta = timestamp - lastTimestamp
      game.update()
    }
  }

  lastTimestamp = timestamp
  requestAnimationFrame(step)
}

// 浏览器函数 > 在每一帧之前执行
requestAnimationFrame(step)
