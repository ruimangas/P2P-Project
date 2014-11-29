package main.java.pt.ist.p2p;

import java.io.Serializable;

public class Bid implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private String _userID;
    private int _bid;
    
    public Bid(String userID,int bid){
        this._userID = userID;
        this._bid = bid;
    }
   
    
    public String getUserID(){
        return _userID;
    }
    
  
    public int getBid(){
        return _bid;
    }
    
    
    public void setBid(int newBid){
        _bid = newBid;
   
    }
    
    public void setUserID(String newUserID){
        _userID = newUserID;
    }
    
}
