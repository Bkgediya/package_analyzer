package exception;

public class InvalidPcapFileException extends ParserException {
    public InvalidPcapFileException(String message) {
        super(message);
    }
    public InvalidPcapFileException(String message, Throwable cause) {
        super(message, cause);
    }
}