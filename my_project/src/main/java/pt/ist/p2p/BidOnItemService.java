package main.java.pt.ist.p2p;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.tomp2p.futures.FutureDHT;	
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class BidOnItemService {
	
    private static Number160 domainKeyBid = Number160.createHash("BIDS");
    private static Number160 domainKeyBidHISTORY = Number160.createHash("BIDSHISTORY");
    private static Number160 OFFITEMS = Number160.createHash("offeredItems");
	private static Number160 PURCHASEDITEMS = Number160.createHash("purchasedItems");

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
	    
	    if(theChamp == bid.getBid() && bid.getBid() > 0){
	            
    	    Number160 locationKey = Number160.createHash(item.getID());
    	    Number160 locationKeyHistory = Number160.createHash(bid.getUserId());
    		
    	    myPeer.add(locationKey).setData(new Data(bid)).setDomainKey(domainKeyBid).start().awaitUninterruptibly();
    		myPeer.add(locationKeyHistory).setData(new Data(bid)).setDomainKey(domainKeyBidHISTORY).start().awaitUninterruptibly();
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
	
	public static Bid getHighestBid(Peer myPeer, Item item) throws IOException, ClassNotFoundException{
		Bid highestBid = null;
	    int bidValue = 0;
		Iterator<Data> iteratorBids = getMyBids(myPeer, item);
		
		while(iteratorBids.hasNext()){
			   Bid bidLocal =(Bid)iteratorBids.next().getObject();
	 		   if(bidLocal.getBid()>bidValue){
	 			   highestBid = bidLocal;
	 			   bidValue = bidLocal.getBid();
	 		   }
		}
		return highestBid;
	}
  
	public static void acceptBid(Peer myPeer, Item item, User u) throws IOException,
	ClassNotFoundException {
		Number160 userName = Number160.createHash(u.getUsername());
		Number160 contentKey = Number160.createHash(item.getName());
		OfferItemServiceDHT.removeDatItem(myPeer, item);
		myPeer.remove(userName).setContentKey(contentKey)
			.setDomainKey(OFFITEMS).start().awaitUninterruptibly();
		
		Bid bid = BidOnItemService.getHighestBid(myPeer, item);
		
		Number160 locationKeyPurchase = Number160.createHash(bid
			.getUserId());
		
		item.setSoldValue(bid.getBid());
		myPeer.add(locationKeyPurchase).setData(new Data(item))
			.setDomainKey(PURCHASEDITEMS).start()
			.awaitUninterruptibly();
	}
	
	public static void listItemsWithBids(Peer myPeer, List<Item> items) {
		int i = 1;
     System.out.println("****************** Results *********************");
     System.out.println("************************************************");
     for (Item item : items){
    	 int highestBid = 0;
    	 String bidder = "";
    	 try {
    		Bid bid = getHighestBid(myPeer, item);
			highestBid = bid.getBid();
			bidder = bid.getUserId();
    	 }catch (Exception e) {
    		 highestBid = 0;
    	 }
		 if(!item.getSold()){  
			 if(highestBid>0)
				 System.out.println(i+" - Item Name: " + item.getName() + " | CurrentBid: " + highestBid + " | CurrentBidder: " + bidder);
			 else
				 System.out.println(i+" - Item Name: " + item.getName() + " | This item have no bids");
			 i++;
       } 
     }
     System.out.println("************************************************");
     System.out.println("Press 0 to go back or press the item number to see details and bid");
  }
}