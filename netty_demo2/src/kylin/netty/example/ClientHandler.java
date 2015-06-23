package kylin.netty.example;

import io.netty.buffer.ByteBuf ;
import io.netty.buffer.Unpooled ;
import io.netty.channel.ChannelHandlerAdapter ;
import io.netty.channel.ChannelHandlerContext ;

/**
 * Created by root on 6/22/15.
 *
 * Handler implementation for the echo client.
 * It initiates the ping-pong traffic between the echo client
 * and server by sending the first message to the server .
 */
public class ClientHandler extends  ChannelHandlerAdapter
{
    private final ByteBuf firstMessage ;

    // create a client side handler

    public ClientHandler ()
    {
        firstMessage = Unpooled.buffer(Client.SIZE) ;

        for ( int i = 0 ; i < firstMessage.capacity(); i++ )
        {
            firstMessage.writeByte ((byte) i) ;
        }
    }

    @Override
    public void channelActive ( ChannelHandlerContext ctx )
    {
        System.out.println ("now i am in channelActive '") ;
        ctx.writeAndFlush ( firstMessage ) ;
    }

    @Override
    public void channelRead ( ChannelHandlerContext ctx, Object msg )
    {
        System.out.println ("client receive message "+ msg.toString()) ;
        ctx.write(msg ) ;
    }

    @Override
    public void channelReadComplete ( ChannelHandlerContext ctx )
    {
        System.out.println ("in method channelReadComplete ") ;
        ctx.flush () ;
    }

    @Override
    public void exceptionCaught ( ChannelHandlerContext ctx , Throwable cause )
    {
        // close the connection when an exception is raised
        cause.printStackTrace () ;
        ctx.close () ;
    }
}
