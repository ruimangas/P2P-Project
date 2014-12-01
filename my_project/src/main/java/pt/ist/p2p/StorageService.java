package main.java.pt.ist.p2p;

import net.tomp2p.peers.Number480;
import net.tomp2p.storage.Data;
import net.tomp2p.p2p.Peer;
import java.io.IOException;
import java.util.Map;

public class StorageService {

    public static int countStoredStuff(String className, Peer peer) throws IOException, ClassNotFoundException {

        int activeStuff = 0;

        Map<Number480, Data> map = peer.getPeerBean().getStorage().map();

        for (Object value : map.values()) {

            Data data = (Data) value;

            try {
                if (data.getObject().getClass().getName().equals("main.java.pt.ist.p2p." + className)) {
                    activeStuff++;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return activeStuff;
    }
}
