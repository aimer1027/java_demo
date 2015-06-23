package kylin.netty.example;

import io.netty.bootstrap.ServerBootstrap ;

import io.netty.channel.ChannelFuture ;
import io.netty.channel.ChannelInitializer ;
import io.netty.channel.ChannelOption ;
import io.netty.channel.EventLoopGroup ;
import io.netty.channel.nio.NioEventLoopGroup ;
import io.netty.channel.socket.SocketChannel ;
import io.netty.channel.socket.nio.NioServerSocketChannel ;


/**
 * discard any incoming data .
 */
public class DiscardServer
{
     private int port ;
     private String ip ;

    public DiscardServer ( String ip ,  int port )
    {
        this.port = port ;
        this.ip = ip ;
    }

    public void run () throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup () ;
        EventLoopGroup workerGroup = new NioEventLoopGroup () ;

        try
        {
            ServerBootstrap b = new ServerBootstrap  ();

            b.group( bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler( new ChannelInitializer<SocketChannel>(){

                    @Override
                    public void initChannel ( SocketChannel ch ) throws Exception
                    {
                        ch.pipeline().addLast ( new DiscardServerHandler ()) ;
                    }

                    }).option(ChannelOption.SO_BACKLOG , 128)
            .childOption( ChannelOption.SO_KEEPALIVE, true );

            // bind and start to accept incoming connections
            ChannelFuture f = b.bind (ip ,port).sync () ;

            System.out.println ("server run") ;

            // wait until the server socket is closed
            // in this example , this does no thappend ,
            // but you can do that to gracefully
            // shutdown your server
            f.channel().closeFuture().sync () ;
        }
        finally
        {
            workerGroup.shutdownGracefully() ;
            bossGroup.shutdownGracefully() ;
        }
    }

    public static void main ( String [] args ) throws Exception
    {
        int port = 8080 ;
        String ip = "10.2.0.27" ;


        new DiscardServer( ip , port  ).run() ;
    }
}
