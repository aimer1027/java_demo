package com.kylin.zhang;

import java.io.*;
import java.util.Properties ;
import org.springframework.core.io.support.PropertiesLoaderUtils ;

// file reader
import java.lang.StringBuilder ;


/**
 * Created by root on 6/23/15.
 */
public class PropertiesFilePath {

    public static void main ( String [] args )
    {
        Properties props = new Properties () ;
        String config_file_path ;
        String server_name = "server.1" ;

        try
        {
            props = PropertiesLoaderUtils.loadAllProperties("config/ipConfig.properties") ;

            config_file_path = props.getProperty("zookeeper_config_path") ;
            System.out.println ( "config file path " + config_file_path ) ;

            // here we open the file and read all the contents into String
            File config_file = new File( config_file_path );

            if ( config_file.exists() && config_file.isFile ())
            {
                try
                {
                   System.out.println (" here we open the file ") ;

                    BufferedReader input = new BufferedReader (  new FileReader( config_file)) ;
                    String a_line ;

                    while(( a_line = input.readLine() )!= null )
                    {
                        System.out.println ("config  file  content "+ a_line ) ;

                         if ( a_line.startsWith(server_name))
                         {
                             // server.1=aimer:2888:3888

                             String key = a_line.substring(0 , a_line.indexOf('=')) ;
                             String hostname_value = a_line.substring(a_line.indexOf('=')+1 , a_line.indexOf(':')) ;
                             String port_begin_value = a_line.substring( a_line.indexOf(':')+1 , a_line.lastIndexOf(':')) ;   ;
                             String port_end_value   = a_line.substring( a_line.lastIndexOf(':')+1 , a_line.length() ) ;


                             // next we need to write this key/value pair write into the key-name.properties file
                            // directly write it into the file
                             // and use the properties to load it

                             String file_path = PropertiesFilePath.class.getResource("/config").getPath() ;
                             file_path+="/"+server_name+".properties" ;
                             File conf_file2 = new File ( file_path ) ;

                             if ( conf_file2.exists())
                             {
                                 conf_file2.delete() ;
                             }
                             conf_file2.createNewFile() ;


                             Properties prop = new Properties() ;
                             prop.setProperty(key+".hostname"  , hostname_value ) ;
                             prop.setProperty(key+".begin_port" , port_begin_value) ;
                             prop.setProperty(key+".end_port" , port_end_value)  ;

                             OutputStream output = new FileOutputStream( conf_file2 ) ;
                             prop.store(output , "" );

                             System.out.println ("hostname_value  "+ prop.getProperty(key+".hostname")) ;
                             System.out.println ("begin_port "+prop.getProperty(key+".begin_port")) ;
                             System.out.println ("end_port "+prop.getProperty(key+".end_port")) ;

                             break ;
                         }
                    }


                    // here we try to get a path to the current project  and create a property file











                }
                finally
                {

                }
            }

        }
        catch ( IOException e)
        {
            e.printStackTrace();
        }

    }

}
