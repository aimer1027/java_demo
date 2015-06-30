package org.kylin.zhang.netty.message;

import io.netty.buffer.ByteBuf ;
import io.netty.channel.ChannelHandlerContext ;


import io.netty.handler.codec.MessageToByteEncoder ;

/**
 * Created by root on 6/30/15.
 */

public class MessageEncoder extends MessageToByteEncoder<Message>
{
    @Override
    public void encode ( ChannelHandlerContext ctx,Message message, ByteBuf out )
    {
        // first , we allocate the ByteBuf enough space

        out = ctx.alloc().buffer(3+ message.Message_Length) ;

        out.writeShort (message.Message_Length) ;
        out.writeChar  (message.Message_Type) ;

        out.writeBytes(message.Message_Data) ;
    }
}
