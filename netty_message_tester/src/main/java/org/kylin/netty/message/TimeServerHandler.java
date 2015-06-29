package org.kylin.netty.message;

import io.netty.channel.ChannelHandlerAdapter ;
import io.netty.channel.ChannelHandlerContext ;
import io.netty.channel.ChannelFuture ;
import io.netty.channel.ChannelFutureListener ;
import io.netty.buffer.ByteBuf ;

/**
 * Created by root on 6/29/15.
 */
public class TimeServerHandler extends ChannelHandlerAdapter
{
    @Override
    public void channelActive ( final ChannelHandlerContext ctx )
    {
        System.out.println ("server handler channel active") ;

        ChannelFuture f = ctx.writeAndFlush( new UnixTime()) ;

        f.addListener(ChannelFutureListener.CLOSE) ;
    }

    @Override
    public void exceptionCaught ( ChannelHandlerContext ctx, Throwable cause )
    {
        cause.printStackTrace () ;
        ctx.close() ;
    }
}
