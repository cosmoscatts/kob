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
import org.springframework.data.redis.core.ZSetOperations;
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
        if (rankings == null) {
            return getListFromDB(pageQuery, searchVO);
        }

        Long total = redisTemplate.opsForValue().get(TOTAL_COUNT_KEY) != null
                ? (Long) redisTemplate.opsForValue().get(TOTAL_COUNT_KEY)
                : userService.count();

        return PageMap.data(total, rankings);
    }

    private List<RankRespVO> getRankingsFromCache(PageQuery pageQuery) {
        int start = (pageQuery.getPage() - 1) * pageQuery.getPageSize();
        int end = start + pageQuery.getPageSize() - 1;

        Set<ZSetOperations.TypedTuple<Object>> rangeWithScores = redisTemplate.opsForZSet()
                .reverseRangeWithScores(RANKING_KEY, start, end);

        if (rangeWithScores == null || rangeWithScores.isEmpty()) {
            refreshRankingCache();
            rangeWithScores = redisTemplate.opsForZSet()
                    .reverseRangeWithScores(RANKING_KEY, start, end);
        }

        return convertToRankRespVO(rangeWithScores, start);
    }

    private PageMap<RankRespVO> getListFromDB(PageQuery pageQuery, RecordSearchVO searchVO) {
        IPage<UserDO> page = new Page<>(pageQuery.getPage(), pageQuery.getPageSize());
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.<UserDO>lambdaQuery()
                .ne(UserDO::getId, 1)
                .orderByDesc(UserDO::getRating);

        if (StringUtils.hasText(searchVO.getName())) {
            wrapper.like(UserDO::getName, searchVO.getName());
        }

        page = userService.page(page, wrapper);
        List<UserDO> list = page.getRecords();

        if (list.isEmpty()) {
            return PageMap.empty();
        }

        List<RankRespVO> data = list.stream()
                .map(UserConverter.INSTANCE::do2RankVO)
                .collect(Collectors.toList());

        // 设置排名
        int startRank = (pageQuery.getPage() - 1) * pageQuery.getPageSize();
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setRankNum(startRank + i + 1);
        }

        return PageMap.data(page.getTotal(), data);
    }

    @Scheduled(fixedRate = 3600000) // 每小时执行一次
    public void refreshRankingCache() {
        // 从数据库获取所有用户数据
        List<UserDO> users = userService.list(
                Wrappers.<UserDO>lambdaQuery()
                        .ne(UserDO::getId, 1)
                        .orderByDesc(UserDO::getRating)
        );

        // 清除旧数据
        redisTemplate.delete(RANKING_KEY);
        redisTemplate.delete(TOTAL_COUNT_KEY);

        // 重建Redis缓存
        if (!users.isEmpty()) {
            Map<String, Double> scoreMembers = new HashMap<>();
            users.forEach(user ->
                    scoreMembers.put(String.valueOf(user.getId()), user.getRating().doubleValue())
            );
            redisTemplate.opsForZSet().add(RANKING_KEY, (Set<ZSetOperations.TypedTuple<Object>>) scoreMembers);
            redisTemplate.opsForValue().set(TOTAL_COUNT_KEY, users.size(), CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            redisTemplate.expire(RANKING_KEY, CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
        }
    }

    private List<RankRespVO> convertToRankRespVO(Set<ZSetOperations.TypedTuple<Object>> rangeWithScores, int startRank) {
        if (rangeWithScores == null) {
            return Collections.emptyList();
        }

        List<RankRespVO> result = new ArrayList<>();
        int rank = startRank + 1;

        for (ZSetOperations.TypedTuple<Object> tuple : rangeWithScores) {
            Integer userId = (Integer) tuple.getValue();
            UserDO user = userService.getById(userId);
            if (user != null) {
                RankRespVO rankVO = UserConverter.INSTANCE.do2RankVO(user);
                rankVO.setRankNum(rank++);
                result.add(rankVO);
            }
        }

        return result;
    }

    // 更新用户评分
    public void updateUserRating(Integer userId, Integer newRating) {
        // 更新数据库
        userService.update(
                Wrappers.<UserDO>lambdaUpdate()
                        .eq(UserDO::getId, userId)
                        .set(UserDO::getRating, newRating)
        );

        // 更新Redis排行榜
        redisTemplate.opsForZSet().add(RANKING_KEY, userId, newRating);
    }

    // ELO评分计算
    public int calculateNewRating(int playerRating, int opponentRating, boolean won) {
        double expectedScore = 1 / (1 + Math.pow(10, (opponentRating - playerRating) / 400.0));
        double actualScore = won ? 1.0 : 0.0;
        return (int) Math.round(playerRating + 32 * (actualScore - expectedScore));
    }
}
