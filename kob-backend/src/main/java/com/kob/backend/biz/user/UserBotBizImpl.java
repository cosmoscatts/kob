package com.kob.backend.biz.user;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.common.DeleteQuery;
import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.controller.user.vo.BotReqVO;
import com.kob.backend.controller.user.vo.BotRespVO;
import com.kob.backend.convert.BotConverter;
import com.kob.backend.dataobject.BotDO;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.security.UserDetailsImpl;
import com.kob.backend.service.BotService;

@Service
public class UserBotBizImpl implements UserBotBiz {
    @Resource
    private BotService botService;

    @Override
    public PageMap<BotRespVO> getList(PageQuery pageQuery) {
        UsernamePasswordAuthenticationToken authenticationToken =
            (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        UserDO user = loginUser.getUser();

        IPage<BotDO> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        page = botService.page(page, Wrappers.<BotDO>lambdaQuery().eq(BotDO::getUserId, user.getId()));

        List<BotDO> list = page.getRecords();

        if (list.isEmpty())
            return PageMap.empty();

        return PageMap.data((long)list.size(),
            list.stream().map(BotConverter.INSTANCE::do2vo).collect(Collectors.toList()));
    }

    @Override
    public void add(BotReqVO botReqVO) {
        UsernamePasswordAuthenticationToken authenticationToken =
            (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        UserDO user = loginUser.getUser();

        if (Objects.isNull(botReqVO.getDescription())) {
            botReqVO.setDescription("这个用户很懒，什么也没留下~");
        }

        Date now = new Date();
        botReqVO.setId(null).setUserId(user.getId()).setCreateTime(now).setModifyTime(now);
        botService.save(BotConverter.INSTANCE.vo2do(botReqVO));
    }

    @Override
    public String update(BotReqVO botReqVO) {
        UsernamePasswordAuthenticationToken authenticationToken =
            (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        UserDO user = loginUser.getUser();

        // 判断 bot 是否存在
        BotDO bot = botService.getById(botReqVO.getId());
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

        botService.updateById(BotConverter.INSTANCE.vo2do(botReqVO));
        return null;
    }

    @Override
    public String delete(DeleteQuery query) {
        // 判断 bot 是否存在
        BotDO bot = botService.getById(query.getId());
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

        botService.removeById(query.getId());
        return null;
    }
}
