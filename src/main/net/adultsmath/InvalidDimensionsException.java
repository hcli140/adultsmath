package main.net.adultsmath;

public class InvalidDimensionsException extends RuntimeException {
    public InvalidDimensionsException() {
        super();
    }

    public InvalidDimensionsException(String message) {
        super(message);
    }
}
