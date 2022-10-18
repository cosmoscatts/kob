package com.kob.backend.biz.rank;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.controller.rank.vo.RankRespVO;
import com.kob.backend.convert.UserConverter;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.service.UserService;

@Service
public class RankBizImpl implements RankBiz {
    @Resource
    private UserService userService;

    @Override
    public PageMap<RankRespVO> getList(PageQuery pageQuery) {
        IPage<UserDO> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        page = userService.page(page, Wrappers.<UserDO>lambdaQuery().orderByDesc(UserDO::getRating));

        List<UserDO> list = page.getRecords();

        if (list.isEmpty())
            return PageMap.empty();

        return PageMap.data(page.getTotal(),
            list.stream().map(UserConverter.INSTANCE::do2RankVO).collect(Collectors.toList()));
    }
}
