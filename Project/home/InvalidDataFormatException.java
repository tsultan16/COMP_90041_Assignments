/**
 * A derived class for invalid data format exceptions.
 *
 * @author Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class InvalidDataFormatException extends Exception {

    /*
     * Class constructor
     */
    public InvalidDataFormatException() {
        super("Invalid data format exception!");
    }

    /*
     * Overloaded class constructor
     */
    public InvalidDataFormatException(String message) {
        super(message);
    }

}