package org.apache.zookeeper.demo;


import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat ;
import org.apache.zookeeper.Watcher.Event ;

import java.io.IOException ;

/**
 * Created by root on 6/28/15.
 */

public class App implements Watcher
{


    public void process( WatchedEvent event )
    {

        if ( event.getType() == Event.EventType.NodeChildrenChanged)
        {
            System.out.println ("child changed ") ;
        }
        if ( event.getType() == Event.EventType.NodeDeleted )
        {
            System.out.println ("node deleted ") ;
        }
        System.out.println(" operation event invoke :") ;
        System.out.println (event) ;
    }

    public static void main ( String [] args )  throws Exception
    {
        final String CONNECT_STRING = "127.0.0.1:2181" ;
        final int SESSION_LIMIT = 20000 ;


        App a = new App ( ) ;



        final ZooKeeper zk = new ZooKeeper( CONNECT_STRING , SESSION_LIMIT , a ) ;

        Stat e = zk.exists("/kylin" , a ) ;

        System.out.println("exists "+ e) ;

        zk.setData("/kylin" , "server.1=127.0.0.1:2181".getBytes() , -1 ) ;

        // here we gonna to create a path , and see what will happen

        // here we set the wabher for this path
        zk.exists("/new_create_path_for_test" , a) ;

        // here we try to re-create , and see if this operation will  invoke the Watcher execution
        zk.create("/new_create_path_for_test/children11" ,"test_path".getBytes() ,
                ZooDefs.Ids.OPEN_ACL_UNSAFE ,
                CreateMode.PERSISTENT) ;


    }
}
