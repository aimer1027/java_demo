package org.kylin.netty.message;

import io.netty.channel.ChannelHandlerAdapter ;
import io.netty.buffer.ByteBuf ;
import io.netty.channel.ChannelHandlerContext ;
import io.netty.channel.ChannelPromise ;

import io.netty.handler.codec.MessageToByteEncoder ;

/**
 * Created by root on 6/29/15.
 */
public class TimeEncoder extends MessageToByteEncoder<UnixTime>
{
    @Override
    protected void encode ( ChannelHandlerContext ctx, UnixTime unx ,ByteBuf out )
    {
        out.writeLong (unx.value()) ;
    }
}
