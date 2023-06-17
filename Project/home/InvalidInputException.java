/**
 * A derived class for invalid input exceptions.
 *
 * @author Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class InvalidInputException extends Exception {

    /*
     * Class constructor
     */
    public InvalidInputException() {
        super("Invalid input exception!");
    }

    /*
     * Overloaded class constructor
     */
    public InvalidInputException(String message) {
        super(message);
    }

}