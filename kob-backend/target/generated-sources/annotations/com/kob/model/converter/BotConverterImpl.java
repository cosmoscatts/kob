package com.kob.model.converter;

import com.kob.model.entity.Bot;
import com.kob.model.vo.request.BotReqVO;
import com.kob.model.vo.response.BotRespVO;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-21T21:21:39+0800",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 1.8.0_442 (Amazon.com Inc.)"
)
public class BotConverterImpl implements BotConverter {

    @Override
    public BotRespVO do2vo(Bot bot) {
        if ( bot == null ) {
            return null;
        }

        BotRespVO botRespVO = new BotRespVO();

        botRespVO.setId( bot.getId() );
        botRespVO.setUserId( bot.getUserId() );
        botRespVO.setTitle( bot.getTitle() );
        botRespVO.setDescription( bot.getDescription() );
        botRespVO.setContent( bot.getContent() );
        botRespVO.setCreateTime( bot.getCreateTime() );
        botRespVO.setModifyTime( bot.getModifyTime() );

        return botRespVO;
    }

    @Override
    public Bot vo2do(BotReqVO botReqVO) {
        if ( botReqVO == null ) {
            return null;
        }

        Bot bot = new Bot();

        bot.setId( botReqVO.getId() );
        bot.setUserId( botReqVO.getUserId() );
        bot.setTitle( botReqVO.getTitle() );
        bot.setDescription( botReqVO.getDescription() );
        bot.setContent( botReqVO.getContent() );
        bot.setCreateTime( botReqVO.getCreateTime() );
        bot.setModifyTime( botReqVO.getModifyTime() );

        return bot;
    }
}
