public class InvalidInputException extends Exception {

    public InvalidInputException() {
        super("Invalid input exception!");
    }

    public InvalidInputException(String message) {
        super(message);
    }

}