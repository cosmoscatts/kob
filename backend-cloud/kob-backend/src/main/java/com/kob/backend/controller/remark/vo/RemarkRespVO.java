package com.kob.backend.controller.remark.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kob.backend.controller.rank.vo.RankRespVO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RemarkRespVO {
    /** id */
    private Integer id;
    /** 用户id */
    private Integer userId;
    /** 意见 */
    private String remark;
    /** 父级id */
    private Integer parentId;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    /** 点赞数 */
    private Integer likes;
    /** 用户 */
    private RankRespVO user;
}
