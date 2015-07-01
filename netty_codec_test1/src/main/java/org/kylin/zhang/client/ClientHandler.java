package org.kylin.zhang.client;

import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup ;

import org.kylin.zhang.common.* ;

import java.io.*;

/**
 * Created by root on 7/1/15.
 */
public class ClientHandler extends SimpleChannelUpstreamHandler
{

    private final ClientHandlerListener listener  ;
    private final ChannelGroup channelGroup ;
    private Channel channel ;

    private String file_path ;
    private String file_name ;
    private long   file_len  ;
    private BufferedWriter output ;


    public ClientHandler(ClientHandlerListener listener , ChannelGroup  channelGroup)
    {
        this.listener = listener ;
        this.channelGroup = channelGroup ;

        this.file_path = "/home/" ;
    }

    @Override
    public void messageReceived ( ChannelHandlerContext ctx, MessageEvent e)
        throws Exception
    {
        if ( e.getMessage () instanceof  Message )
        {
            // output this
          //  this.listener.messageReceived((Message)e.getMessage()) ;

            Message message = (Message)e.getMessage() ;

            // print out message

       //     System.out.println("message received from server "+ message ) ;
            // after receive message , create a new file and write the message content  into it


       //     this.channelGroup.disconnect() ;

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

                file_name = data.substring( data.indexOf(':')+1) ;


                this.file_len =  Long.decode(data.substring(0 , data.lastIndexOf(':')))  ;

                System.out.println ("we got file_name : "+ this.file_name ) ;
                System.out.println ("we got file_len : "+this.file_len ) ;

                // we create file and then open it , and then , create response message to server

                File file = new File( this.file_path+this.file_name ) ;

                if ( file.exists())
                    file.delete() ;

                try
                {
                    file.createNewFile();
                    this.output = new BufferedWriter( new FileWriter( file )) ;
                }
                catch (Exception ex )
                {}

                // here we create message of type of READY_RECV_FILE

                data = "client get ready" ;
                Message response_msg = new Message(MessageType.READY_RECV_FILE , (short)(3+data.getBytes().length),
                        data.getBytes()) ;
                this.channel.write(response_msg) ;
            }

            if ( recv_msg_type == MessageType.FILE_SENDING)
            {
                String data  = new String (message.getData()) ;
                this.output.newLine();
                this.output.write(data);
                this.output.flush();

            }

            if ( recv_msg_type == MessageType.FILE_END)
            {
                String data = new String (message.getData()) ;

                this.output.newLine();
                this.output.write(data) ;
                this.output.flush() ;

                this.output.close() ;

                // create message with type of SHUT_DOWN , after server received this it will
                // disconnect the channel

                data = "client gonning to shut down" ;
                Message msg = new Message (MessageType.SHUT_DOWN , (short)(3+data.getBytes().length) ,
                        data.getBytes()) ;

                e.getChannel().write(msg) ;

                try
                {
                    System.out.println("client going to shut down ......") ;
                    Thread.sleep(4000);
                    e.getChannel().disconnect() ;
                }
                catch ( Exception ex )
                {}
            }

        }
        else
        {
            super.messageReceived(ctx, e ) ;
        }

    }

    @Override
    public void channelConnected( ChannelHandlerContext ctx, ChannelStateEvent e)
        throws Exception
    {
        System.out.println("client channelConnected: ") ;
        this.channel = e.getChannel();



        this.channelGroup.add(e.getChannel()) ;


      /**
       *  here i will write a message sending to the server
       *  tell it a connection is comming
       *

        String data = "test1" ; // need read file name

        Message msg = new Message(MessageType.FILE_BEGIN ,(short)(data.getBytes().length+3), data.getBytes()) ;

        System.out.println("sendConnectMessage : "+ msg ) ;
        this.channel.write( msg ) ;
        */
    }




    public void sendMessage ( Message msg )
    {
        if ( this.channel != null )
        {
            System.out.println("sendMessage : "+ msg ) ;
            this.channel.write(msg) ;

        }
    }
}
