package org.kylin.netty.message;

import io.netty.handler.codec.ByteToMessageDecoder ;
import io.netty.channel.ChannelHandlerContext ;
import io.netty.buffer.ByteBuf ;

import java.util.List ;

/**
 * Created by root on 6/29/15.
 */
public class TimeDecoder extends ByteToMessageDecoder
{
     @Override
     protected void decode (ChannelHandlerContext ctx ,ByteBuf in ,List<Object> out )
     {
         if ( in.readableBytes() < 4)
             return ;

         out.add(new UnixTime(in.readInt())) ;
     }
}
