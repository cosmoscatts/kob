package com.kob.model.vo.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AccountRespVO {
    private String token;
}
