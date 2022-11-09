package com.kob.backend.biz.rank;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.controller.rank.vo.RankRespVO;
import com.kob.backend.controller.record.vo.RecordSearchVO;
import com.kob.backend.convert.UserConverter;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.service.UserService;

@Service
public class RankBizImpl implements RankBiz {
    @Resource
    private UserService userService;

    @Override
    public PageMap<RankRespVO> getList(PageQuery pageQuery, RecordSearchVO searchVO) {
        IPage<UserDO> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.<UserDO>lambdaQuery().orderByDesc(UserDO::getRating);
        if (searchVO.getName() != null && !"".equals(searchVO.getName())) {
            wrapper.like(UserDO::getName, searchVO.getName());
        }
        page = userService.page(page, wrapper);

        List<UserDO> list = page.getRecords();

        if (list.isEmpty())
            return PageMap.empty();

        List<RankRespVO> data = list.stream().map(UserConverter.INSTANCE::do2RankVO).collect(Collectors.toList());
        for (int i = 0; i < data.size(); i++) {
            int a = pageQuery.getPage(), b = pageQuery.getPageSize();
            data.get(i).setRankNum((a - 1) * b + i + 1);
        }
        return PageMap.data(page.getTotal(), data);
    }
}
