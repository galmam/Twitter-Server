package protocol;
import reactor.ConnectionHandler;

public interface ServerProtocolFactory<T> {
   AsyncServerProtocol<T> create(ConnectionHandler<T> CH);
}
