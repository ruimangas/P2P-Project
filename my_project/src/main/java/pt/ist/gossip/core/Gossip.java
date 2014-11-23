package main.java.pt.ist.gossip.core;

import main.java.pt.ist.gossip.messages.*;
import java.net.Socket;
import java.io.*;

public class Gossip {

    private double nodesSumValue;
    private double nodesWeightValue;
    private double initialWeightValue;
    private double initialSumValue;
    private double id = 0;


    public Gossip(){

    }

    public void init(int nodesSumValue, int nodesWeightValue){

        this.nodesSumValue = nodesSumValue;
        this.nodesWeightValue = nodesWeightValue;

        this.initialSumValue = nodesSumValue;
        this.initialWeightValue = nodesWeightValue;

    }

    public void incrementMessage(){
        this.id = this.id + 1;
    }

    public synchronized void handleMsg(Message msg) throws IOException {

        if(msg.getId() == this.id){
            process(msg);
        }
        else if (msg.getId() > this.id){
            this.id = msg.getId();
            resetGossip();
            process(msg);
        }
    }

    public void incrementMesg(){
        this.id = this.id + 1;
    }

    public synchronized void process(Message msg){

        if(msg.getmType().toString().equals("NODES_SUM")) {

            this.nodesSumValue = this.nodesSumValue + msg.getValue();
            this.nodesWeightValue = this.nodesWeightValue + msg.getWeight();

        }
    }

    public synchronized Message getMessage(MessageType messageType) throws IOException{

        Message msg = null;

        if(messageType.toString().equals("NODES_SUM")){

            msg = new Message(messageType, this.nodesSumValue/2, this.nodesWeightValue/2, this.id);
            this.nodesSumValue = this.nodesSumValue/2;
            this.nodesWeightValue = this.nodesWeightValue/2;

        }

        return msg;
    }

    public double calculateNumberNodes(MessageType messageType){

        if(messageType.toString().equals("NODES_SUM")){

            return this.nodesSumValue/this.nodesWeightValue;

        }
        return 0;
    }

    public void resetGossip(){

        this.nodesWeightValue = this.initialWeightValue;
        this.nodesSumValue = this.initialSumValue;

    }

}
