package StompFrames;

public class UNSUBSCRIBE extends StompFrame {

	public StompFrame Process(StompFrameDispatcher d) {
		return d.processFrame(this);
	}
	
	public UNSUBSCRIBE(){
		this.action="UNSUBSCRIBE";
		this.headers.put("id", null);
		this.body="";
	}
	
	public UNSUBSCRIBE(String id){
		this.action="UNSUBSCRIBE";
		this.headers.put("id", id);
		this.body="";
	}
	
	public int getSubscribtionID(){
		return Integer.parseInt(this.headers.get("id"));
	}
	
	public String getSubscribtionName(){
		return this.headers.get("name");
	}
	

}
