export interface AnyObject {
  [key: string]: unknown
}

export interface EntityBase extends AnyObject {
  id?: number
  createTime?: Date
}

export interface ModifiableEntity extends EntityBase {
  modifyTime?: Date
}

export interface UserOwnedEntity {
  userId?: number
}
