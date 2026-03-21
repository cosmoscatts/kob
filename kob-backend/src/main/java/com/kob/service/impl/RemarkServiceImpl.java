package com.kob.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.common.PageMap;
import com.kob.common.PageQuery;
import com.kob.model.vo.response.RankRespVO;
import com.kob.model.vo.request.RemarkReqVO;
import com.kob.model.vo.response.RemarkRespVO;
import com.kob.model.converter.RemarkConverter;
import com.kob.model.converter.UserConverter;
import com.kob.model.entity.RemarkLike;
import com.kob.model.entity.User;
import com.kob.security.UserDetailsImpl;
import com.kob.service.RemarkLikeService;
import com.kob.service.UserService;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.model.entity.Remark;
import com.kob.mapper.RemarkMapper;
import com.kob.service.RemarkService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RemarkServiceImpl extends ServiceImpl<RemarkMapper, Remark> implements RemarkService {
    private final RemarkLikeService remarkLikeService;
    private final UserService userService;

    @Override
    public String addRemark(RemarkReqVO remarkReqVO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        remarkReqVO.setUserId(user.getId()).setCreateTime(new Date())
                .setRemark(EmojiParser.parseToAliases(remarkReqVO.getRemark())); // 表情符号转字符
        this.save(RemarkConverter.INSTANCE.vo2do(remarkReqVO));
        return null;
    }

    @Override
    public PageMap<RemarkRespVO> getRemarkList(PageQuery pageQuery) {
        IPage<Remark> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        page = this.page(page, Wrappers.<Remark>lambdaQuery().orderByDesc(Remark::getCreateTime));

        List<Remark> list = page.getRecords();
        if (list.isEmpty()) {
            return PageMap.empty();
        }

        List<RemarkRespVO> data = list.stream()
                .map(item -> item.setRemark(EmojiParser.parseToUnicode(item.getRemark())))
                .map(RemarkConverter.INSTANCE::do2vo).collect(Collectors.toList());
        List<Integer> remarkIdList = list.stream().map(Remark::getId).collect(Collectors.toList());
        List<RemarkLike> likeList =
                remarkLikeService.list(Wrappers.<RemarkLike>lambdaQuery().in(RemarkLike::getRemarkId, remarkIdList));
        Map<Integer, Integer> likeMap = new HashMap<>();
        for (RemarkLike likeDO : likeList) {
            Integer num = likeMap.getOrDefault(likeDO.getRemarkId(), 0) + 1;
            likeMap.put(likeDO.getRemarkId(), num);
        }

        List<Integer> userIdList = list.stream().map(Remark::getUserId).collect(Collectors.toList());
        List<RankRespVO> userList = userService.list(Wrappers.<User>lambdaQuery().in(User::getId, userIdList))
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
        User user = loginUser.getUser();

        List<Integer> likes =
                remarkLikeService.list(Wrappers.<RemarkLike>lambdaQuery().eq(RemarkLike::getLikeUserId, user.getId()))
                        .stream().map(RemarkLike::getRemarkId).collect(Collectors.toList());
        return likes;
    }

    @Override
    public String likeRemark(Integer remarkId) {
        if (remarkId == null) {
            return "意见id不能为空";
        }
        Remark remark = this.getById(remarkId);
        if (remark == null) {
            return "意见不存在";
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        RemarkLike like = new RemarkLike().setRemarkId(remarkId).setLikeUserId(user.getId());
        remarkLikeService.save(like);
        return null;
    }

    @Override
    public String dislikeRemark(Integer remarkId) {
        if (remarkId == null) {
            return "意见id不能为空";
        }
        Remark remark = this.getById(remarkId);
        if (remark == null) {
            return "意见不存在";
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        remarkLikeService.remove(Wrappers.<RemarkLike>lambdaQuery().eq(RemarkLike::getRemarkId, remarkId)
                .eq(RemarkLike::getLikeUserId, user.getId()));
        return null;
    }
}
