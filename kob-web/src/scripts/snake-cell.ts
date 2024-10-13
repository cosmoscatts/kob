/**
 * 组成蛇的单元格
 */
export class SnakeCell {
  public x: number;
  public y: number;

  constructor(public r: number, public c: number) {
    this.r = r;
    this.c = c;

    this.x = c + 0.5;
    this.y = r + 0.5;
  }
}
