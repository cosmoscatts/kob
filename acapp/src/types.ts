/** 任意 `Object` */
export interface AnyObject {
  [key: string]: unknown
}

/** `通用` 请求参数 */
export interface HttpParams {
  urlParams?: AnyObject | AnyObject[]
}

/** `Get` 请求参数 */
export interface GetParams extends HttpParams {}

/** `Post` 请求参数 */
export interface PostParams extends HttpParams {
  body?: AnyObject
}

/** `Put` 请求参数 */
export interface PutParams extends HttpParams {
  body?: AnyObject
}

/** `Delete` 请求参数 */
export interface DeleteParams extends HttpParams {}

/** `Http` 返回数据结构 */
export interface Result<T> {
  code: number
  data: T
  msg?: string
}

/** 分页查询 */
export interface PageQuery {
  page?: number
  pageSize?: number
}

/** 分页返回数据结构 */
export interface PageResult<T> {
  code: number
  data: {
    [key: string]: any
    records?: T[]
    total?: number
  }
  msg?: string
}

/** 菜单项数据结构 */
export interface Menu {
  id: number
  label: string
  path?: string
  icon?: string
  children?: Menu[]
}

/** 定义用户的数据类型 */
export interface User {
  /** id */
  id?: number
  /** 账号 */
  username?: string
  /** 用户名称 */
  name?: string
  /** 密码 */
  password?: string
  /** 头像 */
  avatar?: string
  /** 手机号 */
  phone?: string
  /** 邮箱 */
  email?: string
  /** 天梯分 */
  rating?: number
  /** 创建时间 */
  createTime?: Date
}

/**
 * 定义 `bot` 数据结构
 */
export interface Bot {
  /** id */
  id?: number
  /** 用户 `id` */
  userId?: number
  /** 标题 */
  title?: string
  /** 描述 */
  description?: string
  /** 内容 */
  content?: string
  /** 创建时间 */
  createTime?: Date
  /** 修改时间 */
  modifyTime?: Date
}

/** 后端返回对局游戏信息的数据结构 */
export interface Game {
  aId: number
  aSx: number
  aSy: number
  bId: number
  bSx: number
  bSy: number
  map: number[][]
}

export interface Record {
  id: number
  aId: number
  aSx: number
  aSy: number
  bId: number
  bSx: number
  bSy: number
  aSteps: string
  bSteps: string
  map: string
  loser: string
  createTime: Date

  aName?: string
  aAvatar?: string
  bName?: string
  bAvatar?: string
}

/** 定义排行榜数据类型 */
export interface Rank {
  /** 用户 id */
  id?: number
  /** 用户名称 */
  name?: string
  /** 头像 */
  avatar?: string
  /** 天梯分 */
  rating?: number
  /** 创建时间 */
  createTime?: Date

  /** 排名 */
  rankNum?: number
}

/** AcWing Code 数据结构 */
export interface AcCode {
  appid?: string
  redirectUri?: string
  scope?: string
  state?: string
}
