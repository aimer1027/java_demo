package org.kylin.zhang.netty.message;

/**
 * Created by root on 6/30/15.
 */
public class MessageType
{
    public final static short FILE_READY  = 1 ;  // data content : none
    public final static short FILE_SENDING = 2 ;  // data content : file binary content
    public final static short FILE_FINISHED = 3 ; // data content : special me4ssage 'You are the best!'---> stupid!
    public final static short SHUT_DOWN     = 4 ; // data content : none
}
