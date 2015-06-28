package org.apache.zookeeper.demo;

import java.util.concurrent.CountDownLatch ;


import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.* ;
import org.apache.zookeeper.ZooKeeper ;





/**
 * Created by root on 6/28/15.
 */
public class SyncConnect_Event_Tester implements Watcher // Watcher 1
{
    ZooKeeper zk = null ;


    Watcher watcher ; // Watcher 2



    final String CONNECTION_STRING = "127.0.0.1:2181" ;

    final int    SESSSION_TIMER    = 2000 ;

    private CountDownLatch connectedSemaphore = new CountDownLatch( 1 ) ;

    public SyncConnect_Event_Tester ()
    {


        try
        {
            // this is for the Watcher 4
            this.watcher = new Watcher () {
                public void process(WatchedEvent e) {
                    try
                    {
                        Event.KeeperState keeperState = e.getState() ;

                        if (keeperState == Event.KeeperState.SyncConnected )
                        {
                            System.out.println ("[Watche4] success connect to ZooKeeper Server ") ;
                            connectedSemaphore.countDown();
                        }
                    }
                    catch ( Exception ex )
                    {
                        // ignored
                    }
                }
            } ;



            zk = new ZooKeeper (CONNECTION_STRING, SESSSION_TIMER , this.watcher) ;

            this.connectedSemaphore.await () ;


 /*
            WatcherLoader w = new WatcherLoader( this.connectedSemaphore ) ;

            zk = new ZooKeeper (CONNECTION_STRING , SESSSION_TIMER , w ) ; // this is for the Watcher 2
            this.connectedSemaphore.await () ;



            zk = new ZooKeeper ( CONNECTION_STRING , SESSSION_TIMER , this  )  ; // this is for Watcher 1

              // this is for the Watcher 3
            zk = new ZooKeeper ( CONNECTION_STRING , SESSSION_TIMER , new Watcher ()
            {
                public void process ( WatchedEvent e )
                {
                    try
                    {
                        Event.KeeperState keeperState = e.getState() ;

                        if (keeperState == Event.KeeperState.SyncConnected )
                        {
                            System.out.println ("[Watche3] success connect to ZooKeeper Server ") ;
                            connectedSemaphore.countDown();
                        }
                    }
                    catch ( Exception ex )
                    {
                        // ignored
                    }
                }
            }) ;





            */

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public void process ( WatchedEvent we )
    {
        try
        {
            Event.KeeperState keeperState = we.getState() ;

            System.out.println (we.getState().toString()) ;

            if (keeperState == Event.KeeperState.SyncConnected )
            {
                System.out.println ("[Watche1] success connect to ZooKeeper Server ") ;

                connectedSemaphore.countDown();
            }
        }
        catch ( Exception ex )
        {

        }
    }



    public static void main ( String [] args )
    {
        SyncConnect_Event_Tester tester = new SyncConnect_Event_Tester() ;
    }
}

class WatcherLoader implements Watcher
{
    private CountDownLatch countDownLatch ;

    public WatcherLoader ( CountDownLatch countDownLatch )
    {
        this.countDownLatch = countDownLatch ;
    }

    public void process ( WatchedEvent e) {
        try
        {
            Event.KeeperState keeperState = e.getState() ;

            if (keeperState == Event.KeeperState.SyncConnected )
            {
                System.out.println ("[Watche2] success connect to ZooKeeper Server ") ;
                countDownLatch.countDown();


            }
        }
        catch ( Exception ex )
        {
            // ignore
        }
    }
}
