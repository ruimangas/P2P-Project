package main.java.pt.ist.p2p.exception;

public class QueryRejectedException extends P2PBayException{
	private static final long serialVersionUID = 1L;
	
	
		@Override
	public String toString(){
		return "Your Query has been rejected";
	}
}
