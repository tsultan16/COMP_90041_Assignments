import java.util.ArrayList;

/**
 * A class for representing location objects.
 *
 * @author Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class Location {

    /* default latitude constant */  
    public static final String DEFAULT_LATITUDE = "10.0000 N";
    /* default longitude constant */  
    public static final String DEFAULT_LONGITUDE = "10.0000 E";

    /**
    *  instance variables 
    */  
    private String latitude;
    private String longitude;
    private boolean trespassing;
    private ArrayList<Character> characters;

    /**
	*  Class default constructor (has exception handling)
    *
    * @param latitude  location latitude
    * @param longitude  location longitude
    * @param tp trespassing status 
    * @param lineNum line number from scenarios file where the location was extracted from 
	*/   
    public Location(String latitude, String longitude, String tp, int lineNum){
        this.latitude = this.validLatitude(latitude, lineNum);
        this.longitude = this.validLongitude(longitude, lineNum);
        if (tp.equals("legal")) {
            this.trespassing = false;
        } else {
            this.trespassing = true;
        }
        this.characters = new ArrayList<Character>(0);
        
    }

    /**
	* Overloaded class constructor without exception handling
    *
    * @param latitude  location latitude
    * @param longitude  location longitude
    * @param tp trespassing status 
	*/       
    public Location(String latitude, String longitude, String tp){
        this.latitude = latitude;
        this.longitude = longitude;
        if (tp.equals("legal")) {
            this.trespassing = false;
        } else {
            this.trespassing = true;
        }
        this.characters = new ArrayList<Character>(0);   
    }

    /**
	*  Overloaded class copy constructor 
    *
    * @param other  the location object to be copied 
	*/ 
    public Location(Location other) {
        this.latitude = other.getLatitude();
        this.longitude = other.getLongitude();
        this.trespassing = other.getTrespassingBool();
        this.characters = other.getCharacters();
    }    

    /**
	 * Mutator method for adding a new character to this location
     * 
	 * @param newChar the new Character object 
	 */
    public void addCharacter(Character newChar) {
        this.characters.add(newChar);
    }

    /**
	 * Accessor method for obtaining total number of characters within the location 
	 *
     * @return number of characters
	 */
    public int getNumCharacters() {
        return this.characters.size();
    }

    /**
	 * Accessor method for obtaining location latitude 
	 *
     * @return location latitude
	 */
    public String getLatitude() {
        return this.latitude;
    }

    /**
	 * Accessor method for obtaining location longitude 
	 *
     * @return location longitude
	 */    
    public String getLongitude() {
        return this.longitude;
    }

    /**
	 * Accessor method for obtaining location tresspassing status 
	 *
     * @return tresspassing status boolean
	 */
    public boolean getTrespassingBool () { 
        return this.trespassing;
    }

    /**
	 * Accessor method for obtaining location tresspassing status 
	 *
     * @return tresspassing status string
	 */
    public String getTrespassing() {
        String tresp;
        if (this.trespassing) {
            tresp = "yes";
        } else {
            tresp = "no";
        }
        return tresp;
    }

    /**
	 * Accessor method for obtaining all charactres within the location 
	 *
     * @return array list of Character objects
	 */
    public ArrayList<Character> getCharacters() {
        // return a deep copy of the characters array list
        ArrayList<Character> charactersCopy = new ArrayList<Character>(0);
        for (Character c: this.characters) {
            charactersCopy.add(c);
        }
        return charactersCopy;
    }

    /**
	 * Helper method for validating latitude value 
	 *
     * @param latitude  location latitude
     * @param lineNum line number from scenarios file where the location was extracted from 
     * @return valid latitude value (returns default value for invalid input)
	 */
    private String validLatitude(String latitude, int lineNum) {
        String valid;
        String[] sp = latitude.split(" ");    
        try {
            // make sure latitude value contains both a degree and a N/S identifier
            if (sp.length == 2) {
                // make sure the degree value is numeric
                try {
                    double degree = Double.parseDouble(sp[0]);
                } catch (NumberFormatException e) {
                    throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                }
                // make sure the degree identifier is either 'N' or 'S'
                if ((sp[1].equals("N")) || (sp[1].equals("S"))) {
                    valid = latitude;
                } else {
                    throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                }
            } else {
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
            }
        } catch(InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
            valid= DEFAULT_LATITUDE;
        }
        return valid;
    }

    /**
	 * Helper method for validating longitude value 
	 *
     * @param latitude  location longitude
     * @param lineNum line number from scenarios file where the location was extracted from 
     * @return valid longitude value (returns default value for invalid input)
	 */    
    private String validLongitude(String longitude, int lineNum) {
        String valid;
        String[] sp = longitude.split(" ");    
        try {
            // make sure longitude value contains both a degree and an E/W identifier
            if (sp.length == 2) {
                // make sure the degree value is numeric 
                try {
                    double degree = Double.parseDouble(sp[0]);
                } catch (NumberFormatException e) {
                    throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                }
                // make sure the degree identifier is either 'E' or 'W'
                if ((sp[1].equals("E")) || (sp[1].equals("W"))) {
                    valid = longitude;
                } else {
                    throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                }
            } else {
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
            }
        } catch (InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
            valid = DEFAULT_LONGITUDE;
        }
        return valid;
    }

}