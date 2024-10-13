import type { ModifiableEntity, UserOwnedEntity } from './base';

export interface Bot extends ModifiableEntity, UserOwnedEntity {
  title?: string
  description?: string
  content?: string
}
