package org.kylin.zhang.netty.message;

import io.netty.handler.codec.ByteToMessageDecoder ;
import io.netty.channel.ChannelHandlerContext ;
import io.netty.buffer.ByteBuf ;

import java.util.List ;

import org.kylin.zhang.netty.message.Message ;
import org.kylin.zhang.netty.message.MessageType ;

/**
 * Created by root on 6/30/15.
 */
public class MessageDecoder extends ByteToMessageDecoder
{
    @Override
    protected void decode ( ChannelHandlerContext ctx , ByteBuf in , List<Object> out )
    {
        System.out.println("in MessageDecoder decode ") ;
        if ( in.readableBytes() < 3 )
        {
            System.out.println ("receive error format message") ;
            return ;
        }

        short msg_len = in.readShort() ;

        char  msg_type = in.readChar() ;

        if ( msg_len != 0 )
        {
            if ( in.readableBytes() < (3+msg_len) )
            {
                System.out.println ("receive error in data content ") ;
                return ;
            }

        }

        byte msg_data [] = new byte [msg_len] ;

        for ( int i = 0 ; i < msg_data.length ; i++)
        {
            msg_data[i] = in.getByte(i) ;

            // first test and if it read wront , change the ith into the i+4
            // which means read the data content from the 4th
        }

        Message message = new Message (  msg_type , msg_data ) ;

     //   in.release() ; // after copy all ByteBuf contents into Message
        // release its space

        System.out.println (message) ;
        out.add( message ) ;
    }
}
