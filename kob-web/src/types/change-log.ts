import type { AnyObject } from './global';

export interface ChangeLog extends AnyObject {
  date?: string
  title?: string
  description?: string
  changes?: ChangeLogItem[]
}

export interface ChangeLogItem extends AnyObject {
  id: string
  type: 'add' | 'update' | 'fix'
  title?: string
  content?: string
}
