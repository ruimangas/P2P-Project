package main.java.pt.ist.gossip;

import java.io.IOException;
import java.io.Serializable;


public class Message implements Serializable{

    private MessageType mType;
    private double value;
    private double weight;
    private double id;

    public Message(){

    }

    public Message(MessageType mType){
        this.mType = mType;
    }

    public Message(MessageType mType, double value, double weight, double id){


        this.mType = mType;
        this.value = value;
        this.weight = weight;
        this.id = id;

    }

    public double getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public double getWeight() {
        return weight;
    }

    public MessageType getmType() {
        return mType;
    }

}
