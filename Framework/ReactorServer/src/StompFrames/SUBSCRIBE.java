package StompFrames;

public class SUBSCRIBE extends StompFrame {

	public StompFrame Process(StompFrameDispatcher d) {
		return d.processFrame(this);
	}
	
	public SUBSCRIBE(){
		this.action="SUBSCRIBE";
		this.headers.put("destination", "/topic/");
		this.body="";
	}
	
	public SUBSCRIBE(String id,String destination){
		this.action="SUBSCRIBE";
		this.headers.put("destination", "/topic/"+destination);
		this.headers.put("id", id);
		this.body="";
	}
	
	public String getDestantion(){ // destantion 
		String ans=this.headers.get("destination");
		String[] parts=ans.substring(1).split("/");
		return parts[1];
	}
	
	public int getID(){
		return Integer.parseInt(this.headers.get("id"));
	}

}
