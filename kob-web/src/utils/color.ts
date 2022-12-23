import { composite } from 'seemly'
import { colord, extend } from 'colord'
import mixPlugin from 'colord/plugins/mix'

extend([mixPlugin])

const ALPHA = 0.8

export type RGBA = [number, number, number, number]
export type RGB = [number, number, number]

/**
 * 创建 Hover 颜色
 */
export const createHoverColor = (color: string | RGB | RGBA) => composite(color, [255, 255, 255, 0.12])

/**
 * 创建 Pressed 颜色
 */
export const createPressedColor = (color: string | RGB | RGBA) => composite(color, [0, 0, 0, 0.15])

/**
 * 给颜色加透明度
 */
export const addColorAlpha = (color: string, alpha: number) => colord(color).alpha(alpha).toHex()

/**
 * 生成主色调的其他状态颜色，包括 hover、pressed、suppl 等状态
 */
export const createPrimaryColor = (primaryColor: string) => ({
  primaryColor,
  primaryColorHover: createHoverColor(primaryColor),
  primaryColorPressed: createPressedColor(primaryColor),
  primaryColorSuppl: addColorAlpha(primaryColor, ALPHA),
})
