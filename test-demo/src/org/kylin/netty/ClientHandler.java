package org.kylin.netty;

import io.netty.buffer.ByteBuf ;
import io.netty.buffer.Unpooled ;
import io.netty.channel.ChannelHandlerAdapter ;
import io.netty.channel.ChannelHandlerContext ;


/**
 * Handler implementation for the echo client .
 * It initiates the ping-pong traffic between the
 * echo client and server by sending the first message
 * to the server .
 */
public class ClientHandler extends ChannelHandlerAdapter
{
  private final ByteBuf firstMessage ;


    // creates a client-side handler
    public ClientHandler ()
    {
        firstMessage = Unpooled.buffer( EchoClient.SIZE) ;

        for ( int i = 0 ; i < firstMessage.capacity() ; i++ )
        {
            firstMessage.writeByte((byte) i)  ;
        }
    }

    @Override
    public void channelActive ( ChannelHandlerContext ctx )
    {
        ctx.writeAndFlush( firstMessage ) ;
    }

    @Override
    public void channelRead( ChannelHandlerContext ctx, Object msg )
    {
        ctx.write(msg) ;
    }

    @Override
    public void channelReadComplete ( ChannelHandlerContext ctx )
    {
        ctx.flush () ;
    }


    @Override
    public void exceptionCaught ( ChannelHandlerContext ctx, Throwable cause)
    {
        // close the connection when an exception is raised
        cause.printStackTrace();
        ctx.close () ;
    }
}
