package com.kob.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.common.DeleteQuery;
import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.controller.user.vo.BotReqVO;
import com.kob.backend.controller.user.vo.BotRespVO;
import com.kob.backend.convert.BotConverter;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.security.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.dataobject.BotDO;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.service.BotService;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BotServiceImpl extends ServiceImpl<BotMapper, BotDO> implements BotService {
    @Override
    public PageMap<BotRespVO> getList(PageQuery pageQuery) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        UserDO user = loginUser.getUser();

        IPage<BotDO> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        page = this.page(page, Wrappers.<BotDO>lambdaQuery().eq(BotDO::getUserId, user.getId()));

        List<BotDO> list = page.getRecords();

        if (list.isEmpty())
            return PageMap.empty();

        return PageMap.data(page.getTotal(),
                list.stream().map(BotConverter.INSTANCE::do2vo).collect(Collectors.toList()));
    }

    @Override
    public String add(BotReqVO botReqVO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        UserDO user = loginUser.getUser();

        // 一个用户最多添加 10 个 bot
        List<BotDO> list = this.list(Wrappers.<BotDO>lambdaQuery().eq(BotDO::getUserId, user.getId()));
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
        UserDO user = loginUser.getUser();

        // 判断 bot 是否存在
        BotDO bot = this.getById(botReqVO.getId());
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
        BotDO bot = this.getById(query.getId());
        if (Objects.isNull(bot)) {
            return "bot 不存在或已删除！";
        }

        // 判断是否是自己的 bot
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authentication.getPrincipal();
        UserDO user = loginUser.getUser();
        if (!bot.getUserId().equals(user.getId())) {
            return "您没有权限删除该 bot！";
        }

        this.removeById(query.getId());
        return null;
    }
}
