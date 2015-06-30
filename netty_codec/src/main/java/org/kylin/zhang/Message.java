package org.kylin.zhang;

import java.io.Serializable ;

/**
 * Created by root on 6/30/15.
 */

public class Message implements Serializable
{
    private MessageType type ;
    private short       length ;
    private byte []     data ;

    public Message ()
    {}

    public short getLength ()
    {
        return this.length ;
    }
    public void setLength ( short len )
    {
        this.length = len ;
    }


    public MessageType getType ()
    {
        return this.type ;
    }

    public void setType ( MessageType type )
    {
        this.type = type ;
    }

    public byte [] getData ()
    {
        return this.data ;
    }

    public void setData ( byte [] data )
    {
        this.data = data ;
    }


    @Override
    public String toString ()
    {
        return "\nmessage type :"+this.type +"\n"
                +"message length :"+this.length +"\n"
                +"message content :"+ new String (this.data) +"\n" ;
    }


    public static void main ( String [] args )
    {
        byte [] data = "Hello Aimer".getBytes() ;

        Message msg =new Message () ;

        msg.setType(MessageType.FILE_BEGIN);

        msg.setLength((short)data.length );

        msg.setData(data );

        System.out.println (msg) ;


    }

}
