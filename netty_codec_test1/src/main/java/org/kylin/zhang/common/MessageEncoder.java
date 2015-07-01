package org.kylin.zhang.common;

import org.jboss.netty.buffer.ChannelBuffer ;
import org.jboss.netty.buffer.ChannelBuffers ;
import org.jboss.netty.channel.Channel ;
import org.jboss.netty.channel.ChannelHandler ;
import org.jboss.netty.channel.ChannelHandlerContext ;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder ;

/**
 * Created by root on 6/30/15.
 *
 * this class is used to converts an Message instance
 * into a ChannelBuffer
 *
 * we use the singleton pattern
 * and set it as stateless in order it can be used by multi-pipeline
 *
 */

@ChannelHandler.Sharable

public class MessageEncoder extends OneToOneEncoder
{
    private MessageEncoder ()
    {}
    private static final class InstanceHolder
    {
        private static final MessageEncoder INSTANCE = new MessageEncoder() ;
    }

    public static MessageEncoder getInstance ()
    {
        return InstanceHolder.INSTANCE ;
    }

    public static ChannelBuffer encodeMessage ( Message message ) throws IllegalArgumentException
    {
       // System.out.println ("encodeMessage :" + message ) ;

        // first check the Message Type , Unknow type and null type are illegaled for a message
        if (message.getType() == null || ( message.getType() == MessageType.UNKNOWN))
        {
            throw new IllegalArgumentException("Message type can not be null or UNKNOWN  ") ;
        }
        if ( message.getLength() == 0  )
        {
            throw new IllegalArgumentException("Message length can not be 0, at least 3 ") ;
        }
        if ( message.getData().length < 0 || message.getData().length > (message.getLength() -3))
        {
            System.out.println ("message.getData().legngth : "+ message.getData().length) ;
            System.out.println("message.getLength () -3: "+ (message.getLength() -3) );
            throw new IllegalArgumentException("Message data length can not less than 0 or larger than message length ") ;
        }

        // type (1b) , length (2b) , data(nb) = (3+n)b
        int size = 3 + message.getData().length ;

        ChannelBuffer buffer = ChannelBuffers.buffer(size) ;

        buffer.writeShort(message.getLength()) ;
        buffer.writeByte (message.getType().getByte()) ;
        buffer.writeBytes(message.getData());

        return buffer ;
    }

    // ---- OneToOneEncoder
    @Override
    protected Object encode ( ChannelHandlerContext ctx, Channel channel , Object msg )
            throws Exception
    {
        if ( msg instanceof Message )
            return encodeMessage((Message)msg) ;
        else
           return msg ;
    }
}
