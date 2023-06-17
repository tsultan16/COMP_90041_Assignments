/**
 * A base class for representing character objects.
 *
 * @author Tanzid Sultan
 */
public class Character {

    /* default gender constant */  
    public static final String DEFAULT_GENDER = "unknown";
    /* default age constant */  
    public static final int DEFAULT_AGE = 15;
    /* default body type constant */  
    public static final String DEFAULT_BODYTYPE = "unspecified";

    /**
    *  instance variables 
    */      
    private String gender;
    private int age;
    private String bodyType;


    /**
	*  Class default constructor (has exception handling)
    *
    * @param gender  character's gender
    * @param age  character's age
    * @param bodyType  character's body type
    * @param lineNum line number from scenarios file where the character was extracted from 
	*/ 
    public Character(String gender, int age, String bodyType, int lineNum) {        
        this.gender = this.validGender(gender, lineNum);
        this.age = this.validAge(age, lineNum);
        this.bodyType = this.validBodyType(bodyType, lineNum);
    }

   /**
	*  Overloaded class constructor without exception handling
    *
    * @param gender  character's gender
    * @param age  character's age
    * @param bodyType  character's body type
	*/     
    public Character(String gender, int age, String bodyType) {        
        this.gender = gender;
        this.age = age;
        this.bodyType = bodyType;
    }

    /**
	*  Overloaded class copy constructor 
    *
    * @param other  the character object to be copied 
	*/     
    public Character(Character other) {
        this.gender = other.getGender();
        this.age = other.getAge();
        this.bodyType = other.getBodyType();
    }

    /**
	 * Helper method for validating gender value 
	 *
     * @param gender  character's gender
     * @param lineNum line number from scenarios file where the location was extracted from 
	 */
    private String validGender(String gender, int lineNum) {
        String valid;    
        try {
            if ((gender.equals("male")) || (gender.equals("female")) || (gender.equals(DEFAULT_GENDER))) {
                valid = gender;
            } else {
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
            }
        } catch (InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
            valid = DEFAULT_GENDER;
        }
        return valid;
    }

    /**
	 * Helper method for validating age value 
	 *
     * @param age  character's age
     * @param lineNum line number from scenarios file where the location was extracted from 
	 */
    private int validAge(int age, int lineNum) {
        int valid;    
        try {
            if (age >= 0) {
                valid = age;
            } else {
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
            }
        } catch(InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
            valid = DEFAULT_AGE;
        }
        return valid;
    }

    /**
	 * Helper method for validating body type value 
	 *
     * @param bodyType  character's body type
     * @param lineNum line number from scenarios file where the location was extracted from 
	 */
    private String validBodyType(String bodyType, int lineNum) {
        String valid;    
        try {
            if ((bodyType.equals("average")) || (bodyType.equals("athletic")) || (bodyType.equals("overweight"))) {
                valid = bodyType;
            } else {
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
            }
        } catch (InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
            valid = DEFAULT_BODYTYPE;
        }
        return valid;
    }

    /**
	 * Accessor method for obtaining character's age
	 *
     * @return character's age
	 */    
    public int getAge() {
        return this.age;
    }

    /**
	 * Accessor method for obtaining character's gender
	 *
     * @return character's gender
	 */    
    public String getGender() {
        return this.gender;
    }

     /**
	 * Accessor method for obtaining character's body type
	 *
     * @return character's body type
	 */    
    public String getBodyType() {
        return this.bodyType;
    }

}