package main.java.pt.ist.p2p;

import java.util.ArrayList;

public class Item{

    private String description;
    private String name;
    private ArrayList<String> allBidders = new ArrayList<String>();
    private String dealer;

    public Item(){

    }

    public Item(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

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
