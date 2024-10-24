package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<UserDO> {
    @Select({"<script>",
            "SELECT u.*, ",
            "(SELECT COUNT(*) + 1 FROM user t WHERE t.rating > u.rating AND t.id != 1) as rank_num ",
            "FROM user u ",
            "WHERE u.id != 1 ",
            "<if test='name != null and name != \"\"'>",
            "AND u.name LIKE CONCAT('%', #{name}, '%') ",
            "</if>",
            "ORDER BY u.rating DESC ",
            "LIMIT #{offset}, #{size}",
            "</script>"})
    List<UserDO> selectUserWithRank(@Param("name") String name,
                                          @Param("offset") int offset,
                                          @Param("size") int size);
}
