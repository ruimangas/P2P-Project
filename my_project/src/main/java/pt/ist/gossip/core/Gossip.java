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

    public synchronized void handleMsg(Message msg, Socket s) throws IOException {


        if(msg.getmType().toString().equals("NODES_SUM")) {

            this.nodesSumValue = this.nodesSumValue + 1;
            this.nodesWeightValue = this.nodesWeightValue + 1;
        }

    }

    public synchronized Message getMessage(MessageType messageType) throws IOException{

        Message msg = null;

        if(messageType.toString().equals("NODES_SUM")){
            msg = new Message(messageType, this.nodesSumValue/2, this.nodesWeightValue/2, this.id);
            setActualNodesValues(this.nodesSumValue/2, this.nodesWeightValue/2);
        }

        return msg;
    }

    private void setActualNodesValues(int nodesSumValue, int nodesWeightValue) {

        this.nodesWeightValue = nodesWeightValue;
        this.nodesSumValue = nodesSumValue;

    }


}
