import type { AnyObject } from './base';

export interface Menu extends AnyObject {
  id: number
  label: string
  path?: string
  icon?: string
  children?: Menu[]
}
