package org.kylin.netty.message;

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


/**
 * Created by root on 6/29/15.
 *
 * here we gonna to create a TimeServer
 */
public class TimeServer
{
    private String host ;
    private int port ;

    public TimeServer ( String host , int port )
    {
        this.host = host ;
        this.port = port ;
    }

    public void run () throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast(new TimeEncoder() , new TimeServerHandler());
                        }

                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // bind and start to accept incoming connections
            ChannelFuture f = b.bind(host, port).sync();

            // wait until the server socket is closed
            // in this example , this does not happen
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


        public static void main ( String args []) throws Exception
        {
            int port  = 1025;
            String host = "kylin" ;

            new TimeServer(host, port).run() ;
        }
    }





