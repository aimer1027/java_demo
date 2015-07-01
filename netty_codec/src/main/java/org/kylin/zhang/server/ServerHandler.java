package org.kylin.zhang.server;

import org.kylin.zhang.common.* ;
import org.jboss.netty.channel.ChannelHandlerContext ;
import org.jboss.netty.channel.ChannelStateEvent ;
import org.jboss.netty.channel.MessageEvent ;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler ;
import org.jboss.netty.channel.group.ChannelGroup ;

/**
 * Created by root on 7/1/15.
 */
public class ServerHandler extends SimpleChannelUpstreamHandler
{
    private final ChannelGroup channelGroup ;

    public ServerHandler ( ChannelGroup channelGroup )
    {
        this.channelGroup = channelGroup ;
    }

    @Override
    public void channelConnected( ChannelHandlerContext ctx, ChannelStateEvent e )
    {
        System.out.println("-------------------------- **server gets a new connection**--------------------------") ;
        this.channelGroup.add(e.getChannel()) ;
    }

    @Override
    public void messageReceived ( ChannelHandlerContext ctx, MessageEvent e )
            throws Exception
    {
        System.out.println("-------------------- **server receive a piece of message** -------------------------- ") ;
        if ( e.getMessage() instanceof Message ) {
            Message received_msg = (Message) e.getMessage();
            System.out.println("server received message :" + received_msg);

            if ( new String (received_msg.getData()).equals("KyLin_Zhang")) {
                //how to shutdown connection
                System.out.println ("received end message "+ received_msg) ;
                this.channelGroup.disconnect() ;
            } else {

                String data = "Server Received Message ";

                Message msg = new Message(MessageType.FILE_END, (short) (data.getBytes().length + 3), data.getBytes());

                e.getChannel().write(msg);

            }
        }
        else
        {
            super.messageReceived(ctx, e);
        }
    }
}
