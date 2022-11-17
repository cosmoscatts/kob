package com.kob.backend.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.kob.backend.controller.remark.vo.RemarkReqVO;
import com.kob.backend.controller.remark.vo.RemarkRespVO;
import com.kob.backend.dataobject.RemarkDO;

@Mapper
public interface RemarkConverter {
    RemarkConverter INSTANCE = Mappers.getMapper(RemarkConverter.class);

    RemarkDO vo2do(RemarkReqVO remarkReqVO);

    RemarkRespVO do2vo(RemarkDO remarkDO);
}
