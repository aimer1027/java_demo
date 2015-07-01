package org.kylin.zhang.server;

import org.kylin.zhang.common.* ;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel ;
import org.jboss.netty.channel.ChannelPipeline ;
import org.jboss.netty.channel.ChannelPipelineFactory ;
import org.jboss.netty.channel.Channels ;
import org.jboss.netty.channel.ServerChannelFactory ;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory ;

import java.net.InetSocketAddress ;
import java.util.concurrent.Executors ;


/**
 * Created by root on 7/1/15.
 */
public class Server {

    private final String host ;
    private final int    port ;
    private DefaultChannelGroup  channelGroup ;
    private ServerChannelFactory serverFactory ;



    public Server ( String host , int port )
    {
        this.host = host ;
        this.port = port ;

    }

    public boolean start ()
    {
        this.serverFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()) ;

        this.channelGroup = new DefaultChannelGroup (this + "-channelGroup") ;

        ChannelPipelineFactory pipelineFactory = new ChannelPipelineFactory ()
        {
            public ChannelPipeline getPipeline() throws Exception
            {
                ChannelPipeline pipeline = Channels.pipeline() ;
                pipeline.addLast ("encoder" , MessageEncoder.getInstance()) ;
                pipeline.addLast("decoder" , new MessageDecoder() ) ;
                pipeline.addLast("handler" , new ServerHandler(channelGroup)) ;

                return pipeline ;
            }
        } ;

        ServerBootstrap bootstrap = new ServerBootstrap(this.serverFactory) ;
        bootstrap.setOption("reuseAddress" , true) ;
        bootstrap.setOption ("child.tcpNoDelay" , true ) ;
        bootstrap.setOption ("child.keepAlive" , true ) ;
        bootstrap.setPipelineFactory(pipelineFactory) ;


        Channel channel = bootstrap.bind( new InetSocketAddress(this.host, this.port)) ;

        if ( !channel.isBound())
        {
            this.stop() ;
            return false ;
        }

        this.channelGroup.add(channel) ;
        return true ;
    }

    public void stop ()
    {
        if (this.channelGroup != null )
            this.channelGroup.close () ;
        if ( this.serverFactory != null )
            this.serverFactory.releaseExternalResources();
    }


    public static void main ( String [] args )
    {
        final Server server = new Server("kylin", 9999 ) ;

        if ( !server.start ())
        {
            System.out.println("server failed to run ") ;
            System.exit(-1);

            return ; // not really needed
        }

        System.out.println("server started ..... ") ;

        Runtime.getRuntime().addShutdownHook( new Thread ()
        {
            @Override
            public void run ()
            {
                server.stop() ;
            }
        });
    }


}
