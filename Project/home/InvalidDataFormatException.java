/**
 * A derived class for invalid data format exceptions.
 *
 * @author: Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class InvalidDataFormatException extends Exception {

    public InvalidDataFormatException() {
        super("Invalid data format exception!");
    }

    public InvalidDataFormatException(String message) {
        super(message);
    }

}