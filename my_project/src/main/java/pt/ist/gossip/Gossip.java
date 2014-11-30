package main.java.pt.ist.gossip;

import main.java.pt.ist.p2p.StorageService;
import main.java.pt.ist.p2p.masterPeer;
import main.java.pt.ist.p2p.tomp2p;

import java.io.*;

import net.tomp2p.p2p.Peer;
import net.tomp2p.replication.ReplicationExecutor;

public class Gossip {

    private final int REPLICATION_FACTOR = 6;

    private double nodesSumValue;
    private double nodesWeightValue;
    private double initialWeightValue;
    private double initialSumValue;

    private double numItemsSum;
    private double numItemsWeight;

    private double initialItemsWeight;


    private double numUsersValue;
    private double numUsersWeight;

    private double initialNumUsersWeight;

    private double messageId = 0;

    public Gossip(){

    }

    public void init(int nodesSumValue, int nodesWeightValue){

        this.nodesSumValue = nodesSumValue;
        this.nodesWeightValue = nodesWeightValue;

        this.initialSumValue = nodesSumValue;
        this.initialWeightValue = nodesWeightValue;

    }

    public void initFiles(double numItemsSum, double numItemsWeight){

        this.numItemsSum = numItemsSum;
        this.numItemsWeight = numItemsWeight;

        this.initialItemsWeight = numItemsWeight;

    }

    public void initUsers(double numUsersValue, double numUsersWeight){

        this.numUsersValue = numUsersValue;
        this.numUsersWeight = numUsersWeight;

        this.initialNumUsersWeight = numUsersWeight;

    }

    public void incrementMessage(){
        this.messageId = this.messageId + 1;
    }

    public synchronized void handleMsg(Message msg, Peer peer) throws IOException, ClassNotFoundException {

        if(msg.getId() == this.messageId){
            process(msg);
        }
        else if (msg.getId() > this.messageId){
            this.messageId = msg.getId();
            resetGossipNodes();
            resetGossipFiles(peer);
            process(msg);
        }
    }

    public double getNodesWeightValue() {
        return nodesWeightValue;
    }

    public double getNumItemsWeight() {
        return numItemsWeight;
    }

    public double getNumUsersValue() {
        return numUsersValue;
    }

    public void incrementMesg(){
        this.messageId = this.messageId + 1;
    }

    public synchronized void process(Message msg){


        if(msg.getmType().toString().equals("NODES_SUM")) {

            this.nodesSumValue = this.nodesSumValue + msg.getValue();
            this.nodesWeightValue = this.nodesWeightValue + msg.getWeight();

        }

        if(msg.getmType().toString().equals("ITEMS_SUM")){

            this.numItemsSum = this.numItemsSum + msg.getValue();
            this.numItemsWeight = this.numItemsWeight + msg.getWeight();
        }

        if(msg.getmType().toString().equals("USERS_SUM")){

            this.numUsersValue = this.numUsersValue + msg.getValue();
            this.numUsersWeight = this.numUsersWeight + msg.getWeight();
        }

       //System.out.println("PESO FILES: " + this.numItemsWeight);

    }

    public synchronized Message getMessage(MessageType messageType) throws IOException{

        Message msg = null;

        if(messageType.toString().equals("NODES_SUM")){

            msg = new Message(messageType, this.nodesSumValue/2, this.nodesWeightValue/2, this.messageId);
            this.nodesSumValue = this.nodesSumValue/2;
            this.nodesWeightValue = this.nodesWeightValue/2;
        }

        if(messageType.toString().equals("ITEMS_SUM")){

            msg = new Message(messageType, this.numItemsSum/2, this.numItemsWeight/2, this.messageId);
            this.numItemsSum = this.numItemsSum/2;
            this.numItemsWeight = this.numItemsWeight/2;
        }

        if(messageType.toString().equals("USERS_SUM")){

            msg = new Message(messageType, this.numUsersValue/2, this.numUsersWeight/2, this.messageId);
            this.numUsersValue = this.numUsersValue/2;
            this.numUsersWeight = this.numUsersWeight/2;
        }

        return msg;
    }

    public double calculateNumberNodes(MessageType messageType){

        if(messageType.toString().equals("NODES_SUM")){

            return this.nodesSumValue/this.nodesWeightValue;

        }
        return 0;
    }

    public double calculateNumberItems(MessageType messageType){

        if(messageType.toString().equals("ITEMS_SUM")){

            return (this.numItemsSum/this.numItemsWeight)/REPLICATION_FACTOR;

        }
        return 0;
    }

    public double calculateNumerUsers(MessageType messageType){

        if(messageType.toString().equals("USERS_SUM")){

            return (this.numUsersValue/this.numUsersWeight)/REPLICATION_FACTOR;
        }

        return 0;
    }

    public void resetGossipNodes(){

        this.nodesWeightValue = this.initialWeightValue;
        this.nodesSumValue = this.initialSumValue;

    }

    public void resetGossipFiles(Peer peer) throws IOException, ClassNotFoundException {

        this.numItemsSum = StorageService.countStoredStuff("ItemSimple", peer);
        this.numItemsWeight = this.initialItemsWeight;
    }

    public void resetGossipUsers(Peer peer) throws IOException, ClassNotFoundException {

        this.numUsersValue = StorageService.countStoredStuff("User", peer);
        this.numUsersWeight = this.initialNumUsersWeight;

    }

}
