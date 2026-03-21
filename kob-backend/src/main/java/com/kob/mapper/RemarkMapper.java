package com.kob.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.model.entity.Remark;

@Mapper
@Repository
public interface RemarkMapper extends BaseMapper<Remark> {}
