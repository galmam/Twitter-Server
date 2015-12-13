package StompFrames;

public class UNSUBSCRIBE extends StompFrame {

	
	public UNSUBSCRIBE(){
		this.action="UNSUBSCRIBE";
		this.headers.put("id", null);
		this.body="";
	}
	
	public UNSUBSCRIBE(String id,String name){
		this.action="UNSUBSCRIBE";
		this.headers.put("id", id);
		this.headers.put("destination", "/topic/"+name);
		this.body="";
	}
	
	public int getSubscribtionID(){
		return Integer.parseInt(this.headers.get("id"));
	}
	
	public String getSubscribtionName(){
		String ans=this.headers.get("destination");
		String[] parts=ans.substring(1).split("/");
		return parts[1];
	}
	

}
