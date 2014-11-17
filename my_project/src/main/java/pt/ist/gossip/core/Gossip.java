package main.java.pt.ist.gossip.core;

import main.java.pt.ist.gossip.messages.*;
import java.net.Socket;
import java.io.*;

public class Gossip {

    private int nodesSumValue;
    private int nodesWeightValue;
    private int id = 0;

    public Gossip(){

    }

    public void init(int nodesSumValue, int nodesWeightValue){

        this.nodesSumValue = nodesSumValue;
        this.nodesWeightValue = nodesWeightValue;

    }

    public synchronized void handleMsg(Message msg) throws IOException {


        if(msg.getmType().toString().equals("NODES_SUM")) {

            this.nodesSumValue = this.nodesSumValue + 1;
            this.nodesWeightValue = this.nodesWeightValue + 1;
        }

    }

    public synchronized Message getMessage(MessageType messageType) throws IOException{

        Message msg = null;

        if(messageType.toString().equals("NODES_SUM")){
            msg = new Message(messageType, this.nodesSumValue/2, this.nodesWeightValue/2, this.id);
            this.nodesWeightValue = this.nodesSumValue/2;
            this.nodesSumValue = this.nodesWeightValue/2;
        }

        return msg;
    }

    public int calculateValue(MessageType messageType){

        int totalValue = 0;

        if(messageType.toString().equals("NODES_SUM")){

            totalValue = this.nodesSumValue/this.nodesWeightValue;

            return totalValue;

        }

        return totalValue;
    }


}
