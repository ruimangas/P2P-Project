package main.java.pt.ist.p2p;
import java.util.*;

public class User{

	private String username;
    private ArrayList<String> offeredItems = new ArrayList<String>();

	public User(){

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