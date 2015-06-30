package org.kylin.zhang.netty.message;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap ;
import io.netty.buffer.ByteBuf ;
import io.netty.channel.ChannelHandlerAdapter ;
import io.netty.channel.ChannelFuture ;
import io.netty.channel.ChannelInitializer ;
import io.netty.channel.EventLoopGroup ;
import io.netty.channel.nio.NioEventLoopGroup ;
import io.netty.channel.ChannelOption ;
import io.netty.channel.socket.SocketChannel ;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel ;
import io.netty.channel.socket.nio.NioSocketChannel ;


/**
 * Created by root on 6/30/15.
 */



public class Server
{
    private String HOST ;
    private int    PORT ;

    public Server ( String host , int port )
    {
        this.HOST = host ;
        this.PORT = port ;
    }

    public void run () throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup () ;
        EventLoopGroup workerGroup = new NioEventLoopGroup () ;

        try
        {
            ServerBootstrap b = new ServerBootstrap () ;

            b.group (bossGroup , workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel( SocketChannel ch ) throws Exception
                        {
                            ch.pipeline().addLast (new MessageEncoder() , new ServerHandler()) ;
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true) ;

            // bind and start to accept incomming connections

            ChannelFuture f = b.bind(this.HOST , this.PORT).sync() ;

            // wait until the server socket is closed
            // in this example
            f.channel().closeFuture().sync()  ;


        }
        finally
        {
            workerGroup.shutdownGracefully() ;
            bossGroup.shutdownGracefully() ;
        }

    }

    public void ConnectOtherServer ( String hostname , int port )
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
          //  f.channel().closeFuture().sync() ;
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

    // here we write a main to test the if the server can connect ot its own

    public static void main ( String [] args ) throws Exception
    {
        String host1 = "aimer" ;
        int    port1 = 1027 ;



        Server server1 = new Server(host1 , port1 ) ;

        System.out.println ("the server1 ready to run") ;
        server1.run() ;
        //server1.ConnectOtherServer(host1, port1);


    }
}
