package org.apache.zookeeper.demo ;

import org.apache.zookeeper.ZooKeeper ;
import org.apache.zookeeper.Watcher ;
import org.apache.zookeeper.WatchedEvent ;

import java.io.* ;

public class Master implements  Watcher {
    ZooKeeper zk;
    String hostPort;

    Master(String hostPort) {
        this.hostPort = hostPort;
    }

    void startZK() {
        try {
            zk = new ZooKeeper(hostPort, 15000, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void process(WatchedEvent e) {
        System.out.println(e);
    }

    public static void main(String args[]) throws Exception
    {
        String hostport = "kylin:2181" ;
        Master m = new Master (hostport) ;

        m.startZK();

        //wait for a bit
        Thread.sleep( 60000) ;
    }
}