package kylin.netty.example;

import io.netty.bootstrap.Bootstrap ;
import io.netty.channel.ChannelFuture ;
import io.netty.channel.ChannelInitializer ;
import io.netty.channel.ChannelOption ;
import io.netty.channel.ChannelPipeline ;
import io.netty.channel.EventLoopGroup ;
import io.netty.channel.nio.NioEventLoopGroup ;
import io.netty.channel.socket.SocketChannel ;
import io.netty.channel.socket.nio.NioSocketChannel ;


/**
 * Created by root on 6/22/15.
 *
 * Sends one message when a connection is open and echoes
 * back any received data to the server.
 * simply put , the echo client initiates the ping-pong traffic
 * between the echo client and server by sending the first
 * message to server.
 */
public class Client
{
    static  final String HOST = System.getProperty("aimer" , "10.2.0.27") ;

    static final int PORT = Integer.parseInt (System.getProperty("port" , "8080")) ;

    static final  int SIZE = Integer.parseInt(System.getProperty("size", "256")) ;

    public static void main ( String [] args ) throws Exception
    {
        // Configure the client
        EventLoopGroup group = new NioEventLoopGroup () ;

        try
        {
            Bootstrap b = new Bootstrap () ;

            b.group(group)
              .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY , true)
                    .handler(new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel ( SocketChannel ch ) throws Exception
                        {
                            ChannelPipeline p = ch.pipeline () ;

                            System.out.println ("client run ") ;
                              p.addLast ( new ClientHandler()) ;
                        }

                    }) ;

                    ChannelFuture f = b.connect( HOST , PORT).sync() ;
                    f.channel().closeFuture().sync () ;
        }
        finally
        {
            // shutdown the event loop to terminate all threads
            group.shutdownGracefully () ;
        }
    }
}
