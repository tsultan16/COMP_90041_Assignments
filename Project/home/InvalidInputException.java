/**
 * A derived class for invalid input exceptions.
 *
 * @author: Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class InvalidInputException extends Exception {

    public InvalidInputException() {
        super("Invalid input exception!");
    }

    public InvalidInputException(String message) {
        super(message);
    }

}