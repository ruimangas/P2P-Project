package main.java.pt.ist.p2p;


import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.*;
import java.io.*;

import main.java.pt.ist.p2p.exception.QueryRejectedException;
import main.java.pt.ist.p2p.exception.ResultsNotFoundException;
import net.tomp2p.connection.Bindings;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.message.Message;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;



public class SearchServiceDHT {

 
   
   private static List<List<Number160>> myNotOperands = new ArrayList<List<Number160>>();
   private static Number160 domainKey = Number160.createHash("ITEMS");
   private static List<List<Number160>> mySearches = new ArrayList<List<Number160>>();
   
   
   public static List<Number160> findReference(Peer peer,String index) throws ClassNotFoundException, IOException{
       
       int counterRef = 0;
       Number160 keyKeyword = Number160.createHash(index);
       Number160 myHash;
       FutureDHT futureGet = peer.get(keyKeyword).setDomainKey(domainKey).setAll().start();
       futureGet.awaitUninterruptibly();
       List<Number160> myReferences = new ArrayList<Number160>();
       List<Number160> myReferencesDev = new ArrayList<Number160>();
       Object o;
       
       if(futureGet.isSuccess()){
        
         Iterator<Data> iteratorRef = futureGet.getDataMap().values().iterator();
        
         while(counterRef < futureGet.getDataMap().size()){
                  o = iteratorRef.next().getObject();
                
                  if(o.getClass().equals(keyKeyword.getClass())){
                    
                        myHash = (Number160)o;
                        myReferences.add(myHash);
                       
                       
                } else {
                                        
                    myReferences.add(keyKeyword);
                 
                }
             counterRef++;
             
         }
       }
     
       for(Number160 n : myReferences){
          if( myReferencesDev.contains(n))
              continue;
          else
              myReferencesDev.add(n);
          
       }   
       
      return myReferencesDev;
   }
   
   public static List<ItemSimple> search(Peer myPeer, List<Number160> references) throws ClassNotFoundException, IOException{ 
       
       int counterItem = 0;
       List<Number160> search = new ArrayList<Number160>();
       search = references;
       
       List<ItemSimple> items = new ArrayList<ItemSimple>();
       Object o;
       
        for(Number160 number: search){
            
            FutureDHT futureGet = myPeer.get(number).setDomainKey(domainKey).setAll().start().awaitUninterruptibly();
      
            if(futureGet.isSuccess()){         
               
               Iterator<Data> iteratorItem = futureGet.getDataMap().values().iterator();
              
               while(counterItem < futureGet.getDataMap().size()){
                 
                   o = iteratorItem.next().getObject();
                   
                   if(!(o.getClass().equals(number.getClass()))){
                       
                       
                       items.add((ItemSimple)o);
                   
                   }
                   counterItem++;
               }
              counterItem = 0;
            }
        } 
        return items;
    }  
           
   
     public static List<Item> searchItem(Peer myPeer, List<ItemSimple> itemSimples) throws ClassNotFoundException, IOException{
         
        List<Item> items = new ArrayList<Item>();
        Item item;
        
        for(ItemSimple i:itemSimples){
            
            Number160 keyWord = Number160.createHash(i.getIdItem());
             
            FutureDHT futureGet = myPeer.get(keyWord).setDomainKey(domainKey).start().awaitUninterruptibly(); 
             
            if(futureGet.isSuccess()){
               
               item = (Item)futureGet.getData().getObject();
               items.add(item);
            }
        } 
        return items;
         
     }
   
   
   
   
    public static List<ItemSimple> booleanSearch(Peer myPeer,List<String> myOperators,List<String> myQuery) throws ClassNotFoundException, IOException{
                  
            
        List<ItemSimple> theOnes = new ArrayList<ItemSimple>();
        List<Number160> mySearch;
            

        if(myOperators.get(0).equals("not"))
            throw new QueryRejectedException();

        String myWord = "";
        int size = 0;

        for(int k=myQuery.size() - 1;k>=0;k--){

            myWord = myQuery.get(k);

            if((myWord.equals("and"))){
                and();

            }else if(myWord.equals("or")){
                or();

            }else if(myWord.equals("not")){
                size = mySearches.size();
                mySearch = mySearches.get(size - 1);
                myNotOperands.add(mySearch);

            }else{

                mySearch = findReference(myPeer,myWord);
                mySearches.add(mySearch);

            }

        }


        if(mySearches.get(0).isEmpty())
            throw new ResultsNotFoundException();


        theOnes = search(myPeer,mySearches.get(0));   

        return theOnes;

    }
    
        
    public static void and(){

        int numOperands = mySearches.size();
        Integer posOne = numOperands -1;
        Integer posTwo = numOperands -2;
        List<Number160> firstOperand = mySearches.get(posOne);
        List<Number160> secondOperand = mySearches.get(posTwo);

        mySearches.remove(firstOperand);
        mySearches.remove(secondOperand);



        if(myNotOperands.contains(firstOperand) && myNotOperands.contains(secondOperand)){
            throw new QueryRejectedException();

        }else if(myNotOperands.contains(firstOperand)){

            secondOperand.removeAll(firstOperand);
            mySearches.add(secondOperand);
            myNotOperands.remove(firstOperand);

        }else if(myNotOperands.contains(secondOperand)){

            firstOperand.removeAll(secondOperand);
            mySearches.add(firstOperand);
            myNotOperands.remove(secondOperand);

        }else{

            firstOperand.retainAll(secondOperand);
            mySearches.add(firstOperand);
        }

    }

    public static void or(){

        int numOperands = mySearches.size();
        List<Number160> firstOperand = mySearches.get(numOperands -1);
        List<Number160> secondOperand = mySearches.get(numOperands -2);

        mySearches.remove(firstOperand);
        mySearches.remove(secondOperand);

        if(myNotOperands.contains(firstOperand) || myNotOperands.contains(secondOperand))
            throw new QueryRejectedException();

        for(Number160 hash : firstOperand){
            if(!(secondOperand.contains(hash)))
                secondOperand.add(hash);
        }

        mySearches.add(secondOperand);


    }


    public static void clearMySearch(){

        mySearches.clear();
        myNotOperands.clear();
    }

   public static void listItems(List<Item> items) {
		int i = 1;
      System.out.println("****************** Results *********************");
      System.out.println("************************************************");
      
      for (Item item : items){
        
          if(!item.getSold()){  
      	   System.out.println(i+" - Name: " + item.getName() + " Dealer: " + item.getDealer());
            i++;
        } 
      }
      System.out.println("************************************************");
      System.out.println("Press 0 to go back or press the item number to see details and bid");
   }
 }
        
    
    
    

