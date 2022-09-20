import { Game } from './Game'
import { GameSnake } from './GameSnake'
import { GameWall } from './GameWall'

const COLOR_EVEN = '#AAD751'
const COLOR_ODD = '#A2D149'

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
  /** 随机障碍物数量 */
  insideRandomWallNum: number
  /** 蛇集合 */
  snakes: GameSnake[]

  constructor(ctx: CanvasRenderingContext2D, parent: HTMLElement) {
    super()

    this.ctx = ctx
    this.parent = parent
    this.L = 0

    this.rows = 13
    this.cols = 14

    this.gameWalls = []
    this.insideRandomWallNum = 20

    this.snakes = [
      new GameSnake({ id: 0, color: '#4876EC', r: this.rows - 2, c: 1 }, this),
      new GameSnake({ id: 1, color: '#F94848', r: 1, c: this.cols - 2 }, this),
    ]
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
    const { rows, cols, insideRandomWallNum } = this

    const g: boolean[][] = []
    for (let r = 0; r < rows; r++) {
      g[r] = []
      for (let c = 0; c < cols; c++)
        g[r][c] = false
    }

    // 给四周加上障碍物
    for (let r = 0; r < rows; r++)
      g[r][0] = g[r][cols - 1] = true
    for (let c = 0; c < cols; c++)
      g[0][c] = g[rows - 1][c] = true

    // 创建随机障碍物
    for (let i = 0; i < insideRandomWallNum / 2; i++) {
      for (let j = 0; j < 1000; j++) {
        const r = parseInt(String(Math.random() * rows))
        const c = parseInt(String(Math.random() * cols))
        if (g[r][c] || g[rows - 1 - r][cols - 1 - c])
          continue
        if ((r === (rows - 2) && c === 1) || (r === 1 && c === (cols - 2)))
          continue
        g[r][c] = g[rows - 1 - r][cols - 1 - c] = true
        break
      }
    }

    const copyG = JSON.parse(JSON.stringify(g))
    if (!this.checkConnectivity(copyG, rows - 2, 1, 1, cols - 2))
      return false

    for (let r = 0; r < rows; r++) {
      for (let c = 0; c < cols; c++) {
        if (g[r][c])
          this.gameWalls.push(new GameWall(r, c, this))
      }
    }

    return true
  }

  addListeningEvents() {
    const canvas = this.ctx.canvas

    canvas.focus()
    canvas.addEventListener('keydown', (e) => {
      // if (e.key === '')
    })
  }

  start() {
    for (let i = 0; i < 1000; i++) {
      if (this.createGameWalls())
        break
    }

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
}
