package org.apache.zookeeper.demo;

import java.util.List ;
import java.util.concurrent.CountDownLatch ;
import java.util.concurrent.atomic.AtomicInteger ;

import org.apache.log4j.PropertyConfigurator ;
import org.apache.zookeeper.CreateMode ;
import org.apache.zookeeper.WatchedEvent ;
import org.apache.zookeeper.Watcher ;
import org.apache.zookeeper.Watcher.Event.EventType ;
import org.apache.zookeeper.Watcher.Event.KeeperState ;
import org.apache.zookeeper.ZooDefs.Ids ;
import org.apache.zookeeper.ZooKeeper ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;

import org.apache.zookeeper.data.Stat ;

/**
 * Created by root on 6/27/15.
 * what kind of thing will trigger
 * event ChildrenChangedEvent happen
 *
 * and
 * what should we do to deal with this event
 *
 */
public class ChildNodeChanged implements Watcher
{
    private static final Logger LOG = LoggerFactory.getLogger( ChildNodeChanged.class ) ;
    AtomicInteger seq = new AtomicInteger () ;
    private static final int SESSION_TIMEOUT = 10000 ;
    private static final String CONNECTION_STRING = "127.0.0.1:2181" ;

    private static final String ZK_PATH = "/KyLin" ;
    private static final String CHILDREN_PATH = "/KyLin/Zhang" ;
    private static final String LOG_PREFIX_OF_MAIN = "[Main_Log]" ;

    public ZooKeeper zk = null ;

    private CountDownLatch connectedSamephore = new CountDownLatch (1 ) ;



    public void createConnection ( String connectString , int sessionTimeOut )
    {
        this.releaseConnection();

        try
        {
            zk = new ZooKeeper ( CONNECTION_STRING , SESSION_TIMEOUT , this ) ;

            LOG.info(this.LOG_PREFIX_OF_MAIN + " connection begin establish");
          this.connectedSamephore.await () ;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public void releaseConnection ()
    {
        if ( zk != null )
        {
            try
            {
                this.zk.close() ;
            }
            catch ( InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }


    public boolean createPath ( String path , String data )
    {
        try
        {
            this.zk.create( path ,
                    data.getBytes() ,
                    Ids.OPEN_ACL_UNSAFE ,
                    CreateMode.PERSISTENT) ;

        }
        catch ( Exception e )
        {
            return false ;
        }

        return true ;
    }

    public void deleteNode ( String path )
    {
        try
        {
            this.zk.delete( path , -1) ;
            LOG.info(LOG_PREFIX_OF_MAIN , " success delete znode , path "
            + path ) ;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private List<String> getChildrenZnodes( String path , boolean needWatch )
    {
        try
        {
            return this.zk.getChildren( path , needWatch ) ;
        }
        catch ( Exception e )
        {
            return null ;
        }
    }

    public void deleteAllTestPath ()
    {
        this.deleteNode(this.CHILDREN_PATH);
        this.deleteNode(this.ZK_PATH);
    }

    public static void main ( String args [] ) throws Exception
    {
        PropertyConfigurator.configure("src/main/resources/log4j.properties") ;

        ChildNodeChanged sample = new ChildNodeChanged() ;

        sample.createConnection ( CONNECTION_STRING , SESSION_TIMEOUT) ;

        // first we create the main path

        sample.zk.create("/KyLin" , "838293".getBytes() , Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT ) ;



        // here we gonna to clare the nodes

     //   sample.deleteAllTestPath();

     /*   if ( sample.createPath (ZK_PATH  , "main path of kylin "))
        {
            List<String > strList = sample.getChildrenZnodes(ZK_PATH , true) ;

            for ( String s : strList )
            {
                System.out.println ("children  content "+s) ;
            }
        }
        now i would like to test in the command line which simulates
        another server leave off and its node is deleted

        i hope this information / message could be received by this
        zookeeper 's handler and see which node has just be deleted


        so before the test , i should use the zk handler
        to create
        /KyLin/folder1
        /KyLin/folder2
        /KyLin/folder3

        three folders like this

        and if this program is executed the second time
        i will check out whether it will invoke the process 's corresponding
        events





        Stat s = new Stat () ;
        System.out.println( sample.zk.getData("/KyLin" , sample , s) ) ;
        */
        sample.zk.create("/KyLin/folder1", "29273".getBytes(), Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT) ;

        sample.zk.create("/KyLin/folder3" , "282233".getBytes() , Ids.OPEN_ACL_UNSAFE ,
                CreateMode.PERSISTENT) ;

        sample.zk.create("/KyLin/folder2" , "29372".getBytes() , Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT) ;

       Thread.sleep(200000) ;





     sample.releaseConnection();
    }

    public void process ( WatchedEvent event )
    {
        try
        {
            Thread.sleep(50);

            KeeperState keeperState = event.getState () ;

            EventType eventType = event.getType () ;

            String path = event.getPath () ;
            String logPrefix = "[watcher log path ]"+this.seq.incrementAndGet() ;

            LOG.info (logPrefix + " receive Watcher notification ") ;
            LOG.info (logPrefix + " connection stat "+ keeperState.toString()) ;
            LOG.info (logPrefix + " event type "+ eventType.toString ()) ;


            if ( KeeperState.SyncConnected == keeperState )
            {
                // success connected to zookeeper server
                if ( EventType.None == eventType )
                {
                    System.out.println ( "success connect to ZK  server") ;

                    this.connectedSamephore.countDown();
                }
                else if ( EventType.NodeCreated == eventType )
                {
                    System.out.println (" create a node ") ;
                    this.zk.exists(path , true ) ;
                }
                else if ( EventType.NodeDataChanged == eventType )
                {
                    System.out.println ("node 's data content has been updated") ;


                }

                else if ( EventType.NodeChildrenChanged == eventType )
                {
                    System.out.println ("node 's child node changed ") ;
                }
                else if ( EventType.NodeDeleted == eventType )
                {
                    System.out.println ("node has been deleted ") ;
                }
            }
            else if ( KeeperState.Disconnected == keeperState )
            {
                System.out.println ("already disconnect to ZK  server ") ;
            }
            else if ( KeeperState.AuthFailed == keeperState)
            {
                System.out.println ("something wrong with authority") ;
            }
            else if ( KeeperState.Expired == keeperState )
            {
                System.out.println ("the session has been expired") ;
            }


            LOG.info ("--------------------------------------") ;

        }
        catch ( Exception e )
        {}
    }


}
