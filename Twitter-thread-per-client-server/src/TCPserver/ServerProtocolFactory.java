package TCPserver;

public interface ServerProtocolFactory {
	ServerProtocol create(ConnectionHandler CH);
}
