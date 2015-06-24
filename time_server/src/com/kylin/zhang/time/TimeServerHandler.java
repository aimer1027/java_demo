package com.kylin.zhang.time;

import io.netty.buffer.ByteBuf ;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter ;
import io.netty.channel.ChannelHandlerContext ;
import io.netty.channel.ChannelFuture ;
/**
 * Created by root on 6/24/15.
 */
public class TimeServerHandler extends ChannelHandlerAdapter
{
    @Override
    public void channelActive ( final ChannelHandlerContext ctx )
    {
        final ByteBuf time = ctx.alloc().buffer(4) ;
        time.writeInt ((int)(System.currentTimeMillis() / 1000L + 2208988800L)) ;

        final ChannelFuture f = ctx.writeAndFlush(time) ;
        f.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                assert f == channelFuture ;
                ctx.close ( ) ;
            }
        })  ;

    }

    @Override
    public void exceptionCaught ( ChannelHandlerContext ctx, Throwable cause)
    {
        cause.printStackTrace(); ;
        ctx.close () ;
    }
}
