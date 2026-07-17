package exception;

public class PacketParsingException extends ParserException {
    public PacketParsingException(String message) {
        super(message);
    }
    public PacketParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}