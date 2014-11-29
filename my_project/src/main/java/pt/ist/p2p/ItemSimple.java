package main.java.pt.ist.p2p;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.tomp2p.p2p.Peer;

public class ItemSimple implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String description;
    private String name;
    private List<Bid> allBidders;
    private String dealer;
    private boolean sold;

    public ItemSimple(){
    }


    public boolean getSold(){
        return this.sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
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

    public List<Bid> getAllBidders() {
        return this.allBidders;
    }

    public void setBidder(Bid bid){
        this.allBidders.add(bid);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void showItemInformation(Peer peer) throws ClassNotFoundException, IOException{
    	System.out.println("Name: " + getName() + " Dealer: " + getDealer());
    	System.out.println("	"+getDescription());
    	BidOnItemService.getBid(peer, this);
    	System.out.println("1 - Bid on Item");
    	System.out.println("0 - Go Back");
    }
}
