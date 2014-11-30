package main.java.pt.ist.p2p;

import java.io.Serializable;


   
   
 
public class Bid implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private String _userID;
    private int _bid;
    private String itemName;
    
    

	public Bid(String userID,int bid, String itemName){
        this._userID = userID;
        this._bid = bid;
        this.itemName = itemName;

    }
   
    
  public String getUserId(){
      return _userID;
  }
    
  
    public int getBid(){
        return _bid;
    }
    
 
    
    public void setBid(int newBid){
        _bid = newBid;
   
    }
    
    public void setUserId(String newUserID){
        _userID = newUserID;
    }
    
    public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

    
}
