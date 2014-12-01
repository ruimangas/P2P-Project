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
import net.tomp2p.futures.FutureCreate;
import net.tomp2p.message.Message;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.builder.PutBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.replication.Replication;
import net.tomp2p.rpc.DirectDataRPC;
import net.tomp2p.storage.Data;






public class OfferItemServiceDHT {

    
    public static void putDatItem(Peer myPeer,ItemSimple itemSimple, Item item) throws IOException{
		Number160 domainKeyOffer = Number160.createHash("offeredItems");
		Number160 userName = Number160.createHash(itemSimple.getDealer());
		Number160 contentKey = Number160.createHash(item.getID());

    
       final int myID = item.getID();
        
        Number160 searchID = Number160.createHash(myID);
        Number160 domainKey = Number160.createHash("ITEMS");
        FutureDHT futurePutItem = myPeer.put(searchID).setData(new Data(item)).setDomainKey(domainKey).start();
        futurePutItem.awaitUninterruptibly();
        
        final String myTitle = itemSimple.getName();

        Number160 searchKey = Number160.createHash(myTitle);
        
        
        System.out.println(searchKey);
        FutureDHT futurePutItemSimple = myPeer.add(searchKey).setData(new Data(itemSimple)).setDomainKey(domainKey).start();
      
        futurePutItemSimple.awaitUninterruptibly(); 

        
        String[] keyWords = myTitle.split("[ ]");
       
        if(keyWords.length != 1){
            for(String index : keyWords){
            
                Number160 keyWord = Number160.createHash(index);
                myPeer.add(keyWord).setData(new Data(searchKey)).setDomainKey(domainKey).start().awaitUninterruptibly();
               
            }
        }
        
        myPeer.put(userName).setData(contentKey, new Data(item))
		.setDomainKey(domainKeyOffer).start().awaitUninterruptibly();

       
    }
    
    public static void removeDatItem(Peer myPeer,Item item) throws IOException{
    	final int myID = item.getID();
        Number160 itemId = Number160.createHash(myID);
        Number160 domainKey = Number160.createHash("ITEMS");
        myPeer.remove(itemId).setDomainKey(domainKey).start().awaitUninterruptibly();
    }
}
