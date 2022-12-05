import type { GameMap } from './map'

const [GAME_WALL_COLOR, GAME_BARRIER_COLOR] = ['#006622', '#6E4633']

const [barrierImage, wallImage] = [
  'https://api.iconify.design/mingcute:box-3-fill.svg?color=white',
  'https://api.iconify.design/game-icons:brick-wall.svg?color=green',
].map((url) => {
  const image = new Image()
  image.src = url
  return image
})

export class Wall {
  private color = [GAME_WALL_COLOR, GAME_BARRIER_COLOR]

  constructor(
    public r: number,
    public c: number,
    public gameMap: GameMap,
  ) {
    this.r = r
    this.c = c
    this.gameMap = gameMap
  }

  public render(ctx: CanvasRenderingContext2D) {
    const { gameMap, color, r, c } = this
    const { L } = gameMap

    const isWall = r === 0
    || c === 0
    || r === gameMap.rows - 1
    || c === gameMap.cols - 1
    ctx.fillStyle = color[Number(!isWall)]
    ctx.fillRect(c * L, r * L, L, L)

    if (isWall)
      ctx.drawImage(wallImage, c * L, r * L, L, L)
    else
      ctx.drawImage(barrierImage, (c + 0.2) * L, (r + 0.2) * L, 0.6 * L, 0.6 * L)
  }
}
