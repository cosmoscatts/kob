package com.kob.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.model.entity.Record;

@Mapper
@Repository
public interface RecordMapper extends BaseMapper<Record> {}
