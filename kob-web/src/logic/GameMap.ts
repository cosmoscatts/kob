import type { Pausable } from '@vueuse/core'
import { Game } from './Game'
import { GameSnake } from './GameSnake'
import { GameWall } from './GameWall'

const COLOR_EVEN = '#C3944E'
const COLOR_ODD = '#A57332'

export class GameMap extends Game {
  ctx: CanvasRenderingContext2D
  parent: HTMLElement
  /** `1` 单位长度 */
  L: number
  /** 地体行数 */
  rows: number
  /** 地图列数 */
  cols: number
  /** 障碍物 */
  gameWalls: GameWall[]
  /** 蛇集合 */
  snakes: GameSnake[]
  /** 录像执行方法 */
  recordFn: Pausable | null

  constructor(ctx: CanvasRenderingContext2D, parent: HTMLElement) {
    super()

    this.ctx = ctx
    this.parent = parent
    this.L = 0

    this.rows = 13
    this.cols = 14

    this.gameWalls = []

    this.snakes = [
      new GameSnake({ id: 0, color: '#206CCF', r: this.rows - 2, c: 1 }, this),
      new GameSnake({ id: 1, color: '#CB272D', r: 1, c: this.cols - 2 }, this),
    ]
    this.recordFn = null
  }

  /**
   * 判断地图连通性
   * sx, sy 起点横纵坐标
   * tx, ty 终点横纵坐标
   */
  checkConnectivity(g: boolean[][], sx: number, sy: number, tx: number, ty: number) {
    if (sx === tx && sy === ty)
      return true
    g[sx][sy] = true

    const dx = [-1, 0, 1, 0]; const dy = [0, 1, 0, -1]
    for (let i = 0; i < 4; i++) {
      const x = sx + dx[i]; const y = sy + dy[i]
      if (!g[x][y] && this.checkConnectivity(g, x, y, tx, ty))
        return true
    }
    return false
  }

  createGameWalls() {
    const { rows, cols } = this
    const { gameMap } = usePkStore()
    const { isRecord, gameMap: gameMap2 } = useRecordStore()

    const g: number[][] = isRecord ? gameMap2! : gameMap!

    for (let r = 0; r < rows; r++) {
      for (let c = 0; c < cols; c++) {
        if (g[r][c])
          this.gameWalls.push(new GameWall(r, c, this))
      }
    }
  }

  addListeningEvents() {
    const { socket } = usePkStore()
    const { isRecord, aSteps, bSteps, loser, updateRecordFinished } = useRecordStore()

    if (isRecord) {
      let k = 0
      const [snake0, snake1] = this.snakes
      let _recordFn: Pausable | null = null

      _recordFn = this.recordFn = useIntervalFn(() => {
        if (k >= aSteps.length - 1) {
          if (['all', 'A'].includes(loser))
            snake0.status = 'die'

          if (['all', 'B'].includes(loser))
            snake1.status = 'die'

          _recordFn?.pause() // 这里注意 this 的指向问题
          updateRecordFinished(true)
        }
        else {
          snake0.setDirection(parseInt(aSteps[k]))
          snake1.setDirection(parseInt(bSteps[k]))
        }
        k++
      }, 300)

      return
    }

    const canvas = this.ctx.canvas
    canvas.focus()

    canvas.addEventListener('keydown', (e) => {
      let d = -1
      if (e.key === 'w')
        d = 0
      else if (e.key === 'd')
        d = 1
      else if (e.key === 's')
        d = 2
      else if (e.key === 'a')
        d = 3

      if (d >= 0) {
        socket?.send(JSON.stringify({
          event: 'move',
          direction: d,
        }))
      }
    })
  }

  start() {
    this.createGameWalls()

    this.addListeningEvents()
  }

  updateSize() {
    const { parent, cols, rows } = this
    const { clientWidth, clientHeight } = parent
    this.L = parseInt(String(Math.min(clientWidth / cols, clientHeight / rows)))
    // 计算 `canvas` 宽高
    this.ctx.canvas.width = this.L * cols
    this.ctx.canvas.height = this.L * rows
  }

  /**
   * 判断两条蛇是否都准备好下一回合
   */
  checkSnakeReady() {
    for (const { direction, status } of this.snakes) {
      if (status !== 'idle')
        return false
      if (direction === -1)
        return false
    }
    return true
  }

  /**
   * 让两条蛇进入下一回合
   */
  nextStep() {
    for (const snake of this.snakes)
      snake.updateNextStep()
  }

  /**
   * 检测目标位置是否合法，未撞到两条蛇的身体或者墙
   */
  // checkValid(cell: GameSnakeCell) {
  //   for (const wall of this.gameWalls) {
  //     if (wall.r === cell.r && wall.c === cell.c)
  //       return false
  //   }

  //   for (const snake of this.snakes) {
  //     let k = snake.snakeCells.length
  //     // 当蛇尾会前进时，不需要判断是否会撞到蛇尾，直接忽略
  //     if (!snake.checkTailIncreasing())
  //       k -= 1

  //     for (let i = 0; i < k; i++) {
  //       if (snake.snakeCells[i].r === cell.r && snake.snakeCells[i].c === cell.c)
  //         return false
  //     }
  //   }

  //   return true
  // }

  update() {
    this.updateSize()
    if (this.checkSnakeReady())
      this.nextStep()
    this.render()
  }

  render() {
    const { ctx, L, cols, rows } = this

    for (let r = 0; r < rows; r++) {
      for (let c = 0; c < cols; c++) {
        ctx.fillStyle = ((r + c) & 1) === 0
          ? COLOR_EVEN
          : COLOR_ODD
        ctx.fillRect(c * L, r * L, L, L)
      }
    }
  }

  beforeDestory() {
    if (this.gameWalls.length > 0)
      this.gameWalls.forEach(wall => wall.destory())
    if (this.snakes.length)
      this.snakes.forEach(snake => snake.destory())
  }
}
