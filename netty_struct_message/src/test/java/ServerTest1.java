import org.kylin.zhang.netty.message.* ;

/**
 * Created by root on 6/30/15.
 */
public class ServerTest1
{
    public static void main ( String [] args ) throws Exception
    {
        String hostname = "kylin" ;
        int    port     =  1027 ;
        Server server = new Server( hostname , port ) ;

      //  System.out.println ("ServerTest1 ready to run ") ;
       // server.run();

        String conn_host = "aimer" ;
        int    conn_port = 1025 ;

       // Thread.sleep(400) ;
        System.out.println ("well , now i try to connect to my Aimer : hostname : aimer , port 1025") ;
        server.ConnectOtherServer(conn_host, conn_port );

    }
}
