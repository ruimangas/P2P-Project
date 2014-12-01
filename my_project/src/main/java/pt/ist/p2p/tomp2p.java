package main.java.pt.ist.p2p;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.*;

import main.java.pt.ist.gossip.Message;
import main.java.pt.ist.gossip.MessageType;
import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.Number480;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;
import main.java.pt.ist.gossip.Gossip;
import static main.java.pt.ist.p2p.tomp2p.getGossip;

public class tomp2p {

	private static Peer peer1 = null;

	private static User u = new User();

	private static Random rnd = new Random();

	private static String USERNAMES = "username";

	private static String OFFITEMS = "offeredItems";

	private static String PURCHASEDITEMS = "purchasedItems";

	private static Gossip gossip = new Gossip();

	private static ArrayList<String> allUsers = new ArrayList<String>();

	public tomp2p() {

	}

	public static Peer PeerBuilder(String port) throws ClassNotFoundException,
			IOException {

		Bindings b = new Bindings();

		peer1 = new PeerMaker(new Number160(rnd.nextInt()))
				.setTcpPort(Integer.parseInt(port))
				.setUdpPort(Integer.parseInt(port)).setBindings(b)
				.setEnableIndirectReplication(true).makeAndListen();

		InetAddress address = Inet4Address.getByName("127.0.0.1");

		PeerAddress peerAddress = new PeerAddress(new Number160(1), address,
				10001, 10001);

		FutureDiscover future = peer1.discover().setPeerAddress(peerAddress)
				.start();
		future.awaitUninterruptibly();

		FutureBootstrap future1 = peer1.bootstrap().setPeerAddress(peerAddress)
				.start();
		future1.awaitUninterruptibly();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}

		return peer1;
	}

	public static String getMyIp() {

		String ip = null;

		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (java.net.UnknownHostException e) {
			System.out.println("error: ip not found");
			e.printStackTrace();
		}

		return ip;
	}

	public static void comandLine() {

		Scanner keyboard = new Scanner(System.in);

		try {

			while (true) {
				System.out.println("\n*****************************");
				System.out.println("*          P2PBAY           *");
				System.out.println("*****************************");
				System.out.println("1 - offer an item for sale");
				System.out.println("2 - search for an item to buy");
				System.out.println("3 - accept a bid");
				System.out.println("4 - purchase and bidding history");
				if (u.getUsername().equals("admin"))
					System.out.println("5 - user management");
				System.out.println("0 - exit app");

				int numero = keyboard.nextInt();
				switch (numero) {
				case 1:
					offerItem();
					break;
				case 2:
					searchItem();
					break;
				case 3:
					acceptBid();
					break;
				case 4:
					history();
					break;
				case 5:
					userManagement();
					break;
				case 6:
				    printStorage();
				    break;
				case 0:
					peer1.shutdown();
					return;
				default:
					System.out.println("Invalid operation");
				}

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static  void printStorage(){

	    System.out.println("tamanho: " + peer1.getPeerBean().getPeerMap().getAll().size());

	    Map<Number480, Data> map = peer1.getPeerBean().getStorage().map();
	    for (Object o : map.entrySet()) {
	        Map.Entry thisEntry = (Map.Entry) o;
	        Object value = thisEntry.getValue();
	        Data data = (Data) value;

	        try {
	            if (data.getObject().getClass().getName().equals("main.java.pt.ist.p2p.ItemSimple")) {
	                ItemSimple it = (ItemSimple) data.getObject();
	                System.out.println(it.getName());
	            }
	            if (data.getObject().getClass().getName().equals("main.java.pt.ist.p2p.User")) {
	                User u = (User) data.getObject();
	                System.out.println(u.getUsername());

	            }

	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

	public static void showItemMenu() {

		System.out.println("1 - Bid on Item");
		System.out.println("2 - See Item Details");
		System.out.println("0 - Go Back");
	}

	public static void showAcceptanceMenu() {

		System.out.println("1 - AcceptBid");
		System.out.println("2 - See Item Details");
		System.out.println("0 - Go Back");
	}

	public static void offerItem() throws IOException, ClassNotFoundException {

		ItemSimple itemSimple;
		int idItem = rnd.nextInt();

		Item item = new Item(idItem);

		Scanner keyboard1 = new Scanner(System.in);

		System.out.println("Please, enter the name of the product:");
		String itemTitle = keyboard1.nextLine();

		System.out.println("Please, enter the description of the product:");
		String itemDescription = keyboard1.nextLine();

		itemSimple = new ItemSimple(itemTitle, u.getUsername(), idItem);
		item.setName(itemTitle.toLowerCase());
		item.setDescription(itemDescription);
		item.setDealer(u.getUsername());
		u.setOfferedItem(item);

		try {

			OfferItemServiceDHT.putDatItem(peer1, itemSimple, item);
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		keyboard1.close();
	}

	public double getActualNumberFiles() {
		return u.getOfferedItems().size();
	}

	public static void searchItem() throws IOException, ClassNotFoundException {

		List<ItemSimple> itemSimples = new ArrayList<ItemSimple>();
		List<Item> items = new ArrayList<Item>();
		System.out.println("String to search:");
		Scanner keyboard1 = new Scanner(System.in);
		String s = keyboard1.nextLine();
		String[] choice = s.split("[ ]");
		List<String> myOperators = new ArrayList<String>();
		List<String> myOperands = new ArrayList<String>();
		List<String> myQuery = new ArrayList<String>();
		List<Number160> hashSimple;

		for (String st : choice) {
			if (st.toLowerCase().equals("and") || st.toLowerCase().equals("or") || st.toLowerCase().equals("not"))
				myOperators.add(st.toLowerCase());
			else
				myOperands.add(st);
			myQuery.add(st);
		}

		
		System.out.println("MyOperands: "+myOperands);
		System.out.println("MyOperators: "+myOperators);
		
		try {

			if (myOperators.size() == 0) {

				hashSimple = new ArrayList<Number160>();

				hashSimple = SearchServiceDHT.findReference(peer1,
						myOperands.get(0));

				itemSimples = SearchServiceDHT.search(peer1, hashSimple);

			} else {

				itemSimples = SearchServiceDHT.booleanSearch(peer1,
						myOperators, myOperands, myQuery);

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		items = SearchServiceDHT.searchItem(peer1, itemSimples);

		SearchServiceDHT.listItems(items);
		SearchServiceDHT.clearMySearch();

		Scanner keyboard = new Scanner(System.in);
		int key = keyboard.nextInt();

		while (key != 0) {
			Item item = items.get(key - 1);
			showItemMenu();

			int key2 = keyboard.nextInt();
			if (key2 == 1) {
				System.out.println("Bid value:");
				int bidValue = keyboard.nextInt();

				try {

					BidOnItemService.bid(new Bid(u.getUsername(), bidValue,
							item.getName()), peer1, item);

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}

			if (key2 == 2) {

				SeeItemsDetailsServiceDHT.seeItemsDetails(peer1, item);
			}

			SearchServiceDHT.listItems(items);
			key = keyboard.nextInt();

		}
		keyboard.close();

	}

	public static void acceptBid() throws ClassNotFoundException, IOException {

		List<Item> items;
		items = SeeItemsDetailsServiceDHT.getUserItems(peer1, u.getUsername());
		BidOnItemService.listItemsWithBids(peer1, items);

		Scanner keyboard = new Scanner(System.in);
		int key = keyboard.nextInt();

		while (key != 0) {

			Item item = items.get(key - 1);

			showAcceptanceMenu();

			int key2 = keyboard.nextInt();

			if (key2 == 1) {
				item.setSold(true);
				try{
					BidOnItemService.acceptBid(peer1, item, u);
				}catch(Exception e){
					System.out.println("This item has no bids, so it has been removed!");
				}
			}

			if (key2 == 2) {

				try {

					SeeItemsDetailsServiceDHT.seeItemsDetails(peer1, item);

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			BidOnItemService.listItemsWithBids(peer1, items);
			key = keyboard.nextInt();

		}
		keyboard.close();

	}

	public static void history() throws ClassNotFoundException, IOException {
		HistoryServiceDHT.getHistory(peer1, u.getUsername());
	}
	
/*	public static void showItemDetails() throws ClassNotFoundException, IOException {
		Scanner input = new Scanner(System.in);

		System.out.println("Please, enter the name of the item:");
		String itemTitle = input.nextLine();

		System.out.println("Please, enter the dealer name:");
		String dealer = input.nextLine();
		try {		
			Item item = SeeItemsDetailsServiceDHT.getSpecificItem(peer1, dealer, itemTitle);
			SeeItemsDetailsServiceDHT.seeItemsDetails(peer1, item);
		} catch (Exception e) {
			System.out.println("There is no item with that parameters");
			//System.out.println(e.getMessage());
		}

	}
	*/
	public static void userManagement(){

	    if(u.getUsername().equals("admin")){

	        System.out.println("There are " + Math.round(getGossip().calculateNumberNodes(MessageType.NODES_SUM)) + " nodes.");
	        System.out.println("There are " + Math.round(getGossip().calculateNumberItems(MessageType.ITEMS_SUM))  + " items.");
	        System.out.println("There are " + Math.round(getGossip().calculateNumerUsers(MessageType.USERS_SUM))  + " Users.");

	    }

	    else System.out.println("Invalid operation");
	}

	public static boolean passVerifier(Peer peer1)
			throws ClassNotFoundException, IOException {

		User user;
		System.out.println("Do you have an account?");
		Scanner keyboard1 = new Scanner(System.in);
		String s = keyboard1.nextLine();

		if (s.equals("yes") || s.equals("y")) {

			System.out.println("User:");
			Scanner keyboard2 = new Scanner(System.in);
			String username = keyboard2.nextLine();
			Number160 userKey = Number160.createHash(USERNAMES);
			FutureDHT futureDHT = peer1.get(Number160.createHash(username))
					.setDomainKey(userKey).start();
			futureDHT.awaitUninterruptibly();
			String userName = "";

			if (futureDHT.isSuccess()) {
				user = (User) futureDHT.getData().getObject();

				if (user.getUsername().equals("admin")) {

					System.out.println("You are logged in as admin");
					userName = "admin";

				} else {

					System.out
							.println("******** WELCOME TO TOMP2P AUCTIONS ********");
					System.out.println("You are logged in as "
							+ user.getUsername());
					userName = user.getUsername();
				}

				u.setUsername(userName);
				u.setBiddedItems(user.getBiddedItems());
				return true;

			} else {
				System.out.println("User not found");
				register();
				return true;
			}
		} else
			register();

		return false;
	}

	public static void register() throws IOException {

		System.out.println("Register Menu");
		Scanner keyboard1 = new Scanner(System.in);
		
		while (true) {

			System.out.println("Username:");

			String username = keyboard1.nextLine();
            
			if(!RegisterServiceDHT.userExists(peer1, username)){
				
    			u.setUsername(username);
    			RegisterServiceDHT.register(peer1, username, u);
    		    System.out.println("you are logged in as " + u.getUsername());
    		    keyboard1.close();
    		    break;
		    
			}
		}
     }
	

     public static Gossip getGossip() {
         return gossip;
     }

     public Peer getPeer1() { return peer1; }

     public User getU() { return u; }

	public static void main(String[] args) throws ClassNotFoundException, IOException {

		tomp2p tom = new tomp2p();

		Peer p = tomp2p.PeerBuilder(args[0]);

		passVerifier(p);
		GossipService.setGossipValues(u,getGossip(),peer1);
		GossipService.setupReplyHandler(peer1, getGossip());
		new sendThread(tom).start();
		tomp2p.comandLine();

	}
}

class sendThread extends Thread {
	final int ONE_SECOND = 1000;
	tomp2p sv;
	int i = 0;

	public sendThread(tomp2p sv){
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