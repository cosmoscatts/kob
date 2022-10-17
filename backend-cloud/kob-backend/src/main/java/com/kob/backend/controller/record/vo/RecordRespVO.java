package com.kob.backend.controller.record.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RecordRespVO {
    private Integer id;
    @JsonProperty("aId")
    private Integer aId;
    @JsonProperty("aSx")
    private Integer aSx;
    @JsonProperty("aSy")
    private Integer aSy;
    @JsonProperty("bId")
    private Integer bId;
    @JsonProperty("bSx")
    private Integer bSx;
    @JsonProperty("bSy")
    private Integer bSy;
    @JsonProperty("aSteps")
    private String aSteps;
    @JsonProperty("bSteps")
    private String bSteps;
    private String map;
    private String loser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    @JsonProperty("aName")
    private String aName;
    @JsonProperty("aAvatar")
    private String aAvatar;
    @JsonProperty("bName")
    private String bName;
    @JsonProperty("bAvatar")
    private String bAvatar;
}
