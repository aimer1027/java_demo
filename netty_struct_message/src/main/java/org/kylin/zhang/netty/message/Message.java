package org.kylin.zhang.netty.message;

/**
 * Created by root on 6/30/15.
 */
public class Message
{
    public short     Message_Length ;  // 2-bytes
    public char      Message_Type ;      // 1-bytes
    public byte []   Message_Data ;       // dyamically corresponding to MessageType

    public Message ( char msg_type , byte [] msg_data )
    {
        Message_Type = msg_type ;

        if ( msg_type == MessageType.FILE_READY || msg_type == MessageType.SHUT_DOWN)
        {
            Message_Length = 0 ;
        }
        else
        {
            Message_Length = (short) msg_data.length;

            Message_Data = new byte[Message_Length];

            System.arraycopy(msg_data, 0, Message_Data, 0, msg_data.length);
        }
        // here we got a message
    }

    @Override
    public String toString ()
    {
        short type ;

        String typeName ;

        switch ( (Message_Type-'0') )
        {
            case MessageType.FILE_READY:
                typeName = "FILE_SENDING" ;
                break;
            case MessageType.FILE_SENDING:
                typeName = "FILE_SENDING" ;
                break ;
            case MessageType.FILE_FINISHED:
                typeName = "FILE_FINISHED" ;
                break ;
            case MessageType.SHUT_DOWN:
                typeName = "SHUT_DOWN" ;
                break ;
            default:
                typeName = "UnknownType" ;
        }

        return "message type :   "+ typeName +"\nmessage data length :"+ Message_Length +"\nmessage data content: \n"
                +new String (Message_Data)+"\n" ;
    }

}
