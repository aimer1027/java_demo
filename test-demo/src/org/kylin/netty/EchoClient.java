package org.kylin.netty;

import io.netty.bootstrap.Bootstrap ;
import io.netty.channel.ChannelFuture ;
import io.netty.channel.ChannelInitializer ;
import io.netty.channel.ChannelOption ;
import io.netty.channel.ChannelPipeline ;
import io.netty.channel.EventLoopGroup ;
import io.netty.channel.nio.NioEventLoopGroup ;
import io.netty.channel.socket.SocketChannel ;
import io.netty.channel.socket.nio.NioSocketChannel ;
import io.netty.handler.ssl.SslContext ;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory ;


/**
 * Sends one message when a connection is open and echoes back
 * any received data to the server. Simply put , the echo client
 * initiates the ping-pong traffic  between the echo client
 * and server by sending the first message to the server
 */
public final class EchoClient
{

    static final String HOST = System.getProperty("host" , "10.2.0.27") ;

    static final int PORT = Integer.parseInt (System.getProperty("port" , "8807")) ;

    static final int SIZE = Integer.parseInt( System.getProperty("size" , "256")) ;

    public static void main ( String [] args ) throws Exception
    {
        // configure ssl.git


        // Configure the client
        EventLoopGroup group = new NioEventLoopGroup () ;

        try {
            Bootstrap b = new Bootstrap() ;

            b.group( group )
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true )
                    .handler(
                            new ChannelInitializer<SocketChannel>()
                            {
                                @Override
                               public void initChannel( SocketChannel ch ) throws Exception
                                {
                                    ChannelPipeline p = ch.pipeline () ;
                                    p.addLast( new ClientHandler()) ;

                                }
                            }

                    );

            // start the client
            ChannelFuture f = b.connect(HOST, PORT).sync() ;

            // wait until the connection is closed
            f.channel().closeFuture().sync() ;
        }
        finally
        {
            // shutdown the event loop to terminate all threads
            group.shutdownGracefully() ;
        }













    }
}
