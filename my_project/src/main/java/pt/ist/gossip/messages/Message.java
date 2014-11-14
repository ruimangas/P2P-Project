package main.java.pt.ist.gossip.messages;

public class Message {

    MessageType mType;
    int value;
    int weight;

    public Message(MessageType mType, int value, int weight){
        this.mType = mType;
        this.value = value;
        this.weight = weight;
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
