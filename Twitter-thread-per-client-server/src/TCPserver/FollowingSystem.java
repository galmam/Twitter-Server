package TCPserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;


/**
 * The Class FollowingSystem.
 */
public class FollowingSystem {

	/** The user on which this following system efer to. */
	private final User user;
	private  final  Logger LOGGER = Logger.getLogger("Logger");
    
    /** list of the user in the server following this user . */
    private Map<User, Integer> ListOfFollowersByUserName = new HashMap<User,Integer>(); 
    
    /** The List of followers by id. */
    private Map<Integer, User> ListOfFollowersByID = new HashMap<Integer, User>();
    
    /** The List of following by user name. */
    private Map<User, Integer> ListOfFollowingByUserName = new HashMap<User,Integer>(); 
    
    /** The List of following by id. */
    private Map<Integer, User> ListOfFollowingByID = new HashMap<Integer,User>(); 
   
    /**
     * Instantiates a new following system.
     *
     * @param user the user
     */
    public FollowingSystem(User user){
    	this.user=user;
    }
    
    /**
     * Gets the user following.
     *
     * @param ID the subscribtion id which the following user used to subscribe this user
     * @return the user following
     */
    public User getUserFollowing(int ID){
    	return this.ListOfFollowingByID.get(ID);
    }
    
    /**
     * Gets the user following.
     *
     * @param User user- the user which is following this user
     * @return int ID- the subscribtion id which the following user used to subscribe to this user
     */
    public int getUserFollowing(User user){
    	return this.ListOfFollowingByUserName.get(user);
    }
    
    /**
     * Gets the follower user.
     *
     * @param ID the subscribtion id which a follower user used to subscribe to this user
     * @return User user-the user Object which is the following
     */
    public User getFollowerUser(int ID){
    	return this.ListOfFollowersByID.get(ID);
    }
    
    /**
     * Gets the follower user.
     *
     * @param user the user
     * @return Int ID-the subscribtion id which this user used to subscribe to this user
     */
    public int getFollowerUser(User user){
    	return this.ListOfFollowersByUserName.get(user);
    }
    
    /**
     * Start following  a user.
     *
     * @param User the user
     * @param ID the subscribtion id
     */
    public void startFollowingUser(User user,int ID){
    	this.ListOfFollowingByID.put(ID, user);
    	this.ListOfFollowingByUserName.put(user, ID);
    	this.LOGGER.warning("The user "+this.user.getName()+" with the subscription id:"+ID+" start following of the user "+user.getName());
    }
    
    /**
     * Adds the user to follower.
     *
     * @param User the user
     * @param ID the  subscribtion id
     */
    public void addFollowerUser(User user,int ID){
    	this.ListOfFollowersByID.put(ID, user);
    	this.ListOfFollowersByUserName.put(user, ID);
    }
    
    
    
    /**
     * Stop following user.
     *
     * @param user the user
     */
    public void stopFollowingUser(User user){
    	int ID=this.ListOfFollowingByUserName.get(user);
    	this.ListOfFollowingByUserName.remove(user);
    	this.ListOfFollowingByID.remove(ID);
    	this.LOGGER.warning("The user "+this.user.getName()+" stop following the user "+user.getName());
    }
    
    /**
     * Stop following user.
     *
     * @param ID the id
     */
    public void stopFollowingUser(int ID){
    	User user=this.ListOfFollowingByID.get(ID);
    	this.ListOfFollowingByUserName.remove(user);
    	this.ListOfFollowingByID.remove(ID);
    	this.LOGGER.warning("The user "+this.user.getName()+" stop following the user "+user.getName());
    }
    
    /**
     * Removes the user from followers.
     *
     * @param user the user
     */
    public void removeUserFromFollowers(User user){
    	int ID=this.ListOfFollowersByUserName.get(user);
    	this.ListOfFollowersByUserName.remove(user);
    	this.ListOfFollowersByID.remove(ID);
    }
    
    /**
     * Removes the user from followers.
     *
     * @param ID the id
     */
    public void removeUserFromFollowers(int ID){
    	User user=this.ListOfFollowersByID.get(ID);
    	this.ListOfFollowersByUserName.remove(user);
    	this.ListOfFollowersByID.remove(ID);
    }
    
    /**
     * Checks if is following.
     *
     * @param user the user
     * @return true, if is following
     */
    public boolean isFollowing(User user){
    	return this.ListOfFollowingByUserName.containsKey(user);
    }
    
    /**
     * Checks if is following.
     *
     * @param ID the id
     * @return true, if is following
     */
    public boolean isFollowing(int ID){
    	return this.ListOfFollowingByID.containsKey(ID);
    }
    	
    /**
     * Checks if is follower.
     *
     * @param user the user
     * @return true, if is follower
     */
    public boolean isFollower(User user){
    	return this.ListOfFollowersByUserName.containsKey(user);
    }
    
    /**
     * Checks if a user is a follower.
     *
     * @param int ID the subscribtion id
     * @return true, if is follower
     */
    public boolean isFollower(int ID){
    	return this.ListOfFollowersByID.containsKey(ID);
    }
    
    /**
     * Followers users.
     *
     * @return Iterator<User> -the iterator of all the users following this user
     */
    public Iterator<User> followersUsers(){
    	return this.ListOfFollowersByUserName.keySet().iterator();
    }
}
