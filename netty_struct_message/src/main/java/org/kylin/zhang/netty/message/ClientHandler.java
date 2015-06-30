package org.kylin.zhang.netty.message;

import io.netty.channel.ChannelHandlerAdapter ;
import io.netty.channel.ChannelHandlerContext ;


/**
 * Created by root on 6/30/15.
 */

public class ClientHandler extends ChannelHandlerAdapter
{
    @Override
    public void channelRead( ChannelHandlerContext ctx , Object msg )
    {
        System.out.println ("in ClientHandler channelRead method ") ;
        Message message = (Message) msg ;

        System.out.println (" got message " + message ) ;

        ctx.close () ;
        // here i should add the switch to justify the kind of different messages
        // and by different messages i choice whether receive the data
        // or close or continue the connection
    }

    @Override
    public void exceptionCaught ( ChannelHandlerContext ctx, Throwable cause )
    {
        cause.printStackTrace();
        ctx.close () ;
    }
}
