package TCPserver;

import java.io.IOException; 
import java.io.OutputStreamWriter;
import java.net.Socket; 
import java.util.logging.Logger;



/**
 * The Class ConnectionHandler.
 */
public class ConnectionHandler implements Runnable { 
  
    /** The Fields. */
    private final Socket Socket; 
    private final Encoder Encoder; 
    private final Tokenizer Tokenizer; 
    private final ServerProtocol Protocol;
    private User User;
    private TcpServer Server;
	private  final  Logger LOGGER = Logger.getLogger("Logger");

  
    /**
     * Instantiates a new connection handler.
     *
     * @param Tcpserver the tcpserver which this connection handler is working on.
     * @param Socket the socket connecting this connection handler to the client
     * @param Encoder the encoder this connection handler is working with
     * @param Tokenizer the tokenizer this connection handler is receiving msg from
     * @param Protocol the protocol this connection handler is using to process the msg
     */

	public ConnectionHandler(TcpServer server,Socket s, Encoder encoder, Tokenizer tokenizer) { 
        Socket = s; 
        Encoder = encoder; 
        Tokenizer = tokenizer;
        this.Server=server;
        Protocol=this.Server.getProtocolFactory().create(this); 
    } 
    
    
	/**
	 * Send a shutdown command to the server.
	 */
	public void shoutDownServer(){
		this.Server.serverShoutDown();
	}
    /**
     * Gets the statistics.
     *
     * @return the statistics
     */
    public String getStatistics(){
    	String s=this.Server.getStatistics();
    	this.LOGGER.info(s);
    	return s;
    }
    
    /**
     * Online users.
     *
     * @return String of all online users
     */
    public String onlineUsers(){
    	String s=this.Server.onlineUsers();
    	this.LOGGER.info(s);
    	return s;
    }
    
    /**
     * Users list.
     *
     * @return String of all  users
     */
    public String usersList(){
    	String s=this.Server.getUsersLists();
    	this.LOGGER.info(s);
    	return s;
    }
    
    /**
     * Adds the new user.
     *
     * @param Name user name
     * @param Password his password
     * @return User- the user object now in the server database
     */
    public User addNewUser(String Name,String Password){
    	this.User=this.Server.addNewUser(Name, Password);
    	return this.User;
    }
    
    /**
     * Sets the user if this connection handler.
     *
     * @param usr the new user
     */
    public void setUser(User usr){
    	this.User=usr;
    	usr.setConnectionHandler(this);
    }
        
    /**
     * User handling.
     *
     * @return String-the name of the user this connection handler is working on
     */
    public String userHandling(){
    	return this.User.getName();
    }
        
    /**
     * Checks if a user is registered.
     *
     * @param Name the user name
     * @return User if the user is registered otherwise null
     */
    public User isRegistered(String name){
    	return this.Server.isRegistered(name);
    }
    
    /**
     * Write to client.
     *
     * @param String msg-to send to the client
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public  synchronized void writeToClient(String msg) {
    	this.LOGGER.warning(Thread.currentThread().getName()+" Receive a message to send to the client");
        try {
        	OutputStreamWriter osw = new OutputStreamWriter(this.Socket.getOutputStream(), this.Encoder.getCharset());  
			osw.write(msg);
			osw.flush();
		} //end try
        catch (IOException e) { 
        	this.LOGGER.warning("can't write to client something happen to the socket");
        	this.Protocol.connectionTerminated();
        	
        }
        //osw.close();
        //TODO maybe need to close more things
    }
    
    public void run() { 
    	while (!Protocol.shouldClose() && !Socket.isClosed()) {                           
    		try { 
    			if (!Tokenizer.isAlive()) {Protocol.connectionTerminated();this.LOGGER.warning("The connection was closed without a disconnect command");}
    			else { 
    				// the connection is still alive
    				Object msg = Tokenizer.nextToken(); 
    				this.LOGGER.warning(Thread.currentThread().getName()+" Finish receiving a message from client.");
    				Object ans = Protocol.processMessage(msg); 
    				this.LOGGER.warning(Thread.currentThread().getName()+" Finish processing a messsage from client.");
    				if (ans != null) { writeToClient(ans.toString()); } 
    			}//end else connection to client is still alive. 
    		} // end try proccesing information from client
    		catch (NullPointerException e) {Protocol.connectionTerminated();break; }
    		catch (IOException e) {Protocol.connectionTerminated();break;}
    	} // end while
    }// end run


	public void shoutDown() {
		Protocol.connectionTerminated(); 
		try { Socket.close();}   catch (IOException ignored) {}
		this.LOGGER.warning(("The connnection to client was terminated by a command stop from a user"));
	} 
}