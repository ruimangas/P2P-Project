package main.java.pt.ist.p2p;

public class Bid {

    private String _userID;
    private int _bid;
    
    public Bid(String userID,int bid){
        _userID = userID;
        _bid = bid;
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
