package main.java.pt.ist.p2p.tests;


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





public class IdleNode {

    private static Random rnd = new Random();
    private static Peer peer1 = null;
    
    public static Peer PeerBuilder(String port) throws ClassNotFoundException,IOException {

        int porto = Integer.parseInt(port) + new Random().nextInt(3000);

        Bindings b = new Bindings();

        peer1 = new PeerMaker(new Number160(rnd.nextInt())).setTcpPort(porto).setUdpPort(porto).setBindings(b).setEnableIndirectReplication(true).makeAndListen();

        InetAddress address = Inet4Address.getByName("194.210.222.55");

        PeerAddress peerAddress = new PeerAddress(new Number160(1), address,10001, 10001);

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

    public static void main(String[] args){
        
        try {
            
            PeerBuilder(args[0]);
        
        } catch (ClassNotFoundException e) {
        
            e.printStackTrace();
        } catch (IOException e) {
         
            e.printStackTrace();
        }
        
        System.out.println("Hello, Im just a idle dude. Im only here to be a part of the network :D");
    }
    
}
