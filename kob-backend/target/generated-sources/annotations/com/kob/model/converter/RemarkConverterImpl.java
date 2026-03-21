package com.kob.model.converter;

import com.kob.model.entity.Remark;
import com.kob.model.vo.request.RemarkReqVO;
import com.kob.model.vo.response.RemarkRespVO;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-21T21:21:39+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 1.8.0_442 (Amazon.com Inc.)"
)
public class RemarkConverterImpl implements RemarkConverter {

    @Override
    public Remark vo2do(RemarkReqVO remarkReqVO) {
        if ( remarkReqVO == null ) {
            return null;
        }

        Remark remark = new Remark();

        remark.setId( remarkReqVO.getId() );
        remark.setUserId( remarkReqVO.getUserId() );
        remark.setRemark( remarkReqVO.getRemark() );
        remark.setParentId( remarkReqVO.getParentId() );
        remark.setCreateTime( remarkReqVO.getCreateTime() );

        return remark;
    }

    @Override
    public RemarkRespVO do2vo(Remark remarkDO) {
        if ( remarkDO == null ) {
            return null;
        }

        RemarkRespVO remarkRespVO = new RemarkRespVO();

        remarkRespVO.setId( remarkDO.getId() );
        remarkRespVO.setUserId( remarkDO.getUserId() );
        remarkRespVO.setRemark( remarkDO.getRemark() );
        remarkRespVO.setParentId( remarkDO.getParentId() );
        remarkRespVO.setCreateTime( remarkDO.getCreateTime() );

        return remarkRespVO;
    }
}
