package main.java.pt.ist.p2p;

import java.io.IOException;
import java.util.Iterator;

import net.tomp2p.futures.FutureDHT;	
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class BidOnItemService {
	
    private static Number160 domainKeyBid = Number160.createHash("BIDS");
    
	public BidOnItemService(){
		
	}

	
	
	public static Iterator<Data> getMyBids(Peer myPeer, Item item){
	    
	    Number160 locationKey = Number160.createHash(item.getID());
        FutureDHT futureGet = myPeer.get(locationKey).setDomainKey(domainKeyBid).setAll().start().awaitUninterruptibly();
        
        
        Iterator<Data> iteratorBids = futureGet.getDataMap().values().iterator();
        
        
        
        return iteratorBids;
	    
	}
	
	
	public static void bid(Bid bid, Peer myPeer, Item item) throws IOException, ClassNotFoundException{
		
	    int theChamp = bid.getBid();
	    int theContender = 0;
	    Iterator<Data> iteratorBids = getMyBids(myPeer, item);
	    
	    while(iteratorBids.hasNext()){
	        
	        Bid bidLocal = (Bid) iteratorBids.next().getObject();
	        theContender = bidLocal.getBid();
	        
	        if(theContender >= theChamp)
	            theChamp = theContender;
	    }
	    
	    if(theChamp == bid.getBid()){
	            
    	    Number160 locationKey = Number160.createHash(item.getID());
    		myPeer.add(locationKey).setData(new Data(bid)).setDomainKey(domainKeyBid).start().awaitUninterruptibly();
    		System.out.println(locationKey);
    		
	    }else{
	        
	        System.out.println("You need to put a bigger bid");
	        
	    }
	            
	}
	
	public static void getBid(Peer myPeer, Item item) throws IOException, ClassNotFoundException{
		
	    Iterator<Data> iteratorBids = getMyBids(myPeer, item);
		
		while(iteratorBids.hasNext()){
			
			   Bid bidLocal =(Bid)iteratorBids.next().getObject();
	 		   System.out.println(bidLocal.getUserId()+" : "+ bidLocal.getBid());
	    }
		
	}

  
}