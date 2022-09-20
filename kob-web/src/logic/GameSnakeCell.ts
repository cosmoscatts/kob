/**
 * 组成蛇的单元格
 */
export class GameSnakeCell {
  r: number
  c: number
  x: number
  y: number

  constructor(r: number, c: number) {
    this.r = r
    this.c = c
    this.x = c + 0.5
    this.y = r + 0.5
  }
}
