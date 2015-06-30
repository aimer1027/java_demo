package org.kylin.zhang.netty.message;

import io.netty.channel.ChannelHandlerAdapter ;
import io.netty.channel.ChannelHandlerContext ;
import io.netty.channel.ChannelFuture ;
import io.netty.channel.ChannelFutureListener ;
import io.netty.buffer.ByteBuf ;

/**
 * Created by root on 6/30/15.
 */

public class ServerHandler extends ChannelHandlerAdapter
{
    @Override
    public void channelActive ( final ChannelHandlerContext ctx )
    {
        System.out.println ("server handler channel is active") ;

        Message msg = new Message ( (char)(MessageType.FILE_FINISHED +'0'), "Hello_Aimer".getBytes() ) ;

        System.out.println ("send message "+ msg +"to connector") ;
        ChannelFuture f = ctx.writeAndFlush ( msg ) ;

        f.addListener(ChannelFutureListener.CLOSE) ;
    }

    @Override
    public void exceptionCaught ( ChannelHandlerContext ctx , Throwable cause )
    {
        cause.printStackTrace();
        ctx.close() ;
    }
}
