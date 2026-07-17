package exception;

public class UdpParsingException extends PacketParsingException {
    public UdpParsingException(String message) {
        super(message);
    }
    public UdpParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
