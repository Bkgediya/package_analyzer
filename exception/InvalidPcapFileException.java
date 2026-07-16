package exception;
public class InvalidPcapFileException extends Exception {
    public InvalidPcapFileException(String message) {
        super(message);
    }
}