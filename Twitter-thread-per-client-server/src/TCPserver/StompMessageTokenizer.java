package TCPserver;

import java.io.BufferedReader;
import java.io.IOException; 
import java.io.InputStreamReader; 
import java.io.StringReader;
import java.util.Scanner;
import java.util.logging.Logger;
import StompFrames.*;
  

/**
 * The Class StompMessageTokenizer.
 */
public class StompMessageTokenizer implements Tokenizer { 
  

	/*
	 * The Fields
	 */
    private BufferedReader BR;
    private Scanner scanner;
    private final String ender="\0";
    private boolean _closed=false; 
	private  final  Logger LOGGER = Logger.getLogger("Logger");
		
		
	/**
	 * Instantiates a new stomp message tokenizer.
	 */
	public StompMessageTokenizer(){
		this.BR=null;
		this.scanner=null;
	}
    
    /**
     * Instantiates a new stomp message tokenizer.
     *
     * @param InputStreamReader the input stream recevied from the socket connected to the client
     */
    public StompMessageTokenizer(InputStreamReader ISR){
     	 this.scanner=new Scanner(ISR).useDelimiter(this.ender);
    }
  
    /**
     * This method process the input stream from the client
     * @return StompFrame according the input receive from the client
     */

    public StompFrame nextToken() throws IOException { 
    	this.LOGGER.warning(Thread.currentThread().getName()+ " Start receiving information from the client");
    	if (!isAlive())  {this.scanner.close();throw new IOException("Tokenizer is closed ");} 
    	/**	 we are using a blocking stream, so we should always end up 
    	 *	 with a StompFrame, or with an exception indicating an error in 
    	 * 	the connection. 
    	 *	read line by line, until encountering the framing character, or the connection is closed.*/ 
    	String line="";
    	StompFrame sf=null;
    	boolean AllHeadersAdded=false;
    	boolean frameInitalze=false;
    	StringBuilder body=new StringBuilder("");
    	if (this.scanner.hasNext()){
    		String data=this.scanner.next();
    		this.BR =new BufferedReader(new StringReader(data));
    		while ((line=BR.readLine())!=null) { 
    			if (!frameInitalze) {sf=StompFrameFactory.Create(line);frameInitalze=true;}
    			else{
    				if (!sf.isComplete()){
    					// if this condition is true then we are still receiving the headers of frame
    					if(!AllHeadersAdded && !line.isEmpty()) {headersAdder(line,sf);} 
    					else {
    						if (!AllHeadersAdded && line.isEmpty() ) {AllHeadersAdded=true;}
    						else {body.append(line);body.append("\n");}
    					}//end else
    				}// end if frame isn't complete
    			}//end else if frame still need to be build
    		} // end while of the buffer reader
    		sf.setBody(body.toString());
    		sf.Complete();		
    		return sf;
    	}//end if of scanner has next
    	if (sf==null) {this._closed=true;} // sf can't be null unless the the connection was lost
    	return sf; 
    } 
  
    /**
     * return true if the connection was not closed
     */
    public boolean isAlive() { 
        return !_closed; 
    }
    
    /**
     * Headers adder.
     *
     * @param String the line recevied from the buffer reader
     * @param StompFrame sf- the stompframe we currently processing
     */
    private StompFrame headersAdder(String line,StompFrame sf){
    	if (line.contains(":")) {
    		String[] parts = line.split(":"); 
    		String part1 = parts[0]; 
    		String part2 = parts[1];
    		sf.addHeaders(part1, part2);
    	} 
    	else {sf=new ERROR("malformed stomp frame received","");sf.Complete();return sf;}
    	return null;
    }
	
	/**
	 * clone this toknizer with the given input stream reader
	 * @param InputStreamReader ISR
	 */
	public Tokenizer clone(InputStreamReader ISR){
		return new StompMessageTokenizer(ISR);
	}
  
}
