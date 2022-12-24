import type { Pausable } from '@vueuse/core'
import { Game } from './game'
import { Snake } from './snake'
import { Wall } from './wall'

const COLOR_EVEN = '#C3944E'
const COLOR_ODD = '#A57332'

export class GameMap extends Game {
  L = 0 // 1 单位长度
  rows = 13 // 地图行数
  cols = 14 // 地图列数
  baseCtx: CanvasRenderingContext2D | undefined

  walls: Wall[] = [] // 障碍物
  snakes: Snake[] = [ // 蛇集合
    new Snake({ id: 0, color: '#206CCF', r: this.rows - 2, c: 1 }, this),
    new Snake({ id: 1, color: '#CB272D', r: 1, c: this.cols - 2 }, this),
  ]

  recordFn: Pausable | null = null // 录像执行方法
  task: Pausable | null = null // 匹配时，读取下一步操作的定时任务

  constructor(
    public ctx: CanvasRenderingContext2D,
    private parent: HTMLElement,
  ) {
    super()

    this.ctx = ctx
    this.parent = parent
  }

  createWalls() {
    const { rows, cols } = this
    const { gameMap } = usePkStore()
    const { isRecord, gameMap: gameMap2 } = useRecordStore()

    const g: number[][] = [gameMap!, gameMap2!][Number(isRecord)]

    for (let r = 0; r < rows; r++) {
      for (let c = 0; c < cols; c++) {
        if (g[r][c]) this.walls.push(new Wall(r, c, this))
      }
    }
  }

  playRecord() {
    const { aSteps, bSteps, loser, updateRecordFinished } = useRecordStore()
    let k = 0
    const [snake0, snake1] = this.snakes
    let _recordFn: Pausable | null = null

    _recordFn = this.recordFn = useIntervalFn(() => {
      if (k >= aSteps.length - 1) {
        if (['all', 'A'].includes(loser)) snake0.status = 'die'
        if (['all', 'B'].includes(loser)) snake1.status = 'die'
        _recordFn?.pause() // 这里注意 this 的指向问题
        updateRecordFinished(true)
      } else {
        snake0.setDirection(parseInt(aSteps[k]))
        snake1.setDirection(parseInt(bSteps[k]))
      }
      k++
    }, 300)
  }

  addListeningEvents() {
    const { socket } = usePkStore()

    const canvas = this.ctx.canvas
    canvas.focus()

    canvas.addEventListener('keydown', (e) => {
      let d = -1
      if (e.key === 'w') d = 0
      else if (e.key === 'd') d = 1
      else if (e.key === 's') d = 2
      else if (e.key === 'a') d = 3

      if (d >= 0) {
        socket?.send(JSON.stringify({
          event: 'move',
          direction: d,
        }))
      }
    })
  }

  start() {
    this.createWalls()
    const { isRecord } = useRecordStore()
    if (isRecord) this.playRecord()
    else this.addListeningEvents()
  }

  updateSize() {
    const { parent, cols, rows } = this
    const { clientWidth, clientHeight } = parent
    this.L = parseInt(String(Math.min(clientWidth / cols, clientHeight / rows)))
    // 计算 canvas 宽高
    this.ctx.canvas.width = this.L * cols
    this.ctx.canvas.height = this.L * rows

    if (this.baseCtx && this.ctx.canvas.width !== this.baseCtx.canvas.width
      && this.ctx.canvas.height !== this.baseCtx.canvas.height)
      this.renderBaseCanvas()
  }

  /**
   * 判断两条蛇是否都准备好下一回合
   */
  checkSnakeReady() {
    for (const { directions, status } of this.snakes) {
      if (status !== 'idle' || !directions.length) return false
    }
    const [{ directions: { length: l1 } }, { directions: { length: l2 } }] = this.snakes
    return l1 === l2
  }

  /**
   * 让两条蛇进入下一回合
   */
  nextStep() {
    for (const snake of this.snakes)
      snake.updateNextStep()
  }

  update() {
    const { isRecord } = useRecordStore()
    const fn = () => {
      if (this.checkSnakeReady()) {
        this.nextStep()
        return true
      }
      return false
    }

    this.updateSize()

    // eslint-disable-next-line max-statements-per-line
    if (isRecord) { fn() }
    else {
      if (!this.task) {
        const flag = fn()
        if (flag)
          this.task = useIntervalFn(fn, 100)
      }
    }

    this.render()
  }

  renderBaseCanvas() {
    if (!this.baseCtx) return
    const { ctx, L, cols, rows } = this

    const { height, width } = ctx.canvas
    this.baseCtx.canvas.width = width
    this.baseCtx.canvas.height = height

    this.baseCtx.clearRect(0, 0, width, height)

    for (let r = 0; r < rows; r++) {
      for (let c = 0; c < cols; c++) {
        this.baseCtx.fillStyle = ((r + c) & 1) === 0
          ? COLOR_EVEN
          : COLOR_ODD
        this.baseCtx.fillRect(c * L, r * L, L, L)
      }
    }
    // 画墙
    this.walls.forEach(wall => wall.render(this.baseCtx!))
  }

  render() {
    if (!this.baseCtx) {
      const baseCanvas = document.createElement('canvas')
      this.baseCtx = baseCanvas.getContext('2d')!
      this.renderBaseCanvas()
    }
    if (this.baseCtx && this.baseCtx.canvas.height > 0)
      this.ctx.drawImage(this.baseCtx.canvas, 0, 0)
    // 画蛇
    this.snakes.forEach(snake => snake.render())
  }

  beforeDestory() {
    // 销毁 canvas
    this.ctx.clearRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height)
  }
}
