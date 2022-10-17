package com.kob.backend.biz.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.kob.backend.controller.record.vo.RecordRespVO;
import com.kob.backend.convert.RecordConverter;
import com.kob.backend.dataobject.RecordDO;
import com.kob.backend.dataobject.UserDO;
import com.kob.backend.security.UserDetailsImpl;
import com.kob.backend.service.RecordService;
import com.kob.backend.service.UserService;

@Service
public class RecordBizImpl implements RecordBiz {
    @Resource
    private RecordService recordService;
    @Resource
    private UserService userService;

    @Override
    public PageMap<RecordRespVO> getList(PageQuery pageQuery) {
        IPage<RecordDO> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        page = recordService.page(page, Wrappers.<RecordDO>lambdaQuery().orderByDesc(RecordDO::getCreateTime));

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
        RecordDO record = recordService.getById(query.getId());
        if (Objects.isNull(record)) {
            return "对局不存在或已删除！";
        }

        // 判断是否是自己的对局
        UsernamePasswordAuthenticationToken authentication =
            (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authentication.getPrincipal();
        UserDO user = loginUser.getUser();
        if (!record.getAId().equals(user.getId()) && !record.getBId().equals(user.getId())) {
            return "您没有权限删除该对局！";
        }

        recordService.removeById(query.getId());
        return null;
    }
}
