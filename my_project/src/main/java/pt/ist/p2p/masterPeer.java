package main.java.pt.ist.p2p;

import java.io.IOException;
import java.util.*;

import net.tomp2p.connection.Bindings;
import net.tomp2p.connection.ChannelCreator;
import net.tomp2p.connection.Sender;
import net.tomp2p.futures.FutureChannelCreator;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.message.Message;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;



public class masterPeer {

    
     private static Peer peerMaster;
     
        
    public masterPeer(){
              
    }
    

  public static void main(String[] args ){
      
      
      
      final List<PeerAddress> my_peers = new ArrayList<PeerAddress>(); 
      final List<PeerAddress> my_peers_send = new ArrayList<PeerAddress>(); 
      int ID = 0;
      Random rnd = new Random();
      Bindings b = new Bindings();
      Number160 id = new Number160(1);
      Message m = new Message();
      Sender s;
      int diff = 0;
      
      try{
      
          peerMaster = new PeerMaker(id).setTcpPort(10001).setUdpPort(10001).setBindings(b).makeAndListen();
          peerMaster.getConfiguration().setBehindFirewall(true);
       //   peerMaster.put(Number160.createHash("master")).setData(new Data("teste")).start().awaitUninterruptibly();
          
      }catch(Exception e){
          
          System.out.println(e.getMessage());
      }
      
     
      System.out.println("my peers:" + peerMaster.getPeerBean().getPeerMap().getAll());
      my_peers.addAll(peerMaster.getPeerBean().getPeerMap().getAll());
      
      
      while(true){
          
              
          try {
              Thread.sleep(10000);
          } catch (InterruptedException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
          
            /*  m.setNeighbors(peerMaster.getPeerBean().getPeerMap().getAll());
              m.setSender(peerMaster.getPeerAddress());
              m.setMessageId(ID++);

              FutureChannelCreator fcc = peerMaster.getConnectionBean().getConnectionReservation().reserve(ID);
              fcc.awaitUninterruptibly();

              ChannelCreator cc = fcc.getChannelCreator();

              FutureResponse fr1 = new FutureResponse(m);
              fr1.awaitUninterruptibly();*/
           // s.sendTCP(m);
           
          if(my_peers.containsAll( peerMaster.getPeerBean().getPeerMap().getAll()))
              continue;
          else{
              
              diff = peerMaster.getPeerBean().getPeerMap().getAll().size() - my_peers.size(); 
              my_peers.clear();
              my_peers.addAll(peerMaster.getPeerBean().getPeerMap().getAll());
              my_peers_send.addAll(peerMaster.getPeerBean().getPeerMap().closePeers(id, diff));
              
              
              try{
                  
              peerMaster.put(Number160.createHash("neighbors")).setData(new Data(my_peers_send)).start().awaitUninterruptibly();
              
              }catch(Exception e){
                  System.out.println(e.getMessage());
              }
          }
              
             

      }
      
  }
    
 }
    
    
    
