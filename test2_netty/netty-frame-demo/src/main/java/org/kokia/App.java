package org.kokia;

import java.util.Properties ;
import java.io.InputStream ;
import java.io.IOException ;

import java.net.URL ;

/**
 * method used to read properties file
 *
 */
public class App 
{
    private String ip_address ;
    private int port ;
    public String path ;

    App () {



    }

    void showConfig ()
    {
        InputStream readInStream = App.class.getClassLoader().getResourceAsStream("config.properties");
        if ( readInStream == null )
        {
            System.out.println ("loading nothing ...") ;
        }
        Properties p = new Properties();

        try
        {
            p.load(readInStream);
            ip_address = (String)(p.getProperty("ip_address")) ;
            port =  Integer.valueOf(p.getProperty("port")  ).intValue() ;
        } catch (IOException e)
        {

            e.printStackTrace();
        }

        System.out.println("ip address : "+ ip_address) ;
        System.out.println("port  :" + port ) ;
    }

    public static void main ( String [] args ) throws IOException
    {

            URL base = App.class.getClass().getResource("") ;
           System.out.println (base.toString()) ;
    }
}
