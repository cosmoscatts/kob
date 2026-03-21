package com.kob.model.vo.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserInfoReqVO {
    private Integer id;
    private String name;
    private String avatar;
    private String email;
    private String phone;
}
