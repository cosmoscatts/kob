import { colord, extend } from 'colord';
import mixPlugin from 'colord/plugins/mix';
import { composite } from 'seemly';

extend([mixPlugin]);

export type RGBA = [number, number, number, number];
export type RGB = [number, number, number];

const WHITE = [255, 255, 255] as RGB;
const BLACK = [0, 0, 0] as RGB;
const HOVER_ALPHA = 0.12;
const PRESSED_ALPHA = 0.15;
const PRIMARY_ALPHA = 0.8;

const withAlpha = (color: RGB, alpha: number): RGBA => [...color, alpha];

export const lighten = (color: string | RGB | RGBA) =>
  composite(color, withAlpha(WHITE, HOVER_ALPHA));

export const darken = (color: string | RGB | RGBA) =>
  composite(color, withAlpha(BLACK, PRESSED_ALPHA));

export const addColorAlpha = (color: string, alpha: number): string =>
  colord(color).alpha(alpha).toHex();

export const createPrimaryColorPalette = (primaryColor: string) => ({
  primaryColor,
  primaryColorHover: lighten(primaryColor),
  primaryColorPressed: darken(primaryColor),
  primaryColorSuppl: addColorAlpha(primaryColor, PRIMARY_ALPHA),
});
