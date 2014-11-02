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
import net.tomp2p.message.Message;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;

public class tomp2p {
    
    private static Peer peer1 = null;

    private static List<PeerAddress> myNeighbors = null;
    

    private static User u = new User();
        

    public tomp2p()  {
        
        
    }        
   

    @SuppressWarnings("unchecked")
    public static void getMeNeighbors() throws ClassNotFoundException, IOException{
        
        FutureDHT futureDHT = peer1.get(Number160.createHash("neighbors")).start();
        futureDHT.awaitUninterruptibly();
       
        if (futureDHT.isSuccess()) {
            myNeighbors = (List<PeerAddress>)futureDHT.getData().getObject();
        }
        
        if(myNeighbors.contains(peer1.getPeerAddress()))
           myNeighbors.remove(peer1.getPeerAddress()); 
        
        for(PeerAddress pa : myNeighbors){
            System.out.println(pa);
            FutureBootstrap future2 = peer1.bootstrap().setPeerAddress(pa).start();
            future2.awaitUninterruptibly();
        }
        
    }
    
    public static void PeerBuilder() throws ClassNotFoundException, IOException{
        
        List<PeerAddress> myNeighbors = null;
        Random rnd = new Random();
        Bindings b = new Bindings();

        
        peer1 = new PeerMaker(new Number160(rnd)).setTcpPort(10002).setUdpPort(10002).setBindings(b).makeAndListen();
        
        System.out.println("depois do peerMaker");
        

        InetAddress address = Inet4Address.getByName("194.210.234.232");

       
        PeerAddress peerAddress = new PeerAddress(new Number160(1),address,10001,10001);
        
        
        FutureDiscover future = peer1.discover().setPeerAddress(peerAddress).start();
        future.awaitUninterruptibly();
        System.out.println("depois do discover");
        FutureBootstrap future1 = peer1.bootstrap().setPeerAddress(peerAddress).start();
        future1.awaitUninterruptibly();
        
        
        
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       
 }

    public static void comandLine(){

        Scanner keyboard = new Scanner(System.in);

        try{

            while(true){


                getMeNeighbors();

                System.out.println("operation number:");
                System.out.println("1 - offer an item for sale");
                System.out.println("2 - accept a bid");
                System.out.println("3 - bid on a item");
                System.out.println("0 - exit app");

                int numero = keyboard.nextInt();

                switch(numero){
                    case 1:
                        offerItem();
                        break;
                    case 2:
                        acceptBid();
                        break;
                    case 3:
                        bidOnItem();
                        break;
                    case 0:
                        peer1.shutdown();
                        return;
                    default:
                        System.out.println("Invalid operation");
                }

            }
        }catch(Exception e){}
    }
                    

    public static void offerItem(){

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

    public static void acceptBid(){
        System.out.println("not yet done");
    }

    public static void bidOnItem(){
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
               System.out.println("antes do PeerBuilder");
            tomp2p.PeerBuilder();
           }catch(Exception e){
               System.out.println(e.getMessage());
           }
        tomp2p.comandLine();
       }
        
    }
}

