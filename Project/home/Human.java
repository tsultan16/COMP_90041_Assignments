/**
 * A derived class for representing human character objects.
 *
 * @author: Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class Human extends Character {

    /**
    *  constants 
    */  
    public static final String DEFAULT_PROFESSION = "none";

    /**
    *  instance variables 
    */   
    private String profession;
    private boolean pregnant;
    private String ageCategory;

    /**
	*  Class default constructor (has exception handling)
    *
    * @param gender  character's gender
    * @param age  character's age
    * @param bodyType  character's body type    
    * @param profession  character's profession    
    * @param pregnant  character's pregnancy status    
    * @param lineNum line number from scenarios file where the character was extracted from 
	*/ 
    public Human(String gender, int age, String bodyType, String profession, String pregnant, int lineNum) {       
        super(gender, age, bodyType, lineNum);
        this.ageCategory = this.categorizeAge(this.getAge());
        
        // check that only adults have non-default professions
        if (!this.ageCategory.equals("adult")){
            // default profession for non-adults
            try {
                if (!profession.equals(DEFAULT_PROFESSION)) {
                     throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                } else {            
                    this.profession = profession;
                }
            } catch (InvalidCharacteristicException e) {
                System.out.println(e.getMessage());
                this.profession = DEFAULT_PROFESSION;
            }
        } else {
            this.profession = profession; //this.validProfession(profession, lineNum);
        }
        
        // check that only adult females are pregnant
        if (this.getGender().equals("male")) {
            try {
                if (pregnant.equals("true")) {
                    throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                } else {
                    this.pregnant = false;
                }
            } catch (InvalidCharacteristicException e) {
                System.out.println(e.getMessage());
                this.pregnant = false;
            }
        } else {
            if (!this.ageCategory.equals("adult")){
                try {
                    if (pregnant.equals("true")) {
                        throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                    } else {
                        this.pregnant = false;
                    }
                } catch (InvalidCharacteristicException e) {
                    System.out.println(e.getMessage());
                    this.pregnant = false;
                }
            } else {
                // only adult females can be pregnant
                this.pregnant = Boolean.parseBoolean(pregnant);
            }    
        }
    }

   /**
	*  @Overload Class constructor without exception handling
    *
    * @param gender  character's gender
    * @param age  character's age
    * @param bodyType  character's body type    
    * @param profession  character's profession    
    * @param pregnant  character's pregnancy status    
	*/     
    public Human(String gender, int age, String bodyType, String profession, boolean pregnant) {        
        super(gender, age, bodyType);
        this.ageCategory = this.categorizeAge(age);
        this.profession = profession;
        this.pregnant = pregnant;
    }

    /**
	*  @Overload Class copy constructor 
    *
    * @param other  the Human object to be copied 
	*/         
    public Human(Human other) {
        super(other.getGender(), other.getAge(), other.getBodyType());
        this.ageCategory = this.categorizeAge(other.getAge());
        this.profession = other.getProfession();
        this.pregnant = other.getPregnant();
    }

    /**
	 * Accessor method for obtaining character's age category
	 *
     * @return age category
	 */   
    public String getAgeCategory() {
        return this.ageCategory;
    }

    /**
	 * Accessor method for obtaining character's profession
	 *
     * @return profession
	 */   
    public String getProfession() {
        return this.profession;
    }

    /**
	 * Accessor method for obtaining character's pregnancy status
	 *
     * @return pregnancy status
	 */   
    public boolean getPregnant() {
        return this.pregnant;
    }

    /**
	 * Helper method for determining the age category based on character's age.
     * 
     * @param  age chcracter's age
     * @return  age category
	 */
    private String categorizeAge(int age) {
        String category;
        if ((age >= 0) && (age <= 4)) {
            category = "baby";
        } else if ((age >= 5) && (age<= 16)) {
            category = "child";
        } else if((age >= 17) && (age <= 68)) {
            category = "adult";
        } else {
            category = "senior";
        }
        return category;
    }
 
    /**
	 * @Override 
     * 
     * @return  string containing all relevant information about the Human object
	 */
    public String toString() {
        String output = "human " + this.getBodyType() + " " + this.ageCategory + " ";
        if (this.ageCategory.equals("adult")) {
            output += this.profession + " ";
        } 
        output += this.getGender();
        if (this.pregnant) {
           output += " pregnant"; 
        }
        return output;
    }

}
