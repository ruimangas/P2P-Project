
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
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;
import main.java.pt.ist.gossip.Gossip;


public class tomp2p {

    private static Peer peer1 = null;

    private static User u = new User();

    private static Random rnd = new Random();

    private static String USERNAMES = "username";

    private static Gossip gossip = new Gossip();

    private static ArrayList<String> allUsers = new ArrayList<String>();

    
    public tomp2p() {


    }

    public static Peer PeerBuilder(String port) throws ClassNotFoundException, IOException {

        Bindings b = new Bindings();

        peer1 = new PeerMaker(new Number160(rnd)).setTcpPort(Integer.parseInt(port)).setUdpPort(Integer.parseInt(port)).setBindings(b).setEnableIndirectReplication(true).makeAndListen();


        InetAddress address = Inet4Address.getByName(getMyIp());


        PeerAddress peerAddress = new PeerAddress(new Number160(1), address, 10001, 10001);

        FutureDiscover future = peer1.discover().setPeerAddress(peerAddress).start();
        future.awaitUninterruptibly();

        FutureBootstrap future1 = peer1.bootstrap().setPeerAddress(peerAddress).start();
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


    public static void offerItem() throws IOException, ClassNotFoundException {

        ItemSimple itemSimple;
        int idItem = rnd.nextInt(); 
        
        Item item = new Item(idItem);
        
        Scanner keyboard1 = new Scanner(System.in);

        System.out.println("Please, enter the name of the product:");
        String itemTitle = keyboard1.nextLine();

        System.out.println("Please, enter the description of the product:");
        String itemDescription = keyboard1.nextLine();

        itemSimple = new ItemSimple(itemTitle,u.getUsername(),idItem);
        
        item.setName(itemTitle.toLowerCase());
        item.setDescription(itemDescription);
        item.setDealer(u.getUsername());
        u.setOfferedItem(item);

        try{
        
            OfferItemServiceDHT.putDatItem(peer1, itemSimple,item);
        
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    public double getActualNumberFiles(){
        return u.getOfferedItems().size();
    }

    public static void searchItem() throws IOException, ClassNotFoundException {


        List<ItemSimple> items = new ArrayList<ItemSimple>();
        System.out.println("String to search:");
        Scanner keyboard1 = new Scanner(System.in);
        String s = keyboard1.nextLine();
        String[] choice = s.split("[ ]");
        List<String> myOperators = new ArrayList<String>();
        List<String> myOperands = new ArrayList<String>();
        List<String> myQuery =  new ArrayList<String>();
        List<Number160> hashSimple;

        
        for (String st : choice) {
            if (st.toLowerCase().equals("and") || st.toLowerCase().equals("or"))
                myOperators.add(st.toLowerCase());
            else
                myOperands.add(st);
            myQuery.add(st);
        }
        
        
       try {  
            
            if(myOperators.size() == 0){
            
                hashSimple = new ArrayList<Number160>();
                
     
                hashSimple = SearchServiceDHT.findReference(peer1, myOperands.get(0));
                
                items = SearchServiceDHT.search(peer1,hashSimple);
                
            
            }else{
                
              items = SearchServiceDHT.booleanSearch(peer1, myOperators, myOperands, myQuery);
                    
            }
        
       }catch (Exception e) {
          System.out.println(e.getMessage());
       }

        for (ItemSimple item : items){
           
         
              System.out.println("Name: " + SearchServiceDHT.searchItem(peer1, item).getName() + " Dealer: " + SearchServiceDHT.searchItem(peer1, item).getDealer());
            
        }
        
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
        
        
        try {
           
            HistoryServiceDHT.seeHistory(peer1, u);
        
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }

    public static void userManagement(){

        if(u.getUsername().equals("admin")){

            System.out.println("There are " + Math.round(getGossip().calculateNumberNodes(MessageType.NODES_SUM)) + " nodes.");
            System.out.println("There are " + Math.round(getGossip().calculateNumberItems(MessageType.ITEMS_SUM))  + " files.");
            System.out.println("There are " + allUsers.size() + " users.");
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
            String username = keyboard2.nextLine();
            Number160 userKey = Number160.createHash(USERNAMES); 
            FutureDHT futureDHT = peer1.get(Number160.createHash(username)).setDomainKey(userKey).start();
            futureDHT.awaitUninterruptibly();
            String userName = "";
            
            if (futureDHT.isSuccess()) {
                user = (User) futureDHT.getData().getObject();
                
                if (user.getUsername().equals("admin")) {
                    
                    System.out.println("you are logged in as admin");
                    userName = "admin";
                    
                } else {
                    
                    System.out.println("******** WELCOME TO TOMP2P AUCTIONS ********");
                    System.out.println("you are logged in as " + user.getUsername());
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
        } else register();

        return false;
    }

    public static void register() throws IOException {

        System.out.println("Register Menu");

        while (true) {

            System.out.println("Username:");

            Scanner keyboard1 = new Scanner(System.in);
            String username = keyboard1.nextLine();
            Number160 userKey = Number160.createHash(USERNAMES);
            FutureDHT futureDHT = peer1.get(Number160.createHash(username)).setDomainKey(userKey).start();
            futureDHT.awaitUninterruptibly();

            if (futureDHT.isSuccess()) {
                System.out.println("user already exists. Please choose another username");

            } else {
                u.setUsername(username);
                peer1.put(Number160.createHash(username)).setData(new Data(u)).setDomainKey(userKey).start().awaitUninterruptibly();
                System.out.println("you are logged in as " + u.getUsername());
                break;
            }
        }
    }

    public static Gossip getGossip() {
        return gossip;
    }


    public void sendMessages(MessageType mType, int i) throws IOException {

        List<PeerAddress> listaPeers = peer1.getPeerBean().getPeerMap().getAll();

        List<PeerAddress> arrayPeer = new ArrayList<PeerAddress>();

        for(PeerAddress pa : listaPeers){
            if (!(pa.getID().equals(new Number160(1)))){
                arrayPeer.add(pa);
            }
        }

        if (!arrayPeer.isEmpty()) {

            int in = arrayPeer.size();
            PeerAddress peerAddress = arrayPeer.get(new Random().nextInt(in));

            if(u.getUsername().equals("admin") && i%50==0){

                getGossip().resetGossipNodes();
                getGossip().resetGossipFiles();
                getGossip().incrementMesg();

            }

            if (getGossip().getNodesWeightValue()>0 && getGossip().getNumItemsWeight()>0) {
                Message msg = getGossip().getMessage(mType);
                peer1.sendDirect(peerAddress).setObject(msg).start();
            }

        } else {

            if(u.getUsername().equals("admin") && i%50==0){

                getGossip().resetGossipNodes();
                getGossip().resetGossipFiles();
                getGossip().incrementMesg();

            }

            Message msg = getGossip().getMessage(mType);
            peer1.sendDirect(peer1.getPeerAddress()).setObject(msg).start();
        }

    }

    public void activeUser() {

        String activeUser = u.getUsername();
        String myKey = "activeUsers";
        FutureDHT futureDHT;

        try {
            futureDHT = peer1.add(Number160.createHash(myKey)).setData(new Data(activeUser)).start();
            futureDHT.awaitUninterruptibly();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void countUsers(int i) {

        if (i%30==0) {

            FutureDHT futureDHT;
            futureDHT = peer1.get(Number160.createHash("activeUsers")).setAll().start();
            futureDHT.awaitUninterruptibly();
            Iterator<Data> iterator = futureDHT.getDataMap().values().iterator();
            while (iterator.hasNext()) {
                try {
                    String userName = (String) iterator.next().getObject();

                    if(!allUsers.contains(userName)){
                        allUsers.add(userName);
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setGossipValues(){

        if(u.getUsername().equals("admin")){
            getGossip().init(1,1);
            getGossip().initFiles(new tomp2p().getActualNumberFiles(), 1);
        }
        else{
            getGossip().init(1,0);
            getGossip().initFiles(new tomp2p().getActualNumberFiles(), 0);
        }
    }


    public static void main(String[] args) throws ClassNotFoundException, IOException {

        tomp2p tom = new tomp2p();

        Peer p = tomp2p.PeerBuilder(args[0]);

        passVerifier(p);
        tom.activeUser();
        setGossipValues();
        setupReplyHandler(p);
        new countUsers(tom).start();
        new sendThread(tom).start();
        tomp2p.comandLine();

    }


    private static void setupReplyHandler(Peer peer1)
    {
        final Peer p = peer1;

        p.setObjectDataReply(new ObjectDataReply()
        {
            @Override
            public Object reply( PeerAddress sender, Object request )
                    throws Exception
            {

                Message m = (Message)request;
                getGossip().handleMsg(m);
                return null;

            }
        } );
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
                sv.sendMessages(MessageType.NODES_SUM, i);
                sv.sendMessages(MessageType.ITEMS_SUM, i);
                i++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class countUsers extends Thread {
    final int ONE_SECOND = 1000;
    tomp2p sv;
    int i=0;

    public countUsers(tomp2p sv){
        this.sv = sv;
    }

    @Override
    public void run(){

        try{
            while(true){
                Thread.sleep(ONE_SECOND);
                sv.countUsers(i);
                i++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}




