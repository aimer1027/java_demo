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
import org.apache.zookeeper.data.Stat ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;



/**
 * Created by root on 6/27/15.
 */
public class ZookeeperWatcher implements Watcher
{
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperWatcher.class) ;
    AtomicInteger seq = new AtomicInteger () ;
    private static final int SESSION_TIMEOUT = 10000 ;
    private static final String CONNECTION_STRING = "127.0.0.1:2181, 127.0.0.1:2182, 127.0.0.1:2183" ;

    private static final String ZK_PATH = "/kokia" ;
    private static final String CHILDREN_PATH="/KyLin/kokia_sub" ;
    private static final String LOG_PREFIX_OF_MAIN = "[Log_Main] " ;

    private ZooKeeper zk = null ;

    private CountDownLatch connectedSemaphore = new CountDownLatch (1) ;


    public void createConnection ( String connectString , int sessionTime )
    {
        this.releaseConnection () ;

        try
        {
            zk = new ZooKeeper( connectString , sessionTime  , this ) ;
            LOG.info (this.LOG_PREFIX_OF_MAIN + " begin connect to ZK  server") ;
            connectedSemaphore.await() ; // why here ? why it ?


        }
        catch ( Exception e )
        {}
    }

    public void releaseConnection ()
    {
        if ( zk != null )
        {
            try
            {
                this.zk.close() ;
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }
        }
    }

    public boolean createPath ( String path , String data )
    {
        try
        {
            this.zk.exists( path , true ) ;

            LOG.info ( this.LOG_PREFIX_OF_MAIN + " success to create znode , Path  "+
            this.zk.create( path , data.getBytes() , Ids.OPEN_ACL_UNSAFE ,CreateMode.PERSISTENT )+
            ", content "+ data ) ;
        }
        catch ( Exception e )
        {}

        return true ;
    }

    public String readData ( String path , boolean needWatch )
    {
        try
        {
            return new String (this.zk.getData(path , needWatch , null )) ;
        }
        catch ( Exception e )
        {
            return "" ;
        }
    }

    public boolean writeData ( String path , String data )
    {
        try
        {
            LOG.info (LOG_PREFIX_OF_MAIN + " needs to update data , path "
            +path +"  , stat "+ this.zk.setData( path , data.getBytes() , -1 ) ) ;
        }
        catch ( Exception e )
        {

        }

        return false ;
    }


    public void deleteNode (String path )
    {
        try
        {
            this.zk.delete( path , -1 ) ;
            LOG.info (LOG_PREFIX_OF_MAIN , " success delete znode , path : "+path ) ;
        }
        catch ( Exception e)
        {}
    }


    public Stat exists ( String path , boolean needWatch )
    {
        try
        {
            return this.zk.exists( path , needWatch ) ;

        }
        catch ( Exception e )
        {
            return null ;
        }
    }

    private List<String> getChildren ( String path , boolean needWatch )
    {
        try
        {
            return this.zk.getChildren ( path , needWatch ) ;
        }
        catch ( Exception e ) {
            return null;
        }
    }

    public void deleteAllTestPath ()
    {
        this.deleteNode(CHILDREN_PATH) ;
        this.deleteNode (ZK_PATH) ;
    }

    public static void main ( String [] args ) throws Exception
    {
        PropertyConfigurator.configure ("src/main/resources/log4j.properties") ;

        ZookeeperWatcher sample = new ZookeeperWatcher() ;

        sample.deleteAllTestPath();

        if ( sample.createPath ( ZK_PATH, System.currentTimeMillis()+""))
        {
            Thread.sleep(2000) ;

            // read data

            sample.readData(ZK_PATH , true ) ;

            // here we gonna read the sub node
            sample.getChildren(ZK_PATH ,  true ) ;


            // here we gonna to update the data
            sample.writeData (ZK_PATH, System.currentTimeMillis()+"" ) ;

            Thread.sleep(2000) ;
        }

        Thread.sleep( 2000 ) ;

        // here we gonna to clean up the nodes
        sample.deleteAllTestPath();
        Thread.sleep(3000) ;

        sample.releaseConnection () ;

    }



   public  void process ( WatchedEvent event ) {
        try
        {
            Thread.sleep(200);

            if ( event == null )
                return ;

            KeeperState keeperState = event.getState() ;

            EventType eventType = event.getType() ;


            String path = event.getPath () ;
            String logPrefix = "Watcher-" +this.seq.incrementAndGet() ;

            LOG.info(logPrefix+" receive Watcher notification ") ;
            LOG.info (logPrefix+" connection state "+ keeperState.toString () ) ;
            LOG.info (logPrefix+ " event Type "+ eventType.toString() ) ;

            if ( KeeperState.SyncConnected == keeperState )
            {
                // success connect to ZK  server

                if ( EventType.None == eventType)
                {
                    LOG.info( logPrefix+" success connect to ZK  server ") ;
                    connectedSemaphore.countDown() ;
                }
                else if ( EventType.NodeCreated == eventType )
                {
                    LOG.info ( logPrefix + " the node you want to create already exists") ;
                    this.exists( path , true ) ;
                }
                else if ( EventType.NodeDataChanged == eventType )
                {
                    LOG.info (logPrefix + " znode 's data content update ") ;
                    LOG.info (logPrefix + "data content is "+ this.readData(ZK_PATH, true)) ;

                }
                else if ( EventType.NodeChildrenChanged == eventType )
                {
                    LOG.info(logPrefix + " child node changed ") ;
                    LOG.info (logPrefix + "child znode list s : "+ this.getChildren(ZK_PATH , true )) ;

                }
                else if ( EventType.NodeDeleted == eventType )
                {
                    LOG.info(logPrefix + " node "+ path + " is deleted ") ;
                }
            }
            else if ( KeeperState.Disconnected == keeperState )
            {
                LOG.info (logPrefix + " disconnected from ZK  server ") ;
            }
            else if ( KeeperState.AuthFailed == keeperState )
            {
                LOG.info( logPrefix + " authority checking failed ") ;
            }
            else if ( KeeperState.Expired == keeperState )
            {
                LOG.info (logPrefix + " session expired " ) ;
            }


            LOG.info("--------------------end ------------------------") ;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }




}
