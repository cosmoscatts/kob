package com.kob.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    List<User> selectUserWithRank(@Param("name") String name,
                                  @Param("offset") int offset,
                                  @Param("size") int size);
}
