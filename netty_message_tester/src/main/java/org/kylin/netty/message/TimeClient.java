package org.kylin.netty.message;

import io.netty.channel.EventLoopGroup ;
import io.netty.bootstrap.Bootstrap ;
import io.netty.channel.ChannelInitializer ;
import io.netty.channel.ChannelOption ;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel ;
import io.netty.channel.socket.SocketChannel ;
import io.netty.channel.ChannelFuture ;


/**
 * Created by root on 6/29/15.
 */
public class TimeClient
{
     public static void main ( String [] args ) throws Exception
     {
         String host = "kylin" ;
         int    port = 1025 ;
        EventLoopGroup workerGroup = new NioEventLoopGroup() ;

         try
         {
             Bootstrap b = new Bootstrap () ;
             b.group(workerGroup) ;
             b.channel( NioSocketChannel.class) ;
             b.option(ChannelOption.SO_KEEPALIVE , true ) ;
             b.handler( new ChannelInitializer<SocketChannel>()
             {
                 @Override
                 public void initChannel( SocketChannel ch) throws Exception
                 {

                     ch.pipeline().addLast(new TimeDecoder(),new TimeClientHandler()) ;
                 }
             }) ;


             // here we start the client
             ChannelFuture f = b.connect(host,port).sync() ;

             // wait until the connection is closed
             f.channel().closeFuture().sync() ;
         }
         finally
         {
             workerGroup.shutdownGracefully () ;
         }

     }
}
