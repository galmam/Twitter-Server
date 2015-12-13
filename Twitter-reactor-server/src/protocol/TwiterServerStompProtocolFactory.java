package protocol;

import StompFrames.*;
import reactor.ConnectionHandler;

public class TwiterServerStompProtocolFactory implements ServerProtocolFactory<StompFrame> {

	@Override
	public TwiterOverStompProtocol create(ConnectionHandler<StompFrame> CH) {
		return new TwiterOverStompProtocol(CH);
	}
}
