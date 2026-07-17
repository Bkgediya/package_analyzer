package exception;

public class IPv4ParsingException extends PacketParsingException {
    public IPv4ParsingException(String message) {
        super(message);
    }
    public IPv4ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
