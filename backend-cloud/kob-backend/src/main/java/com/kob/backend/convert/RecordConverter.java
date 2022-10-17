package com.kob.backend.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.kob.backend.controller.record.vo.RecordRespVO;
import com.kob.backend.dataobject.RecordDO;

@Mapper
public interface RecordConverter {
    RecordConverter INSTANCE = Mappers.getMapper(RecordConverter.class);

    RecordRespVO do2vo(RecordDO recordDO);
}
