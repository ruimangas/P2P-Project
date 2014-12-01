package main.java.pt.ist.p2p;

import java.io.IOException;
import java.util.*;

import main.java.pt.ist.gossip.Gossip;
import main.java.pt.ist.gossip.MessageType;
import net.tomp2p.connection.Bindings;
import net.tomp2p.connection.ChannelCreator;
import net.tomp2p.connection.Sender;
import net.tomp2p.futures.FutureChannelCreator;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureResponse;
import net.tomp2p.message.Message;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.Number480;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;
import net.tomp2p.storage.Data;


public class masterPeer {


    private static Peer peerMaster;

    private static Gossip gossip = new Gossip();


    public masterPeer(){

    }

    public static void setGossipValues() throws IOException, ClassNotFoundException {

       getGossip().init(1,0);
       getGossip().initFiles(StorageService.countStoredStuff("ItemSimple", peerMaster), 0);
       getGossip().initUsers(StorageService.countStoredStuff("User", peerMaster), 0);
    }

    public static Gossip getGossip() {
        return gossip;
    }

    public Peer getPeer() { return peerMaster; }

    public static void main(String[] args ){


        Bindings b = new Bindings();
        Number160 id = new Number160(1);

        try{

            peerMaster = new PeerMaker(id).setTcpPort(10001).setUdpPort(10001).setEnableIndirectReplication(true).setBindings(b).makeAndListen();
            peerMaster.getConfiguration().setBehindFirewall(true);

        }catch(Exception e){

            System.out.println(e.getMessage());
        }

        try {
            setGossipValues();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        GossipService.setupReplyHandler(peerMaster, getGossip());
        masterPeer m = new masterPeer();
        new sThread(m).start();


        while(true) {

/*            System.out.println("tamanho: " + peerMaster.getPeerBean().getPeerMap().getAll().size());

            Map<Number480, Data> map = peerMaster.getPeerBean().getStorage().map();
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

            System.out.println("---------------------------------------");*/

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

           System.out.println("my peers:" + peerMaster.getPeerBean().getPeerMap().getAll());
        }
    }
}

class sThread extends Thread {
    final int ONE_SECOND = 1000;
    masterPeer sv;
    int i = 0;

    public sThread(masterPeer sv){
        this.sv = sv;
    }

    @Override
    public void run() {
        try{
            while (true){
                Thread.sleep(ONE_SECOND);
                GossipService.sendMessages(MessageType.NODES_SUM, i, sv.getPeer(), masterPeer.getGossip(), new tomp2p().getU());
                GossipService.sendMessages(MessageType.ITEMS_SUM, i, sv.getPeer(), masterPeer.getGossip(), new tomp2p().getU());
                GossipService.sendMessages(MessageType.USERS_SUM, i, sv.getPeer(), masterPeer.getGossip(), new tomp2p().getU());
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