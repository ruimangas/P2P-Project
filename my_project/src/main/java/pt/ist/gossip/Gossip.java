package main.java.pt.ist.gossip;

import main.java.pt.ist.p2p.tomp2p;

import java.io.*;

public class Gossip {

    private double nodesSumValue;
    private double nodesWeightValue;
    private double initialWeightValue;
    private double initialSumValue;

    private double numItemsSum;
    private double numItemsWeight;

    private double initialItemsWeight;

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

    public void incrementMessage(){
        this.messageId = this.messageId + 1;
    }

    public synchronized void handleMsg(Message msg) throws IOException {

        if(msg.getId() == this.messageId){
            process(msg);
        }
        else if (msg.getId() > this.messageId){
            this.messageId = msg.getId();
            resetGossipNodes();
            resetGossipFiles();
            process(msg);
        }
    }

    public double getNodesWeightValue() {
        return nodesWeightValue;
    }

    public double getNumItemsWeight() {
        return numItemsWeight;
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

        //System.out.println("PESO_NODES: " + this.nodesWeightValue);
        //System.out.println("PESO_FILES: " + this.numItemsWeight);

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

            return this.numItemsSum/this.numItemsWeight;

        }
        return 0;
    }

    public void resetGossipNodes(){

        this.nodesWeightValue = this.initialWeightValue;
        this.nodesSumValue = this.initialSumValue;

    }

    public void resetGossipFiles(){

        this.numItemsSum = new tomp2p().getActualNumberFiles();
        this.numItemsWeight = this.initialItemsWeight;
    }

}
