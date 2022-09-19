import { Game } from './Game'

export class GameMap extends Game {
  ctx: HTMLCanvasElement
  parent: HTMLElement
  /** `1` 单位长度 */
  L: number

  constructor(ctx: HTMLCanvasElement, parent: HTMLElement) {
    super()

    this.ctx = ctx
    this.parent = parent
    this.L = 0
  }

  start() {

  }

  update() {
    this.render()
  }

  render() {

  }
}
