public class Animal extends Character {

    private String species;
    private boolean isPet;

    // default constructor
    public Animal(String gender, int age, String bodyType, String species, String isPet, int lineNum) {
        super(gender, age, bodyType, lineNum);
        this.species = species;
        boolean ip = Boolean.parseBoolean(isPet);
        try{
            if((species.equals("cat")) || (species.equals("dog")) || (species.equals("ferret"))) {
                this.isPet = ip;
            } else {
                if(ip) {
                    // any species other than cat, dog or ferret cannot be pet'
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

    // constructor without exception handling
    public Animal(String gender, int age, String bodyType, String species, boolean isPet) {        
        super(gender, age, bodyType);
        this.species = species;
        this.isPet = isPet;
    }

    // copy constructor (assumes valid input values, so does not do exception handling)
    public Animal(Animal other) {
        super(other.getGender(), other.getAge(), other.getBodyType());
        this.species = other.getSpecies();
        this.isPet = other.getIsPet();
    }

    public String getSpecies () {
        return this.species;
    }

    public boolean getIsPet() {
        return this.isPet;
    }


    //@Override
    public String toString() {
        String output = "animal " + this.species;
        if(this.isPet) {
            output += " is pet";
        } 
        return output;
    }

}