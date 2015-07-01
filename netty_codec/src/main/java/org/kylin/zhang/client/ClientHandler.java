package org.kylin.zhang.client;

import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup ;

import org.kylin.zhang.common.* ;

/**
 * Created by root on 7/1/15.
 */
public class ClientHandler extends SimpleChannelUpstreamHandler
{

    private final ClientHandlerListener listener  ;
    private final ChannelGroup channelGroup ;
    private Channel channel ;


    public ClientHandler(ClientHandlerListener listener , ChannelGroup  channelGroup)
    {
        this.listener = listener ;
        this.channelGroup = channelGroup ;
    }

    @Override
    public void messageReceived ( ChannelHandlerContext ctx, MessageEvent e)
        throws Exception
    {
        if ( e.getMessage () instanceof  Message )
        {
            // output this
            this.listener.messageReceived((Message)e.getMessage()) ;
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

        System.out.println ("is connected the channel ? "+ this.channel.isConnected()) ;

        this.channelGroup.add(e.getChannel()) ;


      /**
       *  here i will write a message sending to the server
       *  tell it a connection is comming
       *  */

        String data = "connected request" ;

        Message msg = new Message(MessageType.FILE_SENDING ,(short)(data.getBytes().length+3), data.getBytes()) ;
        System.out.println("client flood :" + msg ) ;


        System.out.println("sendConnectMessage : "+ msg ) ;
        this.channel.write( msg ) ;

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
