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

  eyeDx: number[][]
  eyeDy: number[][]

  step: number

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

    // 当前回合数
    this.step = 0
  }

  start() {

  }

  /**
   * 更新蛇的下一步状态
   */
  updateNextStep() {
    const { direction: d, snakeCells, dr, dc } = this

    // 蛇头
    const head = snakeCells[0]
    this.nextCell = new GameSnakeCell(head.r + dr[d], head.c + dc[d])
    // 清空操作
    this.direction = -1
    this.status = 'move'
    this.step++
  }

  move() {

  }

  update() {
    this.move()
    this.render()
  }

  render() {
    const { gameMap, color, snakeCells } = this
    const { ctx, L } = gameMap

    ctx.fillStyle = color
    for (const { x, y } of snakeCells) {
      ctx.beginPath()
      ctx.arc(x * L, y * L, L / 2, 0, Math.PI * 2)
      ctx.fill()
    }
  }
}
