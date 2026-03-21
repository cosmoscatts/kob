package com.kob.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.common.PageMap;
import com.kob.common.PageQuery;
import com.kob.model.vo.request.RemarkReqVO;
import com.kob.model.vo.response.RemarkRespVO;
import com.kob.model.entity.Remark;

import java.util.List;

public interface RemarkService extends IService<Remark> {
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
