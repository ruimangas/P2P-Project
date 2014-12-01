package main.java.pt.ist.p2p.exception;

public class ArgumentosInvalidosException extends P2PBayException  {

private static final long serialVersionUID = 1L;
	
	public String toString(){
		return "Password incorrecta, escreva outra vez";
	}
}
