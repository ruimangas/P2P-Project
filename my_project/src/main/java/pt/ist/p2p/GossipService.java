package main.java.pt.ist.p2p;

import main.java.pt.ist.gossip.Gossip;
import main.java.pt.ist.gossip.Message;
import main.java.pt.ist.gossip.MessageType;
import main.java.pt.ist.gossip.loggingService;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.rpc.ObjectDataReply;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GossipService {

    public static void setGossipValues(User u, Gossip g, Peer p) throws IOException, ClassNotFoundException {

        if(u.getUsername().equals("admin")){
            g.init(1, 1);
            g.initFiles(StorageService.countStoredStuff("ItemSimple", p), 1);
            g.initUsers(StorageService.countStoredStuff("User", p), 1);
        }
        else{
            g.init(1, 0);
            g.initFiles(StorageService.countStoredStuff("ItemSimple", p), 0);
            g.initUsers(StorageService.countStoredStuff("User", p), 0);
        }
    }

    public static void sendMessages(MessageType mType, int i, Peer peer, Gossip gossip, User u) throws IOException, ClassNotFoundException {

        List<PeerAddress> listaPeers = peer.getPeerBean().getPeerMap().getAll();

        if (listaPeers.size()>0) {

            int in = listaPeers.size();
            PeerAddress peerAddress = listaPeers.get(new Random().nextInt(in));

            if(u.getUsername()!= null && u.getUsername().equals("admin") && i%50==0){

                gossip.resetGossipNodes();
                gossip.resetGossipFiles(peer);
                gossip.resetGossipUsers(peer);
                gossip.incrementMesg();

            }

            if (gossip.getNodesWeightValue()>0) {
                Message msg = gossip.getMessage(mType);
                peer.sendDirect(peerAddress).setObject(msg).start();
            }
        }
    }

    public static void setupReplyHandler(Peer peer1, Gossip g)
    {
        final Peer p = peer1;
        final Gossip gossip = g;

        p.setObjectDataReply(new ObjectDataReply()
        {
            @Override
            public Object reply( PeerAddress sender, Object request )
                    throws Exception
            {

                Message m = (Message)request;
                gossip.handleMsg(m, p);
                return null;

            }
        } );
    }
}
