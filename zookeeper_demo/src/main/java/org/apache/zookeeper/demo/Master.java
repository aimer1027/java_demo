package org.apache.zookeeper.demo ;

import java.io.IOException ;
import java.util.ArrayList ;
import java.util.Collections ;
import java.util.List ;

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids ;
import org.apache.zookeeper.ZooDefs.Perms ;

import org.apache.zookeeper.data.ACL ;
import org.apache.zookeeper.data.Id ;
import org.apache.zookeeper.data.Stat ;

import org.apache.curator.* ;


public class Master
{
    /*
    *  @param args
    *  @param IOException
    *  @throws InterruptedException
    *  @throws KeeperException
    * */

    public static void main ( String [] args ) throws IOException , KeeperException ,
                    InterruptedException
    {
        Watcher watcher = new MyWatch () ;
        Stat st = new Stat() ;

        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181" , 1000 , null ) ;

      // zk.create ("/test" , "32167".getBytes() ,    Ids.OPEN_ACL_UNSAFE ,         CreateMode.PERSISTENT) ;

        zk.create ("/test/a" , "3948".getBytes() ,
                Ids.OPEN_ACL_UNSAFE ,
                CreateMode.PERSISTENT) ;
        zk.create ("/test/b" , "4323".getBytes () ,
                Ids.OPEN_ACL_UNSAFE ,
        CreateMode.PERSISTENT) ;


        List<String> list = zk.getChildren ("/test" , watcher ,st ) ;

        for ( String s : list )
        {
            System.out.println (s) ;
        }

        byte [] b = zk.getData("/test" , watcher , st ) ;
        System.out.println ( new String ( b )) ;


        zk.exists("/test/a", watcher ) ;
        zk.exists("/test/b", watcher,  new AsyncCallback.StatCallback(){

        public void processResult ( int rc , String path , Object ctx , Stat stat )
        {   System.out.println ("now the path is "+ path ) ;
            System.out.println ("I am here just to tell you it is because the folder"+ (String )(ctx)) ;
            System.out.println("cause this error ") ;
        } }, "/test/a");


        zk.delete("/test/a" , -1 ) ;
        zk.delete("/test/b", -1) ;



    }
}

class MyWatch implements Watcher
{
    public void process ( WatchedEvent event )
    {
        System.out.println("event") ;
        System.out.println (event.getPath() + "do have the folder") ;
    }
}