import org.kylin.zhang.netty.message.* ;

/**
 * Created by root on 6/30/15.
 */
public class ServerTest2
{
    public static void main ( String [] args ) throws Exception
    {
        String hostname = "aimer" ;
        int    port     =  1025 ;
        Server server = new Server( hostname , port ) ;

        System.out.println("serverTest2 begin to run ") ;
        server.run();

      /*  String conn_host = "kylin" ;
        int    conn_port = 1027 ;


        Thread.sleep(200) ;
        System.out.println ("ok , now i try to connect to the server1: host: kylin , port : 1027") ;
        server.ConnectOtherServer(conn_host, conn_port );
    */

    }
}
