package protocol;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.logging.Logger;
import StompFrames.*;
import reactor.*;


/**
 * The Class StompProtocol.
 */

public class TwiterOverStompProtocol implements AsyncServerProtocol<StompFrame> {

	/** The ConnectionHandler this protocol is working with. */
	private ConnectionHandler<StompFrame> CH;
	
	/** The user this protocol is working with. */
	private User user;
	
	/** The logger. */
	private  final  Logger LOGGER = Logger.getLogger("Logger");
	
	/** a boolean represent if the user sent a disconnect demand. */
	private boolean ShouldClose=false;
	
	/** The Dispatcher. */
	private TwitterDispatcher Dispatcher = new TwitterDispatcher(this);
	
	
	/**
	 * Instantiates a new twiter over stomp protocol.
	 *
	 * @param CH the ch
	 */
	public TwiterOverStompProtocol(ConnectionHandler<StompFrame> CH){
		this.CH=CH;
	}
	
	
	/*
	 * Private methods section.
	 */

	 /**
	 * Send msg to another user.
	 *
	 * @param SEND the msg
	 * @return StompFrame
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
 	private StompFrame SendMsgToAnotherUser(SEND msg){
		// if the user sends a msg to the server then we need to process which information he requested
		if (msg.getDestination().equals("server")){
			String body="";
			// if the users request just stats we send back a MESSAGE frame containing the statistics of the server
			if(msg.getBody().contains("stats")){body=this.CH.getStatistics();}
			if(msg.getBody().contains("stop")){this.user.ClosedServer();this.CH.shoutDownServer();}
			else{
				if (msg.getBody().contains("clients")){
					if (msg.getBody().contains("online")) {body=this.CH.onlineUsers();}// the user requested just the list of the online users
					else{body=this.CH.usersList();}// the users request the list off all users on the server
				}
			}
			MESSAGE reply=new MESSAGE(this.user.getName(),"server",getUniqeMessageID(),body);
			reply.addHeaders("time", String.valueOf(Calendar.getInstance().getTime()));
			reply.addHeaders("sender", "server") ;
			return reply;
		}// end if the user send a msg to the server
		else{
			User other_user=CH.isRegistered(msg.getDestination());
			if (other_user!=null){
				MESSAGE reply=new MESSAGE(msg.getDestination(),this.user.getName(),getUniqeMessageID(),msg.getMessage());
				other_user.receivedMsg(reply.toString());
				return null;
			}
			else {return new ERROR("The user you are sending this message doesn't exists on this server","");}
		}// end else the user didn't send the msg to the server
	}
	
	
	/**
	 * Tweet
	 *
	 * @param user the user
	 * @param msg the msg
	 * @param time the time
	 */
	private void Tweet(User user,SEND msg,String time) {
		MESSAGE reply;
		Iterator<User> followingUsers=user.followersUsers();// the list of the followers of the tweeted user
		while (followingUsers.hasNext()){
			User current_user=followingUsers.next();
			int ID=current_user.getSubscribtionID(user);
			this.LOGGER.warning(user.getName()+" is sending a tweet to the user "+current_user.getName()+" with the subscription ID:"+ID);
			reply=new MESSAGE(current_user.getName(),ID,getUniqeMessageID(current_user),msg.getMessage());
			reply.addHeaders("time", time);
			reply.addHeaders("sender", this.user.getName());
			current_user.receivedMsg(reply.toString());// send the msg to user
		}
	}	
	
	/**
	 * Adds the unique message id.
	 *
	 * @return the string with the unique msg id.
	 */
	private String getUniqeMessageID(){
		return this.user.getUserUniqueMessageID();
	}
	
	/**
	 * Gets the unique message id.
	 *
	 * @param usr the usr
	 * @return the uniqe message id
	 */
	private String getUniqeMessageID(User usr){
		return usr.getUserUniqueMessageID();
	}
	
	/*
	 * End Of Private methods Section
	 */
	
	
	/**
	 * This methos process the object
	 * if it's null we return null
	 * if it's a StompFrame class then we use the abstract method of this class @StompFrame.Process
	 * each StompFrame class that implement the abstract class StompFrame send it back to the protocol according  to the class type
	 *
	 * @param Object the object
	 * @return the stomp frame
	 */
	public StompFrame processMessage(StompFrame Object) {
		if (Object==null) {System.out.println("it's null");return new ERROR("error occured while processing message sent","");}
		return Object.Process(this.Dispatcher);

	}

	/**
	 * Checks if is end.
	 *
	 * @param msg the msg
	 * @return true if the client send a Disconnect frame
	 * False= if the client send anyother StompFrame
	 */
	public boolean isEnd(StompFrame msg) {
		if	(msg.getAction().equals("DISCONNECT")) this.ShouldClose=true;
		return this.ShouldClose;
	}

	/**
	 * Should close.
	 *
	 * @return the value of the field
	 */
	public boolean shouldClose() {
		return this.ShouldClose;
	}



	public void connectionTerminated() {
		this.ShouldClose=true;
		if (this.user!=null) {this.user.logOUT();}
		this.user=null;
		return;
	}

	/**
	 * This methos process a connect frame.
	 *
	 * @param msg the msg
	 * @return StompFrame according to diffrent condition
	 */
	public StompFrame Processframe(CONNECT msg) {
		this.LOGGER.warning(Thread.currentThread().getName()+" start processing a CONNECT frame");
		String name=msg.getUserName();
		String password=msg.getUserPassword();
		User user=CH.isRegistered(name);
		//if after using the method CH.isRegistered(name) user==null that is beacuse that user never registered before to this server
		if (user!=null) {
			//if were are here then the user never registered before to this server.
			this.user=user;// we set the user protocol to be that we found
			if (this.user.isLoggedIN()) {
				this.LOGGER.warning(name +"is already logged-in request denied!");
				return new ERROR(name +"is already logged in","");}
			else{
				// if were are then the user wasn't logged-in and we logged him in if his password is correct
				if (user.CorrectPassword(password)) {
					CH.setUser(this.user);// we set the user of the connection handler working with this protocol
					this.LOGGER.warning(name+" has successfully logged into the server");
					this.CH.writeToClient(new CONNECTED().toString());
					this.user.logIN();
					return null;
				} // end if all test are passed
				else {
					this.LOGGER.warning("Wrong Password,Please try again");
					return new ERROR("Wrong Password,Please try again","");
				}// end else wrong password
			} //end else user isn't logged in
		} //end if user is regesitered 
		else{	
			this.LOGGER.warning(name+" isn't registered before adding him to the server and logging him in");
			this.user=CH.addNewUser(name, password);
			CH.setUser(this.user);
			this.CH.writeToClient(new CONNECTED().toString());
			this.user.logIN();
			Thread.currentThread().setName("CH "+this.user.getName());
			return  null;
		}// end else user not regesitered 
	}
	
	
	/**
	 * This methos process a SUBSCRIBE frame.
	 *
	 * @param msg the msg
	 * @return StompFrame
	 */
	public StompFrame Processframe(SUBSCRIBE msg){
		this.LOGGER.warning(Thread.currentThread().getName()+" start processing a SUBSCRIBE frame");
		if (this.user!=null && this.user.isLoggedIN()){
			//retriving the user that we want to start following him.
			User Other_user=CH.isRegistered(msg.getDestantion());
			if (Other_user!=null){
				if (this.user.startFollowingUser(Other_user, msg.getID())) {
					//if were here then this user wasn't allready following the other user
					this.LOGGER.warning("the user "+this.user.getName()+"start following the user "+Other_user.getName());
					MESSAGE reply=new MESSAGE(CH.userHandling(),msg.getID(),getUniqeMessageID(),"following "+Other_user.getName());
					reply.addHeaders("time", String.valueOf(Calendar.getInstance().getTime()));
					reply.addHeaders("sender", "server") ;
					return reply;}// end if this user isn't following the given user name
				else{this.LOGGER.warning("the user "+this.user.getName()+"already following the user "+Other_user.getName());return new ERROR("already following this user","");}
			}// end if user was found and trying to follow him
			else {
				this.LOGGER.warning("the user "+this.user.getName()+"requested following the user "+msg.getDestantion()+"but this user dosn't exists on this server");
				return new ERROR("User doesn't exists on this server,please try again","");
			}
		}
		else {
			this.LOGGER.warning(this.user.getName()+"request following the user "+msg.getDestantion()+"but isn't logged in ");
			return new ERROR("User isn't Logged in","");
			}
	}
	
	/**
	 * This methos process a UNSUBSCRIBE frame.
	 *
	 * @param msg the msg
	 * @return StompFrame
	 */
	public StompFrame Processframe(UNSUBSCRIBE msg){
		if (this.user!=null && this.user.isLoggedIN()){
			if (msg.getSubscribtionID()==0) {return new ERROR("It's not possible to unfollow yourself","");}
			User user=this.user.getFollowedUserByID(msg.getSubscribtionID());
			//this.LOGGER.warning("The user "+this.user.getName()+ "aks to stop follow "+user.getName());
			if (user!=null){
				if (this.user.unFollowUser(msg.getSubscribtionID())) {
					MESSAGE reply=new MESSAGE(CH.userHandling(),msg.getSubscribtionID(),getUniqeMessageID(),"unfollowing "+user.getName());
					reply.addHeaders("time", String.valueOf(Calendar.getInstance().getTime()));
					reply.addHeaders("sender", "server") ;
					return reply;
				}// end if unfollow was success
				else {
					this.LOGGER.warning(this.user.getName()+"request following the user "+user.getName()+"but he isn't following him");
					return new ERROR("It's not possible to unfollow a user that you are not following","");}
			}
			else {
				this.LOGGER.warning(this.user.getName()+"request following a user , but the user isn't registered on this server ");
				return new ERROR("It's not possible to unfollow a user that isn't registered on this server","");}
		} //end if user isn't logged in
		else {
			this.LOGGER.warning(this.user.getName()+"request following the user "+user.getName()+"but isn't logged in ");
			return new ERROR("User isn't Logged in","");
			}
	}
	
	
	/**
	 * This methos process a SEND frame.
	 *
	 * @param msg the msg
	 * @return StompFrame
	 */
	public StompFrame Processframe(SEND msg){
		this.LOGGER.warning(Thread.currentThread().getName()+" start to process a SEND frame");
		String time=String.valueOf(Calendar.getInstance().getTime());//take a time stamp when the msg received at the server
		long start_delivery_time=Calendar.getInstance().getTimeInMillis();
		if (this.user!=null && this.user.isLoggedIN()){
			this.LOGGER.info("user is logged in");
			if (msg.userTagged()) {
				//if were are here then the user tagged another user in his tweet
				User mentioned_user=this.CH.isRegistered(msg.getMentionedUser());
				this.user.Tweeted();
				// if we didn't find the tagged user then we tweeted as no user was tagged
				if (mentioned_user==null) {
					this.LOGGER.warning(this.user.getName()+" tag the name of "+msg.getMentionedUser()+" but he doesn't exists on this server");
					Tweet(this.user,msg,time);
					return new ERROR("Tagged user "+msg.getMentionedUser()+" doesn't exists on the server","");
				}//end if tagged user doesn't exists on the server
				else{
					this.user.usedMention(mentioned_user);
					Tweet(mentioned_user,msg,time);
					Tweet(this.user,msg,time);
					long end_delivery_time=Calendar.getInstance().getTimeInMillis();
					this.user.updateTweetDeliveryTime(end_delivery_time-start_delivery_time);
					return null;
				}// end else tagged user exists on the server
			}// end if user tagged another user
			else{
				// if the user send msg to himself then he wanted to tweet
				this.LOGGER.info(this.user.getName()+"didn't tag anyone in his tweet");
				if (msg.getDestination().equals(this.user.getName())){
					this.user.Tweeted();
					Tweet(this.user,msg,time);
					long end_delivery_time=Calendar.getInstance().getTimeInMillis();
					this.user.updateTweetDeliveryTime(end_delivery_time-start_delivery_time);
					return null;}
				else { 
					long end_delivery_time=Calendar.getInstance().getTimeInMillis();
					this.user.updateTweetDeliveryTime(end_delivery_time-start_delivery_time);
					return SendMsgToAnotherUser(msg);}
			}// end else user just send one message to another user
		}// end if user is loggned in
		else {this.LOGGER.warning(this.user.getName()+" Isn't logged in");return new ERROR("User isn't Logged in","");}
	}
	
	/**
	 * Processframe.
	 *
	 * @param error the error
	 * @return the same ERROR frame
	 */
	public StompFrame Processframe(ERROR error) {
		return error;
	}
	
	
	/**
	 * This methos process a DISCONNECT frame.
	 *
	 * @param msg the msg
	 * @return RECEIPT with the subscribtion id if the use is logged in.
	 * ERROR if the user is logged off.
	 */
	public StompFrame Processframe(DISCONNECT msg){
		if (this.user!=null && this.user.isLoggedIN()){
			this.user.logOUT();
			this.user=null;
			this.ShouldClose=true;
			return new RECEIPT(msg.getID());
			}
		else {return new ERROR("User isn't Logged in","");}		
	}

}

