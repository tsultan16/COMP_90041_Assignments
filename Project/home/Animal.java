/**
 * A derived class for representing animal character objects.
 *
 * @author Tanzid Sultan
 */
public class Animal extends Character {

    /**
    *  instance variables 
    */   
    private String species;
    private boolean isPet;

    /**
	*  Class default constructor (has exception handling)
    *
    * @param gender  character's gender
    * @param age  character's age
    * @param bodyType  character's body type    
    * @param species  animal species   
    * @param isPet  character's pet status   
    * @param lineNum line number from scenarios file where the character was extracted from 
	*/    
    public Animal(String gender, int age, String bodyType, String species, String isPet, int lineNum) {
        super(gender, age, bodyType, lineNum);
        this.species = species;
        boolean ip = Boolean.parseBoolean(isPet);
        try{
            if ((species.equals("cat")) || (species.equals("dog")) || (species.equals("ferret"))) {
                this.isPet = ip;
            } else {
                if (ip) {
                    // any species other than cat, dog or ferret cannot be pet and throws invalid characteristic exception
                    throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                } else {
                    this.isPet = ip;
                }
            }

        } catch(InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
            this.isPet = false;
        }
    }

    /**
	*  Overloaded class constructor without exception handling
    *
    * @param gender  character's gender
    * @param age  character's age
    * @param bodyType  character's body type    
    * @param species  animal species   
    * @param isPet  character's pet status   
	*/        
    public Animal(String gender, int age, String bodyType, String species, boolean isPet) {        
        super(gender, age, bodyType);
        this.species = species;
        this.isPet = isPet;
    }

    /**
	*  Overloaded class copy constructor 
    *
    * @param other  the Animal object to be copied 
	*/          
    public Animal(Animal other) {
        super(other.getGender(), other.getAge(), other.getBodyType());
        this.species = other.getSpecies();
        this.isPet = other.getIsPet();
    }

    /**
	 * Accessor method for obtaining the animal's species
	 *
     * @return species
	 */  
    public String getSpecies () {
        return this.species;
    }

    /**
	 * Accessor method for obtaining the animal's pet status
	 *
     * @return pet status
	 */  
    public boolean getIsPet() {
        return this.isPet;
    }


    /**
	 * Overridden toString method
     * 
     * @return  string containing all relevant information about the Animal object
	 */    
    public String toString() {
        String output = "animal " + this.species;
        if (this.isPet) {
            output += " pet";
        } 
        return output;
    }

}