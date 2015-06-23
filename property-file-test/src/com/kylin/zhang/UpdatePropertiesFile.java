package com.kylin.zhang;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils ;

import java.io.*;
import java.util.Properties;
import java.net.URLDecoder ;

/**
 * Created by root on 6/23/15.
 */
public class UpdatePropertiesFile {

    public static void main(String[] args) throws IOException
    {
        String server_name ="server.2" ;
        String propFilePath = UpdatePropertiesFile.class.getResource("/").getPath() ;

        String new_properties_file = propFilePath+"config/"+server_name+".properties" ;

        File newServerProp = new File (new_properties_file) ;

        if ( !newServerProp.exists())
            newServerProp.createNewFile() ;


        OutputStream output = new FileOutputStream( newServerProp ) ;

        Properties p_loader = new Properties () ;

        p_loader.setProperty("server" , "aimer") ;
        p_loader.store(output,"");

       // p_loader.setProperty(server_name , "zoo1:2888:3888") ;

        System.out.println ( "writer and immediately read key= "+ server_name +"    "+p_loader.getProperty(server_name ) ) ;


        System.out.println(UpdatePropertiesFile.class.getResource("/config").getPath()) ;

    }
}
