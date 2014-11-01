package main.java.pt.ist.p2p;
import java.util.*;

public class User{

	private String username;
	private ArrayList<String> offeredItems = new ArrayList<String>();

	public User(){

	}

    public User(String user, String pass) {

        username = user;  
    }

    public void setUsername(String user){
    	username = user;
    }

    public void setOfferedItem(String item){
    	offeredItems.add(item);
    }

    public ArrayList<String> getOfferedItems(){
    	return offeredItems;
    }

}