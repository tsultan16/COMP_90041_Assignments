/**
 * A derived class for invalid characteristic value exceptions.
 *
 * @author: Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class InvalidCharacteristicException extends Exception {

    public InvalidCharacteristicException() {
        super("Invalid characteristic exception!");
    }

    public InvalidCharacteristicException(String message) {
        super(message);
    }

}
