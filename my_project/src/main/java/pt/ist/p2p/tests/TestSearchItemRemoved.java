package main.java.pt.ist.p2p.tests;

import static main.java.pt.ist.p2p.tomp2p.getGossip;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import main.java.pt.ist.gossip.MessageType;
import main.java.pt.ist.p2p.Bid;
import main.java.pt.ist.p2p.BidOnItemService;
import main.java.pt.ist.p2p.GossipService;
import main.java.pt.ist.p2p.Item;
import main.java.pt.ist.p2p.ItemSimple;
import main.java.pt.ist.p2p.RegisterServiceDHT;
import main.java.pt.ist.p2p.SearchServiceDHT;
import main.java.pt.ist.p2p.User;
import main.java.pt.ist.p2p.tomp2p;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

public class TestSearchItemRemoved {

	private static Random rnd = new Random();
	private static String[] itemNameList = {"star wars","bike","ET","tele-transporter","laser gun"};
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
		String username = "karan";
		
		user = RegisterServiceDHT.passVerifier(p, username);
		
		if(user==null){
			user = new User();
			user.setUsername(username);
			RegisterServiceDHT.register(p, username, user);
			System.out.println("User karan created");
		}
		
		System.out.println("User karan logged in");
		
		GossipService.setGossipValues(user,tomp2p.getGossip(),p);
		GossipService.setupReplyHandler(p, tomp2p.getGossip());
		new SendThread3(tom).start();
		
		for(int i=0;i<itemNameList.length;i++){	
			List<ItemSimple> itemSimples = new ArrayList<ItemSimple>();
			List<Item> items = new ArrayList<Item>();
			String s = itemNameList[i];
			String[] choice = s.split("[ ]");
			List<String> myOperators = new ArrayList<String>();
			List<String> myQuery = new ArrayList<String>();
			List<Number160> hashSimple;
	
			for (String st : choice) {
				if (st.toLowerCase().equals("and") || st.toLowerCase().equals("or") || st.toLowerCase().equals("not"))
					myOperators.add(st.toLowerCase());
				myQuery.add(st);
			}
	
			
			
			while(items.isEmpty()){
				try {

				    if (myOperators.size() == 0) {

		                hashSimple = SearchServiceDHT.findReference(p,myQuery.get(0));
		                itemSimples = SearchServiceDHT.search(p, hashSimple);

				    } else {
		                    
				        itemSimples = SearchServiceDHT.booleanSearch(p,myOperators, myQuery);

				    }
				    
				    items = SearchServiceDHT.searchItem(p, itemSimples);


				} catch (Exception e) {
				    System.out.println(e.toString());
				    SearchServiceDHT.clearMySearch();
				    return;

				}

			}
			for(Item item : items){
				BidOnItemService.bid(new Bid(user.getUsername(), 1000,
						item.getName()), p, item);
			}
			SearchServiceDHT.listItems(items);
			SearchServiceDHT.clearMySearch();
		}
    	for(int i=0;i<itemNameList.length;i++){	
			List<ItemSimple> itemSimples = new ArrayList<ItemSimple>();
			List<Item> items = new ArrayList<Item>();
			String s = itemNameList[i];
			String[] choice = s.split("[ ]");
			List<String> myOperators = new ArrayList<String>();
			List<String> myQuery = new ArrayList<String>();
			List<Number160> hashSimple;
	
			for (String st : choice) {
				if (st.toLowerCase().equals("and") || st.toLowerCase().equals("or") || st.toLowerCase().equals("not"))
					myOperators.add(st.toLowerCase());
				myQuery.add(st);
			}
	
			
			try {

			    if (myOperators.size() == 0) {

	                hashSimple = SearchServiceDHT.findReference(p,myQuery.get(0));
	                itemSimples = SearchServiceDHT.search(p, hashSimple);

			    } else {
	                    
			        itemSimples = SearchServiceDHT.booleanSearch(p,myOperators, myQuery);

			    }
			    
			    items = SearchServiceDHT.searchItem(p, itemSimples);


			} catch (Exception e) {
			    System.out.println(e.toString());
			    SearchServiceDHT.clearMySearch();
			    return;

			}
			while(!items.isEmpty()){
				try {

				    if (myOperators.size() == 0) {

		                hashSimple = SearchServiceDHT.findReference(p,myQuery.get(0));
		                itemSimples = SearchServiceDHT.search(p, hashSimple);

				    } else {
		                    
				        itemSimples = SearchServiceDHT.booleanSearch(p,myOperators, myQuery);

				    }
				    
				    items = SearchServiceDHT.searchItem(p, itemSimples);


				} catch (Exception e) {
				    System.out.println(e.toString());
				    SearchServiceDHT.clearMySearch();
				    return;

				}

			}
			SearchServiceDHT.listItems(items);
			SearchServiceDHT.clearMySearch();
		}
    	System.out.println("ALL ITEMS REMOVED");

		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.println( sdf.format(cal.getTime()) );
		
    	p.shutdown();
	}
}

class SendThread5 extends Thread {
	final int ONE_SECOND = 1000;
	tomp2p sv;
	int i = 0;

	public SendThread5(tomp2p sv){
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
