package Tokenizer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CharacterCodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Vector;
import java.util.logging.Logger;

import StompFrames.ERROR;
import StompFrames.StompFrame;
import StompFrames.StompFrameFactory;

public class StompFrameTokenizer implements MessageTokenizer<StompFrame> {


	private static final Logger logger = Logger.getLogger("Logger");

	private final String _messageSeparator="\0";//"^@"

	private final StringBuffer _stringBuf = new StringBuffer();
	/**
	 * the fifo queue, which holds data coming from the socket. Access to the
	 * queue is serialized, to ensure correct processing order.
	 */
	private final Vector<ByteBuffer> _buffers = new Vector<ByteBuffer>();

	private final CharsetDecoder _decoder;
	private final CharsetEncoder _encoder;

   public StompFrameTokenizer(Charset charset) {

      this._decoder = charset.newDecoder();
      this._encoder = charset.newEncoder();
   }

   /**
    * Add some bytes to the message.  
    * Bytes are converted to chars, and appended to the internal StringBuffer.
    * Complete messages can be retrieved using the nextMessage() method.
    *
    * @param bytes an array of bytes to be appended to the message.
    */
   public synchronized void addBytes(ByteBuffer bytes) {
	   _buffers.add(bytes);
      
   }

   /**
    * Is there a complete message ready?.
    * @return true the next call to nextMessage() will not return null, false otherwise.
    */
   public synchronized boolean hasMessage() {
	   while(_buffers.size() > 0) {
           ByteBuffer bytes = _buffers.remove(0);
           CharBuffer chars = CharBuffer.allocate(bytes.remaining());
 	      this._decoder.decode(bytes, chars, false); // false: more bytes may follow. Any unused bytes are kept in the decoder.
 	      chars.flip();
 	      this._stringBuf.append(chars);
	   }
	   return this._stringBuf.indexOf(this._messageSeparator) != -1;
   }

   /**
    * Get the next complete message if it exists, advancing the tokenizer to the next message.
    * @return the next complete message, and null if no complete message exist.
    */
   public synchronized StompFrame nextMessage() {
      String message = null;
      int messageEnd = this._stringBuf.indexOf(this._messageSeparator);
      if (messageEnd != -1) {
         message = this._stringBuf.substring(0, messageEnd);
         this._stringBuf.delete(0, messageEnd+this._messageSeparator.length()); 
         try {return createStompFrame(message);} catch (IOException e) {System.out.println("this should never happen");}
      }
      return null;
   }

   /**
    * Convert the String message into bytes representation, taking care of encoding and framing.
    *
    * @return a ByteBuffer with the message content converted to bytes, after framing information has been added.
    */
   public ByteBuffer getBytesForMessage(StompFrame msg)  throws CharacterCodingException {
      StringBuilder sb = new StringBuilder(msg.toString());
      ByteBuffer bb = this._encoder.encode(CharBuffer.wrap(sb));
      return bb;
   }
   
   private StompFrame createStompFrame(String msg) throws IOException{
	   BufferedReader reader = new BufferedReader(new StringReader(msg));
	   String line=reader.readLine();
	   StompFrame sf=null;
	   /** if the first line recevied was a recgonize stomp command we create an instance of this command frame
	    * else we send back to the client an ERROR frame.
	    * */
	   if (line!=null)  {sf=StompFrameFactory.Create(line);}
	   else {return new ERROR("The frame send was malformed","");}
	   if (sf.getAction().equals("ERROR"))	return sf;
	   boolean AllHeadersAdded=false;
	   StringBuilder body=new StringBuilder();
	   while ((line=reader.readLine())!=null) { 
			   if (!sf.isComplete()){
				   // if this condition is true then we are still receiving the headers of frame
				   if(!AllHeadersAdded && !line.isEmpty()) {headersAdder(line,sf);} 
				   else {
					   if (!AllHeadersAdded && line.isEmpty() ) {AllHeadersAdded=true;}
					   else {body.append(line);body.append("\n");}//end else if we did finish adding headers.
				   }//end else
			   }// end if frame isn't complete
	   } // end while of the buffer reader
	   sf.setBody(body.toString());
	   sf.Complete();		
	   logger.info("Finish processing the incoming message from the client with no errors");
	   return sf;
   }
      
   private StompFrame headersAdder(String headers,StompFrame sf){
	   try {
		   BufferedReader reader = new BufferedReader(new StringReader(headers));
		   String line="";
		   while((line=reader.readLine())!=null){
			   if (line.contains(":")) {
				   String[] parts = line.split(":"); 
				   String part1 = parts[0]; 
				   String part2 = parts[1];
				   sf.addHeaders(part1.trim(), part2.trim());
			   } 
			   else {
				   return new ERROR("Frame sent was Malformed","");
			   }
		   }
		   return null;		
	   } 
	   catch (IOException e) {return new ERROR("Frame sent was Malformed","");}
   }

}
