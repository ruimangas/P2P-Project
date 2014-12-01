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


public class RegisterServiceDHT {

    private static String USERNAMES = "username";    
    
    
    
    public static boolean userExists(Peer peer1, String username){
        
        boolean userExists = false;
        Number160 userKey = Number160.createHash(USERNAMES);
        FutureDHT futureDHT = peer1.get(Number160.createHash(username)).setDomainKey(userKey).start();
        futureDHT.awaitUninterruptibly();

        if (futureDHT.isSuccess()) {
            
            System.out.println("user already exists. Please choose another username");
            userExists = true;
            
        }
        
        return userExists;
    }
    
    public static void register(Peer peer1, String username, User u) throws IOException{
        
        Number160 userKey = Number160.createHash(USERNAMES);
        peer1.put(Number160.createHash(username)).setData(new Data(u)).setDomainKey(userKey).start().awaitUninterruptibly();
        
    }
    
    public static void passVerifier(){
        
    }
    
    
    
}
