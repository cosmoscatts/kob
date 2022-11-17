package com.kob.backend.controller.remark.vo;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RemarkReqVO {
    private Integer id;
    private Integer userId;
    @NotBlank(message = "意见不能为空")
    private String remark;
    private Integer parentId;
    private Date createTime;
}
