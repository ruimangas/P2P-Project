package main.java.pt.ist.p2p.exception;

public abstract class P2PBayException extends RuntimeException implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	public P2PBayException(String s) {super(s);}
	public P2PBayException() {}

}
