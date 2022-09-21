package com.kob.backend.dataobject;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("user")
public class UserDO {
    /** id */
    private Integer id;
    /** 账号 */
    private String username;
    /** 名称 */
    private String name;
    /** 密码 */
    private String password;
    /** 头像 */
    private String avatar;
    /** 创建时间 */
    private Date createTime;
}
