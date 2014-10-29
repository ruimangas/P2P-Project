package main.java.pt.ist.p2p;


import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.*;

import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;




public class tomp2p {
    
    private static Peer peer1 = null;
        
    public tomp2p()  {
        
        
    }        
   
  //  public static Peer MasterPeer = masterPeer.getMasterPeer();
    
    public static void PeerBuilder() throws ClassNotFoundException, IOException{
        
        Random rnd = new Random();
        Bindings b = new Bindings();
     // b.addInterface("eth0");
        
        peer1 = new PeerMaker(new Number160(rnd)).setTcpPort(10002).setUdpPort(10002).setBindings(b).makeAndListen();
        
        System.out.println("depois do peerMaker");
        
        InetAddress address = Inet4Address.getByName("127.0.0.1");
       
        PeerAddress peerAddress = new PeerAddress(new Number160(1),address,10001,10001);
        
        
        FutureDiscover future = peer1.discover().setPeerAddress(peerAddress).start();
        future.awaitUninterruptibly();
        System.out.println("depois do discover");
        FutureBootstrap future1 = peer1.bootstrap().setPeerAddress(peerAddress).start();
        future1.awaitUninterruptibly();
        System.out.println("my peers:" + peer1.getPeerBean().getPeerMap().getAll()); 
        
        
        FutureDHT futureDHT = peer1.get(Number160.createHash("master")).start();
        futureDHT.awaitUninterruptibly();
        
        if (futureDHT.isSuccess()) {
            System.out.println(futureDHT.getData().getObject().toString());
        }
        
       }

    public static void comandLine(){
        
        String i = "";
        String[] commands;
        Scanner sc1 = new Scanner(System.in);
        
        do{
        System.out.println("searchFile - para procurar ficheiro");
            i = sc1.nextLine();    
            commands = i.split("[ ]");
            
            if(commands[0].equals("searchFile"))
                System.out.println("comando ainda nao implementado");
            
        }while(!commands[0].equals("logoff"));
        
        peer1.shutdown();
     }
    
    
    
    public static boolean passVerifier(){
        
        boolean accepted = false;
        String userPass = "";
        String[] user;
        Scanner sc = new Scanner(System.in);
        
        while(true){
            System.out.println("############");
            System.out.println("User and Pass:");
            userPass = sc.nextLine();
            user = userPass.split("[ ]");
            
            if(user[0].equals("root") && user[1].equals("root")){
                System.out.println("dentro da validacao");
                accepted = true;
                break;
            }else{
                
                System.out.println("Credenciais incorrectas tente outra vez");
            }
                
            
        }
        
        return accepted;
   }
    
    
    public static void main (String[] args){
        
        
 //      if(tomp2p.passVerifier()){
           try{
               System.out.println("antes do PeerBuilder");
            tomp2p.PeerBuilder();
           }catch(Exception e){
               System.out.println(e.getMessage());
           }
       // tomp2p.comandLine();
   //    }
        
    }

}
