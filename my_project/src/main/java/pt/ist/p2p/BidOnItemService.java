package main.java.pt.ist.p2p;

import java.io.IOException;
import java.util.Iterator;

import net.tomp2p.futures.FutureDHT;	
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class BidOnItemService {
	
	public BidOnItemService(){
		
	}

	public static void bid(Bid bid, Peer myPeer, ItemSimple item) throws IOException, ClassNotFoundException{
		Number160 locationKey = Number160.createHash(item.getName()+item.getDealer());
		myPeer.add(locationKey).setData(new Data(bid)).start().awaitUninterruptibly();
		System.out.println(locationKey);
	            
	}
	
	public static void getBid(Peer myPeer, ItemSimple item) throws IOException, ClassNotFoundException{
		int counterItem = 0;
		Number160 locationKey = Number160.createHash(item.getName()+item.getDealer());
		FutureDHT futureGet = myPeer.get(locationKey).setAll().start().awaitUninterruptibly();
		Iterator<Data> iteratorBids = futureGet.getDataMap().values().iterator();
		System.out.println("Bidders");
		while(counterItem < futureGet.getDataMap().size()){
			Object object = iteratorBids.next().getObject();
			if(object instanceof Bid){
	 		   Bid bidLocal = (Bid) object;
	 		   System.out.println(bidLocal.getUserID()+" : "+ bidLocal.getBid());
	        }
	 	   	counterItem++;
	    }
		counterItem = 0;
	}
}