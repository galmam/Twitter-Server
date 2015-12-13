package StompFrames;

public class RECEIPT extends StompFrame {

	public StompFrame Process(StompFrameDispatcher d) {
		return null;
	}
	
	public RECEIPT(String id){
		this.headers.put("receipt-id", id);
		this.body="";
		this.action="RECEIPT";
	}

}
