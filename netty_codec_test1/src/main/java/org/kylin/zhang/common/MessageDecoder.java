package org.kylin.zhang.common;

import org.jboss.netty.buffer.ChannelBuffer ;
import org.jboss.netty.channel.Channel ;
import org.jboss.netty.channel.ChannelHandlerContext ;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder ;

/**
 * Created by root on 6/30/15.
 *
 * decoder is used to convert ChannelBuffer instance into
 * an Instance of Message
 *
 */
public class MessageDecoder extends ReplayingDecoder<MessageDecoder.DecodingState>
{
    private Message message ;

    public MessageDecoder ()
    {
        this.reset() ;
    }

  @Override
  protected Object decode ( ChannelHandlerContext ctx, Channel ch , ChannelBuffer buffer, DecodingState state )
        throws Exception
  {
    // notice the switch fall-through

      switch (state)
      {
          case MESSAGE_LENGTH:

              short len = buffer.readShort() ;
              if ( len <= 0 )
                  throw new Exception ("invalid message length ");

              // pre-allocate data content buffer
              byte [] data = new byte [len-3] ;
              this.message.setData(data);
              this.message.setLength (len) ;

              checkpoint(DecodingState.MESSAGE_TYPE) ;

          case MESSAGE_TYPE:

              this.message.setType(MessageType.fromByte(buffer.readByte())) ;
              checkpoint(DecodingState.MESSAGE_DATA) ;

          case MESSAGE_DATA:
              buffer.readBytes(this.message.getData() , 0 ,
                      this.message.getLength()-3);

           try
           {
            //   System.out.println ("MessageDecoder : "+message) ;
               return this.message ;
           }
           finally
           {
               this.reset() ;
           }

          default :
              throw new Exception ("Unknown decoding state : "+state ) ;
      }
  }

  private void reset ()
  {
      checkpoint(DecodingState.MESSAGE_LENGTH);
      this.message = new Message () ;
  }


    public enum DecodingState
    {
        MESSAGE_LENGTH,
        MESSAGE_TYPE,
        MESSAGE_DATA ,
    }
}
