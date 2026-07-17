package exception;

public class TcpParsingException extends PacketParsingException {
    public TcpParsingException(String message) {
        super(message);
    }
    public TcpParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
