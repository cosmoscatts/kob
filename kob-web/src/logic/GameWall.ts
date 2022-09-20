import { Game } from './Game'
import type { GameMap } from './GameMap'

const GAME_WALL_COLOR = '#B37226'

export class GameWall extends Game {
  r: number
  c: number
  gameMap: GameMap
  color: string

  constructor(r: number, c: number, gameMap: GameMap) {
    super()

    this.r = r
    this.c = c
    this.gameMap = gameMap
    this.color = GAME_WALL_COLOR
  }

  update() {
    this.render()
  }

  render() {
    const { gameMap, color, r, c } = this
    const { L, ctx } = gameMap

    ctx.fillStyle = color
    ctx.fillRect(c * L, r * L, L, L)
  }
}
