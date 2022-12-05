import { SnakeCell } from './snake-cell'
import type { GameMap } from './map'
import { drawEllipse } from './helper'

export interface SnakeInfo {
  id: number
  color: string
  r: number
  c: number
}

// idle 表示静止，move 表示正在移动，die 表示死亡
export type SnakeStatus = 'idle' | 'move' | 'die'

export class Snake {
  private id: number
  private color: string

  private cells: SnakeCell[]
  private nextCell: SnakeCell | null = null // 下一步目标单元格

  private speed = 5 // 每秒走五个格子

  public directions: number[] = []
  // 下一步的指令，移动的方向，
  // -1 为没有指令，0 | 1 | 2 | 3 - 上 | 右 | 下 | 左
  public status: SnakeStatus = 'idle'

  private dr = [-1, 0, 1, 0] // 4 个方向行的偏移量
  private dc = [0, 1, 0, -1] // 4 个方向列的偏移量

  private step = 0 // 当前回合数
  private eps = 1e-2 // 允许的误差
  private eyeDirection: number

  private eyeDx: number[][] = [ // 蛇眼睛不同方向的 x 的偏移量
    [-1, 1],
    [1, 1],
    [1, -1],
    [-1, -1],
  ]

  private eyeDy: number[][] = [ // 蛇眼睛不同方向的 y 的偏移量
    [-1, -1],
    [-1, 1],
    [1, 1],
    [1, -1],
  ]

  constructor({ id, color, r, c }: SnakeInfo, public gameMap: GameMap) {
    this.id = id
    this.color = color
    this.gameMap = gameMap
    // 存放蛇的身体，cells[0] 为头部
    this.cells = [new SnakeCell(r, c)]
    // 定义蛇头方向
    // 左下角的蛇初始朝上，右上角的蛇朝下
    this.eyeDirection = [2, 0][Number(id === 0)]
  }

  /**
   * 设置操作方向
   */
  public setDirection(d: number) {
    this.directions.push(d)
  }

  /**
   * 检查当前回合蛇的长度是否增加
   *  - 前十回合，每回合增加 1
   *  - 后面回合，每 3 回合增加 1
   */
  private checkTailIncreasing() {
    const { step } = this
    return step <= 10 || (step % 3 === 1)
  }

  /**
   * 更新蛇的下一步状态
   */
  public updateNextStep() {
    const { directions, cells, dr, dc } = this

    const d = directions[0]
    const { r, c } = cells[0] // 蛇头 r, c
    const [nr, nc] = [r + dr[d], c + dc[d]] // 下一步的坐标
    this.nextCell = new SnakeCell(nr, nc)

    // 更新蛇头方向
    this.eyeDirection = d
    // 清空操作
    this.directions.shift()
    this.status = 'move'
    this.step++

    // 将单元格往后移一位
    for (let i = cells.length; i > 0; i--)
      this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]))
  }

  private move() {
    const {
      speed,
      gameMap: { timeDelta },
      nextCell,
      cells,
      eps,
    } = this

    const { x: headX, y: headY } = cells[0]
    const [dx, dy] = [nextCell!.x - headX, nextCell!.y - headY]
    const distance = Math.sqrt(dx * dx + dy * dy)

    if (distance < eps) {
      this.cells[0] = nextCell! // 添加一个新蛇头
      this.nextCell = null
      this.status = 'idle' // 走完了，停下来
      if (!this.checkTailIncreasing()) // 蛇尾不增加，需要删除旧蛇尾
        this.cells.pop()
    }
    else {
      const moveDistance = speed * timeDelta / 1000 // 每两帧之间走的移动距离
      this.cells[0].x += moveDistance * dx / distance
      this.cells[0].y += moveDistance * dy / distance

      if (!this.checkTailIncreasing()) { // 蛇尾不增加，需要移动蛇尾到上一节点
        const [{ x: nx, y: ny }, tail] = cells.filter((_, idx) => idx >= cells.length - 2)
        const [tailDx, tailDy] = [nx - tail.x, ny - tail.y]
        tail.x += moveDistance * tailDx / distance
        tail.y += moveDistance * tailDy / distance
      }
    }
  }

  public render() {
    if (this.status === 'move') // 更新蛇的移动
      this.move()

    const { gameMap, color, cells, eps, status, eyeDirection, eyeDx, eyeDy } = this
    const { ctx, L } = gameMap

    // 蛇死亡 > 白色身体
    ctx.fillStyle = status === 'die'
      ? ['#BEDAFF', '#FDCDC5'][Number(this.id === 1)]
      : color

    for (const { x, y } of cells) {
      ctx.beginPath()
      ctx.arc(x * L, y * L, L / 2 * 0.8, 0, Math.PI * 2)
      ctx.fill()
    }

    // 用长方形补全蛇身体
    for (let i = 1; i < cells.length; i++) {
      const [a, b] = [cells[i - 1], cells[i]]
      if (Math.abs(a.x - b.x) < eps && Math.abs(a.y - b.y) < eps)
        continue
      if (Math.abs(a.x - b.x) < eps) // 垂直方向
        ctx.fillRect((a.x - 0.5 + 0.1) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L)
      else // 水平方向
        ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.5 + 0.1) * L, Math.abs(a.x - b.x) * L, L * 0.8)
    }

    // 画蛇眼睛
    ctx.fillStyle = 'black'
    const { x: headX, y: headY } = cells[0]
    const hPoint = [headX, headY]
    for (let i = 0; i < 2; i++) {
      const [eyeX, eyeY] = [eyeDx, eyeDy].map(
        (dt, idx) => ((hPoint[idx] + dt[eyeDirection][i] * 0.15) * L))
      // 先画外围椭圆
      ctx.fillStyle = 'white'
      const width = [0.3 * L, 0.4 * L][Number([1, 3].includes(eyeDirection))]
      const height = [0.4 * L, 0.3 * L][Number([1, 3].includes(eyeDirection))]
      drawEllipse(ctx, eyeX - width / 2, eyeY - height / 2, width, height)

      ctx.beginPath()
      ctx.fillStyle = 'black'
      ctx.arc(eyeX, eyeY, L * 0.05, 0, Math.PI * 2)
      ctx.fill()
    }
  }
}
