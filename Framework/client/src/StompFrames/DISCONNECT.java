package StompFrames;


public class DISCONNECT extends StompFrame {
	
	public DISCONNECT(){
		this.action="DISCONNECT";
		this.headers.put("receipt", null);
		this.body="";
	}
	
	public DISCONNECT(String id){
		this.action="DISCONNECT";
		this.headers.put("receipt", id);
		this.body="";
	}
	
	public void setReceiptID(String id){
		this.headers.put("receipt", id);
	}
	
	public String getID(){
		return this.headers.get("receipt");
	}

}
