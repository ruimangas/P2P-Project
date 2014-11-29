package main.java.pt.ist.p2p;

public class Bid {

    private String _buyer;
    private int _bid;
    private String _nameItem;
    private String _dealer
    ;
    public Bid(String buyer,int bid, String nameItem,String dealer){
        _buyer = buyer;
        _bid = bid;
        _dealer = dealer;
        _nameItem = nameItem;
    }
   
    
    public String getBuyer(){
        return _buyer;
    }
    
  
    public int getBid(){
        return _bid;
    }
    
    public String getItemName(){
        return _nameItem;
    }
    
    public String getDealer(){
        return _dealer;
    }
    
    
    public void setBid(int newBid){
        _bid = newBid;
   
    }
    
    public void setBuyer(String newUserID){
        _buyer = newUserID;
    }
    
}
