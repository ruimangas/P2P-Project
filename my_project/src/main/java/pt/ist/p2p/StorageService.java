package main.java.pt.ist.p2p;

import net.tomp2p.peers.Number160;
import net.tomp2p.peers.Number480;
import net.tomp2p.storage.Data;
import net.tomp2p.p2p.Peer;
import net.tomp2p.storage.StorageGeneric;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class StorageService {

    public static int countStoredStuff(String classname, Peer peer) throws IOException, ClassNotFoundException {

        int activeStuff = 0;

        StorageGeneric storage = peer.getPeerBean().getStorage();

        Map<Number480, Data> map = peer.getPeerBean().getStorage().map();

        for (Map.Entry<Number480, Data> entry : map.entrySet()) {

            Number160 peerResponsible = storage.findPeerIDForResponsibleContent(entry.getKey().getLocationKey());

            if (peerResponsible.equals(peer.getPeerID())) {

                String classN = entry.getValue().getObject().getClass().getName();

                if (classN.equals("main.java.pt.ist.p2p." + classname)) {
                    activeStuff++;
                }
            }
        }

        return activeStuff;
    }
}