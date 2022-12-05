/**
 * 画椭圆
 */
export function drawEllipse(
  ctx: CanvasRenderingContext2D,
  x: number,
  y: number,
  w: number,
  h: number,
) {
  const kappa = 0.5522848
  const [ox, oy] = [w, h].map(i => (i / 2) * kappa) // control point offset horizontal / vertical
  const [xe, ye, xm, ym] = [x + w, y + h, x + w / 2, y + h / 2]

  ctx.beginPath()
  ctx.moveTo(x, ym)
  ctx.bezierCurveTo(x, ym - oy, xm - ox, y, xm, y)
  ctx.bezierCurveTo(xm + ox, y, xe, ym - oy, xe, ym)
  ctx.bezierCurveTo(xe, ym + oy, xm + ox, ye, xm, ye)
  ctx.bezierCurveTo(xm - ox, ye, x, ym + oy, x, ym)
  ctx.closePath()
  ctx.fill()
}

