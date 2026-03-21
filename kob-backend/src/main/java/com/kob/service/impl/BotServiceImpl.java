package com.kob.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.common.DeleteQuery;
import com.kob.common.PageMap;
import com.kob.common.PageQuery;
import com.kob.model.vo.request.BotReqVO;
import com.kob.model.vo.response.BotRespVO;
import com.kob.model.converter.BotConverter;
import com.kob.model.entity.User;
import com.kob.security.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.model.entity.Bot;
import com.kob.mapper.BotMapper;
import com.kob.service.BotService;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BotServiceImpl extends ServiceImpl<BotMapper, Bot> implements BotService {
    @Override
    public PageMap<BotRespVO> getList(PageQuery pageQuery) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        IPage<Bot> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        page = this.page(page, Wrappers.<Bot>lambdaQuery().eq(Bot::getUserId, user.getId()));

        List<Bot> list = page.getRecords();

        if (list.isEmpty())
            return PageMap.empty();

        return PageMap.data(page.getTotal(),
                list.stream().map(BotConverter.INSTANCE::do2vo).collect(Collectors.toList()));
    }

    @Override
    public List<BotRespVO> getMachineBots() {
        LambdaQueryWrapper<Bot> queryWrapper = Wrappers.<Bot>lambdaQuery().eq(Bot::getUserId, 1);
        List<Bot> list = this.list(queryWrapper);
        return list.stream().map(BotConverter.INSTANCE::do2vo).collect(Collectors.toList());
    }

    @Override
    public String add(BotReqVO botReqVO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        // 一个用户最多添加 10 个 bot
        List<Bot> list = this.list(Wrappers.<Bot>lambdaQuery().eq(Bot::getUserId, user.getId()));
        if (list.size() >= 10) {
            return "最多只能添加 10 个 bot！";
        }

        if (Objects.isNull(botReqVO.getDescription()) || botReqVO.getDescription().length() == 0) {
            botReqVO.setDescription("这个用户很懒，什么也没留下~");
        }

        Date now = new Date();
        botReqVO.setId(null).setUserId(user.getId()).setCreateTime(now).setModifyTime(now);
        this.save(BotConverter.INSTANCE.vo2do(botReqVO));
        return null;
    }

    @Override
    public String update(BotReqVO botReqVO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        // 判断 bot 是否存在
        Bot bot = this.getById(botReqVO.getId());
        if (Objects.isNull(bot)) {
            return "bot 不存在或已删除！";
        }
        // 判断是否是自己的 bot，否则无权修改
        if (!bot.getUserId().equals(user.getId())) {
            return "您没有权限删除该 bot！";
        }

        if (Objects.isNull(botReqVO.getDescription())) {
            botReqVO.setDescription("这个用户很懒，什么也没留下~");
        }

        botReqVO.setModifyTime(new Date());

        this.updateById(BotConverter.INSTANCE.vo2do(botReqVO));
        return null;
    }

    @Override
    public String delete(DeleteQuery query) {
        // 判断 bot 是否存在
        Bot bot = this.getById(query.getId());
        if (Objects.isNull(bot)) {
            return "bot 不存在或已删除！";
        }

        // 判断是否是自己的 bot
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authentication.getPrincipal();
        User user = loginUser.getUser();
        if (!bot.getUserId().equals(user.getId())) {
            return "您没有权限删除该 bot！";
        }

        this.removeById(query.getId());
        return null;
    }
}
