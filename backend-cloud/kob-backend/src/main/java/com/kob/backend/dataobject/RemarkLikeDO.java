package com.kob.backend.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("remark_like")
public class RemarkLikeDO {
    /** id */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 意见id */
    private Integer remarkId;
    /** 喜欢该意见的用户id */
    private Integer likeUserId;
}
