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

import java.io.* ;
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


    private String file_path ;
    private String file_name ;
    private long   file_len  ;
    private BufferedReader input ;

    public Client ( String host , int port )
    {
        this.host = host ;
        this.port = port ;

        // default received stored path
        // make sure its existances before running the program

        this.file_path = "/tmp/data/" ;
    }



    public void messageReceived (Message message)
    {
            // print out message

        System.out.println("message received from server "+ message ) ;
        // after receive message , create a new file and write the message content  into it


        this.channelGroup.disconnect() ;

        // in this branch , we first get the message type and then if else if it
        // by different kinds of messages , we use different methods
        // if - READY_SEND_FILE ---> we extract file name , and file len
        // as static member values, and create the file at target path
        // get its InputStream object , ready to write , and return the 'READY_RECV_FILE'
        // message to the server

        // if - FILE_SENDING ----> we extract file contents and write each line
        // into the opened file

        // if -- FILE_END ---> we write the last block of the message data , then
        // we flush and then close the file



        MessageType recv_msg_type = message.getType() ;

        if ( recv_msg_type == MessageType.READY_SEND_FILE)
        {
           String data  = new String (message.getData()) ;

            file_name = data.substring( data.indexOf(':')) ;


            this.file_len =  Long.decode(data.substring(0 , data.lastIndexOf(':')))  ;

            System.out.println ("we got file_name : "+ this.file_name ) ;
            System.out.println ("we got file_len : "+this.file_len ) ;

            // we create file and then open it , and then , create response message to server

            File  file = new File( this.file_path+this.file_name ) ;

            if ( file.exists())
                file.delete() ;

            try
            {
                file.createNewFile();
                this.input = new BufferedReader( new FileReader( file )) ;
            }
            catch (Exception e )
            {}

            // here we create message of type of READY_RECV_FILE

             data = "client get ready" ;
            Message response_msg = new Message(MessageType.READY_RECV_FILE , (short)(3+data.getBytes().length),
                    data.getBytes()) ;

            this.handler.sendMessage(response_msg) ;
        }

        if ( recv_msg_type == MessageType.FILE_SENDING)
        {}

        if ( recv_msg_type == MessageType.FILE_END)
        {}




        /*

        /// runnin only once
        {
            String data = "KyLin_Zhang" ;

            Message msg = new Message(MessageType.FILE_SENDING ,(short)(data.getBytes().length+3), data.getBytes()) ;
             System.out.println("client messageReceived :" + msg ) ;

            this.handler.sendMessage(msg);

        }
        */
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
        final Client  client = new Client("kylin", 9999) ;



            if (!client.start())
            {
                System.out.println("client failed to start");
                System.exit(-1);
                return;
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

