package main.java.pt.ist.p2p;

import java.io.Serializable;
import java.util.*;


public class BoughtItem {

    private String _itemTitle;
    private int _itemValue;
    private String _dealer;
    
    public BoughtItem(String itemTitle,int itemValue,String Dealer){
        
        _itemTitle = itemTitle;
        _itemValue = itemValue;
        _dealer = Dealer;
        
    }
    
    public String getTitle(){
        return _itemTitle;
    }
    
    public int getValue(){
        return _itemValue;
    }
    
    public String getDealer(){
        return _dealer;
    }
    
    
}
