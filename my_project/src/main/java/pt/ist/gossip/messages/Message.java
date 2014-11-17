package main.java.pt.ist.gossip.messages;

public class Message {

    private MessageType mType;
    private int value;
    private int weight;
    private int id;

    public Message(){

    }

    public Message(MessageType mType, int value, int weight, int id){

        this.mType = mType;
        this.value = value;
        this.weight = weight;
        this.id = id;

    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }

    public MessageType getmType() {
        return mType;
    }
}
