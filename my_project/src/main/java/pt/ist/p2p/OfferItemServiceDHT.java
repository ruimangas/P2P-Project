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

    
    public static void putDatItem(Peer myPeer,ItemSimple item) throws IOException{
        
       final String myTitle = item.getName();    
        Number160 searchKey = Number160.createHash(myTitle);
        Number160 contentKey = Number160.createHash(item.getDealer());
        
        System.out.println(searchKey);
        FutureDHT futurePut = myPeer.put(searchKey).setData(contentKey,new Data(item)).start();
        futurePut.awaitUninterruptibly();
        
        String[] keyWords = myTitle.split("[ ]");
        
        if(keyWords.length != 1){
            for(String index : keyWords){
                
                Number160 keyWord = Number160.createHash(index);
                myPeer.add(keyWord).setData(new Data(searchKey)).start().awaitUninterruptibly();
                
            }
        }
       
    }
}
