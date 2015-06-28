package org.apache.zookeeper.demo;

import java.util.concurrent.CountDownLatch ;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.* ;

import java.util.List ;

/**
 * Created by root on 6/28/15.
 *
 * step descriptions :
 * 1. create temporary folder paths
 *    /test/
 *      |--- t1
 *      |--- t2
 *      |--- t3
 *      |--- t4
 *      |--- t5
 *      |--- t6
 *      |--- t7
 *
 * 2. monitor path /test/ by method exists(String path , Watcher )
 *
 * 3. getChildren by /test/ get all its sub-path names
 *    3.1 set all its sub-path monitored by exists ( sub-path , watcher )
 *        implements an invoke of the watcher that if deleteNode event happens
 *        output the deleted path name on the console-screen
 *
 *    3.2 another method to add invoker of delete sub-path node is
 *        add deleteNode event methods into the step 2. 's Watcher
 *
 * 4. after add the corresponding methods into the invoker ,
 *    we try to delete a znode /test/t4 and see what happen
 *
 * 5. continue testing : if we add a znode on the monitored sub-path
 *    what will happen ?
 *
 * 6. getChildren with passing path as '/test/' , and get sub-path name
 *    and create a new Watcher with NodeChildChanged() and get the list of
 *    sub-child-node ,and get the tip from the stack-over-flow that the
 *    CreateNode Event only valid for exists () method .
 *
 *    So , i have to run a cycle to add every , sorry , i have no idea yet
 *
 *    At last , to increase the flexibility of Watcher , I use lots of
 *    different kinds of class to implement Watcher interface.
 *
 */
public class NodeChildrenChanged_Event_Tester
{
    ZooKeeper zk = null ;

    Conn_Watcher_Impl ConnWatcher = null ;

    final String CONNECTION_STRING = "127.0.0.1:2181" ;

    final int    SESSION_TIMER = 20000 ;


    public NodeChildrenChanged_Event_Tester()
    {
        try
        {
             this.ConnWatcher = new Conn_Watcher_Impl() ;

            this.zk = new ZooKeeper (this.CONNECTION_STRING , this.SESSION_TIMER , this.ConnWatcher ) ;

           // this.connectionSemaphore.await () ; // this will wait the connectSemaphore 's value from 1 to 0
                                                // and then continue the following steps

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
    }

    /**
     * method createPath
     * @param path : name of path which going to be created
     * @param data_info : data_info is used to store path's information on the znode
     * @param watcher : if we want to add a monitor to the new created path ,pass an instance of Watcher
     *                   if do not need add one , pass null is Ok
     * */
    public void createPath ( String path , String data_info , Watcher watcher )
    {
        try
        {
            // in case of Exception , we delete the path first
            // note : -1 means whatever the version is delete them all ,
            // cause ZooKeeper distinguish same path name' node by its version id

            this.zk.create( path , data_info.getBytes() , ZooDefs.Ids.OPEN_ACL_UNSAFE , CreateMode.PERSISTENT) ;

            // after create a znode , we add an Watcher to monitor it
            if ( watcher != null )
            this.zk.exists( path , watcher ) ;

        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
    }

    /**
     * method : showChildList
     * this method is used to show all path's children name on ZooKeeper server
     * if the path does not have any child , print out message
     *
     * @param path : path name
     * */
    public void showChildList ( String path )
    {
        try
        {
            List<String> childList = this.zk.getChildren(path , false ) ;

            if ( childList.isEmpty())
            {
                System.out.println("path : "+path +" on ZooKeeper is null , do not have sub-path ") ;
            }
            else
            {
                for ( String s : childList )
                {
                    System.out.println (path + "/"+ s )  ;
                }
            }

        }
        catch (Exception ex )
        {
            ex.printStackTrace();
        }
    }

    public static void main ( String [] args ) throws Exception
    {
        String main_path = "/test" ;

        NodeChildrenChanged_Event_Tester tester = new NodeChildrenChanged_Event_Tester() ;

        MasterWatcher masterWatcher = new MasterWatcher( tester  , main_path ) ;

        // create main_path node
        tester.createPath (main_path , "main_path_data_content" , masterWatcher ) ;



        // create sub-path
        for ( int i = 1 ; i <= 3 ; i++ )
        {
            String sub_path = main_path+"/t"+i ;
            tester.createPath( sub_path , "sub_path"+i , null ) ; // i can add sub-path=monitors here
        }

        // here we gonna to test showChildList method
        tester.showChildList(main_path );


        Thread.sleep(600000) ;
    }
}

class Conn_Watcher_Impl implements Watcher
{
    public void process ( WatchedEvent watchedEvent )
    {
        KeeperState keeperStat = watchedEvent.getState() ;

        if ( keeperStat == KeeperState.SyncConnected )
        {
            System.out.println ("You have succeed in connecting to ZooKeeper Server ") ;

            // we use the CountDownLatch is comming from the NodeChildrenChanged_Event_Tester
            // it means until we execute this process , the Main thread could exit
            // use it in case of Main thread exiting earlier than this process method ,
            // if so , we could not see this message on screen
        }


    }
}

/**
 * MasterWatcher this is class implements interface Watcher
 * its process method mainly used to monitor the main path's events
 * such as :
 *         1. node deletion
 *         2. child node changed
 *         3. node created
 *         4. node data content be updated
 *
 * and write different events , do different actions :
 *
 *          1. print out messages on console
 *          2. child node changed this situation is a little complex:
 *             2.1 it may be add a node on the child-path
 *             2.2 it may be delete a n ode on the child-path
 *             2.3 it may be the sub-path nodes' data content is updated
 *          3. print out messages on console
 *          4. print out messages on console
 *
 * at beginning ,  what is important is that : distinguish different events
 * so , all kinds of event's actions are simply output messages
 *
 * after we get a good master of different kind events , we write the event's
 * method in detail
 * */
class MasterWatcher implements Watcher
{
    // cause we need to show the path names in this class
    // so we need the handler of the ZooKeeper

    // so we need the ZooKeeper object being sent into this class as
    // constructor's parameter

    NodeChildrenChanged_Event_Tester zk_master = null ;

    String main_path = null ;

    public MasterWatcher ( NodeChildrenChanged_Event_Tester zk_master , String master_path  )
    {
        this.zk_master = zk_master ;
        this.main_path = master_path ;
    }

    public void process ( WatchedEvent watchedEvent)
    {
        KeeperState keeperState = watchedEvent.getState() ;
        EventType   pathEvents  = watchedEvent.getType() ;

        if ( keeperState == KeeperState.Disconnected || keeperState == KeeperState.Expired )
        {
            System.out.println ("connection problems ") ;
        }
        else
        {
             if ( pathEvents == EventType.NodeCreated)
             {
                 System.out.println ("path node already be created") ;
             }
             else if ( pathEvents == EventType.NodeDataChanged )
             {
                 System.out.println ("node data been updated ") ;
             }
            else if ( pathEvents == EventType.NodeDeleted )
             {
                 System.out.println ("node is deleted ") ;
             }
            else if ( pathEvents == EventType.NodeChildrenChanged )
             {
                 System.out.println ("node child-path has been changed ")  ;
             }
            else
             {
                 System.out.println ("here is something else happen") ;
             }
        }

    }
}