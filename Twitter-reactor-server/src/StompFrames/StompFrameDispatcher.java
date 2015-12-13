package StompFrames;




/**
 * The Interface StompFrameDispatcher.
 */
public interface StompFrameDispatcher {

	  /**
  	 * Process frame.
  	 *
  	 * @param CONNECT frame the frame which this methid need to process for the server
  	 * @return StompFrame -an answer to the frame being process by this method
  	 */
  	public StompFrame processFrame(CONNECT frame);
	  
  	/**
  	 * Process frame.
  	 *
  	 * @param SEND frame the frame which this methid need to process for the server
  	 * @return StompFrame -an answer to the frame being process by this method
  	 */
  	public StompFrame processFrame(SEND frame);
	  
  	/**
  	 * Process frame.
  	 *
  	 * @param ERROR frame the frame which this methid need to process for the server
  	 * @return StompFrame -an answer to the frame being process by this method
  	 */
  	public StompFrame processFrame(ERROR frame);
	  
  	/**
  	 * Process frame.
  	 *
  	 * @param SUBSCRIBE frame the frame which this methid need to process for the server
  	 * @return StompFrame -an answer to the frame being process by this method
  	 */
  	public StompFrame processFrame(SUBSCRIBE frame);
	  
  	/**
  	 * Process frame.
  	 *
  	 * @param DISCONNECT frame the frame which this methid need to process for the server
  	 * @return StompFrame -an answer to the frame being process by this method
  	 */
  	public StompFrame processFrame(DISCONNECT frame);
	  
  	/**
  	 * Process frame.
  	 *
  	 * @param UNSUBSCRIBE frame the frame which this methid need to process for the server
  	 * @return StompFrame -an answer to the frame being process by this method
  	 */
  	public StompFrame processFrame(UNSUBSCRIBE frame);



}
