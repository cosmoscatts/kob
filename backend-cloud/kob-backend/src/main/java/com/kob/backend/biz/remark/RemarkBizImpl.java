package com.kob.backend.biz.remark;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.controller.rank.vo.RankRespVO;
import com.kob.backend.controller.remark.vo.RemarkReqVO;
import com.kob.backend.controller.remark.vo.RemarkRespVO;
import com.kob.backend.convert.RemarkConverter;
import com.kob.backend.convert.UserConverter;
import com.kob.backend.dataobject.RemarkDO;
import com.kob.backend.dataobject.RemarkLikeDO;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.security.UserDetailsImpl;
import com.kob.backend.service.RemarkLikeService;
import com.kob.backend.service.RemarkService;
import com.kob.backend.service.UserService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RemarkBizImpl implements RemarkBiz {
    @Resource
    private RemarkService remarkService;
    @Resource
    private RemarkLikeService remarkLikeService;
    @Resource
    private UserService userService;

    @Override
    public String addRemark(RemarkReqVO remarkReqVO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        UserDO user = loginUser.getUser();

        remarkReqVO.setUserId(user.getId()).setCreateTime(new Date())
                .setRemark(EmojiParser.parseToAliases(remarkReqVO.getRemark())); // 表情符号转字符
        remarkService.save(RemarkConverter.INSTANCE.vo2do(remarkReqVO));
        return null;
    }

    @Override
    public PageMap<RemarkRespVO> getRemarkList(PageQuery pageQuery) {
        IPage<RemarkDO> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        page = remarkService.page(page, Wrappers.<RemarkDO>lambdaQuery().orderByDesc(RemarkDO::getCreateTime));

        List<RemarkDO> list = page.getRecords();
        if (list.isEmpty()) {
            return PageMap.empty();
        }

        List<RemarkRespVO> data = list.stream()
                .map(item -> item.setRemark(EmojiParser.parseToUnicode(item.getRemark())))
                .map(RemarkConverter.INSTANCE::do2vo).collect(Collectors.toList());
        List<Integer> remarkIdList = list.stream().map(RemarkDO::getId).collect(Collectors.toList());
        List<RemarkLikeDO> likeList =
                remarkLikeService.list(Wrappers.<RemarkLikeDO>lambdaQuery().in(RemarkLikeDO::getRemarkId, remarkIdList));
        Map<Integer, Integer> likeMap = new HashMap<>();
        for (RemarkLikeDO likeDO : likeList) {
            Integer num = likeMap.getOrDefault(likeDO.getRemarkId(), 0) + 1;
            likeMap.put(likeDO.getRemarkId(), num);
        }

        List<Integer> userIdList = list.stream().map(RemarkDO::getUserId).collect(Collectors.toList());
        List<RankRespVO> userList = userService.list(Wrappers.<UserDO>lambdaQuery().in(UserDO::getId, userIdList))
                .stream().map(UserConverter.INSTANCE::do2RankVO).collect(Collectors.toList());
        Map<Integer, RankRespVO> userMap = new HashMap<>();
        for (RankRespVO rankRespVO : userList) {
            userMap.put(rankRespVO.getId(), rankRespVO);
        }
        for (RemarkRespVO respVO : data) {
            respVO.setLikes(likeMap.getOrDefault(respVO.getId(), 0))
                    .setUser(userMap.getOrDefault(respVO.getUserId(), null));
        }

        return PageMap.data(page.getTotal(), data);
    }

    @Override
    public List<Integer> getCurrentUserLikes() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        UserDO user = loginUser.getUser();

        List<Integer> likes =
                remarkLikeService.list(Wrappers.<RemarkLikeDO>lambdaQuery().eq(RemarkLikeDO::getLikeUserId, user.getId()))
                        .stream().map(RemarkLikeDO::getRemarkId).collect(Collectors.toList());
        return likes;
    }

    @Override
    public String likeRemark(Integer remarkId) {
        if (remarkId == null) {
            return "意见id不能为空";
        }
        RemarkDO remark = remarkService.getById(remarkId);
        if (remark == null) {
            return "意见不存在";
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        UserDO user = loginUser.getUser();

        RemarkLikeDO like = new RemarkLikeDO().setRemarkId(remarkId).setLikeUserId(user.getId());
        remarkLikeService.save(like);
        return null;
    }

    @Override
    public String dislikeRemark(Integer remarkId) {
        if (remarkId == null) {
            return "意见id不能为空";
        }
        RemarkDO remark = remarkService.getById(remarkId);
        if (remark == null) {
            return "意见不存在";
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        UserDO user = loginUser.getUser();

        remarkLikeService.remove(Wrappers.<RemarkLikeDO>lambdaQuery().eq(RemarkLikeDO::getRemarkId, remarkId)
                .eq(RemarkLikeDO::getLikeUserId, user.getId()));
        return null;
    }
}
