package org.kylin.zhang.client;

import org.kylin.zhang.common.* ;
import org.jboss.netty.bootstrap.ClientBootstrap ;
import org.jboss.netty.channel.ChannelFactory ;
import org.jboss.netty.channel.ChannelPipeline ;
import org.jboss.netty.channel.ChannelPipelineFactory ;
import org.jboss.netty.channel.Channels ;
import org.jboss.netty.channel.group.ChannelGroup ;
import org.jboss.netty.channel.group.DefaultChannelGroup ;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory ;

import java.net.InetSocketAddress ;
import java.util.concurrent.Executors ;
import java.util.concurrent.atomic.AtomicInteger ;

/**
 * Created by root on 7/1/15.
 */
public class Client implements ClientHandlerListener
{

    private final String     host ;
    private final int        port ;



    private ClientHandler  handler ;
    private ChannelFactory clientFactory ;
    private ChannelGroup   channelGroup ;



    public Client ( String host , int port )
    {
        this.host = host ;
        this.port = port ;

    }



    public void messageReceived (Message message)
    {
            // print out message

        System.out.println("message received from server "+ message ) ;

        System.out.println ("we send message again") ;

        /// runnin only once
        {
            String data = "KyLin_Zhang" ;

            Message msg = new Message(MessageType.FILE_SENDING ,(short)(data.getBytes().length+3), data.getBytes()) ;
             System.out.println("client messageReceived :" + msg ) ;

            this.handler.sendMessage(msg);

        }
    }

   public boolean start ()
   {
       this.clientFactory = new NioClientSocketChannelFactory(
               Executors.newCachedThreadPool(), Executors.newCachedThreadPool()) ;


        this.channelGroup = new DefaultChannelGroup(this + "-channelGroup") ;

        this.handler = new ClientHandler (this, this.channelGroup ) ;

       ChannelPipelineFactory pipelineFactory = new ChannelPipelineFactory() {
           public ChannelPipeline getPipeline() throws Exception {

               ChannelPipeline pipeline = Channels.pipeline() ;
               pipeline.addLast("encoder" , MessageEncoder.getInstance()) ;
               pipeline.addLast("decoder" , new MessageDecoder  ()) ;
               pipeline.addLast("handler" , handler) ;


               return pipeline ;
            }
       } ;

       ClientBootstrap bootstrap = new ClientBootstrap (this.clientFactory) ;

       bootstrap.setOption("reuseAddress" , true ) ;
       bootstrap.setOption ("tcpNoDealy" , true ) ;
       bootstrap.setOption ("keepAlive" , true ) ;
       bootstrap.setPipelineFactory(pipelineFactory ) ;


       boolean connected = bootstrap.connect( new InetSocketAddress(host, port ))
               .awaitUninterruptibly().isSuccess();

       if ( !connected)
       {
           this.stop() ;
       }

       return connected ;
   }

    public void stop ()
    {
        if ( this.channelGroup != null )
            this.channelGroup.close() ;
        if ( this.clientFactory != null )
            this.clientFactory.releaseExternalResources();
    }
    private void flood()
    {
        if ((this.channelGroup == null) || (this.clientFactory == null)) {
            System.out.println("do not have any resources, return ");
            return;
        }

         /// runnin only once
        {
            String data = "KyLin_Zhang" ;

            Message msg = new Message(MessageType.FILE_SENDING ,(short)(data.getBytes().length+3), data.getBytes()) ;
          //  System.out.println("client flood :" + msg ) ;

            this.handler.sendMessage(msg);

        }
    }

    public static void main ( String [] args ) throws InterruptedException
    {
        final Client client = new Client ("kylin" , 9999) ;

        if ( !client.start())
        {
            System.out.println ("client failed to start") ;
            System.exit(-1) ;
            return ;
        }

        System.out.println("Client started ....") ;

       // System.out.println("call flood") ;
     //   client.flood() ;

       Runtime.getRuntime().addShutdownHook(
               new Thread()
               {
                   @Override
                    public void run ()
                   {
                       client.stop () ;
                   }
               }) ;
    }
}
















