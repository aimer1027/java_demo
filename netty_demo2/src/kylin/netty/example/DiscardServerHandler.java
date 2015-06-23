package kylin.netty.example;

import io.netty.buffer.ByteBuf ;

import io.netty.channel.ChannelHandlerContext ;
import io.netty.channel.ChannelHandlerAdapter ;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel
 */

public class DiscardServerHandler extends ChannelHandlerAdapter
{
    @Override
    public  void channelRead ( ChannelHandlerContext ctx, Object msg )
    {
        // discard the received data silently

        // ((ByteBuf)msg).release () ;

        System.out.println ("here come a connection from client ") ;

        ByteBuf in = (ByteBuf) msg ;
      /*  try {
            while ( in.isReadable () )
            {
                System.out.print ((char) in.readByte()) ;
                System.out.flush();
            }
        }

        finally
        {
            ReferenceCountUtil.release( msg ) ;
        }
        */

        ctx.write ( msg ) ;
        ctx.flush () ;

    }

    @Override
    public void exceptionCaught ( ChannelHandlerContext  ctx, Throwable cause)
    {
        // close the connection when an exception is raised

        cause.printStackTrace () ;
        ctx.close () ;
    }
}
