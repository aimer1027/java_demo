package org.kylin.zhang.common;



/**
 * Created by root on 6/30/15.
 */
public enum MessageType
{
    READY_SEND_FILE((byte)0x01) ,
    READY_RECV_FILE((byte)0x04) ,
    FILE_SENDING((byte)0x02) ,
    FILE_END((byte)0x03) ,
    SHUT_DOWN((byte)0x00),
    UNKNOWN ((byte)0x00) ;


    private final byte b ;

    private MessageType ( byte b )
    {
        this.b = b ;
    }


    public static MessageType fromByte ( byte b )
    {
        for (MessageType code : values())
        {
            if (code.b == b )
                return code  ;
        }

        return UNKNOWN ;
    }

    public byte getByte ()
    {
        return this.b ;
    }
}
