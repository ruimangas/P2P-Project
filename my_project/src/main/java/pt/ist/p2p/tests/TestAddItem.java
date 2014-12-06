package main.java.pt.ist.p2p.tests;

import static main.java.pt.ist.p2p.tomp2p.getGossip;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import main.java.pt.ist.gossip.MessageType;
import main.java.pt.ist.p2p.GossipService;
import main.java.pt.ist.p2p.Item;
import main.java.pt.ist.p2p.ItemSimple;
import main.java.pt.ist.p2p.OfferItemServiceDHT;
import main.java.pt.ist.p2p.RegisterServiceDHT;
import main.java.pt.ist.p2p.User;
import main.java.pt.ist.p2p.tomp2p;
import net.tomp2p.p2p.Peer;

public class TestAddItem {
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
		new SendThread(tom).start();
		
		Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    	System.out.println( sdf.format(cal.getTime()) );
		
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
		p.shutdown();
	}
}

class SendThread extends Thread {
	final int ONE_SECOND = 1000;
	tomp2p sv;
	int i = 0;

	public SendThread(tomp2p sv){
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

