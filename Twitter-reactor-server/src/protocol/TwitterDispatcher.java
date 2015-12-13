package protocol;

import StompFrames.*;

public class TwitterDispatcher implements StompFrameDispatcher {
		
	private TwiterOverStompProtocol protocol;
	
	public TwitterDispatcher (TwiterOverStompProtocol protocol){
		this.protocol=protocol;
	}

	public StompFrame processFrame(CONNECT frame) {
		return this.protocol.Processframe(frame);
	}


	public StompFrame processFrame(SEND frame) {
		return this.protocol.Processframe(frame);
	}


	public StompFrame processFrame(ERROR frame) {
		return this.protocol.Processframe(frame);
	}


	public StompFrame processFrame(SUBSCRIBE frame) {
		return this.protocol.Processframe(frame);
	}


	public StompFrame processFrame(DISCONNECT frame) {
		return this.protocol.Processframe(frame);
	}


	public StompFrame processFrame(UNSUBSCRIBE frame) {
		return this.protocol.Processframe(frame);
	}

}
