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
	
	private static String OFFITEMS = "offeredItems";
    

	public static List<Item> getUserItems(Peer myPeer, String userName) throws ClassNotFoundException, IOException{
		List<Item> listItems = new ArrayList<Item>();
		FutureDHT futureGet = myPeer.get(Number160.createHash(userName)).setDomainKey(Number160.createHash(OFFITEMS)).setAll().start().awaitUninterruptibly();
		Iterator<Data> iteratorItem = futureGet.getDataMap().values().iterator();
		Object o;
		int counterItem = 0;
        while(counterItem < futureGet.getDataMap().size()){         
            o = iteratorItem.next().getObject();
            listItems.add((Item) o);
            counterItem++;
        }  
		return listItems;
	}
	
    public static void seeItemsDetails(Peer myPeer,Item item) throws ClassNotFoundException, IOException{
        
           System.out.println("Nome Item: "+item.getName() +  " | " + "Description: " + item.getDescription());
           System.out.println("Bidders:");
           BidOnItemService.getBid(myPeer, item);
    
    }
    /*
    public static Item getSpecificItem(Peer myPeer, String userName, String itemName) throws ClassNotFoundException, IOException{
		FutureDHT futureGet = myPeer.get(Number160.createHash(userName)).setDomainKey(Number160.createHash(OFFITEMS)).setAll().start().awaitUninterruptibly();
		Iterator<Data> iteratorItem = futureGet.getDataMap().values().iterator();
		Object o;
		int counterItem = 0;
        while(counterItem < futureGet.getDataMap().size()){         
            o = iteratorItem.next().getObject();
            Item item = (Item) o;
            if(item.getName().equals(itemName))
            	return item;
            counterItem++;
        }  
		return null;
	}*/
    
}
