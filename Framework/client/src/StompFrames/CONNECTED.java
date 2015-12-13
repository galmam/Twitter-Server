package StompFrames;

public class CONNECTED extends StompFrame {


	
	public CONNECTED(){
		this.action="CONNECTED";
		this.headers.put("version", "1.2");
		this.body="";
	}

}
