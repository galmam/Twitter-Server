package StompFrames;


public class CONNECT extends StompFrame {


	public CONNECT(){
		this.action="CONNECT";
		this.headers.put("accept-version", "1.2");
		this.body="";
	}
	
	public CONNECT(String user,String password,String host){
		this.action="CONNECT";
		this.headers.put("accept-version", "1.2");
		this.headers.put("host",host );
		this.headers.put("login", user);
		this.headers.put("password", password);
	}
	
	public String getUserName(){
		return this.headers.get("login");
	}
	
	public String getUserPassword(){
		return this.headers.get("password");
	}
	
	public void setHost(String host_name){
		this.headers.put("host",host_name );
	}
	
	public void setUser(String username,String password){
		this.headers.put("login",username );
		this.headers.put("password", password);
	}



}
