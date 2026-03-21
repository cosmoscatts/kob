package com.kob.model.converter;

import com.kob.model.entity.Record;
import com.kob.model.vo.response.RecordRespVO;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-21T21:21:39+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 1.8.0_442 (Amazon.com Inc.)"
)
public class RecordConverterImpl implements RecordConverter {

    @Override
    public RecordRespVO do2vo(Record recordDO) {
        if ( recordDO == null ) {
            return null;
        }

        RecordRespVO recordRespVO = new RecordRespVO();

        recordRespVO.setId( recordDO.getId() );
        recordRespVO.setAId( recordDO.getAId() );
        recordRespVO.setASx( recordDO.getASx() );
        recordRespVO.setASy( recordDO.getASy() );
        recordRespVO.setBId( recordDO.getBId() );
        recordRespVO.setBSx( recordDO.getBSx() );
        recordRespVO.setBSy( recordDO.getBSy() );
        recordRespVO.setASteps( recordDO.getASteps() );
        recordRespVO.setBSteps( recordDO.getBSteps() );
        recordRespVO.setMap( recordDO.getMap() );
        recordRespVO.setLoser( recordDO.getLoser() );
        recordRespVO.setCreateTime( recordDO.getCreateTime() );
        recordRespVO.setMode( recordDO.getMode() );

        return recordRespVO;
    }
}
