package StompFrames;


public class MESSAGE extends StompFrame {
	
	public MESSAGE(String destantion,int subscription,String messageid,String message){
		this.action="MESSAGE";
		this.headers.put("destination", "/topic/"+destantion);
		this.headers.put("subscription", String.valueOf(subscription));
		this.headers.put("message-id", (messageid));
		this.body=message;

	}
	
	public MESSAGE(String destantion,String from,String messageid,String message){
		this.action="MESSAGE";
		this.headers.put("destination", "/topic/"+destantion);
		this.headers.put("source", from);
		this.headers.put("message-id", (messageid));
		this.body=message;

	}
	
	public StompFrame Process(StompFrameDispatcher d) {
		return null;
	}
	
	public String getDestantion(){
		String ans=this.headers.get("destination");
		String[] parts=ans.substring(1).split("/");
		return parts[1];
	}
	
	public String getSubscription(){
		return this.headers.get("subscription");
	}
	
	public String getMessageID(){
		return this.headers.get("message-id");
	}
}
