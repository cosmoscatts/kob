package com.kob.backend.controller.rank.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RankRespVO {
    /** id */
    private Integer id;
    /** 名称 */
    private String name;
    /** 头像 */
    private String avatar;
    /** 积分战斗力 */
    private Integer rating;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    /** 排名 */
    private Integer rankNum;
}
