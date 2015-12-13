package TCPserver;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class Statistics.
 */
/**
 * @author galmam
 *
 */
public class Statistics {

	/*
	 * Fields
	 */

	/** The number of followers the user with the most followrts have.. */
	private int MaxFollowersNum=0; 

	/** The name of the user with the most followers.. */
	private String UserWithMostFollowers; // 

	/** Tthe name of the user with the most mentioning by other users. */
	private String UserThatMentionedOthersTheMost;// 

	/** How much times the user that mentioned the most did it. */
	private int MaxTimesMentionWasUsed=0; // 

	/** The numer of times the most mentioned user have. */
	private int MaxWasMentionedNum=0;// 

	/**  The name of the user that was mentioned in others users tweet the most times. */
	private String UserMentionedTheMostByOthers; 

	/** The max times a user used tweeting. */
	private int MaxNumOfTweets=0; 

	/** The User tweeded the most. */
	private String UserTweededTheMost;

	/** Current number of threads running in background counting tweets per 5s. */
	private int TweeterCounterNum=0; // 

	/** The sum of the results from counter thread that count how much tweet in 5s. */
	private int SumOfAllCounterResults=0;// 

	/** The Maximum number of tweets per5s. */
	private int MaxTweetPer5s=0;

	/** The Num of total tweets. */
	private int NumOfTotalTweets=0;

	/** The sum of all the time each tweet took to send. */
	private long TotalTimeSpentOnDeliverTweet=0; 

	/** The logger. */
	private  final  Logger LOGGER = Logger.getLogger("Logger");

	/** a hasmap that the key is the user and for each user the number of times  he was mentioned in other tweets. */
	private Map<String, Integer> UsersMentioningByOthersNum = new HashMap<String,Integer>();

	/** a hasmap that the key is the user and for each user the number of users he mention in his tweet. */
	private Map<String, Integer> UsersMentioningUsageNum = new HashMap<String,Integer>();

	/**a hashmap ordered by users name as keys and for each username the number of tweeting he did.*/
	private Map<String, Integer> UsersTweetingNum = new HashMap<String,Integer>();

	/**a hashmap ordered by users name as keys and for each username the number of followers he have.*/
	private Map<String, Integer> UsersFollowresNumOrderedByName = new HashMap<String,Integer>();

	/**a hashmap ordered by users name as keys and for each username the status of his logged-in.*/
	private Map<String, String> UsersStatus = new HashMap<String,String>();
	
	/** a boolean field represent the server status. */
	private boolean serverIsRunning=true;
	
	/** long field represent the server start time in milli-sec.*/
	private long StartTime;


	/*
	 * End of fields section
	 */

	
	/**
	 * Instantiates a new statistics.
	 */
	public Statistics(){
		this.StartTime=Calendar.getInstance().getTimeInMillis();
	}
	/*
	 * Private Methods Section
	 */

	/**
	 * Avg tweet per5s.
	 *
	 * @return the double
	 */
	private double AvgTweetPer5s(long time){
		System.out.println(this.NumOfTotalTweets+"/"+time/1000);
		double sec=time/1000;System.out.println(sec);
		double temp=this.NumOfTotalTweets/sec;System.out.println(temp);
		System.out.println(temp*5);return temp*5;
	}

	/**
	 * Avg time take to deliver tweet.
	 *
	 * @return Double
	 */
	private  double  AvgTimeTakeToDeliverTweet(){
		if (this.TotalTimeSpentOnDeliverTweet==0||this.NumOfTotalTweets==0) return 0;
		return this.TotalTimeSpentOnDeliverTweet/this.NumOfTotalTweets;
	}

	/**
	 * Check new max.
	 *
	 * @param map the map
	 * @param max the max
	 * @return the string
	 */
	private String CheckNewMax(Map<String,Integer> map,int max){
		Iterator<Entry<String,Integer>> ITR=map.entrySet().iterator();
		while(ITR.hasNext()){
			Entry<String,Integer> entry=ITR.next();
			if (entry.getValue()==max) return entry.getKey();
		}
		return null;
	}

	/*
	 * End of private methods
	 */

	/**
	 * Server closed. change the status of the server to closed
	 */
	public synchronized void serverClosed(){
		this.serverIsRunning=false;
	}
	
	
	/**
	 * Server is running.
	 *
	 * @return true, if correct, esle false.
	 */
	public synchronized boolean serverIsRunning(){
		return this.serverIsRunning;
	}
	
	/**
	 * Gets the num of total tweet.
	 *
	 * @return the num of total tweet
	 */
	public synchronized int getNumOfTotalTweet(){
		return this.NumOfTotalTweets;
	}
	
	/**
	 * Gets the num of counters.
	 *
	 * @return the num of counters currently running
	 */
	public synchronized int getNumOfCounters(){
		return this.TweeterCounterNum;
	}

	/**
	 * Update max tweet per5s.
	 *
	 * @param num the num
	 */
	public synchronized void updateMaxTweetPer5s(int num){
		if (this.MaxTweetPer5s<num)
		{ this.MaxTweetPer5s=num;}
	}

	/**
	 * update the total time take to deliver a tweet.
	 *
	 * @param time the time
	 */
	public synchronized void updateTimeTakeToDeliverTweet(long time){
		TotalTimeSpentOnDeliverTweet=TotalTimeSpentOnDeliverTweet+time;
	}

	/**
	 * User start following.
	 *
	 * @param followed_user the followed_user
	 */
	public synchronized void userStartFollowing(String followed_user){
		//TODO CheckForMax(followed_user,this.UsersFollowresNumOrderedByName,this.MaxFollowersNum,this.UserWithMostFollowers);
		int num=1;
		// if the users have no followers then we add him to the database with the vakue of 1
		if (!this.UsersFollowresNumOrderedByName.containsKey(followed_user)) {this.UsersFollowresNumOrderedByName.put(followed_user,num);this.LOGGER.info("the user "+followed_user+" has a total of 1 follower");}
		else{
			//the users exists in the database and we increase the numbers of followers of this user by one
			num=num+this.UsersFollowresNumOrderedByName.get(followed_user);
			this.UsersFollowresNumOrderedByName.put(followed_user, num);
			this.LOGGER.info("The user "+followed_user+ "has a total of "+num+" followers");
		}
		//checking if this user is now the user with the most followers
		if (num>this.MaxFollowersNum){ 
			this.MaxFollowersNum=num;
			this.UserWithMostFollowers=followed_user;
			this.LOGGER.warning("After the user "+followed_user+" has the most followers");
		}
	}

	/**
	 * User stop following.
	 *
	 * @param followed_user the followed_user
	 */
	public synchronized void userStopFollowing(String followed_user){
		int num=this.UsersFollowresNumOrderedByName.get(followed_user);
		this.UsersFollowresNumOrderedByName.put(followed_user, num-1);
		if (this.UserWithMostFollowers.equals(followed_user)){
			this.MaxFollowersNum--;
			if (this.UsersFollowresNumOrderedByName.containsValue(num)) {
				this.UserWithMostFollowers=CheckNewMax(this.UsersFollowresNumOrderedByName,num);
			}

		}
	}
	
	/**
	 * Tweet occured.
	 *
	 * @param name the name
	 */
	public synchronized void TweetOccured(String name){
		Thread temp=new Thread(new Tweet5sCounter(this,this.TweeterCounterNum));
		this.TweeterCounterNum++;
		this.NumOfTotalTweets++;
		temp.start();// we start a new thread that we count the number of tweets occured during 5s after the current tweet
		//after tweeting occured we check if this user is not the user tweeted the most
		int num=1;
		// if the user never tweeted before we add his name to the map with the value of 1
		if (!this.UsersTweetingNum.containsKey(name)) {this.UsersTweetingNum.put(name,num);this.LOGGER.info("the user "+name+" has a total of 1 tweet");}
		else
		{
			//the user tweeted before we increase his tweeting num by one
			num=num+this.UsersTweetingNum.get(name);
			this.UsersTweetingNum.put(name, num);
			this.LOGGER.info("The user "+name+ "has a total of "+num+" tweets");
		}
		// now we check if he is the new most tweeted user
		if (num>this.MaxNumOfTweets)
		{ 
			this.MaxNumOfTweets=num;
			this.UserTweededTheMost=name;
			this.LOGGER.warning("After the user "+name+" sent a tweet he is the user with the most numbers of tweets");
		}
		this.LOGGER.info("the user "+this.UserTweededTheMost+" tweeted the most with a total of "+this.MaxNumOfTweets+" tweets");
	}


	//TODO
	/**
	 * Mention occured.
	 *
	 * @param usr_m the usr_m
	 * @param usr_was_m the usr_was_m
	 */
	public synchronized void mentionOccured(String usr_m ,String usr_was_m){
		/*
		 * First part
		 */
		int num=1;
		// if the users wasn't mentioned before then we add him to the database with the value of 1
		if (!this.UsersMentioningByOthersNum.containsKey(usr_was_m)) {this.UsersMentioningByOthersNum.put(usr_was_m,1);this.LOGGER.info("the user "+usr_was_m+"was mention just one time");}
		else{
			//the users exists in the database and we increase the numbers of mentioning of this user by one
			num=num+this.UsersMentioningByOthersNum.get(usr_was_m);
			this.UsersMentioningByOthersNum.put(usr_was_m, num);
			this.LOGGER.info("The user "+usr_was_m+ "has a total of "+num+" of mentions");
		}
		//checking if this user is now the user with the most followers
		if (num>this.MaxWasMentionedNum){ 
			this.MaxWasMentionedNum=num;
			this.UserMentionedTheMostByOthers=usr_was_m;
			this.LOGGER.warning("After the user "+usr_was_m+" has the most followers");
		}
		/*
		 * Second Part
		 */
		num=1;
		// if the users haven't mentioned anyone before then we add him to the database with the value of 1
		if (!this.UsersMentioningUsageNum.containsKey(usr_m)) {this.UsersMentioningUsageNum.put(usr_m,1);this.LOGGER.info("the user "+usr_m+" mention only one user");}
		else{
			//the users exists in the database and we increase the numbers of mentioning used by this user by one
			num=num+this.UsersMentioningUsageNum.get(usr_m);
			this.UsersMentioningUsageNum.put(usr_m, num);
			this.LOGGER.info("The user "+usr_m+ "has a total of "+num+" of mentions");
		}
		//checking if this user is now the user mentioned other users the most
		if (num>this.MaxTimesMentionWasUsed){ 
			this.MaxTimesMentionWasUsed=num;
			this.UserThatMentionedOthersTheMost=usr_m;
			this.LOGGER.warning("Now the user "+usr_m+" has mentioned other users the most");
		}
	}

	/**
	 * Update the number of currently running counters.
	 */
	public synchronized void updateTweeterCounter(){
		this.TweeterCounterNum--;
	}

	/**
	 * Gets the users list.
	 *
	 * @return String- the users list
	 */
	public synchronized String getUsersList(){
		StringBuilder s=new StringBuilder("This is the list of all users on this server:");
		s.append("\n");
		Iterator<Entry<String,String>> ITR=this.UsersStatus.entrySet().iterator();
		while(ITR.hasNext()){
			Entry<String,String> entry=ITR.next();
			s.append(entry.getKey());s.append(",");
		}
		return s.toString().substring(0, s.length()-1);
	}

	/**
	 * Online users.
	 *
	 * @return String- the names of the users currently online
	 */
	public synchronized String onlineUsers(){
		StringBuilder s=new StringBuilder("This is the list of all online users:");
		s.append("\n");
		Iterator<Entry<String,String>> ITR=this.UsersStatus.entrySet().iterator();
		while(ITR.hasNext()){
			Entry<String,String> entry=ITR.next();
			if (entry.getValue().equals("online")) {s.append(entry.getKey());s.append(",");}
		}
		return s.toString().substring(0, s.length()-1);
	}

	/**
	 * User logged in.
	 *
	 * @param name the name
	 */
	public synchronized void userLoggedIN(String name){
		this.UsersStatus.put(name, "online");
	}

	/**
	 * User logged out.
	 *
	 * @param name the name
	 */
	public synchronized void userLoggedOUT(String name){
		this.UsersStatus.put(name, "offline");
	}

	/**
	 * Update sum of all counter results.
	 *
	 * @param num the num
	 */
	public synchronized void updateSumOfAllCounterResults(int num){
		this.SumOfAllCounterResults=this.SumOfAllCounterResults+num;
	}

	/**
	 * Gets the statistics.
	 *
	 * @return String-the statistics
	 */
	public synchronized String getStatistics(){
		StringBuilder s=new StringBuilder("This is Server Statistics ");
		s.append("\n");
		s.append("Max number of tweets per 5 seconds is:");
		s.append(this.MaxTweetPer5s);
		s.append("\n");
		s.append("Avg. number of tweets per 5 seconds is:");
		s.append(this.AvgTweetPer5s(Calendar.getInstance().getTimeInMillis()-this.StartTime));
		s.append("\n");
		s.append("Avg. time (in seconds) to pass a tweet to all users following an account is:");
		s.append(this.AvgTimeTakeToDeliverTweet()/1000);
		s.append("\n");
		s.append("Name of the user with the maximum number of followers is: ");
		s.append(this.UserWithMostFollowers);
		s.append(" and the number of his followers is: ");
		s.append(this.MaxFollowersNum);
		s.append("\n");
		s.append("Name of the user with the maximum number of tweets is: ");
		s.append(this.UserTweededTheMost);
		s.append(" and the number of tweets is: ");
		s.append(this.MaxNumOfTweets);
		s.append("\n");
		s.append("Name of the user with the maximum mentions in other followers tweets is: ");
		s.append(this.UserMentionedTheMostByOthers);
		s.append("\n");
		s.append("Name of the user with the maximum number of mentions in her own tweets is : ");
		s.append(this.UserThatMentionedOthersTheMost);
		return s.toString();
	}

}
