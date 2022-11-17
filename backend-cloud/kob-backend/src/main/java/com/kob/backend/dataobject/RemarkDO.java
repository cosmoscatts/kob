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
@TableName("remark")
public class RemarkDO {
    /** id */
    @TableId(type = IdType.AUTO)
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
}
