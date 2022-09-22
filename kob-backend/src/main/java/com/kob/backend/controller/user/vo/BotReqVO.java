package com.kob.backend.controller.user.vo;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BotReqVO {
    private Integer id;
    private Integer userId;
    @NotBlank(message = "标题不能为空")
    @Length(max = 100, message = "标题长度不能大于 100")
    private String title;
    @Length(max = 300, message = "Bot 描述的长度不能大于 300")
    private String description;
    @NotBlank(message = "代码不能为空")
    @Length(max = 10000, message = "代码长度不能超过 10000")
    private String content;
    private Integer rating;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
}
