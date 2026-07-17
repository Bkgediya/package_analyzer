package exception;

public class EthernetParsingException extends PacketParsingException {
    public EthernetParsingException(String message) {
        super(message);
    }
    public EthernetParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
