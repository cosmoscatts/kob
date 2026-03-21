package com.kob.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.common.DeleteQuery;
import com.kob.common.PageMap;
import com.kob.common.PageQuery;
import com.kob.model.vo.response.RecordRespVO;
import com.kob.model.vo.request.RecordSearchVO;
import com.kob.model.converter.RecordConverter;
import com.kob.model.entity.User;
import com.kob.security.UserDetailsImpl;
import com.kob.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kob.model.entity.Record;
import com.kob.mapper.RecordMapper;
import com.kob.service.RecordService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {
    private final UserService userService;

    @Override
    public PageMap<RecordRespVO> getList(PageQuery pageQuery, RecordSearchVO searchVO) {
        IPage<Record> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        LambdaQueryWrapper<Record> wrapper = Wrappers.<Record>lambdaQuery().orderByDesc(Record::getCreateTime);
        List<Integer> idList = new ArrayList<>();
        if (searchVO.getName() != null && !"".equals(searchVO.getName())) {
            idList = userService.list(Wrappers.<User>lambdaQuery().like(User::getName, searchVO.getName())).stream()
                    .map(User::getId).collect(Collectors.toList());
            if (idList.isEmpty()) {
                return PageMap.empty();
            }
        }
        if (!idList.isEmpty()) {
            wrapper.in(Record::getAId, idList).or().in(Record::getBId, idList);
        }
        page = this.page(page, wrapper);

        List<Record> list = page.getRecords();

        if (list.isEmpty())
            return PageMap.empty();

        List<User> userList = userService.list();
        Map<Integer, User> map = new HashMap<>();
        for (User userDO : userList)
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
        Record record = this.getById(query.getId());
        if (Objects.isNull(record)) {
            return "对局不存在或已删除！";
        }

        // 判断是否是自己的对局
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authentication.getPrincipal();
        User user = loginUser.getUser();
        if (!record.getAId().equals(user.getId()) && !record.getBId().equals(user.getId())) {
            return "您没有权限删除该对局！";
        }

        this.removeById(query.getId());
        return null;
    }
}
