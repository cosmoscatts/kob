package com.kob.backend.controller.user.vo;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRespVO {
    private Integer id;
    private String username;
    private String name;
    private String avatar;
    private Date createTime;
}
