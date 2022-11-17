import { Game } from './Game'
import type { GameMap } from './GameMap'

const GAME_WALL_COLOR = '#006622'
const GAME_BARRIER_COLOR = '#6E4633'

const barrierImage = new Image()
barrierImage.src = 'https://api.iconify.design/mingcute:box-3-fill.svg?color=white'
const wallImage = new Image()
wallImage.src = 'https://api.iconify.design/game-icons:brick-wall.svg?color=green'

export class GameWall extends Game {
  r: number
  c: number
  gameMap: GameMap
  color: string[]

  constructor(r: number, c: number, gameMap: GameMap) {
    super()

    this.r = r
    this.c = c
    this.gameMap = gameMap
    this.color = [GAME_WALL_COLOR, GAME_BARRIER_COLOR]
  }

  update() {
    this.render()
  }

  render() {
    const { gameMap, color, r, c } = this
    const { L, ctx } = gameMap

    const isWall = r === 0 || r === gameMap.rows - 1
    || c === 0 || c === gameMap.cols - 1
    ctx.fillStyle = color[Number(!isWall)]
    ctx.fillRect(c * L, r * L, L, L)

    if (isWall)
      ctx.drawImage(wallImage, c * L, r * L, L, L)
    else
      ctx.drawImage(barrierImage, (c + 0.2) * L, (r + 0.2) * L, 0.6 * L, 0.6 * L)
  }
}
