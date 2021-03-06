package TCPserver;
import java.io.IOException;   
import java.io.InputStreamReader;
  
/** 
 * This interface care of tokenizing an input stream into protocol specific 
 * messages. 
 *  
 */ 
public interface Tokenizer { 
    /** 
     * @return the next token, or null if no token is available. Pay attention 
     *         that a null return value does not indicate the stream is closed, 
     *         just that there is no message pending. 
     * @throws IOException to indicate that the connection is closed. 
     */ 
    Object nextToken() throws IOException; 
  
    /** 
     * @return whether the input stream is still alive. 
     */ 
    boolean isAlive(); 
    
    void setInputStream(InputStreamReader ISR);
    
	Tokenizer clone(InputStreamReader ISR);
     
}