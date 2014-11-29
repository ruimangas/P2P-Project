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

    public static void seeItemsDetails(Peer myPeer,Item item) throws ClassNotFoundException, IOException{
        
           System.out.println("Nome Item: "+item.getName() +  " | " + "Description: " + item.getDescription());
           System.out.println("Bidders:");
           BidOnItemService.getBid(myPeer, item);
    
     }
    
}
