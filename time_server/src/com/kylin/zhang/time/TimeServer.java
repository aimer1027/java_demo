package com.kylin.zhang.time;

import io.netty.bootstrap.ServerBootstrap ;
import io.netty.channel.ChannelFuture ;
import io.netty.channel.ChannelInitializer ;
import io.netty.channel.ChannelOption ;
import io.netty.channel.EventLoopGroup ;
import io.netty.channel.nio.NioEventLoopGroup ;
import io.netty.channel.socket.SocketChannel ;
import io.netty.channel.socket.nio.NioServerSocketChannel ;


// properties file

import com.kylin.zhang.time.PropertiesAccessor ;
import java.util.* ;
import java.io.* ;

/**
 * Created by root on 6/24/15.
 */
public class TimeServer
{
    private String ip ;
    private int port ;
    private String server_name ;


    public TimeServer ( String server_name )
    {
        this.server_name = server_name ;


        Properties prop = PropertiesAccessor.get_properties(PropertiesAccessor.conf_file_name) ;
        String zk_config_path =  prop.getProperty ( PropertiesAccessor.conf_file_key ) ;
System.out.println ("config file path "+ zk_config_path) ;

        try
        {
            String line;

            BufferedReader file_reader = new BufferedReader( new FileReader ( zk_config_path ))  ;

            while ( (line = file_reader.readLine()) != null )
            {
                System.out.println (line) ;

                if ( line.startsWith(server_name) )
                {
                    // we find this server's config information
                    String server_prefix = line.substring( 0 , line.indexOf('=')) ;
                    // here we got the server_name --> aimer

                    String hostname = line.substring(line.indexOf('=')+1 , line.indexOf(':'))  ;
                    String begin_port = line.substring ( line.indexOf(':')+1 , line.lastIndexOf(':')) ;
                    String end_port   = line.substring ( line.lastIndexOf(':')+1 , line.length() ) ;

                    this.ip = hostname ;
                    this.port = Integer.parseInt(begin_port) ;

                    // then we need write the corresponding information into the properties files
                    // which named after server_name ---> server_name.properties under ../config/ path

                    // we first create a file name
                    String server_prop_file = server_name+".properties" ;

                    // then we load all the configuration info corresponding to this server
                    //  to the Properties object
                    Properties p_loader = new Properties () ;

                    p_loader.setProperty(server_prefix+".hostname" , hostname) ;
                    p_loader.setProperty(server_prefix+".begin_port" , begin_port ) ;
                    p_loader.setProperty(server_prefix+".end_port" , end_port ) ;

                    // then we stores all the info from Properties into new created properties file
                    PropertiesAccessor.store_properties_file(p_loader , server_prop_file );
                }
            }

        }
        catch ( IOException e)
        {

        }
    }

    public void run () throws Exception
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup () ;
        EventLoopGroup workerGroup = new NioEventLoopGroup () ;

        try
        {
            ServerBootstrap b = new ServerBootstrap() ;
            b.group( bossGroup, workerGroup )
                    .channel(NioServerSocketChannel.class)
                    .childHandler( new ChannelInitializer<SocketChannel>()
                    {
                        @Override
                        public void initChannel( SocketChannel ch) throws Exception {
                            ch.pipeline().addLast( new TimeServerHandler() );
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true );

            // Bind and start to accept incoming connections
            ChannelFuture f = b.bind (ip, port).sync() ;
            f.channel().closeFuture().sync() ;
        }
        finally
        {
            workerGroup.shutdownGracefully ( ) ;
            bossGroup.shutdownGracefully() ;
        }
    }

    public static void main ( String [] args ) throws Exception
    {
        int port  = 1027 ;
        String ip ="kylin";
        String server_name = "server.1" ;

        new TimeServer(server_name ).run() ;


    }
}
