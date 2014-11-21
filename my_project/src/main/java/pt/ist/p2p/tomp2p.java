package main.java.pt.ist.p2p;


import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.*;
import java.io.*;

import net.tomp2p.connection.Bindings;
import net.tomp2p.connection.PeerConnection;
import net.tomp2p.futures.*;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.RequestP2PConfiguration;
import net.tomp2p.p2p.builder.PutBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.replication.Replication;
import net.tomp2p.rpc.DirectDataRPC;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;
import main.java.pt.ist.gossip.core.Gossip;
import main.java.pt.ist.gossip.messages.*;




public class tomp2p {



    private static Peer peer1 = null;

    private static Gossip gossip = new Gossip();

    private static User u = new User();

    private static Random rnd = new Random();

    static List<Socket> peers = new ArrayList<Socket>();
    int id;

    public tomp2p() {


    }


    public static Peer PeerBuilder(String port) throws ClassNotFoundException, IOException {

        Bindings b = new Bindings();

        peer1 = new PeerMaker(new Number160(rnd)).setTcpPort(Integer.parseInt(port)).setUdpPort(Integer.parseInt(port)).setBindings(b).makeAndListen();

        InetAddress address = Inet4Address.getByName("194.210.221.8");

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
                System.out.println("0 - exit app");

                int numero = keyboard.nextInt();
                switch (numero) {
                    case 1:
                        offerItem();
                        break;
                    case 2:
                        searchItem();
                        break;
                    case 4:
                        acceptBid();
                        break;
                    case 6:
                        history();
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

        ItemSimple item = new ItemSimple();
        Scanner keyboard1 = new Scanner(System.in);


        System.out.println("Please, enter the name of the product:");
        String itemTitle = keyboard1.nextLine();

        System.out.println("Please, enter the description of the product:");
        String itemDescription = keyboard1.nextLine();

        item.setName(itemTitle);
        item.setDescription(itemDescription);
        item.setDealer(u.getUsername());


        OfferItemServiceDHT.putDatItem(peer1, item);

    }

    public static void searchItem() throws IOException, ClassNotFoundException {


        List<ItemSimple> items;
        System.out.println("String to search");
        Scanner keyboard1 = new Scanner(System.in);
        String s = keyboard1.nextLine();

        String[] choice = s.split("[ ]");


        if (!(Arrays.asList(choice).contains("and")) || !(Arrays.asList(choice).contains("or")))
            SearchServiceDHT.search(peer1, choice[0]);
        else
            SearchServiceDHT.booleanSearch(peer1, choice[0], choice[2], choice[1]);

        System.out.println("Nao explodiu");
        items = SearchServiceDHT.getMyItems();

        for (ItemSimple i : items)
            System.out.println(i.getName());


        SearchServiceDHT.clearMyShit();
    }

    public static void bidOnItem() throws IOException, ClassNotFoundException {
        System.out.println("not yet done");
    }

    public static void acceptBid() {

        System.out.println(getGossip().calculateNumberNodes(MessageType.NODES_SUM));

    }

    public static void itemDetails() {
        System.out.println("not yet done");
    }

    public static void history() {
        System.out.println("not yet done");
    }

    public static boolean passVerifier(Peer peer1) throws ClassNotFoundException, IOException {



        System.out.println("Do you have an account?");
        Scanner keyboard1 = new Scanner(System.in);
        String s = keyboard1.nextLine();

        if (s.equals("yes")) {

            System.out.println("user:");
            Scanner keyboard2 = new Scanner(System.in);
            String s2 = keyboard2.nextLine();
            FutureDHT futureDHT = peer1.get(Number160.createHash(s2)).start();
            futureDHT.awaitUninterruptibly();

            if (futureDHT.isSuccess()) {
                u = (User) futureDHT.getData().getObject();
                if (u.getUsername().equals("admin")) {
                    System.out.println("you are logged in as admin");
                    getGossip().init(1, 1);
                    getGossip().resetGossip();
                    getGossip().incrementMessage();
                    System.out.println("RESET GOSSIP");
                    return true;
                } else {
                    System.out.println("******** WELCOME TO TOMP2P AUCTIONS ********");
                    System.out.println("you are logged in as " + u.getUsername());
                    getGossip().init(1, 0);
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

            FutureDHT futureDHT = peer1.get(Number160.createHash(s)).start();
            futureDHT.awaitUninterruptibly();

            if (futureDHT.isSuccess()) {
                System.out.println("user already exists. Please choose another username");

            } else {
                u.setUsername(s);
                peer1.put(Number160.createHash(s)).setData(new Data(u)).start().awaitUninterruptibly();
                System.out.println("you are logged in as " + u.getUsername());
                break;
            }
        }
    }

    public static void registerAdmin() throws IOException {
        User adminUser = new User();
        String admin = "admin";
        adminUser.setUsername(admin);
        peer1.put(Number160.createHash(admin)).setData(new Data(adminUser)).start().awaitUninterruptibly();
    }

    public static Gossip getGossip() {
        return gossip;
    }

    public void sendMessages(MessageType mType) throws IOException {

        Message msg = getGossip().getMessage(mType);

        RequestP2PConfiguration requestP2PConfiguration = new RequestP2PConfiguration(1,10,0);
        FutureDHT futureDHT = peer1.send(Number160.createHash(new Random().nextInt())).setObject(msg).setRequestP2PConfiguration(requestP2PConfiguration).start();
        futureDHT.awaitUninterruptibly();

        System.out.println("VALOR: " + Math.round(getGossip().calculateNumberNodes(mType)));

    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        tomp2p tom = new tomp2p();

        Peer p = tomp2p.PeerBuilder(args[0]);

        registerAdmin();
        passVerifier(p);
        setupReplyHandler(p);
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

            System.err.println("I'm "+p.getPeerID()+" and I just got the message ["+request+"] from "+sender.getID());
            Message m = (Message)request;
            getGossip().handleMsg(m);
            return null;

            }
        } );
    }
}

class sendThread extends Thread {
    tomp2p sv;

    public sendThread(tomp2p sv){
        this.sv = sv;
    }

    @Override
    public void run() {
        try{
            while (true){
                Thread.sleep(100);
                sv.sendMessages(MessageType.NODES_SUM);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


