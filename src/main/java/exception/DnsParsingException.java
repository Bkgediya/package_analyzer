package exception;

public class DnsParsingException extends PacketParsingException {
    public DnsParsingException(String message) {
        super(message);
    }
    public DnsParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
