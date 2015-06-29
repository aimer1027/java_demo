package org.kylin.netty.message;

import java.util.Date ;

import io.netty.channel.ChannelHandlerAdapter ;
import io.netty.channel.ChannelHandlerContext ;
import io.netty.buffer.ByteBuf ;

/**
 * Created by root on 6/29/15.
 */
public class TimeClientHandler extends ChannelHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        UnixTime m = (UnixTime)msg ;

         System.out.println (" channel Read "+m) ;

        ctx.close() ;
    }

    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace();
        ctx.close () ;
    }
}
