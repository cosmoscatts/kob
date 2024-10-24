package com.kob.backend.service.impl;

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
import com.kob.backend.service.RankService;
import com.kob.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RankServiceImpl implements RankService {
    private static final String RANKING_KEY = "user:ranking";
    private static final String TOTAL_COUNT_KEY = "user:ranking:total";
    private static final long CACHE_EXPIRE_TIME = 3600; // 1小时

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserService userService;

    @Override
    public PageMap<RankRespVO> getRankingList(PageQuery pageQuery, RecordSearchVO searchVO) {
        String nameFilter = searchVO.getName();

        // 如果有名称筛选，直接走数据库查询
        if (StringUtils.hasText(nameFilter)) {
            return getListFromDB(pageQuery, searchVO);
        }

        // 尝试从Redis获取数据
        List<RankRespVO> rankings = getRankingsFromCache(pageQuery);
        if (rankings == null || rankings.isEmpty()) {
            return getListFromDB(pageQuery, searchVO);
        }

        // 获取总数
        Object totalObj = redisTemplate.opsForValue().get(TOTAL_COUNT_KEY);
        long total = totalObj != null ?
                Long.valueOf(totalObj.toString()) :
                userService.count();

        return PageMap.data(total, rankings);
    }

    private List<RankRespVO> getRankingsFromCache(PageQuery pageQuery) {
        // 首先获取所有排名数据用于计算实际排名
        Set<Object> allRankings = redisTemplate.opsForZSet().reverseRange(RANKING_KEY, 0, -1);
        if (allRankings == null || allRankings.isEmpty()) {
            refreshRankingCache();
            allRankings = redisTemplate.opsForZSet().reverseRange(RANKING_KEY, 0, -1);
            if (allRankings == null) {
                return Collections.emptyList();
            }
        }

        // 创建排名映射
        Map<Integer, Integer> rankMap = new HashMap<>();
        int rank = 1;
        for (Object userId : allRankings) {
            rankMap.put((Integer) userId, rank++);
        }

        // 获取分页数据
        int start = (pageQuery.getPage() - 1) * pageQuery.getPageSize();
        int end = start + pageQuery.getPageSize() - 1;
        Set<Object> pageRange = redisTemplate.opsForZSet().reverseRange(RANKING_KEY, start, end);

        if (pageRange == null || pageRange.isEmpty()) {
            return Collections.emptyList();
        }

        List<RankRespVO> result = new ArrayList<>();
        for (Object userId : pageRange) {
            UserDO user = userService.getById((Integer) userId);
            if (user != null) {
                RankRespVO rankVO = UserConverter.INSTANCE.do2RankVO(user);
                rankVO.setRankNum(rankMap.get((Integer) userId)); // 设置实际排名
                result.add(rankVO);
            }
        }

        return result;
    }

    private PageMap<RankRespVO> getListFromDB(PageQuery pageQuery, RecordSearchVO searchVO) {
        // 1. 首先获取所有用户并按rating降序排序（用于计算实际排名）
        List<UserDO> allUsers = userService.list(
                Wrappers.<UserDO>lambdaQuery()
                        .ne(UserDO::getId, 1)
                        .orderByDesc(UserDO::getRating)
        );

        // 创建排名映射，记录每个用户ID对应的实际排名
        Map<Integer, Integer> rankMap = new HashMap<>();
        for (int i = 0; i < allUsers.size(); i++) {
            rankMap.put(allUsers.get(i).getId(), i + 1);
        }

        // 2. 使用MyBatis-Plus的分页查询
        IPage<UserDO> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.<UserDO>lambdaQuery()
                .ne(UserDO::getId, 1)
                .orderByDesc(UserDO::getRating);

        // 添加名称筛选条件（如果有）
        if (StringUtils.hasText(searchVO.getName())) {
            wrapper.like(UserDO::getName, searchVO.getName());
        }

        // 执行分页查询
        page = userService.page(page, wrapper);
        List<UserDO> pageRecords = page.getRecords();

        if (pageRecords.isEmpty()) {
            return PageMap.empty();
        }

        // 3. 转换为VO并设置实际排名
        List<RankRespVO> data = pageRecords.stream()
                .map(user -> {
                    RankRespVO rankVO = UserConverter.INSTANCE.do2RankVO(user);
                    rankVO.setRankNum(rankMap.get(user.getId())); // 设置实际排名
                    return rankVO;
                })
                .collect(Collectors.toList());

        return PageMap.data(page.getTotal(), data);
    }

    @Scheduled(fixedRate = 3600000) // 每小时执行一次
    public void refreshRankingCache() {
        List<UserDO> users = userService.list(
                Wrappers.<UserDO>lambdaQuery()
                        .ne(UserDO::getId, 1)
                        .orderByDesc(UserDO::getRating)
        );

        redisTemplate.delete(RANKING_KEY);
        redisTemplate.delete(TOTAL_COUNT_KEY);

        if (!users.isEmpty()) {
            for (UserDO user : users) {
                redisTemplate.opsForZSet().add(RANKING_KEY, user.getId(), user.getRating());
            }
            // 确保使用Long类型存储总数
            redisTemplate.opsForValue().set(TOTAL_COUNT_KEY, Long.valueOf(users.size()),
                    CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            redisTemplate.expire(RANKING_KEY, CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
        }
    }

    public void updateUserRating(Integer userId, Integer newRating) {
        userService.update(
                Wrappers.<UserDO>lambdaUpdate()
                        .eq(UserDO::getId, userId)
                        .set(UserDO::getRating, newRating)
        );

        redisTemplate.opsForZSet().add(RANKING_KEY, userId, newRating);
    }

    public int calculateNewRating(int playerRating, int opponentRating, boolean won) {
        double expectedScore = 1 / (1 + Math.pow(10, (opponentRating - playerRating) / 400.0));
        double actualScore = won ? 1.0 : 0.0;
        return (int) Math.round(playerRating + 32 * (actualScore - expectedScore));
    }
}
