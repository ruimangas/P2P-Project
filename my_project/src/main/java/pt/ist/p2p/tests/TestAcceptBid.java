package main.java.pt.ist.p2p.tests;

import static main.java.pt.ist.p2p.tomp2p.getGossip;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import main.java.pt.ist.gossip.MessageType;
import main.java.pt.ist.p2p.Bid;
import main.java.pt.ist.p2p.BidOnItemService;
import main.java.pt.ist.p2p.GossipService;
import main.java.pt.ist.p2p.Item;
import main.java.pt.ist.p2p.ItemSimple;
import main.java.pt.ist.p2p.OfferItemServiceDHT;
import main.java.pt.ist.p2p.RegisterServiceDHT;
import main.java.pt.ist.p2p.SeeItemsDetailsServiceDHT;
import main.java.pt.ist.p2p.User;
import main.java.pt.ist.p2p.tomp2p;
import net.tomp2p.p2p.Peer;

public class TestAcceptBid{
	private static Random rnd = new Random();
	private static String[] itemNameList = {"star wars","bike","ET","tele-transporter","laser gun"};
	private static String[] itemDescriptionList = {"leaked movie","bike with 3 wheels","kidnapped alien","go to anywere you want","just like the movies"};
	/**
	 * @param args
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws ClassNotFoundException,
			IOException {
		User user = new User();
		tomp2p tom = new tomp2p();
		Peer p = tomp2p.PeerBuilder(args[0]);
		String username = "joao";
		
		user = RegisterServiceDHT.passVerifier(p, username);
		
		if(user==null){
			user = new User();
			user.setUsername(username);
			RegisterServiceDHT.register(p, username, user);
			System.out.println("User joao created");
		}
		
		System.out.println("User joao logged in");
		
		GossipService.setGossipValues(user,tomp2p.getGossip(),p);
		GossipService.setupReplyHandler(p, tomp2p.getGossip());
		new SendThread4(tom).start();
		
    	for(int i=0;i<itemNameList.length;i++){
			ItemSimple itemSimple;
			int idItem = rnd.nextInt();
			Item item = new Item(idItem);
			
			String itemTitle = itemNameList[i];
			String itemDescription = itemDescriptionList[i];
			
			itemSimple = new ItemSimple(itemTitle, user.getUsername(), idItem);
			item.setName(itemTitle.toLowerCase());
			item.setDescription(itemDescription);
			item.setDealer(user.getUsername());
			user.setOfferedItem(item);

			OfferItemServiceDHT.putDatItem(p, itemSimple, item);
			System.out.println("Item " + itemTitle + " has been added to DHT");

		}
    	List<Item> items = null;
    	int i = 0;
    	while(i<itemNameList.length){        	 
    		if(i==0){
    			items = SeeItemsDetailsServiceDHT.getUserItems(p, user.getUsername());
    		}
    		Item item = items.get(i); 
    		int highestBid = 0;
        	String bidder = "";
    	 
        	try {
	    		Bid bid = BidOnItemService.getHighestBid(p, item);
				highestBid = bid.getBid();
				bidder = bid.getUserId();
        	}catch (Exception e) {
    		 highestBid = 0;
        	}
        	if(highestBid != 0)
        		i++;
        	else
        		i=0;
    	}
    	for(Item item : items){
			item.setSold(true);
			try{
				BidOnItemService.acceptBid(p, item, user);
			}catch(Exception e){
				System.out.println("This item has no bids, so it has been removed!");
			}
		}
    	Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.println( sdf.format(cal.getTime()) );
		p.shutdown();
	}
}

class SendThread4 extends Thread {
	final int ONE_SECOND = 1000;
	tomp2p sv;
	int i = 0;

	public SendThread4(tomp2p sv){
		this.sv = sv;
	}

	@Override
	public void run() {
		try{
			while (true){
				Thread.sleep(ONE_SECOND);
				GossipService.sendMessages(MessageType.NODES_SUM, i, sv.getPeer1(), getGossip(), sv.getU());
				GossipService.sendMessages(MessageType.ITEMS_SUM, i, sv.getPeer1(), getGossip(), sv.getU());
				GossipService.sendMessages(MessageType.USERS_SUM, i, sv.getPeer1(), getGossip(), sv.getU());
				i++;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}


