package com.kylin.zhang;

import java.io.InputStream ;
import java.io.IOException ;
import java.util.Properties ;

import java.util.Map ;

import com.kylin.zhang.NodeInfo ;
import  java.util.Iterator ;
import  java.util.HashMap ;
/**
 * Created by root on 6/21/15.
 */
public class Main {

    public String  addr ;
    public  String port ;
    public String hostname ;

    public Main ( String server_name )
    {
        this.hostname = server_name ;

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config/ipConfig.properties") ;

        Properties p = new Properties() ;

        Properties props = System.getProperties() ;



        try
        {
            p.load(inputStream) ;

            addr = p.getProperty(server_name+".ip") ;

            port = p.getProperty(server_name+".port") ;
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    public String toString ()
    {
        return "hostname  "+ hostname + "  ip address " + this.addr +"   "+"port " + this.port ;
    }



    public static void main ( String [] args )
    {

        String server_name = "server1" ;
        Main m = new Main ( server_name ) ;



        String ip = m.addr ;
        short port = Short.parseShort( m.port ) ;



        System.out.println( m  ) ;
    }
}
