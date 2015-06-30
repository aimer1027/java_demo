package org.kylin.zhang.netty.message;

import io.netty.channel.EventLoopGroup ;
import io.netty.bootstrap.Bootstrap ;
import io.netty.channel.ChannelInitializer ;
import io.netty.channel.ChannelOption ;
import io.netty.channel.nio.NioEventLoopGroup ;
import io.netty.channel.socket.nio.NioSocketChannel ;
import io.netty.channel.socket.SocketChannel ;
import io.netty.channel.ChannelFuture ;


/**
 * Created by root on 6/30/15.
 */

public class Client
{
    public static  void ConnectOtherServer ( String hostname , int port )
    {
        EventLoopGroup wGroup = new NioEventLoopGroup () ;

        try
        {
            Bootstrap b = new Bootstrap () ;
            b.group(wGroup) ;
            b.channel(NioSocketChannel.class) ;
            b.option(ChannelOption.SO_KEEPALIVE , true ) ;
            b.handler( new ChannelInitializer<SocketChannel>()
            {
                @Override
                public void initChannel ( SocketChannel ch ) throws Exception
                {
                    System.out.println ("init channel ") ;
                    ch.pipeline().addLast( new MessageDecoder() , new ClientHandler()) ;
                }
            }) ;

            // here we start the client
            System.out.println(" connect to server ") ;
            ChannelFuture f = b.connect (hostname , port).sync() ;

            // wait until the connection is closed
            f.channel().closeFuture().sync() ;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        finally
        {
            wGroup.shutdownGracefully () ;
        }

    }

    public static void main ( String args [] ) throws Exception
    {
        String hostname = "aimer" ;
        int port = 1027 ;

        Client.ConnectOtherServer(hostname , port );
    }

}
