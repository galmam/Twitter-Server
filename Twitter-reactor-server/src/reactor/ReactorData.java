package reactor;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.nio.channels.Selector;
import protocol.*;
import Tokenizer.*;

/**
 * a simple data structure that hold information about the reactor, including getter methods
 */
public class ReactorData<T> {

    private final ExecutorService _executor;
    private final Selector _selector;
    private final ServerProtocolFactory<T> _protocolMaker;
    private final TokenizerFactory<T> _tokenizerMaker;
	/** The Users. */
	private Vector<User> Users;
	/** The Statistics. */
	private Statistics Statistics;
	/** Uniqe zip code for each connection handler messages id*/
	private int uniqueStartNum=0;
	
    private Reactor<T> server;
	
	
	/**
	 * Gets the start num.
	 *
	 * @return the start num
	 */
	public int getStartNum(){
		this.uniqueStartNum++;
		return (this.uniqueStartNum-1);
	}

    
	public void serverShoutDown(){
		this.server.stopReactor();
	}
	
    public ExecutorService getExecutor() {
        return _executor;
    }

    public Selector getSelector() {
        return _selector;
    }

	public ReactorData(Reactor<T> server,ExecutorService _executor, Selector _selector, ServerProtocolFactory<T> protocol, TokenizerFactory<T> tokenizer) {
		this.server=server;
		this._executor = _executor;
		this._selector = _selector;
		this._protocolMaker = protocol;
		this._tokenizerMaker = tokenizer;
		this.Users=new Vector<User>();
		this.Statistics=new Statistics();
		this.Users.add(new User("server","",this.Statistics,this.getStartNum()));
	}

	public ServerProtocolFactory<T> getProtocolMaker() {
		return _protocolMaker;
	}

	public TokenizerFactory<T> getTokenizerMaker() {
		return _tokenizerMaker;
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
	 * Adds the new user.
	 *
	 * @param String Name the user name
	 * @param String Password the user  password
	 * @return User - the user we added to list of all users in the server
	 */
	public User addNewUser(String Name, String Password) {
		User temp=new User(Name,Password,this.Statistics,this.getStartNum());
		this.Users.add(temp);
		return temp;
	}

}
