package com.kob.backend.dataobject;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("user")
public class UserDO {
    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 账号 */
    private String username;
    /** 名称 */
    private String name;
    /** 密码 */
    private String password;
    /** 手机号 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** 头像 */
    private String avatar;
    /** 积分战斗力 */
    private Integer rating;
    /** openId */
    private String openId;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;
}
