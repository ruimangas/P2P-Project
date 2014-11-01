package main.java.pt.ist.p2p;

import java.util.ArrayList;

public class Item {

    private String description;
    private String name;
    private ArrayList<String> allBidders = new ArrayList<String>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getAllBidders() {
        return allBidders;
    }

    public void setBidder(String bidderName){
        allBidders.add(bidderName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
