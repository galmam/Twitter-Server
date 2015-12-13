package reactor;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * The Class User.
 */
public class User {
	
	/*
	 * The Fields
	 */

	private final String Name;	/** The Name. */
	private final String Password;	/** The Password. */
	private boolean isLoggedIN;
	
	@SuppressWarnings("rawtypes")
	private ConnectionHandler CH;/** The connection handler handling this user */
	
	/** List of msg been received from other users and their status. */
	private MessagesSystem Messages ; //
	
	/** a monitoring system that hold a database of all the following realtion relevent to this user. */
	private FollowingSystem Tweeter;
	
	/** The Statistics,an object that shared with all users on this server. */
	private Statistics Statistics;
	
	/** All mesaages sent from the server to this user will start with this zipcode.*/
	private final int zipCode;
	private int MessageReceived=0;
    
    
	/**
	 * Instantiates a new user.
	 *
	 * @param String name the name
	 * @param String password the password
	 * @param Statistics the statistics
	 */
	public User(String name,String password,Statistics Statistics,int ZipCode){
		this.Name=name;
		this.Password=password;
		this.isLoggedIN=true;
		this.Messages=new MessagesSystem();
		this.Tweeter=new FollowingSystem(this);
		this.Statistics=Statistics;
		this.startFollowingUser(this, 0);
		this.zipCode=ZipCode;
	}

	public String getUserUniqueMessageID(){
		this.MessageReceived++;
		return String.valueOf(this.zipCode)+String.valueOf(this.MessageReceived-1);
	}
	
	
	/**
	 * Log out.
	 */
	public synchronized void logOUT(){
		this.isLoggedIN=false;
		this.Statistics.userLoggedOUT(this.Name);
	}
	
	/**
	 * Sets the connection handler.
	 *
	 * @param CH the new connection handler
	 */
	public void setConnectionHandler(ConnectionHandler CH){
		this.CH=CH;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName(){
		return this.Name;
	}
	
	//TODO
	public int getSubscribtionID(User user){
		return this.Tweeter.getUserFollowing(user);
	}
	
	/**
	 * Compare to.
	 *
	 * @param String other the name of other user we are comparing to.
	 * @return true, if their name are the same.
	 */
	public boolean CompareTo(String other){
		return this.Name.equals(other);
	}
	
	/**
	 * Correct password- check if the password given as String is the same as the password stored for this user
	 *
	 * @param String password theus password
	 * @return true, if correct
	 */
	public boolean CorrectPassword(String password){
		return this.Password.equals(password);
	}
	
	//TODO check if realy needed
	/**
	 * Log in.
	 * When a user logged in the server he receive all of his msg when he was online
	 * also we change his status in the server Statistics
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public  synchronized void logIN(){
		this.isLoggedIN=true;
		this.Messages.SendUnSendMessages(this);
		this.Statistics.userLoggedIN(this.Name);
	}
	
	/**
	 * Checks if is logged in.
	 *
	 * @return true, if is logged in
	 */
	public synchronized boolean isLoggedIN(){
		return this.isLoggedIN;
	}
	
	
	
	public synchronized void ClosedServer(){
		this.Statistics.serverClosed();
	}
	
    /**
     * Adds the user to followers.
     *
     * @param user the user
     * @param ID the id
     */
    private synchronized void  addUserToFollowers(User user,int ID){
    		this.Tweeter.addFollowerUser(user, ID);
    }
    
    /**
     * Start following user.
     *
     * @param User user- the user Object we are stat following
     * @param ID the id
     * @return true, if successfully start following
     * @return false if allready following this user.
     */
    public synchronized boolean  startFollowingUser(User user,int ID){
    	if (this.Tweeter.isFollowing(user)){return false;}
    	else{
    		this.Tweeter.startFollowingUser(user, ID);
    		user.addUserToFollowers(this, ID);
    		this.Statistics.userStartFollowing(user.getName());
    		return true;
    	} 	
    }
    
    /**
     * Unfollow user.
     * if this user is following another user with this following id then it will no longer follow him after this function
     * @param ID the subscribtion id which we subscribe to the server when we start following
     * @return true, if successful
     */
    public boolean unFollowUser(int ID){
    	if (this.Tweeter.isFollowing(ID)){
    		User user=this.Tweeter.getUserFollowing(ID);
    		this.Tweeter.stopFollowingUser(ID);
    		user.removeFromFollowers(this);
    		this.Statistics.userStopFollowing(user.getName());
    		return true;
    	}
    	else return false;
    }
    
    /**
     * Removes the from followers.
     *
     * @param user the user
     */
    private void removeFromFollowers(User user){
    	this.Tweeter.removeUserFromFollowers(user);
    }
	
    /**
     * Followers users.
     *
     * @return the iterator
     */
    public Iterator<User> followersUsers(){
    	return this.Tweeter.followersUsers();
    }
    
    /**
     * Following users to string.
     *
     * @return the string
     */
    public String followingUsersToString(){
    	StringBuilder s=new StringBuilder("");
    	Iterator<User> ITR=this.Tweeter.followersUsers();
    	while(ITR.hasNext()){
    		s.append(ITR.next().getName());
    		s.append(",");
    	}
    	return s.toString();
    }
    
    /**
     * Received msg.
     *
     * @param msg the msg
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public synchronized void receivedMsg(String msg) {
    	if (this.isLoggedIN){ 
    		if (this.CH!=null) this.CH.writeToClient(msg);
    		else {System.out.println("connection handler wasn't set");}
    		this.Messages.addSentMsg(msg);}	
    	else {this.Messages.addUnSendMsg(msg);}
    }
    
    //TODO unsure
    /**
     * Gets the subscribtion id.
     *
     * @param user the user
     * @return the subscribtion id
     */
    public User getFollowedUserByID(int id){
    	return this.Tweeter.getUserFollowing(id);
    }
    
    /**
     * Write to client.
     *
     * @param msg the msg
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void writeToClient(String msg) {
    	this.CH.writeToClient(msg);
    } 
    
    /**
     * Used mention.
     *
     * @param user the user
     */
    public void usedMention(User user){
    	this.Statistics.mentionOccured(this.Name,user.getName());
    }
    
    /**
     * Tweeted.
     */
    public void Tweeted(){
    	this.Statistics.TweetOccured(this.Name);
    }
    
    /**
     *  @Override the equals of Object
     */
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof User) {
            User that = (User) other;
            result = that.CompareTo(Name);
        }
        return result;
    }
    
    /**
     * Checks if is a follower.
     *
     * @param user the user
     * @return true, if is a follower
     */
    public boolean isAFollower(User user){
    	return this.Tweeter.isFollower(user);
    }
    
    public void updateTweetDeliveryTime(long time){
    	this.Statistics.updateTimeTakeToDeliverTweet(time);
    }

    
}
