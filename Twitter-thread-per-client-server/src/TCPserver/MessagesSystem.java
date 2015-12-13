package TCPserver;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;


/**
 * The Class MessagesSystem.
 */
public class MessagesSystem {
	
	/** The Sent messages. */
	private Vector<String> SentMessages;
	
	/** The Need to be send messages. */
	private Vector<String> NeedToBeSendMessages;
	
	/**
	 * Instantiates a new messages system.
	 */
	public MessagesSystem(){
		this.SentMessages=new  Vector<String>();
		this.NeedToBeSendMessages=new  Vector<String>();
	}
	
	/**
	 * Gets the unreceived messages.
	 *
	 * @return Iterator<String>- all the messages the user received during his offline time
	 */
	public  Iterator<String> getUnReceivedMessages(){
		return this.NeedToBeSendMessages.iterator();
	}
	
	/**
	 * Gets the sent messages.
	 *
	 * @return the sent messages
	 */
	public Iterator<String> getSentMessages(){
		return this.SentMessages.iterator();
	}
	
	/**
	 * Adds the msg to the unreceived queue for the user
	 *
	 * @param String msg the msg
	 */
	public void addUnSendMsg(String msg){
		this.NeedToBeSendMessages.add(msg);
	}
	
	/**
	 * Adds the sent msg.
	 *
	 * @param msg the msg
	 */
	public void addSentMsg(String msg){
		this.SentMessages.add(msg);
	}
	
	/**
	 * Send un send messages.
	 *
	 * @param User the user to send his all un received msgs
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void SendUnSendMessages(User user){
		for(int i=0;i<this.NeedToBeSendMessages.size();i++){
			user.writeToClient(this.NeedToBeSendMessages.get(i));
			this.SentMessages.add(this.NeedToBeSendMessages.remove(i));
		}
	}

}
