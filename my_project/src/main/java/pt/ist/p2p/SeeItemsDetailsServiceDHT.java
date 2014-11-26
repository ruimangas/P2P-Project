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






public class SeeItemsDetailsServiceDHT {

    public static void seeItemsDetails(Peer myPeer,String nomeItem, String dealer) throws ClassNotFoundException, IOException{
        
        List<Number160> myReferences = new ArrayList<Number160>();
        List<ItemSimple> myItems = new ArrayList<ItemSimple>();
        ItemSimple myItem = null;
        Number160 myHash;
        myHash = Number160.createHash(nomeItem);
        myReferences.add(myHash);
        
        myItems = SearchServiceDHT.search(myPeer, myReferences);
        
        for(ItemSimple i : myItems){
            if(i.getDealer().equals(dealer))
                myItem = i;
        }
        if(myItem.equals(null))
            System.out.println("Send exception!!!!");
        
        
        System.out.println("Title: "+myItem.getName() + " | " + "Description: "+myItem.getDescription());
        System.out.println("Bid History: ");
        
        for(Bid b : myItem.getAllBidders())
            System.out.println("Bidder: "+b.getUserID() + " | " + "Value: " +b.getBid());
    }
    
    
    
}
