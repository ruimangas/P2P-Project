package main.java.pt.ist.p2p;

import java.io.IOException;
import java.util.*;

import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;



public class masterPeer {

    
     private static Peer peerMaster;
     
        
    public masterPeer(){
              
    }
    

  public static void main(String[] args ){
      
      Random rnd = new Random();
      Bindings b = new Bindings();
      Number160 id = new Number160(1);
      
      
      try{
      
          peerMaster = new PeerMaker(id).setTcpPort(10001).setUdpPort(10001).setBindings(b).makeAndListen();
          peerMaster.getConfiguration().setBehindFirewall(true);
          peerMaster.put(Number160.createHash("master")).setData(new Data("teste")).start().awaitUninterruptibly();
      }catch(Exception e){
          
          System.out.println(e.getMessage());
      }
      
      try {
        Thread.sleep(50000);
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
     
      System.out.println("my peers:" + peerMaster.getPeerBean().getPeerMap().getAll());
  }
    
 }
    
    
    

