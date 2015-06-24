package com.kylin.zhang.time;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext ;
import io.netty.buffer.ByteBuf ;
import io.netty.buffer.ByteBufAllocator ;

import java.util.Date ;

/**
 * Created by root on 6/24/15.
 */
public class TimeClientHandler  extends ChannelHandlerAdapter
{
    @Override
    public void channelRead ( ChannelHandlerContext ctx, Object msg )
    {
        ByteBuf m = (ByteBuf) msg ;

        try
        {
            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L)*1000L ;
            System.out.println ( new Date(currentTimeMillis)) ;
            ctx.close () ;
        }
        finally
        {
            m.release () ;
        }
    }

    @Override
    public void exceptionCaught ( ChannelHandlerContext ctx, Throwable cause )
    {
        cause.printStackTrace();
        ctx.close () ;
    }
}
