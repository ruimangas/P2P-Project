package main.java.pt.ist.p2p;


import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.*;
import java.io.*;

import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.futures.FutureCreate;
import net.tomp2p.message.Message;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.p2p.builder.PutBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.replication.Replication;
import net.tomp2p.rpc.DirectDataRPC;
import net.tomp2p.storage.Data;




public class tomp2p {

    private static Peer peer1 = null;

    private static User u = new User();

    private static Random rnd = new Random();

    public tomp2p()  {


    }

    public static void PeerBuilder(String port) throws ClassNotFoundException, IOException{

        Bindings b = new Bindings();

        peer1 = new PeerMaker(new Number160(rnd)).setTcpPort(Integer.parseInt(port)).setUdpPort(Integer.parseInt(port)).setBindings(b).makeAndListen();

        InetAddress address = Inet4Address.getByName("194.210.223.98");

        PeerAddress peerAddress = new PeerAddress(new Number160(1),address,10001,10001);


        FutureDiscover future = peer1.discover().setPeerAddress(peerAddress).start();
        future.awaitUninterruptibly();

        FutureBootstrap future1 = peer1.bootstrap().setPeerAddress(peerAddress).start();
        future1.awaitUninterruptibly();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void comandLine(){

        Scanner keyboard = new Scanner(System.in);

        try{

            while(true){


                System.out.println(peer1.getPeerBean().getPeerMap().getAll());

                System.out.println("operation number:");
                System.out.println("1 - offer an item for sale");
                System.out.println("2 - search for an item to buy");
                System.out.println("3 - bid on an item");
                System.out.println("4 - accept a bid");
                System.out.println("5 - view item details");
                System.out.println("6 - purchase and bidding history");
                System.out.println("0 - exit app");

                int numero = keyboard.nextInt();
                switch(numero){
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
                    case 0:
                        peer1.shutdown();
                        return;
                    default:
                        System.out.println("Invalid operation");
                }

            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    public static void offerItem() throws IOException, ClassNotFoundException {

        Item item = new Item();
        Scanner keyboard1 = new Scanner(System.in);
        Scanner keyboard2 = new Scanner(System.in);

        System.out.println("Please, enter the name of the product:");
        String itemTitle = keyboard1.nextLine();
        System.out.println("Please, enter the description of the product:");
        String itemDescription = keyboard2.nextLine();
        item.setName(itemTitle);
        item.setDescription(itemDescription);
        item.setDealer(u.getUsername());
        u.setOfferedItem(itemTitle);
    }

    public static void searchItem() throws IOException, ClassNotFoundException{

        System.out.println("not yet done");

    }

    public static void bidOnItem() throws IOException, ClassNotFoundException {
        System.out.println("not yet done");
    }

    public static void acceptBid(){

        System.out.println("not yet done");
    }

    public static void itemDetails(){
        System.out.println("not yet done");
            }

    public static void history(){
        System.out.println("not yet done");
    }


    public static boolean verificaUserTxt(String user){

        boolean testaUser = false;

            try{

                BufferedReader br = new BufferedReader(new FileReader("users.txt"));
                String line;

                while ((line = br.readLine()) != null){
                    if(user.equals(line)){
                        testaUser = true;
                    }
                }


            }catch(Exception e){
                System.out.println("erro");
            }

            return testaUser;
        }

    public static boolean passVerifier(){

        boolean accepted;
        Scanner sc = new Scanner(System.in);

        while(true){

            System.out.println("User:");
            String user = sc.nextLine();

            if(verificaUserTxt(user)){
                System.out.println("***** WELCOME TO P2P AUCTIONS *****");
                u.setUsername(user);
                accepted = true;
                break;
            }
            else System.out.println("wrong user");
        }

        return accepted;
    }


    public static void main (String[] args){

        if(tomp2p.passVerifier()){
            try{
                tomp2p.PeerBuilder(args[0]);
            }catch(Exception e){
               System.out.println(e.getMessage());
           }
        tomp2p.comandLine();
        }
    }
}
