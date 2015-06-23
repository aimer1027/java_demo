package org.kylin.netty;

import io.netty.channel.ChannelHandler.Sharable ;
import io.netty.channel.ChannelHandlerAdapter ;
import io.netty.channel.ChannelHandlerContext ;


/**
 * Echoes back any received data from a client
 */

@Sharable
public class ServerHandler extends ChannelHandlerAdapter
{
    @Override
    public void channelRead( ChannelHandlerContext ctx , Object msg )
    {
        ctx.write(msg) ;
    }

    @Override
    public void channelReadComplete ( ChannelHandlerContext ctx )
    {
        ctx.flush () ;
    }

    @Override
    public void exceptionCaught ( ChannelHandlerContext ctx , Throwable cause)
    {
        // close the connection when an exception
        cause.printStackTrace();
        ctx.close () ;
    }

}



















