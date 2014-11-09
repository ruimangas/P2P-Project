package main.java.pt.ist.p2p;


import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.*;
import java.io.*;

import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.message.Message;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;



public class SearchServiceDHT {

   private static ItemSimple item = null;
   private static List<ItemSimple> items = new ArrayList<ItemSimple>(); 
   
   public static Number160 findReference(Peer peer,String index) throws ClassNotFoundException, IOException{
       
       Number160 searchKey = null;
       Number160 keyKeyword = Number160.createHash(index);
       FutureDHT futureGet = peer.get(keyKeyword).start();
       futureGet.awaitUninterruptibly();
       
       if(futureGet.isSuccess()){
          
           if(futureGet.getData().getObject().getClass().equals(keyKeyword.getClass())){
              searchKey = (Number160)futureGet.getData().getObject();
           }    
           else{
              searchKey = keyKeyword;
           }   
       }
      
       return searchKey;
   }
   
    public static void search(Peer myPeer, String index) throws ClassNotFoundException, IOException{ 
        
        Number160 searchKey = findReference(myPeer, index); 
      
        FutureDHT futureGet = myPeer.get(searchKey).start().awaitUninterruptibly();
      
        if(futureGet.isSuccess()){
           item = (ItemSimple)futureGet.getData().getObject();
        }
            
        items.add(item);  
    }  
           
    public static void booleanSearch(Peer myPeer,String index1,String index2,String operator) throws ClassNotFoundException, IOException{
                  
           if(operator.toLowerCase().equals("and")){
                   search(myPeer,index1);
                   search(myPeer,index2);
           }
                    
     }
        
    
    public static void clearMyShit(){
        items.clear();
        
    }
    
    public static List<ItemSimple> getMyItems(){
        return items;
    }

 }
        
    
    
    

