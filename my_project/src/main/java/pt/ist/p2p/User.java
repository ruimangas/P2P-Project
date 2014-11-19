package main.java.pt.ist.p2p;
import java.io.Serializable;
import java.util.*;

public class User implements Serializable {

	private String username;
    private ArrayList<String> offeredItems = new ArrayList<String>();
    private ArrayList<String> biddedItems = new ArrayList<String>();
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

    public ArrayList<String> getBiddedItems() {
        return biddedItems;
    }

    public void setBiddedItems(String biddedItem) {
        this.biddedItems.add(biddedItem);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setOfferedItem(String item){

        offeredItems.add(item);
    }

    public ArrayList<String> getOfferedItems() {
        return offeredItems;
    }
}