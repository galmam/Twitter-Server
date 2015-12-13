package TCPserver;

import java.io.IOException; 
import java.io.InputStreamReader; 
import java.net.InetAddress;
import java.net.ServerSocket; 
import java.net.Socket; 
import java.net.InetSocketAddress; 
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


// TODO: Auto-generated Javadoc
/**
 * The Class TcpServer.
 */
/**
 * @author galmam
 *
 */
public class TcpServer { 
	
	/** The Clients threads. */
	private Vector<Thread> ClientsThreads;
	
	/** The Connections handler. */
	private Vector<ConnectionHandler> ConnectionsHandler;
	
	/** The Users. */
	private Vector<User> Users;
	
	/** The Statistics. */
	private Statistics Statistics;
	
	/** The Thread counter. */
	private  int ThreadCounter=1;
	
	/** Uniqe zip code for each connection handler messages id*/
	private int uniqueStartNum=0;
	
	/** Protocol factory create for each connection handler his protocol.*/
	private ServerProtocolFactory ProtocolFactory;
	private  final  Logger LOGGER = Logger.getLogger("Logger");
	
	/** this field represent if the server should continue running.*/ 
	private boolean ShouldClose;
	
	/** this is the socket of the server.*/
    private ServerSocket  ServerSocket;

	/**
	 * Instantiates a new tcp server.
	 */
	public TcpServer(ServerProtocolFactory pfactory){
		this.ClientsThreads=new Vector<Thread>();
		this.ConnectionsHandler=new Vector<ConnectionHandler>();
		this.Users=new Vector<User>();
		this.Statistics=new Statistics();
		this.Users.add(new User("server","",this.Statistics,this.getStartNum()));
		this.ProtocolFactory=pfactory;
		this.ShouldClose=false;
		try {this.ServerSocket= new ServerSocket();} 
		catch (IOException e) {this.ServerSocket=null;System.out.println("should not happen");} 
	}
	
	/**
	 * Tell the server to close all the running connections handlers.
	 */
	public void serverShoutDown(){
		for(int i=0;i<this.ConnectionsHandler.size();i++){
			this.ConnectionsHandler.get(i).shoutDown();
			this.ShouldClose=true;
			try {this.ServerSocket.close();} 
			catch (IOException e) {this.LOGGER.warning("The ServerSocket is already closed.");}
			}
	}
	
	/**
	 * Gets the protocol factory.
	 *
	 *
	 * @return the protocol factory
	 */
	public ServerProtocolFactory getProtocolFactory(){
		return this.ProtocolFactory;
	}
	
	/**
	 * Gets the start num.
	 *
	 * @return the start num
	 */
	public int getStartNum(){
		this.uniqueStartNum++;
		return (this.uniqueStartNum-1);

	}
    /**
     * Gets the statistics.
     *
     * @return String-the statistics of this server
     */
    public String getStatistics(){
    	return this.Statistics.getStatistics();
    }
	
    /**
     * Online users.
     *
     * @return String- the list of all online users
     */
    public String onlineUsers(){
    	return this.Statistics.onlineUsers();
    }
	
	/**
	 * Gets the users lists.
	 *
	 * @return String-the users lists
	 */
	public String getUsersLists(){
		return this.Statistics.getUsersList();
	}
	
    /**
     * Checks if is registered.
     *
     * @param Strng the user  name
     * @return the user
     */
    public synchronized User isRegistered(String name){
    	for(int i=0;i<this.Users.size();i++){
    		if (this.Users.get(i).CompareTo(name)) {return this.Users.get(i);}
    	}
    	return null;
    }
	
	/**
	 * Run.
	 *
	 * @param port the port
	 * @param toknzier the toknzier
	 * @param protocol the protocol
	 */
	private  void Run(String port, Tokenizer toknzier) 
    
    { 
        try { 
            final Encoder encoder = new SimpleEncoder(); 
            InetAddress address=InetAddress.getLocalHost();
            ServerSocket.bind(new InetSocketAddress(address,Integer.parseInt(port)));
            this.LOGGER.warning("Server start running on "+ServerSocket.getInetAddress().getHostAddress()+"on port: "+port);
            while (!this.ShouldClose){ 
                // accept() blocks until a client connects to us 
                // It returns a socket connected to the client. 
            	this.LOGGER.info("Server is waiting for a new connection");
                final Socket client = ServerSocket.accept();
                this.LOGGER.warning("Server Received new connection");
                InputStreamReader isr = new InputStreamReader(client.getInputStream(), encoder.getCharset());
                Tokenizer ConnectionToknizer=toknzier.clone(isr);
                ConnectionHandler ch=new ConnectionHandler(this,client,encoder,ConnectionToknizer);
                this.ConnectionsHandler.add(ch);
                Thread thread=new Thread(ch);
                this.ClientsThreads.add(thread);
                thread.setName("Unname ConnectionHandler"+this.ThreadCounter);
                this.ThreadCounter++;
                thread.start();
            } 
        } catch (IOException e) { this.LOGGER.warning("The server was shutdown");  } 
    } 
     

	/**
	 * Adds the new user.
	 *
	 * @param String Name the user name
	 * @param String Password the user  password
	 * @return User - the user we added to list of all users in the server
	 */
	public User addNewUser(String Name, String Password) {
		User temp=new User(Name,Password,this.Statistics,this.getStartNum());
		this.Users.add(temp);
		//TODO check if it's good that the user isLogged is true
		//TODO sync
		return temp;
	}
	
	public static void main(String[] args) throws SecurityException, IOException {
		
		final  Logger LOGGER = Logger.getLogger("Logger");
		FileHandler handler=new FileHandler("Simulation.log");
		handler.setFormatter(new CustomizeFormatter()); 
		LOGGER.getParent().getHandlers()[0].setFormatter(new CustomizeFormatter());
		LOGGER.addHandler(handler);
		StompTwiterProtocolFactroy factory=new StompTwiterProtocolFactroy();
    	TcpServer s=new TcpServer(factory);
    	StompMessageTokenizer tokenzier=new StompMessageTokenizer();
    	s.Run(args[0],tokenzier);
	}

}
