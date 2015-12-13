package TCPserver;

public class StompTwiterProtocolFactroy implements ServerProtocolFactory {

	public ServerProtocol create(ConnectionHandler CH) {
		return new TwiterOverStompProtocol(CH);
	}

}
