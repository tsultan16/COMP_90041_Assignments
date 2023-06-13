public class Animal extends Character {

    private String species;
    private boolean isPet;

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

    //@Override
    public String toString() {
        String output = this.species;
        if(this.isPet) {
            output += " is pet";
        } 
        return output;
    }

}