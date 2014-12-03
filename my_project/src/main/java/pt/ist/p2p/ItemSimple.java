package main.java.pt.ist.p2p;



import java.io.Serializable;


import java.io.IOException;
import java.io.Serializable;



public class ItemSimple implements Serializable{


    private static final long serialVersionUID = 1L;

    private String _name;
    private String _dealer;
    private int _idItem;


    public ItemSimple(){
    }



    public ItemSimple(String name, String dealer, int idItem){
         _name = name;
         _dealer = dealer;
         _idItem = idItem;
    }


    public int getIdItem(){
        return _idItem;
    }
 

   public String getName() {
        
        return _name;
    }
   
   public String getDealer() {
       
       return _dealer;
   }
   



}
