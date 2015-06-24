package com.kylin.zhang.time;

import java.util.Properties;
import java.io.* ;
import org.springframework.core.io.support.PropertiesLoaderUtils;


/**
 * Created by root on 6/24/15.
 */
public class PropertiesAccessor
{
    /*
    *  This class is used for reading in properties files
    *  and parse it
    *  then create properties file and then write key-value
    *  pairs into it
    * */

    public static String conf_file_name ="zkconfig.properties" ;
    public static String conf_file_key = "ZK_CONF_PATH"  ;



    public static String get_config_folder_path()
    {
      return  PropertiesAccessor.class.getResource("/config").getPath() ;
    }

    public static String get_config_file_path ( )
    {
        return get_config_folder_path()+"/"+ conf_file_name;
    }


    public static File  create_properties_file( String file_path )
    {
        file_path = get_config_folder_path()+"/"+file_path ;

        File config_file = new File ( file_path ) ;

        if ( config_file.exists() && config_file.isFile() )
        {
            config_file.delete() ;
        }

        try
        {
            config_file.createNewFile();
        }
        catch ( IOException e)
        {
            e.printStackTrace();
        }

        return config_file ;
    }

    public static void store_properties_file ( Properties p , String prop_file_name )
    {
        try
        {
            File prop_store_file = create_properties_file(prop_file_name);

            OutputStream output = new FileOutputStream(prop_store_file);

            p.store(output , "" ) ;

            output.flush();
            output.close() ;
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    /*
    *  In get_properties_file
    *  you can send a name of a properties file already exists in
    *  current project /config/ path
    *  this method can load the properties file
    *  and wrapped it into an Properties object
    *  and give it back to the method's caller
    * */
    public static Properties get_properties  ( String prop_file_name )
    {
        Properties prop = null ;
        try
        {
           prop = PropertiesLoaderUtils.loadAllProperties("config/"+prop_file_name);

        }
        catch ( IOException ie )
        {
                ie.printStackTrace();
        }

        return prop ;
    }

    public static String  get_properties_value_by_key (  Properties p , String key)
    {
        return p.getProperty( key )  ;
    }

}
