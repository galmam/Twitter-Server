package StompFrames;

public class RECEIPT extends StompFrame {

	
	public RECEIPT(String id){
		this.headers.put("receipt-id", id);
		this.body="";
		this.action="RECEIPT";
	}

}
