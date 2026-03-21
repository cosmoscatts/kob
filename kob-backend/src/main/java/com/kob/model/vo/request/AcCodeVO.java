package com.kob.model.vo.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AcCodeVO {
    String code;
    String state;
}
