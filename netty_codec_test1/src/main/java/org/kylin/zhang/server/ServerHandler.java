package org.kylin.zhang.server;

import org.kylin.zhang.common.* ;
import org.jboss.netty.channel.ChannelHandlerContext ;
import org.jboss.netty.channel.ChannelStateEvent ;
import org.jboss.netty.channel.MessageEvent ;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler ;
import org.jboss.netty.channel.group.ChannelGroup ;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by root on 7/1/15.
 */
public class ServerHandler extends SimpleChannelUpstreamHandler
{
    private final ChannelGroup channelGroup ;
    private Map<String,String> fileHash ;

    public ServerHandler ( ChannelGroup channelGroup )
    {

        this.channelGroup = channelGroup ;

        this.fileHash = new HashMap<String,String>() ;

        // here we initialize the fileHash
        fileHash.put("test1" , "/tmp/test1.txt") ;
        fileHash.put("test2" , "/tmp/test2.txt") ;
    }

    @Override
    public void channelConnected( ChannelHandlerContext ctx, ChannelStateEvent e )
    {
        System.out.println("-------------------------- **server gets a new connection**--------------------------") ;
        this.channelGroup.add(e.getChannel()) ;


        byte [] data = null ;

        File file = new File ( fileHash.get("test1") ) ;
        String len_fname = String.valueOf(file.length()) ;
        len_fname += ":test1" ;

        // file_length:file_name

        data = len_fname.getBytes() ;

        Message msg = new Message(MessageType.READY_SEND_FILE , (short)(3+data.length) ,data);

        e.getChannel().write(msg) ;

     //   e.getChannel().disconnect() ;
    }

    @Override
    public void messageReceived ( ChannelHandlerContext ctx, MessageEvent e )
            throws Exception
    {

        if ( e.getMessage() instanceof Message ) {


            Message received_msg = (Message) e.getMessage();

            MessageType recv_msg_type = received_msg.getType () ;

            System.out.println("server received message: " + received_msg);

            if ( recv_msg_type == MessageType.READY_RECV_FILE)
            {
                // in this open the file and use while cycle send file data <=250 bytes to client
                // if read the last line 'the end of the file!'
                // change the response file state from SENDING_FILE to END_FILE
                // and then close the channel

                File f = new File (this.fileHash.get("test1")) ;
                BufferedReader input = new BufferedReader( new FileReader(f) ) ;

                String line ;

                while(!( line = input.readLine()).equals("the end of file!"))
                {
                    Message send_msg = new Message( MessageType.FILE_SENDING , (short)(3+line.getBytes().length) ,
                            line.getBytes()) ;

                    e.getChannel().write(send_msg) ;
                }

                if (line.equals("the end of file!"))
                {
                    Message send_msg = new Message (MessageType.FILE_END, (short)(3+line.getBytes().length),
                            line.getBytes() ) ;
                    e.getChannel().write(send_msg) ;
                }

            }
            else if ( recv_msg_type == MessageType.SHUT_DOWN)
            {
                e.getChannel().disconnect() ;
            }
            else
            {
                System.out.println ("UNKNOWN Message ") ;
            }


        }
        else
        {
            super.messageReceived(ctx, e);
        }







      /*
        System.out.println("-------------------- **server receive a piece of message** -------------------------- ") ;


        if ( e.getMessage() instanceof Message ) {


            Message received_msg = (Message) e.getMessage();

            System.out.println("server received message: "+ received_msg) ;

            byte [] data = received_msg.getData() ;

            if ( received_msg.getType() == MessageType.FILE_BEGIN)
            {

                System.out.println ("client request for reading file: "+ new String (data)+".") ;

                String filename = new String (data) ;
                String filePath = null ;
                String str_data = null ;

                Iterator iter = this.fileHash.keySet().iterator() ;

                while ( iter.hasNext() )
                {
                    String key = (String)iter.next() ;

                    System.out.println ("key (filename) : "+ key ) ;
                    System.out.println ("key value (file ab path)"+ this.fileHash.get(key)) ;

                    if ( key.equals(filename))
                    {
                        // we got the client'q request file's path
                        filePath = this.fileHash.get(key) ;

                        break ;
                    }
                }

                if ( filePath == null )
                {
                    // here we create file not found message and send it back
                    // client receive it close the connection , here
                    // after the server send the message back , it also close the conn

                     str_data = "not exists" ;
                }

                else
                {
                    // and here we open the file , load its contents
                    File file = new File(filePath);

                    if (file.isFile() && file.exists()) {
                        BufferedReader input = new BufferedReader(new FileReader(file));

                        String line;


                        while ((line = input.readLine()) != null) {
                            str_data += line;
                        }

                    } else {
                        // here we create file not found message and send it back
                        // client receive it close the connection , here
                        // after the server send the message back , it also close the conn
                        str_data = "not file";
                    }
                } // branch for reading file

                byte [] send_data = new byte [str_data.getBytes().length ] ;
                send_data = str_data.getBytes() ;

                MessageType type = MessageType.FILE_SENDING ;


                e.getChannel().write( new Message(type , (short)(send_data.length +3), send_data)) ;

            }
            if ( received_msg.getType() == MessageType.FILE_END)
            {

            }





            System.out.println("server received message :" + received_msg);

            if ( new String (received_msg.getData()).equals("KyLin_Zhang")) {
                //how to shutdown connection
                System.out.println ("received end message "+ received_msg) ;
                this.channelGroup.disconnect() ;
            } else {

                String data = "Server Received Message ";

                Message msg = new Message(MessageType.FILE_END, (short) (data.getBytes().length + 3), data.getBytes());

                e.getChannel().write(msg);

            }

        }
        else
        {
            super.messageReceived(ctx, e);
        } */
    }
}
