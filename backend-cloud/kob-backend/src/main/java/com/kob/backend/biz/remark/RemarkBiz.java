package com.kob.backend.biz.remark;

import java.util.List;

import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.controller.remark.vo.RemarkReqVO;
import com.kob.backend.controller.remark.vo.RemarkRespVO;

public interface RemarkBiz {
    // 添加意见
    String addRemark(RemarkReqVO remarkReqVO);

    // 获取意见列表
    PageMap<RemarkRespVO> getRemarkList(PageQuery pageQuery);

    // 获取登录用户点赞的意见
    List<Integer> getCurrentUserLikes();

    // 支持意见
    String likeRemark(Integer remarkId);

    // 取消支持
    String dislikeRemark(Integer remarkId);
}
