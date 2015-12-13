package StompFrames;
import TCPserver.StompFrameDispatcher;

public class SEND extends StompFrame {

	public StompFrame Process(StompFrameDispatcher d) {
		return d.processFrame(this);
	}
	
	public SEND(){
		this.action="SEND";
		this.headers.put("destination", "/topic/");
		this.body="";
	}
	
	public SEND(String destination,String msg){
		this.action="SEND";
		this.headers.put("destination", "/topic/"+destination);
		this.body=msg;
	}
	
	public String getDestination(){
		String ans=this.headers.get("destination");
		String[] parts=ans.split("/");
		return parts[2];
	}
	
	public String getMessage(){
		return this.body;
	}
	
	public void setMessage(String s){
		this.body=s;
	}
	
	public boolean userTagged(){
		return this.body.contains("@");
	}
	
	public String getMentionedUser(){
		String ans=null;
		String msg=this.body;
		if (msg.contains("@")){
			String[] parts=msg.split("@");
			return parts[1].trim();
		}
		return ans;
	}

}
