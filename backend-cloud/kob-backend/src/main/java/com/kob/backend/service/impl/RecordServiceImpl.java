package com.kob.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.common.DeleteQuery;
import com.kob.backend.common.PageMap;
import com.kob.backend.common.PageQuery;
import com.kob.backend.controller.record.vo.RecordRespVO;
import com.kob.backend.controller.record.vo.RecordSearchVO;
import com.kob.backend.convert.RecordConverter;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.security.UserDetailsImpl;
import com.kob.backend.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.backend.dataobject.RecordDO;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.service.RecordService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, RecordDO> implements RecordService {
    @Resource
    private UserService userService;

    @Override
    public PageMap<RecordRespVO> getList(PageQuery pageQuery, RecordSearchVO searchVO) {
        IPage<RecordDO> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        LambdaQueryWrapper<RecordDO> wrapper = Wrappers.<RecordDO>lambdaQuery().orderByDesc(RecordDO::getCreateTime);
        List<Integer> idList = new ArrayList<>();
        if (searchVO.getName() != null && !"".equals(searchVO.getName())) {
            idList = userService.list(Wrappers.<UserDO>lambdaQuery().like(UserDO::getName, searchVO.getName())).stream()
                    .map(UserDO::getId).collect(Collectors.toList());
            if (idList.isEmpty()) {
                return PageMap.empty();
            }
        }
        if (!idList.isEmpty()) {
            wrapper.in(RecordDO::getAId, idList).or().in(RecordDO::getBId, idList);
        }
        page = this.page(page, wrapper);

        List<RecordDO> list = page.getRecords();

        if (list.isEmpty())
            return PageMap.empty();

        List<UserDO> userList = userService.list();
        Map<Integer, UserDO> map = new HashMap<>();
        for (UserDO userDO : userList)
            map.put(userDO.getId(), userDO);

        return PageMap.data(page.getTotal(),
                list.stream().map(RecordConverter.INSTANCE::do2vo)
                        .map(i -> i.setAName(map.get(i.getAId()).getName()).setAAvatar(map.get(i.getAId()).getAvatar())
                                .setBName(map.get(i.getBId()).getName()).setBAvatar(map.get(i.getBId()).getAvatar()))
                        .collect(Collectors.toList()));
    }

    @Override
    public String delete(DeleteQuery query) {
        // 判断对局是否存在
        RecordDO record = this.getById(query.getId());
        if (Objects.isNull(record)) {
            return "对局不存在或已删除！";
        }

        // 判断是否是自己的对局
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authentication.getPrincipal();
        UserDO user = loginUser.getUser();
        if (!record.getAId().equals(user.getId()) && !record.getBId().equals(user.getId())) {
            return "您没有权限删除该对局！";
        }

        this.removeById(query.getId());
        return null;
    }
}
