/**
 * Created by root on 6/24/15.
 */

import com.kylin.zhang.time.PropertiesAccessor ;

import java.io.* ;
import java.util.* ;

public class Properties_Accessor_Tester
{
    public static void main ( String [] args ) throws IOException
    {

    String server_name = "server.1" ;


    String prop_path =   PropertiesAccessor.get_config_folder_path() ;
    String prop_file_path = PropertiesAccessor.get_config_file_path()  ;
    System.out.println ("config folder path "+ prop_path  ) ;
    System.out.println ("config file path "+prop_file_path ) ;


     String config_file_name = PropertiesAccessor.conf_file_name ;

     Properties p = PropertiesAccessor.get_properties( config_file_name ) ;


    String key = "ZK_CONF_PATH" ;
    System.out.println ( "key : ZK_HOME value :" + p.getProperty(key) ) ;
    String ZooKeeper_Conf_Home = p.getProperty(key) ;



    BufferedReader zk_config_file = new BufferedReader( new FileReader(ZooKeeper_Conf_Home) ) ;

        // here we gonna to extract information from the config file
        // and write them into the local properties file

    String line  ;

    while ( (line = zk_config_file.readLine()) != null )
    {
        if ( line.startsWith(server_name))
        {
            // we got target server configure information
            // here we divide it into small parts
            // info like this : server.1=aimer:2888:3888


            String server_prefix = line.substring( 0 , line.indexOf('=')) ;
            // here we got the server_name --> aimer

            String hostname = line.substring(line.indexOf('=')+1 , line.indexOf(':'))  ;
            String begin_port = line.substring ( line.indexOf(':')+1 , line.lastIndexOf(':')) ;
            String end_port   = line.substring ( line.lastIndexOf(':')+1 , line.length() ) ;

            // next we create a local properties file , and then write the info into it
            // local properties file are named after the server's name

            String local_file_path = server_name + ".properties" ;
            Properties prop_loader = new Properties () ;

            prop_loader.setProperty(server_prefix+".hostname", hostname) ;
            prop_loader.setProperty(server_prefix+".begin_port" , begin_port ) ;
            prop_loader.setProperty(server_prefix+".end_port" , end_port ) ;

            PropertiesAccessor.store_properties_file(prop_loader , local_file_path  );

            break ;
        }
    }
    }
}
