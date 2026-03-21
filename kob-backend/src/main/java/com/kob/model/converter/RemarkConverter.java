package com.kob.model.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.kob.model.vo.request.RemarkReqVO;
import com.kob.model.vo.response.RemarkRespVO;
import com.kob.model.entity.Remark;

@Mapper
public interface RemarkConverter {
    RemarkConverter INSTANCE = Mappers.getMapper(RemarkConverter.class);

    Remark vo2do(RemarkReqVO remarkReqVO);

    RemarkRespVO do2vo(Remark remarkDO);
}
