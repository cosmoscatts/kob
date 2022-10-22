import { Game } from './Game'
import { GameSnakeCell } from './GameSnakeCell'
import type { GameMap } from './GameMap'

export interface SnakeInfo {
  id: number
  color: string
  r: number
  c: number
}

// `idle` 表示静止，`move` 表示正在移动，`die` 表示死亡
export type SnakeStatus = 'idle' | 'move' | 'die'

export class GameSnake extends Game {
  id: number
  color: string
  gameMap: GameMap

  snakeCells: GameSnakeCell[]
  /** 下一步目标单元格 */
  nextCell: GameSnakeCell | null

  speed: number

  direction: number
  status: SnakeStatus

  dr: number[]
  dc: number[]

  step: number
  eps: number

  eyeDirection: number
  eyeDx: number[][]
  eyeDy: number[][]

  constructor({ id, color, r, c }: SnakeInfo, gameMap: GameMap) {
    super()

    this.id = id
    this.color = color
    this.gameMap = gameMap

    // 存放蛇的身体，cells[0] 为头部
    this.snakeCells = [new GameSnakeCell(r, c)]
    this.nextCell = null

    // 每秒走五个格子
    this.speed = 5

    // 下一步的指令，移动的方向，
    // -1 为没有指令，0 | 1 | 2 | 3 - 上 | 右 | 下 | 左
    this.direction = -1
    this.status = 'idle'

    // 4 个方向行的偏移量
    this.dr = [-1, 0, 1, 0]
    // 4 个方向列的偏移量
    this.dc = [0, 1, 0, -1]

    // 当前回合数
    this.step = 0
    // 允许的误差
    this.eps = 1e-2

    // 定义蛇头方向
    // 左下角的蛇初始朝上，右上角的蛇朝下
    this.eyeDirection = this.id === 0
      ? 0
      : 2
    // 蛇眼睛不同方向的 `x` 的偏移量
    this.eyeDx = [
      [-1, 1],
      [1, 1],
      [1, -1],
      [-1, -1],
    ]
    // 蛇眼睛不同方向的 `y` 的偏移量
    this.eyeDy = [
      [-1, -1],
      [-1, 1],
      [1, 1],
      [1, -1],
    ]
  }

  start() {

  }

  /**
   * 设置操作方向
   */
  setDirection(d: number) {
    this.direction = d
  }

  /**
   * 检查当前回合蛇的长度是否增加
   *  - 前十回合，每回合增加 `1`
   *  - 后面回合，每 `3` 回合增加 `1`
   */
  checkTailIncreasing() {
    const { step } = this

    if (step <= 10)
      return true
    else if (step % 3 === 1)
      return true
    else return false
  }

  /**
   * 更新蛇的下一步状态
   */
  updateNextStep() {
    const { direction: d, snakeCells, dr, dc } = this

    // 蛇头
    const head = snakeCells[0]
    this.nextCell = new GameSnakeCell(head.r + dr[d], head.c + dc[d])
    // 更新蛇头方向
    this.eyeDirection = d
    // 清空操作
    this.direction = -1
    this.status = 'move'
    this.step++

    // 将单元格往后移一位
    const cellLength = snakeCells.length
    for (let i = cellLength; i > 0; i--)
      this.snakeCells[i] = JSON.parse(JSON.stringify(this.snakeCells[i - 1]))

    // 下一步操作非法（撞墙或者撞蛇），蛇死亡
    // if (!gameMap.checkValid(this.nextCell))
    //   this.status = 'die'
  }

  move() {
    const { speed, timeDelta, nextCell, snakeCells, eps } = this

    const head = snakeCells[0]
    const dx = nextCell!.x - head.x
    const dy = nextCell!.y - head.y
    const distance = Math.sqrt(dx * dx + dy * dy)

    if (distance < eps) {
      // 添加一个新蛇头
      this.snakeCells[0] = nextCell!
      this.nextCell = null
      // 走完了，停下来
      this.status = 'idle'
      // 蛇尾不增加，需要删除旧蛇尾
      if (!this.checkTailIncreasing())
        this.snakeCells.pop()
    }
    else {
      // 每两帧之间走的移动距离
      const moveDistance = speed * timeDelta / 1000
      this.snakeCells[0].x += moveDistance * dx / distance
      this.snakeCells[0].y += moveDistance * dy / distance

      // 蛇尾不增加，需要移动蛇尾到上一节点
      if (!this.checkTailIncreasing()) {
        const cellLength = snakeCells.length
        const tail = snakeCells[cellLength - 1]
        const tailTarget = snakeCells[cellLength - 2]
        const tailDx = tailTarget.x - tail.x
        const tailDy = tailTarget.y - tail.y
        tail.x += moveDistance * tailDx / distance
        tail.y += moveDistance * tailDy / distance
      }
    }
  }

  update() {
    if (this.status === 'move')
      this.move()
    this.render()
  }

  render() {
    const { gameMap, color, snakeCells, eps, status, eyeDirection, eyeDx, eyeDy } = this
    const { ctx, L } = gameMap

    // 蛇死亡 > 白色身体
    ctx.fillStyle = status === 'die'
      ? 'white'
      : color

    for (const { x, y } of snakeCells) {
      ctx.beginPath()
      ctx.arc(x * L, y * L, L / 2 * 0.8, 0, Math.PI * 2)
      ctx.fill()
    }

    // 用长方形补全蛇身体
    for (let i = 1; i < snakeCells.length; i++) {
      const a = snakeCells[i - 1]; const b = snakeCells[i]
      if (Math.abs(a.x - b.x) < eps && Math.abs(a.y - b.y) < eps)
        continue
        // 垂直方向
      if (Math.abs(a.x - b.x) < eps)
        ctx.fillRect((a.x - 0.5 + 0.1) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L)

      // 水平方向
      else
        ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.5 + 0.1) * L, Math.abs(a.x - b.x) * L, L * 0.8)
    }

    // 画蛇眼睛
    ctx.fillStyle = 'black'
    const head = snakeCells[0]
    for (let i = 0; i < 2; i++) {
      const eyeX = (head.x + eyeDx[eyeDirection][i] * 0.15) * L
      const eyeY = (head.y + eyeDy[eyeDirection][i] * 0.15) * L

      // 先画外围椭圆
      ctx.fillStyle = 'white'
      const width = [1, 3].includes(eyeDirection) ? 0.4 * L : 0.3 * L
      const height = [1, 3].includes(eyeDirection) ? 0.3 * L : 0.4 * L
      this.drawEllipse(ctx, eyeX - width / 2, eyeY - height / 2, width, height)

      ctx.beginPath()
      ctx.fillStyle = 'black'
      ctx.arc(eyeX, eyeY, L * 0.05, 0, Math.PI * 2)
      ctx.fill()
    }
  }

  /**
   * 画椭圆
   */
  drawEllipse(ctx: CanvasRenderingContext2D, x: number, y: number, w: number, h: number) {
    const kappa = 0.5522848
    const ox = (w / 2) * kappa // control point offset horizontal
    const oy = (h / 2) * kappa // control point offset vertical
    const xe = x + w // x-end
    const ye = y + h // y-end
    const xm = x + w / 2 // x-middle
    const ym = y + h / 2 // y-middle

    ctx.beginPath()
    ctx.moveTo(x, ym)
    ctx.bezierCurveTo(x, ym - oy, xm - ox, y, xm, y)
    ctx.bezierCurveTo(xm + ox, y, xe, ym - oy, xe, ym)
    ctx.bezierCurveTo(xe, ym + oy, xm + ox, ye, xm, ye)
    ctx.bezierCurveTo(xm - ox, ye, x, ym + oy, x, ym)
    ctx.closePath()
    ctx.fill()
  }
}
