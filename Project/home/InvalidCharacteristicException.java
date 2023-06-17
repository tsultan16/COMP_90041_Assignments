/**
 * A derived class for invalid characteristic value exceptions.
 *
 * @author Tanzid Sultan
 */
public class InvalidCharacteristicException extends Exception {

    /*
     * Class constructor
     */
    public InvalidCharacteristicException() {
        super("Invalid characteristic exception!");
    }

    /*
     * Overloaded class constructor
     */
    public InvalidCharacteristicException(String message) {
        super(message);
    }

}
