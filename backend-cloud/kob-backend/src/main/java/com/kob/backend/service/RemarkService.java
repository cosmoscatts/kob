package com.kob.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.controller.remark.vo.RemarkReqVO;
import com.kob.backend.controller.remark.vo.RemarkRespVO;
import com.kob.backend.dataobject.RemarkDO;

import java.util.List;

public interface RemarkService extends IService<RemarkDO> {
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
