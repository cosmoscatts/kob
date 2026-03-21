package com.kob.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.kob.model.vo.response.RecordRespVO;
import com.kob.model.entity.Record;

@Mapper
public interface RecordConverter {
    RecordConverter INSTANCE = Mappers.getMapper(RecordConverter.class);

    RecordRespVO do2vo(Record recordDO);
}
