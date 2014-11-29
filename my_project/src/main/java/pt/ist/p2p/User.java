package main.java.pt.ist.p2p;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {

	private String username;
    private ArrayList<Item> offeredItems = new ArrayList<Item>();
    private ArrayList<Bid> biddedItems = new ArrayList<Bid>();
    private ArrayList<String> acquiredItems = new ArrayList<String>();
    private String nickname;

    public User(){

	}

    public ArrayList<String> getAcquiredItems() {
        return acquiredItems;
    }

    public void setAcquiredItems(String item) {
        this.acquiredItems.add(item);
    }

    public ArrayList<Bid> getBiddedItems() {
        return biddedItems;
    }

    public void setBiddedItems(ArrayList<Bid> biddedItem) {
        this.biddedItems = biddedItem;
    }
    
    public void addBiddedItem(Bid b){
        this.biddedItems.add(b);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setOfferedItem(Item item){

        offeredItems.add(item);
    }

    public ArrayList<Item> getOfferedItems() {
        return offeredItems;
    }
}