
package main.java.pt.ist.p2p;


import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.*;

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
import net.tomp2p.storage.Data;
import main.java.pt.ist.gossip.Gossip;

import static main.java.pt.ist.p2p.tomp2p.getGossip;


public class tomp2p {

    private static Peer peer1 = null;

    private static User u = new User();

    private static String USERNAMES = "username";

    private static Gossip gossip = new Gossip();

    public tomp2p() {

    }

    public static Peer PeerBuilder(String port) throws ClassNotFoundException, IOException {

        Random random = new Random();
        Bindings bindings = new Bindings();

        peer1 = new PeerMaker(new Number160(random)).setTcpPort(Integer.parseInt(port)).setUdpPort(Integer.parseInt(port)).setEnableIndirectReplication(true).setBindings(bindings).makeAndListen();

        InetAddress address = Inet4Address.getByName("194.210.222.69");

        PeerAddress peerAddress = new PeerAddress(new Number160(1), address, 10001, 10001);

        FutureDiscover future = peer1.discover().setPeerAddress(peerAddress).start();
        future.awaitUninterruptibly();

        FutureBootstrap future1 = peer1.bootstrap().setPeerAddress(peerAddress).start();
        future1.awaitUninterruptibly();

        return peer1;
    }

    public static void comandLine() {

        Scanner keyboard = new Scanner(System.in);

        try {

            while (true) {


                System.out.println("operation number:");
                System.out.println("1 - offer an item for sale");
                System.out.println("2 - search for an item to buy");
                System.out.println("3 - bid on an item");
                System.out.println("4 - accept a bid");
                System.out.println("5 - view item details");
                System.out.println("6 - purchase and bidding history");
                if(u.getUsername().equals("admin")) System.out.println("7 - user management");
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
                        bidOnItem();
                        break;
                    case 4:
                        acceptBid();
                        break;
                    case 5:
                        itemDetails();
                        break;
                    case 6:
                        history();
                        break;
                    case 7:
                        userManagement();
                        break;
                    case 8:
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

        int i=0;

        Map<Number480, Data> map = peer1.getPeerBean().getStorage().map();
        for (Object o : map.entrySet()) {
            Map.Entry thisEntry = (Map.Entry) o;
            Object value = thisEntry.getValue();
            Data data = (Data) value;

            try {
                if (data.getObject().getClass().getName().equals("main.java.pt.ist.p2p.ItemSimple")) {
                    ItemSimple n = (ItemSimple) data.getObject();
                    i++;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(i);

    }


    public static void offerItem() throws IOException, ClassNotFoundException {

        ItemSimple item = new ItemSimple();
        Scanner keyboard1 = new Scanner(System.in);

        System.out.println("Please, enter the name of the product:");
        String itemTitle = keyboard1.nextLine();

        System.out.println("Please, enter the description of the product:");
        String itemDescription = keyboard1.nextLine();

        item.setName(itemTitle.toLowerCase());
        item.setDescription(itemDescription);
        item.setDealer(u.getUsername());
        u.setOfferedItem(itemTitle);

        OfferItemServiceDHT.putDatItem(peer1, item);

    }

    public static void searchItem() throws IOException, ClassNotFoundException {


        List<ItemSimple> items = new ArrayList<ItemSimple>();
        System.out.println("String to search:");
        Scanner keyboard1 = new Scanner(System.in);
        String s = keyboard1.nextLine();
        String[] choice = s.split("[ ]");
        List<String> myOperators = new ArrayList<String>();
        List<String> myOperands = new ArrayList<String>();
        List<Number160> hashSimple;

        
        for (String st : choice) {
            if (st.toLowerCase().equals("and") || st.toLowerCase().equals("or") || st.toLowerCase().equals("not"))
                myOperators.add(st.toLowerCase());
            else
                myOperands.add(st);
        }

        try {  
            
            if(myOperators.size() == 0){

                hashSimple = SearchServiceDHT.findReference(peer1, myOperands.get(0));
                
                items = SearchServiceDHT.search(peer1,hashSimple);
             
            
            }else{
                
              items = SearchServiceDHT.booleanSearch(peer1, myOperators, myOperands);
                    
            }
        
       }catch (Exception e) {
          System.out.println(e.getMessage());
       }

        for (ItemSimple item : items)
            System.out.println("Name: " + item.getName() + " Dealer: " + item.getDealer());

        SearchServiceDHT.clearMySearch();

    }

    public static void bidOnItem() throws IOException, ClassNotFoundException {
        System.out.println("not yet done");
    }

    public static void acceptBid() {

        System.out.println("not yet done");
    }

    public static void itemDetails() {
        
        String itemName;
        String dealer;
        Scanner keyboard1 = new Scanner(System.in);
        
        System.out.println("Item name:");
        
        itemName = keyboard1.nextLine();
        
        System.out.println("Dealer:");
        dealer = keyboard1.nextLine();
        
        
       try {
        
           SeeItemsDetailsServiceDHT.seeItemsDetails(peer1, itemName, dealer);
    
       } catch (ClassNotFoundException e) {
        
          e.printStackTrace();
       } catch (IOException e) {
        
          e.printStackTrace();
       }
        
    }

    public static void history() {
        System.out.println("not yet done");
    }

    public static void userManagement(){

        if(u.getUsername().equals("admin")){

            System.out.println("There are " + Math.round(getGossip().calculateNumberNodes(MessageType.NODES_SUM)) + " nodes.");
            System.out.println("There are " + Math.round(getGossip().calculateNumberItems(MessageType.ITEMS_SUM))  + " items.");
            System.out.println("There are " + Math.round(getGossip().calculateNumerUsers(MessageType.ITEMS_SUM))  + " Users.");

        }

        else System.out.println("Invalid operation");
    }

    public static boolean passVerifier(Peer peer1) throws ClassNotFoundException, IOException {

        User user;
        System.out.println("Do you have an account?");
        Scanner keyboard1 = new Scanner(System.in);
        String s = keyboard1.nextLine();

        if (s.equals("yes")) {

            System.out.println("user:");
            Scanner keyboard2 = new Scanner(System.in);
            String s2 = keyboard2.nextLine();
            String myHash = s2 + USERNAMES;
            FutureDHT futureDHT = peer1.get(Number160.createHash(myHash)).start();
            futureDHT.awaitUninterruptibly();

            if (futureDHT.isSuccess()) {
                user = (User) futureDHT.getData().getObject();
                if (user.getUsername().equals("admin")) {
                    System.out.println("you are logged in as admin");
                    u.setUsername("admin");
                    return true;
                } else {
                    u.setUsername(user.getUsername());
                    System.out.println("******** WELCOME TO TOMP2P AUCTIONS ********");
                    System.out.println("you are logged in as " + user.getUsername());
                    return true;
                }

            } else {
                System.out.println("User not found");
                register();
                return true;
            }
        } else register();

        return false;
    }

    public static void register() throws IOException {

        System.out.println("Register Menu");

        while (true) {

            System.out.println("Username:");

            Scanner keyboard1 = new Scanner(System.in);
            String s = keyboard1.nextLine();
            String userKey = s + USERNAMES;
            FutureDHT futureDHT = peer1.get(Number160.createHash(userKey)).start();
            futureDHT.awaitUninterruptibly();

            if (futureDHT.isSuccess()) {
                System.out.println("user already exists. Please choose another username");

            } else {
                u.setUsername(s);
                peer1.put(Number160.createHash(userKey)).setData(new Data(u)).start().awaitUninterruptibly();
                System.out.println("you are logged in as " + u.getUsername());
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










