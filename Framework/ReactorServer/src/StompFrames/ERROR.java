package StompFrames;

public class ERROR extends StompFrame {

	public ERROR(){
		this.action="ERROR";
		this.headers.put("message", "");
		this.body="";
	}
	
	public ERROR(String error,String error_info){
		this.action="ERROR";
		this.headers.put("message", error);
		this.body=error_info;
	}
	
	public void setErrorMessage(String Error){
		this.headers.put("message", Error);
	}
	
	public StompFrame Process(StompFrameDispatcher d) {
		return d.processFrame(this);
	}
	

}
