package StompFrames;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import TCPserver.StompFrameDispatcher;



public abstract class StompFrame  {

    protected String action;
    protected Map<String, String> headers = new HashMap<String, String>();
    protected String body;
    protected boolean isComplete=false;;


    public  abstract StompFrame Process(StompFrameDispatcher dispatcher);
    
    public void Complete(){
    	this.isComplete=true;
    }
    public boolean isComplete(){
    	return this.isComplete;
    }
    
    public String getAction() {
        return action;
    }
   
    public String getBody() {
    	return body;
    }
    
    public void addHeaders(String key,String value){
    	this.headers.put(key, value);
    }
    
    public void setBody(String body){
    	this.body=body;
    } 
    
    public String toString(){
    	StringBuilder s=new StringBuilder();
    	s.append(this.action);
    	s.append("\n");
		Iterator<Entry<String,String>> ITR=this.headers.entrySet().iterator();
		while(ITR.hasNext()){
			Entry<String,String> entry=ITR.next();
			s.append(entry.getKey());
			s.append(":");
			s.append(entry.getValue());
			s.append("\n");
		}//end while
		s.append("\n");
		s.append(this.body);
		s.append("\u0000");
		s.append("\n");
		return s.toString();
    }

}
