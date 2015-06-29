package org.kylin.netty.message;

import io.netty.buffer.ByteBuf ;
import io.netty.buffer.Unpooled ;

import java.io.Serializable ;
import java.util.ArrayList ;
import java.util.Collections ;
import java.util.List ;

import io.netty.buffer.UnpooledByteBufAllocator;
import org.kylin.netty.message.MessageType ;

/**
 * Created by root on 6/29/15.
 *
 * by using Message class , you can package the message you want .
 * after pass in the data length (2 bytes) and message type (1 bytes)
 * and data contents into the corresponding methods ,
 * you can get an object which packages all these things into
 *
 * a ByteBuf object , by calling getPacketMessage () method you can get it.
 *
 */
public class MessageCodec
{
    public static ByteBuf messageEncoder ( int messageType , String messageContent )
    {
        ByteBuf encodedMessage  ;

        byte []  msg_content = messageContent.getBytes() ;
        short    msg_length  = (short)msg_content.length ;
        char     msg_type    = (char)('0'+ messageType ) ;

        encodedMessage = Unpooled.buffer (2+1+ msg_length) ;

        // first we write in message length
        encodedMessage.writeShort(msg_length) ;
        System.out.println ("message length "+ msg_length ) ;

        // then we write in message type
        encodedMessage.writeChar(msg_type) ;
        System.out.println("message type "+ msg_type ) ;

        // at last we write in message contents
        encodedMessage.writeBytes( msg_content ) ;
        System.out.println("message content "+ new String (msg_content) ) ;

        return encodedMessage ;
    }


    public static Message MessageDecoder ( ByteBuf encodedMsg )
    {
        Message msg = new Message () ;

        short msg_length = encodedMsg.readShort() ;

        System.out.println ("message length " + msg_length ) ;

        char  msg_type = encodedMsg.readChar()   ;

        System.out.println ("message type "+ msg_type ) ;

        msg.DataContent = new byte[ encodedMsg.capacity() -3] ;

        int counter = 0 ;

        for ( int i = 4 ; i < encodedMsg.capacity() ; i++ )
        {
            msg.DataContent[counter++] = encodedMsg.getByte(i);
        }

        System.out.println ("message content "+ new String (msg.DataContent)) ;


        msg.DataLength = msg_length ;
        msg.MessageType = msg_type ;



        return msg ;
    }

    public static void main ( String [] args )
    {
        String file_content = "Hello World" ;
        ByteBuf buf_msg =  MessageCodec.messageEncoder(MessageType.BEGIN_SEND_FILE , file_content ) ;

        Message message = MessageDecoder( buf_msg) ;
    }
}

class Message
{


   public short DataLength ;

    public char  MessageType ;

    public byte []  DataContent ;
}
