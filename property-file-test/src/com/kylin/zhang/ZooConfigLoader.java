package com.kylin.zhang;

import java.io.*;
import java.util.Map ;
import java.util.HashMap ;

import java.util.Properties ;


// used for file reading
import java.io.BufferedReader ;


import com.kylin.zhang.NodeInfo ;

/**
 * Created by root on 6/23/15.
 */
public class ZooConfigLoader {

    private Map<String , NodeInfo > zooTable ;
    String zookeeperConfigFilePath ;
    String file_content ;

    ZooConfigLoader ()
    {
        // in this method , read in the ipConfig.properties file
        // and get the  zookeeper config file 's path
        // and  append the configure file 's contents to ipConfig.properties file

        // then use the getProperty to initialize each String and NodeInfo
        // then write a getConfigTable to return the  Map infomation to other callers

        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream("config/ipConfig.properties") ;


        Properties p = new Properties () ;

        try
        {
            p.load(inputStream) ;
            zookeeperConfigFilePath = p.getProperty("zookeeper_config_path") ;


            File config_file = new File ( zookeeperConfigFilePath ) ;

            if (config_file.isFile() && config_file.exists() )
            {
                BufferedReader input = new BufferedReader( new FileReader(config_file)) ;

                String line ;
                while (( line = input.readLine()) != null )
                {
                    // parse the zookeeper 's zoo.cfg file
                    /*
                    *  tickTime=2000
                    *  dataDir=/var/zookeeper/
                    *  clientPort=2181
                    *  initLimit=5
                    *  syncLimit=2
                    *  server.1=zoo1:2888:3888
                    *  server.2=zoo2:2888:3888
                    *  server.3=zoo3:2888:3888
                    * */
                }



            }

        }
        catch ( IOException e)
        {
            System.out.println ("failed load source file ") ;
            e.printStackTrace();
        }

    }
}
